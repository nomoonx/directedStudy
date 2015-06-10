package thesis_network_growth;

import java.util.ArrayList;

public class Person {
	
	public static String CURR_POSITION_CHILD = "Child";
	public static String CURR_POSITION_STUDENT = "Student";
	public static String CURR_POSITION_WORKING = "Working";
	public static String CURR_POSITION_UNEMPLOYED = "Unemployed";
	public static String CURR_POSITION_RETIRED = "Retired";
	public static String CURR_POSITION_DEAD = "Dead";
	
	
	
	// Static array to record all Persons; not needed in PersonAttributes class.
	static int NextAvailablePersonID = 0;
	//static ArrayList AllPersons = new ArrayList();
	
	
	
	// Unique to Person; not needed in PersonAttributes class.
	private int id = Integer.MIN_VALUE;
	
	// Basic.
	private Boolean isAlive = true;				// Important flag indicating whether or not person is living. Default to true.
	private int sex = Integer.MIN_VALUE;
	private int initAge = Integer.MIN_VALUE;	// The person's initial age, before the simulation begins time-stepping.
	private int age = Integer.MIN_VALUE;
	private int yearBorn = Integer.MIN_VALUE;
	
	// Life Expectancy.
	private int expYearDeath = Integer.MIN_VALUE;

	// Culture.
	private int raceIndex = Integer.MIN_VALUE;
	private int religionIndex = Integer.MIN_VALUE;
	private String nationality = null;
	private String templeAttending = null;
	
	// Interests.
	private double[] interests = null;
	private double[] interestWeights = null;
	
	// Relationship.
	private int relationshipStatus = Integer.MIN_VALUE;			// Right now: {0 = Single; 1 = Married} ... Eventually will change to: {0 = Single; 1 = Dating; 2 = Married; (3 = Divorced ?)}
	private int partner_id = Integer.MIN_VALUE;
	private double interestSimilarity = Double.MIN_VALUE;
	private double relationshipStrength = Double.MIN_VALUE;
	private int relationshipStartYear = Integer.MIN_VALUE;


	
	// Personality.
	private double[] personality = null;				// {I/E, S/N, T/F, J/P}
	
	// Extra Traits/Attributes (useful in club assignment)
	private double intelligence = Double.MIN_VALUE;
	private double athleticism = Double.MIN_VALUE;
	
	
	
	// Career and Education.
	private String careerPathID = null;
	private String currentPosition = null;				// This will be either the current career position or "Student" if in school.
	private int income = Integer.MIN_VALUE;
	private String education = null;					// {N = None; HS = High School; C = College; U = University}
	private int educationPSYears = Integer.MIN_VALUE;
	private Boolean isInSchool = null;
	private int yearStartedPSSchool = Integer.MIN_VALUE;
	private int yearFinishedPSSchool = Integer.MIN_VALUE;
	
	// History.
	private ActivityArchive hometownCheckpoints = null;		// This is a temporary hometown archive until the actual hometownHistory is created, from some of the checkpoint locations in this object. This will then be set to null later to restore the memory.
	private ActivityArchive hometownHistory = null;
	private ActivityArchive schoolHistory = null;
	private ActivityArchive workHistory = null;

	// History In Society.							// These are important because the original archive arrays contain ALL history for the person,
	private ActivityArchive soc_HometownHistory = null;		// but these contain just the activities in the society, so Group assignments are made from these.
	private ActivityArchive soc_SchoolHistory = null;
	private ActivityArchive soc_WorkHistory = null;

	// Family.
	private int family_id = Integer.MIN_VALUE;
	private ArrayList<Integer> parent_ids = null;
	private ArrayList<Integer> children_ids = null;
	private ArrayList<Integer> siblings_ids = null;
	
	// Clubs.
	private ArrayList<Integer> club_ids = null;
	
	// Groups.
	private ArrayList<ArrayList<String>> group_ids = null;
	
	// Friendship determination.
	private ArrayList<Integer> friend_prob_ids = null;
	private ArrayList<Double> friend_probabilities = null;
	private ArrayList<String> friend_probRoles = null;
	
	// Friendship.
	private ArrayList<Friendship> friends = null;
	
	
	
	
	public Person () {
		
		//System.out.println("In Person()");
		
		// Assign this person the NextAvailablePersonID, and then immediately increment the variable to keep it ready at all times.
		//id = NextAvailablePersonID;
		//NextAvailablePersonID++;
		
		hometownCheckpoints = new ActivityArchive();
		
	} // end Person()
	
	public void giveAutomaticID () {
		setID( NextAvailablePersonID );
		NextAvailablePersonID++;
	} // end giveAutomaticID()
	
	
	
	
	/*
	public Person (PersonAttributes attributes) {

		System.out.println("In Person(attributes)");
		
		// Assign this person the NextAvailablePersonID, and then immediately increment the variable to keep it ready at all times.
		id = NextAvailablePersonID;
		NextAvailablePersonID++;

	} // end Person()
	*/
	
	public void die () {
		// This is called when the person dies in the simulation. This is important as many things must be cleaned up after
		// a person's death to ensure the remaining society has no connections to the dead people.
		
		//System.out.println("====================================");
		//System.out.println("At death, person " + this.getID() + " has the following hometown history... (current year = " + Configuration.SocietyYear + ")");
		//DebugTools.printArray(getHometownHistory());
		
		
		
		setIsAlive(false);
		
		
		ArtificialSociety.removePersonByID(this.getID());
		
		
	} // end die()
	
	

