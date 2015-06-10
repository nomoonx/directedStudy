package thesis_network_growth;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;


public class AttributeAssigner {
	
	static ArrayList careersDatabase;
	static ArrayList careersTable;
	static ArrayList[] careersByPersonality;
	
	public static void initializeCareersTables () {
		careersTable = Careers.createOpeningsTable(Configuration.N_Population_Size);
		careersByPersonality = Careers.getPersonalityCareers();
		careersDatabase = Careers.getFullCareersDatabase();
		//System.out.println("created careersTable for population of " + Configuration.N_Population_Size);
		//DebugTools.printArray(careersTable);
	} // end initializeCareersTables()
	
	
	
	
	// ==================================================================================================================================================================
	// Independent Attributes
	// ==================================================================================================================================================================
	
	public static void assignID (Person attr) {
		// This will give the person the next available ID, and then increment the "next available ID" value to be ready for the next person.
		attr.giveAutomaticID();
	} // end assignID()
	
	public static void assignSex (Person attr) {
		// Assign a random sex to the person. Use a uniform distribution in [0, 1].
		int v = Distribution.uniform(0, 1);
		attr.setSex(v);
	} // end assignSex()
	

	public static void assignInitAge (Person attr) {
		int v = Distribution.uniform(Configuration.AdultAgeMin, Configuration.AdultAgeMax);
		attr.setInitAge(v);
	} // end assignInitAge()
		
	
	
	public static void assignBirthYear (Person attr) {
		// Assign the birth year to the person. First a random initial age is generated, then the birth year is calculated
		// from the age and current society year. This is then set, so that as time passes in the simulation, the person's
		// age can be re-computed each year. 
		// Note that this may not be precise, as month or day is not in the equation. But it should be close enough for the simulation.
		
		
		
		//int age = Distribution.uniform(Configuration.AdultAgeMin, Configuration.AdultAgeMax);
		//System.out.println(age);
		int v = Configuration.SocietyYear - attr.getInitAge();
		attr.setYearBorn(v);
		//System.out.println(birthYear);

	} // end assignBirthYear()
	
	public static void assignAge (Person attr) {
		
		int v = Configuration.SocietyYear - attr.getYearBorn();
		attr.setAge(v);
		
	} // end assignAge()

	
	public static void assignAgez (Person attr) {
		// Assign the person's age based on their birth year and the current year.
		
		if (!ValidationTools.empty(attr.getYearBorn())) {
			int v = Configuration.SocietyYear - attr.getYearBorn();
			//System.out.println("age = " + v);
			attr.setAge(v);
			//System.out.println(v);
		} else {
			// If the person's birth year has not been set.
			System.err.println("In AttributeAssigner->assignAge(); attempting to assign age but birth year has not yet been assigned.");
		} // end if (ensure person's birth year has been set)

	} //  end assignAge()
	public static void assignAgeXX (Person attr) {
		// Assign a random age to the person. Use a uniform distribution in [25, 45].
		int v = Distribution.uniform(Configuration.AdultAgeMin, Configuration.AdultAgeMax);
		attr.setAge(v);
	} //  end assignAge()
	
	public static void assignBirthYearX (Person attr) {
		// Assign the year of birth to the person. This is calculated from their age (if set) and the current year of the society.
		// Note that this may not be precise, as month or day is not in the equation. But it should be close enough for the simulation.
		if (!ValidationTools.empty(attr.getAge())) {
			// If the person has an age, then calculate their year of birth.
			int v = Configuration.SocietyYear - attr.getAge();
			attr.setYearBorn(v);
		} else {
			// If the person does not have an age, then their year of birth cannot be set. This should never be the case though.
		} // end if (check if person's age has been set)

	} // end assignBirthYear()
	
	
	public static void assignExpectedYearOfDeath (Person attr) {
		// Compute the expected year that this person will die of natural causes. In future work, person could die earlier but
		// this is the maximum year that they would stay alive.
		
		if (ValidationTools.empty(attr.getYearBorn())) {
			System.err.println("In AttributeAssigner->assignExpectedYearOfDeath(); attempting to assign life expectancy but birth year has not yet been assigned.");
		} // end if (ensure that person's birth year has been set)
		
		
		
		int ageOfDeath = (int)Math.round(DistributionParser.parseStatisticalDistribution(Configuration.LifeExpectancyDistr));

		int yearOfDeath = ageOfDeath + attr.getYearBorn();
		
		//System.out.println("expected life expectancy ==> " + ageOfDeath + ", year ==> " + yearOfDeath);
		
		//System.err.println("In AttributeAssigner->assignExpectedYearOfDeath(); year born -> " + attr.getYearBorn() + " || " + yearOfDeath + " [" + attr.getID() + "].");
		
		attr.setExpectedDeathYear(yearOfDeath);
		
	} // end assignExpectedYearOfDeath()
	
	public static void assignRace (Person attr) {
		// Assign a random race to the person. Use the custom distribution as defined in the Configuration file.
		int v = Distribution.custom(Configuration.RaceDistr);
		attr.setRace(v);
	} // end assignRace()

	public static void assignReligion (Person attr) {
		// Assign a random religion to the person. Use the custom distribution as defined in the Configuration file.
		int v = Distribution.custom(Configuration.ReligionDistr);
		attr.setReligion(v);
	} // end assignReligion()

	public static void assignNationality (Person attr) {
		// Assign the nationality of the person. use the current society name.
		attr.setNationality(Configuration.SocietyName);
	} // end assignNationality()
	
	public static void assignPersonality (Person attr) {
		// Assign personality traits to this person. Currently use the MBTI model with 4 traits, and use ternary values (0, 0.5, or 1).
		//
		// 0	1
		// ------
		// I	E
		// S	N
		// T	F
		// J	P

		int numTraits = 4;
		int t;
		double[] traits = new double[numTraits];
		for (t = 0; t < numTraits; t++) {
			traits[t] = (double)(Distribution.uniform(1, 3) - 1.0) / 2.0; // Choose 0, 0.5, or 1 uniformly.
		}
		attr.setPersonality(traits);
	} // end assignPersonality()
	
	public static void assignIntelligence (Person attr) {
		// Generate a random intelligence value for the person. Currently using a Gaussian distribution.
		double intelligence_val = ValidationTools.clipValueToRange(Distribution.normal(0.5, 0.1), 0.0, 1.0);
		attr.setIntelligence(intelligence_val);
	} // end assignIntelligence()
	
