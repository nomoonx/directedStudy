package thesis_network_growth;


public class Friendship {
	
	public static int Type_Family = 0;
	public static int Type_Partner = 1;
	//public static int Type_BestFriend = 2;
	public static int Type_Friend = 3;
	//public static int Type_Acquaintance = 4;
	
	
	int idA;
	int idB;
	int type;
	double strength;
	String description;
	// Possible addition could be: int contactFriendFrequency or seeFriendFrequency
	
	
	
	public Friendship(Person personA, Person personB, int rel_type, double rel_strength, String rel_description) {
		idA = personA.getID();
		idB = personB.getID();
		type = rel_type;
		strength = rel_strength;
		description = rel_description;
		
	} // end Friendship() constructor

	public Friendship(int personA_id, int personB_id, int rel_type, double rel_strength, String rel_description) {
		idA = personA_id;
		idB = personB_id;
		type = rel_type;
		strength = rel_strength;
		description = rel_description;
		
	} // end Friendship() constructor
	
	public int getID () {
		return idA;
	} // end getID()
	
	public int getFriendID () {
		return idB;
	} // end getFriendID()
	
	public int getFriendType () {
		return type;
	} // end getFriendType()
	
	public double getFriendStrength () {
		return strength;
	} // end getFriendStrength()
	
	public String getFriendDescription () {
		return description;
	} // end getFriendDescription()
	
} // end Friendship class
