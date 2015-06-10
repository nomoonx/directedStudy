package thesis_network_growth;

import java.util.ArrayList;
import java.util.Hashtable;

import thesis_network_growth.m1_dyadic.Dyadic_NetworkGenerator;

public class PersonGenerator {
	
	public static final int ALL_RANDOM = 0;
	public static final int RANDOM_MALE = 1;
	public static final int RANDOM_FEMALE = 2;
	public static final int RANDOM_IMMIGRANT = 3;
	public static final int SPOUSE_MALE = 4;
	public static final int SPOUSE_FEMALE = 5;
	public static final int CHILD = 6;
	 
	
	public static Person GeneratePerson (int personType, Person[] optionalPersons) {
		// This static function will create a person based on the input type and optionally, based upon other persons.
		// param personType: an integer that indicates what type of person to generate
		// param optionalPersons: an optional array of existing persons from which the new person will be based. For example, in the case of
		// generating a spouse for person A, the spouse will have some attributes similar to A, so A will be sent in in this array. For those that
		// are not based on other persons, this argument can be null.
		// returns: a Person instance of the newly generated agent.

		//PersonAttributes personAttrs;
		//personAttrs = new PersonAttributes();
		
		Person person = new Person();

		switch (personType) {
			case ALL_RANDOM:
				break;
			
			case RANDOM_MALE:
				person.setSex(1);
				break;
			
			case RANDOM_FEMALE:
				person.setSex(0);
				break;
			
			case RANDOM_IMMIGRANT:
				person.setNationality("Elsewhere");
				break;
			
			case SPOUSE_MALE:
				person.setSex(1);
				if (optionalPersons != null && optionalPersons.length == 1) {
					person = AssignSpousalAttributes(person, optionalPersons[0]);
				} else {
					System.err.print("There must be a person for this spouse to be with!");
				}
				break;
			
			case SPOUSE_FEMALE:
				person.setSex(0);
				if (optionalPersons != null && optionalPersons.length == 1) {
					person = AssignSpousalAttributes(person, optionalPersons[0]);
				} else {
					System.err.print("There must be a person for this spouse to be with!");
				}
				break;
			
			case CHILD:
				if (optionalPersons != null && optionalPersons.length == 2) {
					// GOOD! Do nothing. AssignChildAttributes() is now called from RelationshipCalculator->CreateChildren().
					//person = AssignChildAttributes(person, optionalPersons[0], optionalPersons[1]);
				} else {
					System.err.print("There must be a two persons from whom this child is born!");
				}
				break;
			default:
				break;
		} // end switch-case (personType)
		
		
		
		
		
		
		// Ensure that all missing attributes are filled in now, before assigning the values into the Person object. After this is called, the PersonAttributes will have values for every member variable.
		//personAttrs.fillInMissingAttributes();
		//person.fillInMissingAttributes();
		
		// Lastly, create the Person object.
		//Person person = new Person(personAttrs);

		// Add Person reference to global array.
		//Dyadic_NetworkGenerator.addPersonToArray(person);
		
		// Delete the PersonAttributes object now that it is no longer needed.
		//personAttrs = null;

		// Return the person object.
		return person;
	} // end GeneratePerson()
	
	
	public static Person complete (Person person) {
		// This function completes the addition of a newly generated Person, person, by filling in all its empty attributes and adding the Person to the global array.
		// This used to be done within GeneratePerson(), but it became necessary to separate this step so that a Person could be generated in GeneratePerson() but then
		// assigned specific attributes before being filled in with random attributes.
		//
		// param person: the Person for whom to fill in the data and add to array
		//
		// return: the same Person as is input to the function, but now with completed attributes
		
		person.fillInMissingAttributes();
		person.addPersonToGroups();
		ArtificialSociety.addPersonToSociety(person);
		
		return person;
	} // end complete()
	
	
	
