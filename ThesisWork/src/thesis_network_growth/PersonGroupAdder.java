package thesis_network_growth;

import java.util.ArrayList;
import java.util.Hashtable;


public class PersonGroupAdder {

	static int nextFamilyID = 0;
	
	
	public static void createRelationship (Person personA, Person personB, int relType) {
		// Indicate the relationship between the two persons, personA and personB.
		// param personA: one person in the relationship
		// param personB: the other person in the relationship
		// param relType: an integer representing the relationship type {0 = Single, 1 = Married, 2 = Dating}
		
		personA.setRelationshipStatus(relType);
		personB.setRelationshipStatus(relType);
		
		personA.setPartnerID(personB.getID());
		personB.setPartnerID(personA.getID());
		
		RelationshipCalculator.calculateAndSetInterestSimilarity(personA, personB);


		RelationshipCalculator.CalculateAndSetRelationshipStrength(personA, personB, 0); // Before children are created, so pass = 0.

		// DELETED assignRelationshipStart() on Feb. 16.
		// Determine the year this relationship began (if married, then it's the year that they became married, not the year they started dating).
		//AttributeAssigner.assignRelationshipStart(personA, personB, relType);
		
		// Calculate relationship strength between couple.
		//double relStrength = RelationshipCalculator.CalculateRelationshipStrength(personA, personB);
		//personA.relationshipStrength = relStrength;
		//personB.relationshipStrength = relStrength;
		
		int familyID = PersonGroupAdder.nextFamilyID;
		personA.setFamilyID(familyID);
		personB.setFamilyID(familyID);
		
		// Increment static variable for nextFamilyID, so each new relationship creates a new family ID.
		PersonGroupAdder.nextFamilyID++;
		
	} // end createRelationship()
	
	/*
	public static double CalculateRelationshipStrength (Person personA, Person personB) {
		// Calculate the strength of the relationship between the two people.
		// param personA: one person in the relationship
		// param personB: the other person in the relationship
		
		double strength = 0.0;
		
		// Factors to include:
		//	- Relationship Type
		//	- Have children? How many?
		//	- How long together (??)
		//	- MBTI traits (?)
		//	- I.S. based on array of interests
		//	- Race, religion, etc.
		//	- Random factor
		
		
		return strength;
	} // end CalculateRelationshipStrength()
	*/

	public static void createChildrenConnections (Person parentA, Person parentB, Person[] children) {
		// Add family connections for parents and children, including parent, children, and siblings connections.
		// param parentA: the first parent in the family
		// param parentB: the second parent in the family
		// param children: an array of the children in the family


		if (parentA.getFamilyID() == parentB.getFamilyID()) {
			// Check if parents belong to the same family. This should always be the case, unless the simulation includes family separations and re-marriages.

			int c, k;

			// Loop through all children from the given couple.
			for (c = 0; c < children.length; c++) {
				
				// Add both parents to child's parent_ids list.
				//children[c].parent_ids.add(new Integer(parentA.getID()));
				//children[c].parent_ids.add(new Integer(parentB.getID()));
				children[c].getParentIDs().add(new Integer(parentA.getID()));
				children[c].getParentIDs().add(new Integer(parentB.getID()));
				
				// Add child to each parent's children_ids list.
				//parentA.children_ids.add(new Integer(children[c].getID()));
				//parentB.children_ids.add(new Integer(children[c].getID()));
				parentA.getChildrenIDs().add(new Integer(children[c].getID()));
				parentB.getChildrenIDs().add(new Integer(children[c].getID()));
				
				
				for (k = 0; k < children.length; k++) {
					
					// Add child to other children's siblings_ids list.
					if (c != k) {
						//System.out.println("Adding sibling connection for children " + c + " and " + k + " ||| " + children[k].getID() + "  " + children[c].getID());
						children[c].getSiblingIDs().add(new Integer(children[k].getID()));
						//children[k].getSiblingIDs().add(new Integer(children[c].getID()));
					} else { // end if (only add sibling connections if it is a different child (c != k))
						//System.out.println("no connection for " + c + " and " + k);
					}
				} // end for k (loop again through all children in nested loop so each child can have connection with its siblings) 
				
				//System.out.println("Creating child " + children[c].getID() + " connections...");
				//DebugTools.printArray(children[c].getSiblingIDs().toArray());
				
				
			} // end for c (loop through children)
			
		} // end if (two parents are in the same family)

		
		

		
	} // end createRelationship()
	
	
	
	
	
	
	