	public void removeFriend (int personID) {
		// Remove this person's friend, with the given ID, from their list of friends.
		// Note that in this simulation, the only way to remove a friend is when somebody dies.
		//
		// param personID: the ID of the person who is to be removed from this person's friend list

		int i = 0;
		Friendship friendship;

		// Loop through all friends as there could be multiple friendships between the same two people, with different roles.
		while (i <getFriends().size()) {
			
			friendship = getFriends().get(i);
			if (friendship.getFriendID() == personID) {
				getFriends().remove(i); // Remove friendship.
				i--; // Decrement counter so the subsequent friendship does not get omitted due to the removed element shifting indices)
			} // end if (check if this friendship is with the specified person)
			
			i++; // Increment to move on to the next friendship.
		} // end while (loop through friend list)
		
	} // end removeFriend()
	
	
	// ==========================================================================================================================
	// GETTERS.
	// ==========================================================================================================================

	public String getCurrentHometown () {
		return (String)getHometownHistory().getLastActivityName();
	} // end getCurrentHometown()
	
	public int getID () {
		// Get person's ID.
		return id;
	} // end getID()
	
	public Boolean getIsAlive () {
		// Get whether or not person is alive.
		return isAlive;
	} // end getIsAlive()

	public int getSex () {
		// Get person's sex.
		return sex;
	} // end getSex()

	public int getInitAge () {
		// Get person's initial age (before timesteps in simulation).
		return initAge;
	} // end getInitAge()

	public int getAge () {
		// Get person's age.
		return age;
	} // end getAge()

	public int getYearBorn () {
		// Get person's birth year.
		return yearBorn;
	} // end getYearBorn()

	public int getExpectedDeathYear () {
		// Get person's expected year of death.
		return expYearDeath;
	} // end getExpectedDeathYear()
	
	public int getRace () {
		// Get person's race.
		return raceIndex;
	} // end getRace()

	public int getReligion () {
		// Get person's religion.
		return religionIndex;
	} // end getReligion()
	
	public String getNationality () {
		// Get person's nationality.
		return nationality;
	} // end getNationality()
	
	public String getTempleAttending () {
		// Get the name of the temple this person attends.
		return templeAttending;
	} // end getTempleAttending()
	
	public double[] getInterests () {
	// Get person's interests.
		return interests;
	} // end getInterests()
	
	public double[] getInterestWeights () {
	// Get person's interest weights.
		return interestWeights;
	} // end getInterestWeights()
	
	public double[] getPersonality () {
		// Get person's MBTI personality values.
		return personality;
	} // end getPersonality()
	
	public double getIntelligence () {
	// Get person's personality intelligence value.
		return intelligence;
	} // end getIntelligence()
	
	public double getAthleticism () {
	// Get person's personality athleticism value.
		return athleticism;
	} // end getAthleticism()
	
	public String getCareer () {
		// Get person's career ID.
		return careerPathID;
	} // end getCareer()
	
	public String getCareerTitle () {
		// Get person's career title (position string).
		return Careers.getCareerTitleById(Careers.getFullCareersDatabase(), this.careerPathID).replaceAll(" ",  "_");
	} // end getCareerTitle()
	
	public String getCurrentPosition () {
		return currentPosition;
	} // end getCurrentPosition()
	
	public int getIncome () {
		// Get person's income;
		return income;
	} // end getIncome()
	
	public String getEducation () {
		// Get person's education status.
		return education;
	} // end getEducation()
	
	public int getPSEducationYears () {
		// Get persons's required number of post-secondary school years (may or may not be completed at current time in society)
		return educationPSYears;
	} // end getPSEducationYears()

	public Boolean getIsInSchool () {
		// Get flag indicating whether or not person is currently in school.
		return isInSchool;
	} // end getIsInSchool()
	
	public int getPSStartYear () {
		// Get person's starting year of post-secondary education.
		return yearStartedPSSchool;
	} // end getPSStartYear()
	
	public int getPSFinishYear () {
		// Get person's finishing year of post-secondary education.
		return yearFinishedPSSchool;
	} // end getPSFinishYear()
	
	
	public int getRelationshipStatus () {
		return relationshipStatus;
	} // end getRelationshipType()
	
	public int getPartnerID () {
		return partner_id;
	} // end getPartnerID()
	
	public double getInterestSimilarity () {
		return interestSimilarity;
	} // end getInterestSimilarity()
	
	public double getRelationshipStrength () {
		return relationshipStrength;
	} // end getRelationshipStrength()
	
	public int getRelationshipStartYear () {
		return relationshipStartYear;
	} // end getRelationshipStartYear()
	
	public ActivityArchive getHometownHistory () {
		return hometownHistory;	
	} // end getHometownHistory()
	
	public ActivityArchive getHometownCheckpoints () {
		return hometownCheckpoints;	
	} // end getHometownCheckpoints()
	
	
	public ActivityArchive getSchoolHistory () {
		return schoolHistory;	
	} // end getSchoolHistory()
	
	public ActivityArchive getWorkHistory () {
		return workHistory;	
	} // end getWorkHistory()
	
	public ActivityArchive getSocietalHometownHistory () {
		return soc_HometownHistory;	
	} // end getSocietalHometownHistory()
	
