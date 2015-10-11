package org.noMoon.ArtificalSociety.commons.utils;


public class TraitMatcher {
	
	/*
	
	public static double calculateTotalProbability (double[] matchValues, boolean doesNotMeetRequirements) {
		
		double p;
		
		if (doesNotMeetRequirements || matchValues.length == 0) {
			// If there are no matches, then there is no chance of taking this career (or should there still be a small chance for an outlier?)
			p = 0.0;
		
		} else {
			// If there were at least one matched trait, then calculate the average probability from those matched traits.
		
			int t;
			double totalValSum = 0.0;
			for (t = 0; t < matchValues.length; t++) {
				totalValSum += matchValues[t];
			} // end for t (loop through all trait match values)
			// Calculate probability as the average probability for the traits that matched.
			p = totalValSum / (double)matchValues.length;

		} // end

		
		return p;
	} // end calculateClubProbability()
	
	
	
	
	public static double calculateTraitMatch (Person person, String[] trait) {
		
		// This function will determine whether or not the person has the given trait or attribute (fully, partially, or not at all) and calculates a match value.
		// param person: the Person whose traits are being examined
		// trait: a String array containing information on the trait being examined, this is of the format [traitType, traitValue, traitReq]
		//
		// returns: a match value between 0 and 1 representing how well the person matches the given trait

		double p_match = 0.0;

		String traitType = trait[0];
		String traitValue = trait[1];
		// Note that the trait array has 3 parts: [traitType, traitValue, traitReq]; however, the traitReq part is not used at this level. It is handled when determining
		// whether or not a person is assigned to a club, career, etc. so it is not necessary here.


		if (traitType.equals("mbti")) {
			p_match = calculateTraitMatch_MBTI(person, traitValue);
		
		} else if (traitType.equals("age")) {
			// Use discrete age categories: {Child, Teen, Young Adult, Adult, Elderly}
			p_match = calculateTraitMatch_Age(person, traitValue);
			
		} else if (traitType.equals("sex")) {
			p_match = calculateTraitMatch_Sex(person, traitValue);
	
		} else if (traitType.equals("race")) {
			p_match = calculateTraitMatch_Race(person, traitValue);

		} else if (traitType.equals("religion")) {
			p_match = calculateTraitMatch_Religion(person, traitValue);

		} else if (traitType.equals("rel_status")) {
			p_match = calculateTraitMatch_RelationshipStatus(person, traitValue);

		} else if (traitType.equals("careerID")) {
			p_match = calculateTraitMatch_Career(person, traitValue);

		} else if (traitType.equals("workID")) {
			p_match = calculateTraitMatch_Work(person, traitValue);
			
		} else if (traitType.equals("extra")) {
			p_match = calculateTraitMatch_Extra(person, traitValue);
			
		} else if (traitType.equals("num_ps_years")) {
			p_match = calculateTraitMatch_NumYearsPSEducationRequired(person, traitValue);
			
		} else {
			System.err.println("Could not find the given attribute type '" + traitType + "'. Ensure the files use trait types that can be handled.");
		}
		
		//System.out.print(" " + p_match + " ");
		return p_match;
	} // end calculateTraitMatch()
	
	
	
	
	
	private static double calculateTraitMatch_MBTI (Person person, String traitValue) {
		
		
		double p = 0.0;
		// -------------------------------------
		// MBTI Traits
		// -------------------------------------
		// I/E, S/N, T/F, J/P
		if (traitValue.equals("Introverted")) {
			p = 1.0 - person.getPersonality()[0];			// Introversion = 1 - Extroversion
		} else if (traitValue.equals("Extroverted")) {
			p = person.getPersonality()[0];
		} else if (traitValue.equals("Sensing")) {
			p = 1.0 - person.getPersonality()[1];			// Sensing = 1 - Intuition
		} else if (traitValue.equals("Intuition")) {
			p = person.getPersonality()[1];
		} else if (traitValue.equals("Thinking")) {
			p = 1.0 - person.getPersonality()[2];			// Thinking = 1 - Feeling
		} else if (traitValue.equals("Feeling")) {
			p = person.getPersonality()[2];
		} else if (traitValue.equals("Judging")) {
			p = 1.0 - person.getPersonality()[3];			// Judging = 1 - Perceiving
		} else if (traitValue.equals("Perceiving")) {
			p = person.getPersonality()[3];
		} // end if (checking which MBTI trait is being used)

		return p;
	} // end calculateTraitMatch_MBTI()
	
	
	private static double calculateTraitMatch_Age (Person person, String traitValue) {
		// Use discrete age categories: {Child, Teen, Young Adult, Adult, Elderly}
		int[][] ageBins = new int[][] {
			new int[] {6, 12},
			new int[] {13, 19},
			new int[] {20, 34},
			new int[] {35, 64},
			new int[] {65, 99},
		};

		double p = 0.0;

		int binIndex = 0;

		// Determine which age bin to use.
		if (traitValue.equals("Child")) {
			binIndex = 0;
		} else if (traitValue.equals("Teen")) {
			binIndex = 1;
		} else if (traitValue.equals("Young Adult")) {
			binIndex = 2;
		} else if (traitValue.equals("Adult")) {
			binIndex = 3;
		} else if (traitValue.equals("Elderly")) {
			binIndex = 4;
		} // end if (checking which sex is being used)

		
		// Check if person's age is within the given category/bin.
		if (ValidationTools.numberIsWithin(person.getAge(), ageBins[binIndex][0], ageBins[binIndex][1])) {
			p = 1.0;
		} else {
			// For now, if the person is not in the given age bin, then the probability is 0.0.
			// This could be modified to look how close to the bin the person is (i.e. if they are within 1 year, they could still have some probability).
			p = 0.0;
		}
		
		return p;
	} // end calculateTraitMatch_Age()


	
	
	
	
	private static double calculateTraitMatch_Sex (Person person, String traitValue) {
		double p = 0.0;
		if (traitValue.equals("0")) {
			p = (double)(1.0 - person.getSex());
		} else if (traitValue.equals("1")) {
			p = (double)(person.getSex());
		} // end if (checking which sex is being used)

		return p;
	} // end calculateTraitMatch_Sex()
	
	
	private static double calculateTraitMatch_Race (Person person, String traitValue) {
		double p = 0.0;
		int i;
		for (i = 0; i < Configuration.RaceLabels.length; i++) {
			
			if (traitValue.equals(Configuration.RaceLabels[i])) {
				if (person.getRace() == i) {
					p = 1.0;
				} else {
					p = 0.0;
				} // end if (person's race matches the one being used)
			} // end if (check if race in loop is the one being used for the club criteria)

		} // end for i (loop through all races to find the appropriate one)
		
		return p;
	} // end calculateTraitMatch_Race()
	
	private static double calculateTraitMatch_Religion (Person person, String traitValue) {
		double p = 0.0;
		int i;
		for (i = 0; i < Configuration.ReligionLabels.length; i++) {
			
			if (traitValue.equals(Configuration.ReligionLabels[i])) {
				if (person.getReligion() == i) {
					p = 1.0;
				} else {
					p = 0.0;
				} // end if (person's religion matches the one being used)
			} // end if (check if religion in loop is the one being used for the club criteria)

		} // end for i (loop through all religion to find the appropriate one)
		
		return p;
	} // end calculateTraitMatch_Religion()
	
	
	private static double calculateTraitMatch_Career (Person person, String traitValue) {
		double p = 0.0;

		// Check if the given careerID (given in traitValue argument) matches the person's careerPathID.
		if (traitValue.equals(person.getCareer())) {
			p = 1.0;
		} else {
			p = 0.0;
		} // end if (check if person's career matches the one being used in club criteria)

		return p;
	} // end calculateTraitMatch_Career()
	
	private static double calculateTraitMatch_Work (Person person, String traitValue) {
		double p = 0.0;
		
		if (ValidationTools.empty(person.getWorkHistory()) || person.getWorkHistory().isEmpty()) {
			// If person has no current work.
			p = 0.0;
			return p;
		}
		
		//DebugTools.printArray(person.workHistory);
		String[] currWork = (String[])person.getWorkHistory().getLastActivityName();

		// Check if the given workplaceID (given in traitValue argument) matches the person's current workplace ID. (Note that currWork[0] is the workplace ID, while currWork[1] would be their career ID at that workplace).
		if (traitValue.equals(currWork[0])) {
			p = 1.0;
		} else {
			p = 0.0;
		} // end if (check if person's workplace matches the one being used in club criteria)

		return p;
	} // end calculateTraitMatch_Work()
	
	
	private static double calculateTraitMatch_RelationshipStatus (Person person, String traitValue) {
		double p = 0.0;
		// TODO   When other relationship status options are being used, this will have to be modified to allow all options to work properly.
		if (traitValue.equals("Single")) {
			p = 1.0 - person.getRelationshipStatus();		// Single = 1 - Married
		} else if (traitValue.equals("Married")) {
			p = person.getRelationshipStatus();
		} // end if (checking which "rel_status" option is being used)
		
		return p;
	} // end calculateTraitMatch_RelationshipStatus()
	
	
	
	private static double calculateTraitMatch_Extra (Person person, String traitValue) {
		
		
		double p = 0.0;
		
		if (traitValue.equals("Intelligent")) {
			p = person.getIntelligence();
		} else if (traitValue.equals("Athletic")) {
			p = person.getAthleticism();
		} // end if (checking which extra trait is being used)

		return p;
	} // end calculateTraitMatch_Extra()
	
	
	private static double calculateTraitMatch_NumYearsPSEducationRequired (Person person, String traitValue) {
		//
		
		
		double p = 0.0;
		
		int relStartYear = -888;
		int projectedEduFinishYear = -888;
		
		
		if (person.getRelationshipStatus() == RelationshipCalculator.REL_TYPE_MARRIED) {
			
			relStartYear = person.getRelationshipStartYear();
			projectedEduFinishYear = person.getYearBorn() + Configuration.SchoolFinishAge + Integer.parseInt(traitValue);
		
			
			
			
			// Check if the person would be completely finished their education before their marriage year.
			if (projectedEduFinishYear <= relStartYear) {
				// They are completed all education before marriage.
				p = 1.0;
			} else {
				// They get married they would complete their education.
				// TODO (?)
				// NOTE: The location of each of the people before marriage could be examined here as another factor so they could  s
				til l take the longer programs.
				
				p = 0.0;
				
				
			} // end if (check whether or not person would be finished their education before their marriage)
			
			//System.out.println("Projected To Finish School In  " + projectedEduFinishYear);
			
			
			//System.out.println("Person was married in " + relStartYear + "; so will they want to commit to P.S. for " + traitValue + " years?\t|\t" + p);
			
		} else {
			// If not married, then they do not need to consider the length of their program.
			p = 1.0;
		} // end if (check whether or not person is married)

		return p;
	} // end calculateTraitMatch_NumYearsPSEducationRequired()
*/
} // end TraitMatcher class