	public static void AddPersonToAllGroups (Person person) {
		// This function calls the individual smaller group-adder functions to ensure the given Person, person, is added to all the groups to which they belong.
		// The groups include school institutions they attended, workplaces they worked at, and extra-curricular groups like clubs, temples, gyms, etc.
		// Note that in this simulation, people are not assigned homes, and thus neighbourhood is not part of this group assignment, but in a different simulation,
		// neighbourhoods and specific buildings could be considered groups so that people who live nearby are put into locality groups together.
		//
		// param person: the Person instance who is being added to the groups

		// Schools.
		AddToSchoolGroups(person);

		// Work.
		AddToWorkGroups(person);

		// Temples.
		AddToTempleGroups(person);

		// Clubs.
		AddToClubGroups(person);

		
		//if (person.getID() == 48) {
			//DebugTools.printArray(person.getSchoolHistory());
			//DebugTools.printArray(person.getSocietalSchoolHistory());
			//DebugTools.printArray(person.getHometownHistory());
			//DebugTools.printArray(person.getGroups().toArray());
		//}
		
	} // end AddPersonToAllGroups()
	
	public static void UpdatePersonInAllGroups (Person person) {
		// This function calls the individual smaller group-updater functions to ensure the given Person, person, has updated groups during an active simulation.
		// The groups include school institutions they attended, workplaces they worked at, and extra-curricular groups like clubs, temples, gyms, etc.
		// Note that in this simulation, people are not assigned homes, and thus neighbourhood is not part of this group assignment, but in a different simulation,
		// neighbourhoods and specific buildings could be considered groups so that people who live nearby are put into locality groups together.
		//
		// param person: the Person instance who is being added to the groups

		// Schools.
		UpdateSchoolGroups(person);

		// Work.
		UpdateWorkGroups(person);

		// Temples.
		UpdateTempleGroups(person);

		// Clubs.
		UpdateClubGroups(person);

	} // end UpdatePersonInAllGroups()
	
	
	// ============================================================================================
	// ADD TO GROUPS FUNCTIONS.
	// ============================================================================================

	
	public static void AddToSchoolGroups (Person person) {
		// This function adds the person to school groups for the school they attended, within a small range around the years they attended school.

		if (ValidationTools.empty(person.getSocietalSchoolHistory()) || person.getSocietalSchoolHistory().isEmpty()) {
			// The school history has not been set, or the person did not attend any school in this society, so leave function now.
			return;
		} // end if (no local school history set)

		// DEBUG
		//if (person.getID() == 41) System.err.println("ADDING SCHOOL GROUPS TO PERSON 41... " + person.getAge());
		
		
		
		// ------------------------------------------------------------------------------------------------
		// Elementary schools.
		// ------------------------------------------------------------------------------------------------
		if (person.getAge() >= Configuration.SchoolFinishAge) {
			// When person reaches the end of their elementary school, add them into the proper groups.
			//System.out.println("Person " + person.getID() + " is finished elementary school at age = " + person.getAge() + "; born in " + person.getYearBorn() + ". Current year = " + Configuration.SocietyYear);

			//DebugTools.printArray(person.getHometownHistory());
			//DebugTools.printArray(person.getSchoolHistory());
			//DebugTools.printArray(person.getSocietalSchoolHistory());
			
			//DisplayStudentGroups_TEMP(person);
			
			int yearFinishedElementary = person.getYearBorn() + Configuration.SchoolFinishAge - 1;
			//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  " + yearFinishedElementary);
			ArrayList<Object> schoolInFinalYear = person.getSocietalSchoolHistory().getActivityAndYearsAtYear( yearFinishedElementary );
			if (schoolInFinalYear != null) {
				
				// ADD GROUPS!
				addSchoolInfo(person, schoolInFinalYear);
				//System.out.println("After adding school groups...");
				//DisplayStudentGroups_TEMP(person);
				
			} // end if (check if local school information was found for the person's graduating year)
			
			//DisplayStudentGroups_TEMP(person);
					
		} // end if (check if person is at the end of their elementary school currently)



		// ------------------------------------------------------------------------------------------------
		// Post-secondary schools.
		// ------------------------------------------------------------------------------------------------
		if (Configuration.SocietyYear >= person.getPSFinishYear()) {
			//System.out.println("Person " + person.getID() + " is finished post-secondary school at age = " + person.getAge() + ". Current year = " + Configuration.SocietyYear);

			//DebugTools.printArray(person.getSocietalSchoolHistory());
			
			int yearFinishedElementary = person.getPSFinishYear();
			ArrayList<Object> schoolInFinalYear = person.getSocietalSchoolHistory().getActivityAndYearsAtYear( yearFinishedElementary );
			if (schoolInFinalYear != null) {
				
				// ADD GROUPS!
				addSchoolInfo(person, schoolInFinalYear);
				//System.out.println("After adding school groups...");
				//DisplayStudentGroups_TEMP(person);
				
			} // end if (check if local school information was found for the person's graduating year)

					
		} // end if (check if person is at the end of their post-secondary school currently)
		
		//System.out.println("After adding school groups...");
		//DisplayStudentGroups_TEMP(person);
		//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
	} // end AddToSchoolGroups()
	
	
	/*
	public static void AddToSchoolGroups (Person person) {
		// This function adds the person to school groups for the school they attended, within a small range around the years they attended school.

		if (ValidationTools.empty(person.getSocietalSchoolHistory()) || person.getSocietalSchoolHistory().isEmpty()) {
			// The school history has not been set, or the person did not attend any school in this society, so leave function now.
			return;
		} // end if (no local school history set)

		// DEBUG
		if (person.getID() == 41) System.err.println("ADDING SCHOOL GROUPS TO PERSON 41... " + person.getAge());
		
		
		
		
		
		
		// ------------------------------------------------
		// SCHOOLS
		// ------------------------------------------------
		int socSchoolNum = person.getSocietalSchoolHistory().size();
		int g;
		int y;
		ArrayList schInfo;
		String[] schoolInfo;
		String schoolType;
		String schoolName;
		int[] schoolPeriod;
		Group grp;
		for (g = 0; g < socSchoolNum; g++) {
			schInfo = person.getSocietalSchoolHistory().getDictEntryNameAndYears((Hashtable)person.getSocietalSchoolHistory().get(g));
			
			schoolInfo = (String[])schInfo.get(0);
			schoolType = schoolInfo[0];
			schoolName = schoolInfo[1];
			schoolPeriod = (int[])schInfo.get(1);
			
			
			if (person.getID() == 41) {
				System.out.println("PersonGroupAdder->AddToSchoolGroups(); Person #" + person.getID() + " | age = " + person.getAge() + " || " + person.getYearBorn() + "   current year = " + Configuration.SocietyYear);
				System.out.println("schoolType = " + schoolType + "  schoolName = " + schoolName + "  schoolPeriod = " + schoolPeriod[0] + "," + schoolPeriod[1]);
			}
			//System.out.println("PersonGroupAdder->AddToSchoolGroups(); Person #" + person.getID() + "  schoolType = " + schoolType + "  schoolName = " + schoolName + "  schoolPeriod = " + schoolPeriod[0] + "," + schoolPeriod[1]);

			// Rather than include every year the person was in school, use their graduating/final year and the years within a small range of it.
			for (y = schoolPeriod[1] - Configuration.DeltaNumYearsPersonSocializeWithInSchool; y <= schoolPeriod[1] + Configuration.DeltaNumYearsPersonSocializeWithInSchool; y++) {
				grp = (Group)GroupGenerator.getItemEfficient(new String[] {GroupGenerator.schoolsLabel, schoolType, schoolName}, y);
				if (grp != null) {
					//person.addToGroup(grp.GroupID);
					person.addToGroupBeta(grp.GroupID, "Student");
				} // end if (ensure that group was found properly)
			} // end for y (looping from FinalYear-Delta to FinalYear+Delta)

		} // end for g (looping through in-society schools person attended)

		//DebugTools.printArray(person.getSchoolHistory());
		//DebugTools.printArray(person.getSocietalSchoolHistory());
		
	} // end AddToSchoolGroups()
	*/

