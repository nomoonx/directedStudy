package thesis_network_growth;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;



public class ActivityArchive extends ArrayList<Dictionary<Object, int[]>> {
	
	//static int PRESENT = -1;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1664930430461431527L;



	public ActivityArchive () {
		super();
	}
	
	
	public void addEntry (Object activity, int startYear) {
		// Indicate that person is currently involved in the given activity, and call addEntry() with this information.
		addEntry(activity, startYear, Configuration.SocietyYear);
	}
	
	
	public void addEntry (Object activity, int startYear, int endYear) {
		// Add the given activity and time period to the archive array.
		// Each activity item is a Hashtable Dictionary containing an Object (typically a String) identifying the activity (i.e. location or
		// position title, etc.) and an int array of 2 integers, the start year and end year of that activity. If the person is actively in the
		// activity presently, then the end year is set as the current society year.
		//
		// *** NOTE *** This may cause problems when running a timestep simulation. There should be another way to indicate ongoing involvement in the activity!

		int[] years = new int[] {startYear, endYear};
		
		Dictionary<Object, int[]> dict = new Hashtable<Object, int[]>();
		dict.put(activity, years);
		//dict.put(years, activity);
		
		this.add(dict);
	}
	
	public void patchSequentialEntries () {
		// Sometimes entries are added sequentially for the same entry (i.e. hometown could be London for [1990,1995] followed by London
		// for [1995,1999]; and this should be patched together to be just London for [1990,1999].) This function loops through the array
		// and puts together any entries that should be merged.
		
		Hashtable<Object, int[]> curr, next;
		Hashtable<Object, int[]> tmp;
		ArrayList currInfo, nextInfo;
		Object currName, nextName;
		int[] currYears;
		int[] nextYears;
		int[] mergedPeriods;
		
		int i = 0;
		
		// Looking at consecutive entries, so loop from the start to the second last entry, so that each one will have a consecutive conjugate.
		while (i < this.size() - 1) {
			curr = (Hashtable<Object, int[]>)this.get(i);
			next = (Hashtable<Object, int[]>)this.get(i+1);
			
			currInfo = getDictEntryNameAndYears(curr);
			nextInfo = getDictEntryNameAndYears(next);
			currName = currInfo.get(0);
			nextName = nextInfo.get(0);
			currYears = (int[])currInfo.get(1);
			nextYears = (int[])nextInfo.get(1);
			
			
			// If the two sequential entries have the identical name AND one ends and the next begins in the same year
			if (currName.equals(nextName) && (currYears[1] == nextYears[0])) {
				//System.out.println("merging {" + currName + " " + currYears[0] + "," + currYears[1] + "} and {" + nextName + " " + nextYears[0] + "," + nextYears[1] + "}.");
				mergedPeriods = new int[] {currYears[0], nextYears[1]}; // Merge two time periods.

				tmp = new Hashtable<Object, int[]>();
				tmp.put(currName, mergedPeriods);
				
				// Update hashtable with merged time period.
				this.set(i, tmp);
				// Remove second entry since it is now included in the above entry.
				this.remove(i+1);
				
				// This decrement is important for multiple duplicates (i.e. if there are 3 entries in a row to be merged, then this will allow the loop to continue checking all).
				i--;
			} // end if (check if two consecutive entries contain the same activity name and consecutive periods)
			
			// Increment loop counter.
			i++;
		}
	}
	
	
	public int[] getLastActivityPeriod () {
		Hashtable act = (Hashtable)this.get(this.size()-1);
		int[] period = this.getDictEntryYears(act);
		return period;
	} // end getLastActivityPeriod()
	
	public Object getLastActivityName () {
		
		if (this.size() == 0) {
			System.err.println("In ActivityArchive->getLastActivityName(); the archive is empty, so canot get last activity.");
			return "--";
		} // end if (check if empty)
		
		Hashtable act = (Hashtable)this.get(this.size()-1);
		Object name = this.getDictEntryName(act);
		return name;
	} // end getLastActivityName()

