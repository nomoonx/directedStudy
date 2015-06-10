package thesis_network_growth;

import java.util.ArrayList;


public class TempleSet extends InstitutionSet {
	
	static ArrayList<String> religions;
	
	
	
	public TempleSet (String filepath, String elementName, String treeRootName) {
		super(filepath, elementName, treeRootName);
	} // end TempleSet() constructor

	public static void initTemples () {
		// Create the list of all religions.
		createReligionsList();
	} // end initTemples()

	public static void createReligionsList () {
		// This function will create an arraylist containing each of the religions (as given from the Configuration file). This is used in GroupGenerator to
		// create a Group for each of the religious bodies (i.e. one group is the body of Catholics in the society, etc.).
		// NOTE we are removing "None" from the array for this list, as this is used in generating the religious bodies/temples groups, so None is unnecessary here.
		
		religions = new ArrayList<String>();
		
		int r;
		// Loop through all religions, and add all except for "None" to the arraylist, religions.
		for (r = 0; r < Configuration.ReligionLabels.length; r++) {
			if (!Configuration.ReligionLabels[r].equals("None")) {
				religions.add(Configuration.ReligionLabels[r]);
			} // end if (check if religion is "None")
		} // end for r (loop through all religions)

	} // end createReligionsList()

	public static ArrayList<String> getReligionsList () {
		return religions;
	} // end getReligionsList()
	
} // end TempleSet class