	public ActivityArchive getSocietalSchoolHistory () {
		return soc_SchoolHistory;	
	} // end getSocietalSchoolHistory()
	
	public ActivityArchive getSocietalWorkHistory () {
		//System.err.println("Person " + this.getID() + " needs societal work history. Kewl.");
		return soc_WorkHistory;	
	} // end getSocietalWorkHistory()
	
	public int getFamilyID () {
		// Get person's family ID.
		return family_id;
	} // end getFamilyID()
	
	public ArrayList<Integer> getParentIDs () {
		return parent_ids;
	} // end getParentIDs()
	
	public ArrayList<Integer> getChildrenIDs () {
		return children_ids;
	} // end getChildrenIDs()
	
	public ArrayList<Integer> getSiblingIDs () {
		return siblings_ids;
	} // end getSiblingIDs()
	
	public ArrayList<Integer> getClubIDs () {
		return club_ids;
	} // end getClubIDs()
	
	public ArrayList<ArrayList<String>> getGroupIDs () {
		return group_ids;
	} // end getGroupIDs()

	public ArrayList<Integer> getFriendProbIDs () {
		return friend_prob_ids;
	} // end getFriendProbIDs()
	
	public ArrayList<Double> getFriendProbabilities () {
		return friend_probabilities;
	} // end getFriendProbabilities()
	
	public ArrayList<String> getFriendProbRoles() {
		return friend_probRoles;
	} // end getFriendProbRoles()
	
	public ArrayList<Friendship> getFriends () {
		return friends;
	} // end getFriends()
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public ArrayList<String> getGroups () {
		// Get list of groups in which person is involved.
		
		int g;
		ArrayList<String> groupLabelsList = new ArrayList<String>();
		Group grp;
		Integer grpID;
		ArrayList<String> groupRoleInfo;
		for (g = 0; g < group_ids.size(); g++) {
			groupRoleInfo = (ArrayList<String>)group_ids.get(g);
			grpID = Integer.parseInt((String)groupRoleInfo.get(0));
			grp = Group.getGroupByID(grpID);
			groupLabelsList.add(grp.GroupLabel + " (" + grp.GroupYear + ")");
		} // end for g (loop through all groups person is involved in)

		return groupLabelsList;
	} // end getGroups()

	public String getGroupsString () {
		// Get list of groups in which person is involved.
		
		int g;
		String groupListString = "[";
		Group grp;
		String lbl;
		ArrayList<String> groupRoleInfo;
		Integer grpID;
		String grpRole;
		
		for (g = 0; g < group_ids.size(); g++) {
			groupRoleInfo = (ArrayList<String>)group_ids.get(g);
			grpID = Integer.parseInt((String)groupRoleInfo.get(0));
			grpRole = (String)groupRoleInfo.get(1);
			grpRole = grpRole.replaceAll(" ",  "_");
			
			//grpID = (Integer)group_ids.get(g);
			grp = Group.getGroupByID(grpID.intValue());
			lbl = grp.GroupLabel.replaceAll(" ", "_");
			groupListString += lbl + "(" + grp.GroupYear + ")|" + grpRole + ";";
		} // end for g (loop through all groups person is involved in)

		groupListString += "]";
		
		return groupListString;
	} // end getGroups()

	
	
	// ==========================================================================================================================
	// SETTERS.
	// ==========================================================================================================================

	public void setID (int v) {
		id = v;

		// Add to global index table.
		ArtificialSociety.addPersonID(id);
		
	} // end setID()
	
	public void setIsAlive (Boolean v) {
		isAlive = v;
	} // end setIsAlive()
	
	public void setSex (int v) {
		sex = v;
	} // end setSex()
	
	public void setInitAge (int v) {
		initAge = v;
	} // end setInitAge()
	
	public void setAge (int v) {
		age = v;
	} // end setAge()
	
	public void setYearBorn (int v) {
		yearBorn = v;
	} // end setYearBorn()
	
	public void setExpectedDeathYear (int v) {
		expYearDeath = v;
	} // end setExpectedDeathYear()
	
	public void setRace (int v) {
		raceIndex = v;
	} // end setRace()
	
	public void setReligion (int v) {
		religionIndex = v;
	} // end setReligion()
	
	public void setNationality (String v) {
		nationality = v;
	} // end setNationality()
	
	public void setTempleAttending (String v) {
		templeAttending = v;
	} // end setTempleAttending()
	
	public void setInterests (double[] v) {
		interests = v;
	} // end setInterests()
	
	public void setInterestWeights (double[] v) {
		interestWeights = v;
	} // end setInterestWeights()

	public void setRelationshipStatus (int v) {
		relationshipStatus = v;
	} // end setRelationshipStatus()
	
	public void setPartnerID (int v) {
		partner_id = v;
	} // end setPartnerID()
	
	public void setInterestSimilarity (double v) {
		interestSimilarity = v;
	} // end setInterestSimilarity()
	
	public void setRelationshipStrength (double v) {
		relationshipStrength = v;
	} // end setRelationshipStrength()
	
	public void setRelationshipStartYear (int v) {
		relationshipStartYear = v;
	} // end setRelationshipStartYear()
	
	public void setPersonality (double[] v) {
		personality = v;
	} // end setPersonality()
	
	public void setIntelligence (double v) {
		intelligence = v;
	} // end setIntelligence()
	
