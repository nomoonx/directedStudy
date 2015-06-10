package thesis_network_growth;

import java.util.ArrayList;
import java.util.Arrays;


//import static thesis_network_growth.ArtificialSociety.AllPersons;

public class FriendshipCalculator {

	/*
	public static double DetermineFriendThreshold(Person A) {
		// This function calculates the friendship threshold for the given Person, A, such that their friendship candidates will be made
		// into real friendships if and only if the friendship probability is above this threshold.
		double friendThreshold;
		
		// To start, just set a fixed threshold. This will have to change soon though!
		friendThreshold = 0.5;
		
		return friendThreshold;
	}
	*/

	public static int DetermineLikelyNumberOfFriends(Person A) {
		// This function estimates how many friendships the given Person, A, will have.
		// An important factor in this calculation is the person's level of Intraversion/Extraversion.
		// NOTE: This function is used in the SelectFriendships_Option_B() method.
		// 
		// param A: one Person from whom to check for a potential friendship
		//
		// return: the integer representing how many friends person A is likely to have

		int numFriends;
		
		
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!! How many friends?? !!!");
		
		// To start, just set a fixed number. This will have to change soon though!
		numFriends = 6;
		
		return numFriends;
	} // end DetermineLikelyNumberOfFriends()
	
	
	public static double CalculateFriendshipProbability(Person A, Person B, Group grp) {
		// This function calculates the likelihood of two Persons, A and B, becoming friends based on their involvement in the given Group, grp.
		// The group size is a reasonable term to include in the computation since smaller groups would be more friendship-prone than large groups.
		// Another possible factor to consider is the level of Intraversion/Extraversion of the persons A and/or B.
		// NOTE: This function is used in the SelectFriendships_Option_A() method.
		// 
		// param A: one Person from whom to check for a potential friendship
		// param B: the Person who is being considered as a possible friend for A
		// param grp: the Group from which persons A and B are involved; attributes of the group can help decide the likelihood of the friendship
		//
		// return: the double representing the probabilitiy of persons A and B becoming friends from this particular group (not their overall probability).
		
		double f_prob;

		// Calculate as a fixed number divided by the group size.
		//f_prob = 0.4 / (double)grp.getNumMembers();
		f_prob = Configuration.FriendFactor / (double)grp.getNumMembers();
		

		// Scale probabilities with Intraversion/Extraversion value. We will only use person A's I/E value here, not person B's.
		double Extraversion = A.getPersonality()[0];
		// Multiply the I/E value by 0.5, and add it to 0.75, so the scaleFactor will be in {0.75, 1.0, 1.25}.
		double scaleFactor = Extraversion*0.5 + 0.75;
		
		
		// Scale the probability by the scaleFactor.
		f_prob = f_prob * scaleFactor;
		
		
		
		return f_prob;
	} // CalculateFriendshipProbability()
	
	
	
	
	public static void CreateEntireFriendshipNetwork () {
		// Before this function is called, people must first be people are added to groups from the PersonGroupAdder class, and assigned relationship/family
		// information from the AttributeAssigner class. Assuming those parts are finished, then this function creates the friendship connections.
		
		
		int p;
		Person person;
		
		for (p = 0; p < ArtificialSociety.getSociety().size(); p++) {
			// Get person from global array.
			person = ArtificialSociety.getPersonByIndex(p);
		
			// CREATE FRIENDSHIPS NETWORK
			createFriendshipNetwork(person);

			// CREATE FAMILY NETWORK
			createFamilyNetwork(person);
		} // end for p (loop through all people in society)

	} // end CreateEntireFriendshipNetwork()
	
	
	public static void CreateFriendshipNetwork (Person person) {
		// Create person's friendship network.
		
		// CREATE FRIENDSHIPS NETWORK
		createFriendshipNetwork(person);

		// CREATE FAMILY NETWORK
		//createFamilyNetwork(person); // No need to call this here, as family members are already connected at child birth.

	} // end CreateFriendshipNetwork()
	