	public static void GenerateCouple (int relType) {
		
		Person personA, personB;
		int personAAge, personBAge;
		int yearStartedRelationship;
		ActivityArchive[] coupleHometownCheckpoints;

		if (relType == RelationshipCalculator.REL_TYPE_MARRIED) {

			personAAge = Distribution.uniform(Configuration.MinMarriedAge, Configuration.MaxMarriedAge);
			personBAge = personAAge + Distribution.uniform(-5, 5);					// personB's age is within 5 years of personA's age.
			yearStartedRelationship = AssignCoupleRelationshipStart(personAAge, personBAge, relType);
			coupleHometownCheckpoints = AssignCoupleHometownHistories(relType, yearStartedRelationship);

		} else if (relType == RelationshipCalculator.REL_TYPE_DATING) {

			personAAge = Distribution.uniform(Configuration.MinDatingAge, Configuration.MaxDatingAge);
			personBAge = personAAge + Distribution.uniform(-5, 5);					// personB's age is within 5 years of personA's age.
			yearStartedRelationship = AssignCoupleRelationshipStart(personAAge, personBAge, relType);
			coupleHometownCheckpoints = AssignCoupleHometownHistories(relType, yearStartedRelationship);

		} else {
			// Handle error in case an unknown relationship type is mistakenly sent in.
			System.err.println("In PersonGenerator->GenerateCouple(); unknown relationship type: " + relType);
			return;
			
		} // end if (checking the relationship type)
		
		//DebugTools.printArray(coupleHometownCheckpoints);
		
		// MALE.
		personA = PersonGenerator.GeneratePerson(1, null);
		personA.setRelationshipStatus(relType); // The relationship status must be set before some other attributes are assigned (career/education for example!).
		personA.setInitAge(personAAge);
		AttributeAssigner.assignBirthYear(personA);
		AttributeAssigner.assignAge(personA);
		AttributeAssigner.assignExpectedYearOfDeath(personA);
		
		//System.out.println("A | " + personA.getAge() + " : " + personA.getExpectedDeathYear());
		personA.setRelationshipStartYear(yearStartedRelationship);
		personA.setHometownCheckpointsHistory(coupleHometownCheckpoints[0]);
		complete(personA); // Important: personA must be completed before personB is created.

		
		
		// FEMALE.
		personB = PersonGenerator.GeneratePerson(5, new Person[] { personA });
		personB.setRelationshipStatus(relType);
		personB.setInitAge(personBAge);
		AttributeAssigner.assignBirthYear(personB);
		AttributeAssigner.assignAge(personB);
		AttributeAssigner.assignExpectedYearOfDeath(personB);
		//System.out.println("B | " + personB.getAge() + " : " + personB.getExpectedDeathYear());
		personB.setRelationshipStartYear(yearStartedRelationship);
		personB.setHometownCheckpointsHistory(coupleHometownCheckpoints[1]);
		complete(personB); // Complete personB.
		

		// Create relationship between the couple.
		PersonGroupAdder.createRelationship(personA, personB, relType);
		
		
		// ----------------------------------------------------------------
		// Check for death(s) in relationship.
		// ----------------------------------------------------------------
		//if (personA.getExpectedDeathYear() <= Configuration.SocietyYear) {
			//personA.die();
		//} // end if (check if personA is dead)
		//if (personB.getExpectedDeathYear() <= Configuration.SocietyYear) {
			//personB.die();
		//} // end if (check if personB is dead)
		
		
	} // end GenerateCouple()
	
	
	
	
	
	
	
	public static Person AssignSpousalAttributes (Person newSpouse, Person person) {
		//System.out.println("AssignSpousalAttributes()!");
		// RACE.
		AttributeAssigner.assignSpouseRace(person, newSpouse);
		//double[] race_probs = Configuration.p_same_race_for_spouse[person.raceIndex]; // Extract the appropriate row of race probabilities.
		//newSpouse.raceIndex = Distribution.custom(race_probs);
		
		// RELIGION.
		AttributeAssigner.assignSpouseReligion(person, newSpouse);
		//double[] religion_probs = Configuration.p_same_religion_for_spouse[person.religionIndex]; // Extract the appropriate row of religion probabilities.
		//newSpouse.religionIndex = Distribution.custom(religion_probs);
		
		// AGE. // EDIT: The ages are assigned in the GenerateCouple() method.
		//AttributeAssigner.assignSpouseAge(person, newSpouse);
		//System.err.println(person.getAge() + " && " + newSpouse.getAge() + " assignSpousalAttributes()");
		
		
		return newSpouse;
	} // end AssignSpousalAttributes()
	
	
	
	
	
	
	