	public static void assignAthleticism (Person attr) {
		// Generate a random athletic value for the person. Currently using a Gaussian distribution.
		double ath_val = ValidationTools.clipValueToRange(Distribution.normal(0.5, 0.1), 0.0, 1.0);
		attr.setAthleticism(ath_val);
	} // end assignAthleticism()
	
	
	/*
	public static void assignCareer (Person attr) {
		
		// If the person's age and/or personality have not yet been set, then exit, since both are required before assigning a career.
		if (ValidationTools.empty(attr.getAge()) || ValidationTools.empty(attr.getPersonality())) {
			System.err.println("Attempting to assign career but personality has not been set.");
			return;
		} // end if (check if age or personality is not set)



		// If the person is not of working age, then exit. 
		//if (attr.age < Configuration.WorkingAgeMin || attr.age > Configuration.WorkingAgeMax) {
			//System.out.println(attr.age);
			//System.err.println("Attempting to assign career but age is not valid for working.");
			//return;
		//} // end if (check if working age)
		
		
		
		
		double[] traits = attr.getPersonality();
		int numFuzzyTraits = ArrayTools.countOccurrencesInArray(traits, 0.5);
		int numTotalBinaryTraits = (int) Math.pow(2, numFuzzyTraits);
		int numTraits = traits.length;
		
		// The numTurns variable will represent how many sequences of 0s or 1s will have to be looped through.
		// It will start at 2, and may increase if there are multiple fuzzy traits.
		int numTurns = 2;
		int numPerTurn;
		int fillInVal;	// This will always be either 0 or 1, and will be set within the loops as it is used to fill in the fuzzy trait bins.
		int t;
		int m, n;
		int fuzzyBinIndex;
		//Integer[][] FuzzyBinaryCombinations = new Integer[numTraits][numTotalBinaryTraits];
		Integer[][] FuzzyBinaryCombinations = new Integer[numTotalBinaryTraits][numTraits];
		
		//DebugTools.printArray(traits);

		// ----------------------------------------------------------------------------
		// Begin section for determining all personality indexes.
		// ----------------------------------------------------------------------------
		
		// Loop through each trait to see which category or categories the person fits in.
		for (t = 0; t < numTraits; t++) {
			if (traits[t] == 0.0) {
				// If this trait is OFF.
				ArrayTools.fillRowWithValue(FuzzyBinaryCombinations, t, new Integer(0));
				//DebugTools.printArray(FuzzyBinaryCombinations);
			} else if (traits[t] == 1.0) {
				// If this trait is ON.
				ArrayTools.fillRowWithValue(FuzzyBinaryCombinations, t, new Integer(1));
				//DebugTools.printArray(FuzzyBinaryCombinations);
			} else {		// Rather than check if == 0.5, it is safer to use the else statement, since decimals are not stored accurately.
				// If this trait is both ON and OFF.
				numPerTurn = numTotalBinaryTraits / numTurns;

				fillInVal = 0;

				for (m = 0; m < numTurns; m++) {

					for (n = 0; n < numPerTurn; n++) {

						fuzzyBinIndex = m*numPerTurn + n;

						FuzzyBinaryCombinations[fuzzyBinIndex][t] = new Integer(fillInVal);
						
					} // end n (number per turn - the number of values of this value before the end of the turn)
					
					// Toggle between 0 and 1.
					if (fillInVal == 0) {
						fillInVal = 1;
					} else {
						fillInVal = 0;
					} // end if (toggle between 0 and 1)
					
					
				} // end m (turns - a turn is a sequence of 0s or 1s before toggling to the other; as in a truth table)
				
				// Each subsequent fuzzy option will require doubling the number of turns (or sequences of 0s or 1s).
				numTurns *= 2;
				
			} // end if-else (checking if value is 0, 1, or 0.5)
			
			
		} // end for t (traits)
		
		// ----------------------------------------------------------------------------
		// End section for determining all personality indexes.
		// ----------------------------------------------------------------------------
		
		
		
		// ----------------------------------------------------------------------------
		// Begin section for gathering all possible careers from personalities.
		// ----------------------------------------------------------------------------
		ArrayList fuzzyCareerOptionsCombined = new ArrayList();

		int j, k;
		int binNum;
		int careersGroupNum;
		Integer[] binaryNumArr;
		ArrayList careersForPersonality;

		// Loop through all possible personality profiles that apply to this person (multiple ones when person has 0.5 for traits).
		for (j = 0; j < numTotalBinaryTraits; j++) {
			binaryNumArr = FuzzyBinaryCombinations[j];
			binNum = NumericTools.convertBinIntArrayToDecInt(binaryNumArr);

			// Extract array of careers from the personality indexed at binNum. The indices correlate to personality trait sequences.
			careersForPersonality = careersByPersonality[binNum];
			careersGroupNum = careersForPersonality.size();
			
			// Get each career from local array and add to combined, larger array.
			for (k = 0; k < careersGroupNum; k++) {
				fuzzyCareerOptionsCombined.add(careersForPersonality.get(k));
			} // end for k (loop through all careers in the personality-career group)
		} // end for j (number of binary personality options)

		// ----------------------------------------------------------------------------
		// End section for gathering all possible careers from personalities.
		// ----------------------------------------------------------------------------

		//DebugTools.printArray(careersTable);
		//DebugTools.printArray(fuzzyCareerOptionsCombined);

		// ----------------------------------------------------------------------------
		// Begin section to determine the total number of career openings for person.
		// ----------------------------------------------------------------------------
		int totalNumOpenings = 0;
		int c;
		int numPossibleCareers = fuzzyCareerOptionsCombined.size();
		String careerEntry;
		ArrayList matchCareer;
		Integer numOpeningsInt;
		int numOpenings;
		ArrayList careerProbabilities = new ArrayList();	// This will store all career options, including duplicates (if applicable) so that a random career will be chosen from here!
		
		for (c = 0; c < numPossibleCareers; c++) {
			careerEntry = (String)fuzzyCareerOptionsCombined.get(c);
			matchCareer = Careers.getCareerById(careersTable, careerEntry);

			numOpeningsInt = (Integer)matchCareer.get(Careers.CareerTable_Num);
			numOpenings = numOpeningsInt.intValue();
			totalNumOpenings += numOpenings;
			
			// While looping through these careers, we will set up the probabilities here too, rather than have a redundant second loop for this.
			for (k = 0; k < numOpenings; k++) {
				careerProbabilities.add(careerEntry);
			} // end for k (loop through number of openings for the c'th career)
			
		} // end for c (looping through all possible career options for person)
		// ----------------------------------------------------------------------------
		// End section to determine the total number of career openings for person.
		// ----------------------------------------------------------------------------



		// Finally, select a career for the person.
		String careerGlobalTableID;
		if (totalNumOpenings == 0) {	// If there are no possible openings for the careers that match this person's personalities.
			
			// This means Unemployed! If there is a safer way to indicate this, then update this.
			careerGlobalTableID = "00000";
			
		} else {						// If there are career openings for this person.
			
			// Choose random career from list of possible careers,
			careerGlobalTableID = (String)Distribution.uniformRandomObject(careerProbabilities);

		} // end if-else (checking if there are career openings for this person)

		attr.setCareer(careerGlobalTableID);

	} // end assignCareer()
	*/
	
	
	public static void assignCareer0 (Person attr) {
		// This method of assigning a career loops through every possible career, as read in from the Careers XML file, and looks at which traits correspond to the
		// careers, and calculate how well the given person matches the required traits.  If a trait is required (has a "req" attribute in the file), then if the
		// person does not match this trait, the hasUnmatchedRequiredTrait flag is set to true indicating that this career is an invalid option for this person.
		// The probability (weight) of the person taking a career is the product of the trait match value for that career and the number of job openings for that career
		// in the society. After looping through all the careers, a random career is selected from the array of normalized weights (scaled so the total sum is 1). 
		// 
		// An alternate way to choose a career is to select the one with the highest weight. For now, the randomizer works well.
		//
		// param attr: the Person for whom to select a career matching their personality and other attributes
		
		
		// If the person's age and/or personality have not yet been set, then exit, since both are required before assigning a career.
		if (ValidationTools.empty(attr.getAge()) || ValidationTools.empty(attr.getPersonality())) {
			System.err.println("Attempting to assign career but personality has not been set.");
			return;
		} // end if (check if age or personality is not set)


		// ----------------------------------------------------------------------------------------------------------------------------------------------------
		// NEW
		// ----------------------------------------------------------------------------------------------------------------------------------------------------
		
		
		// ------------------------------------------------
		// CAREERS
		// ------------------------------------------------
		int c, t;
		ArrayList career;
		ArrayList careerTraits;
		double[] traitMatches;

		double p_inCareer;
		String[] careerTraitInfo;
		String careerTraitReq;
		boolean hasUnmatchedRequiredTrait = false;
		double[] careerProbs = new double[Careers.getFullCareersDatabase().size()];
		ArrayList<Object> matchCareer;
		double[] numOpenings = new double[Careers.getFullCareersDatabase().size()];
				
		for (c = 0; c < Careers.getFullCareersDatabase().size(); c++) {
			career = (ArrayList<Object>)Careers.getFullCareersDatabase().get(c);

			// Get set of traits that correspond to career.
			careerTraits = (ArrayList<String[]>)career.get(Careers.Career_Traits);
			// Create array to store all calculated trait matches.
			traitMatches = new double[careerTraits.size()];
			// Get number of openings for this career.
			matchCareer = Careers.getCareerById(careersTable, (String)career.get(Careers.CareerTable_ID));
			numOpenings[c] = (double)((Integer)(matchCareer.get(Careers.CareerTable_Num))).intValue();

			hasUnmatchedRequiredTrait = false;

			for (t = 0; t < careerTraits.size(); t++) {
				careerTraitInfo = (String[])careerTraits.get(t);
				traitMatches[t] = TraitMatcher.calculateTraitMatch(attr, careerTraitInfo);
				careerTraitReq = careerTraitInfo[2]; // Remember that clubTraitReq is of the form: [traitType, traitValue, traitReq]
				// Check if trait is required, and if so, check if it matches the required condition (from the XML file). If not, then the career is invalid for the person.
				if (!careerTraitReq.isEmpty() && !ValidationTools.parseAndCheckCondition(traitMatches[t], careerTraitReq)) {
					hasUnmatchedRequiredTrait = true;
					break; // Don't bother looping through following traits for this club - since this required one is false, it is pointless to check other conditions)
				} // end if (check whether or not the trait is required and if so, if it is matched)
			} // end for t (loop through all traits corresponding to this club)

			p_inCareer = TraitMatcher.calculateTotalProbability(traitMatches, hasUnmatchedRequiredTrait);
			careerProbs[c] = p_inCareer; // Store probability in array.

		} // end for c (loop through all careers)


		// For just choosing the maximum probability career, not taking into account the number of openings.
		//double[] maxCareer = ArrayTools.getMaximumItemAndIndex(careerProbs);		// Currently returns an array: [maxProbability, maxProbIndex].
		//int careerIndex = (int)maxCareer[1];
		//ArrayList selectedCareer = (ArrayList)Careers.getFullCareersDatabase().get(careerIndex);
		//System.out.println("Found maxValue = " + maxCareer[0] + " at position = " + maxCareer[1] + " .. " + careerIndex + " | " + selectedCareer.get(1));

		
		// Multiply number of career openings by the probability of this person having the career.
		double[] weightedProbabilities = ArrayTools.multiplyElementWise(careerProbs, numOpenings);
		
		
		// Scale down probabilities so they are weights (the sum of all probabilities is 1.0).
		weightedProbabilities = ArrayTools.scaleAsWeights(weightedProbabilities);
		//System.out.println(ArrayTools.sum(careerProbs));
		//DebugTools.printArray(numOpenings);
		//DebugTools.printArray(careerProbs);
		//System.out.println(ArrayTools.sum(scaledProbs));
		//DebugTools.printArray(weightedProbabilities);
		
		int randCareer = Distribution.custom(weightedProbabilities);
		ArrayList selectedCareer = (ArrayList)Careers.getFullCareersDatabase().get(randCareer);
		//System.out.println("randomly selected " + randCareer + "  " + (String)selectedCareer.get(Careers.Career_Title) + " ~~~~~ " + Configuration.ReligionLabels[attr.religionIndex]);
		
		
		//System.out.println("selectedCareer = " + selectedCareer);
		
		
		// Retrieve career ID from selected career, and assign to person's careerPathID member.
		attr.setCareer((String)selectedCareer.get(Careers.Career_ID));
		//attr.careerPathID = "00000";

		
		// ----------------------------------------------------------------------------------------------------------------------------------------------------
		// END NEW
		// ----------------------------------------------------------------------------------------------------------------------------------------------------
		
		

	} // end assignCareer()
	
	
	public static void assignCurrentPosition (Person attr) {

		if (ValidationTools.empty(attr.getIsInSchool()) || ValidationTools.empty(attr.getCareer())) {
			System.err.println("Attempting to assign current position but isInSchool has not been set.");
			return;
		} // end if (ensure prerequisite information has been set)
		
		String currPosition = "uninitialized role";
		
		//System.out.println("is in school == " + attr.getIsInSchool());
		
		
		// DEATH. Check this first to avoid doing unnecessary work on dead persons.
		if (!attr.getIsAlive()) {
			System.err.println("Person " + attr.getID() + " has passed away.");
			attr.setCurrentPosition( Person.CURR_POSITION_DEAD );
			return;
		} // end if (check if person is dead)
		
		
		// BEFORE SCHOOL - TODDLER.
		if (attr.getAge() < Configuration.SchoolStartAge) {
			attr.setCurrentPosition( Person.CURR_POSITION_CHILD );
			return;
		} // end if (check if person is too young for school or work)

		
		// IN SCHOOL - STUDENT.
		if (attr.getIsInSchool()) {
			attr.setCurrentPosition( Person.CURR_POSITION_STUDENT );
			return;
		} // end if (check if person is in school)
		
		
		// AFTER SCHOOL - WORKING, UNEMPLOYED, OR RETIRED.
		if (attr.getAge() > Configuration.WorkingAgeMax) {
			currPosition =  Person.CURR_POSITION_RETIRED;
		} else {
			currPosition = Person.CURR_POSITION_UNEMPLOYED; // DEFAULT TO UNEMPLOYED UNTIL THEY FIND A JOB.
			
			//if (attr.getCareer().equals("00000")) {	// Career position for "Unemployed" {This may change to not be under the "career" heading!}
			//	currPosition = Person.CURR_POSITION_UNEMPLOYED;
			//} else {
			//	currPosition = Person.CURR_POSITION_WORKING;
			//} // end if (check if career is unemployed or actual career)
		} // end if (check if person is working, unemployed, or retired)

		attr.setCurrentPosition(currPosition);
	} // end assignCurrentPosition()
	
	
	
	
	public static void assignIncome (Person attr) {
		// Assign personality traits to this person. Currently use the MBTI model with 4 traits, and use ternary values (0, 0.5, or 1).
		// Modifies attributes: income
		
		// If the person does not have a career assigned, then exit the function, as income is based on career.
		if (ValidationTools.empty(attr.getCareer())) {
			System.err.println("Attempting to assign income but careerPathID has not been set.");
			return;
		}

		// If person is not old enough yet to work, then don't assign an income. They can wait until they actually being working to have an income!
		if (attr.getAge() < Configuration.WorkingAgeMin) {
			// If too young to be working, then default their income to 0.
			attr.setIncome(0);
			return;
		} // end if (check if working age)
		
		
		
		ArrayList careerInfo = Careers.getCareerById(careersDatabase, attr.getCareer());
		
		// Get the base income associated with the person's career.
		int careerIncome = Integer.parseInt((String)careerInfo.get(Careers.Career_Salary));
		// Randomly choose an amount (positive or negative) to offset the person's income from the base.
		int salarySTD = Distribution.uniform(-Configuration.SalarySTD, Configuration.SalarySTD);
		
		// debug
		if ((careerIncome + salarySTD) < 6000) {
			System.err.println("ERROR CALCULATING INCOME: " + careerIncome + ", " + salarySTD);
		}
		// debug
		
		// Set the income.
		attr.setIncome(careerIncome + salarySTD);
	} // end assignIncome()
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void assignEducation (Person attr) {
		// Assign the appropriate education level to the person, based on the career or societal role they are given. This will be important
		// when determining work history, and especially for keeping track of education for immigrants who did not go to school in this society.
		// Modifies attributes: isInSchool, education, educationPSYears
		
		// Ensure that the person's birth year has been set.
		if (ValidationTools.empty(attr.getAge()) || ValidationTools.empty(attr.getCareer())) {
			System.err.println("Attempting to assign education but age or careerID have not been set.");
			return;
		}
		
		// Determine from hometownHistory when the person went to post-secondary school.
		ArrayList careerInfo = Careers.getCareerById(careersDatabase, attr.getCareer());
		String reqEduType = (String)careerInfo.get(Careers.Career_ReqEducation);
		int reqEduYears = Integer.parseInt((String)careerInfo.get(Careers.Career_ReqSchoolYears));
		
		// Indicate whether or not person is currently in school.
		if (attr.getAge() < Configuration.SchoolFinishAge + reqEduYears) {
			// If peron's age is less then the age they will be in their final year of school (including P.S. school), then they are in school now.
			attr.setIsInSchool(true);
		} else {
			// Otherwise, they have finished school.
			attr.setIsInSchool(false);
		} // end if (check if person is finished all school)

		
		int year_startPSSchool = attr.getYearBorn()+Configuration.SchoolFinishAge;		// NOTE - randomize to add possible year "off" before starting P.S. school.
		int year_finishPSSchool = year_startPSSchool + reqEduYears;
		
		attr.setEducation(reqEduType);
		attr.setPSEducationYears(reqEduYears);
		attr.setPSStartYear(year_startPSSchool);
		attr.setPSFinishYear(year_finishPSSchool);
		
	} // end assignEducation()
	
	
	
