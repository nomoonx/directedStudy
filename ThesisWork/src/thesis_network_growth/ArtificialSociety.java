package thesis_network_growth;

import java.util.ArrayList;

public class ArtificialSociety {

	static ArrayList<Integer> PersonIndexTable;

	//static ArrayList<Person> AllPersons;
	static Population AllPersons;

	static Population DeadPersons;
	static ArrayList<Integer> DeadPeopleIndices; // TEMP!
	
	
	//static Graph<String, String> graph;


	public static void createNewSociety () {
	
		PersonIndexTable = new ArrayList<Integer>();
		Person.NextAvailablePersonID = 0; // Reset IDs at 0. 
	
		//AllPersons = new ArrayList<Person>();
		AllPersons = new Population(); // Living only!
		DeadPersons = new Population();


		DeadPeopleIndices = new ArrayList<Integer>(); // TEMP
	} // end createNewSociety()
	
	
	public static Population getSociety () {
		return AllPersons;
	} // end getSociety()

	public static int getPersonID (int i) {
		return PersonIndexTable.get(i);
	} // end getPersonID()

	
	public static Person getPersonByIndex (int index) {
		// Get the Person at the given index in the array.
		//
		// param index: the array index to return the person from
		//
		// return: the Person instance at the given index (regardless of person ID)

		if (index >= getSociety().size()) {
			// Index is out of the society limits.
			System.err.println("ArtificialSociety->getPersonByIndex(); invalid index in society: " + index);
			return null;
		} else {
			// Index is valid, within the society.
			return getSociety().get(index);
		} // end if (check if given index is valid)

	} // end getPersonByIndex()
	
	/*
	public static Person getPersonByIndex (int index) {
		// Get the Person at the given index in the array.
		//
		// param index: the array index to return the person from
		//
		// return: the Person instance at the given index (regardless of person ID)

		return getPersonByIndex(getSociety(), index);
		
	} // end getPersonByIndex()
	

	public static Person getPersonByIndex (Population population, int index) {
		// Get the Person at the given index in the array.
		//
		// param index: the array index to return the person from
		//
		// return: the Person instance at the given index (regardless of person ID)

		return population.get(index);

	} // end getPersonByIndex()
	*/
	
	public static int getLivingPersonIndexByID (int id) {
		// Get the array index pointing to the Person with the specified ID.
		//
		// param id: the ID of the person to be returned
		//
		// return: the integer index pointing to the Person instance with the specified ID
		
		//System.out.println("getLivingPersonIndexByID(); id = " + id);
		
		// Since people can be removed, so that IDs can become greater than the indices, we are going to search for the given
		// ID in the array by beginning the index at the value of the ID, and then DECREMENTING the index until it is found.
		// If the ID is beyond the array size, then default to the last element of the array and then decrement from there.
		int i;
		//if (id < getSociety().size()) {
		if (id < getSociety().size() - 1) {
			//System.out.println("Begin search at id (" + id + ")");
			i = id; // The default starting index at which to search is the person's ID.
		} else {
			//System.out.println("Begin search at len-1 (" + (getSociety().size()-1) + ")");
			i = getSociety().size()-1; // If ID is not a valid index, then begin at the last element in the array.
		} // end if (check if person's ID is even a valid array index still!)

		
		//System.out.println("getLivingPersonIndexByID(); id = " + id + "; i = " + i + " before loop");
		//System.out.println("Debugging || i = " + i + " person at i has id = " + getPersonByIndex(i).getID());
		
		boolean foundElement = false;
		while (!foundElement && i >= 0) {
			
			// Check if this ID is the one being searched for.
			if (getPersonByIndex(i).getID() == id) {
				//System.out.println("getLivingPersonIndexByID(); id = " + id + "; i = " + i + " INSIDE LOOP/IF   " + getPersonByIndex(i).getID());
				// Indicate that the element was found.
				foundElement = true;
			} else {
				// Decrement index to search at previous array element.
				i--;
			} // end if (check if ID at index i is the one being searched for)

		} // end while (loop from ID down toward 0 until ID is found or no match is ever found)
		
		//System.out.println("getLivingPersonIndexByID(); id = " + id + "; i = " + i + " after loop");
		
		if (foundElement) {
			// If the specified ID was found, then return the index to the Person with that ID.
			return i;
		} else {
			System.err.println("In ArtificialSociety->getLivingPersonIndexByID(); the search completed and the person was not found with ID " + id + ".");
			System.out.println("ERRRRRRRRRRRRRRRRRRRRROR");
			return Integer.MIN_VALUE;
		} // end if (check whether the Person's ID was found or if the search finished without finding the Person's ID.
		
	} // end getLivingPersonIndexByID()
	