	public void setAthleticism (double v) {
		athleticism = v;
	} // end setAthleticism()
	

	
	
	
	public void setCareer (String v) {
		careerPathID = v;
	} // end setCareer()
	
	public void setCurrentPosition (String v) {
		currentPosition = v;
	} // end setCurrentPosition()
	
	public void setIncome (int v) {
		income = v;
	} // end setIncome()
	
	public void setEducation (String v) {
		education = v;
	} // end setEducation()

	public void setPSEducationYears (int v) {
		educationPSYears = v;
	} // end setPSEducationYears()

	public void setIsInSchool (boolean v) {
		isInSchool = v;
	} // end setIsInSchool()

	public void setPSStartYear (int v) {
		yearStartedPSSchool = v;
	} // end setPSStartYear()

	public void setPSFinishYear (int v) {
		yearFinishedPSSchool = v;
	} // end setPSFinishYear()

	public void setHometownCheckpointsHistory (ActivityArchive v) {
		hometownCheckpoints = v;
	} // end setHometownCheckpointsArchive()
	
	public void setHometownHistory (ActivityArchive v) {
		hometownHistory = v;
	} // end setHometownArchive()
	
	public void setSchoolHistory (ActivityArchive v) {
		schoolHistory = v;
	} // end setSchoolArchive()
	
	public void setWorkHistory (ActivityArchive v) {
		workHistory = v;
	} // end setWorkArchive()
	
	public void setSocietalHometownHistory (ActivityArchive v) {
		soc_HometownHistory = v;
	} // end setSocietalHometownArchive()
	
	public void setSocietalSchoolHistory (ActivityArchive v) {
		soc_SchoolHistory = v;
	} // end setSocietalSchoolArchive()
	
	public void setSocietalWorkHistory (ActivityArchive v) {
		soc_WorkHistory = v;
	} // end setSocietalWorkArchive()
	
	public void setFamilyID (int v) {
		family_id = v;
	} // end setFamilyID()
	
	public void setParentIDs (ArrayList<Integer> v) {
		parent_ids = v;
	} // end setParentIDs()
	
	public void setChildrenIDs (ArrayList<Integer> v) {
		children_ids = v;
	} // end setChildrenIDs()
	
	public void setSiblingIDs (ArrayList<Integer> v) {
		siblings_ids = v;
	} // end setSiblingIDs()
	
	public void setClubIDs (ArrayList<Integer> v) {
		club_ids = v;
	} // end setClubIDs()
	
	public void setGroupIDs (ArrayList<ArrayList<String>> v) {
		group_ids = v;
	} // end setGroupIDs()

	public void setFriendProbIDs (ArrayList<Integer> v) {
		friend_prob_ids = v;
	} // end setFriendProbIDs()
	
	public void setFriendProbabilities (ArrayList<Double> v) {
		friend_probabilities = v;
	} // end setFriendProbabilities()
	
	public void setFriendProbRoles (ArrayList<String> v) {
		friend_probRoles = v;
	} // end setFriendProbRoles()

	public void setFriends (ArrayList<Friendship> v) {
		friends = v;
	} // end setFriends()
	
	
	// ==========================================================================================================================
	// FRIENDSHIP FUNCTIONS.
	// ==========================================================================================================================
	
	
	/*
	public void addToGroup (int grpID) {
		// Add this person to the group with the given ID, grpID.
		// * NOTE * This method both adds the group ID to person's list of group IDs, AND it adds the person's ID to the group membership list.
		// This is the ONLY place from which the group membership list should be added to.
		this.group_ids.add(new Integer(grpID));
		
		Group grp = Group.getGroupByID(grpID);
		grp.addMember(this);
	} // end addToGroup()
	*/

	
	
	public boolean isInGroupRole (int grpID, String role) {
		// Check whether or not this person is already in the given group with the specific role. This helps avoid duplicate entries of
		// the same role in a group. This is called from addToGroupBeta().
		//
		// param grpID: the integer index of the Group to check for
		// param role: the role within the given group that we are checking for
		//
		// returns: a boolean flag indicating whether or not the person is currently in the group with the given role
		
		//System.out.println("~~~ " + getGroupIDs() + " ~~~");

		int g;
		int numGroups = getGroupIDs().size();
		ArrayList<String> grp;
		int g_id;

		for (g = 0; g < numGroups; g++) {
			grp = getGroupIDs().get(g);
			g_id = Integer.parseInt(grp.get(0));
			
			if (g_id == grpID && grp.get(1).equals(role)) {
				return true;
			} // end if (check if person is in group with same role)
			
		} // end for g (loop through all current groups)

		return false;
		
	} // end isInGroupRole()

	public void addToGroupBeta (int grpID, String role) {
		// Add this person to the group with the given ID, grpID.
		// * NOTE * This method both adds the group ID to person's list of group IDs, AND it adds the person's ID to the group membership list.
		// This is the ONLY place from which the group membership list should be added to.
		
		// NEW
		if (!isInGroupRole(grpID, role)) {

			ArrayList<String> roleInGroup = new ArrayList<String>();
			roleInGroup.add(String.valueOf(grpID));
			roleInGroup.add(role);
			this.group_ids.add(roleInGroup);

		} else {
			//System.out.println("Person " + this.getID() + " is in group, " + grpID + ", already, with role = " + role);
		}
		// END NEW
		
		
		
		
		


		Group grp = Group.getGroupByID(grpID);
		grp.addMember(this);
	} // end addToGroup()
	
