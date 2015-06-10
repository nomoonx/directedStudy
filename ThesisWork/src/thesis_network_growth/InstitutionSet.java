package thesis_network_growth;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;




public class InstitutionSet {
	
	// Global static arrays.
	public static ArrayList<ArrayList<Object>> InstitutionList;
	public static ArrayList<ArrayList<Object>> ElementarySchoolsList;
	public static ArrayList<ArrayList<Object>> PostSecondarySchoolsList;
	public static ArrayList<ArrayList<Object>> TemplesList;
	
	// Local array for each class instance containing the elements of its own type.
	ArrayList<ArrayList<Object>> instanceInstitutionList;
	
	// Array index indicators.
	public static int Institution_ID = 0;
	public static int Institution_Name = 1;
	public static int Institution_Type = 2;
	public static int Institution_Subtype = 3;
	public static int Institution_City = 4;
	public static int Institution_Pop = 5; 
	public static int Institution_StYr = 6;
	public static int Institution_EndYr = 7;
	
	
	public static void loadAllInstitutions () {
		InstitutionList = new ArrayList<ArrayList<Object>>();
		InstitutionSet allElemSchools = new SchoolSet("Schools.xml", "school", GroupGenerator.schoolsLabel);
		InstitutionSet allPSSchools = new SchoolSet("PostSecondarySchools.xml", "school", GroupGenerator.schoolsLabel);
		InstitutionSet allTemples = new TempleSet("Temples.xml", "temple", GroupGenerator.religionsLabel);

		ElementarySchoolsList = allElemSchools.getInstitutionList();
		PostSecondarySchoolsList = allPSSchools.getInstitutionList();
		TemplesList = allTemples.getInstitutionList();
		
		addToInstitutionsList(allElemSchools.getInstitutionList());
		addToInstitutionsList(allPSSchools.getInstitutionList());
		addToInstitutionsList(allTemples.getInstitutionList());
	}
	
	public static void addToInstitutionsList (ArrayList<ArrayList<Object>> list) {
		// This function adds the given sub-list, list, of schools, temples, etc. to the big global array, AllInstitutions which contains all of those.
		// param list: arraylist of a set of institutions, such as elementary schools, post-secondary schools, or temples, etc.

		int i;
		for (i = 0; i < list.size(); i++) {
			InstitutionList.add(list.get(i));
		} // end for

	} // end addToInstitutionsList()
	
	
	