	private static void createFriendshipNetwork (Person person) {
		
		int friendshipSelectionMethod = 0;	// Set to 0 for method A; and 1 for method B.
		
		
		//int p;
		int f;
		//Person person;
		Person newFriend;
		ArrayList<Object> selectedFriendships;
		int[] selConnections;
		String[] selConnRoles;

			
		selectedFriendships = SelectFriendships_Option_A(person);
		//selectedFriendships = SelectFriendships_Option_B(person);					// Need to fix this up!
		selConnections = ArrayTools.arrayListToIntArray((ArrayList)selectedFriendships.get(0));
		selConnRoles = ArrayTools.arrayListToStringArray((ArrayList)selectedFriendships.get(1));
			
		//System.out.println("EXAMINING PERSON " + person.getID());
		//DebugTools.printArray(selConnections);
		//ArtificialSociety.DisplayIndexTable();
		//System.out.println("Dead People = " + ArtificialSociety.DeadPeopleIndices);

		// Get randomly selected list of friends.
		//selConnections = SelectFriendships_Option_A(person);
		//if (friendshipSelectionMethod == 0) {
			//selConnections = SelectFriendships_Option_A(person);
		//} else {
			//selConnections = SelectFriendships_Option_B(person);
		//} // end if (check which friendship selection method to use)
			
			
		if (selConnections != null && selConnections.length > 0) {
				
			for (f = 0; f < selConnections.length; f++) {

				newFriend = ArtificialSociety.getPersonByID(selConnections[f]);
				createFriendship(person, newFriend, Friendship.Type_Friend, selConnRoles[f].replaceAll(" ", "_"));

			} // end for f (loop through all new friends for person)

		} // end if (check if there are friends in the list)

	} // end createFriendshipNetwork()
	
	
	
