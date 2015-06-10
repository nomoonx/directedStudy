package thesis_network_growth;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;






import static thesis_network_growth.ArtificialSociety.AllPersons;

public class InputSocietyCompactly {

	/*
	public static void ReadSocietyFromBinary (String filename) {
		
		// Attempt to open/create file.
		DataInputStream inStream = openStream(filename);
		
		// If stream was not successfully opened/created, then exit function now.
		if (inStream == null) return;
		
		// At this point, the file has been successfully created, so begin writing data to the file now.
		try {
			readFromFile(inStream);
			//readFromFile();
		} catch (IOException e) {
			System.out.println("In InputSocietyCompactly->ReadSocietyFromBinary(); there was an error reading from the file.");
		}
		
		// Close file after reading from it.
		closeStream(inStream);
		
	} // end WriteSocietyToBinary()
	*/

	
	public static void readFromFileEfficiently(String filename) {
		try {
	        BufferedInputStream in = new BufferedInputStream(new FileInputStream(filename));
	        //int bufferSize = (int) Math.pow(2, 25); // Set buffer size. I use 2^25 to ensure that even large societies will be read in all at once.
	        int bufferSize = (int) Math.pow(2, 28); // Set buffer size. I use 2^25 to ensure that even large societies will be read in all at once.
	        byte[] ioBuf = new byte[bufferSize];       
	        int bytesRead;
	        while ((bytesRead = in.read(ioBuf)) != -1){
	            processInputData(ioBuf);
	        } // end while (loop through file contents one buffer at a time {ideally just in one buffer though!})

	        in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	} // end readFromFileNEW()
	
	
	public static void processInputData (byte[] arr) throws IOException {

		DataInputStream stream = new DataInputStream( new ByteArrayInputStream(arr) );
		
		// --------------------------------------------------------------------------
		// Read out header information - general society information.
		// --------------------------------------------------------------------------

		int populationSize = stream.readInt();
		String SocietyName = stream.readUTF();
		int SocietyYear = stream.readInt();
		
		
		Configuration.N_Population_Size = populationSize;
		Configuration.SocietyName = SocietyName;
		Configuration.SocietyYear = SocietyYear;
		

		// --------------------------------------------------------------------------
		// Read out each person's entire set of information.
		// --------------------------------------------------------------------------
		int p;
		int i;
		Person person;
		double[] interests;
		double[] interestWeights;
		double[] personality;
		int archiveSize;
		Friendship friendship;
		String tmpStr;
		String tmpStr2;
		int tmpSYear;
		int tmpEYear;
		int tmpInt;
		int tmpInt2;
		double tmpDbl;
		int personID;
		int numInterests;
		int numInterestWeights;
		int numPersonalityTraits;

		ActivityArchive hometownArchive;
		ActivityArchive schoolArchive;
		ActivityArchive workArchive;
		ActivityArchive socHometownArchive;
		ActivityArchive socSchoolArchive;
		ActivityArchive socWorkArchive;

		int numParents;
		ArrayList<Integer> parents;
		int numChildren;
		ArrayList<Integer> children;
		int numSiblings;
		ArrayList<Integer> siblings;
		int numClubs;
		ArrayList<Integer> clubs;
		int numGroups;
		ArrayList<ArrayList<String>> groups;
		ArrayList<String> grp;
		int numFriendDeterm;
		ArrayList<Integer> friendDetermIDs;
		ArrayList<Double> friendDetermProbs;
		ArrayList<String> friendDetermProbRoles;
		int numFriends;

		ArrayList<Friendship> friends;
		
		
		
		
		for (p = 0; p < populationSize; p++) {
			// Get person p.
			person = new Person();					// Create new person.
			
			
			// BASIC.
				personID = stream.readInt();
				person.setID( personID );										// Person ID
				person.setSex( stream.readInt() );								// Person sex
				person.setAge( stream.readInt() );								// Person age
				person.setYearBorn( stream.readInt() );							// Person birth year

			// LIFE EXPECTANCY.
				person.setExpectedDeathYear( stream.readInt() );				// Person death year

			// CULTURE.
				person.setRace( stream.readInt() );								// Person race
				person.setReligion( stream.readInt() );							// Person religion
				person.setNationality( stream.readUTF() );						// Person nationality
				person.setTempleAttending( stream.readUTF() );					// Person temple
			
			// INTERESTS.
				//interests = person.getInterests();
				numInterests = stream.readInt();							// Interest number
				interests = new double[numInterests];
				for (i = 0; i < interests.length; i++) {
					//stream.readDouble( interests[i] );
					interests[i] = stream.readDouble();							// Interest values (loop)
				} // end for i (read out all interests)
				person.setInterests(interests);
				//interestWeights = person.getInterestWeights();
				numInterestWeights = stream.readInt();						// Interest weight number
				interestWeights = new double[numInterestWeights];
				for (i = 0; i < numInterestWeights; i++) {
					interestWeights[i] = stream.readDouble();					// Interest weight values (loop)
				} // end for i (read out all interests)
				person.setInterestWeights(interestWeights);

			// RELATIONSHIP.
				person.setRelationshipStatus( stream.readInt() );				// Relationship type
				person.setPartnerID( stream.readInt() );						// Relationship partner ID
				person.setInterestSimilarity( stream.readDouble() );			// Relationship I.S.
				person.setRelationshipStrength( stream.readDouble() );			// Relationship strength
				person.setRelationshipStartYear( stream.readInt() );			// Relationship starting year
				
			// PERSONALITY.
				//personality = person.getPersonality();
				numPersonalityTraits = stream.readInt();					// Person personality number
				personality = new double[numPersonalityTraits];
				//stream.readInt( personality.length );
				for (i = 0; i < numPersonalityTraits; i++) {
					personality[i] = stream.readDouble();						// Person personality trait values (loop)
				} // end for i (read out all personality trait values)
				person.setPersonality(personality);
				person.setIntelligence( stream.readDouble() );					// Person personality intelligence
				person.setAthleticism( stream.readDouble() );					// Person personality athleticism

			// CAREER.
				person.setCareer( stream.readUTF() );							// Person career
				person.setCurrentPosition( stream.readUTF() );					// Person current position
				person.setIncome( stream.readInt() );							// Person income
				person.setEducation( stream.readUTF() );						// Person education
				person.setPSEducationYears( stream.readInt() );					// Person number of PS years
				person.setIsInSchool( stream.readBoolean() );					// Person is in school currently
				person.setPSStartYear( stream.readInt() );						// Person starting PS year
				person.setPSFinishYear( stream.readInt() );						// Person finishing PS year
				
			// HISTORY.
				// Don't need hometownCheckpoints (???)

				// Hometown History.
				archiveSize = stream.readInt();									// Person number of hometown history elements
				hometownArchive = new ActivityArchive();
				for (i = 0; i < archiveSize; i++) {
					//tmpArrayList = archive.getDictEntryNameAndYears(i);
					tmpStr = stream.readUTF();									// Person hometown history location (loop)
					//tmpIntArray = (int[])tmpArrayList.get(1);
					tmpSYear = stream.readInt();								// Person hometown history start year (loop)
					tmpEYear = stream.readInt();								// Person hometown history end year (loop)
					hometownArchive.addEntry(tmpStr, tmpSYear, tmpEYear);
				} // end for i (read out all hometown archive elements)
				person.setHometownHistory(hometownArchive);
				
				// School History.
				archiveSize = stream.readInt();									// Person number of school history elements
				schoolArchive = new ActivityArchive();
				for (i = 0; i < archiveSize; i++) {
					//tmpArrayList = archive.getDictEntryNameAndYears(i);
					//tmpStrArray = (String[])tmpArrayList.get(0);
					tmpStr = stream.readUTF();									// Person school history education type (loop)
					tmpStr2 = stream.readUTF();									// Person school history institution (loop)
					//tmpIntArray = (int[])tmpArrayList.get(1);
					tmpSYear = stream.readInt();								// Person school history start year (loop)
					tmpEYear = stream.readInt();								// Person school history end year (loop)
					schoolArchive.addEntry(new String[] {tmpStr, tmpStr2}, tmpSYear, tmpEYear);
				} // end for i (read out all hometown archive elements)
				person.setSchoolHistory(schoolArchive);
				
				// Work History.
				archiveSize = stream.readInt();									// Person number of work history elements
				workArchive = new ActivityArchive();
				for (i = 0; i < archiveSize; i++) {
					//tmpArrayList = archive.getDictEntryNameAndYears(i);
					//tmpStrArray = (String[])tmpArrayList.get(0);
					tmpStr = stream.readUTF();									// Person work history workplace ID (loop)
					tmpStr2 = stream.readUTF();									// Person work history career ID  (loop)
					//tmpIntArray = (int[])tmpArrayList.get(1);
					tmpSYear = stream.readInt();								// Person work history start year (loop)
					tmpEYear = stream.readInt();								// Person work history end year (loop)
					workArchive.addEntry(new String[] {tmpStr, tmpStr2}, tmpSYear, tmpEYear);
				} // end for i (read out all hometown archive elements)
				person.setWorkHistory(workArchive);

			// SOCIETAL HISTORY.
				// Hometown History.
				archiveSize = stream.readInt();									// Person number of societal hometown history elements
				socHometownArchive = new ActivityArchive();
				for (i = 0; i < archiveSize; i++) {
					//tmpArrayList = archive.getDictEntryNameAndYears(i);
					tmpStr = stream.readUTF();									// Person societal hometown history location (loop)
					//tmpIntArray = (int[])tmpArrayList.get(1);
					tmpSYear = stream.readInt();								// Person societal hometown history start year (loop)
					tmpEYear = stream.readInt();								// Person societal hometown history end year (loop)
					socHometownArchive.addEntry(tmpStr, tmpSYear, tmpEYear);
				} // end for i (read out all hometown archive elements)
				person.setSocietalHometownHistory(socHometownArchive);
				
				// School History.
				archiveSize = stream.readInt();									// Person societal number of school history elements
				socSchoolArchive = new ActivityArchive();
				for (i = 0; i < archiveSize; i++) {
					//tmpArrayList = archive.getDictEntryNameAndYears(i);
					//tmpStrArray = (String[])tmpArrayList.get(0);
					tmpStr = stream.readUTF();									// Person societal school history education type (loop)
					tmpStr2 = stream.readUTF();									// Person societal school history institution (loop)
					//tmpIntArray = (int[])tmpArrayList.get(1);
					tmpSYear = stream.readInt();								// Person societal school history start year (loop)
					tmpEYear = stream.readInt();								// Person societal school history end year (loop)
					socSchoolArchive.addEntry(new String[] {tmpStr, tmpStr2}, tmpSYear, tmpEYear);
				} // end for i (read out all hometown archive elements)
				person.setSocietalSchoolHistory(socSchoolArchive);
				
				// Work History.
				archiveSize = stream.readInt();									// Person societal number of work history elements
				socWorkArchive = new ActivityArchive();
				for (i = 0; i < archiveSize; i++) {
					//tmpArrayList = archive.getDictEntryNameAndYears(i);
					//tmpStrArray = (String[])tmpArrayList.get(0);
					tmpStr = stream.readUTF();									// Person societal work history workplace ID (loop)
					tmpStr2 = stream.readUTF();									// Person societal work history career ID  (loop)
					//tmpIntArray = (int[])tmpArrayList.get(1);
					tmpSYear = stream.readInt();								// Person societal work history start year (loop)
					tmpEYear = stream.readInt();								// Person societal work history end year (loop)
					socWorkArchive.addEntry(new String[] {tmpStr, tmpStr2}, tmpSYear, tmpEYear);
				} // end for i (read out all hometown archive elements)
				person.setSocietalWorkHistory(socWorkArchive);
				
			// FAMILY.
				person.setFamilyID( stream.readInt() );							// Person family ID
				//tmpArrayListInt = person.getParentIDs();
				numParents = stream.readInt();								// Person number of parents
				parents = new ArrayList<Integer>();
				for (i = 0; i < numParents; i++) {
					parents.add( stream.readInt() );							// Person parent ID (loop)
				} // end for i (read out all parent IDs)
				person.setParentIDs(parents);
				numChildren = stream.readInt();								// Person number of children
				children = new ArrayList<Integer>();
				for (i = 0; i < numChildren; i++) {
					children.add(stream.readInt());								// Person child ID (loop)
				} // end for i (read out all children IDs)
				person.setChildrenIDs(children);
				numSiblings = stream.readInt();								// Person number of siblings
				siblings = new ArrayList<Integer>();
				for (i = 0; i < numSiblings; i++) {
					siblings.add(stream.readInt());								// Person sibling ID (loop)
				} // end for i (read out all sibling IDs)
				person.setSiblingIDs(siblings);

			// CLUBS.
				//tmpArrayListInt = person.getClubIDs();
				numClubs = stream.readInt();								// Person number of clubs
				clubs = new ArrayList<Integer>();
				for (i = 0; i < numClubs; i++) {
					clubs.add(stream.readInt());								// Person's club ID (loop)
				} // end for i (read out all club IDs)
				person.setClubIDs(clubs);

			// GROUPS.
				numGroups = stream.readInt();								// Person number of groups
				groups = new ArrayList<ArrayList<String>>();
				grp = new ArrayList<String>();
				for (i = 0; i < numGroups; i++) {
					grp = new ArrayList<String>();
					grp.add(stream.readUTF());									// Person group ID (loop)
					grp.add(tmpStr2 = stream.readUTF());						// Person group role (loop)
					groups.add(grp);
				} // end for i (read out all group IDs)
				person.setGroupIDs(groups);

			// FRIENDSHIP DETERMINATION.
				//tmpArrayListInt = person.getFriendProbIDs();
				//tmpArrayListDbl = person.getFriendProbabilities();
				numFriendDeterm = stream.readInt();							// Person number of potential friendships
				friendDetermIDs = new ArrayList<Integer>();
				friendDetermProbs = new ArrayList<Double>();
				friendDetermProbRoles = new ArrayList<String>();
				for (i = 0; i < numFriendDeterm; i++) {
					friendDetermIDs.add(stream.readInt());						// Person possible friendship ID (loop)
				} // end for i (read out all friendship prob IDs)
				// No need to read number of friendship probabilities, since it MUST be equal to that of friendship prob ids.
				for (i = 0; i < numFriendDeterm; i++) {
					friendDetermProbs.add(stream.readDouble());					// Person possible friendship probability (loop)
				} // end for i (read out all friendship probabilities)
				for (i = 0; i < numFriendDeterm; i++) {
					friendDetermProbRoles.add(stream.readUTF());					// Person possible friendship probability (loop)
				} // end for i (read out all friendship probabilities)
				person.setFriendProbIDs(friendDetermIDs);
				person.setFriendProbabilities(friendDetermProbs);
				person.setFriendProbRoles(friendDetermProbRoles);
				
			// FRIENDS.
				//tmpArrayList = person.getFriends();
				numFriends = stream.readInt();								// Person number of friends
				friends = new ArrayList<Friendship>();
				for (i = 0; i < numFriends; i++) {
					//friendship = (Friendship)tmpArrayList.get(i);
					// No need to record person's ID here from friendship, since it is the ID written at the beginning.
					tmpInt = stream.readInt();									// Person friend ID (loop)
					tmpInt2 = stream.readInt();									// Person friend type (loop)
					tmpDbl = stream.readDouble();								// Person friend strength (loop)
					tmpStr = stream.readUTF();									// Person friend description (loop)
					friendship = new Friendship(personID, tmpInt, tmpInt2, tmpDbl, tmpStr);
					friends.add(friendship);
				} // end for i (read out all friend IDs)
				person.setFriends(friends);
				
				
				AllPersons.add(person);
				
		} // end for p (loop through all people to create them)
		
		
		// --------------------------------------------------
		// CLEAN-UP MEMORY.
		// --------------------------------------------------
		
		
		person = null;
		interests = null;
		interestWeights = null;
		personality = null;
		friendship = null;
		tmpStr = null;
		tmpStr2 = null;
		hometownArchive = null;
		schoolArchive = null;
		workArchive = null;
		socHometownArchive = null;
		socSchoolArchive = null;
		socWorkArchive = null;
		parents = null;
		children = null;
		siblings = null;
		clubs = null;
		groups = null;
		grp = null;
		friendDetermIDs = null;
		friendDetermProbs = null;
		friendDetermProbRoles = null;
		friends = null;
		
		
		
		
		
		
		

		
	} // end processInputData()
	
	
	

	
	
	/*
	private static void readFromFile (DataInputStream stream) throws IOException {

		
		// --------------------------------------------------------------------------
		// Read out header information - general society information.
		// --------------------------------------------------------------------------

		int populationSize = stream.readInt();
		String SocietyName = stream.readUTF();
		int SocietyYear = stream.readInt();
		
		
		Configuration.N_Population_Size = populationSize;
		Configuration.SocietyName = SocietyName;
		Configuration.SocietyYear = SocietyYear;
		
		
		//System.out.println("Read from file;");
		//System.out.println("populationSize = " + populationSize);
		//System.out.println("SocietyName = " + SocietyName);
		//System.out.println("SocietyYear = " + SocietyYear);
		
		
		// --------------------------------------------------------------------------
		// Read out each person's entire set of information.
		// --------------------------------------------------------------------------
		int p;
		int i;
		Person person;
		double[] interests;
		double[] interestWeights;
		double[] personality;
		//ActivityArchive archive;
		int archiveSize;
		//ArrayList tmpArrayList;
		//ArrayList<Integer> tmpArrayListInt;
		//ArrayList<Double> tmpArrayListDbl;
		//ArrayList<String> tmpArrayListStr;
		//ArrayList tmpArrayListx;
		//String[] tmpStrArray;
		//int[] tmpIntArray;
		Friendship friendship;
		String tmpStr;
		String tmpStr2;
		int tmpSYear;
		int tmpEYear;
		int tmpInt;
		int tmpInt2;
		double tmpDbl;
		int personID;
		int numInterests;
		int numInterestWeights;
		int numPersonalityTraits;

		ActivityArchive hometownArchive;
		ActivityArchive schoolArchive;
		ActivityArchive workArchive;
		ActivityArchive socHometownArchive;
		ActivityArchive socSchoolArchive;
		ActivityArchive socWorkArchive;

		int numParents;
		ArrayList<Integer> parents;
		int numChildren;
		ArrayList<Integer> children;
		int numSiblings;
		ArrayList<Integer> siblings;
		int numClubs;
		ArrayList<Integer> clubs;
		int numGroups;
		ArrayList<ArrayList<String>> groups;
		ArrayList<String> grp;
		int numFriendDeterm;
		ArrayList<Integer> friendDetermIDs;
		ArrayList<Double> friendDetermProbs;
		int numFriends;

		ArrayList<Friendship> friends;
		
		
		
		
		for (p = 0; p < populationSize; p++) {
			// Get person p.
			person = new Person();					// Create new person.
			
			
			// BASIC.
				personID = stream.readInt();
				person.setID( personID );										// Person ID
				person.setSex( stream.readInt() );								// Person sex
				person.setAge( stream.readInt() );								// Person age
				person.setYearBorn( stream.readInt() );							// Person birth year

			// LIFE EXPECTANCY.
				person.setExpectedDeathYear( stream.readInt() );				// Person death year

			// CULTURE.
				person.setRace( stream.readInt() );								// Person race
				person.setReligion( stream.readInt() );							// Person religion
				person.setNationality( stream.readUTF() );						// Person nationality
				person.setTempleAttending( stream.readUTF() );					// Person temple
			
			// INTERESTS.
				//interests = person.getInterests();
				numInterests = stream.readInt();							// Interest number
				interests = new double[numInterests];
				for (i = 0; i < interests.length; i++) {
					//stream.readDouble( interests[i] );
					interests[i] = stream.readDouble();							// Interest values (loop)
				} // end for i (read out all interests)
				person.setInterests(interests);
				//interestWeights = person.getInterestWeights();
				numInterestWeights = stream.readInt();						// Interest weight number
				interestWeights = new double[numInterestWeights];
				for (i = 0; i < numInterestWeights; i++) {
					interestWeights[i] = stream.readDouble();					// Interest weight values (loop)
				} // end for i (read out all interests)
				person.setInterestWeights(interestWeights);

			// RELATIONSHIP.
				person.setRelationshipStatus( stream.readInt() );				// Relationship type
				person.setPartnerID( stream.readInt() );						// Relationship partner ID
				person.setInterestSimilarity( stream.readDouble() );			// Relationship I.S.
				person.setRelationshipStrength( stream.readDouble() );			// Relationship strength
				person.setRelationshipStartYear( stream.readInt() );			// Relationship starting year
				
			// PERSONALITY.
				//personality = person.getPersonality();
				numPersonalityTraits = stream.readInt();					// Person personality number
				personality = new double[numPersonalityTraits];
				//stream.readInt( personality.length );
				for (i = 0; i < numPersonalityTraits; i++) {
					personality[i] = stream.readDouble();						// Person personality trait values (loop)
				} // end for i (read out all personality trait values)
				person.setPersonality(personality);
				person.setIntelligence( stream.readDouble() );					// Person personality intelligence
				person.setAthleticism( stream.readDouble() );					// Person personality athleticism

			// CAREER.
				person.setCareer( stream.readUTF() );							// Person career
				person.setCurrentPosition( stream.readUTF() );					// Person current position
				person.setIncome( stream.readInt() );							// Person income
				person.setEducation( stream.readUTF() );						// Person education
				person.setPSEducationYears( stream.readInt() );					// Person number of PS years
				person.setIsInSchool( stream.readBoolean() );					// Person is in school currently
				person.setPSStartYear( stream.readInt() );						// Person starting PS year
				person.setPSFinishYear( stream.readInt() );						// Person finishing PS year
				
			// HISTORY.
				// Don't need hometownCheckpoints (???)

				// Hometown History.
				archiveSize = stream.readInt();									// Person number of hometown history elements
				hometownArchive = new ActivityArchive();
				for (i = 0; i < archiveSize; i++) {
					//tmpArrayList = archive.getDictEntryNameAndYears(i);
					tmpStr = stream.readUTF();									// Person hometown history location (loop)
					//tmpIntArray = (int[])tmpArrayList.get(1);
					tmpSYear = stream.readInt();								// Person hometown history start year (loop)
					tmpEYear = stream.readInt();								// Person hometown history end year (loop)
					hometownArchive.addEntry(tmpStr, tmpSYear, tmpEYear);
				} // end for i (read out all hometown archive elements)
				person.setHometownHistory(hometownArchive);
				
				// School History.
				archiveSize = stream.readInt();									// Person number of school history elements
				schoolArchive = new ActivityArchive();
				for (i = 0; i < archiveSize; i++) {
					//tmpArrayList = archive.getDictEntryNameAndYears(i);
					//tmpStrArray = (String[])tmpArrayList.get(0);
					tmpStr = stream.readUTF();									// Person school history education type (loop)
					tmpStr2 = stream.readUTF();									// Person school history institution (loop)
					//tmpIntArray = (int[])tmpArrayList.get(1);
					tmpSYear = stream.readInt();								// Person school history start year (loop)
					tmpEYear = stream.readInt();								// Person school history end year (loop)
					schoolArchive.addEntry(new String[] {tmpStr, tmpStr2}, tmpSYear, tmpEYear);
				} // end for i (read out all hometown archive elements)
				person.setSchoolHistory(schoolArchive);
				
				// Work History.
				archiveSize = stream.readInt();									// Person number of work history elements
				workArchive = new ActivityArchive();
				for (i = 0; i < archiveSize; i++) {
					//tmpArrayList = archive.getDictEntryNameAndYears(i);
					//tmpStrArray = (String[])tmpArrayList.get(0);
					tmpStr = stream.readUTF();									// Person work history workplace ID (loop)
					tmpStr2 = stream.readUTF();									// Person work history career ID  (loop)
					//tmpIntArray = (int[])tmpArrayList.get(1);
					tmpSYear = stream.readInt();								// Person work history start year (loop)
					tmpEYear = stream.readInt();								// Person work history end year (loop)
					workArchive.addEntry(new String[] {tmpStr, tmpStr2}, tmpSYear, tmpEYear);
				} // end for i (read out all hometown archive elements)
				person.setWorkHistory(workArchive);

			// SOCIETAL HISTORY.
				// Hometown History.
				archiveSize = stream.readInt();									// Person number of societal hometown history elements
				socHometownArchive = new ActivityArchive();
				for (i = 0; i < archiveSize; i++) {
					//tmpArrayList = archive.getDictEntryNameAndYears(i);
					tmpStr = stream.readUTF();									// Person societal hometown history location (loop)
					//tmpIntArray = (int[])tmpArrayList.get(1);
					tmpSYear = stream.readInt();								// Person societal hometown history start year (loop)
					tmpEYear = stream.readInt();								// Person societal hometown history end year (loop)
					socHometownArchive.addEntry(tmpStr, tmpSYear, tmpEYear);
				} // end for i (read out all hometown archive elements)
				person.setSocietalHometownHistory(socHometownArchive);
				
				// School History.
				archiveSize = stream.readInt();									// Person societal number of school history elements
				socSchoolArchive = new ActivityArchive();
				for (i = 0; i < archiveSize; i++) {
					//tmpArrayList = archive.getDictEntryNameAndYears(i);
					//tmpStrArray = (String[])tmpArrayList.get(0);
					tmpStr = stream.readUTF();									// Person societal school history education type (loop)
					tmpStr2 = stream.readUTF();									// Person societal school history institution (loop)
					//tmpIntArray = (int[])tmpArrayList.get(1);
					tmpSYear = stream.readInt();								// Person societal school history start year (loop)
					tmpEYear = stream.readInt();								// Person societal school history end year (loop)
					socSchoolArchive.addEntry(new String[] {tmpStr, tmpStr2}, tmpSYear, tmpEYear);
				} // end for i (read out all hometown archive elements)
				person.setSocietalSchoolHistory(socSchoolArchive);
				
				// Work History.
				archiveSize = stream.readInt();									// Person societal number of work history elements
				socWorkArchive = new ActivityArchive();
				for (i = 0; i < archiveSize; i++) {
					//tmpArrayList = archive.getDictEntryNameAndYears(i);
					//tmpStrArray = (String[])tmpArrayList.get(0);
					tmpStr = stream.readUTF();									// Person societal work history workplace ID (loop)
					tmpStr2 = stream.readUTF();									// Person societal work history career ID  (loop)
					//tmpIntArray = (int[])tmpArrayList.get(1);
					tmpSYear = stream.readInt();								// Person societal work history start year (loop)
					tmpEYear = stream.readInt();								// Person societal work history end year (loop)
					socWorkArchive.addEntry(new String[] {tmpStr, tmpStr2}, tmpSYear, tmpEYear);
				} // end for i (read out all hometown archive elements)
				person.setSocietalWorkHistory(socWorkArchive);
				
			// FAMILY.
				person.setFamilyID( stream.readInt() );							// Person family ID
				//tmpArrayListInt = person.getParentIDs();
				numParents = stream.readInt();								// Person number of parents
				parents = new ArrayList<Integer>();
				for (i = 0; i < numParents; i++) {
					parents.add( stream.readInt() );							// Person parent ID (loop)
				} // end for i (read out all parent IDs)
				person.setParentIDs(parents);
				numChildren = stream.readInt();								// Person number of children
				children = new ArrayList<Integer>();
				for (i = 0; i < numChildren; i++) {
					children.add(stream.readInt());								// Person child ID (loop)
				} // end for i (read out all children IDs)
				person.setChildrenIDs(children);
				numSiblings = stream.readInt();								// Person number of siblings
				siblings = new ArrayList<Integer>();
				for (i = 0; i < numSiblings; i++) {
					siblings.add(stream.readInt());								// Person sibling ID (loop)
				} // end for i (read out all sibling IDs)
				person.setSiblingIDs(siblings);

			// CLUBS.
				//tmpArrayListInt = person.getClubIDs();
				numClubs = stream.readInt();								// Person number of clubs
				clubs = new ArrayList<Integer>();
				for (i = 0; i < numClubs; i++) {
					clubs.add(stream.readInt());								// Person's club ID (loop)
				} // end for i (read out all club IDs)
				person.setClubIDs(clubs);

			// GROUPS.
				numGroups = stream.readInt();								// Person number of groups
				groups = new ArrayList<ArrayList<String>>();
				grp = new ArrayList<String>();
				for (i = 0; i < numGroups; i++) {
					grp = new ArrayList<String>();
					grp.add(stream.readUTF());									// Person group ID (loop)
					grp.add(tmpStr2 = stream.readUTF());						// Person group role (loop)
					groups.add(grp);
				} // end for i (read out all group IDs)
				person.setGroupIDs(groups);

			// FRIENDSHIP DETERMINATION.
				//tmpArrayListInt = person.getFriendProbIDs();
				//tmpArrayListDbl = person.getFriendProbabilities();
				numFriendDeterm = stream.readInt();							// Person number of potential friendships
				friendDetermIDs = new ArrayList<Integer>();
				friendDetermProbs = new ArrayList<Double>();
				for (i = 0; i < numFriendDeterm; i++) {
					friendDetermIDs.add(stream.readInt());						// Person possible friendship ID (loop)
				} // end for i (read out all friendship prob IDs)
				// No need to read number of friendship probabilities, since it MUST be equal to that of friendship prob ids.
				for (i = 0; i < numFriendDeterm; i++) {
					friendDetermProbs.add(stream.readDouble());					// Person possible friendship probability (loop)
				} // end for i (read out all friendship probabilities)
				person.setFriendProbIDs(friendDetermIDs);
				person.setFriendProbabilities(friendDetermProbs);
				
			// FRIENDS.
				//tmpArrayList = person.getFriends();
				numFriends = stream.readInt();								// Person number of friends
				friends = new ArrayList<Friendship>();
				for (i = 0; i < numFriends; i++) {
					//friendship = (Friendship)tmpArrayList.get(i);
					// No need to record person's ID here from friendship, since it is the ID written at the beginning.
					tmpInt = stream.readInt();									// Person friend ID (loop)
					tmpInt2 = stream.readInt();									// Person friend type (loop)
					tmpDbl = stream.readDouble();								// Person friend strength (loop)
					tmpStr = stream.readUTF();									// Person friend description (loop)
					friendship = new Friendship(personID, tmpInt, tmpInt2, tmpDbl, tmpStr);
					friends.add(friendship);
				} // end for i (read out all friend IDs)
				person.setFriends(friends);
				
				
				AllPersons.add(person);
				
		} // end for p (loop through all people to create them)
		


	} // end readFromFile()
	*/
	
	private static DataInputStream openStream (String filename) {
		DataInputStream stream = null;
		try {
			stream = new DataInputStream(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			System.err.println("In OutputSocietyCompactly->WriteSocietyToBinary(); could not create binary file successfully.");
			e.printStackTrace();
		}
		return stream;
	} // end openStream()
	
	private static void closeStream (DataInputStream stream) {
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} // end closeStream()
	
} // end InputSocietyCompactly