	public String getRoleInGroup (int grpID) {
		// Loop through person's group IDs and return their role associated with that group. This assumed each person is involved in a group with at most one role.
		int g;
		ArrayList<String> group;
		int group_id;
		String group_role;
		for (g = 0; g < group_ids.size(); g++) {
			group = (ArrayList<String>)group_ids.get(g);
			group_id = Integer.parseInt((String)group.get(0));

			if (group_id == grpID) {
				group_role = (String)group.get(1);
				return group_role;
			} // end if (found matching group by ID)
		} // end for g (loop through groups this person is in)
		return null;

	} // end getRoleInGroup()
	
	public boolean hasConnectionWith (Person acq) {
		// Check whether or not this person has a potential friendship (shared membership in at least one group) with the given Person, acq.
		
		int acq_id = acq.id;
		
		int f;
		Integer tmp;
		for (f = 0; f < friend_prob_ids.size(); f++) {
			tmp = (Integer)friend_prob_ids.get(f);
			if (tmp.intValue() == acq_id) {
				// If the potential acquaintance is found here, then return true, since the two Persons have a connection already.
				return true;
			}
		} // end for f (friendship ids)
		
		// If the potential acquaintance was not found anywhere in the friendship array, then return false, since there is no current connection.
		return false;
	} // end hasConnectionWith()
	
	public int getFriendshipIndex (Person acq) {
		// Get the index in this person's friend_prob_ids at which their acquaintance (or potential friend), acq, exists.
		int acq_id = acq.id;

		int f;
		Integer tmp;
		for (f = 0; f < friend_prob_ids.size(); f++) {
			tmp = (Integer)friend_prob_ids.get(f);
			if (tmp.intValue() == acq_id) {
				// If the potential acquaintance is found here, then return the index f, since the two Persons have a connection already.
				return f;
			}
		} // end for f (friendship ids)

		// If the potential acquaintance was not found anywhere in the friendship array, then return -1, since there is no current connection (but this should never occur!)
		return -1;
	} // end getFriendshipIndex()

	public void accumulateFriendshipProbability (int f_id, double addVal) {
		Double currFriendshipProb = (Double)this.friend_probabilities.get(f_id);
		this.friend_probabilities.set(f_id, new Double(currFriendshipProb.doubleValue() + addVal));
	} // end accumulateFriendshipProbability()
	
	
	
	
	public boolean isFriendsWith (Person friend, int rel_type) {
		// Check whether or not this person is friends with the given Person, friend, of the given friendship type, rel_type.
		
		int fr_id = friend.id;
		
		int f;
		Friendship fship;
		for (f = 0; f < friends.size(); f++) {
			fship = (Friendship)friends.get(f);

			if (fship.getFriendID() == fr_id && fship.getFriendType() == rel_type) {
				// If the friend is found here, then return true, since the two Persons have a connection already.
				return true;
			} // end if (check if friend id matches and the relationship role/type matches

		} // end for f (friendship ids)
		
		// If the friend was not found anywhere in the friendship array, then return false, since there is no current connection.
		return false;
	} // end isFriendsWith()
	
	/*
	public boolean isFriendsWith (Person friend) {
		// Check whether or not this person is friends with the given Person, friend.
		
		int fr_id = friend.id;
		
		int f;
		Integer tmp;
		for (f = 0; f < friends.size(); f++) {
			tmp = (Integer)friends.get(f);
			if (tmp.intValue() == fr_id) {
				// If the friend is found here, then return true, since the two Persons have a connection already.
				return true;
			}
		} // end for f (friendship ids)
		
		// If the friend was not found anywhere in the friendship array, then return false, since there is no current connection.
		return false;
	} // end isFriendsWith()
	*/
	
	
	
	
	public void addFriend (Person newFriend, Friendship friendship) {
		// Add the given Person, newFriend, to  this person's list of friends. Note that "friend" can refer generally to any form of relationship connection.
		// param newFriend: the Person with whom this person (the current object) is becoming friends
		
		//ArrayList friendship = new ArrayList();
		
		//friendship.add(new Integer(newFriend.id));
		//friendship.add(new Integer(newFriend.id));
		
		this.friends.add(friendship);
		
	} // end addFriend()
	
	
	public void addPersonToGroups () {
		// At this point, the person should have all attributes assigned, so this function will now put the person into the appropriate groups
		// based on their school, work, other activities, etc.
		// This is an important function, as each person must be put into groups in order to make friendships.
		
		
		PersonGroupAdder.AddPersonToAllGroups(this);
		

		
		
		
	} // end addPersonToGroups()
	
	
	
	private void moveToCity (String location, boolean isInSociety) {
		// This method will move the given person to the new city given as the location parameter.
		// Note that this method is called from moveFamilyToCity(), so one Person will invoke that method which will then
		// call this sub-routine for that person and their family members individually.
		//
		// param location: the name of the city to which the person is moving
		// param isInSociety: a boolean flag indicating whether or not the person is moving into the simulation society

		this.getHometownHistory().addEntry(location, Configuration.SocietyYear);
		if (isInSociety) {
			this.getSocietalHometownHistory().addEntry(location, Configuration.SocietyYear);
		} // end if (check if person is moving to local society)
		
	} // end moveToCity()
	
