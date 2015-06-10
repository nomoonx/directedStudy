package thesis_network_growth;

import java.util.ArrayList;
import java.util.Arrays;

import tests.growingsocialnetwork.TestAgent;


import static thesis_network_growth.Configuration.*;

public class RelationshipCalculator {
	
	// Relationship Types.
	public final static int REL_TYPE_SINGLE = 0;
	public final static int REL_TYPE_MARRIED = 1;
	public final static int REL_TYPE_DATING = 2;
	public final static int REL_TYPE_WIDOWED = 3;
	
	
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
	
	public static void calculateAndSetInterestSimilarity (Person personA, Person personB) {
		
		double IS_AB = calculateInterestSimilarity(personA, personB);
		double IS_BA = calculateInterestSimilarity(personB, personA);
		
		//System.out.println("IS_AB = " + IS_AB + " and IS_BA = " + IS_BA + "       {" + personA.getID() + "," + personB.getID() + "}.");
		
		// Set each person's interest similarity.
		personA.setInterestSimilarity(IS_AB);
		personB.setInterestSimilarity(IS_BA);
		
		
	} // end calculateAndSetInterestSimilarity()
	
	private static double calculateInterestSimilarity (Person personA, Person personB) {
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

		double[] personAWeights = personA.getInterestWeights();
		double[] personAInterests = personA.getInterests();
		double[] personBInterests = personB.getInterests();

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
	
	
	
	// ------------------------------------------------------------------------------------------------------
	// SINGLES SEARCH FOR PARTNER.
	// ------------------------------------------------------------------------------------------------------

	public static void SearchForPartner (Person person) {
		// This method will search for a compatible match for the given Person.
		//
		// param person: the given Person for whom to find a compatible partner
		//
		// ????????????????????????
		
		int i;
		Person possPartner;
		int numPotentialFriends = person.getFriendProbIDs().size();
		int chosenPartnerID;
		
		// If there are no potential friends, then person has no potential partner, so leave function immediately.
		if (numPotentialFriends == 0) {
			return;
		} // end if (check if there are no potential friends->partners)
		
		
		double[] compatibilityValues = new double[numPotentialFriends]; // Array for compatibility value for each possible person!
		
		for (i = 0; i < numPotentialFriends; i++) {
			possPartner = ArtificialSociety.getPersonByID( person.getFriendProbIDs().get(i) );

			
			compatibilityValues[i] = RelationshipCalculator.CalculateCompatibility(person, possPartner);
			//System.out.println(possPartner.getID() + " {" + i + "}  " + compatibilityValues[i]);

		} // end for i (loop through all potential friends to search for a potential partner)
		
		//System.out.println("Search for partner for Person #" + person.getID());
		//DebugTools.printArray(compatibilityValues);
		//Arrays.sort(compatibilityValues);
		//DebugTools.printArray(compatibilityValues);
		

		
		
		// Sort compatibility values.
		int c;
		double maxCompatibility = compatibilityValues[0];
		int maxIndex = 0;
		for (c = 0; c < compatibilityValues.length; c++) {
			// If a higher value is found, update the max.
			if (compatibilityValues[c] > maxCompatibility) {
				maxCompatibility = compatibilityValues[c];
				maxIndex = c;
			} // end if (check for higher compatibility value)
		} // end for c (loop through all compatibility values)
		 
		
		//System.out.println("FOUND MAXIMUM OF <" + maxCompatibility + "> AT INDEX <" + maxIndex + ">.");
		
		
		
		Person mostCompatiblePerson = null;
		double THRESHOLD = -10.0;
		if (maxCompatibility > THRESHOLD) {
			mostCompatiblePerson = ArtificialSociety.getPersonByID( person.getFriendProbIDs().get(maxIndex) );	
		} // end if (ensure that compatibility reaches a valid threshold)
		
		
		
		
		double p_formRelationshipWithCompatiblePartner = 0.7;
		double rndFormRelationship = Distribution.uniform(0.0, 1.0);
		
		// Check opposite (if NOT going to match up).
		if (rndFormRelationship > p_formRelationshipWithCompatiblePartner) {
			return;
		} // end if (check if couple will NOT get together)
		
		
		//System.out.println("mostCompatiblePerson ID = " + mostCompatiblePerson.getID());
 
		
		//System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
		
		
		
		
		
		/*
		// BAD TEMP! DELETE ASAP
		Person possPers;
		int pIndex = 0;
		int numTotalPeople = ArtificialSociety.getSociety().size();
		do {
			possPers = ArtificialSociety.getPersonByIndex(pIndex);
			pIndex++;
			//System.out.println("Looking at " + pIndex + " out of " + numTotalPeople);
			
			// If entire population has been searched and no good match was found, then break. The person doesn't find a match.
			if (pIndex >= numTotalPeople) {
				possPers = null;
				break;
			} // end if (check if entire population has been searched over with no match)
		} while ((!RelationshipAllowance_Family(person,possPers)) || (possPers.getID() == person.getID()) || (possPers.getRelationshipStatus() == REL_TYPE_MARRIED || possPers.getRelationshipStatus() == REL_TYPE_DATING)); // end do-while (loop until a valid person is selected)
		//} while ((possPers.getID() == person.getID()) || (possPers.getRelationshipStatus() == REL_TYPE_MARRIED || possPers.getRelationshipStatus() == REL_TYPE_DATING)); // end do-while (loop until a valid person is selected)
		// BAD TEMP! DELETE ASAP
		//
		*/
		
		
		
		
		
		//System.err.println("searching dating");

	
		// If a match was found, then create the dating relationship between the two.
		if (mostCompatiblePerson != null) {
			
			CreateRelationshipInSimulation(person, mostCompatiblePerson, REL_TYPE_DATING);
		} // end if (check if a match was found for the person)

		
	} // end SearchForPartner()
	
	
	public static double CalculateCompatibility (Person personA, Person personB) {
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
		if (personA.getID() == personB.getID()) {
			//System.err.println("A");
			p_compatible = -999.99;
		} // end if (check if two persons are actually the same person)

		// ----------------------------------------------------------------
		// GENDER (heterosexual relationships only).
		// ----------------------------------------------------------------
		if (personA.getSex() == personB.getSex()) {
			//System.err.println("B) between " + personA.getID() + " and " + personB.getID() + ".");
			p_compatible = -999.99;
		} // end if (check if two persons are of same sex)

		// ----------------------------------------------------------------
		// STATUS (person must be single).
		// ----------------------------------------------------------------
		if (personB.getRelationshipStatus() != RelationshipCalculator.REL_TYPE_SINGLE) {
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
	
	
	private static boolean RelationshipAllowance_Family (Person personA, Person personB) {
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
		if (SocietyYear < YearToDisallowInterbreeding) {
			// Compare the current simulation year to the interbreeding-is-no-long-allowed year to determine whether or not it is allowed currently.
			return true;
		} // end if (check if family-based relationships are allowed at this time)
		
		
		// For simplicity, just check if the two are siblings. We won't worry about cousins right now, for computational reasons.
		// We also don't really need to check if the people have a parent-child kinship, because the parents will be married
		// so they are unavailable anyway, and because of the age gap, they wouldn't also be considered.
		
		// Check if siblings. (Since this is a reflexive relationship, only a one-way check is required)
		if (personA.getSiblingIDs().contains(personB.getID())) {
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
	
	
	public static void CreateRelationshipInSimulation (Person personA, Person personB, int relType) {
		// Initialize a couple's relationship from a live simulation, by setting their relationship start year as the
		// current year in the simulation, and by then creating the relationship between them.
		//
		// param personA: one of the people in the new relationship
		// param personB: the other person in the new relationship
		// param relType: the integer indicating the relationship type (dating or married)

		personA.setRelationshipStartYear( Configuration.SocietyYear );
		personB.setRelationshipStartYear( Configuration.SocietyYear );
		
		PersonGroupAdder.createRelationship(personA, personB, relType);
		//System.out.println("Relationship statuseseses: " + personA.getRelationshipStatus() + " && " + personB.getRelationshipStatus());
		
	} // end CreateRelationshipInSimulation()
	
	
	public static void DetermineFateOfDatingCouple (Person personA, Person personB) {
		// Determine the fate of the dating couple. The couple could break up, get married, or continue dating.
		
		// Pre-determine some info that is necessary for the strength factor calculations.
		double societyPopulation = (double)ArtificialSociety.getSociety().size();
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

			personA.setRelationshipStatus(REL_TYPE_SINGLE);
			personB.setRelationshipStatus(REL_TYPE_SINGLE);
			personA.setRelationshipStartYear( Integer.MIN_VALUE );
			personB.setRelationshipStartYear( Integer.MIN_VALUE );
			personA.setRelationshipStrength( Double.MIN_VALUE );
			personB.setRelationshipStrength( Double.MIN_VALUE );
			
			
			//System.out.println("Couple broke up: " + personA.getID() + " and " + personB.getID());

		} else if (relStrength >= Configuration.CoupleGetMarriedThresholdStrength) {
			// Couple gets married.

			personA.setRelationshipStatus(REL_TYPE_MARRIED);
			personB.setRelationshipStatus(REL_TYPE_MARRIED);
			CalculateAndSetRelationshipStrength(personA, personB, 2);

		} else {
			// Couple continues dating.

			// Do nothing.
		} // end if (determine what will happen to the dating couple) 
		
		
		
		
	} // end DetermineFateOfDatingCouple()
	
	// ------------------------------------------------------------------------------------------------------
	// DETERMINE RELATIONSHIP CHILDREN.
	// ------------------------------------------------------------------------------------------------------
	
	public static int DetermineNumberOfChildren (Person personA, Person personB) {
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
		
		if (rndChildren < p_likelihoodMarriedCoupleHasChildren) {
			// (EDIT for below line: Not true anymore.. At this point there is not necessarily going to be children. Could select zero here.
			// There will be children. Now determine the number of children.
			
			
			// TODO Incorporate relationship strength and/or other factors into number of children equation.
			// Get relationship strength (should be identical for both persons, so just get it from personA for simplicity).
			double detNumChildrenFactor_RelStrength = personA.getRelationshipStrength();
			//System.out.println(personB.getRelationshipStrength());
		

			numChildren = ValidationTools.clipValueToRange((int)Math.round(DistributionParser.parseStatisticalDistribution(marriageChildrenDistr)), marriageChildrenMin, marriageChildrenMax);
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
	
	public static int DetermineCurrentChildBirth (Person personA, Person personB) {
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
		
		if (rndChildren < p_likelihoodMarriedCoupleHasChildren) {
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
				int expNumChildren = ValidationTools.clipValueToRange((int)Math.round(DistributionParser.parseStatisticalDistribution(marriageChildrenDistr)), marriageChildrenMin, marriageChildrenMax);
				int numChildrenAlready = personA.getChildrenIDs().size();
				
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
	


	public static void CreateChildren (Person parentA, Person parentB, boolean isBackFilling) {
		// This function will determine how many children (including the option of zero) the couple will have
		// and it will create those children. Note that this is being used for back-filling children for a model
		// that creates a population all at once, as well as for creating children on-the-fly during a simulation step.
		//
		// param parentA: the first person in the relationship
		// param parentB: the second person in the relationship
		// param isBackFilling: the important flag that indicates whether this is to create a population with children 
		//		created after-the-fact [isBackFilling=true], or if is for an active simulation to create children
		//		on-the-fly, one at a time [isBackFilling=false].
		
		
		Person child;
		Person[] children;
		int numChildren;
		int c;
		
		// Ensure a couple's children are only from one parent's pass through the loop.
		if (parentA.getID() > parentB.getID()) { // Only create children at the person with the greater ID. This way children won't be created twice for the same couple.
						
			// Determine the number of children this couple has.
			if (isBackFilling) {
				// If back-filling children after-the-fact.
				//System.out.println("Method I");
				numChildren = DetermineNumberOfChildren(parentA, parentB);
			} else {
				// If running a live simulation at current time.
				//System.out.println("Method II");
				numChildren = DetermineCurrentChildBirth(parentA, parentB);
				//System.err.println("In live simulation, parents " + parentA.getID() + " and " + parentB.getID() + " have " + numChildren + " children.");
			}

			// Create array to keep track of the children as they are created.
			children = new Person[numChildren];
			
			//System.err.println("Creating " + numChildren + " children.");

			// Loop through and create children for couple.
			for (c = 0; c < numChildren; c++) {
				child = PersonGenerator.GeneratePerson(6, new Person[] {parentA, parentB});	// CHILD
				child = PersonGenerator.AssignChildAttributes(child, parentA, parentB, isBackFilling);
				child = PersonGenerator.complete(child); // complete() fills in the missing attributes for the person.

				// Add child to array.
				children[c] = child;
			} // end for c (creating children for couple)

			// Create all interconnected relationships for the parents and children.
			PersonGroupAdder.createChildrenConnections(parentA, parentB, children);

		} // end if (compare couple's IDs to ensure children are only created once per couple)
	} // end CreateChildren()
	
	
	
	
	
	// ------------------------------------------------------------------------------------------------------
	// RELATIONSHIP STRENGTH.
	// ------------------------------------------------------------------------------------------------------
	
	public static void CalculateAndSetRelationshipStrength (Person personA, Person personB, int pass) {
		
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

	private static double CalculateRelationshipStrength (Person personA, Person personB, double[] factorWeights, int pass) {
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

		
		
		

		//System.out.println("type\t\t" + calcRelStrength_Type(personA, personB) + " (" + factorWeights[REL_STRENGTH_TYPE] + ")");
		//System.out.println("children\t" + calcRelStrength_Children(personA, personB) + " (" + factorWeights[REL_STRENGTH_CHILDREN] + ")");
		//System.out.println("race\t\t" + calcRelStrength_Race(personA, personB) + " (" + factorWeights[REL_STRENGTH_RACE] + ")");
		//System.out.println("religion\t" + calcRelStrength_Religion(personA, personB) + " (" + factorWeights[REL_STRENGTH_RELIGION] + ")");
		//System.out.println(personA.getID() + "," + personB.getID() + " pass #" + pass + " -- years\t\t" + calcRelStrength_YearsMarried(personA, personB) + " (" + factorWeights[REL_STRENGTH_YEARSMARRIED] + ") \t [" + (Configuration.SocietyYear - personA.getRelationshipStartYear()) + "]");
		//System.out.println("education\t" + calcRelStrength_Education(personA, personB) + " (" + factorWeights[REL_STRENGTH_EDUCATION] + ")");
		//System.out.println("random\t\t" + calcRelStrength_Random(personA, personB) + " (" + factorWeights[REL_STRENGTH_RANDOM] + ")");
		//System.out.println("total strength\t" + strength);


		//System.out.println("relationship strength between " + personA.id + " and " + personB.id + " = "+ strength);

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

	private static double calcRelStrength_Type (Person personA, Person personB) {
		// Return the strength factor from the relationship type. For now, these are just two fixed numbers and the
		// value for a dating relationship is a little less than that of a married relationship.
		if (personA.getRelationshipStatus() == REL_TYPE_MARRIED) {
			return 1.0;
		} else if (personA.getRelationshipStatus() == REL_TYPE_DATING) {
			return 0.8;
		} else {
		} // end if (determine relationship type)
		
		return 0.0;
	} // end calcRelStrength_Type()

	private static double calcRelStrength_Children (Person personA, Person personB) {
		// Return the strength factor from the couple's children. In the current implementation, there is no possibility
		// of step-children from past relationships, but in a future work, that would be a consideration, so that's
		// why here we get the list of children from both personA and personB.
		ArrayList personAChildren = personA.getChildrenIDs();
		ArrayList personBChildren = personB.getChildrenIDs();

		if (personAChildren.isEmpty() || personBChildren.isEmpty()) {
			// No children.
			return 1.0;
		} // end if (check if either person has no children) - MAY HAVE TO BE MODIFIED



		int[] aChildren = ArrayTools.arrayListToIntArray(personAChildren);
		int[] bChildren = ArrayTools.arrayListToIntArray(personBChildren);
			
		aChildren = ArrayTools.unique(ArrayTools.sort(aChildren));
		bChildren = ArrayTools.unique(ArrayTools.sort(bChildren));
			
		//DebugTools.printArray(aChildren);
		//DebugTools.printArray(bChildren);
			
		//System.out.println("~~~~~~~~~~~~~~~~~~~~" + personA.id + " || " + personB.id + "~~~~~~~~~~~~~~~~~~~~~~~~~~");
			
		return 0.0;
	} // end calcRelStrength_Children()

	private static double calcRelStrength_Race (Person personA, Person personB) {
		// Determine whether or not the couple are the same race. If they are the same, then return 1.0, otherwise return 0.0. At this point, there is no need for
		// an in-between or fuzzy area.
			
		if (personA.getRace() == personB.getRace()) {
			return 1.0;
		} else {
			return 0.0;
		} // end if (checking if two people are same race)
	} // end calcRelStrength_Race()

	private static double calcRelStrength_Religion (Person personA, Person personB) {
		// Determine whether or not the couple are the same religion. If they are the same, then return 1.0, otherwise return 0.0. At this point, there is no need for
		// an in-between or fuzzy area.
			
		if (personA.getReligion() == personB.getReligion()) {
			return 1.0;
		} else {
			return 0.0;
		} // end if (checking if two people are same religion)
	} // end calcRelStrength_Religion()

	private static double calcRelStrength_YearsMarried (Person personA, Person personB) {
		// Determine how long the couple has been together.
		
		if (personA.getRelationshipStartYear() != personB.getRelationshipStartYear()) {
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
	
	private static double calcRelStrength_Education (Person personA, Person personB) {
		// Determine whether or not the two people have the same education level as one another. This is currently checked as a simple equality check, so University (U)
		// and College (C) count as different education levels and thus will return 0.0. This could be modified to allow those to be equal or similar.
		
		if (personA.getEducation().equals(personB.getEducation())) {
			return 1.0;
		} else {
			return 0.0;
		} // end if (checking if two people have the same education)
	} // end calcRelStrength_Education()

	private static double calcRelStrength_Random (Person personA, Person personB) {
		// Add a random element into the relationship strength, since no relationship is completely mathematically computable with a formula.
		return Distribution.uniform(0.0, 1.0);
	} // end calcRelStrength_Random()

	private static double calcRelStrength_Income (Person personA, Person personB) {
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
	
	private static double calcRelStrength_Interest (Person personA, Person personB) {
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

	
	private static double calcRelStrength_Location (Person personA, Person personB) {
		// Determine the strength associated to the geographical location of the two persons.
		
		String personALocation = (String)personA.getHometownHistory().getLastActivityName();
		String personBLocation = (String)personB.getHometownHistory().getLastActivityName();
		
		// If two people live in same city, then return 1.0, otherwise return 0.0.
		if (personALocation.equals(personBLocation)) {
			return 1.0;
		} else {
			return 0.0;
		} // end if (check if two people live in same society)
		
	} // end calcRelStrength_Location()
	
	
	
} // end RelationshipCalculator