	/*
	public static int getDeadPersonIndexByID (int id) {
		// Get the array index pointing to the dead Person with the specified ID.
		//
		// param id: the ID of the dead person to be returned
		//
		// return: the integer index pointing to the dead Person instance with the specified ID
		
		int numDeadPeople = DeadPersons.size();

		int i = 0;
		boolean foundElement = false;
		while (!foundElement && i < numDeadPeople) {
			
			// Check if this ID is the one being searched for.
			if (getPersonByIndex(DeadPersons, i).getID() == id) {
				// Indicate that the element was found.
				foundElement = true;
			} else {
				// Increment index.
				i++;
			} // end if (check if ID at index i is the one being searched for)

		} // end while (loop from ID down toward 0 until ID is found or no match is ever found)
		
		if (foundElement) {
			// If the specified ID was found, then return the index to the Person with that ID.
			return i;
		} else {
			System.err.println("In ArtificialSociety->getDeadPersonIndexByID(); the search completed and the person was not found with ID " + id + ".");
			return Integer.MIN_VALUE;
		} // end if (check whether the Person's ID was found or if the search finished without finding the Person's ID.
		
	} // end getDeadPersonIndexByID()
	*/
	
	
	
	public static Person getPersonByID (int id) {
		// Get the Person with the specified ID.
		//
		// param id: the ID of the person to be returned
		//
		// return: the Person instance with the specified ID (regardless of the array index)
		
		//System.out.println("before... in getPersonByID..." + id);
		int index = getLivingPersonIndexByID(id);
		//System.out.println("after ... in getPersonByID..." + id);

		
		// Check if index is valid.
		if (index == Integer.MIN_VALUE) {
			
			// SHOULD NEVER REACH HERE!
			System.err.println("Should not be here, in ArtificialSociety->getPersonByID() ... " + index + " | " + id);
			
			// Ensure that if no match is found, that the person is dead.
			//if (id < 0) {
				// Return dead person. (ID is negative when dead).
				
				
				//System.out.println("Person is dead (ID = " + id + "). and index = " + index);
				//return DeadPersons.get(index);
			
			
			//} else {
				//System.err.println("MAJOR PROBLEMS! In ArtificialSociety->getPersonByID()");
				//return null;
			//} // end if (ensure that person is dead)
			
		} // end if (check if ID was found by checking if index == Integer.MIN_VALUE)
		


			
		//System.out.println("Person is alive (ID = " + id + ").");
			
		// Return living person.
		return getPersonByIndex(index);
		
		
		
		
		
		

			
	} // end getPersonByID()

	
	/*
	public static Person getPersonByID (int id) {
		// Get the Person with the specified ID.
		//
		// param id: the ID of the person to be returned
		//
		// return: the Person instance with the specified ID (regardless of the array index)
		
		
		// Since people can be removed, so that IDs can become greater than the indices, we are going to search for the given
		// ID in the array by beginning the index at the value of the ID, and then DECREMENTING the index until it is found.
		int i = id; // The default starting index at which to search is the ID.
		boolean foundElement = false;
		Population popn = getSociety();
		while (!foundElement && i >= 0) {
			
			// Check if this ID is the one being searched for.
			if (getPersonByIndex(i).getID() == id) {
				// Indicate that the element was found.
				foundElement = true;
			} else {
				// Decrement index to search at previous array element.
				i--;
			} // end if (check if ID at index i is the one being searched for)

		} // end while (loop from ID down toward 0 until ID is found or no match is ever found)
		
		if (foundElement) {
			// If the specified ID was found, then return the Person with that ID.
			return getPersonByIndex(i);
		} else {
			System.err.println("In ArtificialSociety->getPersonById(); the search completed and the person was not found with ID " + id + ".");
			return null;
		} // end if (check whether the Person's ID was found or if the search finished without finding the Person's ID.
		
	} // end getPersonByID()
	*/
	
	
	