	public static void assignHometownHistory (Person attr) {
		// Determine where the person lived throughout their life, and add each of the location periods to the hometown archive.
		// Modifies attributes: yearStartedPSSchool, yearFinishedPSSchool, hometownHistory, soc_HometownHistory
		
		ActivityArchive homeHistory = new ActivityArchive();	// Entire archive.
		ActivityArchive socHomeHistory = new ActivityArchive();	// Only in society.
		
		// Ensure that the person's birth year has been set.
		if (ValidationTools.empty(attr.getYearBorn()) || ValidationTools.empty(attr.getAge()) || ValidationTools.empty(attr.getNationality()) || ValidationTools.empty(attr.getEducation()) || ValidationTools.empty(attr.getPSEducationYears())) {
			System.err.println("Attempting to assign hometownHistory but birthYear, age, nationality, education, or educationPSYears have not been set.");
			return;
		} // end if (check if prerequiste information has been set)

		// If person has not yet started school, then leave function now. *NOTE * Assume no moves during these first few years - but this MAY change, based on parent's hometowns!!!
		if (attr.getAge() < Configuration.SchoolStartAge) {
			//System.err.println("Assuming child did not move during first few years. Be sure to modify this based on parents' hometowns! It should also apply to students up to 17 years old!");

			//BSActivityArchive.addHometownsForPeriod("", homeHistory, socHomeHistory, attr.getNationality(), 0.7, 0, 0, attr.getYearBorn(), attr.getYearBorn()+attr.getAge(), Configuration.AllCities);
			ActivityArchive.addHometownsForPeriod(homeHistory, socHomeHistory, attr.getYearBorn(), attr.getYearBorn()+attr.getAge(), Configuration.AllCities);
			
			return;
		} // end if (person is too young to have started school)

		
		// -----------------------------------------------------------------------------------------
		// Childhood.
		// -----------------------------------------------------------------------------------------
				
		assignHometown_ChildhoodYears(attr, homeHistory, socHomeHistory);

		// -----------------------------------------------------------------------------------------
		// Post-Secondary.
		// -----------------------------------------------------------------------------------------
				
		assignHometown_PostSecondaryYears(attr, homeHistory, socHomeHistory);

				
		// -----------------------------------------------------------------------------------------
		// After all school.
		// -----------------------------------------------------------------------------------------
					
		assignHometown_AfterAllSchoolYears(attr, homeHistory, socHomeHistory, Configuration.SocietyYear);

			

		
		//DebugTools.printArray(homeHistory);
		
		// Merge consecutive hometown entries of the same location.
		homeHistory.patchSequentialEntries();
		socHomeHistory.patchSequentialEntries();
		
		//System.out.println(attr.education + "  " + attr.educationPSYears);
		//DebugTools.printArray(homeHistory);
		//System.out.println("==================");
		//DebugTools.printArray(socHomeHistory);
		
				
		// Assign person attribute values.
		attr.setHometownHistory(homeHistory);
		attr.setSocietalHometownHistory(socHomeHistory);
		

		
		//System.err.println("NOTE TO SELF: Remember to also account for soc_hometownHistory in assignHometownHistory()");
		
		
		
		//DebugTools.printArray(homeHistory);

	} // end assignHometownHistory()

