package thesis_network_growth;

import java.util.ArrayList;


public class GroupGenerator {
	
	// Keep track of all Groups in one data structure.
	public static Tree groupTree;
	
	
	
	// Common sub-tree labels.
	public static String schoolsLabel = "Schools";
	public static String elementarySchoolsLabel = "Elementary";
	public static String universitySchoolsLabel = "U";
	public static String collegeSchoolsLabel = "C";
	
	public static String workLabel = "Workplaces";
	public static String religionsLabel = "Religious Communities";
	public static String clubsLabel = "Clubs";
	
	
	public GroupGenerator () {}
	
	public static void generateGroups () {
		groupTree = new Tree("AllGroups", false, 0, 0);
		
		/////////////////////////////////////////////////////////////////////////////////////////////
		// Generate all school groups.
		///////////////////////////////////////////////////////////////////////////////////////////
		Tree schoolsTree = groupTree.addTree(schoolsLabel, false, 0, 0);
		Tree elemSchoolsTree = schoolsTree.addTree(elementarySchoolsLabel, false, 0, 0);
		Tree universityTree = schoolsTree.addTree(universitySchoolsLabel, false, 0, 0);
		Tree collegeTree = schoolsTree.addTree(collegeSchoolsLabel, false, 0, 0);
		addSchoolsGroups(elemSchoolsTree, InstitutionSet.ElementarySchoolsList);
		addSchoolsGroups(universityTree, InstitutionSet.getInstitutionsByFilters(InstitutionSet.PostSecondarySchoolsList, new int[] {InstitutionSet.Institution_Subtype}, new String[] {"U"}));
		addSchoolsGroups(collegeTree, InstitutionSet.getInstitutionsByFilters(InstitutionSet.PostSecondarySchoolsList, new int[] {InstitutionSet.Institution_Subtype}, new String[] {"C"}));

		/////////////////////////////////////////////////////////////////////////////////////////////
		// Generate all religious body/temple groups.
		///////////////////////////////////////////////////////////////////////////////////////////
		Tree religionsTree = groupTree.addTree(religionsLabel, false, 0, 0);
		addReligionGroups(religionsTree, TempleSet.getReligionsList(), InstitutionSet.TemplesList);

		/////////////////////////////////////////////////////////////////////////////////////////////
		// Generate all club groups.
		///////////////////////////////////////////////////////////////////////////////////////////
		Tree clubsTree = groupTree.addTree(clubsLabel, false, 0, 0);
		addClubsGroups(clubsTree, Club.getAllClubs());

		/////////////////////////////////////////////////////////////////////////////////////////////
		// Generate all work groups.
		///////////////////////////////////////////////////////////////////////////////////////////
		Tree workTree = groupTree.addTree(workLabel, false, 0, 0);
		addWorkplacesGroups(workTree, Careers.getWorkplaces());
		
		
		
		
		
		//groupTree.displayTree();

		
		
		
		
		//Group res;
		//res = (Group)getItemEfficient(new String[] {religionsLabel}, 0);
		//System.out.println(" ----------------------> " + res.GroupLabel + " (" + res.GroupYear + ") | " + res.GroupID);
				

		
		//Group resG;
		//resG = (Group)getItemEfficient(new String[] {"Schools", "School B"}, 1991);
		

	} // end GroupGenerator() constructor
	
	private static void addSchoolsGroups (Tree tree, ArrayList<ArrayList<Object>> database) {
		int s;
		ArrayList<Object> sch;
		String schName;
		int schStYear;
		int schEndYear;
		
		for (s = 0; s < database.size(); s++) {
			sch = (ArrayList<Object>)database.get(s);
			//schName = (String)sch.get(Schools.School_Name);
			//schStYear = Integer.parseInt((String)sch.get(Schools.School_StYr));
			//schEndYear = Integer.parseInt((String)sch.get(Schools.School_EndYr));
			
			
			schName = (String)sch.get(InstitutionSet.Institution_Name);
			schStYear = Integer.parseInt((String)sch.get(InstitutionSet.Institution_StYr));
			schEndYear = Integer.parseInt((String)sch.get(InstitutionSet.Institution_EndYr));
			tree.addTree(schName, true, schStYear, schEndYear);
		} // end for s (looping through all schools)
		
	}// end addSchoolsGroups()
	