	public void moveFamilyToCity (String location, boolean isInSociety) {
		
		// MOVE THIS PERSON.
		this.moveToCity(location, isInSociety);
		//this.getHometownHistory().addEntry(location, Configuration.SocietyYear);
		//if (isInSociety) {
		//	this.getSocietalHometownHistory().addEntry(location, Configuration.SocietyYear);
		//} // end if (check if person is moving to local society)
		
		// MOVE SPOUSE (only in marriage, not dating relationship).
		if (this.getRelationshipStatus() == RelationshipCalculator.REL_TYPE_MARRIED) {
			Person spouse = ArtificialSociety.getPersonByID( this.getPartnerID() );
			spouse.moveToCity(location, isInSociety);
			//spouse.getHometownHistory().addEntry(location, Configuration.SocietyYear);
			//if (isInSociety) {
			//	spouse.getSocietalHometownHistory().addEntry(location, Configuration.SocietyYear);
			//} // end if (check if person is moving to local society)
		} // end if (check if person is married)
		
		// MOVE CHILDREN.
		ArrayList<Integer> children = this.getChildrenIDs();
		int numChildren = children.size();
		int i;
		Person child;
		if (numChildren > 0) {
			for (i = 0; i < numChildren; i++) {
				child = ArtificialSociety.getPersonByID( children.get(i) );
				// Only move children who are young (elementary/secondary school age or younger)
				if (child.getAge() < Configuration.SchoolFinishAge) {
					child.moveToCity(location, isInSociety);
				} // check if child is young enough that they would move with the parents)
			} // end for i (loop through all children)
			
		} // end if (check if person has children)
		
		
	} // end moveFamilyToCity()
	
	
	/*
	public void LookForJob () {
		
		
		//
		System.err.println("Person->LookForJob(); looking for job for person with:\n" +
				this.getEducation() +
				this.getPSEducationYears()
		);
		
		
		
		// BAD TEMP!!!
		this.setCareer("Zoologist");
		
		// END BAD TMP!!!
		
	} // LooForJob()
	*/
	
	
	
