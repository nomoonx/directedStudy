package thesis_network_growth;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.security.AllPermission;
import java.util.ArrayList;

public class SimulationStepper {

	int trialNumber;
	
	public SimulationStepper (ArrayList<Person> persons, int trial) {
		trialNumber = trial;
	} // end SimulationStepper() constructor
	
	
	public void begin (int n) {
		
		
		
		
		OutputTrialResults trialResults = new OutputTrialResults();
		
	
		
		//System.out.println(person.getID() + ":  " + person.getAge() + " | " + person.getCurrentPosition());
		//System.out.println( Configuration.SocietyYear + "," + ArtificialSociety.getSociety().size() );
		
		//trialResults.appendFileContents(  Configuration.SocietyYear + "," + ArtificialSociety.getSociety().size() );
		//int[] raceNumbers = SocietyQuery.GetRaceNumbers();
		//trialResults.appendFileContents(  Configuration.SocietyYear + "," + ArrayTools.convertArrayToCSV(raceNumbers) ); // Race
		//trialResults.appendFileContents(  Configuration.SocietyYear + "," + ArtificialSociety.getSociety().size() ); // Population

		
		int i;
		for (i = 0; i < n; i++) {
			Configuration.SocietyYear += 1;
			evaluateSociety();
			//System.out.println(person.getID() + ":  " + person.getAge() + " | " + person.getCurrentPosition());
			
			//System.out.println(person.getID() + ":  " + person.getAge() + " | " + person.getCurrentPosition());
			//System.out.println(person.getExpectedDeathYear());
			//DebugTools.printArray(person.getSchoolHistory());
			//DebugTools.printArray(person.getHometownHistory());
			
			//ArtificialSociety.DisplayAttributeTable("age");
			
			//System.out.println("Person's friend number: " + person.getFriends().size());
			//System.out.println("Person's friend number: " + person.getFriends().size() + " fr_probIDs size: " + person.getFriendProbIDs().size() + " fr_probs size: " + person.getFriendProbabilities().size());
			//for (j = 0; j < person.getFriends().size(); j++)
			
			//System.out.println( Configuration.SocietyYear + "," + ArtificialSociety.getSociety().size() );
			
			
			// Population.
			//trialResults.appendFileContents(  Configuration.SocietyYear + "," + ArtificialSociety.getSociety().size() );
			
			// Race.
			//raceNumbers = SocietyQuery.GetRaceNumbers();
			//trialResults.appendFileContents(  Configuration.SocietyYear + "," + ArrayTools.convertArrayToCSV(raceNumbers) );	// Race
			//trialResults.appendFileContents(  Configuration.SocietyYear + "," + ArtificialSociety.getSociety().size() );		// Population
			
			
		} // end for i (loop through simulation steps)
		
		
		//trialResults.writeToFile("Trial Results/Results_Population_20YearSim.txt");						// TEST population
		//trialResults.writeToFile("Trial Results/Race/Results_Race_50YearSim_" + trialNumber + ".txt");	// Race
		//trialResults.writeToFile("Trial Results/Population from Child Distr/Results_Popn_RegDistr_" + trialNumber + ".txt");	// Population from regular child distribution.
		//trialResults.writeToFile("Trial Results/Population from Child Distr/Results_Popn_OneChildPolicy_" + trialNumber + ".txt");	// Population from one-child policy.
		
		
	} // end begin()
	
	
	public void runToPopulationThreshold (int popThreshold) {
		
		
		
		
		OutputTrialResults trialResults = new OutputTrialResults();
		
		System.out.println( Configuration.SocietyYear + "," + ArtificialSociety.getSociety().size() );

		
		int i;
		while (ArtificialSociety.getSociety().size() < popThreshold) {
			Configuration.SocietyYear += 1;
			evaluateSociety();
			//System.out.println(person.getID() + ":  " + person.getAge() + " | " + person.getCurrentPosition());
			
			//System.out.println(person.getID() + ":  " + person.getAge() + " | " + person.getCurrentPosition());
			//System.out.println(person.getExpectedDeathYear());
			//DebugTools.printArray(person.getSchoolHistory());
			//DebugTools.printArray(person.getHometownHistory());
			
			//ArtificialSociety.DisplayAttributeTable("age");
			
			//System.out.println("Person's friend number: " + person.getFriends().size());
			//System.out.println("Person's friend number: " + person.getFriends().size() + " fr_probIDs size: " + person.getFriendProbIDs().size() + " fr_probs size: " + person.getFriendProbabilities().size());
			//for (j = 0; j < person.getFriends().size(); j++)
			
			System.out.println( Configuration.SocietyYear + "," + ArtificialSociety.getSociety().size() );
			
			
			// Population.
			//trialResults.appendFileContents(  Configuration.SocietyYear + "," + ArtificialSociety.getSociety().size() );
			
			// Race.
			//raceNumbers = SocietyQuery.GetRaceNumbers();
			//trialResults.appendFileContents(  Configuration.SocietyYear + "," + ArrayTools.convertArrayToCSV(raceNumbers) );	// Race
			//trialResults.appendFileContents(  Configuration.SocietyYear + "," + ArtificialSociety.getSociety().size() );		// Population
			
			if (ArtificialSociety.getSociety().size() == 0) {
				break;
			}
			
		} // end for i (loop through simulation steps)
		
		
		//trialResults.writeToFile("Trial Results/Results_Population_20YearSim.txt");						// TEST population
		//trialResults.writeToFile("Trial Results/Race/Results_Race_50YearSim_" + trialNumber + ".txt");	// Race
		//trialResults.writeToFile("Trial Results/Population from Child Distr/Results_Popn_RegDistr_" + trialNumber + ".txt");	// Population from regular child distribution.
		//trialResults.writeToFile("Trial Results/Population from Child Distr/Results_Popn_OneChildPolicy_" + trialNumber + ".txt");	// Population from one-child policy.
		
		
	} // end begin()
	
	
	
	
	
	
	public void evaluateSociety () {
		

		
		ArrayList<Person> personsToDieThisYear = new ArrayList<Person>();
		
		// Loop through everybody in society.
		int i;
		Person person;
		//System.out.println("Population = " + numPersons);
		for (i = 0; i < ArtificialSociety.getSociety().size(); i++) {

			// Get person.
			person = ArtificialSociety.getPersonByIndex(i);
			
			// Evaluate if person has died.
			boolean isPersonDying = evaluatePerson_Death(person);
			
			// Handle person dying this year. Put them into the array, so that they will die at the end of the year. 
			if (isPersonDying) {
				personsToDieThisYear.add(person);
			} // end if (check if person is supposed to die this year)
			
			// -----------------------------------------------------------------------------------------------
			// Evaluate all elements for persons.
			// -----------------------------------------------------------------------------------------------
			
			// Evaluate person's basic information.
			evaluatePerson_Basic(person);
			// Evaluate person's relationship status.
			evaluatePerson_Relationship(person);
			// Evaluate person's current position (student, working, etc).
			evaluatePerson_CurrentPosition(person);
	
			// Evaluate person's hometown.
			evaluatePerson_Hometown(person);
			// Evaluate person's school/work involvement.
			evaluatePerson_PositionInvolvement(person);
			// Evaluate person's hometown.
			//evaluatePerson_Hometown(person);
			
			
			// Evaluate person's groups (school, work, temple, and clubs groups).
			evaluatePerson_Groups(person);
			// Evaluate person's friendships.
			evaluatePerson_Friends(person);


		} // end for i (loop through all persons)
		
		
		// -----------------------------------------------------------------------------------------------
		// At the end of the year, all people who were to die this year are now going to die.
		// -----------------------------------------------------------------------------------------------
		int d;
		int numDyingPersons = personsToDieThisYear.size();
		Person diePerson;
		for (d = 0; d < numDyingPersons; d++) {
			diePerson = personsToDieThisYear.get(d);
			diePerson.die(); // Kill off the persons who were set to die this year.
		} // end for d (loop through persons who die this year)
		
		//if (Configuration.SocietyYear >= 2056) {
			//System.out.println("At the end of year " + Configuration.SocietyYear);
			//ArtificialSociety.DisplayIndexTable();
		//}
		
	} // end evaluateSociety()

	
	private void evaluatePerson_Basic (Person person) {
		
		// Calculate new age of person.
		AttributeAssigner.assignAge(person);
		
		
		
	} // end evaluatePerson_Basic()
	
	
	private boolean evaluatePerson_Death (Person person) {

		// Determine if person has reached their life expectancy.
		if (Configuration.SocietyYear >= person.getExpectedDeathYear()) {
			//if (Configuration.SocietyYear == 2058) {
				//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>> PERSON #" + person.getID() + " JUST DIED! :(");
			//}
			//person.die();
			// Return true to indicate person having to die this year.
			return true;
		} // end if (check if person's life expectancy has been reached)
		
		// Person does not die now.
		return false;

	} // end evaluatePerson_Death()

