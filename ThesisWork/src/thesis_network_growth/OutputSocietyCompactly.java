package thesis_network_growth;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import thesis_network_growth.m1_dyadic.Dyadic_NetworkGenerator;

import static thesis_network_growth.ArtificialSociety.AllPersons;

public class OutputSocietyCompactly {

	public static void WriteSocietyToBinary (String filename) {
		
		// Attempt to open/create file.
		DataOutputStream outStream = openStream(filename);
		
		// If stream was not successfully opened/created, then exit function now.
		if (outStream == null) return;
		
		// At this point, the file has been successfully created, so begin writing data to the file now.
		try {
			writeToFile(outStream, AllPersons);
		} catch (IOException e) {
			System.out.println("In OutputSocietyCompactly->WriteSocietyToBinary(); there was an error writing to the file.");
		}
		
		// Close file after writing to it.
		closeStream(outStream);
		
	} // end WriteSocietyToBinary()
	
	private static void writeToFile (DataOutputStream stream, ArrayList<Person> population) throws IOException {
		
		// --------------------------------------------------------------------------
		// Write out header information - general society information.
		// --------------------------------------------------------------------------

		stream.writeInt( population.size() );
		stream.writeUTF( Configuration.SocietyName );
		stream.writeInt( Configuration.SocietyYear );
		
		
		// --------------------------------------------------------------------------
		// Write out each person's entire set of information.
		// --------------------------------------------------------------------------
		int p;
		int i;
		Person person;
		double[] interests;
		double[] interestWeights;
		double[] personality;
		ActivityArchive archive;
		int archiveSize;
		ArrayList tmpArrayList;
		ArrayList<Integer> tmpArrayListInt;
		ArrayList<Double> tmpArrayListDbl;
		ArrayList<String> tmpArrayListStr;
		ArrayList<ArrayList<String>> tmpArrayListNestStr;
		String[] tmpStrArray;
		int[] tmpIntArray;
		Friendship friendship;
		
		for (p = 0; p < population.size(); p++) {
			// Get person p.
			person = (Person)population.get(p);
			
			
			// BASIC.
				stream.writeInt( person.getID() );								// Person ID
				stream.writeInt( person.getSex() );								// Person sex
				stream.writeInt( person.getAge() );								// Person age
				stream.writeInt( person.getYearBorn() );						// Person birth year

			// LIFE EXPECTANCY.
				stream.writeInt( person.getExpectedDeathYear() );				// Person death year

			// CULTURE.
				stream.writeInt( person.getRace() );							// Person race
				stream.writeInt( person.getReligion() );						// Person religion
				stream.writeUTF( person.getNationality() );						// Person nationality
				stream.writeUTF( person.getTempleAttending() );					// Person temple.
			
			// INTERESTS.
				interests = person.getInterests();
				stream.writeInt( interests.length );							// Interest number
				for (i = 0; i < interests.length; i++) {
					stream.writeDouble( interests[i] );							// Interest values (loop)
				} // end for i (write out all interests)
				interestWeights = person.getInterestWeights();
				stream.writeInt( interestWeights.length );						// Interest weight number
				for (i = 0; i < interestWeights.length; i++) {
					stream.writeDouble( interestWeights[i] );					// Interest weight values (loop)
				} // end for i (write out all interests)

			// RELATIONSHIP.
				stream.writeInt( person.getRelationshipStatus() );				// Relationship type
				stream.writeInt( person.getPartnerID() );						// Relationship partner ID
				stream.writeDouble( person.getInterestSimilarity() );			// Relationship I.S.
				stream.writeDouble( person.getRelationshipStrength() );			// Relationship strength
				stream.writeInt( person.getRelationshipStartYear() );			// Relationship starting year
				
			// PERSONALITY.
				personality = person.getPersonality();
				stream.writeInt( personality.length );							// Person personality number
				for (i = 0; i < personality.length; i++) {
					stream.writeDouble( personality[i] );						// Person personality trait values (loop)
				} // end for i (write out all personality trait values)
				stream.writeDouble( person.getIntelligence() );					// Person personality intelligence
				stream.writeDouble( person.getAthleticism() );					// Person personality athleticism

			// CAREER.
				stream.writeUTF( person.getCareer() );							// Person career
				stream.writeUTF( person.getCurrentPosition() );					// Person current position
				stream.writeInt( person.getIncome() );							// Person income
				stream.writeUTF( person.getEducation() );						// Person education
				stream.writeInt( person.getPSEducationYears() );				// Person number of PS years
				stream.writeBoolean( person.getIsInSchool() );					// Person is in school currently
				stream.writeInt( person.getPSStartYear() );						// Person starting PS year
				stream.writeInt( person.getPSFinishYear() );					// Person finishing PS year
				
			// HISTORY.
				// Don't need hometownCheckpoints (???)

				// Hometown History.
				archive = person.getHometownHistory();
				if (archive == null) {
					stream.writeInt( 0 );											// Person number of hometown history elements (ZERO!)
				} else {
					archiveSize = archive.size();
					stream.writeInt( archiveSize );									// Person number of hometown history elements
					for (i = 0; i < archiveSize; i++) {
						tmpArrayList = archive.getDictEntryNameAndYears(i);
						stream.writeUTF( (String)tmpArrayList.get(0) );				// Person hometown history location (loop)
						tmpIntArray = (int[])tmpArrayList.get(1);
						stream.writeInt( tmpIntArray[0] );							// Person hometown history start year (loop)
						stream.writeInt( tmpIntArray[1] );							// Person hometown history end year (loop)
					} // end for i (write out all hometown archive elements)
				} // end if (check if archive has been initialized)

				// School History.
				archive = person.getSchoolHistory();
				if (archive == null) {
					stream.writeInt( 0 );											// Person number of school history elements (ZERO!)
				} else {
					archiveSize = archive.size();
					stream.writeInt( archiveSize );									// Person number of school history elements
					for (i = 0; i < archiveSize; i++) {
						tmpArrayList = archive.getDictEntryNameAndYears(i);
						tmpStrArray = (String[])tmpArrayList.get(0);
						stream.writeUTF( tmpStrArray[0] );							// Person school history education type (loop)
						stream.writeUTF( tmpStrArray[1] );							// Person school history institution (loop)
						tmpIntArray = (int[])tmpArrayList.get(1);
						stream.writeInt( tmpIntArray[0] );							// Person school history start year (loop)
						stream.writeInt( tmpIntArray[1] );							// Person school history end year (loop)
					} // end for i (write out all hometown archive elements)
				} // end if (check if archive has been initialized)
				
				// Work History.
				archive = person.getWorkHistory();
				if (archive == null) {
					stream.writeInt( 0 );											// Person number of work history elements (ZERO!)
				} else {
					archiveSize = archive.size();
					stream.writeInt( archiveSize );									// Person number of work history elements
					for (i = 0; i < archiveSize; i++) {
						tmpArrayList = archive.getDictEntryNameAndYears(i);
						tmpStrArray = (String[])tmpArrayList.get(0);
						stream.writeUTF( tmpStrArray[0] );							// Person work history workplace ID (loop)
						stream.writeUTF( tmpStrArray[1] );							// Person work history career ID  (loop)
						tmpIntArray = (int[])tmpArrayList.get(1);
						stream.writeInt( tmpIntArray[0] );							// Person work history start year (loop)
						stream.writeInt( tmpIntArray[1] );							// Person work history end year (loop)
					} // end for i (write out all hometown archive elements)
				} // end if (check if archive has been initialized)
		
			// SOCIETAL HISTORY.
				// Hometown History.
				archive = person.getSocietalHometownHistory();
				if (archive == null) {
					stream.writeInt( 0 );										// Person number of local hometown history elements (ZERO!)
				} else {
					archiveSize = archive.size();
					stream.writeInt( archiveSize );									// Person number of societal hometown history elements
					for (i = 0; i < archiveSize; i++) {
						tmpArrayList = archive.getDictEntryNameAndYears(i);
						stream.writeUTF( (String)tmpArrayList.get(0) );				// Person societal hometown history location (loop)
						tmpIntArray = (int[])tmpArrayList.get(1);
						stream.writeInt( tmpIntArray[0] );							// Person societal hometown history start year (loop)
						stream.writeInt( tmpIntArray[1] );							// Person societal hometown history end year (loop)
					} // end for i (write out all hometown archive elements)
				} // end if (check if archive has been initialized)

				// School History.
				archive = person.getSocietalSchoolHistory();
				if (archive == null) {
					stream.writeInt( 0 );										// Person number of local school history elements (ZERO!)
				} else {
					archiveSize = archive.size();
					stream.writeInt( archiveSize );									// Person societal number of school history elements
					for (i = 0; i < archiveSize; i++) {
						tmpArrayList = archive.getDictEntryNameAndYears(i);
						tmpStrArray = (String[])tmpArrayList.get(0);
						stream.writeUTF( tmpStrArray[0] );							// Person societal school history education type (loop)
						stream.writeUTF( tmpStrArray[1] );							// Person societal school history institution (loop)
						tmpIntArray = (int[])tmpArrayList.get(1);
						stream.writeInt( tmpIntArray[0] );							// Person societal school history start year (loop)
						stream.writeInt( tmpIntArray[1] );							// Person societal school history end year (loop)
					} // end for i (write out all hometown archive elements)
				} // end if (check if archive has been initialized)

				// Work History.
				archive = person.getSocietalWorkHistory();
				if (archive == null) {
					stream.writeInt( 0 );										// Person number of local work history elements (ZERO!)
				} else {
					archiveSize = archive.size();
					stream.writeInt( archiveSize );									// Person societal number of work history elements
					for (i = 0; i < archiveSize; i++) {
						tmpArrayList = archive.getDictEntryNameAndYears(i);
						tmpStrArray = (String[])tmpArrayList.get(0);
						stream.writeUTF( tmpStrArray[0] );							// Person societal work history workplace ID (loop)
						stream.writeUTF( tmpStrArray[1] );							// Person societal work history career ID  (loop)
						tmpIntArray = (int[])tmpArrayList.get(1);
						stream.writeInt( tmpIntArray[0] );							// Person societal work history start year (loop)
						stream.writeInt( tmpIntArray[1] );							// Person societal work history end year (loop)
					} // end for i (write out all hometown archive elements)
				} // end if (check if archive has been initialized)

			// FAMILY.
				stream.writeInt( person.getFamilyID() );						// Person family ID
				tmpArrayListInt = person.getParentIDs();
				stream.writeInt( tmpArrayListInt.size() );						// Person number of parents
				for (i = 0; i < tmpArrayListInt.size(); i++) {
					stream.writeInt( tmpArrayListInt.get(i) );					// Person parent ID (loop)
				} // end for i (write out all parent IDs)
				tmpArrayListInt = person.getChildrenIDs();
				stream.writeInt( tmpArrayListInt.size() );						// Person number of children
				for (i = 0; i < tmpArrayListInt.size(); i++) {
					stream.writeInt( tmpArrayListInt.get(i) );					// Person child ID (loop)
				} // end for i (write out all children IDs)
				tmpArrayListInt = person.getSiblingIDs();
				stream.writeInt( tmpArrayListInt.size() );						// Person number of siblings
				for (i = 0; i < tmpArrayListInt.size(); i++) {
					stream.writeInt( tmpArrayListInt.get(i) );					// Person sibling ID (loop)
				} // end for i (write out all sibling IDs)

			// CLUBS.
				tmpArrayListInt = person.getClubIDs();
				stream.writeInt( tmpArrayListInt.size() );						// Person number of clubs 
				for (i = 0; i < tmpArrayListInt.size(); i++) {
					stream.writeInt( tmpArrayListInt.get(i) );					// Person's club ID (loop)
				} // end for i (write out all club IDs)

			// GROUPS.
				tmpArrayListNestStr = person.getGroupIDs();
				stream.writeInt( tmpArrayListNestStr.size() );						// Person number of groups
				for (i = 0; i < tmpArrayListNestStr.size(); i++) {
					tmpArrayListStr = (ArrayList<String>)tmpArrayListNestStr.get(i);
					stream.writeUTF( tmpArrayListStr.get(0) );					// Person group ID (loop)
					stream.writeUTF( tmpArrayListStr.get(1) );					// Person group role (loop)
				} // end for i (write out all group IDs)

			// FRIENDSHIP DETERMINATION.
				tmpArrayListInt = person.getFriendProbIDs();
				tmpArrayListDbl = person.getFriendProbabilities();
				tmpArrayListStr = person.getFriendProbRoles();
				stream.writeInt( tmpArrayListInt.size() );						// Person number of potential friendships
				for (i = 0; i < tmpArrayListInt.size(); i++) {
					stream.writeInt( tmpArrayListInt.get(i) );					// Person possible friendship ID (loop)
				} // end for i (write out all friendship prob IDs)
				// No need to write number of friendship probabilities, since it MUST be equal to that of friendship prob ids.
				for (i = 0; i < tmpArrayListDbl.size(); i++) {
					stream.writeDouble( tmpArrayListDbl.get(i) );				// Person possible friendship probability (loop)
				} // end for i (write out all friendship probabilities)
				for (i = 0; i < tmpArrayListStr.size(); i++) {
					stream.writeUTF( tmpArrayListStr.get(i) );				// Person possible friendship probability (loop)
				} // end for i (write out all friendship prob roles)

			// FRIENDS.
				tmpArrayList = person.getFriends();
				stream.writeInt( tmpArrayList.size() );							// Person number of friends
				for (i = 0; i < tmpArrayList.size(); i++) {
					friendship = (Friendship)tmpArrayList.get(i);
					// No need to record person's ID here from friendship, since it is the ID written at the beginning.
					stream.writeInt( friendship.getFriendID() );				// Person friend ID (loop)
					stream.writeInt( friendship.getFriendType() );				// Person friend type (loop)
					stream.writeDouble( friendship.getFriendStrength() );		// Person friend strength (loop)
					stream.writeUTF( friendship.getFriendDescription() );		// Person friend description (loop)
				} // end for i (write out all friend IDs)
				
				
		} // end for p (loop through all people to write to file)
		

	} // end writeToFile()
	
	
	private static DataOutputStream openStream (String filename) {
		DataOutputStream stream = null;
		try {
			stream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));
		} catch (FileNotFoundException e) {
			System.err.println("In OutputSocietyCompactly->WriteSocietyToBinary(); could not create binary file successfully.");
			e.printStackTrace();
		}
		return stream;
	} // end openStream()
	
	private static void closeStream (DataOutputStream stream) {
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} // end closeStream()
	
} // end OutputSocietyCompactly class
