package thesis_network_growth;

import java.util.ArrayList;

public class Logger {

	public static void LogRelationshipStrength () {
		
		ArrayList<Person> society = ArtificialSociety.getSociety();
		Person person;
		int numPersons = society.size();
		int i;
		
		for (i = 0; i < numPersons; i++) {
		
			//person = society.get(i); // BS_Feb18
			person = ArtificialSociety.getPersonByIndex(i); 

			if (person.getRelationshipStatus() == RelationshipCalculator.REL_TYPE_MARRIED) {
				
				if (person.getID() > person.getPartnerID()) {
					
					System.out.println(person.getID() + " " + person.getPartnerID() + " | " + person.getRelationshipStrength());
					
				} // end if (check for greater ID so each relationship is examined only once)

			} // end if (check if person is married)
			
			
			
		} // end for i (loop through all people in society)
		
		
		
	} // end LogRelationshipStrength()
	
} // end Logger class