	public static void assignHometownHistory_CP (Person attr) {
		// Determine where the person lived throughout their life, and add each of the location periods to the hometown archive.
		// This method for assigning hometowns uses "checkpoints" which the other assignHometownHistory() does not do.
		// NOTE:	This is a simplified version of checkpoints. There is at most one period of time where the person is not already
		//			given a hometown. The length of years varies, but there will still only be one period. Thus, find the start and
		//			end year of that period, and then fill in that gaps with the appropriate age range methods.
		// Modifies attributes: yearStartedPSSchool, yearFinishedPSSchool, hometownHistory, soc_HometownHistory
		
		ActivityArchive homeHistory = new ActivityArchive();	// Entire archive.
		ActivityArchive socHomeHistory = new ActivityArchive();	// Only in society.
		
		// Ensure that the person's birth year has been set.
		if (ValidationTools.empty(attr.getYearBorn()) || ValidationTools.empty(attr.getAge()) || ValidationTools.empty(attr.getNationality()) || ValidationTools.empty(attr.getEducation()) || ValidationTools.empty(attr.getPSEducationYears())) {
			System.err.println("Attempting to assign hometownHistory but birthYear, age, nationality, education, or educationPSYears have not been set.");
			return;
		} // end if (check if prerequiste information has been set)

		ActivityArchive checkpointHometowns = attr.getHometownCheckpoints();
		
		// Flags for determining which section(s) to fill in. Default all to True because if no checkpoints are set, then all sections
		// must be filled in. If there are checkpoints, then some flags will be set to False based on those checkpointed periods.
		boolean[] gapsNeedFilling = new boolean[] {true, true, true}; // There are 3 different periods: {Childhood, Post-Seconday, and After All School} 
		
		boolean copyCheckpointsToEnd = true;

		// If person has some checkpointed hometowns from marriage (living with spouse) or as child (living with parents).
		if (!checkpointHometowns.isEmpty()) {
			
			
			Object homeAtBirth = checkpointHometowns.getActivityAtYear( attr.getYearBorn() );
			//System.out.println(homeAtBirth);
			if (homeAtBirth == null) {
				// Check if person has no recorded hometown at birth (i.e. this is a MARRIED COUPLE).
				//System.err.println("Married couple. Assign early years to them. [" + attr.getYearBorn() + "," + attr.getRelationshipStartYear() + "].");
				//fillInAllChildhoodHometowns = true;
				//System.out.println("before: " + gapsNeedFilling[0] + "," + gapsNeedFilling[1] + "," + gapsNeedFilling[2] + "]");
				determineCheckpointPeriodGapYears(gapsNeedFilling, false, attr);
				
				copyCheckpointsToEnd = true; // This must be True for people who need their latter years (married folk) appended to their main hometown history.
				
				//System.out.println("person age AS MARRIED " + attr.getAge());
				
				//System.out.println("after:  " + gapsNeedFilling[0] + "," + gapsNeedFilling[1] + "," + gapsNeedFilling[2] + "]");
			} else {
				// Check if person has a recorded hometown at birth (i.e. this is a CHILD).

				determineCheckpointPeriodGapYears(gapsNeedFilling, true, attr);
				
				copyCheckpointsToEnd = false;
				
				
			} // end if (check whether or not person needs early years filled in or later years filled in)

		} // end if (check if person has checkpoints)

		// -----------------------------------------------------------------------------------------
		// Childhood.
		// -----------------------------------------------------------------------------------------
		if (gapsNeedFilling[0]) {
			assignHometown_ChildhoodYears(attr, homeHistory, socHomeHistory);
		}

		// -----------------------------------------------------------------------------------------
		// Post-Secondary.
		// -----------------------------------------------------------------------------------------
		if (gapsNeedFilling[1]) {
			assignHometown_PostSecondaryYears(attr, homeHistory, socHomeHistory);
		}

		
		// -----------------------------------------------------------------------------------------
		// After all school.
		// -----------------------------------------------------------------------------------------
		
		// gapsNeedFilling[2] is the flag for the working period (after all school). If True, fill it completely to current year.
		// If False, fill it until year married, since the rest is already filled in from the marriage.
		if (gapsNeedFilling[2]) {
			assignHometown_AfterAllSchoolYears(attr, homeHistory, socHomeHistory, Configuration.SocietyYear);
		} else {
			assignHometown_AfterAllSchoolYears(attr, homeHistory, socHomeHistory, attr.getRelationshipStartYear());
		} // end if (check whether or not person is married, to indicate whether or not the entirety of this period needs to be filled) 
		


		
		// -----------------------------------------------------------------------------------------
		// Copy checkpointed hometowns to main hometown archive.
		// -----------------------------------------------------------------------------------------
		if (copyCheckpointsToEnd) {
			
			// Append checkpoint hometowns (from marriage) to end of main hometown array.
			int i;
			Dictionary<Object, int[]> activity;
			String activityLocation;
			for (i = 0; i < checkpointHometowns.size(); i++) {
				activity = checkpointHometowns.get(i);
				activityLocation = (String)checkpointHometowns.getDictEntryNameAndYears(i).get(0); // get(0) means the String of the location.
				
				homeHistory.add(activity);
				
				
				
				if (activityLocation.equals(Configuration.SocietyName)) {
					
					socHomeHistory.add(activity);
				} // end if (check if local)
				
			} // end for i (loop through checkpoint activities to copy to main archive)
			
		} else {
			
			// Prepend checkpoint hometowns (from childhood) to end of main hometown array.
			
			ActivityArchive tmpCopyArchive = new ActivityArchive();
			
			// First add checkpoint entries to tmp archive.
			int i;
			Dictionary<Object, int[]> activity;
			String activityLocation;
			for (i = 0; i < checkpointHometowns.size(); i++) {
				activity = checkpointHometowns.get(i);
				activityLocation = (String)checkpointHometowns.getDictEntryNameAndYears(i).get(0); // get(0) means the String of the location.
				
				tmpCopyArchive.add(activity);
				
				// If local, directly add to the local archive now.
				if (activityLocation.equals(Configuration.SocietyName)) {
					socHomeHistory.add(activity);
				} // end if (check if local)
				
			} // end for i (loop through checkpoint activities to copy to main archive)

			// Then add the main archive entries to the tmp archive.
			for (i = 0; i < homeHistory.size(); i++) {
				activity = homeHistory.get(i);
							
				tmpCopyArchive.add(activity);
			} // end for i (loop through checkpoint activities to copy to main archive)
			
			

			
			// Copy the temporary archive to the main archive.
			homeHistory = (ActivityArchive)tmpCopyArchive.clone();

			
			// Delete temporary archive now to free memory. No longer needed.
			tmpCopyArchive = null;

			
			
		} // end if (check if checkpointed hometowns are appended or prepended)
		
		
		
		
		
		// Merge consecutive hometown entries of the same location.
		homeHistory.patchSequentialEntries();
		socHomeHistory.patchSequentialEntries();
		
		
		// Assign person attribute values.
		attr.setHometownHistory(homeHistory);
		attr.setSocietalHometownHistory(socHomeHistory);


	} // end assignHometownHistory_CP()
	
	private static void determineCheckpointPeriodGapYears (boolean[] gapsNeedFilling, boolean isChild, Person attr) {
		// This method will set the boolean flags in the gapsNeedFilling array to indicate which time periods need to be
		// examined and filled with hometowns. Both children and married people will have checkpoints, so both will use
		// this method to determine which periods need to be used.
		//
		// param gapsNeedFilling: a boolean array of size 3, with flags for each of the time periods. These get set in here!
		// param isChild: a boolean flag indicating True if this is a child and False if this is a married person (the only two options).
		// param attr: the Person object for whom the work is being done
		//
		// No return, but the gapsNeedFilling array gets assigned values in here.
	
		//System.err.println("AttributeAssigner->determineCheckpointPeriodGapYears(); fill in the method!!!");
		//childhoodYears = true;
		
		int CHILDHOOD = 0;
		int POST_SECONDARY = 1;
		int WORKING = 2;
		
		
		
		
		if (isChild) {
			// * CHILD * with given childhood years - fill in later years
			
			// None of the childhood needs to be filled in, as this is all copied directly from the parents.
			gapsNeedFilling[CHILDHOOD] = false;
			
			// If child has finished secondary school, then the post-secondary portion needs to be worked on.
			if (attr.getAge() >= Configuration.SchoolFinishAge) {
				gapsNeedFilling[POST_SECONDARY] = true;
			} // end if (check if child is finished elementary (including secondary) school)
			
			// If child is not in school (i.e. finished all education), then fill in the work portion too.
			if (!attr.getIsInSchool()) {
				gapsNeedFilling[WORKING] = true;
			} // end if (check if child is beyond all education years)
				
				//System.out.println("THIS IS A CHILD!!!!!!!!!!!!!!!");
				
		} else {
			// * MARRIED PERSON* with given marriage years - fill in early years
			gapsNeedFilling[CHILDHOOD] = true;
			gapsNeedFilling[POST_SECONDARY] = true;
			gapsNeedFilling[WORKING] = false;
			//fillChildhoodYears = true;
			//fillPSYears = true;
			
		}
		
		//childhoodYears[0] = 1900;
		//childhoodYears[1] = 1905;
	} // end determineCheckpointPeriodGapYears()
	
	private static void assignHometown_ChildhoodYears (Person attr, ActivityArchive homeHistory, ActivityArchive socHomeHistory) {

		int finalElemSchoolYear;
		if (Configuration.SocietyYear > attr.getYearBorn()+Configuration.SchoolFinishAge) {
			finalElemSchoolYear = attr.getYearBorn()+Configuration.SchoolFinishAge;
		} else {
			finalElemSchoolYear = attr.getYearBorn()+attr.getAge();
			//System.err.println("Assuming child did not move during first few years. Be sure to modify this based on parents' hometowns! It should also apply to students up to 17 years old!");
		} // end if (check if person has finished elementary school)
		
		//System.out.println(Configuration.SocietyYear + "  " + (attr.yearBorn+Configuration.SchoolFinishAge) + " || " + finalElemSchoolYear);

		//BSActivityArchive.addHometownsForPeriod("", homeHistory, socHomeHistory, attr.getNationality(), 0.7, 1, 2, attr.getYearBorn(), finalElemSchoolYear, Configuration.AllCities);
		ActivityArchive.addHometownsForPeriod(homeHistory, socHomeHistory, attr.getYearBorn(), finalElemSchoolYear, Configuration.AllCities);

	} // end assignHometown_ElementaryYears()
	
	private static void assignHometown_PostSecondaryYears (Person attr, ActivityArchive homeHistory, ActivityArchive socHomeHistory) {
		//System.err.println("MADE IT TO assignHometownHistory()");
				// If person is not finished elementary school yet, then skip this portion. 
				if (attr.getAge() >= Configuration.SchoolFinishAge) {

					//System.err.print("MADE IT ***INSIDE*** assignHometownHistory() -- ");
					//System.err.println(attr.getAge() + " | " + attr.getCareer() + " <> "+ attr.getPSEducationYears() + " || " + attr.getEducation());
					
					int year_startPSSchool = 0;
					int year_finishPSSchool = 0;
				
					if (attr.getEducation().equals("C") || attr.getEducation().equals("U")) {	// If person needs college or university education.
						year_startPSSchool = attr.getYearBorn()+Configuration.SchoolFinishAge;		// NOTE - randomize to add possible year "off" before starting P.S. school.
						year_finishPSSchool = year_startPSSchool + attr.getPSEducationYears();
					
						// If person has not yet finished post-secondary school, then use current year as current final year.
						if (attr.getAge() < (Configuration.SchoolFinishAge+attr.getPSEducationYears())) {
							year_finishPSSchool = Configuration.SocietyYear;
						} else {
							year_finishPSSchool = year_startPSSchool + attr.getPSEducationYears();
						} // end if (person is currently in post-secondary school)
					
						//System.err.println("> MADE IT *WITHIN* TO assignHometownHistory() <");
					
						String psSchoolType = attr.getEducation();
						//String[] citiesWithSchoolsByType = ArrayTools.arrayListToStringArray(Schools.getSchoolsByFilters(Schools.getFullPostSecondarySchoolsDatabase(), new int[] {Schools.School_Type}, new Object[] {psSchoolType}, new int[] {Schools.School_City}));
						//String[] citiesWithSchoolsByType = ArrayTools.arrayListToStringArray(InstitutionSet.getInstitutionsByFilters(InstitutionSet.PostSecondarySchoolsList, new int[] {InstitutionSet.Institution_Subtype}, new Object[] {psSchoolType}, new int[] {InstitutionSet.Institution_City}));
						ArrayList<String> citiesWithSchoolsByType = InstitutionSet.getInstitutionsByFilters(InstitutionSet.PostSecondarySchoolsList, new int[] {InstitutionSet.Institution_Subtype}, new Object[] {psSchoolType}, new int[] {InstitutionSet.Institution_City});

						//BSActivityArchive.addHometownsForPeriod("", homeHistory, socHomeHistory, (String)homeHistory.getLastActivityName(), 0.7, 1, 1, year_startPSSchool, year_finishPSSchool, citiesWithSchoolsByType);
						ActivityArchive.addHometownsForPeriod(homeHistory, socHomeHistory, year_startPSSchool, year_finishPSSchool, citiesWithSchoolsByType);
					} // end if (check if post-secondary school is required)
				

					//int actualFinalSchoolYear = year_startPSSchool + attr.getPSEducationYears();
					
					
					
					
				} // ?????
	} // end assignHometown_PostSecondaryYears()
	