	// --------------------------------------------------------------------------------------------
	// InstitutionSet constructor
	// --------------------------------------------------------------------------------------------
	public InstitutionSet (String filepath, String elementName, String treeRootName) {
		try {
			instanceInstitutionList = loadFromFile(filepath, elementName, treeRootName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // end InstitutionSet()
	
	
	public ArrayList<ArrayList<Object>> getInstitutionList () {
		return instanceInstitutionList;
	} // end getInstitutionList()
	
	
	
	private ArrayList<ArrayList<Object>> loadFromFile (String filepath, String elementName, String treeRootName) throws Exception {
		// Load the list of schools and related info (title, p_attend, etc.) from the XML file. Note that p_attend is the probability in [0,1]
		// that a student will attend this school. The sum of all schools' p_attend should equal 1.0.
		// param filepath: The string of the file name, assuming the file is in the root directory of this project.
		// returns: arraylist containing arraylists for each of the career's information
		File file = new File(filepath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbFactory.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();

		// ----------------------------------------------------------------------------------------------------
		// Get all school nodes.
		// ----------------------------------------------------------------------------------------------------
		NodeList demoList = doc.getElementsByTagName(elementName);

		int i, d;
		ArrayList<ArrayList<Object>> AllInstInSet = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> InstitutionInfo;
		String tmp;

		// For each of the main elements within the XML file.
		for (d = 0; d < demoList.getLength(); d++) {

			InstitutionInfo = new ArrayList<Object>();
			
			// --------------------------------------------------
			// Get section root.
			// --------------------------------------------------
			Node demoNode = demoList.item(d);							// Get main element as Node.
			Element demoElement = (Element)demoNode;					// Convert to Element.

			NodeList InstInfo;

			// ----- ID -----
			InstInfo = demoElement.getElementsByTagName("id");			// Extract component as NodeList.
			for (i = 0; i < InstInfo.getLength(); i++) {
				Element demoRaceCatElement = (Element)InstInfo.item(i);	// Convert to element.
				NodeList nodeList = demoRaceCatElement.getChildNodes();
				InstitutionInfo.add(Institution_ID, nodeList.item(0).getNodeValue());			// Add this institution info to the array for this institution.
			} // end i (loop through elements)

			// ----- Title -----
			InstInfo = demoElement.getElementsByTagName("title");		// Extract component as NodeList.
			for (i = 0; i < InstInfo.getLength(); i++) {
				Element demoRaceCatElement = (Element)InstInfo.item(i);	// Convert to element.
				NodeList nodeList = demoRaceCatElement.getChildNodes();
				InstitutionInfo.add(Institution_Name, nodeList.item(0).getNodeValue());			// Add this institution info to the array for this institution.
			} // end i (loop through elements)

			// ----- Type -----
			InstitutionInfo.add(Institution_Type, treeRootName);
			
			// ----- Subtype -----
			InstInfo = demoElement.getElementsByTagName("type");			// Extract component as NodeList.
			for (i = 0; i < InstInfo.getLength(); i++) {
				Element demoRaceCatElement = (Element)InstInfo.item(i);	// Convert to element.
				NodeList nodeList = demoRaceCatElement.getChildNodes();
				InstitutionInfo.add(Institution_Subtype, nodeList.item(0).getNodeValue());			// Add this institution info to the array for this institution.
			} // end i (loop through elements)

			// ----- City -----
			InstInfo = demoElement.getElementsByTagName("city");			// Extract component as NodeList.
			for (i = 0; i < InstInfo.getLength(); i++) {
				Element demoRaceCatElement = (Element)InstInfo.item(i);	// Convert to element.
				NodeList nodeList = demoRaceCatElement.getChildNodes();
				InstitutionInfo.add(Institution_City, nodeList.item(0).getNodeValue());			// Add this institution info to the array for this institution.
			} // end i (loop through elements)

			// ----- Population -----
			InstInfo = demoElement.getElementsByTagName("pop");			// Extract component as NodeList.
			for (i = 0; i < InstInfo.getLength(); i++) {
				Element demoRaceCatElement = (Element)InstInfo.item(i);	// Convert to element.
				NodeList nodeList = demoRaceCatElement.getChildNodes();
				InstitutionInfo.add(Institution_Pop, nodeList.item(0).getNodeValue());			// Add this institution info to the array for this institution.
			} // end i (loop through elements)

			// ----- Year Started -----
			InstInfo = demoElement.getElementsByTagName("yearStarted");	// Extract component as NodeList.
			for (i = 0; i < InstInfo.getLength(); i++) {
				Element demoRaceCatElement = (Element)InstInfo.item(i);	// Convert to element.
				NodeList nodeList = demoRaceCatElement.getChildNodes();
				InstitutionInfo.add(Institution_StYr, nodeList.item(0).getNodeValue());			// Add this institution info to the array for this institution.
			} // end i (loop through elements)

			// ----- Year Ended -----
			InstInfo = demoElement.getElementsByTagName("yearEnded");	// Extract component as NodeList.
			for (i = 0; i < InstInfo.getLength(); i++) {
				Element demoRaceCatElement = (Element)InstInfo.item(i);	// Convert to element.
				NodeList nodeList = demoRaceCatElement.getChildNodes();
				tmp = nodeList.item(0).getNodeValue();

				// Determine actual endYear
				if (tmp.equals("-")) {
					// If a "-" is given in file, then school is currently running, so set endYear as society's current year.
					// EDIT: Rather than current year, use the MaxYear here. This is so in the simulation, they are initialized beyond the current year.
					InstitutionInfo.add(Institution_EndYr, String.valueOf(Configuration.MaxYear));	// Add this school info to the array for this school.
				} else {
					// Parse int year given in file.
					InstitutionInfo.add(Institution_EndYr, nodeList.item(0).getNodeValue());			// Add this school info to the array for this school.
				} // end if (checking if endYear == "-")

				
			} // end i (loop through race elements)



			
			
			// Add this school array to large array.
			AllInstInSet.add(InstitutionInfo);

		} // end d (demographics loop)

		return AllInstInSet;
	} // end loadCareers()
	
	
	
	public static ArrayList<ArrayList<Object>> getInstitutionsByFilters (ArrayList<ArrayList<Object>> database, int[] keys, Object[] filtVals) {
		// Find which post-secondary school(s) are of the given filters, as given in the keys and filtVals parameters.
		// Note that these parameters are parallel arrays and cannot be rearranged, because the keys correspond to the values to check for.
		//
		// 
		// returns: an array of the schools that match the criteria - these each contain all the school information

		if (keys.length != filtVals.length) {
			System.err.println("In InstitutionSet->getSchoolsByFilters(), keys and filtVals must be the same length.");
			return null;
		} // end if (check if keys and filtVals are different lengths - and thus invalid parameters)

		ArrayList<ArrayList<Object>> institutionNames = new ArrayList<ArrayList<Object>>();
		int s;
		int c;
		ArrayList<Object> institution;
		boolean conditionsMet = true;

		for (s = 0; s < database.size(); s++) {
			conditionsMet = true;
			institution = (ArrayList<Object>)database.get(s);
			
			// Loop through all conditions and if ALL conditions are met, then add school to array.
			for (c = 0; c < keys.length; c++) {
				
				// Check if school matches condition c.
				if (!institution.get(keys[c]).equals(filtVals[c])) {
					conditionsMet = false;
				} // end if (condition is met at index c, as given in keys and filtVals parameters)
				
			} // end for c (loop through all conditions in keys and filtVals arrays)



			// If, at this point, the conditionsMet flag is True, then all conditions must be true, so add school to return array.
			if (conditionsMet) {
				institutionNames.add(institution);
			} // end if (all conditions are met)



		} // end for s (loop through all post secondary schools)
		return institutionNames;
	} // end getInstitutionsByFilters()
	
	
	
	
	public static ArrayList<String> getInstitutionsByFilters (ArrayList<ArrayList<Object>> database, int[] keys, Object[] filtVals, int[] attributes) {
		// Find which post-secondary school(s) are of the given filters, as given in the keys and filtVals parameters.
		// Note that these parameters are parallel arrays and cannot be rearranged, because the keys correspond to the values to check for.
		//
		// 
		// returns: an array of the schools that match the criteria - these each contain the school attributes as indicated in the attributes parameter 

		if (keys.length != filtVals.length) {
			System.err.println("In InstitutionSet->getSchoolsByFilters(), keys and filtVals must be the same length.");
			return null;
		} // end if (check if keys and filtVals are different lengths - and thus invalid parameters)

		//ArrayList<ArrayList<String>> returnInstMatches = new ArrayList<ArrayList<String>>();
		ArrayList<String> institutionAttr = new ArrayList<String>();
		int s;
		int c;
		int a;
		ArrayList<Object> institution;
		boolean conditionsMet = true;

		for (s = 0; s < database.size(); s++) {
			conditionsMet = true;
			institution = (ArrayList<Object>)database.get(s);

			// Loop through all conditions and if ALL conditions are met, then add school to array.
			for (c = 0; c < keys.length; c++) {

				// Check if school matches condition c.
				if (!institution.get(keys[c]).equals(filtVals[c])) {
					conditionsMet = false;
				} // end if (condition is met at index c, as given in keys and filtVals parameters)

			} // end for c (loop through all conditions in keys and filtVals arrays)



			// If, at this point, the conditionsMet flag is True, then all conditions must be true, so add school to return array.
			if (conditionsMet) {
				for (a = 0; a < attributes.length; a++) {
					institutionAttr.add((String)institution.get(attributes[a]));
				} // end for a (loop through desired attributes to be added to array)
				//returnInstMatches.add(institutionAttr);
			} // end if (all conditions are met)

		} // end for s (loop through all post secondary schools)

		return institutionAttr;
	} // end getInstitutionsByFilters()
	
	
	public static String getSchoolTitleAt (ArrayList<ArrayList<Object>> database, int index) {
		// Return the title of the school at the given index.
		// param database: the arraylist of the set of institutions of a certain type from which to search (i.e. elementarySchools, temples, etc.)
		// param index: an integer index of a particular institution
		// returns: the string of the institution's title
		
		ArrayList<Object> institution = (ArrayList<Object>)database.get(index);
		return (String)institution.get(Institution_Name);
	}

	
} // end InstitutionSet class