	public static Person AssignChildAttributes (Person child, Person parentA, Person parentB, boolean isBackFilling) {

		// NOTE: the age is now assigned separately because back-filled children are given ages differently than live newborns!
		AssignChildAge(parentA, parentB, child, isBackFilling);
		AssignChildRace(parentA, parentB, child);
		AssignChildReligion(parentA, parentB, child);



		AttributeAssigner.assignChildHometowns(child, parentA, parentB);

		//System.err.println("CREATING CHILD WITH ID #" + child.getID() + " from parents " + parentA.getID() + " and " + parentB.getID() + ".");
		
		return child;
	} // end AssignChildAttributes()

	
	
	
	
	
	// ----------------------------------------------------------------------------------------------------------------------
	// METHODS FOR ASSIGNING CHILDREN ATTRIBUTES
	// ----------------------------------------------------------------------------------------------------------------------
	
	
	private static Person AssignChildAge (Person parentA, Person parentB, Person child, boolean isBackFilled) {
		// Assign the age to the child. If isBackFilled is True, then the child is not a newborn baby, and thus the age
		// will be determined based on the parents' marriage. Otherwise, the child IS a baby so the age will be 0.
		//
		// param child: the Person instance of the child being created
		// param parentA: one of the parents of the child
		// param parentB: the other parent of the child
		// param isBackFilled: the boolean flag indicating whether or not the child is being created after the fact

		if (isBackFilled) {
			// Create child after the fact (not a newborn baby!)
			return AssignChildAge_BackFilled(parentA, parentB, child);

		} else {
			// Create a newborn baby during a live simulation!
			return AssignChildAge_New(child);

		} // end if (check if the child is a newborn baby or a back-filled older person)
		
		
	} // end AssignChildAge()
	
	
	
	private static Person AssignChildAge_New (Person child) {
		// This is called when a new baby is born, and the current society year. This is necessary so that the child isn't
		// given a random age, as is the case when creating back-filled children.
		//
		// param child: the new baby who was just born!
		//
		// return: the baby with the assigned age and year of birth

		
		// Child was born in projectedChildBirthYear, so now just compute their current age.
		int childAge = 0;

		// Set the child's age.
		child.setInitAge(childAge);
		
		// Call method to assign birth year.
		AttributeAssigner.assignBirthYear(child);
		AttributeAssigner.assignAge(child);
		
		return child;
		
	} // end AssignChildAge()
	
	private static Person AssignChildAge_BackFilled (Person A, Person B, Person child) {
		// This is called when a back-filled child is created from a married couple in an initial population.
		// In this case, the child is given a random age and birth year based upon the parents' marriage.
		//
		// param A: one of the parents who had the child
		// param B: the other parent who had the child
		// param child: the child of parents A and B
		//
		// return: the child with an assigned age and year of birth
		
		
		//System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		//System.out.println(A.getAge());
		//System.out.println(B.getAge());
		//System.out.println(A.getRelationshipStartYear() + " | " + B.getRelationshipStartYear());
		
		// For simplicity, children's ages are randomly taken from a distribution over the years following the parents' marriage.
		int numYearsIntoMarriage = (int)Math.round(DistributionParser.parseStatisticalDistribution(Configuration.ParentHaveChildYearDistr));
		numYearsIntoMarriage = ValidationTools.clipValueToRange(numYearsIntoMarriage, Configuration.ParentHaveChildYearMin, Configuration.ParentHaveChildYearMax);

		// In case the chosen year for the child's birth is later than the current society year, clip it to the current year.
		int projectedChildBirthYear = A.getRelationshipStartYear() + numYearsIntoMarriage;
		if (projectedChildBirthYear > Configuration.SocietyYear) {
			// Limit the child's birth to be in current year.
			projectedChildBirthYear = Configuration.SocietyYear;
		} // end if (check if projected child's birth would be beyond current year)

		// Child was born in projectedChildBirthYear, so now just compute their current age.
		int childAge = Configuration.SocietyYear - projectedChildBirthYear;

		// Set the child's age.
		child.setInitAge(childAge);
		// Call method to assign birth year.
		AttributeAssigner.assignBirthYear(child);
		AttributeAssigner.assignAge(child);

		return child;
	} // end AssignChildAge()
	
	public static void AssignChildRace (Person A, Person B, Person child) {
		// Assign a race to a new child.
		// For simplicity, we will use one random parent's race as the child's race, if the parents are interracial.
		// param A: one of the parents of the child
		// param B: the other parent of the child
		// param child: the new child from parents A and B
		
		int childRace;

		if (A.getRace() == B.getRace()) {	// Same race.
			childRace= A.getRace();
		} else {							// Interracial.
			// Select a random parent, and use their race as the child's race.
			int rndParentIndex = Distribution.uniform(0, 1);
			if (rndParentIndex == 0) {		// If 0, use parent A's race.
				childRace = A.getRace();
			} else {						// If 1, use parent B's race.
				childRace = B.getRace();
			} // end if (determine which parent was randomly chosen)

		} // end if (check if parents are of same race)
		
		
		child.setRace(childRace);
		
	} // end AssignChildRace()
	