	private static void assignHometown_AfterAllSchoolYears (Person attr, ActivityArchive homeHistory, ActivityArchive socHomeHistory, int lastYearToAdd) {
		
		// If person is not finished elementary school yet, then skip this portion. 
		if (attr.getAge() >= Configuration.SchoolFinishAge) {
			
			int year_startPSSchool = 0;
			int actualFinalSchoolYear = 0;
			
			if (!attr.getIsInSchool()) {

				year_startPSSchool = attr.getYearBorn()+Configuration.SchoolFinishAge;		// NOTE - randomize to add possible year "off" before starting P.S. school.
				actualFinalSchoolYear = year_startPSSchool + attr.getPSEducationYears();
				
				int yearStartingWork;
				if (attr.getPSEducationYears() == 0) {
					// If the person did not attend post-secondary education, then the year they finished elementary (secondary) school is the year they can begin working.
					yearStartingWork = attr.getYearBorn() + Configuration.SchoolFinishAge;
				} else {
					// If person went to post-secondary school, then the year they finished that education is the year they can begin working.
					yearStartingWork = actualFinalSchoolYear;
				} // end if (determining when the person starting working)
				//int numYearsWorking = Configuration.SocietyYear - yearStartingWork;
				//System.out.println(year_startPSSchool + "/" + actualFinalSchoolYear + "  Working for " + numYearsWorking + " years.");
	
	

	
				//BSActivityArchive.addHometownsForPeriod("", homeHistory, socHomeHistory, (String)homeHistory.getLastActivityName(), 0.3, 1, 3, yearStartingWork, lastYearToAdd, Configuration.AllCities);
				ActivityArchive.addHometownsForPeriod(homeHistory, socHomeHistory, yearStartingWork, lastYearToAdd, Configuration.AllCities);
			
			
			
			
				// Set years started and finished for Post-Secondary school.
				attr.setPSStartYear(year_startPSSchool);
				// Rather than use the year_finishPSSchool, go back to the original year they actually finish, so that running the simulation, it is recorded when the person will graduate (rather than being clipped to the current year).
				attr.setPSFinishYear(actualFinalSchoolYear);
			
			
			
			} // end if (check if person is done school)


		
		
		
		
		

	
		} // end if (person is finished elementary school)
		
	} // end assignHometown_PostSecondaryYears()
	
	
	
	
	
	
	public static void assignSchoolHistory (Person attr) {
		// Determine when person was living in the society during school years, and add each of those periods to the school archive.
		// Modifies attributes: schoolHistory, soc_SchoolHistory

		ActivityArchive schoolHistory = new ActivityArchive();		// Entire archive.
		ActivityArchive socSchoolHistory = new ActivityArchive();	// Only in society.
		
		// Ensure that the person's birth year has been set.
//System.out.println(ValidationTools.empty(attr.yearBorn) + " || " + ValidationTools.empty(attr.age) + " || " + ValidationTools.empty(attr.hometownHistory)  + " || " + ValidationTools.empty(attr.education) + " || " + ValidationTools.empty(attr.educationPSYears));
		if (ValidationTools.empty(attr.getYearBorn()) || ValidationTools.empty(attr.getAge()) || ValidationTools.empty(attr.getHometownHistory())  || ValidationTools.empty(attr.getEducation()) || ValidationTools.empty(attr.getPSEducationYears())) {
			System.err.println("Attempting to assign schoolHistory but birthYear, age, hometownHistory, education, or eduPSYears have not been set.");
			return;
		} // end if (ensure person has prerequisite information assigned)
		
		ArrayList<Integer> schoolYearsAnywhere = new ArrayList<Integer>();
		ArrayList<Integer> schoolYearsInSociety = new ArrayList<Integer>();

		// Check if person is old enough to have already started school.
		if (attr.getAge() < Configuration.SchoolStartAge) {
			//System.err.println("Attempting to assign schoolHistory but person is too young to attend school.");
			attr.setSchoolHistory(schoolHistory); // This is important so the child at least has an initialized archive.
			attr.setSocietalSchoolHistory(socSchoolHistory); // This is important so the child at least has an initialized archive.
			return;
		} // end if (check if old enough to at least have started school)

		// Determine age of person in their final year of school - or current age if they are still in school.
		int finalSchoolYearAge;
		if (attr.getAge() > Configuration.SchoolFinishAge) {
			finalSchoolYearAge = Configuration.SchoolFinishAge;
		} else {
			finalSchoolYearAge = attr.getAge();
		} // end if (determining if currently in school or finished school)

		//System.out.println(Configuration.SchoolStartAge + " -- " + finalSchoolYearAge);
		
		int y;
		int sch_year;
		String hometown;

		for (y = Configuration.SchoolStartAge; y < finalSchoolYearAge; y++) {

			// Get the actual year number.
			sch_year = attr.getYearBorn() + y;
			
			// Get activity from archive for the given year.
			// *** NOTE ***	This should be checked with someone who moves cities during their school years.
			// 				Should both locations be added or just one of the two?!
			hometown = (String)attr.getHometownHistory().getActivityAtYear(sch_year);

			// Determine if person was in the current society during this school year.
			if (hometown.equals(Configuration.SocietyName)) {
				// If the person lived in this society, then add year to array.
				schoolYearsInSociety.add(new Integer(sch_year));
			} // end if (person lived in this society)
			schoolYearsAnywhere.add(new Integer(sch_year));
		} // end for y (looping through all school years)

		
		String schoolName = "Elementary";
		// If array of schoolYearsInSociety is empty now, then exit function, because the person is either too young to attend school or lived in a different city during their school years.
		if (!schoolYearsInSociety.isEmpty()) {
			// Now that at this point, the person spent at least one year in this society during school years, determine what school(s) they attended.

			// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
			// *** NOTE *** THIS IS ASSUMING THE SCHOOL WAS IN SERVICE THROUGHOUT THEIR ENTIRE CHILDHOOD. THIS WILL NOT ALWAYS BE TRUE!
			// 				CURRENTLY ALL SCHOOLS ARE SET TO GO FROM 1900 TO PRESENT TO AVOID THIS PROBLEM, BUT THIS SHOULD BE MODIFIED
			//				SO IF A SCHOOL SHUTS DOWN, A NEW RANDOM SCHOOL IS CHOSEN FOR THE STUDENT.
			// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
			Integer firstSchoolYear = (Integer)schoolYearsInSociety.get(0);
			//System.out.println(firstSchoolYear);
			double[] schoolProbs = SchoolSet.getSchoolProbabilityTable(firstSchoolYear.intValue());

			// Assume each person only went to one school while living in this society. This could be expanded to allow school changes, but this is
			// not an important feature, as the school history is really only used in putting the person into groups for possible friend networking. 
			int rndSchoolIndex = Distribution.custom(schoolProbs);
			//addConsecutiveActivitySequences(schoolYearsInSociety, socSchoolHistory, Schools.getSchoolTitleAt(rndSchoolIndex));
			schoolName = InstitutionSet.getSchoolTitleAt(InstitutionSet.ElementarySchoolsList, rndSchoolIndex);
			addConsecutiveActivitySequences(schoolYearsInSociety, socSchoolHistory, schoolName);

		} // end if (check if array is empty)

		if (!schoolYearsAnywhere.isEmpty()) {
			//addConsecutiveActivitySequences(schoolYearsAnywhere, schoolHistory, "Elementary");
			addConsecutiveActivitySequences(schoolYearsAnywhere, schoolHistory, schoolName);
		} // end if (check if socSchoolhistory is empty)



		// ------------------------------------------------------------------------------------
		// Post-Secondary Education
		// ------------------------------------------------------------------------------------
		
		if (attr.getAge() >= Configuration.SchoolFinishAge) {
		
			int PSStartYear, PSEndYear;
			PSStartYear = attr.getPSStartYear();

			// Check if person is finished or currently enrolled in post-secondary school.
			if (attr.getPSFinishYear() < Configuration.SocietyYear) {
				PSEndYear = attr.getPSFinishYear();
			} else {
				PSEndYear = Configuration.SocietyYear;
			} // end if (check if person has started post-secondary school already)
		
		
		
			// * NOTE * This assumes that the person stayed at ONE post-secondary and lived in ONE city during that period. If the program is modified to allow
			//			moves to other cities and transfers to other institutions, then this part will have to be modified to allow those changes.
			//String PSLocation = (String)attr.hometownHistory.getActivityAtYear(PSStartYear+1); // Add 1 to start year because that year is also in the archive as the final year of elementary school.
			String PSLocation = (String)attr.getHometownHistory().getActivityAtYear(PSStartYear+1); // Add 1 to start year because that year is also in the archive as the final year of elementary school.
		
			//System.out.println(PSLocation);
		
			// * NOTE * This assumes the institutions were/are in service the whole time this simulation takes place. If this needs to be changed, then the school's time
			//			period would have to be examined to see if it was/is in existence during the person's required time in post-secondary school.
			//String[] schoolsByType = ArrayTools.arrayListToStringArray(Schools.getSchoolsByFilters(Schools.getFullPostSecondarySchoolsDatabase(), new int[] {Schools.School_Type, Schools.School_City}, new Object[] {attr.education, PSLocation}, new int[] {Schools.School_Name}));
			String[] schoolsByType = ArrayTools.arrayListToStringArray(InstitutionSet.getInstitutionsByFilters(InstitutionSet.PostSecondarySchoolsList, new int[] {InstitutionSet.Institution_Subtype, InstitutionSet.Institution_City}, new Object[] {attr.getEducation(), PSLocation}, new int[] {InstitutionSet.Institution_Name}));

			// Check if there were no post-secondary schools found for the person (either because they didn't require any or if they are too young yet).
			if (schoolsByType.length == 0) {
				///////
				// NOTE: the following inner if-statement block can be removed. It was a simple debugging check, but not necessary.
				///////
				if (attr.getPSEducationYears() > 0) {
					//System.err.println("In AttributeAssigner->assignSchoolHistory(), there were no post-secondary institutions for this person!");
					// Add post-secondary school to archive. Assume that attr.education is the type of school this school is (i.e. U or C)
					schoolHistory.addEntry(new String[] {attr.getEducation(), "External Institution"}, PSStartYear, PSEndYear);
				} else {
					// Do nothing. These people did not require post-secondary, so it won't find any for them!
				} // end if (check if person who did not receive an institution was supposed to attend one or not)
			} else {
				String institution = (String)Distribution.uniformRandomObject(schoolsByType);
				//System.out.println(institution);
			
				// Add post-secondary school to archive. Assume that attr.education is the type of school this school is (i.e. U or C)
				schoolHistory.addEntry(new String[] {attr.getEducation(), institution}, PSStartYear, PSEndYear);

				// If the person's post-secondary institution is in the society, then add it to the local archive.
				if (PSLocation.equals(Configuration.SocietyName)) {
					socSchoolHistory.addEntry(new String[] {attr.getEducation(), institution}, PSStartYear, PSEndYear);
					//socSchoolHistory.addEntry(institution, PSStartYear, PSEndYear);
				} // end if (institution location is in the local society)

			} // end if (no institutions were found for the given criteria)

		} // end if (person is done elementary school, and thus post-secondary school is assigned)

		//DebugTools.printArray(schoolHistory);
		
		attr.setSchoolHistory(schoolHistory);
		attr.setSocietalSchoolHistory(socSchoolHistory);

	} // end assignSchoolHistory()
	
	
	
	
	
	
	
	
	
	
	/*
	public static void assignSchoolHistory (PersonAttributes attr) {
		// Determine when person was living in the society during school years, and add each of those periods to the school archive.
		// Modifies attributes: schoolHistory, soc_SchoolHistory

		ActivityArchive schoolHistory = new ActivityArchive();		// Entire archive.
		ActivityArchive socSchoolHistory = new ActivityArchive();	// Only in society.
		
		// Ensure that the person's birth year has been set.
		if (ValidationTools.empty(attr.yearBorn) || ValidationTools.empty(attr.age) || ValidationTools.empty(attr.hometownHistory)  || ValidationTools.empty(attr.education) || ValidationTools.empty(attr.educationPSYears)) {
			System.err.println("Attempting to assign schoolHistory but birthYear, age, hometownHistory, education, or eduPSYears have not been set.");
			return;
		}
		
		ArrayList schoolYearsAnywhere = new ArrayList();
		ArrayList schoolYearsInSociety = new ArrayList();

		// Check if person is old enough to have already started school.
		if (attr.age < Configuration.SchoolStartAge) {
			System.err.println("Attempting to assign schoolHistory but person is too young to attend school.");
			return;
		} // end if (check if old enough to at least have started school)

		// Determine age of person in their final year of school - or current age if they are still in school.
		int finalSchoolYearAge;
		if (attr.age > Configuration.SchoolFinishAge) {
			finalSchoolYearAge = Configuration.SchoolFinishAge;
		} else {
			finalSchoolYearAge = attr.age;
		} // end if (determining if currently in school or finished school)

		int y;
		int sch_year;
		String hometown;

		for (y = Configuration.SchoolStartAge; y < finalSchoolYearAge; y++) {

			// Get the actual year number.
			sch_year = attr.yearBorn + y;

			// Get activity from archive for the given year.
			// *** NOTE ***	This should be checked with someone who moves cities during their school years.
			// 				Should both locations be added or just one of the two?!
			hometown = (String)attr.hometownHistory.getActivityAtYear(sch_year);

			// Determine if person was in the current society during this school year.
			if (hometown.equals(Configuration.SocietyName)) {
				// If the person lived in this society, then add year to array.
				schoolYearsInSociety.add(new Integer(sch_year));
			} // end if (person lived in this society)
			schoolYearsAnywhere.add(new Integer(sch_year));
		} // end for y (looping through all school years)

		// If array of schoolYearsInSociety is empty now, then exit function, because the person is either too young to attend school or lived in a different city during their school years.
		if (!schoolYearsInSociety.isEmpty()) {
			// Now that at this point, the person spent at least one year in this society during school years, determine what school(s) they attended.

			// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
			// *** NOTE *** THIS IS ASSUMING THE SCHOOL WAS IN SERVICE THROUGHOUT THEIR ENTIRE CHILDHOOD. THIS WILL NOT ALWAYS BE TRUE!
			// 				CURRENTLY ALL SCHOOLS ARE SET TO GO FROM 1900 TO PRESENT TO AVOID THIS PROBLEM, BUT THIS SHOULD BE MODIFIED
			//				SO IF A SCHOOL SHUTS DOWN, A NEW RANDOM SCHOOL IS CHOSEN FOR THE STUDENT.
			// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
			Integer firstSchoolYear = (Integer)schoolYearsInSociety.get(0);
			double[] schoolProbs = Schools.getSchoolProbabilityTable(firstSchoolYear.intValue());


			// Assume each person only went to one school while living in this society. This could be expanded to allow school changes, but this is
			// not an important feature, as the school history is really only used in putting the person into groups for possible friend networking. 
			int rndSchoolIndex = Distribution.custom(schoolProbs);
			addConsecutiveActivitySequences(schoolYearsInSociety, socSchoolHistory, Schools.getSchoolTitleAt(rndSchoolIndex));

		} // end if (check if array is empty)

		if (!schoolYearsAnywhere.isEmpty()) {
			addConsecutiveActivitySequences(schoolYearsAnywhere, schoolHistory, "Elementary");
		} // end if (check if socSchoolhistory is empty)

		// ------------------------------------------------------------------------------------
		// Post-Secondary Education
		// ------------------------------------------------------------------------------------
		ArrayList postSecondarySchoolYearsAnywhere = new ArrayList();
		ArrayList postSecondarySchoolYearsInSociety = new ArrayList();

		// Determine what type of post-secondary institution the person attended (College or University).
		String psSchoolName = "";
		if (attr.education.equals("C")) {
			psSchoolName = (String)Schools.getPSSchoolsByType("C").get(0);
		} else if (attr.education.equals("U")) {
			psSchoolName = (String)Schools.getPSSchoolsByType("U").get(0);
		} else {
			// Do nothing.
		} // end if (determining post-secondary institution type)

		psSchoolName = psSchoolName + "...";
		
		if (ValidationTools.empty(psSchoolName)) {
			System.err.println("In assignSchoolHistory(), there was an error finding a valid post-secondary institution.");
		} // end if (error with finding school name)

		
		//System.out.println("###------------------------------------------------------------------------###");
		//DebugTools.printArray(attr.hometownHistory);
		
		

		// If a student is finished post-secondary school, then find their age in their final year.
		// If they are currently in a post-secondary school program, then use current age. 
		int finalPSSchoolYearAge = finalSchoolYearAge + attr.educationPSYears;	// Age in student's final year of P.S. school.
		if (finalPSSchoolYearAge > attr.age) {		// If they are younger than the age they'll be in the final year, then use current age.
			finalPSSchoolYearAge = attr.age;

			// * NOTE * This could also be where 'isInSchool' is set to True, rather then in assignEducation().

		} // end if (checking if old enough to have finished P.S. school)
		
		
		// Loop through all required years of post-secondary institution to add to archives.
		for (y = finalSchoolYearAge; y < finalPSSchoolYearAge; y++) {
		//for (y = finalSchoolYearAge; y < (finalSchoolYearAge + attr.educationPSYears); y++) {

			// Get the actual year number.
			sch_year = attr.yearBorn + y;

			// Get activity from archive for the given year.
			// *** NOTE ***	This should be checked with someone who moves cities during their school years.
			// 				Should both locations be added or just one of the two?!
			hometown = (String)attr.hometownHistory.getActivityAtYear(sch_year);

			// Determine if person was in the current society during this school year.
			if (hometown.equals(Configuration.SocietyName)) {
				// If the person lived in this society, then add year to array.
				postSecondarySchoolYearsInSociety.add(new Integer(sch_year));
			} else {
				// If person lived in different society for school year.
				//postSecondarySchoolYearsAnywhere.add(new Integer(sch_year));
			} // end if (person lived in this society)
			postSecondarySchoolYearsAnywhere.add(new Integer(sch_year));

		} // end for y (looping through all school years)

		// If array of postSecondarySchoolYearsInSociety is empty now, then exit function, because the person is either too young to attend school or lived in a different city during their school years.
		if (!postSecondarySchoolYearsInSociety.isEmpty()) {
			addConsecutiveActivitySequences(postSecondarySchoolYearsInSociety, socSchoolHistory, psSchoolName);
		} // end if (check if array is empty)

		if (!postSecondarySchoolYearsAnywhere.isEmpty()) {
			addConsecutiveActivitySequences(postSecondarySchoolYearsAnywhere, schoolHistory, "Post Secondary");
		} // end if (check if socSchoolhistory is empty)
			


		// Assign schoolHistory array to person's attributes.
		attr.schoolHistory = schoolHistory;
		attr.soc_SchoolHistory = socSchoolHistory;
	} // end assignSchoolHistory()
*/