	private static void addWorkplacesGroups (Tree tree, ArrayList<ArrayList<Object>> database) {
		int w;
		ArrayList<Object> work;
		String workName;
		int workStYear;
		int workEndYear;
		
		String instRef;
		ArrayList<ArrayList<Object>> instMatches;
		ArrayList<Object> instMatch;
		Tree linkTree;

		for (w = 0; w < database.size(); w++) {
			work = (ArrayList<Object>)database.get(w);
			//workName = (String)work.get(Careers.Work_ID);
			workName = (String)work.get(Careers.Work_Title);
			workStYear = Integer.parseInt((String)work.get(Careers.Work_YearStarted));
			workEndYear = Integer.parseInt((String)work.get(Careers.Work_YearEnded));

 
			instRef = (String)work.get(Careers.Work_InstitutionRef);
			if (!instRef.isEmpty()) {
				// If there is an institution reference, then just link workplace group to its respective institution.
				//System.out.println(instRef);
				
				
				instMatches = InstitutionSet.getInstitutionsByFilters(InstitutionSet.InstitutionList, new int[] {InstitutionSet.Institution_ID}, new String[] {instRef});
				if (instMatches.size() == 1) {
					// There should always be exactly 1 match when there is an institution reference (can't be multiple institutions with the same ID).
					instMatch = (ArrayList<Object>)instMatches.get(0);
					//System.err.println(instMatch.get(InstitutionSet.Institution_Type) + " -> " + instMatch.get(InstitutionSet.Institution_Subtype) + " -> " + instMatch.get(InstitutionSet.Institution_Name));
					linkTree = GroupGenerator.getSpecificTree(new String[] {(String)instMatch.get(InstitutionSet.Institution_Type), (String)instMatch.get(InstitutionSet.Institution_Subtype), (String)instMatch.get(InstitutionSet.Institution_Name)});
					// Add linked tree as child sub-tree. This tree is simply a reference to the matched institution's tree that already exists!
					tree.addTree(linkTree);
				} else {
					// If there are 0 matches or more than 1 match, then there is a mistake in one of the files. This should never occur!
					System.err.println("In GroupGenerator->addWorkplacesGroups(); there is an institution reference but not exactly 1 match was found. Ensure that the XML files are correct.");
				} // end if (ensure there is exactly 1 matched institution)
				//tree.addTree(workName, true, workStYear, workEndYear);
				
			} else {
				// If there is not an institution reference, then create new group for this workplace.
				tree.addTree(workName, true, workStYear, workEndYear);
			} // end if (check if instRef has a value; i.e. there is a reference to an institution)

			
		} // end for w (looping through all workplaces)
		
	} // end addWorkplacesGroups()
	
	
	private static void addReligionGroups (Tree tree, ArrayList<String> religions, ArrayList<ArrayList<Object>> temples) {
		int t;
		String religion;
		ArrayList<Object> temple;
		String templeName;
		String templeType;
		int templeStYear;
		int templeEndYear;


		for (t = 0; t < religions.size(); t++) {
			religion = (String)religions.get(t);
			tree.addTree(religion, false, 0, 0);
		} // end for t (looping through all religions)

		Tree religionTree;

		for (t = 0; t < temples.size(); t++) {
			temple = (ArrayList<Object>)temples.get(t);
			templeName = (String)temple.get(InstitutionSet.Institution_Name);
			templeType = (String)temple.get(InstitutionSet.Institution_Subtype);
			templeStYear = Integer.parseInt((String)temple.get(InstitutionSet.Institution_StYr));
			templeEndYear = Integer.parseInt((String)temple.get(InstitutionSet.Institution_EndYr));

			religionTree = getSpecificTree(new String[] {religionsLabel, templeType});
			religionTree.addTree(templeName, true, templeStYear, templeEndYear);
		} // end for t (looping through all temples)

	} // end addReligionGroups()
	
	
	

	
	