	public static void AddToWorkGroups (Person person) {
		// This function adds the person to workplace groups for each and every year they worked there.
		// 
		
		if (ValidationTools.empty(person.getSocietalWorkHistory()) || person.getSocietalWorkHistory().isEmpty()) {
			// The work history has not been set, or the person did not work (yet) in this society, so leave function now.
			return;
		} // end if (no local work history set)

		// ------------------------------------------------
		// WORK
		// ------------------------------------------------
		int socWorkplaceNum = person.getSocietalWorkHistory().size();
		int g;
		int y;
		ArrayList workInfo;
		String[] workPosition;
		String workName;
		int[] workPeriod;
		Group grp;
		String careerTitle;
		for (g = 0; g < socWorkplaceNum; g++) {
			workInfo = person.getSocietalWorkHistory().getDictEntryNameAndYears((Hashtable)person.getSocietalWorkHistory().get(g));
			workPosition = (String[])workInfo.get(0);		// Keep in mind that for the work archive, each key value is a String array of [workplaceID, careerID].
			workName = workPosition[0];
			workPeriod = (int[])workInfo.get(1);
			careerTitle = Careers.getCareerTitleById(Careers.getFullCareersDatabase(), workPosition[1]); // Get person's career title in workplace.
			
			// For the case of workplaces, each year the person worked at this place, they are added to the group for that year of the workplace.
			for (y = workPeriod[0]; y <= workPeriod[1]; y++) {
				grp = (Group)GroupGenerator.getItemEfficient(new String[] {GroupGenerator.workLabel, workName}, y);
				if (grp != null) {
					//person.addToGroup(grp.GroupID);
					//person.addToGroupBeta(grp.GroupID, "Worker_["+careerTitle.replaceAll(" ", "")+"]");
					person.addToGroupBeta(grp.GroupID, "Worker (" + careerTitle + ")");
				} // end if (ensure that group was found properly)
			} // end for y (looping from FinalYear-Delta to FinalYear+Delta)

		} // end for g (looping through in-society schools person attended)

		
		
		
	} // end AddToWorkGroups()
	