	private static void createFamilyNetwork (Person person) {
		// Create the family connections for all people in the population, including spouse, children, parents, and siblings.
		
		//int p;
		//Person person;
		Person relative;

			
		// ------------------------------
		// Spouse / Partner.
		// ------------------------------
		if (!ValidationTools.empty(person.getPartnerID())) {
			relative = ArtificialSociety.getPersonByID(person.getPartnerID());
			createFriendship(person, relative, Friendship.Type_Partner, "Spouse");
		} // end if (person has spouse/partner)
			
		// ------------------------------
		// Children.
		// ------------------------------
		if (!ValidationTools.empty(person.getChildrenIDs())) {
			int i;
			int child;
			for (i = 0; i < person.getChildrenIDs().size(); i++) {
				child = person.getChildrenIDs().get(i);
				relative = ArtificialSociety.getPersonByID(child);
				createFriendship(person, relative, Friendship.Type_Family, "Child");
			} // end for i (loop through all children of this person)
		} // end if (person has children)

			
		// ------------------------------
		// Parents.
		// ------------------------------
		if (!ValidationTools.empty(person.getParentIDs())) {
			int i;
			int parent;
			for (i = 0; i < person.getParentIDs().size(); i++) {
				parent = person.getParentIDs().get(i);
				relative = ArtificialSociety.getPersonByID(parent);
				createFriendship(person, relative, Friendship.Type_Family, "Parent");
			} // end for i (loop through all parents of this person)
		} // end if (person has parents)

		// ------------------------------
		// Siblings.
		// ------------------------------
		if (!ValidationTools.empty(person.getChildrenIDs())) {
			int i;
			int sibling;
			for (i = 0; i < person.getSiblingIDs().size(); i++) {
				sibling = person.getSiblingIDs().get(i);
				relative = ArtificialSociety.getPersonByID(sibling);
				createFriendship(person, relative, Friendship.Type_Family, "Sibling");
			} // end for i (loop through all children of this person)
		} // end if (person has children)

	} // end createFamilyNetwork()
	
	
	
	
	
	
	/*
	private static void createFriendshipNetwork () {
		
		int friendshipSelectionMethod = 0;	// Set to 0 for method A; and 1 for method B.
		
		
		int p;
		int f;
		Person person;
		Person newFriend;
		ArrayList<Object> selectedFriendships;
		int[] selConnections;
		String[] selConnRoles;
		
		for (p = 0; p < ArtificialSociety.getSociety().size(); p++) {
			// Get person from global array.
			person = ArtificialSociety.getPersonByIndex(p);
			
			
			selectedFriendships = SelectFriendships_Option_A(person);
			//selectedFriendships = SelectFriendships_Option_B(person);					// Need to fix this up!
			selConnections = ArrayTools.arrayListToIntArray((ArrayList)selectedFriendships.get(0));
			selConnRoles = ArrayTools.arrayListToStringArray((ArrayList)selectedFriendships.get(1));
			
			//System.out.println("EXAMINING PERSON " + person.getID());
			//DebugTools.printArray(selConnections);
			//ArtificialSociety.DisplayIndexTable();
			//System.out.println("Dead People = " + ArtificialSociety.DeadPeopleIndices);
			
			// Get randomly selected list of friends.
			//selConnections = SelectFriendships_Option_A(person);
			//if (friendshipSelectionMethod == 0) {
				//selConnections = SelectFriendships_Option_A(person);
			//} else {
				//selConnections = SelectFriendships_Option_B(person);
			//} // end if (check which friendship selection method to use)
			
			
			if (selConnections != null && selConnections.length > 0) {
				
				for (f = 0; f < selConnections.length; f++) {
					//newFriend = (Person)AllPersons.get(selConnections[f]);
					
					//DEBUG
					//if (Configuration.SocietyYear == 2022 && selConnections[f] == 9) {
						//System.err.println("HOW CAN THIS BE?????????????");
					//}
					
					newFriend = ArtificialSociety.getPersonByID(selConnections[f]);
					
					
					//System.out.println(person.religionIndex + "  " + newFriend.religionIndex + " | " + person.id + " && " + newFriend.id + " || " + selConnRoles[f]);
					
					
					//System.out.println(person.id + " && " + newFriend.id);
					createFriendship(person, newFriend, Friendship.Type_Friend, selConnRoles[f].replaceAll(" ", "_"));
				} // end for f (loop through all new friends for person)
				
			} // end if (check if there are friends in the list)
			
		} // end for p (loop through all people in society population)
	} // end createFriendshipNetwork()
	
	
	
	private static void createFamilyNetwork () {
		// Create the family connections for all people in the population, including spouse, children, parents, and siblings.
		
		int p;
		Person person;
		Person relative;
		
		for (p = 0; p < ArtificialSociety.getSociety().size(); p++) {
			// Get person from global array.
			person = ArtificialSociety.getPersonByIndex(p);
			
			// ------------------------------
			// Spouse / Partner.
			// ------------------------------
			if (!ValidationTools.empty(person.getPartnerID())) {
				relative = ArtificialSociety.getPersonByID(person.getPartnerID());
				createFriendship(person, relative, Friendship.Type_Partner, "Spouse");
			} // end if (person has spouse/partner)
			
			// ------------------------------
			// Children.
			// ------------------------------
			if (!ValidationTools.empty(person.getChildrenIDs())) {
				int i;
				int child;
				for (i = 0; i < person.getChildrenIDs().size(); i++) {
					child = person.getChildrenIDs().get(i);
					relative = ArtificialSociety.getPersonByID(child);
					createFriendship(person, relative, Friendship.Type_Family, "Child");
				} // end for i (loop through all children of this person)
			} // end if (person has children)

			
			// ------------------------------
			// Parents.
			// ------------------------------
			if (!ValidationTools.empty(person.getParentIDs())) {
				int i;
				int parent;
				for (i = 0; i < person.getParentIDs().size(); i++) {
					parent = person.getParentIDs().get(i);
					relative = ArtificialSociety.getPersonByID(parent);
					createFriendship(person, relative, Friendship.Type_Family, "Parent");
				} // end for i (loop through all parents of this person)
			} // end if (person has parents)
			
			// ------------------------------
			// Siblings.
			// ------------------------------
			if (!ValidationTools.empty(person.getChildrenIDs())) {
				int i;
				int sibling;
				for (i = 0; i < person.getSiblingIDs().size(); i++) {
					sibling = person.getSiblingIDs().get(i);
					relative = ArtificialSociety.getPersonByID(sibling);
					createFriendship(person, relative, Friendship.Type_Family, "Sibling");
				} // end for i (loop through all children of this person)
			} // end if (person has children)
			
		} // end for p (loop through all people in society population)
		
	} // end createFamilyNetwork()
	*/
	