	private static void addConsecutiveActivitySequences (ArrayList<Integer> yearsInSociety, ActivityArchive archive, String schoolName) {
		// Loop through all years listed in schoolYearsInSociety array, and check for consecutive years so that each sequence of consecutive
		// years can be added to the schoolHistory archive.
		// param yearsInSociety: the arraylist of all individual years that person was in society as a student
					
		int i;
		int yr, prevYr;
		int consecutiveSequenceStartYear;
		int currLastYearInSequence;
		int numSchoolYearsInSociety = yearsInSociety.size();
		prevYr = Integer.parseInt(yearsInSociety.get(0).toString());
		consecutiveSequenceStartYear = prevYr; 		// This will hold the start year of a new sequence.
		currLastYearInSequence = prevYr; 			// This will hold the final year in the current sequence, which will be updated on at each iteration through the loop. At the end of a sequence, this will be hold the final year of that sequence.
					
		// If person was in society for one than one year.
		if (numSchoolYearsInSociety > 1) {


			// Start at index 1, because the index 0 year is dealt with before this loop as an initialization.
			for (i = 1; i < numSchoolYearsInSociety; i++) {
				yr = Integer.parseInt(yearsInSociety.get(i).toString());

				if (yr == prevYr + 1) {
					// If this year at index i is the consecutive year after the one at index i-1, then update the "finalYear" in the sequence.
					currLastYearInSequence = yr;
				} else {
					// End previous sequence. The last sequence is finished now, so add that entry to the array.
					archive.addEntry(new String[] {"Elementary", schoolName}, consecutiveSequenceStartYear, currLastYearInSequence);
								
					// Create new sequence.
					consecutiveSequenceStartYear = yr;
					currLastYearInSequence = yr;
				} // end if (two years are consecutive)
							
				// Keep track of previous year.
				prevYr = yr;
			} // end for i (loop through schoolYearsInSociety array)

			archive.addEntry(new String[] {"Elementary", schoolName}, consecutiveSequenceStartYear, currLastYearInSequence);

		} // end if (more than 1 year in array)

	} // end addConsecutiveActivitySequences()
	
	
	
	
	
	
	public static void assignWorkHistory (Person attr) {
		// Determine all places the person has worked throughout their life, and add each of those work periods to the work archive.
		// Modifies attributes: workHistory, soc_WorkHistory
		
		// * NOTE: For now, just put person into current career, such that they began after finishing school and held same job to present.
		// This will change eventually so that they have a series of different jobs throughout their life.
		
		ActivityArchive workHistory = new ActivityArchive();
		ActivityArchive societalWorkHistory = new ActivityArchive();
		
		if (ValidationTools.empty(attr.getAge()) || ValidationTools.empty(attr.getCareer()) || ValidationTools.empty(attr.getSchoolHistory())) {
			// NOTE: Do not bother displaying message, because young children will not have school history yet, so no need to display error messages for them!
			//System.err.println("Attempting to assign workHistory but age, careerID, or schoolHistory have not been set.");
			return;
		} // end if (check if person has the prerequisite information to get a work history) 

		// Determine if person is currently in school. Assume they have no work until after done school (part-time work would have to be different. Ignore that for now!)
		if (attr.getIsInSchool()) {
			// Rather than having a null archive, we want to set the empty (but initialized!) archives here before returning out.
			attr.setIncome(0);
			attr.setWorkHistory(workHistory);
			attr.setSocietalWorkHistory(societalWorkHistory);
			return;
		} // end if (person is in school currently)
		
		//System.err.println("88888888888888888888888888888888888888      " + attr.getYearBorn() + " (" + attr.getAge() + ").");
		
		// Get the year in which the person finished all their school.
		//int finishedSchoolYear = attr.getSchoolHistory().getLastActivityPeriod()[1];

		

		
		// Set these empty archives now, so that they can be populated from the below procedure.
		attr.setWorkHistory(workHistory);
		attr.setSocietalWorkHistory(societalWorkHistory);
		
		
		
		
		ActivityArchive hometownHist = attr.getHometownHistory();
		int finishedSchoolYear = attr.getSchoolHistory().getLastActivityPeriod()[1];
		//if (attr.getID() == 15) System.out.println("finishedSchoolYear = " + finishedSchoolYear);
		
		// Loop through all ENTRIES in the hometownHistory (not looping through individual years!).
		int i;
		ArrayList<Object> hometownEntry;
		String hometownName;
		int[] hometownYears;

		for (i = 0; i < hometownHist.size(); i++) {
			hometownEntry = hometownHist.getDictEntryNameAndYears(i);
			hometownName = (String)hometownEntry.get(0); // 0th element is location name.
			hometownYears = (int[])hometownEntry.get(1); // 1st element is int array of period.
			//System.out.println(hometownName + " in [" + hometownYears[0] + "," + hometownYears[1] + "].");
			
			// =========================================================
			// CHECK WHICH ENTRIES ARE DURING PERSON's WORKING LIFE.
			// =========================================================
			
			
			// -------------------------------------------------------------
			// Hometown period DURING which the person become working age.
			// -------------------------------------------------------------
			if (ValidationTools.numberIsWithin(finishedSchoolYear, hometownYears[0], hometownYears[1]) && (finishedSchoolYear != hometownYears[1])) { // The second condition is to prevent the end of an entry being used.

				if (hometownName.equals(Configuration.SocietyName)) {
					//if (attr.getID() == 15) System.out.println("Need to find DETAILED work for [" + finishedSchoolYear + "," + hometownYears[1] + "]. (PARTIAL)");
					assignLocalWorkplaces(attr, finishedSchoolYear, hometownYears[1]);
				} else {
					//if (attr.getID() == 15) System.out.println("Need to find external work for [" + finishedSchoolYear + "," + hometownYears[1] + "]. (PARTIAL)");
					assignExternalWorkplaces(attr, finishedSchoolYear, hometownYears[1]);
				} // end if (check if hometown for this period is local society)

			} // end if (determine the hometown period DURING which the person becomes working age)

			// -------------------------------------------------------------
			// Hometown periods AFTER person is working age.
			// -------------------------------------------------------------
			if (hometownYears[0] > finishedSchoolYear) {

				if (hometownName.equals(Configuration.SocietyName)) {
					//if (attr.getID() == 15) System.out.println("Need to find DETAILED work for [" + hometownYears[0] + "," + hometownYears[1] + "]. (FULL)");
					assignLocalWorkplaces(attr, hometownYears[0], hometownYears[1]);
				} else {
					//if (attr.getID() == 15) System.out.println("Need to find external work for [" + hometownYears[0] + "," + hometownYears[1] + "]. (FULL)");
					assignExternalWorkplaces(attr, hometownYears[0], hometownYears[1]);
				} // end if (check if hometown for this period is local society)

			} // end if (determining the hometown periods completely AFTER the peson is working age)

		} // end for i (loop through all entries in hometown history)

		
		
		/*
		if (attr.getID() == 36) {
			System.out.println("Person #" + attr.getID() + " graduated in " + attr.getPSFinishYear() + ".");
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			DebugTools.printArray(attr.getHometownHistory());
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			DebugTools.printArray(attr.getSocietalHometownHistory());
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			DebugTools.printArray(attr.getWorkHistory());
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			DebugTools.printArray(attr.getSocietalWorkHistory());
		}
		*/
		
		
		
		
		/*
		// Get list of possible workplaces person could work with the pre-determined career.
				String[] possibleWorkplaces = Careers.getWorkplaceById(attr.getCareer());
				
				
				// If the peron's career does not match any workplace options, then stop now. (This should only ever be the case for "Unemployed, id="00000").
				if (possibleWorkplaces.length == 0) {
					//System.err.println("Person does not have a career that any workplace is interested in.");
					// Rather than having a null archive, we want to set the empty (but initialized!) archives here before returning out.
					attr.setWorkHistory(workHistory);
					attr.setSocietalWorkHistory(societalWorkHistory);
					return;
				} // end if (check if any possible work locations were found)
						
				//System.out.println("work history...");
						
				String chosenWorkplace = (String)Distribution.uniformRandomObject(possibleWorkplaces);
				//System.out.println(attr.careerID + " ---> chosen workplace: " + chosenWorkplace);
						
				//DebugTools.printArray(possibleWorkplaces);
						
						//int numWorkplaces = Distribution.uniform(1, 6);
						
						
				// Add career to archive to start from the year they finished school until present.
				String[] workPositionInfo = new String[] {chosenWorkplace, attr.getCareer()};				// Note the array is of the format [workplaceID, careerID].
				Careers.getWorkplaceById(attr.getCareer());
				workHistory.addEntry(workPositionInfo, finishedSchoolYear);
				societalWorkHistory.addEntry(workPositionInfo, finishedSchoolYear);	
						
						
						
				// --------------------------------------------------------------------------------------------------------
				// FROM assignIncome()
				//
				ArrayList careerInfo = Careers.getCareerById(careersDatabase, attr.getCareer());
				// Get the base income associated with the person's career.
				int careerIncome = Integer.parseInt((String)careerInfo.get(Careers.Career_Salary));
				// Randomly choose an amount (positive or negative) to offset the person's income from the base.
				int salarySTD = Distribution.uniform(-Configuration.SalarySTD, Configuration.SalarySTD);
				attr.setIncome(careerIncome + salarySTD);
				//
				// FROM assignIncome()
				// --------------------------------------------------------------------------------------------------------

				attr.setWorkHistory(workHistory);
				attr.setSocietalWorkHistory(workHistory);
		
		
		
		
		
		
		
		
		
		
		
		
		
	



		attr.setWorkHistory(workHistory);
		attr.setSocietalWorkHistory(workHistory);
		*/
		
	} // end assignWorkHistory()
	
	
	
