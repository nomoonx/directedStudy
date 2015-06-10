package thesis_network_growth;

import java.util.ArrayList;


public class PersonAttributes {
	/* This class is important for putting as many attributes as are known into this instance to pass into
	 * the Person constructor when generating each person. This way, some information will be known a priori
	 * whereas other information will be decided later, and this PersonAttributes instance will contain as
	 * much or as little as known at the time of generating the person.
	 * 
	 * This class should contain essentially all the same member variables as the Person class does. The only
	 * exception (as of now) is the 'id' member which is not necessary here, but is in Person.
	 */
	
	// Basic.
	int sex = Integer.MIN_VALUE;
	int age = Integer.MIN_VALUE;
	int yearBorn = Integer.MIN_VALUE;
	int relationshipStatus = Integer.MIN_VALUE;				// {0 = Single; 1 = Dating; 2 = Married; (3 = Divorced ?)}
	int partner_id = Integer.MIN_VALUE;
	
	// Interests.
	double[] interests = null;
	double[] interestWeights = null;
	
	// Relationship.
	int interestSimilarity = Integer.MIN_VALUE;
	double relationshipStrength = Double.MIN_VALUE;

	// Culture.
	int raceIndex = Integer.MIN_VALUE;
	int religionIndex = Integer.MIN_VALUE;
	String nationality = null;
	
	// Personality.
	double[] personality = null;

	// Extra Traits/Attributes (useful in club assignment)
	double intelligence = Double.MIN_VALUE;
	double athleticism = Double.MIN_VALUE;

	// Career and Education.
	String careerPathID = null;
	int income = Integer.MIN_VALUE;
	String education = null;			// {N = None; HS = High School; C = College; U = University}
	int educationPSYears = Integer.MIN_VALUE;
	boolean isInSchool;
	int yearStartedPSSchool;
	int yearFinishedPSSchool;
	
	// History.
	ActivityArchive hometownHistory = null;
	ActivityArchive schoolHistory = null;
	ActivityArchive workHistory = null;

	// History In Society.
	ActivityArchive soc_HometownHistory = null;
	ActivityArchive soc_SchoolHistory = null;
	ActivityArchive soc_WorkHistory = null;

	// Family.
	int family_id = Integer.MIN_VALUE;
	ArrayList parent_ids = null;
	ArrayList children_ids = null;
	ArrayList siblings_ids = null;

	// Clubs.
	ArrayList club_ids;

	// Groups.
	ArrayList group_ids = null;
	
	// Friendship determination.
	ArrayList friend_prob_ids = null;
	ArrayList friend_probabilities = null;

	// Friendship.
	ArrayList friends;
	
	
	public PersonAttributes () {
		
	}
	
	
	/*
	public void fillInMissingAttributes () {
		//id = 0;

		// Basic.
		if (ValidationTools.empty(sex)) { AttributeAssigner.assignSex(this); }
		if (ValidationTools.empty(age)) { AttributeAssigner.assignAge(this); };
		if (ValidationTools.empty(yearBorn)) { AttributeAssigner.assignBirthYear(this); }
		//if (empty(marital_status)) { AttributeAssigner.assignSex(this); }
		//if (empty(partner_id)) { }
		

		
		// Relationship.
		//if (empty(interestSimilarity)) { }
		//if (empty(relationshipStrength)) { }

		// Culture.
		if (ValidationTools.empty(raceIndex)) { AttributeAssigner.assignRace(this); }
		if (ValidationTools.empty(religionIndex)) { AttributeAssigner.assignReligion(this); }
		if (ValidationTools.empty(nationality)) { AttributeAssigner.assignNationality(this); }
		
		// Personality.
		if (ValidationTools.empty(personality)) { AttributeAssigner.assignPersonality(this); }
		
		// Extra Traits/Attributes.
		if (ValidationTools.empty(intelligence)) { AttributeAssigner.assignIntelligence(this); }
		if (ValidationTools.empty(athleticism)) { AttributeAssigner.assignAthleticism(this); }
		
		
		
		// Career and Education.
		if (ValidationTools.empty(careerPathID)) { AttributeAssigner.assignCareer(this); }
		if (ValidationTools.empty(income)) { AttributeAssigner.assignIncome(this); }
		if (ValidationTools.empty(education)) { AttributeAssigner.assignEducation(this); }
		
		// History.
		if (ValidationTools.empty(hometownHistory)) { AttributeAssigner.assignHometownHistory_1(this); }
		if (ValidationTools.empty(schoolHistory)) { AttributeAssigner.assignSchoolHistory(this); }
		if (ValidationTools.empty(workHistory)) { AttributeAssigner.assignWorkHistory(this); }
		
		// Family.
		if (ValidationTools.empty(family_id)) { }
		if (ValidationTools.empty(parent_ids)) { parent_ids = new ArrayList(); }
		if (ValidationTools.empty(children_ids)) { children_ids = new ArrayList(); }
		if (ValidationTools.empty(siblings_ids)) { siblings_ids = new ArrayList(); }

		// Clubs.
		if (ValidationTools.empty(club_ids)) { club_ids = new ArrayList(); }

		// Groups.
		if (ValidationTools.empty(group_ids)) { group_ids = new ArrayList(); }
		
		// Friendship and Friendship Determination.
		if (ValidationTools.empty(friend_prob_ids)) { friend_prob_ids = new ArrayList(); }
		if (ValidationTools.empty(friend_probabilities)) { friend_probabilities = new ArrayList(); }
		if (ValidationTools.empty(friends)) { friends = new ArrayList(); }
		
		
		// Interests.
		if (ValidationTools.empty(interests)) { AttributeAssigner.assignInterests(this); }
		if (ValidationTools.empty(interestWeights)) { AttributeAssigner.assignInterestWeights(this); }
	}
	
	
	
	*/
	
	
	
	
	
}