	public Object getActivityAtYear (int year) {
		
		Hashtable dict;
		Object key;
		int[] value;
		int m = this.size();
		int i;

		for (i = 0; i < m; i++) {
			dict = (Hashtable)this.get(i);

			ArrayList keyValue = this.getDictEntryNameAndYears(dict);
			key = keyValue.get(0);
			value = (int[])keyValue.get(1);

			// ----------------------------------------------------------------------------------------------------------------
			// *** NOTE *** What if there are multiple activities in the same year? This only returns the first one found!
			// ----------------------------------------------------------------------------------------------------------------
			// The second clause (year != value[1]) is added so that an activity ending in that year is not chosen as the correct year's activity.
			if (isYearWithinRange(year, value[0], value[1])) {
			//if (ValidationTools.numberIsWithin(year, value[0], value[1]) && (year != value[1])) {
				return key;
			} // end if (check if given year is within activity period)
		} // end for i (looping through each archive element) 

		return null;
		
	} // end getActivityAtYear()
	
	public ArrayList<Object> getActivityAndYearsAtYear (int year) {
		
		Hashtable dict;
		Object key;
		int[] value;
		int m = this.size();
		int i;

		for (i = 0; i < m; i++) {
			dict = (Hashtable)this.get(i);

			ArrayList keyValue = this.getDictEntryNameAndYears(dict);
			key = keyValue.get(0);
			value = (int[])keyValue.get(1);

			// ----------------------------------------------------------------------------------------------------------------
			// *** NOTE *** What if there are multiple activities in the same year? This only returns the first one found!
			// ----------------------------------------------------------------------------------------------------------------
			if (isYearWithinRange(year, value[0], value[1])) {
			//if (ValidationTools.numberIsWithin(year, value[0], value[1]) && (year != value[1])) {
				return keyValue;
			} // end if (check if given year is within activity period)
		} // end for i (looping through each archive element) 

		return null;
		
	} // end getActivityAtYear()
	
	
	
	
	
	
	private Object getDictEntryName (Hashtable dict) {
		Iterator iter;
		Map.Entry entry;
		Object key = "";
		iter = dict.entrySet().iterator();
		while (iter.hasNext()) {
			entry = (Entry) iter.next();
			key = entry.getKey();
		} // end while (iterating through dictionary)
		return key;
	} // end getDictEntry()
	
	private int[] getDictEntryYears (Hashtable dict) {
		Iterator iter;
		Map.Entry entry;
		int[] value = null;
		iter = dict.entrySet().iterator();
		while (iter.hasNext()) {
			entry = (Entry) iter.next();
			value = (int[])entry.getValue();
		} // end while (iterating through dictionary)
		return value;
	} // end getDictEntry()
	
	public ArrayList<Object> getDictEntryNameAndYears (Hashtable<Object, int[]> dict) {
		Iterator<Map.Entry<Object, int[]>> iter;
		Map.Entry<Object, int[]> entry;
		Object key;
		int[] value;
		ArrayList<Object> returnArray = new ArrayList<Object>();
		iter = dict.entrySet().iterator();
		while (iter.hasNext()) {
			entry = (Entry<Object,int[]>) iter.next();
			key = entry.getKey();
			value = (int[])entry.getValue();
			returnArray.add(key);
			returnArray.add(value);
		} // end while (iterating through dictionary)
		return returnArray;
	} // end getDictEntry()
	
	public ArrayList<Object> getDictEntryNameAndYears (int index) {

		// Get the hashtable entry at position index.
		Hashtable<Object, int[]> dict = (Hashtable<Object, int[]>)this.get(index);
		
		Iterator<Map.Entry<Object, int[]>> iter;
		Map.Entry<Object, int[]> entry;
		Object key;
		int[] value;
		ArrayList<Object> returnArray = new ArrayList<Object>();
		iter = dict.entrySet().iterator();
		while (iter.hasNext()) {
			entry = (Entry<Object, int[]>) iter.next();
			key = entry.getKey();
			value = (int[])entry.getValue();
			returnArray.add(key);
			returnArray.add(value);
		} // end while (iterating through dictionary)
		return returnArray;
	} // end getDictEntry()





