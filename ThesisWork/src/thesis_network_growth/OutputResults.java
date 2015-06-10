package thesis_network_growth;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import thesis_network_growth.m1_dyadic.Dyadic_NetworkGenerator;

import static thesis_network_growth.ArtificialSociety.AllPersons;

public class OutputResults {

	
	public static void writePopulationToFile (ArrayList persons, String nodeFileName, String edgeFileName) {
		
		try {
			// ----------------------------------------------------------------------------
			// Write person to file (nodes).
			// ----------------------------------------------------------------------------
			FileWriter writer = new FileWriter(nodeFileName);
			PrintWriter printer = new PrintWriter(writer);
			int i;
			Person person;
			
			// First print out a header with column descriptions.
			printer.println("PersonID" + " " + "Sex" + " " + "Race" + " " + "Religion" + " " + "Extroversion" + " " + "Career" + " " + "Groups");

			for (i = 0; i < persons.size(); i++) {
				person = (Person)persons.get(i);

				printer.println(person.getID() + " " + person.getSex() + " " + person.getRace() + " " + person.getReligion() + " " + person.getPersonality()[0] + " " + person.getCareerTitle() + " " + person.getGroupsString());
			} // end for i (loop through all persons)
			
			// Close file.
			printer.close();


			// ----------------------------------------------------------------------------
			// Write friendships to file (edges).
			// ----------------------------------------------------------------------------
			writer = new FileWriter(edgeFileName);
			printer = new PrintWriter(writer);
			int c;
			ArrayList friends;
			int f_id;
			Person friend;
			Friendship friendship;
			
			// First print out a header with column descriptions.
			printer.println("PersonID" + " " + "FriendID" + " " + "FriendshipType" + " " + "FriendshipDescription");
			
			for (i = 0; i < persons.size(); i++) {
				person = (Person)persons.get(i);
				friends = person.getFriends();
				
				for (c = 0; c < friends.size(); c++) {

					friendship = (Friendship)friends.get(c);
					
					f_id = friendship.getFriendID();
					
					//friend = (Person)AllPersons.get(f_id);
					friend = ArtificialSociety.getPersonByID(f_id);

					
					// Check if the friend is the person's spouse, and if this friendship is their romantic relationship.
					// This essentially will print out romantic friendships with that specific role, and all non-romantic relationships.
					// It omits printing out the non-romantic-role-friendships between two people in a couple.
					if (person.getPartnerID() == friend.getID()) {
						if (friendship.getFriendType() == 1) {
							printer.println(person.getID() + " " + friend.getID() + " " + friendship.getFriendType() + " " + friendship.getFriendDescription());
						} // end if (check if this particular relationship between the couple is their romantic relationship)
					} else {
						printer.println(person.getID() + " " + friend.getID() + " " + friendship.getFriendType() + " " + friendship.getFriendDescription());
					} // end if (check whether or not the two persons are romantically involved together)
					
					// Write out the person's ID, the friend's ID, and an indicator of the relationship type (i.e. family, friend, etc.)
					//printer.println(person.getID() + " " + friend.getID() + " " + friendship.getFriendType() + " " + friendship.getFriendDescription());
					//printer.println(person.getID() + " " + friend.getID() + " " + friendship.getFriendType() + " " + friendship.getFriendDescription());

				} // end for c (loop through friend connections of person)

			} // end for i (loop through all persons)

			// Close file.
			printer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	} // end writePopulationToFile()
	
	
	
	public static void writePopulationToFile (ArrayList persons, String nodeFileName, ArrayList connections, String edgeFileName) {
		
		try {
			// ----------------------------------------------------------------------------
			// Write person to file (nodes).
			// ----------------------------------------------------------------------------
			FileWriter writer = new FileWriter(nodeFileName);
			PrintWriter printer = new PrintWriter(writer);
			int i;
			Person person;
			for (i = 0; i < persons.size(); i++) {
				person = (Person)persons.get(i);
				printer.println(person.getID());
			} // end for i (loop through all persons)
			
			// Close file.
			printer.close();


			// ----------------------------------------------------------------------------
			// Write friendships to file (edges).
			// ----------------------------------------------------------------------------
			writer = new FileWriter(edgeFileName);
			printer = new PrintWriter(writer);
			ArrayList conn;
			Integer p_id;
			int[] edges;
			int c;
			for (i = 0; i < connections.size(); i++) {
				conn = (ArrayList)connections.get(i);
				p_id = (Integer)conn.get(0);				// Person ID
				edges = (int[])conn.get(1);									// Person connections

				
				for (c = 0; c < edges.length; c++) {
					printer.println(p_id.intValue() + " " + edges[c]);
				} // end for c (loop through all connections from person i)
				
				
				
			} // end for i (loop through all persons)
			
			// Close file.
			printer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	} // end writePopulationToFile()
	
} // end OutputResults class