	public static void AssignChildReligion (Person A, Person B, Person child) {
		// Assign a religion to a new child.
		// For simplicity, we will use one random parent's religion as the child's religion, if the parents are of different religions.
		// param A: one of the parents of the child
		// param B: the other parent of the child
		// param child: the new child from parents A and B
		
		int childReligion;

		if (A.getReligion() == B.getReligion()) {	// Same religion.
			childReligion= A.getReligion();
		} else {									// Different religions.
			// Select a random parent, and use their religion as the child's religion.
			int rndParentIndex = Distribution.uniform(0, 1);
			if (rndParentIndex == 0) {		// If 0, use parent A's religion.
				childReligion = A.getReligion();
			} else {						// If 1, use parent B's religion.
				childReligion = B.getReligion();
			} // end if (determine which parent was randomly chosen)

		} // end if (check if parents are of same religion)
		
		
		child.setReligion(childReligion);
	} // end AssignChildReligion()
	
	
	
	
	// ----------------------------------------------------------------------------------------------------------------------
	// METHODS FOR ASSIGNING COUPLE ATTRIBUTES
	// ----------------------------------------------------------------------------------------------------------------------
	
	
	
	
	
	
	
	public static int AssignCoupleRelationshipStart (int pAAge, int pBAge, int relType) {
		// Determine the year that the couple got married. This is obtained from an age distribution, and the couples' ages.
		//
		// param pAAge: the age of person A
		// param pBAge: the age of person B
		// param relType: the relationship type (Dating, Marriage, etc.)
		//
		// return: the integer representing the year that the couple's relationship began
		
		int relStartYear;
		
		if (relType == RelationshipCalculator.REL_TYPE_MARRIED) {
			// Married.

			int NormMarriedAge = (int)Math.round(DistributionParser.parseStatisticalDistribution(Configuration.MarriageAgeDistr));
			int pAMarriedAge = ValidationTools.clipValueToRange(NormMarriedAge, Configuration.MinMarriedAge, pAAge);
			int pABirthYear = Configuration.SocietyYear - pAAge;

			relStartYear = pABirthYear + pAMarriedAge;

			//System.out.println(pAAge + "," + pBAge + " . " + pAMarriedAge + " | got married in year " + relStartYear + " at ages (" + (relStartYear-pABirthYear) + "," + (relStartYear-pBBirthYear) + ")");
			
		} else if (relType == RelationshipCalculator.REL_TYPE_DATING) {
			// Dating.

			
			int NormDatingAge = (int)Math.round(DistributionParser.parseStatisticalDistribution(Configuration.DatingAgeDistr));
			int pAMarriedAge = ValidationTools.clipValueToRange(NormDatingAge, Configuration.MinDatingAge, pAAge);
			int pABirthYear = Configuration.SocietyYear - pAAge;

			relStartYear = pABirthYear + pAMarriedAge;

		
		} else {
			// Handle error in case an unknown relationship type is mistakenly sent in.
			System.err.println("In PersonGenerator->GenerateCouple(); unknown relationship type: "+ relType);
			relStartYear = -1;
		} // end if (checking the relationship type)
		
		return relStartYear;
		
	} // end AssignCoupleRelationshipStart()
	
	
	
	

	public static ActivityArchive[] AssignCoupleHometownHistories (int relType, int startYear) {
		// Determine the hometowns of a couple who are created as a dyadic unit.
		//
		// param relType: the relationship type between the couple (either MARRIED or DATING)
		// param startYear: the year that the couple formed this relationship
		// param p_alwaysLiveTogether: the probability that the couple always lived together (during marriage!) - NOTE: this is deprecated!
		//
		// return: the array of the ActivityArchive objects for the couple

		ActivityArchive tempHometownArchive = new ActivityArchive();
		ActivityArchive tempLocalHometownArchive = new ActivityArchive();
		ActivityArchive personAHometowns = new ActivityArchive();
		ActivityArchive personBHometowns = new ActivityArchive();

		
		// Determine what type of relationship the couple are in.
		if (relType == RelationshipCalculator.REL_TYPE_MARRIED) {
		

			// Create a list of hometowns during the period that the couple is married.
			//System.err.println(startYear);
			ActivityArchive.addHometownsForPeriod(tempHometownArchive, tempLocalHometownArchive, startYear, Configuration.SocietyYear, Configuration.AllCities);
			
			// Begin with both people having the same history of hometowns as a default.
			personAHometowns = (ActivityArchive)tempHometownArchive.clone();
			personBHometowns = (ActivityArchive)tempHometownArchive.clone();
		
		} else if (relType == RelationshipCalculator.REL_TYPE_DATING) {
			//System.err.println("In PersonGenerator->AssignCoupleHometownHistories(); should dating couples live in the same hometown?!?!");
			
		} else {
			System.err.println("In PersonGenerator->AssignCoupleHometownHistories(); unknwon relationship type found: " + relType);
		} // end if (determine the relationship type)

		// Return the new set of hometown histories for the couple.
		return new ActivityArchive[] {personAHometowns, personBHometowns};

	} // end AssignCoupleHometownHistories()
	
	
	
