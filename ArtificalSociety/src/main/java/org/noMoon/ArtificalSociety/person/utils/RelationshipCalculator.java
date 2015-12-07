package org.noMoon.ArtificalSociety.person.utils;

import org.noMoon.ArtificalSociety.commons.utils.Configuration;
import org.noMoon.ArtificalSociety.commons.utils.Distribution;
import org.noMoon.ArtificalSociety.commons.utils.DistributionParser;
import org.noMoon.ArtificalSociety.commons.utils.ValidationTools;
import org.noMoon.ArtificalSociety.group.DTO.GroupDTO;
import org.noMoon.ArtificalSociety.group.Services.GroupService;
import org.noMoon.ArtificalSociety.history.DTO.HometownHistoryDTO;
import org.noMoon.ArtificalSociety.history.services.HistoryService;
import org.noMoon.ArtificalSociety.person.DAO.PersonMapper;
import org.noMoon.ArtificalSociety.person.DTO.PersonDTO;
import org.noMoon.ArtificalSociety.person.Enums.RelationStatusEnum;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class RelationshipCalculator {

    private static HistoryService historyService;
    private static GroupService groupService;
    private static PersonMapper personMapper;
	

	// Factors.
	public final static int REL_STRENGTH_TYPE = 0;
	public final static int REL_STRENGTH_CHILDREN = 1;
	public final static int REL_STRENGTH_RACE = 2;
	public final static int REL_STRENGTH_RELIGION = 3;
	public final static int REL_STRENGTH_YEARSMARRIED = 4;
	public final static int REL_STRENGTH_EDUCATION = 5;
	public final static int REL_STRENGTH_RANDOM = 6;
	public final static int REL_STRENGTH_INCOME = 7;
	public final static int REL_STRENGTH_INTEREST = 8;
	public final static int REL_STRENGTH_LOCATION = 9;
	
	
	
	// ------------------------------------------------------------------------------------------------------
	// INTEREST SIMILARITY.
	// ------------------------------------------------------------------------------------------------------
	
	public static void calculateAndSetInterestSimilarity (PersonDTO personA, PersonDTO personB) {
		
		double IS_AB = calculateInterestSimilarity(personA, personB);
		double IS_BA = calculateInterestSimilarity(personB, personA);
		
		//System.out.println("IS_AB = " + IS_AB + " and IS_BA = " + IS_BA + "       {" + personA.getID() + "," + personB.getID() + "}.");
		
		// Set each person's interest similarity.
		personA.setInterestSimilarity(IS_AB);
		personB.setInterestSimilarity(IS_BA);
		
		
	} // end calculateAndSetInterestSimilarity()
	
	private static double calculateInterestSimilarity (PersonDTO personA, PersonDTO personB) {
		// Calculate the IS (interest similarity) between two persons, as per Eq. 1 in
		// 'Modeling Adaptive Behaviors on Growing Social Networks'.
		//
		// param personA: the first agent from which the comparison will take place
		// param personB: the second agent, who will be compared to personA
		// output: calculated IS value

		double IS;
		
		int i;
		
		double tmp;
		double i_diff;
		double w_sqr;
		
		double sumNumerator = 0.0;
		double sumDenominator = 0.0;

		double[] personAWeights = personA.getInterestWeight();
		double[] personAInterests = personA.getInterest();
		double[] personBInterests = personB.getInterest();

		int numInterests = personAWeights.length;

		for (i = 0; i < numInterests; i++) {

			// Pre-compute because this is used both in the numerator and denominator.
			w_sqr = personAWeights[i] * personAWeights[i];

			// Perform summation for numerator of equation.
			i_diff = personAInterests[i] - personBInterests[i];
			tmp = w_sqr * (i_diff * i_diff);
			sumNumerator += tmp;
			
			// Perform summation for denominator of equation.
			sumDenominator += w_sqr;
		} // end for i (loop through interests) 
		
		//System.out.println(sumNumerator + " / " + sumDenominator);
		
		IS = 1.0 - Math.sqrt(sumNumerator / sumDenominator);
		
		return IS;
	} // end calculateInterestSimilarity()


    public static void CalculateAndSetRelationshipStrength (PersonDTO personA, PersonDTO personB, int pass) {

        double strength;
        if (pass == 0) {
            strength = CalculateRelationshipStrength(personA, personB, Configuration.RelStrengthWeights_PreChildren, pass);
        } else {
            strength = CalculateRelationshipStrength(personA, personB, Configuration.RelStrengthWeights_PostChildren, pass);
        } // end if (check which pass for calculating relationship strength)

        personA.setRelationshipStrength(strength);
        personB.setRelationshipStrength(strength);
        //System.out.println("setting rel strength for " + personA.getID() + " and " + personB.getID() + " to " + strength);

    } // end CalculateAndSetRelationshipStrength()

    private static double CalculateRelationshipStrength (PersonDTO personA, PersonDTO personB, double[] factorWeights, int pass) {
        // Calculate the strength of the relationship between the two people.
        // param personA: one person in the relationship
        // param personB: the other person in the relationship
        // param pass: the number indicating whether this is before or after childbirth (0 = before children; 1 (or anything else) = after children)

        double strength = 0.0;


        strength += (factorWeights[REL_STRENGTH_TYPE] * calcRelStrength_Type(personA, personB));
        strength += (factorWeights[REL_STRENGTH_CHILDREN] * calcRelStrength_Children(personA, personB));
        strength += (factorWeights[REL_STRENGTH_RACE] * calcRelStrength_Race(personA, personB));
        strength += (factorWeights[REL_STRENGTH_RELIGION] * calcRelStrength_Religion(personA, personB));
        strength += (factorWeights[REL_STRENGTH_YEARSMARRIED] * calcRelStrength_YearsMarried(personA, personB));
        strength += (factorWeights[REL_STRENGTH_EDUCATION] * calcRelStrength_Education(personA, personB));
        strength += (factorWeights[REL_STRENGTH_RANDOM] * calcRelStrength_Random(personA, personB));
        strength += (factorWeights[REL_STRENGTH_INCOME] * calcRelStrength_Income(personA, personB));
        strength += (factorWeights[REL_STRENGTH_INTEREST] * calcRelStrength_Interest(personA, personB));
        strength += (factorWeights[REL_STRENGTH_LOCATION] * calcRelStrength_Location(personA, personB));

        // Factors to include:
        //	- Relationship Type
        //	- Have children? How many?
        //	- How long together (??)
        //	- MBTI traits (?)
        //	- I.S. based on array of interests
        //	- Race, religion, etc.
        //	- Random factor

        // Clip relationship to strength to be in [0.01, 0.99].
        strength = ValidationTools.clipValueToRange(strength, 0.01, 0.99);
        return strength;
    } // end CalculateRelationshipStrength()

    private static double calcRelStrength_Type (PersonDTO personA, PersonDTO personB) {
        // Return the strength factor from the relationship type. For now, these are just two fixed numbers and the
        // value for a dating relationship is a little less than that of a married relationship.
        if (personA.getRelationshipStatus().equals(RelationStatusEnum.MARRIED)) {
            return 1.0;
        } else if (personA.getRelationshipStatus().equals(RelationStatusEnum.DATING)) {
            return 0.8;
        } else {
        } // end if (determine relationship type)

        return 0.0;
    } // end calcRelStrength_Type()

    private static double calcRelStrength_Children (PersonDTO personA, PersonDTO personB) {
        // Return the strength factor from the couple's children. In the current implementation, there is no possibility
        // of step-children from past relationships, but in a future work, that would be a consideration, so that's
        // why here we get the list of children from both personA and personB.
        List<String> personAChildren = personA.getChildrenIds();
        List<String> personBChildren = personB.getChildrenIds();

        if (personAChildren.isEmpty() || personBChildren.isEmpty()) {
            // No children.
            return 1.0;
        } // end if (check if either person has no children) - MAY HAVE TO BE MODIFIED

        return 0.0;
    } // end calcRelStrength_Children()

    private static double calcRelStrength_Race (PersonDTO personA, PersonDTO personB) {
        // Determine whether or not the couple are the same race. If they are the same, then return 1.0, otherwise return 0.0. At this point, there is no need for
        // an in-between or fuzzy area.

        if (personA.getRaceIndex() == personB.getRaceIndex()) {
            return 1.0;
        } else {
            return 0.0;
        } // end if (checking if two people are same race)
    } // end calcRelStrength_Race()

    private static double calcRelStrength_Religion (PersonDTO personA, PersonDTO personB) {
        // Determine whether or not the couple are the same religion. If they are the same, then return 1.0, otherwise return 0.0. At this point, there is no need for
        // an in-between or fuzzy area.

        if (personA.getReligionIndex() == personB.getReligionIndex()) {
            return 1.0;
        } else {
            return 0.0;
        } // end if (checking if two people are same religion)
    } // end calcRelStrength_Religion()

    private static double calcRelStrength_YearsMarried (PersonDTO personA, PersonDTO personB) {
        // Determine how long the couple has been together.

        if (!personA.getRelationshipStartYear().equals( personB.getRelationshipStartYear())) {
//            System.out.println("personA: "+String.valueOf(personA.getRelationshipStartYear())+" PersonB: "+String.valueOf(personB.getRelationshipStartYear()));
            System.err.println("In RelationshipCalculator->calcRelStrength_YearsMarried(); the two people have different relationship start years even though they are in a relationship together. Fix this error!");
            //System.err.println("In RelationshipCalculator->calcRelStrength_YearsMarried(); the two people have different relationship start years (" + personA.getRelationshipStartYear() + "," +
            //personB.getRelationshipStartYear() + ") even though they are in a relationship together. Fix this error!");
            return 0.0;
        } // end if (ensure the couple has the same relationship start year)

        // personA and personB should have the same relationship start year. Now that we ensure this, calculate how long the two have been together.
        int numYearsTogether = Configuration.SocietyYear - personA.getRelationshipStartYear();

        // Calculate the relationship length strength as a function of the number of years they have been together.
        double minYearsSuccessfulRelationship = 25;
        double numYearsStrength = (double)numYearsTogether / minYearsSuccessfulRelationship;

        //System.out.println(personA.relationshipStartYear + " " + personB.relationshipStartYear + " | " + numYearsTogether + " || " + numYearsStrength);

        // Ensure the probability is within [0,1] even if calculation yielded a greater number.
        return ValidationTools.clipValueToRange(numYearsStrength, 0.0, 1.0);
    } // end calcRelStrength_YearsMarried()

    private static double calcRelStrength_Education (PersonDTO personA, PersonDTO personB) {
        // Determine whether or not the two people have the same education level as one another. This is currently checked as a simple equality check, so University (U)
        // and College (C) count as different education levels and thus will return 0.0. This could be modified to allow those to be equal or similar.

        if (personA.getEducation().equals(personB.getEducation())) {
            return 1.0;
        } else {
            return 0.0;
        } // end if (checking if two people have the same education)
    } // end calcRelStrength_Education()

    private static double calcRelStrength_Random (PersonDTO personA, PersonDTO personB) {
        // Add a random element into the relationship strength, since no relationship is completely mathematically computable with a formula.
        return Distribution.uniform(0.0, 1.0);
    } // end calcRelStrength_Random()

    private static double calcRelStrength_Income (PersonDTO personA, PersonDTO personB) {
        // Determine whether or not the two people are in the same socio-economic class as one another.

        //System.out.print(personA.getIncome() + " | " + personB.getIncome());
        //System.out.print("\t\t" + personA.getCareerTitle() + " | " + personB.getCareerTitle() + " === ");

        // If two persons' income is close to the other (within the threshold), then return 1, otherwise return 0.
        if (Math.abs(personA.getIncome() - personB.getIncome()) < Configuration.IncomeClassThreshold) {
            //System.out.println(personA.getCareerTitle() + "(" + personA.getIncome() + ") | " + personB.getCareerTitle() + "(" + personB.getIncome() + ") === 1.0");
            //System.out.println("1.0");
            return 1.0;
        } else {
            //System.out.println(personA.getCareerTitle() + "(" + personA.getIncome() + ") | " + personB.getCareerTitle() + "(" + personB.getIncome() + ") === 0.0");
            //System.out.println("0.0");
            return 0.0;
        } // end if (checking if two people are similar in income level)
    } // end calcRelStrength_Income()

    private static double calcRelStrength_Interest (PersonDTO personA, PersonDTO personB) {
        // Determine how similar the people are in their interests/values.

        double min_IS_threshold = -15.0; // THis is just an imposed threshold so values below this are cut off.
        double max_IS_threshold = 1.0; // Maximum possible value is 1.

        if (personA.getInterestSimilarity() < min_IS_threshold || personB.getInterestSimilarity() < min_IS_threshold) {
            // If either person's I.S. is below the cut-off, then return 0.
            return 0.0;
        } // end if (check if either person has too low an IS)

        // Take the mean I.S. from the two people.
        double meanIS = (personA.getInterestSimilarity()+personB.getInterestSimilarity()) / 2.0;

        // Scale the mean I.S. from [min_IS_threshold, max_IS_threshold] to [0, 1].
        double oldRange = max_IS_threshold - min_IS_threshold;
        double newRange = 1.0 - 0.0;
        double scaledIS = (((meanIS-min_IS_threshold) * newRange) / oldRange) + 0.0;

        // Return the scaled I.S. measure on the interval [0,1].
        return scaledIS;
    } // end calcRelStrength_Interest()


    private static double calcRelStrength_Location (PersonDTO personA, PersonDTO personB) {
        // Determine the strength associated to the geographical location of the two persons.
        HometownHistoryDTO personAHistory=historyService.getHometownHistoryById(personA.getHometownHistoryId());
        HometownHistoryDTO personBHistory=historyService.getHometownHistoryById(personB.getHometownHistoryId());

        String personALocation = personAHistory.getRecordList().get(personAHistory.getRecordList().size()-1).getLocation();
        String personBLocation = personBHistory.getRecordList().get(personBHistory.getRecordList().size()-1).getLocation();

        // If two people live in same city, then return 1.0, otherwise return 0.0.
        if (personALocation.equals(personBLocation)) {
            return 1.0;
        } else {
            return 0.0;
        } // end if (check if two people live in same society)

    } // end calcRelStrength_Location()

    public static int DetermineNumberOfChildren (PersonDTO personA, PersonDTO personB) {
        // Determine how many children the couple has had, that are being created now in back-filling the population.
        // This will look at relationship strength, status, among other factors to decide the number of children they had.
        //
        // param parentA: the first person in the relationship
        // param parentB: the second person in the relationship
        //
        // returns: integer representing the number of children that couple has had.

        //double strength = CalculateRelationshipStrength(personA, personB);

        int numChildren;

        double rndChildren = Distribution.uniform(0.0, 1.0);

        if (rndChildren < Configuration.p_likelihoodMarriedCoupleHasChildren) {
            // (EDIT for below line: Not true anymore.. At this point there is not necessarily going to be children. Could select zero here.
            // There will be children. Now determine the number of children.


            // TODO Incorporate relationship strength and/or other factors into number of children equation.
            // Get relationship strength (should be identical for both persons, so just get it from personA for simplicity).
//            double detNumChildrenFactor_RelStrength = personA.getRelationshipStrength();
            //System.out.println(personB.getRelationshipStrength());


            numChildren = ValidationTools.clipValueToRange((int)Math.round(DistributionParser.parseStatisticalDistribution(Configuration.marriageChildrenDistr)), Configuration.marriageChildrenMin, Configuration.marriageChildrenMax);
            //numChildren = Distribution.uniform(0, 1);

            // TODO THIS SHOULD BE IN A CHILDREN FACTOR METHOD, ALONG WITH THE OTHER FACTORS!!!
            if ((Configuration.SocietyYear - personA.getRelationshipStartYear()) < Configuration.MinNumYearsMarriedBeforeChildren) {
                numChildren = 0;
            } // end if (check if couple's marriage began too recently for them to want to have a child)

        } else {
            // There will not be any children.
            numChildren = 0;

        } // end if (whether or not there will be children, from random number)

        //System.out.println("numChildren = " + numChildren);

        return numChildren;

    } // end DetermineNumberOfChildren()

    public static int DetermineCurrentChildBirth (PersonDTO personA, PersonDTO personB) {
        // Determine whether or not a couple will opt to have a child right now, during an active simulation.
        // This will look at relationship strength, number of current children, and time since last child was born, among
        // other factors to decide whether or not they will have a child at this time.
        //
        // param parentA: the first person in the relationship
        // param parentB: the second person in the relationship
        //
        // returns: integer of either 0 (no child right now) or 1 (having a child right now).
        // 		{If twins, triplets, etc. were an option, then here we could return 2, 3, etc. as well, but for now we
        //		will leave it at single children}.

        // TODO!!!

        int numChildren;

        double rndChildren = Distribution.uniform(0.0, 1.0);

        if (rndChildren < Configuration.p_likelihoodMarriedCoupleHasChildren) {
            // There will be children. Now determine the number of children.

            // TODO Incorporate relationship strength and/or other factors into number of children equation.
            // Get relationship strength (should be identical for both persons, so just get it from personA for simplicity).
            double detNumChildrenFactor_RelStrength = personA.getRelationshipStrength();
            //System.out.println(personB.getRelationshipStrength());


            // AGE.
            if (personA.getAge() > Configuration.MaxAgeHaveChildren || personB.getAge() > Configuration.MaxAgeHaveChildren) {
                // If either, or both, of the parents are too old, then they will not have children.
                //System.out.println("0 kids | A");
                numChildren = 0;
            } else {	// Both parents are young enough to have children.
                int expNumChildren = ValidationTools.clipValueToRange((int)Math.round(DistributionParser.parseStatisticalDistribution(Configuration.marriageChildrenDistr)), Configuration.marriageChildrenMin, Configuration.marriageChildrenMax);
                int numChildrenAlready = personA.getChildrenIds().size();

                if (expNumChildren > numChildrenAlready) {
                    // If the couple hasn't yet had as many kids as they expect to have, then let them have one now.
                    numChildren = 1;
                } else {
                    // If they have already had as many children as they expect, then don't have any more!
                    //System.out.println("0 kids | B " + expNumChildren + " <> " + numChildrenAlready);
                    numChildren = 0;
                } // end if (check if couple has already had as many children as they currently expect to have)
            } // end if (check if parents are too old for having children)

        } else {
            // There will not be any children.
            //System.out.println("0 kids | C");
            numChildren = 0;

        } // end if (whether or not there will be children, from random number)


        return numChildren;

    } // end DetermineCurrentChildBirth()

    public static void SearchForPartner (PersonDTO person) {
        // This method will search for a compatible match for the given Person.
        //
        // param person: the given Person for whom to find a compatible partner
        //
        // ????????????????????????
        Set<String> potentialPartnerIdSet=new HashSet<String>();
        for(Long groupId:person.getGroupIds().keySet()){
            GroupDTO group=groupService.getGroupDTOById(groupId);
            potentialPartnerIdSet.addAll(group.getMemberList());
        }

        // If there are no potential friends, then person has no potential partner, so leave function immediately.
        if (potentialPartnerIdSet.size() == 0) {
            return;
        } // end if (check if there are no potential friends->partners)


        double maxCompatibility=-100000;
        String maxId="";

        for (String partnerId:potentialPartnerIdSet) {

            PersonDTO possPartner = new PersonDTO(personMapper.selectById(partnerId));


            double compatibilityValue = RelationshipCalculator.CalculateCompatibility(person, possPartner);
            if(compatibilityValue>maxCompatibility){
                maxCompatibility=compatibilityValue;
                maxId=partnerId;
            }
            //System.out.println(possPartner.getID() + " {" + i + "}  " + compatibilityValues[i]);

        } // end for i (loop through all potential friends to search for a potential partner)


        PersonDTO mostCompatiblePerson = null;
        double THRESHOLD = -10.0;
        if (maxCompatibility > THRESHOLD) {
            mostCompatiblePerson = new PersonDTO(personMapper.selectById(maxId));
        } // end if (ensure that compatibility reaches a valid threshold)




        double p_formRelationshipWithCompatiblePartner = 0.7;
        double rndFormRelationship = Distribution.uniform(0.0, 1.0);

        // Check opposite (if NOT going to match up).
        if (rndFormRelationship > p_formRelationshipWithCompatiblePartner) {
            return;
        } // end if (check if couple will NOT get together)


        // If a match was found, then create the dating relationship between the two.
        if (mostCompatiblePerson != null) {

            CreateRelationshipInSimulation(person, mostCompatiblePerson, RelationStatusEnum.DATING);
        } // end if (check if a match was found for the person)


    } // end SearchForPartner()

    public static void CreateRelationshipInSimulation (PersonDTO personA, PersonDTO personB, RelationStatusEnum relType) {
        // Initialize a couple's relationship from a live simulation, by setting their relationship start year as the
        // current year in the simulation, and by then creating the relationship between them.
        //
        // param personA: one of the people in the new relationship
        // param personB: the other person in the new relationship
        // param relType: the integer indicating the relationship type (dating or married)

        personA.setRelationshipStartYear( Configuration.SocietyYear );
        personB.setRelationshipStartYear( Configuration.SocietyYear );

        GroupAdder.createRelationship(personA, personB, relType);
        //System.out.println("Relationship statuseseses: " + personA.getRelationshipStatus() + " && " + personB.getRelationshipStatus());
        personMapper.updateById(personA.convertToPerson());
        personMapper.updateById(personB.convertToPerson());
    } // end CreateRelationshipInSimulation()

    public static double CalculateCompatibility (PersonDTO personA, PersonDTO personB) {
        // Calculate the compatibility between the two given Persons to help determine whether or not they will form a relationship.
        //
        // param personA: one of the two people for whom to calculate compatibility
        // param personB: the other of the two people for whom to calculate compatibility
        //
        // return: the double value representing the compatibility between the two (0 means horrible match, and 1 means perfect match).

        // TODO - Add factors here (family members? correct gender? age? I.S.?, etc.)

        double p_compatible = 1.0;

        // ----------------------------------------------------------------
        // SAME PERSON (person cannot be in relationship with themselves)
        // ----------------------------------------------------------------
        if (personA.getId().equals(personB.getId())) {
            //System.err.println("A");
            p_compatible = -999.99;
        } // end if (check if two persons are actually the same person)

        // ----------------------------------------------------------------
        // GENDER (heterosexual relationships only).
        // ----------------------------------------------------------------
        if (personA.getSex().equals( personB.getSex())) {
            //System.err.println("B) between " + personA.getID() + " and " + personB.getID() + ".");
            p_compatible = -999.99;
        } // end if (check if two persons are of same sex)

        // ----------------------------------------------------------------
        // STATUS (person must be single).
        // ----------------------------------------------------------------
        if (!personB.getRelationshipStatus().equals(RelationStatusEnum.SINGLE)) {
            //System.err.println("C) between " + personA.getID() + " and " + personB.getID() + ".");
            p_compatible = -999.99;
        } // end if (check if potential partner is single or not)

        // ----------------------------------------------------------------
        // AGE (within 5 years)
        // ----------------------------------------------------------------
        if (Math.abs(personA.getAge() - personB.getAge()) > 5) {
            //System.err.println("D) between " + personA.getID() + " and " + personB.getID() + ".");
            p_compatible = -999.99;
        } // end if (check if two persons are more than 5 years apart in age)

        // ----------------------------------------------------------------
        // FAMILY
        // ----------------------------------------------------------------
        if (!RelationshipAllowance_Family(personA, personB)) {
            //System.err.println("E) between " + personA.getID() + " and " + personB.getID() + ".");
            p_compatible = -999.99;
        } // end if (check if two persons are family members when family relations are prohibited)


        // ================================================================================================================
        // At this point, if p_compatible is 1.0, then the couple is "allowed" to date. Now calculate their I.S.
        // ================================================================================================================
        if (p_compatible > 0.9) { // Compare to 0.9 in case the "1.0" is inaccurately store. Values now should either be 0.0 or 1.0.

            // Calculate Interest Similarity.
            p_compatible = calculateInterestSimilarity(personA, personB);

        } // end if (check if two people are allowed to date)


        return p_compatible;
    } // end CalculateCompatibility()

    private static boolean RelationshipAllowance_Family (PersonDTO personA, PersonDTO personB) {
        // This method will determine whether or not the two given persons are "allowed" to be romantically involved, solely in
        // terms of family. If the two persons are closely related, then they should not form a romantic relationship. However,
        // in some cases (the early generations in the Genesis model especially), where family relationships are still allowed.
        //
        // param personA: one of the two people for whom to calculate family-based compatibility
        // param personB: the other of the two people for whom to calculate family-based compatibility
        //
        // return: the boolean flag indicating whether or not the two persons are "allowed" to get together romantically

        // Check if inter-family romantic relationships are prohibited at this point in the simulation.
        //if (SocietyYear >= YearToDisallowInterbreeding) {
        if (Configuration.SocietyYear < Configuration.YearToDisallowInterbreeding) {
            // Compare the current simulation year to the interbreeding-is-no-long-allowed year to determine whether or not it is allowed currently.
            return true;
        } // end if (check if family-based relationships are allowed at this time)


        // For simplicity, just check if the two are siblings. We won't worry about cousins right now, for computational reasons.
        // We also don't really need to check if the people have a parent-child kinship, because the parents will be married
        // so they are unavailable anyway, and because of the age gap, they wouldn't also be considered.

        // Check if siblings. (Since this is a reflexive relationship, only a one-way check is required)
        if (personA.getSiblingsIds().contains(personB.getId())) {
            //System.out.println("Person " + personA.getID() + " is a sibling with Person " + personB.getID());
            //DebugTools.printArray(personA.getSiblingIDs().toArray());
            //DebugTools.printArray(personB.getSiblingIDs().toArray());
            //System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
            return false;
        } // end if (check if two people are siblings)

        //System.out.println("Person " + personA.getID() + " and Person " + personB.getID() + " are legit.");
        //DebugTools.printArray(personA.getSiblingIDs().toArray());
        //DebugTools.printArray(personB.getSiblingIDs().toArray());

        return true;

    } // end RelationshipAllowance_Family()

    public static void DetermineFateOfDatingCouple (PersonDTO personA, PersonDTO personB) {
        // Determine the fate of the dating couple. The couple could break up, get married, or continue dating.

        // Pre-determine some info that is necessary for the strength factor calculations.
        double societyPopulation = (double)Configuration.N_Population_Size;
        double numYearsDating = (double)(Configuration.SocietyYear - personA.getRelationshipStartYear());

        double relFate_Strength = personA.getRelationshipStrength();
        double relFate_YearsDating = ValidationTools.clipValueToRange((numYearsDating / 5.0), 0.0, 1.0); // The longer a couple is together, the more chance of getting married. It maxes out after 5 years.
        double relFate_PopulationSize = ValidationTools.clipValueToRange(1.0 + ((1.0 - societyPopulation) / 1000.0), 0.0, 1.0); // Linear downward slope from 1.0 to 0.0 for population on [0,1000].
        double relFate_Random = Distribution.uniform(0.0, 1.0);

        double[] datingFateFactors = new double[] {relFate_Strength, relFate_YearsDating, relFate_PopulationSize, relFate_Random};
        double[] datingFateWeights = new double[] {0.15, 0.45, 0.35, 0.05};
        //double[] datingFateFactors = new double[] {relFate_Strength};
        //double[] datingFateWeights = new double[] {1.0};


        double relStrength = 0.0;
        int f;
        for (f = 0; f < datingFateFactors.length; f++) {
            relStrength += (datingFateWeights[f]*datingFateFactors[f]);
        } // end for f (loop through factors to calculate the overall strength of dating couple's relationship)

        //System.out.println("REL STRENGTH ======= " + relStrength);


        // Determine what will happen to the couple.
        if (relStrength <= Configuration.CoupleBreakupThresholdStrength) {
            // Couple breaks up.

            personA.setRelationshipStatus(RelationStatusEnum.SINGLE);
            personB.setRelationshipStatus(RelationStatusEnum.SINGLE);
            personA.setRelationshipStartYear( Integer.MIN_VALUE );
            personB.setRelationshipStartYear( Integer.MIN_VALUE );
            personA.setRelationshipStrength( Double.MIN_VALUE );
            personB.setRelationshipStrength( Double.MIN_VALUE );


            //System.out.println("Couple broke up: " + personA.getID() + " and " + personB.getID());

        } else if (relStrength >= Configuration.CoupleGetMarriedThresholdStrength) {
            // Couple gets married.

            personA.setRelationshipStatus(RelationStatusEnum.MARRIED);
            personB.setRelationshipStatus(RelationStatusEnum.MARRIED);
            CalculateAndSetRelationshipStrength(personA, personB, 0);

        } else {
            // Couple continues dating.

            // Do nothing.
        } // end if (determine what will happen to the dating couple)

    } // end DetermineFateOfDatingCouple()

    public void setHistoryService(HistoryService historyService) {
        RelationshipCalculator.historyService = historyService;
    }

    public void setGroupService(GroupService groupService) {
        RelationshipCalculator.groupService = groupService;
    }

    public void setPersonMapper(PersonMapper personMapper) {
        RelationshipCalculator.personMapper = personMapper;
    }
} // end RelationshipCalculator