	public static void AddToTempleGroups (Person person) {
		// This function adds person to religious body groups and temple groups.

		if (ValidationTools.empty(person.getReligion()) || ValidationTools.empty(person.getSocietalHometownHistory())) {
			// If person has not been assigned a religion index or societal hometown history.
			System.err.println("In PersonGroupAdder->AddToTempleGroups(); attempting to assign temple groups but person has not been assigned a religionIndex or a local hometown history.");
			return;
		} // end if (no religion set [this should never actually be the case!])
		
		
		// If the person has no religion, then exit function.
		if (Configuration.ReligionLabels[person.getReligion()].equals("None")) {
			return;
		} // end if (check if person has no religion)
		
		
		
		
		
		// Set the boundary years that person attended this temple. For now, assume they always went to this temple while living in t
		int yearFirstAttendedTemple = person.getYearBorn();
		int yearLastAttendedTemple = Configuration.SocietyYear;
		
		// Determine all years that person lived in society and were attending the temple.
		int y;

		ArrayList localReligiousYears = new ArrayList();
		// Loop through all years that person was religious (currently their whole life but that could be modified).
		for (y = yearFirstAttendedTemple; y <= yearLastAttendedTemple; y++) {
			// Check if person was living in local society in the year y.
	// DEBUG
	if (person.getHometownHistory().getActivityAtYear(y) == null) {
		System.err.println("NULL POINTER HERE.  " + y + "  " + person.getYearBorn() + " societyYear = " + Configuration.SocietyYear);
		System.err.println(person.getID() + " || " + person.getYearBorn());
		//person.displayPerson();
		DebugTools.printArray(person.getHometownHistory());
		DebugTools.printArray(person.getSchoolHistory());
		DebugTools.printArray(person.getWorkHistory());
		
		//System.out.println("AT THE END OF THE WORLD; YEAR: " + Configuration.SocietyYear);
		//ArtificialSociety.DisplayIndexTable();
		
		//System.out.println("end null");
	}
	// DEBUG
			if (person.getHometownHistory().getActivityAtYear(y).equals(Configuration.SocietyName)) {
				localReligiousYears.add(new Integer(y));
			} // end if (check if living in current society in the year y)
		} // end for y (loop through all years that person attended this temple)

		String religion = Configuration.ReligionLabels[person.getReligion()];

		Tree religTree = (Tree)GroupGenerator.getSpecificTree(new String[] {GroupGenerator.religionsLabel, religion});
		Group[] religGroup = religTree.groupsAtNode;
		
		// -------------------------------------------------------------------------------
		// Add person to group of the religious body in the society.
		// -------------------------------------------------------------------------------
		// Ensure that there is one appropriate group for the religion in the society (i.e. the body of followers of that faith).
		if (religGroup.length == 1 && religGroup[0] != null) {
			//person.addToGroup(religGroup[0].GroupID);
			person.addToGroupBeta(religGroup[0].GroupID, "Spiritual Member");
		} // end if (ensure that group was found properly)

		ArrayList<Tree> religTemples = religTree.getChildren();

		if (religTemples.size() == 0) {
			// If there are no temples in the society that match this person's religion, then exit function now.
			return;
		} // end if (check if there are no temples of religion)
		
		Tree rndTemple = (Tree)Distribution.uniformRandomObjectObj(Tree.treeArrayToObjectArray(religTemples));
		
		
		// Indicate this temple as the person's primary temple they attend.
		person.setTempleAttending(rndTemple.data.toString()); // temple.data is the name/label of the temple.
		
		
		//Tree rndTemple = (Tree)Distribution.uniformRandomObjectObj(religTemples);
		// -------------------------------------------------------------------------------
		// Add person to group for a religious temple.
		// -------------------------------------------------------------------------------
		Group grp;
		Integer year;
		for (y = 0; y < localReligiousYears.size(); y++) {
			year = (Integer)localReligiousYears.get(y);
			grp = (Group)GroupGenerator.getItemEfficient(rndTemple, year.intValue());
			if (grp != null) {
				//person.addToGroup(grp.GroupID);
				person.addToGroupBeta(grp.GroupID, "Spiritual Student");
			} // end if (ensure that group was found properly)
		} // end for y (loop through years that person lived locally and held this faith)

	} // end AddToTempleGroups()
	
	
	public static void AddToClubGroups (Person person) {
		// This function adds the person to club groups.
		// param person: the Person instance for whom to add to clubs based on their trait attributes


		// ------------------------------------------------
		// CLUBS
		// ------------------------------------------------
		int c, t;
		ArrayList club;
		ArrayList clubTraits;
		double[] traitMatches;

		double p_inClub;
		double rnd_inClub;
		Group grp;
		String[] clubTraitInfo;
		String clubTraitReq;
		boolean hasUnmatchedRequiredTrait = false;
		
		for (c = 0; c < Club.getAllClubs().size(); c++) {
			club = (ArrayList)Club.getAllClubs().get(c);
			//System.out.print("\n" + club.get(Club.Club_Name) + "  ");
			
			// Get set of traits that correspond to club.
			clubTraits = (ArrayList)club.get(Club.Club_Traits);
			// Create array to store all calculated trait matches.
			traitMatches = new double[clubTraits.size()];
			
			hasUnmatchedRequiredTrait = false;
			
			for (t = 0; t < clubTraits.size(); t++) {
				clubTraitInfo = (String[])clubTraits.get(t);
				traitMatches[t] = TraitMatcher.calculateTraitMatch(person, clubTraitInfo);
				clubTraitReq = clubTraitInfo[2]; // Remember that clubTraitReq is of the form: [traitType, traitValue, traitReq]
				// Check if trait is required, and if so, check if it matches the required condition (from the XML file). If not, then the club is invalid for the person.
				if (!clubTraitReq.isEmpty() && !ValidationTools.parseAndCheckCondition(traitMatches[t], clubTraitReq)) {
					hasUnmatchedRequiredTrait = true;
					break; // Don't bother looping through following traits for this club - since this required one is false, it is pointless to check other conditions)
				} // end if (check whether or not the trait is required and if so, if it is matched)

			} // end for t (loop through all traits corresponding to this club)

			p_inClub = TraitMatcher.calculateTotalProbability(traitMatches, hasUnmatchedRequiredTrait);
			
			
			// Generate random number to determine if person is in this club or not.
			rnd_inClub = Distribution.uniform(0.0, 1.0);
			
			if (rnd_inClub <= p_inClub && !hasUnmatchedRequiredTrait) {
				// Add person to club group.
				
				grp = (Group)GroupGenerator.getItemEfficient(new String[] {GroupGenerator.clubsLabel, (String)club.get(Club.Club_Name)});
				if (grp != null) {
					//person.addToGroup(grp.GroupID);
					person.addToGroupBeta(grp.GroupID, "Member");
				} // end if (ensure that group was found properly)
				
			} // end if (determine if person is assigned into club or not)
			
			//System.out.println("............................................");
		} // end for c (loop through all clubs)
		

	} // end AddToClubGroups()

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// ============================================================================================
	// UPDATE GROUPS FUNCTIONS.
	// ============================================================================================

	
	public static void UpdateSchoolGroups (Person person) {
		// This function updates the person's school groups for the school they attended, within a small range around the years they attended school.

		if (ValidationTools.empty(person.getSocietalSchoolHistory()) || person.getSocietalSchoolHistory().isEmpty()) {
			// The school history has not been set, or the person did not attend any school in this society, so leave function now.
			//System.err.println("PersonGroupAdder->UpdateSchoolGroups(); the person's societal school history has not been initialized.");
			return;
		} // end if (no local school history set)

		//if (Configuration.SocietyYear > person.getPSFinishYear()+Configuration.DeltaNumYearsPersonSocializeWithInSchool) {
			// If person is beyond the age of finishing post-secondary education, then there is nothing to update.
			//return;
		//} // end if (check if person is beyond school years)
		
		//System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

		/*
		DebugTools.printArray(person.getSchoolHistory());
		DebugTools.printArray(person.getSocietalSchoolHistory());
		System.out.println(person.getID());
		*/
		
		// ------------------------------------------------------------------------------------------------
		// Elementary schools.
		// ------------------------------------------------------------------------------------------------
		if (person.getAge() == Configuration.SchoolFinishAge) {
			// When person reaches the end of their elementary school, add them into the proper groups.
			//System.out.println("Person " + person.getID() + " is now finished elementary school at age = " + person.getAge() + "; born in " + person.getYearBorn() + ". Current year = " + Configuration.SocietyYear);

			//DebugTools.printArray(person.getHometownHistory());
			//DebugTools.printArray(person.getSchoolHistory());
			
			//DisplayStudentGroups_TEMP(person);
			


			ArrayList<Object> schoolInFinalYear = person.getSocietalSchoolHistory().getActivityAndYearsAtYear( Configuration.SocietyYear );
			if (schoolInFinalYear != null) {
				
				// ADD GROUPS!
				addSchoolInfo(person, schoolInFinalYear);
				//System.out.println("After adding school groups...");
				//DisplayStudentGroups_TEMP(person);
				
			} // end if (check if local school information was found for the person's graduating year)
			
			
			
			
		} // end if (check if person is at the end of their elementary school currently)
		
		
		
		// ------------------------------------------------------------------------------------------------
		// Post-secondary schools.
		// ------------------------------------------------------------------------------------------------
		if (Configuration.SocietyYear == person.getPSFinishYear() - 1) {

			ArrayList<Object> schoolInFinalYear = person.getSocietalSchoolHistory().getActivityAndYearsAtYear( Configuration.SocietyYear );
			if (schoolInFinalYear != null) {
				
				// ADD GROUPS!
				addSchoolInfo(person, schoolInFinalYear);

				
			} // end if (check if local school information was found for the person's graduating year)

		} // end if (check if person is at the end of their post-secondary school currently)
		
		
		
		
		
		
		/*
		// ------------------------------------------------
		// SCHOOLS
		// ------------------------------------------------
		int socSchoolNum = person.getSocietalSchoolHistory().size();
		int g;
		int y;
		ArrayList schInfo;
		String[] schoolInfo;
		String schoolType;
		String schoolName;
		int[] schoolPeriod;
		Group grp;
		for (g = 0; g < socSchoolNum; g++) {
			schInfo = person.getSocietalSchoolHistory().getDictEntryNameAndYears((Hashtable)person.getSocietalSchoolHistory().get(g));
			
			schoolInfo = (String[])schInfo.get(0);
			schoolType = schoolInfo[0];
			schoolName = schoolInfo[1];
			schoolPeriod = (int[])schInfo.get(1);

			System.err.println("><><><><><><><><><><><");
			DebugTools.printArray(schoolPeriod);
			System.err.println("><><><><><><><><><><><");
			
			//Rather than include every year the person was in school, use their graduating/final year and the years within a small range of it.
			for (y = schoolPeriod[1] - Configuration.DeltaNumYearsPersonSocializeWithInSchool; y <= schoolPeriod[1] + Configuration.DeltaNumYearsPersonSocializeWithInSchool; y++) {
				grp = (Group)GroupGenerator.getItemEfficient(new String[] {GroupGenerator.schoolsLabel, schoolType, schoolName}, y);
				if (grp != null) {
					//person.addToGroup(grp.GroupID);
					person.addToGroupBeta(grp.GroupID, "Student");
				} // end if (ensure that group was found properly)
			} // end for y (looping from FinalYear-Delta to FinalYear+Delta)

		} // end for g (looping through in-society schools person attended)
		 */
	} // end UpdateSchoolGroups()
	