	private void evaluatePerson_Relationship (Person person) {
		Person partner;
		switch (person.getRelationshipStatus()) {
			case RelationshipCalculator.REL_TYPE_SINGLE:
				evaluateRelationship_Single(person);
				break;
			case RelationshipCalculator.REL_TYPE_MARRIED:
				if (person.getRelationshipStatus() == 0) {
					System.err.println("ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
				}
				partner = ArtificialSociety.getPersonByID(person.getPartnerID());
				evaluateRelationship_Couple(person, partner, RelationshipCalculator.REL_TYPE_MARRIED);
				break;
			case RelationshipCalculator.REL_TYPE_DATING:
				partner = ArtificialSociety.getPersonByID(person.getPartnerID());
				if (person.getRelationshipStatus() == 0) {
					System.err.println("ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
				}
				evaluateRelationship_Couple(person, partner, RelationshipCalculator.REL_TYPE_DATING);
				break;
			case RelationshipCalculator.REL_TYPE_WIDOWED:
				// Do anything ???
				break;
			default:
				System.err.println("In SimulationStepper->evaluateSociety(); unknown relationship status found: " + person.getRelationshipStatus());
				break;
		} // end switch (checking the person's relationship status)
	} // end evaluatePerson_Relationship()
	
	private void evaluatePerson_CurrentPosition (Person person) {

		String pos = person.getCurrentPosition();

		if (pos.equals( Person.CURR_POSITION_CHILD )) {
			// Look for year to begin school.
			evaluatePosition_Child(person);
		} else if (pos.equals( Person.CURR_POSITION_STUDENT )) {
			// Check for year finished school, then look for work.
			evaluatePosition_Student(person);
		} else if (pos.equals( Person.CURR_POSITION_WORKING )) {
			// Check age range.
			evaluatePosition_Working(person);
		} else if (pos.equals( Person.CURR_POSITION_UNEMPLOYED )) {
			// Look for work, if within working age range.
			evaluatePosition_Unemployed(person);
		} else if (pos.equals( Person.CURR_POSITION_RETIRED )) {
			// Do nothing. Person is retired.
			evaluatePosition_Retired(person);
		} else if (pos.equals( Person.CURR_POSITION_DEAD )) {
			// Do nothing. Person is dead.
		} else {
			// Do nothing.
		} // end if (determine which position person was in)
		
		// TODO (Do I need this? I don't think I do.. It seems to all be handled in here already!!)
		//AttributeAssigner.assignCurrentPosition(person);
		
	} // end evaluatePerson_CurrentPosition()
	
	private void evaluatePerson_PositionInvolvement (Person person) {
		
		//System.out.println("person.getCurrentPosition() = " + person.getCurrentPosition());
		
		if (person.getCurrentPosition().equals( Person.CURR_POSITION_STUDENT )) {
			// STUDENT.
			evaluatePerson_Student(person);
		
		} else if (person.getCurrentPosition().equals( Person.CURR_POSITION_UNEMPLOYED )) {
			// UNEMPLOYED.
			//System.err.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
			//System.out.println("Before, person " + person.getID() + " unemployed [" + person.getCareer() + "].");
			evaluatePerson_Unemployed(person);
			//System.out.println("After, person " + person.getID() + " employed with [" + person.getCareer() + "].");
			//DebugTools.printArray(person.getWorkHistory());
			//System.out.println("---------------------------------------------------------");
			
		} else if (person.getCurrentPosition().equals( Person.CURR_POSITION_WORKING )) {
			// WORKING.
			evaluatePerson_Working(person);
			
		} // end if (check if person is student
		
		
	} // end evaluatePerson_PositionInvolvement()

	private void evaluatePerson_Hometown (Person person) {
		// Evaluate the person's hometown. Note that any moves for work will be done in evaluatePerson_Unemployed() when they
		// receive a job in a new location. In this method, the hometown is simply updated to include the current year.

		/*
		if (person.getID() == 42 && Configuration.SocietyYear >= 2055) {
			System.out.println("-------------------------------------------------------------------");
			System.out.println("UPDATING PERSON 42's HOMETOWN!!!!!!!! (" + Configuration.SocietyYear + ") WHERE WILL THEY RESIDE?");
			DebugTools.printArray(person.getHometownHistory());
			System.out.println("-------------------------------------------------------------------");
		}
		*/
		
		if (person.getHometownHistory() != null && !person.getHometownHistory().isEmpty()) {
			person.getHometownHistory().updateSameLastEntry();
			
			// If person lives locally, then update the societal archive too.
			if (person.getSocietalHometownHistory() != null && !person.getSocietalHometownHistory().isEmpty()) {
				if (person.getHometownHistory().getLastActivityName().toString().equals(Configuration.SocietyName)) {
					person.getSocietalHometownHistory().updateSameLastEntry();
				} // end if (check if person lives locally)
				
			} // end if (check if person's societal hometown history has been initialized)
			
		} // end if (check if hometown history has been initialized) -- THIS IS TEMPORARY!!!
		
	} // end evaluatePerson_Hometown()

	private void evaluatePerson_Groups (Person person) {
		
		PersonGroupAdder.UpdatePersonInAllGroups(person);
		
		//PersonGroupAdder.AddPersonToAllGroups(person);
		
	} // end evaluatePerson_Groups()
	

	private void evaluatePerson_Friends (Person person) {
		
		FriendshipCalculator.CreateFriendshipNetwork(person);
		
	} // end evaluatePerson_Friends()

	
	// -----------------------------------------------------------------------------------------------------------------------------
	// SUB-FUNCTIONS FOR RELATIONSHIP EVALUATION
	// -----------------------------------------------------------------------------------------------------------------------------
	
	private void evaluateRelationship_Single (Person person) {
		// Examine single person from time t-1, and determine whether or not the person will be in a relationship at time t.
		
		//System.out.println("Before, person " + person.getID() + " is single... " + person.getPartnerID());
		
		// If single, person may find a partner to form a dating relationship with.
		RelationshipCalculator.SearchForPartner(person);
		
		//System.out.println("After, person " + person.getID() + " is courting person " + person.getPartnerID());
		
	} // end evaluateRelationship_Single()
	
	
	
	
	private void evaluateRelationship_Couple (Person person, Person partner, int relationshipType) {
		// Examine the couple from time t-1, and determine the relationship strength at time t.
		// This also allows couples to have children at different times. 
		// In future work, this could indicate if the couple separates, but for now, we just calculate strength but disallow separation.
		
		//int TMP_D_COUPLES = 0;
		//int TMP_M_COUPLES = 0;
		
		
		
		// Ensure a couple's evaluation is only done once, by only using the parent with the greater ID.
		if (person.getID() > partner.getID()) { // Only create children at the person with the greater ID. This way children won't be created twice for the same couple.
		
			//System.out.println("Before:\t" + person.getRelationshipStrength() + " | " + partner.getRelationshipStrength());
		
			if (person.getChildrenIDs().isEmpty()) {
				// If couple has not had any children.
				RelationshipCalculator.CalculateAndSetRelationshipStrength(person, partner, 0);
			} else {
				// If couple has children.
				RelationshipCalculator.CalculateAndSetRelationshipStrength(person, partner, 1);
			} // end if (check whether or not couple has previously had children)

			// DATING.
			if (relationshipType == RelationshipCalculator.REL_TYPE_DATING) {
				// The couple may get married, may break up, or continue dating.
				RelationshipCalculator.DetermineFateOfDatingCouple(person, partner);
				
				// DEBUG
				/*
				if (person.getRelationshipStatus() == RelationshipCalculator.REL_TYPE_MARRIED) {
					System.out.println("!!!!!!!!!!! Congrats to " + person.getID() + " && " + partner.getID() + " on getting MARRIED!!!!!!!!!!!!");
				} else if (person.getRelationshipStatus() == RelationshipCalculator.REL_TYPE_SINGLE) {
					System.out.println("Awful... the couple " + person.getID() + " && " + partner.getID() + " just broke up. :(");					
				} else {
					System.out.println("___ lame couple " + person.getID() + " && " + partner.getID() + " are still " + person.getRelationshipStatus() + "... After " + (Configuration.SocietyYear - person.getRelationshipStartYear()) + " years.");
				}
				*/
				// DEBUG
				//TMP_D_COUPLES++;
			
			// MARRIED.
			} else if (relationshipType == RelationshipCalculator.REL_TYPE_MARRIED) {
				// The couple may have children, based upon several factors. There is NO option for divorce/separation.
				RelationshipCalculator.CreateChildren(person, partner, false); // isBackFilling = false here because this is a live simulation!
				//TMP_M_COUPLES++;

			} // end if (determine whether couple is dating or married)

		} // end if (compare couple's IDs to ensure the evaluation is done only once per couple)

		
		//System.out.println("Married: " + TMP_M_COUPLES + " || Dating: " + TMP_D_COUPLES);
		
	} // end evaluateRelationship_Couple()
	
	
	
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// SUB-FUNCTIONS FOR CURRENT POSITION EVALUATION
	// -----------------------------------------------------------------------------------------------------------------------------
	
	private void evaluatePosition_Child (Person person) {
		// Examine child from time t-1, and determine their position at time t.
		// param person: the Person whom we are examining
		//
		// Note: possible position states that could be assigned in this method: {STUDENT, CHILD (same)}

		if (person.getAge() >= Configuration.SchoolStartAge) {
			// Child is now old enough to begin school.

			person.setIsInSchool(true);
			person.setCurrentPosition(Person.CURR_POSITION_STUDENT);

		} // end if (check if child is old enough for school)

	} // end evaluatePosition_Child()
	
	private void evaluatePosition_Student (Person person) {
		// Examine child from time t-1, and determine their position at time t.
		// param person: the Person whom we are examining
		//
		// Note: possible position states that could be assigned in this method: {UNEMPLOYED, STUDENT (same)}

		
		if (Configuration.SocietyYear >= person.getPSFinishYear()) {
			// Person is done school, so they are now unemployed and will begin searching for work.
			person.setIsInSchool(false);
			person.setCurrentPosition( Person.CURR_POSITION_UNEMPLOYED );
			//if (person.getID() == 36) System.err.println("NOW UNEMPLOYED FROM SCHOOL");
		} else {
			// Do nothing. Person is still a student.
		} // end if (check if person has finished their school, based only on their age)
		
		
		
		
		/*
		if (ValidationTools.numberIsWithin(person.getAge(), Configuration.SchoolStartAge, Configuration.SchoolFinishAge)) {
			// ---------------------------------------
			// ELEMENTARY/SECONDARY SCHOOL.
			// ---------------------------------------
			if (person.getSchoolHistory().isEmpty()) {
				// Person is just starting school so select a school for them.
				double[] schoolProbs = SchoolSet.getSchoolProbabilityTable(Configuration.SocietyYear);
 				int rndSchoolIndex = Distribution.custom(schoolProbs);
 				String schoolName = InstitutionSet.getSchoolTitleAt(InstitutionSet.ElementarySchoolsList, rndSchoolIndex);
 				person.getSchoolHistory().addEntry(new String[] {"Elementary", schoolName}, Configuration.SocietyYear);
 				
 				//System.out.println("evalPos_Student() -> new school!");
			} else {
				person.getSchoolHistory().updateSameLastEntry();	// Assume student stays in same school through all elementary/secondary years.

			} // end if (check if person has already been assigned into a school)
			
			
		} else if (ValidationTools.numberIsWithin(Configuration.SocietyYear, person.getPSStartYear(), person.getPSFinishYear())) {
			// ---------------------------------------
			// POST-SECONDARY SCHOOL.
			// ---------------------------------------
			// Do nothing.
		} else {
			// ---------------------------------------
			// FINISHED ALL SCHOOL.
			// ---------------------------------------
			person.setCurrentPosition( Person.CURR_POSITION_UNEMPLOYED );
		} // end if (determine which age phase this person is in)
		*/
		
	} // end evaluatePosition_Student()
	
	private void evaluatePosition_Working (Person person) {
		// Examine working person from time t-1, and determine their position at time t.
		// param person: the Person whom we are examining
		//
		// Note: possible position states that could be assigned in this method: {RETIRED, WORKING (same)}
		// Note: working person can also go to UNEMPLOYED option, but NOT from this method! That would occur from evaluatePerson_Working().

		if (person.getAge() > Configuration.WorkingAgeMax) {
			// Person is old enough, so let them retire.
			person.setCurrentPosition( Person.CURR_POSITION_RETIRED );
		} // end if (check if person is at age for retirement)

	} // end evaluatePosition_Working()
	
	private void evaluatePosition_Unemployed (Person person) {
		// Examine unemployed person from time t-1, and determine their position at time t.
		// param person: the Person whom we are examining
		//
		// Note: possible position states that could be assigned in this method: {RETIRED, UNEMPLOYED (same)}
		// Note: unemployed person can also go to WORKING option, but NOT from this method! That would occur from evaluatePerson_Unemployed().

		if (person.getAge() >= Configuration.WorkingAgeMax) {
			// Retire.
			person.setCurrentPosition( Person.CURR_POSITION_RETIRED );
		} // end if (check if person is old enough that they are considered retired rather than unemployed)
		
	} // end evaluatePosition_Unemployed()
	
	private void evaluatePosition_Retired (Person person) {
		// Examine retiree from time t-1, and determine their position at time t.
		// param person: the Person whom we are examining
		//
		// Note: possible position states that could be assigned in this method: {RETIRED (same)}

		// Do nothing. Once retired, always retired.
		
	} // end evaluatePosition_Retired()
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// SUB-FUNCTIONS FOR POSITION INVOLVEMENT EVALUATION (SCHOOL, WORK, ETC.)
	// -----------------------------------------------------------------------------------------------------------------------------
	
	private void evaluatePerson_Student (Person person) {
		// This method will assign a school to the person to attend.

		if (ValidationTools.numberIsWithin(person.getAge(), Configuration.SchoolStartAge, Configuration.SchoolFinishAge)) {
			
			String PSLocation = (String)person.getHometownHistory().getActivityAtYear(Configuration.SocietyYear);
			
			// ---------------------------------------
			// ELEMENTARY/SECONDARY SCHOOL.
			// ---------------------------------------
			if (person.getSchoolHistory().isEmpty()) {

				// Person is just starting school, so create new entry for them.
				FindSchoolForNewStudent(person, PSLocation);

			} else {
				// Person is already in school, so update their archives.

				// Get the person's location in the previous year to determine whether or not they are in the same location now.
				String prevLocation = (String)person.getHometownHistory().getActivityAtYear(Configuration.SocietyYear - 1);
				boolean sameHometownAsLastYear;
				if (prevLocation.equals(PSLocation)) {
					sameHometownAsLastYear = true;
				} else {
					sameHometownAsLastYear = false;
				} // end if (check if person is in same location this year as last year)
				
				if (sameHometownAsLastYear) {		// Person is in same hometown as they were in the previous year.
					person.getSchoolHistory().updateSameLastEntry();
					if (PSLocation.equals(Configuration.SocietyName)) {
						person.getSocietalSchoolHistory().updateSameLastEntry();
					} // end if (check if new location is in local society)
					
				} else { 							// Person is in a new hometown this year.
					FindSchoolForNewStudent(person, PSLocation);	
				} // end if (check if hometown this year is the same as the previous year)

			} // end if (check if person has already been assigned into a school)
			
			
			
		} else if (ValidationTools.numberIsWithin(Configuration.SocietyYear, person.getPSStartYear(), person.getPSFinishYear())) {
			// ---------------------------------------
			// POST-SECONDARY SCHOOL.
			// ---------------------------------------
			// Do nothing.
			
			String[] lastSchool = (String[])person.getSchoolHistory().getLastActivityName();
			String lastSchoolType = lastSchool[0]; // The school entries are of the format [schoolType, schoolName].
			
			

				
			String PSLocation = (String)person.getHometownHistory().getActivityAtYear(Configuration.SocietyYear);
				
			if (lastSchoolType.equals("Elementary")) {	// Person is just starting PS education. (Last entry is for elementary school).
					
				// Find new post-secondary school for person.
				FindPostSecondarySchoolForNewStudent(person, PSLocation, person.getEducation());
					
			} else {
				
				// Get the person's location in the previous year to determine whether or not they are in the same location now.
				String prevLocation = (String)person.getHometownHistory().getActivityAtYear(Configuration.SocietyYear - 1);
				boolean sameHometownAsLastYear;
				if (prevLocation.equals(PSLocation)) {
					sameHometownAsLastYear = true;
				} else {
					sameHometownAsLastYear = false;
				} // end if (check if person is in same location this year as last year)
				
				
				if (sameHometownAsLastYear) {			// Person lives in same location as they did last year.
					// Person has already begun at post-secondary school, so just update their current year there.
					person.getSchoolHistory().updateSameLastEntry();

					if (PSLocation.equals(Configuration.SocietyName)) {
						// Update societal school history since person has already been attending there locally.
						person.getSocietalSchoolHistory().updateSameLastEntry();
					} // end if (check if person lives locally, so local history can also be updated)
						
				} else {								// Person lives in a new location, different from last year.
					
					// Find new post-secondary school for person.
					FindPostSecondarySchoolForNewStudent(person, PSLocation, person.getEducation());
						
				} // end if (check if person is in same location as new year or new location)
				
			} // end if (check if person is just beginning PS school or has already begun)
				
				
				
				
				////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////////////////
			////////////////////////////////////////////////////////////////////////////////////////////////////////////
				////////////////////////////////////////////////////////////////////////////////////////////////////////////



				/*
				
				// * NOTE * This assumes the institutions were/are in service the whole time this simulation takes place. If this needs to be changed, then the school's time
				//			period would have to be examined to see if it was/is in existence during the person's required time in post-secondary school.
				//String[] schoolsByType = ArrayTools.arrayListToStringArray(Schools.getSchoolsByFilters(Schools.getFullPostSecondarySchoolsDatabase(), new int[] {Schools.School_Type, Schools.School_City}, new Object[] {attr.education, PSLocation}, new int[] {Schools.School_Name}));
				String[] schoolsByType = ArrayTools.arrayListToStringArray(InstitutionSet.getInstitutionsByFilters(InstitutionSet.PostSecondarySchoolsList, new int[] {InstitutionSet.Institution_Subtype, InstitutionSet.Institution_City}, new Object[] {person.getEducation(), PSLocation}, new int[] {InstitutionSet.Institution_Name}));

				// Check if there were no post-secondary schools found for the person (either because they didn't require any or if they are too young yet).
				if (schoolsByType.length == 0) {
					///////
					// NOTE: the following inner if-statement block can be removed. It was a simple debugging check, but not necessary.
					///////
					if (person.getPSEducationYears() > 0) {
						//System.err.println("In AttributeAssigner->assignSchoolHistory(), there were no post-secondary institutions for this person!");
						// Add post-secondary school to archive. Assume that attr.education is the type of school this school is (i.e. U or C)
						person.getSchoolHistory().addEntry(new String[] {person.getEducation(), "External Institution"}, Configuration.SocietyYear);
					} else {
						// Do nothing. These people did not require post-secondary, so it won't find any for them!
					} // end if (check if person who did not receive an institution was supposed to attend one or not)
				} else {
					String institution = (String)Distribution.uniformRandomObject(schoolsByType);
					//System.out.println(institution);
				
					// Add post-secondary school to archive. Assume that attr.education is the type of school this school is (i.e. U or C)
					person.getSchoolHistory().addEntry(new String[] {person.getEducation(), institution}, Configuration.SocietyYear);

					// If the person's post-secondary institution is in the society, then add it to the local archive.
					if (PSLocation.equals(Configuration.SocietyName)) {
						person.getSocietalSchoolHistory().addEntry(new String[] {person.getEducation(), institution}, Configuration.SocietyYear);
						//socSchoolHistory.addEntry(institution, PSStartYear, PSEndYear);
					} // end if (institution location is in the local society)

				} // end if (no institutions were found for the given criteria)
				

			} else {
				// Person has already begun at post-secondary school, so just update their current year there.
				person.getSchoolHistory().updateSameLastEntry();
				
				
				
				if (person.getCurrentHometown().equals(Configuration.SocietyName)) {
					
					// DEBUG
					if (person.getSocietalSchoolHistory().isEmpty()) {
						System.err.println("Person " + person.getID() + " has no local schools yet!");
						DebugTools.printArray(person.getSchoolHistory());
						DebugTools.printArray(person.getSocietalSchoolHistory());
						DebugTools.printArray(person.getHometownHistory());
						DebugTools.printArray(person.getSocietalHometownHistory());
					}
					// DEBUG
					
					
					else
					
						person.getSocietalSchoolHistory().updateSameLastEntry();
					
					
					
				} // end if (check if new location is in local society)

			} // end if (check if person is just beginning at post-secondary school or has already been going there)
*/
		} else {
			// ---------------------------------------
			// FINISHED ALL SCHOOL.
			// ---------------------------------------
			// Do nothing.

		} // end if (determine which age phase this person is in)

	} // end evaluatePerson_Student()
	
	/*	
	private void evaluatePerson_Student (Person person) {
		// This method will assign a school to the person to attend.

		if (ValidationTools.numberIsWithin(person.getAge(), Configuration.SchoolStartAge, Configuration.SchoolFinishAge)) {
			
			String PSLocation = (String)person.getHometownHistory().getActivityAtYear(Configuration.SocietyYear);
			
			// ---------------------------------------
			// ELEMENTARY/SECONDARY SCHOOL.
			// ---------------------------------------
			if (person.getSchoolHistory().isEmpty()) {

				// Person is just starting school, so create new entry for them.
				FindSchoolForNewStudent(person, PSLocation);

			} else {
				// Person is already in school, so update their archives.

				// Get the person's location in the previous year to determine whether or not they are in the same location now.
				String prevLocation = (String)person.getHometownHistory().getActivityAtYear(Configuration.SocietyYear - 1);
				boolean sameHometownAsLastYear;
				if (prevLocation.equals(PSLocation)) {
					sameHometownAsLastYear = true;
				} else {
					sameHometownAsLastYear = false;
				} // end if (check if person is in same location this year as last year)
				
				if (sameHometownAsLastYear) {		// Person is in same hometown as they were in the previous year.
					person.getSchoolHistory().updateSameLastEntry();
					if (PSLocation.equals(Configuration.SocietyName)) {
						person.getSocietalSchoolHistory().updateSameLastEntry();
					} // end if (check if new location is in local society)
					
				} else { 							// Person is in a new hometown this year.
					FindSchoolForNewStudent(person, PSLocation);	
				} // end if (check if hometown this year is the same as the previous year)

			} // end if (check if person has already been assigned into a school)
			
			
			
		} else if (ValidationTools.numberIsWithin(Configuration.SocietyYear, person.getPSStartYear(), person.getPSFinishYear())) {
			// ---------------------------------------
			// POST-SECONDARY SCHOOL.
			// ---------------------------------------
			// Do nothing.
			
			String[] lastSchool = (String[])person.getSchoolHistory().getLastActivityName();
			String lastSchoolType = lastSchool[0]; // The school entries are of the format [schoolType, schoolName].
			
			if (lastSchoolType.equals("Elementary")) {

				
				String PSLocation = (String)person.getHometownHistory().getActivityAtYear(Configuration.SocietyYear);
				
				
				
				// * NOTE * This assumes the institutions were/are in service the whole time this simulation takes place. If this needs to be changed, then the school's time
				//			period would have to be examined to see if it was/is in existence during the person's required time in post-secondary school.
				//String[] schoolsByType = ArrayTools.arrayListToStringArray(Schools.getSchoolsByFilters(Schools.getFullPostSecondarySchoolsDatabase(), new int[] {Schools.School_Type, Schools.School_City}, new Object[] {attr.education, PSLocation}, new int[] {Schools.School_Name}));
				String[] schoolsByType = ArrayTools.arrayListToStringArray(InstitutionSet.getInstitutionsByFilters(InstitutionSet.PostSecondarySchoolsList, new int[] {InstitutionSet.Institution_Subtype, InstitutionSet.Institution_City}, new Object[] {person.getEducation(), PSLocation}, new int[] {InstitutionSet.Institution_Name}));

				// Check if there were no post-secondary schools found for the person (either because they didn't require any or if they are too young yet).
				if (schoolsByType.length == 0) {
					///////
					// NOTE: the following inner if-statement block can be removed. It was a simple debugging check, but not necessary.
					///////
					if (person.getPSEducationYears() > 0) {
						//System.err.println("In AttributeAssigner->assignSchoolHistory(), there were no post-secondary institutions for this person!");
						// Add post-secondary school to archive. Assume that attr.education is the type of school this school is (i.e. U or C)
						person.getSchoolHistory().addEntry(new String[] {person.getEducation(), "External Institution"}, Configuration.SocietyYear);
					} else {
						// Do nothing. These people did not require post-secondary, so it won't find any for them!
					} // end if (check if person who did not receive an institution was supposed to attend one or not)
				} else {
					String institution = (String)Distribution.uniformRandomObject(schoolsByType);
					//System.out.println(institution);
				
					// Add post-secondary school to archive. Assume that attr.education is the type of school this school is (i.e. U or C)
					person.getSchoolHistory().addEntry(new String[] {person.getEducation(), institution}, Configuration.SocietyYear);

					// If the person's post-secondary institution is in the society, then add it to the local archive.
					if (PSLocation.equals(Configuration.SocietyName)) {
						person.getSocietalSchoolHistory().addEntry(new String[] {person.getEducation(), institution}, Configuration.SocietyYear);
						//socSchoolHistory.addEntry(institution, PSStartYear, PSEndYear);
					} // end if (institution location is in the local society)

				} // end if (no institutions were found for the given criteria)
				

			} else {
				// Person has already begun at post-secondary school, so just update their current year there.
				person.getSchoolHistory().updateSameLastEntry();
				
				
				
				if (person.getCurrentHometown().equals(Configuration.SocietyName)) {
					
					// DEBUG
					if (person.getSocietalSchoolHistory().isEmpty()) {
						System.err.println("Person " + person.getID() + " has no local schools yet!");
						DebugTools.printArray(person.getSchoolHistory());
						DebugTools.printArray(person.getSocietalSchoolHistory());
						DebugTools.printArray(person.getHometownHistory());
						DebugTools.printArray(person.getSocietalHometownHistory());
					}
					// DEBUG
					
					
					else
					
						person.getSocietalSchoolHistory().updateSameLastEntry();
					
					
					
				} // end if (check if new location is in local society)

			} // end if (check if person is just beginning at post-secondary school or has already been going there)

		} else {
			// ---------------------------------------
			// FINISHED ALL SCHOOL.
			// ---------------------------------------
			// Do nothing.

		} // end if (determine which age phase this person is in)

	} // end evaluatePerson_Student()
	*/
	
	
	
	
	
	
	
	
	
	
	private void evaluatePerson_Unemployed (Person person) {
		// This method helps an unemployed person to search for an appropriate job. They may not end up finding one or getting
		// the job they find, but this will potentially get them employed.
		
		//System.out.println("Unemployed person looking for work... Hometown = " + person.getHometownHistory().getLastActivityName());
		
		
		
		//if (person.getID() == 42) { System.err.println("Debugging 42 in " + Configuration.SocietyYear + " | " + person.getCurrentPosition() + " ^ evaluatePerson_Unemployed()"); }
		
		
		boolean jobSuccess, extJobSuccess;
		
		// DETERMINE WHERE THE PERSON CURRENTLY LIVES.
		if (person.getCurrentHometown().equals(Configuration.SocietyName)) {
			// IF THE PERSON LIVES LOCALLY.

			jobSuccess = SearchForJob(person);
			
			
			
			// If person did not get a job locally, then try to get employment externally.
			if (jobSuccess) {
				// Person found a job in the local society.
				// Do nothing. The job has been added to the work archive from within SearchForJob().
				//System.err.println("AAA Person " + person.getID() + " just got a job!!");
				//if (person.getID() == 36) { System.err.println("AAA Person " + person.getID() + " just got a job!!"); }
			} else {
				// Person could not find job in the local society, so search externally now for a job.
				extJobSuccess = SearchForExternalJob(person);
				
				
				
				// If local person found work externally, then move them to the other society. And their family must move too!
				if (extJobSuccess) {
					// TODO MOVE OUT OF LONDON! AND FAMILY TOO!
					String newCity = Distribution.uniformRandomObjectStr(Configuration.OtherCities);
					//System.err.println("AAA Person " + person.getID() + " just got a job externally!! ... Moving to: " + newCity);
					//if (person.getID() == 36) { System.err.println("AAA Person " + person.getID() + " just got a job externally!! ... Moving to: " + newCity + " in year " + Configuration.SocietyYear); }
					person.moveFamilyToCity( newCity, false );
				} // end if (check if external person found work in this simulation society)
				
			} // end if (check if person is still unemployed)
			
			
		} else {
			// IF THE PERSON DOES NOT LIVE LOCALLY.

			// Begin by looking for a job in their current hometown (which is external because they do not live in our society)
			extJobSuccess = SearchForExternalJob(person);
			
			if (extJobSuccess) {
				// Person found a job in their own hometown (not in this simulation's society)
				// TODO Add External Entry to work archive
				//System.err.println("BBB Person " + person.getID() + " just got a job in their hometown!!");
				//if (person.getID() == 36) { System.err.println("BBB Person " + person.getID() + " just got a job in their hometown!!"); }
			} else {
				// Person could not find job in their hometown, so search now in this society for a job.
				jobSuccess = SearchForJob(person);

				// If external person found work in the local society, then move them to this society.
				if (jobSuccess) {
					// TODO MOVE TO LONDON! AND FAMILY TOO!
					//System.err.println("BBB Person " + person.getID() + " just got a job in London!!");
					//if (person.getID() == 36) { System.err.println("BBB Person " + person.getID() + " just got a job in London!!"); }
					person.moveFamilyToCity( Configuration.SocietyName, true );
					
				} // end if (check if external person found work in this simulation society)

			} // end if (check if external person could not find employment in their current hometown)
			
			
		} // end if (check if person lives in local society)
		
		
		

	} // end evaluatePerson_Unemployed()
	
	
	private void evaluatePerson_Working (Person person) {
		// Evaluate person's workplace placement. Use a randomizer to see if the person loses their job this year.
		// The randomization will be based onthe number of years the person has worked at the current place. The longer they have
		// worked there, the less likely they are to lose that job.
		// If person loses their job, they are considered UNEMPLOYED again. Otherwise, they keep the same job they already have.

		//if (person.getID() == 42) { System.err.println("Debugging 42 in " + Configuration.SocietyYear + " | " + person.getCurrentPosition() + " ^ evaluatePerson_Working()"); }

		int numYearsWorking = 0;

		if (!person.getWorkHistory().isEmpty()) {
			int[] currWorkplacePeriod = person.getWorkHistory().getLastActivityPeriod();
			String[] work = (String[])person.getWorkHistory().getLastActivityName();
			//System.out.println("Person #" + person.getID() + " has been working at " + work[0] +
			//		" on the period, [" + currWorkplacePeriod[0] + "," + currWorkplacePeriod[1] + "].");
			numYearsWorking = currWorkplacePeriod[1] - currWorkplacePeriod[0];
		} else {
			// Person has an empty work history, so do nothing.
			
			System.out.println("Person #" + person.getID() + " POSITION: " + person.getCurrentPosition());
			DebugTools.printArray(person.getHometownHistory());
			DebugTools.printArray(person.getWorkHistory());
			
		} // end if (check if person has had a job before)
		
		//System.out.println("Person worked at current place for " + numYearsWorking + " years.");
		
		double rndKeepJob = Distribution.uniform(0.0, 1.0);
		
		
		
		// We calculate the probability of keeping their job as p = 0.5^n, where n is the number of years they've worked there.
		double p_keepJob;
		if (numYearsWorking <= 1) {
			// If person has worked at workplace for 0 years, let them stay on. Give them a chance!
			p_keepJob = 1.0;
		} else {
			// Calculate probability of keeping job as 0.5^n.
			p_keepJob = Math.pow(0.5, numYearsWorking);
		} // end if (check if number of years at workplace is 0)
		
		
		// --- EDIT --- No longer using this. We want people to stay at least 1 year at a job.
		// We calculate the probability of keeping their job as p = 0.5^n, where n is the number of years they've worked there.
		/*
		double p_keepJob;
		if (numYearsWorking == 0) {
			// If person has worked at workplace for 0 years (is this possible?) then default to a low probability.
			p_keepJob = 0.3;
		} else {
			// Calculate probability of keeping job as 0.5^n.
			p_keepJob = Math.pow(0.5, numYearsWorking);
		} // end if (check if number of years at workplace is 0)
		*/
		
		
		if (rndKeepJob > p_keepJob) {
			// Lose job.
			
			//System.err.println("Working person chilling (SimStepper)... major layoffs");
			person.setCurrentPosition( Person.CURR_POSITION_UNEMPLOYED );
			person.setIncome(0);

			
			// This seems incorrect, but it is necessary.
			// We update the last work history because we assume the person will work into the new year. The UNEMPLOYED status
			// will stop this from continually updating for more years.
			person.getWorkHistory().updateSameLastEntry();
			
			//System.err.println("Person " + person.getID() + " just lost their job in year " + Configuration.SocietyYear);
			
			
			//person.getWorkHistory().endActivityInCurrentYear();
			//System.out.println("person.getCurrentPosition() = " + person.getCurrentPosition());
			//System.err.println("Person " + person.getID() + " just LOST their job.");
			
		
		} else {
			// Person still has same career at same workplace, so just update the work archive to include the new year!
			person.getWorkHistory().updateSameLastEntry();
			
			
			
		} // end if (check if person keeps their job)
		
	} // end evaluatePerson_Working()
	
	// ===============================================================================
	// ADDITIONAL FUNCTIONS.
	// ===============================================================================
	
	private void FindSchoolForNewStudent (Person person, String personLocation) {
		// This method will find a school for the new student (who will just be beginning at the chosen school), and it adds
		// the school to the person's schoolHistory and, if local, to the person's societalSchoolHistory.
		
		String schoolName = "Elementary"; // Default to "Elementary". This will be changed IF school is local.
		
		if (personLocation.equals(Configuration.SocietyName)) {
			// Person is just starting school so select a school for them.
			double[] schoolProbs = SchoolSet.getSchoolProbabilityTable(Configuration.SocietyYear);
			int rndSchoolIndex = Distribution.custom(schoolProbs);
			schoolName = InstitutionSet.getSchoolTitleAt(InstitutionSet.ElementarySchoolsList, rndSchoolIndex);
			person.getSocietalSchoolHistory().addEntry(new String[] {"Elementary", schoolName}, Configuration.SocietyYear);
		} // end if (check if person is going to school in local society)
		//System.out.println("A) PSLocation = " + personLocation);
		person.getSchoolHistory().addEntry(new String[] {"Elementary", schoolName}, Configuration.SocietyYear);
		
	} // end FindSchoolForNewStudent()
	
	private void FindPostSecondarySchoolForNewStudent (Person person, String personLocation, String instType) {
		// This method will find a post-secondary school for the new student (who will just be beginning at the chosen school), and it
		// adds the school to the person's schoolHistory and, if local, to the person's societalSchoolHistory.
		
		String institution = "External Institution"; // Default to "External Institution". This will be changed IF school is local.
		
		if (personLocation.equals(Configuration.SocietyName)) {
			// Person is just starting school so select a school for them.
			String[] schoolsByType = ArrayTools.arrayListToStringArray(InstitutionSet.getInstitutionsByFilters(InstitutionSet.PostSecondarySchoolsList, new int[] {InstitutionSet.Institution_Subtype, InstitutionSet.Institution_City}, new Object[] {instType, personLocation}, new int[] {InstitutionSet.Institution_Name}));

			institution = (String)Distribution.uniformRandomObject(schoolsByType);
			person.getSocietalSchoolHistory().addEntry(new String[] {instType, institution}, Configuration.SocietyYear);

		} // end if (check if person is going to school in local society)
		//System.out.println("A) PSLocation = " + personLocation);
		person.getSchoolHistory().addEntry(new String[] {instType, institution}, Configuration.SocietyYear);
		
	} // end FindPostSecondarySchoolForNewStudent()

	private boolean SearchForJob (Person person) {
		// This method will search for a potential workplace for the person to find employment at. The person may or may not
		// actually get a job at this time with the workplace found.
		//
		// param person: the Person who is searching for a job
		//
		// return: a boolean flag indicating whether or not the person received a job at this time

		//if (person.getID() == 42) { System.err.println("Debugging 42 in " + Configuration.SocietyYear + " | " + person.getCurrentPosition() + " ^ SearchForJob()"); }
		
		
		String[] possibleWorkplaces = Careers.getWorkplaceById(person.getCareer());
		
		// If the peron's career does not match any workplace options, then stop now. (This should only ever be the case for "Unemployed, id="00000").
		if (possibleWorkplaces.length == 0) {
			// Stay unemployed. No jobs were found.
			return false;
		} // end if (check if any possible work locations were found)
		
		String chosenWorkplace = (String)Distribution.uniformRandomObject(possibleWorkplaces);
		
		// Add career to archive to start from the year they finished school until present.
		String[] workPositionInfo = new String[] {chosenWorkplace, person.getCareer()};				// Note the array is of the format [workplaceID, careerID].
		Careers.getWorkplaceById(person.getCareer());
		
		
		if (person.getIsInSchool()) {
			// This shouldn't be executed ever, but just in case the person is still in school, then exit this.
			System.err.println("In SimulationStepper->evaluatePerson_Unemployed(); the person is considered unemployed but is still in school. Fix this.");
			return false;
		} // end if (ensure that person has graduated from the program)
		
		
		// -------------------------------------------------------------------------------
		// At this point, the person has a potential job, but a randomizer will determine
		// whether or not the person gets the job.
		// -------------------------------------------------------------------------------
		
		double rnd_GetJob = Distribution.uniform(0.0, 1.0);
		
		if (rnd_GetJob <= Configuration.p_getJobFromUnemployment) {
		
			
		
			ArrayList careerInfo = Careers.getCareerById(Careers.getFullCareersDatabase(), person.getCareer());
		
			// Add the new job to the person's work history and societal work history.
			person.getWorkHistory().addEntry(workPositionInfo, Configuration.SocietyYear);
			person.getSocietalWorkHistory().addEntry(workPositionInfo, Configuration.SocietyYear);

			// Get the base income associated with the person's career.
			int careerIncome = Integer.parseInt((String)careerInfo.get(Careers.Career_Salary));
			// Randomly choose an amount (positive or negative) to offset the person's income from the base.
			int salarySTD = Distribution.uniform(-Configuration.SalarySTD, Configuration.SalarySTD);
			person.setIncome(careerIncome + salarySTD);

			// UPDATE person's position to WORKING!
			person.setCurrentPosition( Person.CURR_POSITION_WORKING );

			//System.err.println("><><><><><><><><><><>< PERSON " + person.getID() + " GOT A JOB! ><>< " + person.getCareer() + " ><><><><><><><><><><><><><><");
			
			// Return true to indicate that the person successfully landed a job.
			return true;

		} // end if (check if person will actually get the employment, based on random number)

		// Return false to indicate that the person did not get a job.
		return false;
		
	} // end SearchForJob()
	
	
	
	
	private boolean SearchForExternalJob (Person person) {
		// This method will decide whether or not the person finds a job in another location. The person may live in the
		// simulation society OR externally, but either way they will search for a job externally. This is simplified greatly!
		//
		// param person: the Person who is searching for a job
		//
		// return: a boolean flag indicating whether or not the person received a job at this time
		
		//if (person.getID() == 42) { System.err.println("Debugging 42 in " + Configuration.SocietyYear + " | " + person.getCurrentPosition() + " ^ SearchForExternalJob()"); }
		
		double rnd_GetJob = Distribution.uniform(0.0, 1.0);
		
		
		// THIS IS VERY SIMPLIFIED. Simply use a random number to decide whether or not the person found a job.
		if (rnd_GetJob <= Configuration.p_getJobFromUnemployment) {
			// Person received a job, so return true to indicate this job success.
			
			String chosenWorkplace = "External Company";
			
			String[] workPositionInfo = new String[] {chosenWorkplace, person.getCareer()};				// Note the array is of the format [workplaceID, careerID].
			person.getWorkHistory().addEntry(workPositionInfo, Configuration.SocietyYear);
			
			
			ArrayList careerInfo = Careers.getCareerById(Careers.getFullCareersDatabase(), person.getCareer());
			// Get the base income associated with the person's career.
			int careerIncome = Integer.parseInt((String)careerInfo.get(Careers.Career_Salary));
			// Randomly choose an amount (positive or negative) to offset the person's income from the base.
			int salarySTD = Distribution.uniform(-Configuration.SalarySTD, Configuration.SalarySTD);
			person.setIncome(careerIncome + salarySTD);

			// UPDATE person's position to WORKING!
			person.setCurrentPosition( Person.CURR_POSITION_WORKING );
			
			
			
			return true;
		} // end if (determine whether or not the person receives employment at this time)
		
		// If the randomizer didn't yield the person finding work, then return false to show that the person is still unemployed.
		return false;
	} // end SearchForExternalJob()
	
} // end SimulationStepper class
