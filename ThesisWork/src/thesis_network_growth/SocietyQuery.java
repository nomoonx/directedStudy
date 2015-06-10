package thesis_network_growth;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

public class SocietyQuery {
	
	private static long getTime () {
		// Get the current system time in nanoseconds. This is used in computing execution time.
		return System.nanoTime();
	} // end getTime()
	
	private static long getElapsedTime (long startTime) {
		// Calculate the execution time as the difference between the current system time and the given initial system time.
		//
		// param startTime: the initial system time
		//
		// return: the difference between current time and the given initial time, measured in nanoseconds
		return (getTime() - startTime);
	} // end getElapsedTime()
	
	private static double getElapsedTime_Seconds (long startTime) {
		// Calculate the execution time (in seconds) as the difference between the current system time and the given initial system time.
		//
		// param startTime: the initial system time
		//
		// return: the difference between current time and the given initial time, converted in seconds
		return (getTime() - startTime) / 1000000000.0;
	} // end getElapsedTime()
	
	
	
	public static double executeQuery1_AddPerson (int numQueryTrials) {

		int i;

		// Get initial time.
		long sTime = getTime();
		
		// Query.
		for (i = 0; i < numQueryTrials; i++) {
			Query1_AddPerson();
		} // end for i (run query a number of times)

		// Get total elapsed time.
		double totalTime = getElapsedTime_Seconds(sTime);

		return totalTime;

	} // end executeQuery1()
	
	
	
	public static double executeQuery2_AddCouple (int numQueryTrials) {

		int i;

		// Get initial time.
		long sTime = getTime();
		
		// Query.
		
		for (i = 0; i < numQueryTrials; i++) {
			Query2_AddCouple();
		} // end for i (run query a number of times)

		// Get total elapsed time.
		double totalTime = getElapsedTime_Seconds(sTime);

		return totalTime;

		
	} // end executeQuery2_AddCouple()
	
	
	public static double executeQuery3_KillPerson (int numQueryTrials) {

		int i;

		// Get initial time.
		long sTime = getTime();

		// Query.
		for (i = 0; i < numQueryTrials; i++) {
			Query3_KillPerson();
		} // end for i (run query a number of times)

		// Get total elapsed time.
		double totalTime = getElapsedTime_Seconds(sTime);

		return totalTime;

	} // end executeQuery3_KillPerson()
	
	public static double executeQuery4_GetRaceNumbers (int numQueryTrials) {

		int i;

		// Get initial time.
		long sTime = getTime();

		// Query.
		for (i = 0; i < numQueryTrials; i++) {
			Query4_GetRaceNumbers();
		} // end for i (run query a number of times)
		

		// Get total elapsed time.
		double totalTime = getElapsedTime_Seconds(sTime);

		return totalTime;

	} // end executeQuery4_GetRaceNumbers()

	public static double executeQuery5_GetTotalFriendships (int numQueryTrials) {

		int i;

		// Get initial time.
		long sTime = getTime();

		// Query.
		for (i = 0; i < numQueryTrials; i++) {
			Query5_GetTotalFriendships();
		} // end for i (run query a number of times)
		

		// Get total elapsed time.
		double totalTime = getElapsedTime_Seconds(sTime);

		return totalTime;

	} // end executeQuery5_GetTotalFriendships()
	
	public static double executeQuery6_GetTotalLocalPeople (int numQueryTrials) {
		
		int i;

		// Get initial time.
		long sTime = getTime();

		// Query.

		for (i = 0; i < numQueryTrials; i++) {
			Query6_GetTotalLocalPeople();
		} // end for i (run query a number of times)

		// Get total elapsed time.
		double totalTime = getElapsedTime_Seconds(sTime);

		return totalTime;

	} // end executeQuery6_GetTotalLocalPeople()
	
	public static double executeQuery7_GetNumberOfSingleFriends (int numQueryTrials) {

		int i;

		// Get initial time.
		long sTime = getTime();

		// Query.

		for (i = 0; i < numQueryTrials; i++) {
			Query7_GetNumberOfSingleFriends();
		} // end for i (run query a number of times)

		// Get total elapsed time.
		double totalTime = getElapsedTime_Seconds(sTime);

		return totalTime;

	} // end executeQuery7_GetNumberOfSingleFriends()
	
	
	
	
	
	
	
	
	
	// ==========================================================================================================================
	
	// ==========================================================================================================================
	
	
	