	public static void displayNetwork () {
		
		int i, j;
		int f_id;
		Person personA, personB;
		Friendship friendship;
		
		for (i = 0; i < ArtificialSociety.getSociety().size(); i++) {
			
			// Get person from global array.
			personA = ArtificialSociety.getPersonByIndex(i);
			
			
			//DebugTools.printArray(personA.friend_prob_ids);
			//DebugTools.printArray(personA.friends);
			
			// Loop through all person's friends.
			for (j = 0; j < personA.getFriends().size(); j++) {
				
				friendship = (Friendship)personA.getFriends().get(j);
				
				f_id = friendship.getFriendID();
				
				personB = ArtificialSociety.getPersonByID(f_id);
				
				System.out.println(personA.getID() + " <---> " + personB.getID());
				
			} // end for j (loop through friends list)
						
			
		} // end for i (loop through all people)
		
	} // end displayNetwork()
	
	
	
	
	public static void createFriendship (Person personA, Person personB, int friendshipType, String desc) {
		// This function will create any friendship, by setting both Persons, personA and personB, to have one another in the list of
		// friends. Note that the term "friend" or "friendship" does not just mean friends but rather a more general meaning for a connection between two people.
		// param personA: the first person in the new friendship
		// param personB: the first person in the new friendship
		
		// Note that for any friendship, we check if the friendship already exists with those roles (i.e. two people may have a family connection but not a
		// friend connection), and also check that the friend is not the person themselves. A person cannot be friends with themselves.
		
		
		if (!personA.isFriendsWith(personB, friendshipType) && personA.getID() != personB.getID()) {
			// Only add friendship if there is not already a friendship between them.
			Friendship newFriendship = new Friendship(personA, personB, friendshipType, 0.0, desc);
			personA.addFriend(personB, newFriendship);
		} // end if (check if A is already friends with B)
		
		if (!personB.isFriendsWith(personA, friendshipType) && personB.getID() != personA.getID()) {
			// Only add friendship if there is not already a friendship between them.
			Friendship newFriendship = new Friendship(personB, personA, friendshipType, 0.0, desc);
			personB.addFriend(personA, newFriendship);
		} // end if (check if B is already friends with A)
		
	} // end createFriendship()
	
	
	
	//private static int[] SelectFriendships_Option_A (Person person) {
	private static ArrayList<Object> SelectFriendships_Option_A (Person person) {

		
		//System.out.println("SelectFriendships_Option_A");
	
		// Get array of Groups in which this person is involved.
		ArrayList<ArrayList<String>> personGroupIDs = person.getGroupIDs(); // List of group ids.

		int numGroups = personGroupIDs.size();
		Group[] personGroups = new Group[numGroups]; // Make array of Group objects.
		String[] personGroupRoles = new String[numGroups]; // Make array to keep track of person's roles in the groups.
		int i;
		ArrayList<String> groupRoleInfo;
		int grp_id;
		String grp_role;
		for (i = 0; i < numGroups; i++) {
			groupRoleInfo = (ArrayList<String>)personGroupIDs.get(i);
			grp_id = Integer.parseInt((String)groupRoleInfo.get(0));				// groupRoleInfo is of the format: [GroupID, GroupRole]
			grp_role = (String)groupRoleInfo.get(1);
			//Integer grp_id = (Integer)personGroupIDs.get(i);
			personGroups[i] = Group.getGroupByID(grp_id);
			personGroupRoles[i] = grp_role;
		} // end for i (groups that person is involved in)

		// Create friendships in Groups.
		int g, m;
		int numMembers;
		int mem_index;
		Group grp;
		String friendRole;
		//ArrayList<String> friend_prob_roles = new ArrayList<String>();
		ArrayList<Person> members;
		Person member;
		
		
		for (g = 0; g < numGroups; g++) {
			grp = personGroups[g];
			members = grp.getMembers();
			numMembers = members.size();
			
			
			
			// Loop through all members in the group.
			for (m = 0; m < numMembers; m++) {
				member = (Person)members.get(m);

				// Only examine the group member if it isn't the person himself.
				if (person.getID() != member.getID()) {
					
					friendRole = member.getRoleInGroup(grp.GroupID);

					// If this member is not currently a candidate friend for this person, then add them to the list.
					if (!person.hasConnectionWith(member)) {
						person.getFriendProbIDs().add(new Integer(member.getID()));	// Add this member to the person's list of friendship possibilities.
						person.getFriendProbabilities().add(new Double(0.0));	// Initialize probability at 0.
						//friend_prob_roles.add(friendRole);
						person.getFriendProbRoles().add(friendRole);
					} // end if (new acquaintance connection)

					// Add friendship likelihood for these two members, according to this group only (i.e. each group they share will accumulate friendship likelihood).
					mem_index = person.getFriendshipIndex(member);

					person.accumulateFriendshipProbability(mem_index, FriendshipCalculator.CalculateFriendshipProbability(person, member, grp));

				} // end if (member is not the person)

			} // end for m (members in group g)

		} // end for g (groups that person is involved in) 

		// Create random friendships based on the candidates' probabilities.
		int numCandidates = person.getFriendProbabilities().size();
		
		ArrayList<Integer> formedConn_ids = new ArrayList<Integer>();
		ArrayList<String> formedConn_roles = new ArrayList<String>();
		int r;
		double p_assoc;
		double rnd;
		Double p_Dbl;

		for (r = 0; r < numCandidates; r++) {
			// Extract the probability for friendship with person at index r.
			p_Dbl = (Double)person.getFriendProbabilities().get(r);
			p_assoc = p_Dbl.doubleValue();
			// Generate a random number.
			rnd = Distribution.uniform(0.0, 1.0);

			if (rnd <= p_assoc) {
				formedConn_ids.add(person.getFriendProbIDs().get(r));
				formedConn_roles.add(person.getFriendProbRoles().get(r));

			} // end if (random number is within threshold)
		} // end for r (loop through all friend candidates)
		
		ArrayList<Object> formedConnections = new ArrayList<Object>();
		formedConnections.add(formedConn_ids);
		formedConnections.add(formedConn_roles);

		return formedConnections;

	} // end SelectFriendships_Option_A()




