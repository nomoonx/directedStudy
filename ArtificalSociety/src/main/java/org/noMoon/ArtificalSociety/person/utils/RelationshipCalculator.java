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
	
	

	
	
	
} // end RelationshipCalculator