	public static void UpdateWorkGroups (Person person) {
		// This function adds the person to workplace groups for each and every year they worked there.
		// 
		
		if (ValidationTools.empty(person.getSocietalWorkHistory()) || person.getSocietalWorkHistory().isEmpty()) {
			// The work history has not been set, or the person did not work (yet) in this society, so leave function now.
			return;
		} // end if (no local work history set)

		if (!person.getCurrentHometown().equals(Configuration.SocietyName)) {
			// If person does not live in the current society, don't bother proceeding.
			return;
		} // end if (check if person lived outside of the local society now)

		String[] workPosition = (String[])person.getSocietalWorkHistory().getLastActivityName(); // Keep in mind that for the work archive, each key value is a String array of [workplaceID, careerID].
		String workName = workPosition[0];
		String careerTitle = Careers.getCareerTitleById(Careers.getFullCareersDatabase(), workPosition[1]); // Get person's career title in workplace.
		
		Group grp = (Group)GroupGenerator.getItemEfficient(new String[] {GroupGenerator.workLabel, workName}, Configuration.SocietyYear);
		if (grp != null) {
			//person.addToGroup(grp.GroupID);
			//person.addToGroupBeta(grp.GroupID, "Worker_["+careerTitle.replaceAll(" ", "")+"]");
			person.addToGroupBeta(grp.GroupID, "Worker ("+careerTitle+")");
		} // end if (ensure that group was found properly)

	} // end UpdateWorkGroups()
	