	public static void addPersonID (int id) {
		// Add new person's ID to the table at the same index (id = index).
		getPersonIndexTable().add(id);
		

		//DisplayIndexTable();
	} // end addPersonID()
	
	public static ArrayList<Integer> getPersonIndexTable () {
		return PersonIndexTable;
	} // end getPersonIndexTable()
	
	public static void addPersonToSociety (Person person) {
		// Add the given Person, person, to the global persons array.
		AllPersons.add(person);
	} // end addPersonToSociety()
	
	
	
	public static void removePersonByID (int personID) {
		// Remove the person, with the given ID, from the society. Note that this Person instance still exists, so memory is not
		// freed from the Person instance. However, the society removes references to this person so any loops over the society
		// will not include the dead persons.
		//
		// param personID: the integer ID of the person to be removed (note that ID is different than index!)

		//System.out.println("Person " + personID + " has now died.");


		Person person = ArtificialSociety.getPersonByID(personID);
		
		//DisplayIndexTable();
		//DisplayIndexTable2();
		
		int personIndexInArray = getLivingPersonIndexByID(personID);

		//System.out.println("Person " + personID + " has now died.    index = " + personIndexInArray);
		
		//DisplayIndexTable();
		
		// Remove person from living population and index table.
		getSociety().remove(personIndexInArray);
		getPersonIndexTable().remove(personIndexInArray);
		
		//DisplayIndexTable();

		
		// Add person to dead population.
		DeadPersons.add(person);
		DeadPeopleIndices.add(personID); // TEMP
		
		// -------------------------------------------------------------------------------
		// REMOVE DEAD PERSON FROM ALL REFERENCES.
		// -------------------------------------------------------------------------------

		Person connPerson; // This will hold any person who has a connection to the deceased person.
		int i;

		// =============================
		// SPOUSE / PARTNER
		// =============================
		// If in relationship, update partner.
		if (person.getRelationshipStatus() == RelationshipCalculator.REL_TYPE_MARRIED || person.getRelationshipStatus() == RelationshipCalculator.REL_TYPE_DATING) {

			connPerson = getPersonByID( person.getPartnerID() );

			// Update partner's partner_id, and change the partner's status to be widowed.
			connPerson.setPartnerID( Integer.MIN_VALUE );
			connPerson.setRelationshipStatus( RelationshipCalculator.REL_TYPE_WIDOWED );
			
		} // end if (check if couple is dating or married)

		// =============================
		// PARENTS.
		// =============================
		for (i = 0; i < person.getParentIDs().size(); i++) {
			connPerson = getPersonByID( person.getParentIDs().get(i) );
			connPerson.getChildrenIDs().remove(new Integer(personID)); // The parents would store this person in their children array.
		} // end for i (loop through person's parents)

		// =============================
		// CHILDREN.
		// =============================
		for (i = 0; i < person.getChildrenIDs().size(); i++) {
			connPerson = getPersonByID( person.getChildrenIDs().get(i) );
			connPerson.getParentIDs().remove(new Integer(personID)); // The children would store this person in their parents array.
		} // end for i (loop through person's children)

		// =============================
		// SIBLINGS.
		// =============================
		for (i = 0; i < person.getSiblingIDs().size(); i++) {
			connPerson = getPersonByID( person.getSiblingIDs().get(i) );
			connPerson.getSiblingIDs().remove(new Integer(personID));
		} // end for i (loop through person's siblings)

		// =============================
		// FRIENDS.
		// =============================
		Friendship friendship;
		for (i = 0; i < person.getFriends().size(); i++) {
			friendship = person.getFriends().get(i);
			connPerson = getPersonByID( friendship.getFriendID() );
			connPerson.removeFriend(personID);
		} // end for i (loop through person's friends)
	
		// =============================
		// POTENTIAL FRIENDS.
		// * IMPORTANT NOTE * This is different than all the above removals of references to the deceased person.
		// For all the others, the connections are reflexive, but for potential friends, it is NOT reflexive so the entire
		// society is being looped through and anyone who has the deceased as a potential friend will remove them.
		// =============================
		int j;
		int numTotalPersons = getSociety().size();
		for (i = 0; i < numTotalPersons; i++) { // Loop through everyone because potential friends are not reflexive!
			connPerson = getPersonByIndex( i );
			
			int numOccurrences = ArrayTools.countOccurrencesInArray(connPerson.getFriendProbIDs(), personID);
			if (numOccurrences > 1) {
				System.err.println("ArtificialSociety->removePersonByID(); multiple occurences of this person as a potential friend. Add loop to remove ALL matches!");
			}
			
			// Since friendProbIDs, friendProbabilities, and friendProbRoles are parallel arrays, we must find the index of this
			// person in the connPerson's friendProbIDs list, and then remove from all arrays the element at that index.
			int friendProbIndex = ArrayTools.getElementIndex(connPerson.getFriendProbIDs(), personID); // Get index in array.

			if (numOccurrences == 1) {
				connPerson.getFriendProbIDs().remove(friendProbIndex);
				connPerson.getFriendProbabilities().remove(friendProbIndex);
				connPerson.getFriendProbRoles().remove(friendProbIndex);
			} // end if (check if a match was found)

		} // end for i (loop through person's friends)

		// =============================
		// GROUPS.
		// =============================
		ArrayList<String> group;
		Group grp;
		for (i = 0; i < person.getGroupIDs().size(); i++) {
			group = person.getGroupIDs().get(i);
			grp = Group.getGroupByID(Integer.parseInt(group.get(0))); // group[0] is the ID, and group[1] is the person's role.
			grp.removeMember(person);
		} // end for i (loop through person's groups)

	} // end removePerson()

	
	