	private static void assignLocalWorkplaces (Person attr, int startYear, int endYear) {
		// This method will find a workplace in the local society for the person over the given period.
		//
		// param attr: the given Person who is being given a work history
		// param startYear: the integer of the year that begins this period of work
		// param endYear: the integer of the year that ends this period of work
		
		// Get list of possible workplaces person could work with the pre-determined career.
		String[] possibleWorkplaces = Careers.getWorkplaceById(attr.getCareer());

		
		// If the peron's career does not match any workplace options, then stop now. (This should only ever be the case for "Unemployed, id="00000").
		if (possibleWorkplaces.length == 0) {
			//System.err.println("Person does not have a career that any workplace is interested in.");
			// Rather than having a null archive, we want to set the empty (but initialized!) archives here before returning out.
			//attr.setWorkHistory(workHistory);
			//attr.setSocietalWorkHistory(localWorkHistory);
			return;
		} // end if (check if any possible work locations were found)

		String chosenWorkplace = (String)Distribution.uniformRandomObject(possibleWorkplaces);

		// Add career to archive to start from the year they finished school until present.
		String[] workPositionInfo = new String[] {chosenWorkplace, attr.getCareer()};				// Note the array is of the format [workplaceID, careerID].
		Careers.getWorkplaceById(attr.getCareer());
		attr.getWorkHistory().addEntry(workPositionInfo, startYear, endYear);
		attr.getSocietalWorkHistory().addEntry(workPositionInfo, startYear, endYear);	
		
		
		// Update person's current position to WORKING.
		attr.setCurrentPosition(Person.CURR_POSITION_WORKING);
		
		
		// --------------------------------------------------------------------------------------------------------
		// FROM assignIncome()
		//
		ArrayList careerInfo = Careers.getCareerById(careersDatabase, attr.getCareer());
		// Get the base income associated with the person's career.
		int careerIncome = Integer.parseInt((String)careerInfo.get(Careers.Career_Salary));
		// Randomly choose an amount (positive or negative) to offset the person's income from the base.
		int salarySTD = Distribution.uniform(-Configuration.SalarySTD, Configuration.SalarySTD);
		attr.setIncome(careerIncome + salarySTD);
		//
		// FROM assignIncome()
		// --------------------------------------------------------------------------------------------------------

	} // end assignLocalWorkplaces()
	
	
	private static void assignExternalWorkplaces (Person attr, int startYear, int endYear) {
		// This method will find a workplace in an external location for the person over the given period.
		//
		// param attr: the given Person who is being given a work history
		// param startYear: the integer of the year that begins this period of work
		// param endYear: the integer of the year that ends this period of work



		String chosenWorkplace = "External Company";

		// Add career to archive over the given period.
		String[] workPositionInfo = new String[] {chosenWorkplace, attr.getCareer()};				// Note the array is of the format [workplaceID, careerID].
		
		attr.getWorkHistory().addEntry(workPositionInfo, startYear, endYear);
		
		
		// Update person's current position to WORKING.
		attr.setCurrentPosition(Person.CURR_POSITION_WORKING);
		
		
		// --------------------------------------------------------------------------------------------------------
		// FROM assignIncome()
		//
		ArrayList careerInfo = Careers.getCareerById(careersDatabase, attr.getCareer());
		// Get the base income associated with the person's career.
		int careerIncome = Integer.parseInt((String)careerInfo.get(Careers.Career_Salary));
		// Randomly choose an amount (positive or negative) to offset the person's income from the base.
		int salarySTD = Distribution.uniform(-Configuration.SalarySTD, Configuration.SalarySTD);
		attr.setIncome(careerIncome + salarySTD);
		//
		// FROM assignIncome()
		// --------------------------------------------------------------------------------------------------------



	} // end assignExternalWorkplaces()
	
	
	
	
	public static void assignInterests (Person attr) {
		// Assign a set of interests to the person. These are each in [0,100].
		// We are using these interests: [race, religion, athleticism, interest3, interest4, interest5, interest6, interest7]
		// The [race, religion, and athleticism] interests are determined from the person's respective's attribute values.
		// The other interests are taken randomly from a normal distribution.

		
		// INTERESTS: [race, religion, athleticism, interest3, interest4, interest5, interest6, interest7] 

		double[] interests = new double[Configuration.NumInterests];
		int i;
		
		// 0: RACE.
		// Scale race interest to [0,100] and add a random number from [-10,10]. Clip the result onto [0,100].
		i = 0;
		double raceInt = Configuration.race_interest_value[attr.getRace()] * 100.0;
		interests[i] = ValidationTools.clipValueToRange((raceInt+Distribution.uniform(-10.0, 10.0)), 0.0, 100.0);
		
		// 1: RELIGION.
		// Scale religion interest to [0,100] and add a random number from [-10,10]. Clip the result onto [0,100].
		i = 1;
		double religionInt = Configuration.religion_interest_value[attr.getReligion()] * 100.0;
		interests[i] = ValidationTools.clipValueToRange((religionInt+Distribution.uniform(-10.0, 10.0)), 0.0, 100.0);
		
		// 2: ATHLETICISM.
		i = 2;
		interests[i] = attr.getAthleticism()*100.0; // Scale from [0,1] to [0,100].
		
		// 3-7: RANDOM EXTRA INTERESTS.
		for (i = 3; i < Configuration.NumInterests; i++) {
			interests[i] = Distribution.normal(50.0, 10.0);
		} // end for i (interest options)
		
		//DebugTools.printArray(interests);
		
		attr.setInterests(interests);
	} // end assignInterests()
	
	
	