	public void fillInMissingAttributes () {

		// Basic.
		if (ValidationTools.empty(id)) { AttributeAssigner.assignID(this); }
		if (ValidationTools.empty(sex)) { AttributeAssigner.assignSex(this); }
		if (ValidationTools.empty(initAge)) { AttributeAssigner.assignInitAge(this); }
		if (ValidationTools.empty(yearBorn)) { AttributeAssigner.assignBirthYear(this); }
		if (ValidationTools.empty(age)) { AttributeAssigner.assignAge(this); };
		
		
		if (ValidationTools.empty(expYearDeath)) { AttributeAssigner.assignExpectedYearOfDeath(this); };
		
		//System.out.println(this.getAge() + " | " + this.getYearBorn() + " || " + (getAge()+getYearBorn()));
		
		//if (ValidationTools.empty(age)) { AttributeAssigner.assignAgeX(this); };
		//if (ValidationTools.empty(yearBorn)) { AttributeAssigner.assignBirthYearX(this); }
		
		
		//if (empty(marital_status)) { AttributeAssigner.assignSex(this); }
		//if (empty(partner_id)) { }
		

		
		// Relationship.
		if (ValidationTools.empty(relationshipStatus)) { relationshipStatus = RelationshipCalculator.REL_TYPE_SINGLE; } // Default to single if nothing has been set yet.


		// Culture.
		if (ValidationTools.empty(raceIndex)) { AttributeAssigner.assignRace(this); }
		if (ValidationTools.empty(religionIndex)) { AttributeAssigner.assignReligion(this); }
		if (ValidationTools.empty(nationality)) { AttributeAssigner.assignNationality(this); }
		if (ValidationTools.empty(templeAttending)) { templeAttending = ""; }
		
		
		

		
		
		
		// Personality.
		if (ValidationTools.empty(personality)) { AttributeAssigner.assignPersonality(this); }
		
		// Extra Traits/Attributes.
		if (ValidationTools.empty(intelligence)) { AttributeAssigner.assignIntelligence(this); }
		if (ValidationTools.empty(athleticism)) { AttributeAssigner.assignAthleticism(this); }
		
		
		
		// Career and Education.
		//if (ValidationTools.empty(careerPathID)) { AttributeAssigner.assignCareer(this); }
		if (ValidationTools.empty(careerPathID)) { AttributeAssigner.assignCareer0(this); }
		
		
		//if (ValidationTools.empty(income)) { AttributeAssigner.assignIncome(this); }
		if (ValidationTools.empty(education)) { AttributeAssigner.assignEducation(this); }
		
		// History.
		//if (ValidationTools.empty(hometownHistory)) { AttributeAssigner.assignHometownHistory(this); }
		if (ValidationTools.empty(hometownHistory)) { AttributeAssigner.assignHometownHistory_CP(this); }
		if (ValidationTools.empty(schoolHistory)) { AttributeAssigner.assignSchoolHistory(this); }
		if (ValidationTools.empty(workHistory)) { AttributeAssigner.assignWorkHistory(this); }
		
		//DebugTools.printArray(hometownHistory);
		
		// Current Position (student, working, etc.)
		if (ValidationTools.empty(currentPosition)) { AttributeAssigner.assignCurrentPosition(this); }
		
		// Family.
		if (ValidationTools.empty(family_id)) { }
		if (ValidationTools.empty(parent_ids)) { parent_ids = new ArrayList<Integer>(); }
		if (ValidationTools.empty(children_ids)) { children_ids = new ArrayList<Integer>(); }
		if (ValidationTools.empty(siblings_ids)) { siblings_ids = new ArrayList<Integer>(); }

		// Clubs.
		if (ValidationTools.empty(club_ids)) { club_ids = new ArrayList<Integer>(); }

		// Groups.
		if (ValidationTools.empty(group_ids)) { group_ids = new ArrayList<ArrayList<String>>(); }
		
		// Friendship and Friendship Determination.
		if (ValidationTools.empty(friend_prob_ids)) { friend_prob_ids = new ArrayList<Integer>(); }
		if (ValidationTools.empty(friend_probabilities)) { friend_probabilities = new ArrayList<Double>(); }
		if (ValidationTools.empty(friend_probRoles)) { friend_probRoles = new ArrayList<String>(); }
		if (ValidationTools.empty(friends)) { friends = new ArrayList<Friendship>(); }
		
		
		// Interests.
		if (ValidationTools.empty(interests)) { AttributeAssigner.assignInterests(this); }
		if (ValidationTools.empty(interestWeights)) { AttributeAssigner.assignInterestWeights(this); }
	} // end fillInMissingAttributes()
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	
	public int[] SelectFriendships_Option_A () {



		// Get array of Groups in which this person is involved.
		ArrayList personGroupIDs = this.group_ids; // List of group ids.
		int numGroups = personGroupIDs.size();
		Group[] personGroups = new Group[numGroups]; // Make array of Group objects.
		int i;
		for (i = 0; i < numGroups; i++) {
			Integer grp_id = (Integer)personGroupIDs.get(i);
			personGroups[i] = Group.getGroupByID(grp_id.intValue());
		} // end for i (groups that person is involved in)

		// Create friendships in Groups.
		int g, m;
		int numMembers;
		int mem_index;
		Group grp;
		for (g = 0; g < numGroups; g++) {
			grp = personGroups[g];
			ArrayList members = grp.MemberList;
			numMembers = members.size();
			
			// Loop through all members in the group.
			for (m = 0; m < numMembers; m++) {
				Person member = (Person) members.get(m);

				// Only examine the group member if it isn't the person himself.
				if (this.id != member.id) {

					// If this member is not currently a candidate friend for this person, then add them to the list.
					if (!this.hasConnectionWith(member)) {
						this.friend_prob_ids.add(new Integer(member.id));	// Add this member to the person's list of friendship possibilities.
						this.friend_probabilities.add(new Double(0.0));	// Initialize probability at 0.
					} // end if (new acquaintance connection)

					// Add friendship likelihood for these two members, according to this group only (i.e. each group they share will accumulate friendship likelihood).
					mem_index = getFriendshipIndex(member);

					accumulateFriendshipProbability(mem_index, FriendshipCalculator.CalculateFriendshipProbability(this, member, grp));
				} // end if (member is not the person)

			} // end for m (members in group g)

		} // end for g (groups that person is involved in) 

		// Create random friendships based on the candidates' probabilities.
		int numCandidates = this.friend_probabilities.size();
		ArrayList formedConnections = new ArrayList();
		int r;
		double p_assoc;
		double rnd;
		Double p_Dbl;
		for (r = 0; r < numCandidates; r++) {
			// Extract the probability for friendship with person at index r.
			p_Dbl = (Double)this.friend_probabilities.get(r);
			p_assoc = p_Dbl.doubleValue();
			// Generate a random number.
			rnd = Distribution.uniform(0.0, 1.0);

			if (rnd <= p_assoc) {
				formedConnections.add(this.friend_prob_ids.get(r));
			} // end if (random number is within threshold)

		} // end for r (loop through all friend candidates)

		//System.out.println("Final:");
		//DebugTools.printArray(formedConnections);

		return ArrayTools.arrayListToIntArray(formedConnections);

	} // end SelectFriendships_Option_A()
	
	
	
	
	public int[] SelectFriendships_Option_B () {

		

		// Get array of Groups in which this person is involved.
		ArrayList personGroupIDs = this.group_ids; // List of group ids.
		int numGroups = personGroupIDs.size();
		
		
		if (numGroups == 0) {
			return null;
		}
		
		Group[] personGroups = new Group[numGroups]; // Make array of Group objects.
		int i;
		for (i = 0; i < numGroups; i++) {
			Integer grp_id = (Integer)personGroupIDs.get(i);
			personGroups[i] = Group.getGroupByID(grp_id.intValue());
		} // end for i (groups that person is involved in)
		
		// Determine number of friendships to form.
		int numFriendConnections = FriendshipCalculator.DetermineLikelyNumberOfFriends(this);

		// Start with an empty array; soon, the group members will be added here and then the friendships will be selected from this pot.
		ArrayList possible_connections = new ArrayList();
		
		// Create friendships in Groups.
		int g, m;
		int numMembers;
		Group grp;
		int[] possConnectionsIDs;
		int[] uniquePossIDs;
		// Loop through all groups that person is involved in.
		for (g = 0; g < numGroups; g++) {
			grp = personGroups[g];
			ArrayList members = grp.MemberList;
			numMembers = members.size();
					
			// Loop through all members in the group.
			for (m = 0; m < numMembers; m++) {
				Person member = (Person) members.get(m);

				// Only examine the group member if it isn't the person himself.
				if (this.id != member.id) {
					possible_connections.add(new Integer(member.id));
				} // end if (member is not the person)

			} // end for m (members in group g)

		} // end for g (groups that person is involved in)
		
		// If no members in any of the groups this person is in, then stop now.
		if (possible_connections.isEmpty()) {
			//System.err.println("No fellow group members.");
			return null;
		}

		possConnectionsIDs = ArrayTools.arrayListToIntArray(possible_connections);		// This is the main array of candidates from which the friends will be selected.
		int numTotalCandidates = possConnectionsIDs.length;
		Arrays.sort(possConnectionsIDs);

		//DebugTools.printArray(possConnectionsIDs);
		uniquePossIDs = ArrayTools.unique(possConnectionsIDs);
		int numUniqueCandidates = uniquePossIDs.length;

		// Create friendship randomly from candidates.
		ArrayList selectedFriendships = new ArrayList();
		int r;

		boolean reiterate;
		int numConnections;
		int rndIndex;
		int rndPerson = -1;
		for (r = 0; r < numFriendConnections; r++) {
			reiterate = true;

			numConnections = selectedFriendships.size();
			// If the person has become friends with ALL possible candidates, then break out of the loop. This is uncommon but a good catch-all for those who aren't involved in many groups.
			if (numConnections == numUniqueCandidates) {
				// Break out of for-loop and continue execution below the loop, at the bottom of the function.
				break;
			} // end if (person has no unique candidates left)

			while (reiterate) {
				// Select random candidate from array.
				rndIndex = Distribution.uniform(0, numTotalCandidates-1);
				rndPerson = possConnectionsIDs[rndIndex];

				// Check if person has already been selected previously.
				if (ArrayTools.countOccurrencesInArray(selectedFriendships, rndPerson) > 0) {	// If the person has previously been selected.
					reiterate = true;
				} else {																		// If the person has NOT previously been selected.
					reiterate = false;
				} // end if (check whether or not person has duplicate selections)
			} // end while (selected candidate has previously been selected)

			selectedFriendships.add(new Integer(rndPerson));

		} // end for r (friendship connections)

		//System.out.println("Final:");
		//DebugTools.printArray(selectedFriendships);

		return ArrayTools.arrayListToIntArray(selectedFriendships);

	} // end SelectFriendships_Option_B()
	
	
	*/
	
	
	
	
	
	
	public void displayPerson () {
		Object[] allAttributes = new Object[] {
			new Integer(id),
				
			// Basic.
			new Integer(sex),
			new Integer(age),
			new Integer(yearBorn),
			new Integer(relationshipStatus),
			new Integer(partner_id),
				
			// Interests.
			//interests,
			//interestWeights,
				
			// Relationship.
			new Double(interestSimilarity),
			new Double(relationshipStrength),

			// Culture.
			new Integer(raceIndex),
			new Integer(religionIndex),
			nationality,
				
			// Personality.
			//personality,

			// Career.
			careerPathID,
			new Integer(income),
				
			// History.
			//hometownHistory,
			//schoolHistory,
			//workHistory,
				
			// Family.
			new Integer(family_id),
			//parent_ids,
			//children_ids,

			// Groups.
			//group_ids,

			// Friendships.
			//friend_prob_ids,
			//friend_probabilities
		};

		System.out.format(
				"id\t\t\t%s\n" +
				"sex\t\t\t%s\n" +
				"age\t\t\t%s\n" +
				"yearBorn\t\t%s\n" +
				"marital_status\t\t%s\n" +
				"partner_id\t\t%s\n" +
				//"interests\t\t%s\n" +
				//"interestWeights\t\t%s\n" +
				"I.S.\t\t\t%s\n" +
				"rel_strength\t\t%s\n" +
				"race\t\t\t%s\n" +
				"religion\t\t%s\n" +
				"nationality\t\t%s\n" +
				//"personality\t\t%s\n" +
				"career\t\t\t%s\n" +
				"income\t\t\t%s\n" +
				//"hometownHist\t\t%s\n" +
				//"schoolHist\t\t%s\n" +
				//"workHist\t\t%s\n" +
				"family_id\t\t%s\n",
				//"parent_ids\t\t%s\n" +
				//"children_ids\t\t%s\n" +
				//"group_ids\t\t%s\n" +
				//"friend_prob_ids\t\t%s\n" +
				//"friend_probs\t\t%s\n",
				allAttributes);
		System.out.print("interests = ");
		DebugTools.printArray(interests);
		System.out.print("interestWeights = ");
		DebugTools.printArray(interestWeights);
		System.out.print("personality = ");
		DebugTools.printArray(personality);
		//System.out.print("hometownHistory = ");
		//DebugTools.printArray(hometownHistory);
		//System.out.print("schoolHistory = ");
		//DebugTools.printArray(schoolHistory);
		//System.out.print("workHistory = ");
		//DebugTools.printArray(workHistory);
		//System.out.print("parent_ids = ");
		//DebugTools.printArray(parent_ids);
		//System.out.print("children_ids = ");
		//DebugTools.printArray(children_ids);
		//System.out.print("group_ids = ");
		//DebugTools.printArray(group_ids);
		//System.out.print("friend_prob_ids = ");
		//DebugTools.printArray(friend_prob_ids);
		//System.out.print("friend_probabilities = ");
		//DebugTools.printArray(friend_probabilities);

	} // end displayPerson()

} // end Person class