	// This is a static function, not a regular class member.
	public static void xxxaddHometownsForPeriodxxx (String tmpMarker, ActivityArchive archive, ActivityArchive localArchive, String prevHometown, double p_sameAsPrevious, int minNumMoves, int maxNumMoves, int startYear, int endYear, ArrayList<String> possibleCitiesForMoves) {
	//public static void addHometownsForPeriod (String tmpMarker, ActivityArchive archive, ActivityArchive localArchive, String prevHometown, double p_sameAsPrevious, int minNumMoves, int maxNumMoves, int startYear, int endYear, String[] possibleCitiesForMoves) {
		// This function is important for generating a random sequence of hometowns for a person, based on a variety of probabilities and other parameters.
		// To begin, if the prevHometown value is part of the possibleCitiesForMoves array of locations, then that prevHometown has a probability of continuing
		// to be the person's hometown, based on the probability p_sameAsPrevious. If, however, the prevHometown is not a valid option (an example of this
		// is if the person needs to live in a city with a university but the prevHometown does not have a university, then the person cannot stay there),
		// then the person definitely will move. In the case of a person moving, random locations and time periods are generated based on the parameters
		// minNumMoves, maxNumMoves, startYear, and endYear. NOTE that the location generation is uniform, in that there are equal probabilities of choosing
		// any of the cities. In the case of staying in the prevHometown, nothing needs to be done. All hometown information is added to the ActivityArchive
		// parameter archive, and all hometowns in the simulation's society are recorded also in the localArchive parameter.
		//
		// param archive: the ActivityArchive for all activity
		// param localArchive: the ActivityArchive for activity that occurs locally, in the society of focus
		// param prevHometown: the name of the location that the person currently lives in
		// param p_sameAsPrevious: the probability that the person will remain in their current location, prevHometown (ONLY if prevHometown is a valid option!)
		// param minNumMoves: the minimum number of moves the person will make during the given period
		// param maxNumMoves: the maximum number of moves the person will make during the given period
		// param startYear: the starting year for the residence activity to take place 
		// param endYear: the final year for the residence activity to take place
		// param possibleCitiesForMoves: the array of possible locations that person can live in for the given period

		double rndSameHometown = Distribution.uniform(0.0, 1.0);

		// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		// * NOTE * This ensures that the person only stays in their current hometown if that city is a possible location for them at this time; and if the random number allows it.
		// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		if (ArrayTools.containsElement(possibleCitiesForMoves, prevHometown) && (rndSameHometown <= p_sameAsPrevious)) {
			// Same hometown throughout this period.

			// Add hometown to archive.
			archive.addEntry(prevHometown + tmpMarker, startYear, endYear);
			// If in the local society, then add to local archive.
			if (prevHometown.equals(Configuration.SocietyName)) {
				localArchive.addEntry(prevHometown + tmpMarker, startYear, endYear);
			} // end if (check if in society)
			
			
		} else {
			// Multiple hometowns throughout this period.
			int numMoves = Distribution.uniform(minNumMoves, maxNumMoves); 				// Should be Config parameters.
			int y;
			int rndYear;
			int prevMove = startYear;
			String moveCity;
			
			for (y = 0; y < numMoves - 1; y++) {
				// Choose random location for move.
				moveCity = (String)Distribution.uniformRandomObjectStr(possibleCitiesForMoves);
				// Choose random year for move.
				//System.out.println("Generating move year for " + startYear + ", " + endYear);
				
				// ><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><
				// TODO: ensure that this is working. It seems as though the startYear parameter should instead be prevMove.
				rndYear = Distribution.uniform(startYear, endYear);
				// ><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><
				
				// Add hometown to archive.
				archive.addEntry(moveCity + tmpMarker, prevMove, rndYear);
				// If in the local society, then add to local archive.
				if (moveCity.equals(Configuration.SocietyName)) {
					localArchive.addEntry(moveCity + tmpMarker, prevMove, rndYear);
				} // end if (check if in society)
				
				// Update previous move.
				prevMove = rndYear;
			} // end for y (number of moves)
			
			// Choose random location for move.
			moveCity = (String)Distribution.uniformRandomObjectStr(possibleCitiesForMoves);
			// Add hometown to archive.
			archive.addEntry(moveCity + tmpMarker, prevMove, endYear);
			// If in the local society, then add to local archive.
			if (moveCity.equals(Configuration.SocietyName)) {
				localArchive.addEntry(moveCity + tmpMarker, prevMove, endYear);
			} // end if (check if in society)
			
			
		} // end if (same hometown during period)
		

	} // end addHometownsForPeriod()



	public static void addHometownsForPeriod (ActivityArchive archive, ActivityArchive localArchive, int startYear, int endYear, ArrayList<String> possibleCitiesForMoves) {
	//public static void addHometownsForPeriod (ActivityArchive archive, ActivityArchive localArchive, int minNumMoves, int maxNumMoves, int startYear, int endYear, String[] possibleCitiesForMoves) {
			// This function is important for generating a random sequence of hometowns for a person, based on a variety of probabilities and other parameters.
			// To begin, if the prevHometown value is part of the possibleCitiesForMoves array of locations, then that prevHometown has a probability of continuing
			// to be the person's hometown, based on the probability p_sameAsPrevious. If, however, the prevHometown is not a valid option (an example of this
			// is if the person needs to live in a city with a university but the prevHometown does not have a university, then the person cannot stay there),
			// then the person definitely will move. In the case of a person moving, random locations and time periods are generated based on the parameters
			// minNumMoves, maxNumMoves, startYear, and endYear. NOTE that the location generation is uniform, in that there are equal probabilities of choosing
			// any of the cities. In the case of staying in the prevHometown, nothing needs to be done. All hometown information is added to the ActivityArchive
			// parameter archive, and all hometowns in the simulation's society are recorded also in the localArchive parameter.
			//
			// param archive: the ActivityArchive for all activity
			// param localArchive: the ActivityArchive for activity that occurs locally, in the society of focus
			// param minNumMoves: the minimum number of moves the person will make during the given period
			// param maxNumMoves: the maximum number of moves the person will make during the given period
			// param startYear: the starting year for the residence activity to take place 
			// param endYear: the final year for the residence activity to take place
			// param possibleCitiesForMoves: the array of possible locations that person can live in for the given period

			double rndSameHometown = Distribution.uniform(0.0, 1.0);

			// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			// * NOTE * This ensures that the person only stays in their current hometown if that city is a possible location for them at this time; and if the random number allows it.
			// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

			

				// Multiple hometowns throughout this period.
				//int numMoves = Distribution.uniform(minNumMoves, maxNumMoves); 				// Should be Config parameters.
				//int numMoves = Configuration.DetermineNumMoves(endYear-startYear);
				int numMoves = (int)Math.round((endYear-startYear) / Configuration.numAvgYearsBetweenMoves);
				
				//System.out.println(startYear + " - " + endYear + " || " + numMoves);

				int y;
				int rndYear;
				int prevMove = startYear;
				String moveCity;

				if (numMoves == 0) {
					// IF ONE HOMETOWN OVER ENTIRE PERIOD.

					ArrayList<String> hometownLocations = Distribution.permutation(Configuration.OtherCities, 1);
					moveCity = (String)hometownLocations.get(0);
					archive.addEntry(moveCity, startYear, endYear);
					if (moveCity.equals(Configuration.SocietyName)) {
						localArchive.addEntry(moveCity, startYear, endYear);
					} // end if (check if in society)
				} else {
					// IF MULTIPLE HOMETOWNS OVER PERIOD.
					
					
					
					//System.err.println("numMoves = " + numMoves);
				
					// Create array of hometown locations. This will include the local society and numMoves-1 randomly chosen locations.
					ArrayList<String> hometownLocations = Distribution.permutation(Configuration.OtherCities, numMoves-1);	// First get numMoves-1 random locations from outside of the society.
					hometownLocations.add(Configuration.SocietyName);									// Then add society to ensure it is included somewhere (currently at end). If we didn't add it separately, there would be a chance that the society would get omitted from the final list.
					//DebugTools.printArray(hometownLocations.toArray());
					hometownLocations = (ArrayList<String>)Distribution.permutation(hometownLocations);					// Shuffle up list again, this time with society in the list.
					//System.out.println("\t~~~~~\t");
					//DebugTools.printArray(hometownLocations.toArray());
					//System.out.println("........................................................");
					
					int gapSupporter;
				
					// Loop through to create all hometowns except final one.
					for (y = 0; y < numMoves - 1; y++) {
					
						// Choose random location for move.
						//moveCity = (String)Distribution.uniformRandomObject(possibleCitiesForMoves);
						moveCity = (String)hometownLocations.get(y);		// Get locations from shuffled list.
					
					
					
					
						// The gapSupporter helps the moves to be spaced out so that they can't be all clustered together.
						gapSupporter = (numMoves-y-1)*Configuration.numAvgYearsBetweenMoves;
					
						//System.err.println("Remaining moves: " + (numMoves-y) + " | " + gapSupporter + " || <" + prevMove + "," + (endYear-gapSupporter) + ">");
					
						// Choose random year for move.
						//rndYear = Distribution.uniform(prevMove, endYear);
						
						do {
							// Ensure that the person does not move too frequently. Choose a new move year if it is too close to the last move.
							rndYear = Distribution.uniform(prevMove, endYear-gapSupporter); // Subtract 1 from endYear because we don't want the person to move more than once in the same year.
						} while ((rndYear-prevMove) < Configuration.minYearsBetweenMoves); // end do-while (ensure person doesn't move too many times within a short amount of time)
						
						//System.err.println("Remaining moves: " + (numMoves-y) + " | " + gapSupporter + " || <" + prevMove + "," + (endYear-gapSupporter) + "> --- " + rndYear);
						
						//System.out.println("random number in [" + prevMove + "," + (endYear-gapSupporter) + "] --> " + rndYear);
					
						//System.out.println("Generating move year for " + startYear + ", " + endYear + " || " + rndYear);

						// Add hometown to archive.
						archive.addEntry(moveCity, prevMove, rndYear);
						// If in the local society, then add to local archive.
						if (moveCity.equals(Configuration.SocietyName)) {
							localArchive.addEntry(moveCity, prevMove, rndYear);
						} // end if (check if in society)

						// Update previous move.
						prevMove = rndYear;
					} // end for y (number of moves)
				
				
					// Lastly, add the final, current hometown to the archive.
				
					// Choose random location for move.
					//moveCity = (String)Distribution.uniformRandomObject(possibleCitiesForMoves);
					moveCity = (String)hometownLocations.get(numMoves-1);		// Get last location from shuffled list.
				
					// Add hometown to archive.
					archive.addEntry(moveCity, prevMove, endYear);	// This endYear must be set in the final archive entry. This is why we can't include this in the above loop.
					// If in the local society, then add to local archive.
					if (moveCity.equals(Configuration.SocietyName)) {
						localArchive.addEntry(moveCity, prevMove, endYear);
					} // end if (check if in society)
				
				} // end if (determine whether or not the person has multiple hometowns over the period)
			
				//DebugTools.printArray(archive);

		} // end addHometownsForPeriod()
	
	
	
	
	
	
	
	
	
	
	
	// This is a static function, not a regular class member.
	public static void addHometownsForPeriodzzz (ActivityArchive archive, ActivityArchive localArchive, int minNumMoves, int maxNumMoves, int startYear, int endYear, ArrayList<String> possibleCitiesForMoves) {
	//public static void addHometownsForPeriod (ActivityArchive archive, ActivityArchive localArchive, int minNumMoves, int maxNumMoves, int startYear, int endYear, String[] possibleCitiesForMoves) {
			// This function is important for generating a random sequence of hometowns for a person, based on a variety of probabilities and other parameters.
			// To begin, if the prevHometown value is part of the possibleCitiesForMoves array of locations, then that prevHometown has a probability of continuing
			// to be the person's hometown, based on the probability p_sameAsPrevious. If, however, the prevHometown is not a valid option (an example of this
			// is if the person needs to live in a city with a university but the prevHometown does not have a university, then the person cannot stay there),
			// then the person definitely will move. In the case of a person moving, random locations and time periods are generated based on the parameters
			// minNumMoves, maxNumMoves, startYear, and endYear. NOTE that the location generation is uniform, in that there are equal probabilities of choosing
			// any of the cities. In the case of staying in the prevHometown, nothing needs to be done. All hometown information is added to the ActivityArchive
			// parameter archive, and all hometowns in the simulation's society are recorded also in the localArchive parameter.
			//
			// param archive: the ActivityArchive for all activity
			// param localArchive: the ActivityArchive for activity that occurs locally, in the society of focus
			// param minNumMoves: the minimum number of moves the person will make during the given period
			// param maxNumMoves: the maximum number of moves the person will make during the given period
			// param startYear: the starting year for the residence activity to take place 
			// param endYear: the final year for the residence activity to take place
			// param possibleCitiesForMoves: the array of possible locations that person can live in for the given period

			double rndSameHometown = Distribution.uniform(0.0, 1.0);

			// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			// * NOTE * This ensures that the person only stays in their current hometown if that city is a possible location for them at this time; and if the random number allows it.
			// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

			

				// Multiple hometowns throughout this period.
				//int numMoves = Distribution.uniform(minNumMoves, maxNumMoves); 				// Should be Config parameters.
				//int numMoves = Configuration.DetermineNumMoves(endYear-startYear);
				int numMoves = (int)Math.round((endYear-startYear) / Configuration.numAvgYearsBetweenMoves);
				
				//System.err.println(startYear + " - " + endYear + " || " + numMoves);

				int y;
				int rndYear;
				int prevMove = startYear;
				String moveCity;

				if (numMoves == 0) {
					// IF ONE HOMETOWN OVER ENTIRE PERIOD.

					ArrayList<String> hometownLocations = Distribution.permutation(Configuration.OtherCities, 1);
					moveCity = (String)hometownLocations.get(0);
					archive.addEntry(moveCity, startYear, endYear);
					if (moveCity.equals(Configuration.SocietyName)) {
						localArchive.addEntry(moveCity, startYear, endYear);
					} // end if (check if in society)
				} else {
					// IF MULTIPLE HOMETOWNS OVER PERIOD.
					
					
					
				//System.err.println("numMoves = " + numMoves);
				
					// Create array of hometown locations. This will include the local society and numMoves-1 randomly chosen locations.
					//System.out.println("numMoves = " + numMoves);
					//ArrayList otherCities = ArrayTools.stringArrayToArrayList(Configuration.OtherCities);
					ArrayList<String> hometownLocations = Distribution.permutation(Configuration.OtherCities, numMoves-1);	// First get numMoves-1 random locations from outside of the society.
					hometownLocations.add(Configuration.SocietyName);									// Then add society to ensure it is included somewhere (currently at end). If we didn't add it separately, there would be a chance that the society would get omitted from the final list.
					//DebugTools.printArray(hometownLocations);
					hometownLocations = (ArrayList<String>)Distribution.permutation(hometownLocations);					// Shuffle up list again, this time with society in the list.
					//DebugTools.printArray(hometownLocations);
				
					// Loop through to create all hometowns except final one.
					for (y = 0; y < numMoves - 1; y++) {
					
						// Choose random location for move.
						//moveCity = (String)Distribution.uniformRandomObject(possibleCitiesForMoves);
						moveCity = (String)hometownLocations.get(y);		// Get locations from shuffled list.
					
					
					
					
					
						int gapSupporter = (numMoves-y-1)*Configuration.numAvgYearsBetweenMoves;
					
						//System.err.println("Remaining moves: " + (numMoves-y) + " | " + gapSupporter + " || <" + prevMove + "," + (endYear-gapSupporter) + ">");
					
						// Choose random year for move.
						//rndYear = Distribution.uniform(prevMove, endYear);
						
						do {
							// Ensure that the person does not move too frequently. Choose a new move year if it is too close to the last move.
							rndYear = Distribution.uniform(prevMove, endYear-gapSupporter); // Subtract 1 from endYear because we don't want the person to move more than once in the same year.
						} while ((rndYear-prevMove) < Configuration.minYearsBetweenMoves); // end do-while (ensure person doesn't move too many times within a short amount of time)
						
						//System.err.println("Remaining moves: " + (numMoves-y) + " | " + gapSupporter + " || <" + prevMove + "," + (endYear-gapSupporter) + "> --- " + rndYear);
						
						//System.out.println("random number in [" + prevMove + "," + (endYear-gapSupporter) + "] --> " + rndYear);
					
						//System.out.println("Generating move year for " + startYear + ", " + endYear + " || " + rndYear);

						// Add hometown to archive.
						archive.addEntry(moveCity, prevMove, rndYear);
						// If in the local society, then add to local archive.
						if (moveCity.equals(Configuration.SocietyName)) {
							localArchive.addEntry(moveCity, prevMove, rndYear);
						} // end if (check if in society)

						// Update previous move.
						prevMove = rndYear;
					} // end for y (number of moves)
				
				
					// Lastly, add the final, current hometown to the archive.
				
					// Choose random location for move.
					//moveCity = (String)Distribution.uniformRandomObject(possibleCitiesForMoves);
					moveCity = (String)hometownLocations.get(numMoves-1);		// Get last location from shuffled list.
				
					// Add hometown to archive.
					archive.addEntry(moveCity, prevMove, endYear);	// This endYear must be set in the final archive entry. This is why we can't include this in the above loop.
					// If in the local society, then add to local archive.
					if (moveCity.equals(Configuration.SocietyName)) {
						localArchive.addEntry(moveCity, prevMove, endYear);
					} // end if (check if in society)
				
				} // end if (determine whether or not the person has multiple hometowns over the period)
			
				//DebugTools.printArray(archive);

		} // end addHometownsForPeriod()
	
	
	
	public void updateSameLastEntry () {
		// This method will automatically update the last entry in the archive to contain the current society year.
		// For example, if a person's hometown was {London from 2007 to 2015} initially, then at 2016, that entry will update to:
		// {London from 2007 to 2016}. 
		
		Object actName = getLastActivityName();
		int[] actYears = getLastActivityPeriod();
		
		// Add additional entry at the end to reflect the new year at this activity.
		this.addEntry(actName, actYears[1], Configuration.SocietyYear);
		
		// Merge all consecutive matching entries into one. This should only ever have to deal with the last entry.
		patchSequentialEntries();
		
		
	} // end updateSameLastEntry()
	
	
	
	private boolean isYearWithinRange (int year, int rangeMin, int rangeMax) {
		// The second clause (year != value[1]) is added so that an activity ending in that year is not chosen as the correct year's activity.
		//if (ValidationTools.numberIsWithin(year, rangeMin, rangeMax) && ((year != rangeMax) && (year != rangeMin))) {
		if (ValidationTools.numberIsWithin(year, rangeMin, rangeMax)) {
			return true;
		} // end if (check if year is in range, but not ending year unless it is also the starting year)

		return false;
	} // end isYearWithinRange()

	/*
	private boolean isYearWithinRange2 (int year, int rangeMin, int rangeMax) {
		// The second clause (year != value[1]) is added so that an activity ending in that year is not chosen as the correct year's activity.
		//if (ValidationTools.numberIsWithin(year, rangeMin, rangeMax) && ((year != rangeMax) && (year != rangeMin))) {
		if (ValidationTools.numberIsWithin(year, rangeMin, rangeMax)) {
			if (((year == rangeMax) && (year == rangeMin)) || (year != rangeMax)) {
				return true;
			} else {
				//System.out.println("Why is the year not in range?!?!?!  " + year + " in [" + rangeMin + "," + rangeMax + "].");
				return false;
			}
		} // end if (check if year is in range, but not ending year unless it is also the starting year)
		
		//System.out.println("This is even worse! or is it?!?!?!  " + year + " in [" + rangeMin + "," + rangeMax + "].");
		return false;
	} // end isYearWithinRange()
	*/

} // end ActivityArchive class