	public static void DestroySociety () {
		// Delete all the components of the society to free memory.
		
		int p;
		Person person;
		for (p = 0; p < getSociety().size(); p++) {
			person = getPersonByIndex(p);
			person = null;
		}
		
		for (p = 0; p < DeadPersons.size(); p++) {
			person = DeadPersons.get(p);
			person = null;
		}
		
		p = 0;
		while (AllPersons.size() > 0) {
			AllPersons.remove(p);
		} // end while (removing each person from population)
		
		PersonIndexTable = null;
		AllPersons = null;
		DeadPersons = null;
		DeadPeopleIndices = null;
	} // end DestroySociety();
	
	
	
	
	// TEMPORARY
	public static void DisplayIndexTable () {
		int numPersons = getPersonIndexTable().size();
		System.out.println("-------------------");
		System.out.println("Total\t|\t" + numPersons);
		int i;
		System.out.println("-------------------");
		for (i = 0; i < numPersons; i++) {
			//System.out.println(" " + i + "\t|\t" + getPersonIndexTable().get(i) + " | " + getPersonID(i));
			System.out.println(" " + i + "\t|\t" + getPersonIndexTable().get(i));
			//System.out.println(" " + i + "\t|\t" + getPersonByIndex(i));
			
		} // end for i (loop through all people)
		System.out.println("-------------------");
	} // end DisplayIndexTable()
	
	public static void DisplayAttributeTable (String attribute) {
		// THIS CURRENTLY ONLY DISPLAYS AGES, BUT COULD BE MODIFIED TO USE THE "attribute" PARAMETER FOR OTHER ATTRIBUTES.
		int numPersons = getPersonIndexTable().size();
		System.out.println("-------------------");
		System.out.println("Total\t|\t" + numPersons);
		int i;
		System.out.println("-------------------");
		for (i = 0; i < numPersons; i++) {
			//System.out.println(" " + i + "\t|\t" + getPersonIndexTable().get(i) + " | " + getPersonID(i));
			System.out.println(" " + i + "\t|\t" + getPersonByIndex(i).getAge());
			//System.out.println(" " + i + "\t|\t" + );
			
		} // end for i (loop through all people)
		System.out.println("-------------------");
	} // end DisplayIndexTable()
	
	
	
	
	
	public static void DisplayIndexTable2 () {
		int numPersons = getSociety().size();
		int i;
		System.out.println("-------------------");
		for (i = 0; i < numPersons; i++) {
			System.out.println(" " + i + "\t|\t" + getPersonByIndex(i).getID());
			
		} // end for i (loop through all people)
		System.out.println("-------------------");
	} // end DisplayIndexTable()
	
	/*
	public static void setGraph (Graph<String,String> gr) {
		graph = gr;
	} // end setGraph()
	
	public static Graph<String,String> getGraph () {
		return graph;
	} // end getGraph()
	*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
} // end ArtificialSociety class