	private static void addClubsGroups (Tree tree, ArrayList<ArrayList<Object>> database) {
		int c;
		ArrayList<Object> club;
		String clubName;
		int clubStYear;
		int clubEndYear;
		
		for (c = 0; c < database.size(); c++) {
			club = (ArrayList<Object>)database.get(c);
			clubName = (String)club.get(Club.Club_Name);
			clubStYear = Integer.parseInt((String)club.get(Club.Club_StYr));
			clubEndYear = Integer.parseInt((String)club.get(Club.Club_EndYr));


			// TODO The second parameter indicates whether or not there is a timeline. For now, use False here so people aren't in clubs for certain years but just
			// all the time! This will have to change eventually.
			tree.addTree(clubName, false, clubStYear, clubEndYear);
			//tree.addTree(clubName, true, clubStYear, clubEndYear);
			
		} // end for c (looping through all clubs)
		
	} // end addSchoolsGroups()
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static Tree getSpecificTree(String[] nodePath) {
		// Default to entire groupTree, and then it will be re-assigned to sub-trees at each new depth in the sub-trees.
		Tree tree = groupTree;
		int pathLength = nodePath.length;
		int d = 0;

		while (d < pathLength && tree != null) {
			tree = tree.getChildByName(nodePath[d]);
			d++;
		} // end while (traversing down tree according to the given labels)
			
		return tree;
	} // end getSpecificTree()
	
	
	
	
	
	
	public static Group getItemEfficient(String[] nodePath) {
		// Default to entire groupTree, and then it will be re-assigned to sub-trees at each new depth in the sub-trees.
		Tree tree = (Tree)getSpecificTree(nodePath);
		
		// If tree is null, display an error message.
		if (tree == null) {
			System.err.println("Attempting to getItemEfficient in GroupGenerator, but tree is null.   " + nodePath[1]);
			return null;
		}
		
		// Get the list of groups in this tree; but it should only ever have 1 group because there is no timeline attached (otherwise, the overloaded method should be called with the year parameter)
		Group[] treeGroups = tree.groupsAtNode;

		
		// If the specific year is not found, then return null.
		return treeGroups[0]; // There should only ever be the 1 group here because this is for non-timelined group trees.
	} // end getItemEfficient()
	
	
	
	public static Group getItemEfficient(String[] nodePath, int year) {
		// Default to entire groupTree, and then it will be re-assigned to sub-trees at each new depth in the sub-trees.
		Tree tree = (Tree)getSpecificTree(nodePath);
		
		// If tree is null, display an error message.
		if (tree == null) {
			System.err.println("Attempting to getItemEfficient in GroupGenerator, but tree is null.   " + nodePath[1] + "  " + year);
			return null;
		}
		
		// Loop through all the years in the tree's timeline of groups and search for the one that matches the given year.
		Group[] treeGroups = tree.groupsAtNode;
		int numGroups = treeGroups.length;
		int g;
		
		for (g = 0; g < numGroups; g++) {
			if (treeGroups[g].GroupYear == year) {
				return treeGroups[g];
			} // end if (checking for match in year)
		} // end for g (looping through groups in the timeline of given tree)
		
		// If the specific year is not found, then return null.
		return null;
		
	} // end getItemEfficient()
	
	public static Group getItemEfficient(Tree tree, int year) {
		
		// If tree is null, display an error message.
		if (tree == null) {
			System.err.println("Attempting to getItemEfficient in GroupGenerator, but tree is null.");
			return null;
		}
		
		// Loop through all the years in the tree's timeline of groups and search for the one that matches the given year.
		Group[] treeGroups = tree.groupsAtNode;
		int numGroups = treeGroups.length;
		int g;
		
		for (g = 0; g < numGroups; g++) {
			if (treeGroups[g].GroupYear == year) {
				return treeGroups[g];
			} // end if (checking for match in year)
		} // end for g (looping through groups in the timeline of given tree)
		
		// If the specific year is not found, then return null.
		return null;
		
	} // end getItemEfficient()
	
	
	
	
	public static void DestroyGroups () {
		
		groupTree.DestroyAllNodes(0);
		groupTree = null;
		
	} // end DestroyGroups()


} // end GroupGenerator class