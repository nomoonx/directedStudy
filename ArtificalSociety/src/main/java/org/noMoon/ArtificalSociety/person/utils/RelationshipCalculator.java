package org.noMoon.ArtificalSociety.person.utils;

import org.noMoon.ArtificalSociety.person.DTO.PersonDTO;


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
    /*

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
        if (personA.getRelationshipStatus() == REL_TYPE_MARRIED) {
            return 1.0;
        } else if (personA.getRelationshipStatus() == REL_TYPE_DATING) {
            return 0.8;
        } else {
        } // end if (determine relationship type)

        return 0.0;
    } // end calcRelStrength_Type()

    private static double calcRelStrength_Children (PersonDTO personA, PersonDTO personB) {
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

    private static double calcRelStrength_Race (PersonDTO personA, PersonDTO personB) {
        // Determine whether or not the couple are the same race. If they are the same, then return 1.0, otherwise return 0.0. At this point, there is no need for
        // an in-between or fuzzy area.

        if (personA.getRace() == personB.getRace()) {
            return 1.0;
        } else {
            return 0.0;
        } // end if (checking if two people are same race)
    } // end calcRelStrength_Race()

    private static double calcRelStrength_Religion (PersonDTO personA, PersonDTO personB) {
        // Determine whether or not the couple are the same religion. If they are the same, then return 1.0, otherwise return 0.0. At this point, there is no need for
        // an in-between or fuzzy area.

        if (personA.getReligion() == personB.getReligion()) {
            return 1.0;
        } else {
            return 0.0;
        } // end if (checking if two people are same religion)
    } // end calcRelStrength_Religion()

    private static double calcRelStrength_YearsMarried (PersonDTO personA, PersonDTO personB) {
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

        String personALocation = (String)personA.getHometownHistory().getLastActivityName();
        String personBLocation = (String)personB.getHometownHistory().getLastActivityName();

        // If two people live in same city, then return 1.0, otherwise return 0.0.
        if (personALocation.equals(personBLocation)) {
            return 1.0;
        } else {
            return 0.0;
        } // end if (check if two people live in same society)

    } // end calcRelStrength_Location()
	
	*/
	
} // end RelationshipCalculator
