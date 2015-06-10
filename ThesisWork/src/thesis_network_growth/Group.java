package thesis_network_growth;

import java.util.ArrayList;

public class Group {
	
	// Static information about Groups in general.
	static int nextAvailableID = 0;
	static ArrayList<Group> AllGroups = new ArrayList<Group>();
	
	// Group information.
	int GroupID;
	
	String GroupLabel;
	int GroupYear;
	ArrayList<Person> MemberList;
	boolean isActive;
	ArrayList<Integer> ParentGroupIDs;
	
	
	public Group (String label) {
		initGroup(label);
	} // end Group() constructor
	
	public Group (String label, int year) {
		GroupYear = year;
		initGroup(label);
	} // end Group() constructor
	
	private void initGroup (String label) {
		// Assign this new group with the next available ID, then ensure the nextAvailableID is incremented right away!
		GroupID = nextAvailableID;
		nextAvailableID++;

		GroupLabel = label;
		
		// Initialize the member arrays.
		MemberList = new ArrayList<Person>();
		ParentGroupIDs = new ArrayList<Integer>();

		// Add this group to the static global list of all groups.
		AllGroups.add(this);
	} // end initGroup()
	
	
	public void addMember (Person person) {
		// Add the given person to this group member list.
		// * NOTE * This is called from Person->addToGroup() ONLY! It should not be called from anywhere else, or there could be issues!

		MemberList.add(person);
	} // end addMember()
	
	public void removeMember (Person person) {
		// Remove the given person from the group member list.

		//System.err.println("removing member from group .. " + person.getID() + " from grp " + this.getGroupLabel());

		
		
		int i = 0;
		while (i < getNumMembers()) {
			// Loop through all members, and if the current member matches the ID of the one being removed, then remove them!
			if (getMembers().get(i).getID() == person.getID()) {
				MemberList.remove(i);
				i--; // Decrement counter so that the subsequent element does not get omitted after index shift)
			} // end if (check if member in array matches the person being removed)
			
			i++; // Increment counter to move on to the next member.
		} // end while (loop through all members)

		//MemberList.remove(person);
	} // end removeMember()
	
	public ArrayList<Person> getMembers () {
		// Get the list of members of this group.
		return MemberList;
	} // end getMembers()
	
	public int getNumMembers () {
		// Get the number of members of this group.
		return MemberList.size();
	} // end getNumMembers()
	
	public String getGroupLabel () {
		return GroupLabel + " (" + GroupYear + ")";
	} // end getGroupLabel()
	
	public static Group getGroupByID (int id) {
		// Get the Group with the given ID.
		return (Group)AllGroups.get(id);
	} // end getGroupByID()
	
	

} // end Group class