	/*
	public static ActivityArchive[] AssignCoupleHometownHistories (int relType, int startYear, double p_alwaysLiveTogether) {
		//
		

		ActivityArchive tempHometownArchive = new ActivityArchive();
		ActivityArchive tempLocalHometownArchive = new ActivityArchive();
		ActivityArchive personAHometowns = new ActivityArchive();
		ActivityArchive personBHometowns = new ActivityArchive();

		

		// Create a list of hometowns during the period that the couple is married.
		ActivityArchive.addHometownsForPeriod(tempHometownArchive, tempLocalHometownArchive, 2, 5, startYear, Configuration.SocietyYear, Configuration.AllCities);
			
		// Begin with both people having the same history of hometowns as a default.
		personAHometowns = (ActivityArchive)tempHometownArchive.clone();
		personBHometowns = (ActivityArchive)tempHometownArchive.clone();
			
			
			
			
			
			
			
			
		// Use randomness to allow long-distance for a period.
		double rndAlwaysTogether = Distribution.uniform(0.0, 1.0);

		// -----------------------------------------------------------------------
		// Check if couple always lives together or not.
		// Note: For simplicity, we assume there is at most one period in which
		// the couple lives apart.
		// -----------------------------------------------------------------------
		if (rndAlwaysTogether <= p_alwaysLiveTogether) {
			// If the couple always lived together (never apart).

			//DebugTools.printArray(personAHometowns);
			//System.out.println("-o-");
			//DebugTools.printArray(personBHometowns);

				
		} else {
			// If the couple spent some periods living in different locations.
			// Note: For simplicity, we assume there is only one period where the two people are in two distinct cities. This could be modified to allow multiple periods.

			// -------------------------------------------------------------------------------------
			// Now change one person's history to account for the time they lived apart.
			// -------------------------------------------------------------------------------------
			// Choose which person will live in a different location.
			int rndPartner = Distribution.uniform(0, 1); // 0 for personA or 1 for personB

			// Move one partner to different hometown.
			if (rndPartner == 0) {	// personA moves to a different location
				MovePersonToNewHometown(personAHometowns, 0);
			} else {				// personB moves to a different location
				MovePersonToNewHometown(personBHometowns, 1);
			} // end if (determine which spouse moves to a different location)

			//DebugTools.printArray(personAHometowns);
			//System.out.println("-o-");
			//DebugTools.printArray(personBHometowns);

		} // end if (check if couple always lived together)

		// Return the new set of hometown histories for the couple.
		return new ActivityArchive[] {personAHometowns, personBHometowns};

	} // end AssignCoupleHometownHistories()
	*/

	
	
	
	
	
	/*
	private static void MovePersonToNewHometown (ActivityArchive hist, int TMP) {

		// Select a random entry index from the hist archive (one hometown entry).
		int rndPeriod = Distribution.uniform(0, hist.size() - 1);
		
		Hashtable<Object, int[]> dict = (Hashtable<Object, int[]>)hist.get(rndPeriod);
		ArrayList<Object> arr = hist.getDictEntryNameAndYears(dict);
		String origLocation = (String)arr.get(0);
		int[] period = (int[])arr.get(1);

		
		// Select new location for this person.
		ArrayList<String> possCitiesAwayFromSpouse = ArrayTools.removeFromArray(Configuration.AllCities, origLocation);
		String selDistantCity = (String)Distribution.uniformRandomObjectStr(possCitiesAwayFromSpouse);

		
		//System.out.print(TMP + " -> " + origLocation + " | moved to " + selDistantCity);
		//DebugTools.printInlineArray(period);
		//System.out.println();
		
		

		
		// Add different location in place of the original location for the chosen period.
		dict = new Hashtable<Object, int[]>();
		dict.put(selDistantCity, period);
		hist.set(rndPeriod, dict);
		
	} // end MovePersonToNewHometown()
	*/
} // end PersonGenerator class