	//private static int[] SelectFriendships_Option_B (Person person) {
	private static ArrayList<Object> SelectFriendships_Option_B (Person person) {
	
		//System.out.println("SelectFriendships_Option_B");

		// Get array of Groups in which this person is involved.
		ArrayList<ArrayList<String>> personGroupIDs = person.getGroupIDs(); // List of group ids.
		int numGroups = personGroupIDs.size();
		
		
		if (numGroups == 0) {
			return null;
		} // end if (return null if the person is not in any groups)
		
		Integer grp_id;
		Group[] personGroups = new Group[numGroups]; // Make array of Group objects.
		ArrayList<String> groupRoleInfo;
		int i;
		for (i = 0; i < numGroups; i++) {
			groupRoleInfo = personGroupIDs.get(i);
			grp_id = Integer.parseInt((String)groupRoleInfo.get(0));
			personGroups[i] = Group.getGroupByID(grp_id.intValue());
		} // end for i (groups that person is involved in)
		
		// Determine number of friendships to form.
		int numFriendConnections = FriendshipCalculator.DetermineLikelyNumberOfFriends(person);

		// Start with an empty array; soon, the group members will be added here and then the friendships will be selected from this pot.
		ArrayList<Integer> possible_connections = new ArrayList<Integer>();
		
		// Create friendships in Groups.
		int g, m;
		int numMembers;
		Group grp;
		int[] possConnectionsIDs;
		int[] uniquePossIDs;
		ArrayList<Person> members;
		Person member;
		// Loop through all groups that person is involved in.
		for (g = 0; g < numGroups; g++) {
			grp = personGroups[g];
			members = grp.getMembers();
			numMembers = members.size();
					
			// Loop through all members in the group.
			for (m = 0; m < numMembers; m++) {
				member = (Person) members.get(m);

				// Only examine the group member if it isn't the person himself.
				if (person.getID() != member.getID()) {
					possible_connections.add(new Integer(member.getID()));
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
		ArrayList<Object> selectedFriendships = new ArrayList<Object>();
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
				if (ArrayTools.countOccurrencesInArrayList(selectedFriendships, rndPerson) > 0) {	// If the person has previously been selected.
					reiterate = true;
				} else {																		// If the person has NOT previously been selected.
					reiterate = false;
				} // end if (check whether or not person has duplicate selections)
			} // end while (selected candidate has previously been selected)

			selectedFriendships.add(new Integer(rndPerson));

		} // end for r (friendship connections)

		//System.out.println("Final:");
		//DebugTools.printArray(selectedFriendships);

		return selectedFriendships;
		//return ArrayTools.arrayListToIntArray(selectedFriendships);

	} // end SelectFriendships_Option_B()
	
	
	
} // end FriendshipCalculator class