	public static void UpdateTempleGroups (Person person) {
		// This function updates the person's religious body groups and temple groups.

		if (ValidationTools.empty(person.getReligion()) || ValidationTools.empty(person.getSocietalHometownHistory())) {
			// If person has not been assigned a religion index or societal hometown history.
			System.err.println("In PersonGroupAdder->AddToTempleGroups(); attempting to assign temple groups but person has not been assigned a religionIndex or a local hometown history.");
			return;
		} // end if (no religion set [this should never actually be the case!])
		
		
		// If the person has no religion, then exit function.
		if (Configuration.ReligionLabels[person.getReligion()].equals("None")) {
			return;
		} // end if (check if person has no religion)
		
		//System.err.println("Updating person " + person.getID() + "'s temple/religious groups.");
		
		
		String religion = Configuration.ReligionLabels[person.getReligion()];

		// -------------------------------------------------------------------------------
		// Add person to group of the religious body in the society.
		// -------------------------------------------------------------------------------

		Tree religTree = (Tree)GroupGenerator.getSpecificTree(new String[] {GroupGenerator.religionsLabel, religion});
		Group[] religGroup = religTree.groupsAtNode;

		// Ensure that there is one appropriate group for the religion in the society (i.e. the body of followers of that faith).
		if (religGroup.length == 1 && religGroup[0] != null) {
			//person.addToGroup(religGroup[0].GroupID);
			person.addToGroupBeta(religGroup[0].GroupID, "Spiritual Member");
		} // end if (ensure that group was found properly)

		
		
		// If person does not live in local society, then don't update their temple groups.
		if (!person.getCurrentHometown().equals(Configuration.SocietyName)) {
			return;
		} // end if (check if person has moved away)

		
		// -------------------------------------------------------------------------------
		// Add person to group for a religious temple.
		// -------------------------------------------------------------------------------

		String temple = person.getTempleAttending();
		Tree templeTree;
		if (temple == null || ValidationTools.empty(temple)) {
			ArrayList<Tree> religTemples = religTree.getChildren();
			templeTree = (Tree)Distribution.uniformRandomObjectObj(Tree.treeArrayToObjectArray(religTemples));
		} else {
			templeTree = (Tree)GroupGenerator.getSpecificTree(new String[] {GroupGenerator.religionsLabel, religion, temple});
		} // end if (check if person already has a temple (they should ALWAYS already have a temple at this point!)
		
		
		
		Group grp = (Group)GroupGenerator.getItemEfficient(templeTree, Configuration.SocietyYear);
		if (grp != null) {
			//person.addToGroup(grp.GroupID);
			person.addToGroupBeta(grp.GroupID, "Spiritual Student");
		} // end if (ensure that group was found properly)

		
	} // end UpdateTempleGroups()
	
	
	public static void UpdateClubGroups (Person person) {
		// This function adds the person to club groups.
		// param person: the Person instance for whom to add to clubs based on their trait attributes


		// ------------------------------------------------
		// CLUBS
		// ------------------------------------------------
		int c, t;
		ArrayList club;
		ArrayList clubTraits;
		double[] traitMatches;

		double p_inClub;
		double rnd_inClub;
		Group grp;
		String[] clubTraitInfo;
		String clubTraitReq;
		boolean hasUnmatchedRequiredTrait = false;
		
		for (c = 0; c < Club.getAllClubs().size(); c++) {
			club = (ArrayList)Club.getAllClubs().get(c);
			//System.out.print("\n" + club.get(Club.Club_Name) + "  ");
			
			// Get set of traits that correspond to club.
			clubTraits = (ArrayList)club.get(Club.Club_Traits);
			// Create array to store all calculated trait matches.
			traitMatches = new double[clubTraits.size()];
			
			hasUnmatchedRequiredTrait = false;
			
			for (t = 0; t < clubTraits.size(); t++) {
				clubTraitInfo = (String[])clubTraits.get(t);
				traitMatches[t] = TraitMatcher.calculateTraitMatch(person, clubTraitInfo);
				clubTraitReq = clubTraitInfo[2]; // Remember that clubTraitReq is of the form: [traitType, traitValue, traitReq]
				// Check if trait is required, and if so, check if it matches the required condition (from the XML file). If not, then the club is invalid for the person.
				if (!clubTraitReq.isEmpty() && !ValidationTools.parseAndCheckCondition(traitMatches[t], clubTraitReq)) {
					hasUnmatchedRequiredTrait = true;
					break; // Don't bother looping through following traits for this club - since this required one is false, it is pointless to check other conditions)
				} // end if (check whether or not the trait is required and if so, if it is matched)

			} // end for t (loop through all traits corresponding to this club)

			p_inClub = TraitMatcher.calculateTotalProbability(traitMatches, hasUnmatchedRequiredTrait);
			
			
			// Generate random number to determine if person is in this club or not.
			rnd_inClub = Distribution.uniform(0.0, 1.0);
			
			if (rnd_inClub <= p_inClub && !hasUnmatchedRequiredTrait) {
				// Add person to club group.
				
				grp = (Group)GroupGenerator.getItemEfficient(new String[] {GroupGenerator.clubsLabel, (String)club.get(Club.Club_Name)});
				if (grp != null) {
					//person.addToGroup(grp.GroupID);
					person.addToGroupBeta(grp.GroupID, "Member");
				} // end if (ensure that group was found properly)
				
			} // end if (determine if person is assigned into club or not)
			
			//System.out.println("............................................");
		} // end for c (loop through all clubs)
		

	} // end UpdateClubGroups()
	
	
	
	
	