	/*
	public static double GetDegreesOfSeparation () {
		// ========================================================================
		// QUERY: Get the degree of separation between two random persons.
		// ========================================================================

		// ---------------------------------------
		// Select two random persons.
		// ---------------------------------------
		int numTotalPersons = ArtificialSociety.getSociety().size();
		int rndA = Distribution.uniform(0, numTotalPersons-1);
		int rndB = Distribution.uniform(0, numTotalPersons-1);
		Person rndPA = ArtificialSociety.getPersonByIndex(rndA);
		Person rndPB = ArtificialSociety.getPersonByIndex(rndB);
		String personA = String.valueOf(rndPA.getID());
		String personB = String.valueOf(rndPB.getID());
				
		Graph<String,String> graph = ArtificialSociety.getGraph();
		DijkstraShortestPath<String, String> d = new DijkstraShortestPath<String, String>(graph);

		if (d.getDistance(personA, personB) == null) {
			System.err.println("NULL DISTANCE FOR PERSONS " + personA + " and " + personB );
			ArtificialSociety.DisplayIndexTable();
		}
		
		return (Double)d.getDistance(personA, personB);
				
	} // end GetDegreesOfSeparation()
	*/
	
	
	
	
	
	
	
	
	
	
	public static void Query1_AddPerson () {

		// ========================================================================
		// QUERY: Create new random person.
		// ========================================================================
		Person newPerson = PersonGenerator.GeneratePerson(1, null);
		PersonGenerator.complete(newPerson);
				
	} // end Query1_AddPerson()

	public static void Query2_AddCouple () {

		// ========================================================================
		// QUERY: Create random married couple.
		// ========================================================================
		PersonGenerator.GenerateCouple(RelationshipCalculator.REL_TYPE_MARRIED);

	} // end Query2_AddCouple()

	public static void Query3_KillPerson () {

		// ========================================================================
		// QUERY: Kill off random person.
		// ========================================================================
		int rndIndex = Distribution.uniform(0, ArtificialSociety.getSociety().size()-1);
		Person rndPerson = ArtificialSociety.getPersonByIndex(rndIndex);
		rndPerson.die();

	} // end Query3_KillPerson()
	

	
	
	
	
	
	public static int[] Query4_GetRaceNumbers () {
		int numRaces = Configuration.RaceLabels.length;
		int numPersons = ArtificialSociety.getSociety().size();
		int[] raceNumbers = new int[numRaces];
		int i;
		Person person;
		
		// Initialize all race counts to 0.
		for (i = 0; i < numRaces; i++) {
			raceNumbers[i] = 0;
		} // end for i (loop  through all races)
		
		// Loop through all people in society.
		for (i = 0; i < numPersons; i++) {
			person = ArtificialSociety.getPersonByIndex(i);
			raceNumbers[person.getRace()]++;			// Increment race count from each person's race variable.
		} // end for i (loop through all persons in society)
		
		return raceNumbers;
	} // end Query4_GetRaceNumbers
	
	public static int Query5_GetTotalFriendships () {
		int totalCount = 0;
		
		int p;
		int numPersons = ArtificialSociety.getSociety().size();
		Person person;
		for (p = 0; p < numPersons; p++) {
			person = ArtificialSociety.getPersonByIndex(p);
			// Increment the totalCount of friendships with each person's total number of friends.
			totalCount += person.getFriends().size();
		} // end for p (loop through all persons in society)
		
		
		// Optionally, we could divide totalCount by 2 here at the end, since friendships are reflexive and don't need to be counted twice.
		totalCount = totalCount / 2;
		
		return totalCount;
		
	} // end Query5_GetTotalFriendships()
	
	
	public static int Query6_GetTotalLocalPeople () {
		int totalCount = 0;
		
		int p;
		int numPersons = ArtificialSociety.getSociety().size();
		Person person;
		for (p = 0; p < numPersons; p++) {
			person = ArtificialSociety.getPersonByIndex(p);
			
			if (person.getCurrentHometown().equals( Configuration.SocietyName )) {
				// Increment the totalCount if person lives locally.
				totalCount++;
			} // end if (check if person lives in local society)
		} // end for p (loop through all persons in society)

		return totalCount;
		
	} // end Query6_GetTotalLocalPeople()
	
	public static int Query7_GetNumberOfSingleFriends () {
		
		
		
		int rndIndex = Distribution.uniform(0, ArtificialSociety.getSociety().size()-1);
		Person rndPerson = ArtificialSociety.getPersonByIndex(rndIndex);
		ArrayList<Friendship> friendships = rndPerson.getFriends();
		Friendship friendship;
		
		int f;
		Person friend;
		
		int totalCount = 0;

		for (f = 0; f < friendships.size(); f++) {
			friendship = friendships.get(f);
			
			friend = ArtificialSociety.getPersonByID( friendship.getFriendID() );
			
			if (friend.getRelationshipStatus() == RelationshipCalculator.REL_TYPE_SINGLE) {
				totalCount++;
			} // end if (check if friend is single)
			
			
		} // end for f (loop through all rndPerson's friends)

		return totalCount;
		
	} // end Query7_GetNumberOfSingleFriends()
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	public static String getIndexOfMapElementX (Map<String,String> map, String element) {
		int i;
		
		//System.out.println(map.values());
		//System.out.println(new ArrayList<String>(map.values()));
		ArrayList<String> keys = new ArrayList<String>(map.keySet());
		ArrayList<String> vals = new ArrayList<String>(map.values());
		
		for (i = 0; i < map.size(); i++) {
			if (vals.get(i) != null && vals.get(i).equals(element)) {
				return keys.get(i);
			}
		}
		
		// If not found or null, then return the 0th element.
		return keys.get(0);
	} // end getIndexOfMapElement()
	*/
	
} // end SocietyQuery class