	public static void assignInterestWeights (Person attr) {
		// Assign a set of interest weights to the person. These are each in [0,1] and they must sum to 1.0.

		double[] wgts = new double[Configuration.NumInterests];
		int i;
		double equalFraction = 1.0 / Configuration.NumInterests;
		for (i = 0; i < Configuration.NumInterests; i++) {
			wgts[i] = equalFraction;
		} // end for i (interest options)

		attr.setInterestWeights(wgts);
	} // end assignInterestWeights()
	
	
	
	
	
	
	
	
	// ==================================================================================================================================================================
	// Dependent Attributes (for spouses, children, etc.)
	// ==================================================================================================================================================================
	
	public static void assignSpouseRace (Person personA, Person personB) {
		double[] race_probs = Configuration.p_same_race_for_spouse[personA.getRace()]; // Extract the appropriate row of race probabilities.
		personB.setRace(Distribution.custom(race_probs));
	} // end assignSpouseRace()
	
	public static void assignSpouseReligion (Person personA, Person personB) {
		double[] religion_probs = Configuration.p_same_religion_for_spouse[personA.getReligion()]; // Extract the appropriate row of religion probabilities.
		personB.setReligion(Distribution.custom(religion_probs));
	} // end assignSpouseReligion()
	
	public static void assignSpouseAge (Person personA, Person personB) {
		System.err.println("Is this method used anymore? Probably deprecated. AttributeAssigner->assignSpouseAge()");
		//int b_age = personA.getAge() + (int)Math.round(Distribution.normal(0.0, 2.0));
		//personB.setAge(b_age);
	} // end assignSpouseReligion()
	
	

	
	public static void assignChildHometowns (Person attr, Person parentA, Person parentB) {

		ActivityArchive tmpChildArchive = new ActivityArchive();
		
		//System.out.println(".................... In AttributeAssigner->assignChildHometowns() ....................");

		//System.out.println("child born in " + attr.getYearBorn());
		
		int y;
		
		int finalYear;
		//System.out.println("How old is the child?? " + attr.getAge() + ", and he/she must finish at " + Configuration.SchoolFinishAge);
		// If the person is still in school, then add years between their birth and the current year, otherwise, fill from birht until the year they finished school.
		if (attr.getAge() < Configuration.SchoolFinishAge) {
			// The current year.
			finalYear = Configuration.SocietyYear;
		} else {
			// The year this person finished elementary (secondary) school.
			finalYear = attr.getYearBorn()+Configuration.SchoolFinishAge;
		} // end if (determine the final year for the child living with their parents - either the current year or their last school year)
		
		ArrayList<Object> actEntry;
		int[] tmpActYears;
		int tmpEntryBeginYear;
		int tmpEntryFinalYear;
		// Loop through all childhood years from birth until the end of elementary (secondary) school.
		for (y = attr.getYearBorn(); y < finalYear; y++) {
			//System.out.println("> " + y + " | " + parentA.getHometownHistory().getActivityAtYear( y ));
			actEntry = parentA.getHometownHistory().getActivityAndYearsAtYear( y );

			tmpActYears = (int[])actEntry.get(1);
			
			// ADJUST STARTING YEAR OF ENTRY.
			// If the year, in this loop iteration, is the first one of that entry, then indicate that first year.
			if (y == tmpActYears[0]+1) {
				tmpEntryBeginYear = y - 1;
			} else {
				tmpEntryBeginYear = y;
			} // end if (check if the loop year is the first year of the given activity entry)
			
			// ADJUST ENDING YEAR OF ENTRY.			
			// If the year, in this loop iteration, is the final one of that entry, then indicate that final year.
			if (y == tmpActYears[1]) {
				tmpEntryFinalYear = y;
			} else {
				tmpEntryFinalYear = y+1;
			} // end if (check if the loop year is the final year of the given activity entry)
			
			//tmpChildArchive.addEntry(actEntry.get(0), tmpActYears[0], tmpActYears[1]);
			//tmpChildArchive.addEntry(actEntry.get(0), y, y+1);
			tmpChildArchive.addEntry(actEntry.get(0), tmpEntryBeginYear, tmpEntryFinalYear);

		} // end for y (loop through years that child lives with parents)
		
		// Ensure the last hometown was added properly. If there was a move in the current societal year, then it won't add
		// properly initially. This will ensure that it is appended to the child's archives.
		int[] lastActTime = parentA.getHometownHistory().getLastActivityPeriod();
		if (lastActTime[0] == Configuration.SocietyYear) {
			Object lastActName = parentA.getHometownHistory().getLastActivityName();
			tmpChildArchive.addEntry(lastActName, lastActTime[0], lastActTime[1]);

		} // end if (check if last activity begins in current year, and adds it to the child's archive)
		
		// Add last entry separately.
		
		//int[] lastActTime = parentA.getHometownHistory().getLastActivityPeriod();
		
		

		
		
		//System.out.println(parentA.getHometownHistory().getActivityAtYear( attr.getYearBorn() ));
		//System.out.println(parentA.getHometownHistory().getActivityAtYear( attr.getYearBorn() ));
		
		
		//System.out.println("~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~");
		//System.out.println("o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o");
		//DebugTools.printArray(parentA.getHometownHistory());
		//DebugTools.printArray(parentB.getHometownHistory());
		
		
		tmpChildArchive.patchSequentialEntries();
	
		//System.out.println("~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~");
		//DebugTools.printArray(tmpChildArchive);
		//System.out.println("~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~");
		

		attr.setHometownCheckpointsHistory(tmpChildArchive);
		
		
	} // end assignChildHometowns()
	
	
	
	
	
	
	
	
	
	

} // end AttributeAssigner class