	private static void addSchoolInfo (Person person, ArrayList<Object> schInfo) {
		
		String[] schoolInfo = (String[])schInfo.get(0);
		String schoolType = schoolInfo[0];
		String schoolName = schoolInfo[1];
		int[] schoolPeriod = (int[])schInfo.get(1);
		int y;

		Group grp;
		for (y = schoolPeriod[1] - Configuration.DeltaNumYearsPersonSocializeWithInSchool; y <= schoolPeriod[1] + Configuration.DeltaNumYearsPersonSocializeWithInSchool; y++) {
			grp = (Group)GroupGenerator.getItemEfficient(new String[] {GroupGenerator.schoolsLabel, schoolType, schoolName}, y);
			if (grp != null) {
				//person.addToGroup(grp.GroupID);
				person.addToGroupBeta(grp.GroupID, "Student");
			} // end if (ensure that group was found properly)
		} // end for y (looping from FinalYear-Delta to FinalYear+Delta)
		
		
	} // end addSchoolInfo()
	
	
	
	public static void DisplayStudentGroups_TEMP (Person person) {
		ArrayList<String> grp;
		Group group;
		int numSchoolGroups = 0;
		
		for (int g = 0; g < person.getGroupIDs().size(); g++) {
			grp = person.getGroupIDs().get(g);
			if (grp.get(1).equals("Student")) {
				//DebugTools.printArray(grp.toArray());
				group = Group.getGroupByID(Integer.parseInt(grp.get(0)));
				System.err.println(grp.get(0) + " --> " + group.getGroupLabel() + " \t\t\t[" + person.getID() + "]");
				numSchoolGroups++;
			} // end if

		} // end for g
		
		if (numSchoolGroups == 0) {
			//System.err.println("No school groups for this person.");
		} // end if
		
	} // end DisplayStudentGroups_TEMP()
	
	
	public static void DisplayAllGroups_TEMP (Person person) {
		ArrayList<String> grp;
		Group group;

		for (int g = 0; g < person.getGroupIDs().size(); g++) {
			grp = person.getGroupIDs().get(g);
			group = Group.getGroupByID(Integer.parseInt(grp.get(0)));
			System.err.println(grp.get(0) + " --> " + group.getGroupLabel() + " \t\t\t[" + person.getID() + "]");

		} // end for g

	} // end DisplayStudentGroups_TEMP()
	
	

} // end PersonGroupAdder class
