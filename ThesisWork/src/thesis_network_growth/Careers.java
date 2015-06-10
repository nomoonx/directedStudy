package thesis_network_growth;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class Careers {
	
	static ArrayList[] AllPersonalityCategories;
	static ArrayList<ArrayList<Object>> AllCareers;
	static ArrayList<ArrayList<Object>> AllWorkplaces;


	// All Careers attributes
	public static int Career_ID = 0;
	public static int Career_Title = 1;
	public static int Career_Salary = 2;
	public static int Career_Percent = 3;
	public static int Career_ReqEducation = 4;
	public static int Career_ReqSchoolYears = 5;
	public static int Career_Traits = 6;
	public static int Career_Workplaces = 7;	// Added later after Workplaces file is loaded in.

	// Career Table attributes
	public static int CareerTable_ID = 0;
	public static int CareerTable_Num = 1;

	// Workplaces attributes
	public static int Work_ID = 0;
	public static int Work_Title = 1;
	public static int Work_City = 2;
	public static int Work_Careers = 3;
	public static int Work_YearStarted = 4;
	public static int Work_YearEnded = 5;
	public static int Work_InstitutionRef = 6;

	public Careers () {

	}
	
	public static void loadFiles () {
		
		try {
			//AllCareers = loadCareers("Careers.xml");
			AllCareers = loadCareers2("CareersNew.xml");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//try {
			//AllPersonalityCategories = loadPersonalityCareers("PersonalityCareers.xml");
		//} catch (Exception e) {
			//e.printStackTrace();
		//}
		
		try {
			AllWorkplaces = loadWorkplaces("Workplaces.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		addWorkplacesToCareers();
		
		
		int i;
		for (i = 0; i < AllWorkplaces.size(); i++) {
			//DebugTools.printInlineArray((ArrayList)AllWorkplaces.get(i));
			//System.out.println();
		}
		
		/*
		System.out.println("----------------------------------------------------------------");
		ArrayList careerInfo;
		careerInfo = getCareerById(AllCareers, "15304");
		System.out.println(careerInfo);
		DebugTools.printArray(getWorkplaceById("15304"));
		careerInfo = getCareerById(AllCareers, "74793");
		System.out.println(careerInfo);
		DebugTools.printArray(getWorkplaceById("74793"));
		careerInfo = getCareerById(AllCareers, "58371");
		System.out.println(careerInfo);
		DebugTools.printArray(getWorkplaceById("58371"));
		careerInfo = getCareerById(AllCareers, "42758");
		System.out.println(careerInfo);
		DebugTools.printArray(getWorkplaceById("42758"));
		*/
		//DebugTools.printArray(careerInfo);
		
		
		/*
		ArrayList arr;
		arr = (ArrayList)AllWorkplaces.get(0);
		DebugTools.printArray((String[])arr.get(3));
		arr = (ArrayList)AllWorkplaces.get(1);
		DebugTools.printArray((String[])arr.get(3));
		arr = (ArrayList)AllWorkplaces.get(2);
		DebugTools.printArray((String[])arr.get(3));
		*/
	}

	
	public static ArrayList<ArrayList<Object>> getFullCareersDatabase () {
		return AllCareers;
	}
	public static ArrayList[] getPersonalityCareers () {
		return AllPersonalityCategories;
	}
	public static ArrayList<ArrayList<Object>> getWorkplaces () {
		return AllWorkplaces;
	}

	
	
	
	
	
	
	public static ArrayList<ArrayList<Object>> loadCareers (String filepath) throws Exception {
		// Load the list of careers and related info (salary, etc.) from the XML file.
		// param filepath: The string of the file name, assuming the file is in the root directory of this project.
		// returns: arraylist containing arraylists for each of the career's information
		File file = new File(filepath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbFactory.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();

		// ----------------------------------------------------------------------------------------------------
		// Get all career nodes.
		// ----------------------------------------------------------------------------------------------------
		NodeList demoList = doc.getElementsByTagName("career");

		int i, d;
		ArrayList<ArrayList<Object>> AllCareers = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> Career;
		Node demoNode;
		Element demoElement;
		NodeList CarInfo;
		Element demoRaceCatElement;
		NodeList nodeList;
		Element trait;
		
		String traitType;
		String traitTypeValue;
		String traitReq;
		NodeList traitList;
		ArrayList<String[]> traitSet;
		String[] traitPair;

		for (d = 0; d < demoList.getLength(); d++) {

			Career = new ArrayList<Object>();
			
			// --------------------------------------------------
			// Get section root.
			// --------------------------------------------------
			demoNode = demoList.item(d);										// Get demographics element as Node.
			demoElement = (Element)demoNode;								// Convert to Element.

			// ----- ID -----
			CarInfo = demoElement.getElementsByTagName("id");			// Extract career component as NodeList.
			for (i = 0; i < CarInfo.getLength(); i++) {
				demoRaceCatElement = (Element)CarInfo.item(i);	// Convert to element.
				nodeList = demoRaceCatElement.getChildNodes();
				Career.add(nodeList.item(0).getNodeValue());			// Add this career info to the array for this career.
			} // end i (loop through race elements)

			// ----- Title -----
			CarInfo = demoElement.getElementsByTagName("title");		// Extract career component as NodeList.
			for (i = 0; i < CarInfo.getLength(); i++) {
				demoRaceCatElement = (Element)CarInfo.item(i);	// Convert to element.
				nodeList = demoRaceCatElement.getChildNodes();
				Career.add(nodeList.item(0).getNodeValue());			// Add this career info to the array for this career.
			} // end i (loop through race elements)

			// ----- Salary Mean -----
			CarInfo = demoElement.getElementsByTagName("salary_mean");	// Extract career component as NodeList.
			for (i = 0; i < CarInfo.getLength(); i++) {
				demoRaceCatElement = (Element)CarInfo.item(i);	// Convert to element.
				nodeList = demoRaceCatElement.getChildNodes();
				Career.add(nodeList.item(0).getNodeValue());			// Add this career info to the array for this career.
			} // end i (loop through race elements)

			// ----- Num Percent -----
			CarInfo = demoElement.getElementsByTagName("num_percent");	// Extract career component as NodeList.
			for (i = 0; i < CarInfo.getLength(); i++) {
				demoRaceCatElement = (Element)CarInfo.item(i);	// Convert to element.
				nodeList = demoRaceCatElement.getChildNodes();
				Career.add(nodeList.item(0).getNodeValue());			// Add this career info to the array for this career.
			} // end i (loop through race elements)
			
			// ----- Required Education -----
			CarInfo = demoElement.getElementsByTagName("education");	// Extract career component as NodeList.
			for (i = 0; i < CarInfo.getLength(); i++) {
				demoRaceCatElement = (Element)CarInfo.item(i);	// Convert to element.
				nodeList = demoRaceCatElement.getChildNodes();
				Career.add(nodeList.item(0).getNodeValue());			// Add this career info to the array for this career.
			} // end i (loop through race elements)

			// ----- Required Num Years In P.S. School -----
			CarInfo = demoElement.getElementsByTagName("num_year_post_secondary");	// Extract career component as NodeList.
			for (i = 0; i < CarInfo.getLength(); i++) {
				demoRaceCatElement = (Element)CarInfo.item(i);	// Convert to element.
				nodeList = demoRaceCatElement.getChildNodes();
				Career.add(nodeList.item(0).getNodeValue());			// Add this career info to the array for this career.
			} // end i (loop through race elements)

			// ----- Traits -----
			traitList = demoElement.getElementsByTagName("trait");			// Extract component as NodeList.
			traitSet = new ArrayList<String[]>();
						
			for (i = 0; i < traitList.getLength(); i++) {
				trait = (Element)traitList.item(i);
				traitType = trait.getAttribute("type");						// Get attribute "type".
				traitTypeValue = trait.getFirstChild().getNodeValue();		// Get value.
				if (trait.hasAttribute("req")) {
					traitReq = trait.getAttribute("req");					// Get attribute "req".
				} else {
					traitReq = "";
				} // end if (check if club has "req" attribute)
				// Add both the type and value to a string array and add that array to the career's arraylist.
				traitPair = new String[] {traitType, traitTypeValue, traitReq};
				traitSet.add(traitPair);									// Add this info to the array for this element.
			} // end i (loop through elements)
			Career.add(traitSet);
			
			// ----- Workplaces (create an empty arraylist now, workplaces will be added later) -----
			Career.add(new ArrayList<String>());

			
			// Add this career array to large array.
			//DebugTools.printArray(Career);
			AllCareers.add(Career);

		} // end d (demographics loop)

		return AllCareers;
	} // end loadCareers()
	
	
	
	
	
	
	
	
	
	public static ArrayList<ArrayList<Object>> loadCareers2 (String filepath) throws Exception {
		// Load the list of careers and related info (salary, etc.) from the XML file.
		// param filepath: The string of the file name, assuming the file is in the root directory of this project.
		// returns: arraylist containing arraylists for each of the career's information
		File file = new File(filepath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbFactory.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();

		// ----------------------------------------------------------------------------------------------------
		// Get all career nodes.
		// ----------------------------------------------------------------------------------------------------
		NodeList demoList = doc.getElementsByTagName("career");

		int i, d;
		ArrayList<ArrayList<Object>> AllCareers = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> Career;
		Node demoNode;
		Element demoElement;
		NodeList CarInfo;
		Element demoRaceCatElement;
		NodeList nodeList;
		Element trait;
		
		String traitType;
		String traitTypeValue;
		String traitReq;
		NodeList traitList;
		ArrayList<String[]> traitSet;
		String[] traitPair;

		for (d = 0; d < demoList.getLength(); d++) {

			Career = new ArrayList<Object>();
			
			// --------------------------------------------------
			// Get section root.
			// --------------------------------------------------
			demoNode = demoList.item(d);										// Get demographics element as Node.
			demoElement = (Element)demoNode;								// Convert to Element.

			// ----- ID -----
			CarInfo = demoElement.getElementsByTagName("id");			// Extract career component as NodeList.
			for (i = 0; i < CarInfo.getLength(); i++) {
				demoRaceCatElement = (Element)CarInfo.item(i);	// Convert to element.
				nodeList = demoRaceCatElement.getChildNodes();
				Career.add(nodeList.item(0).getNodeValue());			// Add this career info to the array for this career.
			} // end i (loop through race elements)

			// ----- Title -----
			CarInfo = demoElement.getElementsByTagName("title");		// Extract career component as NodeList.
			for (i = 0; i < CarInfo.getLength(); i++) {
				demoRaceCatElement = (Element)CarInfo.item(i);	// Convert to element.
				nodeList = demoRaceCatElement.getChildNodes();
				Career.add(nodeList.item(0).getNodeValue());			// Add this career info to the array for this career.
			} // end i (loop through race elements)

			// ----- Salary Mean -----
			CarInfo = demoElement.getElementsByTagName("salary_mean");	// Extract career component as NodeList.
			for (i = 0; i < CarInfo.getLength(); i++) {
				demoRaceCatElement = (Element)CarInfo.item(i);	// Convert to element.
				nodeList = demoRaceCatElement.getChildNodes();
				Career.add(nodeList.item(0).getNodeValue());			// Add this career info to the array for this career.
			} // end i (loop through race elements)

			// ----- Num Percent -----
			CarInfo = demoElement.getElementsByTagName("num_percent");	// Extract career component as NodeList.
			for (i = 0; i < CarInfo.getLength(); i++) {
				demoRaceCatElement = (Element)CarInfo.item(i);	// Convert to element.
				nodeList = demoRaceCatElement.getChildNodes();
				Career.add(nodeList.item(0).getNodeValue());			// Add this career info to the array for this career.
			} // end i (loop through race elements)
			
			// ----- Required Education -----
			CarInfo = demoElement.getElementsByTagName("education");	// Extract career component as NodeList.
			for (i = 0; i < CarInfo.getLength(); i++) {
				demoRaceCatElement = (Element)CarInfo.item(i);	// Convert to element.
				nodeList = demoRaceCatElement.getChildNodes();
				Career.add(nodeList.item(0).getNodeValue());			// Add this career info to the array for this career.
			} // end i (loop through race elements)

			// ----- Required Num Years In P.S. School -----
			CarInfo = demoElement.getElementsByTagName("num_year_post_secondary");	// Extract career component as NodeList.
			for (i = 0; i < CarInfo.getLength(); i++) {
				demoRaceCatElement = (Element)CarInfo.item(i);	// Convert to element.
				nodeList = demoRaceCatElement.getChildNodes();
				Career.add(nodeList.item(0).getNodeValue());			// Add this career info to the array for this career.
			} // end i (loop through race elements)

			// ----- Traits -----
			traitList = demoElement.getElementsByTagName("trait");			// Extract component as NodeList.
			traitSet = new ArrayList<String[]>();
						
			for (i = 0; i < traitList.getLength(); i++) {
				trait = (Element)traitList.item(i);
				traitType = trait.getAttribute("type");						// Get attribute "type".
				traitTypeValue = trait.getFirstChild().getNodeValue();		// Get value.
				if (trait.hasAttribute("req")) {
					traitReq = trait.getAttribute("req");					// Get attribute "req".
				} else {
					traitReq = "";
				} // end if (check if club has "req" attribute)
				// Add both the type and value to a string array and add that array to the career's arraylist.
				traitPair = new String[] {traitType, traitTypeValue, traitReq};
				traitSet.add(traitPair);									// Add this info to the array for this element.
			} // end i (loop through elements)
			Career.add(traitSet);
			
			// ----- Workplaces (create an empty arraylist now, workplaces will be added later) -----
			Career.add(new ArrayList<String>());

			
			// Add this career array to large array.
			//DebugTools.printArray(Career);
			AllCareers.add(Career);

		} // end d (demographics loop)

		return AllCareers;
	} // end loadCareers()
	
	
	
	
	
	
	
	
	
	public static ArrayList[] loadPersonalityCareers (String filepath) throws Exception {
		File file = new File(filepath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbFactory.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();

		// ----------------------------------------------------------------------------------------------------
		// Get personality categories.
		// ----------------------------------------------------------------------------------------------------
		NodeList demoList = doc.getElementsByTagName("personality");

		int i, d;

		ArrayList[] AllCategories = new ArrayList[16];				// This is assuming 2^4 possible personality types!
		ArrayList personalityCategory;

		for (d = 0; d < demoList.getLength(); d++) {

			personalityCategory = new ArrayList();

			// --------------------------------------------------
			// Get section root.
			// --------------------------------------------------
			Node demoNode = demoList.item(d);										// Get demographics element as Node.
			Element demoElement = (Element)demoNode;								// Convert to Element.

			String personalityType = demoElement.getAttribute("id");
			int mbti_index = convertPersonalityToBinIndex(personalityType);

			// --------------------------------------------------
			// Extract 'career' component.
			// --------------------------------------------------
			NodeList demoRaceCat = demoElement.getElementsByTagName("career");		// Extract career component as NodeList.

			for (i = 0; i < demoRaceCat.getLength(); i++) {
				Element demoRaceCatElement = (Element)demoRaceCat.item(i);				// Convert to element.
				// Add this career to the array for this personality category.
				personalityCategory.add(demoRaceCatElement.getAttribute("id"));

			} // end i (loop through race elements)

			// Add this personality-career category array to large array.
			AllCategories[mbti_index] = personalityCategory;

		} // end d (demographics loop)

		return AllCategories;
	} // end loadPersonalityCareers()
	
	public static ArrayList loadWorkplaces (String filepath) throws Exception {
		// Load the list of workplaces and related info (careers involved, city, etc.) from the XML file.
		// param filepath: The string of the file name, assuming the file is in the root directory of this project.
		// returns: arraylist containing arraylists for each of the workplace's information
		File file = new File(filepath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbFactory.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();

		// ----------------------------------------------------------------------------------------------------
		// Get all workplace nodes.
		// ----------------------------------------------------------------------------------------------------
		NodeList demoList = doc.getElementsByTagName("place");

		int i, d;
		String tmpCareers;
		String[] careersList;
		ArrayList AllWorkplaces = new ArrayList();
		ArrayList Workplace;
		String tmp;
		String tmpTitle = "";
		String workplaceInstitution = "";

		for (d = 0; d < demoList.getLength(); d++) {

			Workplace = new ArrayList();
			
			// --------------------------------------------------
			// Get section root.
			// --------------------------------------------------
			Node demoNode = demoList.item(d);										// Get demographics element as Node.
			Element demoElement = (Element)demoNode;								// Convert to Element.

			NodeList CarInfo;

			// ----- ID -----
			CarInfo = demoElement.getElementsByTagName("id");			// Extract career component as NodeList.
			for (i = 0; i < CarInfo.getLength(); i++) {
				Element demoRaceCatElement = (Element)CarInfo.item(i);	// Convert to element.
				NodeList nodeList = demoRaceCatElement.getChildNodes();
				Workplace.add(nodeList.item(0).getNodeValue());			// Add this career info to the array for this career.
			} // end i (loop through race elements)

			// ----- Title -----
			CarInfo = demoElement.getElementsByTagName("title");		// Extract career component as NodeList.
			for (i = 0; i < CarInfo.getLength(); i++) {
				Element demoRaceCatElement = (Element)CarInfo.item(i);	// Convert to element.
				workplaceInstitution = demoRaceCatElement.getAttribute("inst");		// Get attribute for institution referencing (which is performed below).
				NodeList nodeList = demoRaceCatElement.getChildNodes();
				tmpTitle = nodeList.item(0).getNodeValue(); // String of workplace title.
				Workplace.add(tmpTitle);			// Add this career info to the array for this career.
			} // end i (loop through race elements)

			// ----- City -----
			CarInfo = demoElement.getElementsByTagName("city");			// Extract career component as NodeList.
			for (i = 0; i < CarInfo.getLength(); i++) {
				Element demoRaceCatElement = (Element)CarInfo.item(i);	// Convert to element.
				NodeList nodeList = demoRaceCatElement.getChildNodes();
				Workplace.add(nodeList.item(0).getNodeValue());			// Add this career info to the array for this career.
			} // end i (loop through race elements)

			// ----- Career IDs In This Workplace -----
			CarInfo = demoElement.getElementsByTagName("careers");	// Extract career component as NodeList.
			for (i = 0; i < CarInfo.getLength(); i++) {
				Element demoRaceCatElement = (Element)CarInfo.item(i);	// Convert to element.
				NodeList nodeList = demoRaceCatElement.getChildNodes();
				tmpCareers = nodeList.item(0).getNodeValue();
				careersList = tmpCareers.split(",");
				Workplace.add(careersList);			// Add this career info to the array for this career.
			} // end i (loop through race elements)
			
			// ----- Year Workplace Started -----
			CarInfo = demoElement.getElementsByTagName("yearStarted");	// Extract career component as NodeList.
			for (i = 0; i < CarInfo.getLength(); i++) {
				Element demoRaceCatElement = (Element)CarInfo.item(i);	// Convert to element.
				NodeList nodeList = demoRaceCatElement.getChildNodes();
				Workplace.add(nodeList.item(0).getNodeValue());			// Add this career info to the array for this career.
			} // end i (loop through race elements)

			// ----- Year Workplace Ended -----
			CarInfo = demoElement.getElementsByTagName("yearEnded");	// Extract career component as NodeList.
			for (i = 0; i < CarInfo.getLength(); i++) {
				Element demoRaceCatElement = (Element)CarInfo.item(i);	// Convert to element.
				NodeList nodeList = demoRaceCatElement.getChildNodes();
				tmp = nodeList.item(0).getNodeValue();

				// Determine actual endYear
				if (tmp.equals("-")) {
					// If a "-" is given in file, then school is currently running, so set endYear as society's current year.
					Workplace.add(String.valueOf(Configuration.MaxYear));	// Add this school info to the array for this school.
				} else {
					// Parse int year given in file.
					Workplace.add(nodeList.item(0).getNodeValue());			// Add this school info to the array for this school.
				} // end if (checking if endYear == "-")
			} // end i (loop through race elements)

			
			// Link workplace to institution.
			if (!workplaceInstitution.isEmpty()) {
				linkWorkplaceToInstitution(Workplace, workplaceInstitution, tmpTitle);
			} else {
				Workplace.add("");			// No reference to an institution.
			} // end if (workplace is linked to an institution (school, temple, etc.))
			
			// ----- Institution Ref (it will be an ID reference to the institution it represents, or "" if not applicable) -----
			
			
			// Add this career array to large array.
			//DebugTools.printArray(Career);
			AllWorkplaces.add(Workplace);

		} // end d (demographics loop)

		return AllWorkplaces;
	} // end loadWorkplaces()
	
	
	
	
	private static void linkWorkplaceToInstitution(ArrayList workplace, String instType, String workTitle) {
		// This function links a given workplace entry from the XML file to an institution (school, temple, etc.).
		
		ArrayList<String> matchResults = null;
		
		
		
		// TODO It may be more efficient to look at the instType and then examine the type-based institution lists based on that instType (i.e. list of temples rather than entire list of ALL institutions).
		matchResults = InstitutionSet.getInstitutionsByFilters(InstitutionSet.InstitutionList, new int[] {InstitutionSet.Institution_Name}, new String[] {workTitle}, new int[] {InstitutionSet.Institution_ID});		
		
		
		

		
		//System.out.println(workTitle + "  " + instType);
		
		
		/*
		if (instType.equals("school")) {
			matchResults = Schools.getSchoolsByFilters(Schools.AllElementarySchools, new int[] {Schools.School_Name}, new String[] {workTitle}, new int[] {Schools.School_ID});
		} else if (instType.equals("PSschool")) {
			matchResults = Schools.getSchoolsByFilters(Schools.AllPostSecondarySchools, new int[] {Schools.School_Name}, new String[] {workTitle}, new int[] {Schools.School_ID});
		} else if (instType.equals("temple")) {
			
		} else {
			System.err.println("In Careers->linkWorkplaceToInstitution(), attempting to link workplace to institution, but type is unknown: " + instType);
		}
		*/
		
		if (matchResults == null || matchResults.isEmpty()) {
			System.err.println("In Careers->linkWorkplaceToInstitution(), found no match for '" + workTitle + "' in the category '" + instType + "'. Make sure the titles in the files match exactly!");
		} else {
			if (matchResults.size() == 1) {
				//System.out.println(matchResults.size() + " | " + matchResults.get(0));
				workplace.add(Work_InstitutionRef, (String)matchResults.get(0));
			} else {
				System.err.println("In Careers->linkWorkplaceToInstitution(), found multiple institution references for '" + workTitle + "' in the category '" + instType + "'. There should be at most one institution reference!");
			} // end if (ensure there is just one result)
		} // end if (check if there is an institution reference to add)
		
		
		
		
	} // end linkWorkplaceToInstitution()
	
	
	private static void addWorkplacesToCareers () {
		ArrayList careers = getFullCareersDatabase();
		ArrayList workplaces = getWorkplaces();
		
		
		ArrayList place;
		String[] careerIDs;
		String carID;
		ArrayList careerInfo;
		ArrayList carWorkplaces;
		int w;
		int c;
		for (w = 0; w < workplaces.size(); w++) {
			
			place = (ArrayList)workplaces.get(w);
			careerIDs = (String[])place.get(Work_Careers);

			for (c = 0; c < careerIDs.length; c++) {
				carID = careerIDs[c];
				careerInfo = getCareerById(careers, carID);
				carWorkplaces = (ArrayList)careerInfo.get(Career_Workplaces);
				//carWorkplaces.add(place.get(Work_ID));
				carWorkplaces.add(place.get(Work_Title));
			} // end for c (loop through all careers listed with this workplace)
			
		} // end for w (loop through all workplaces)
	}
	
	
	
	
	
	
	
	
	public static int convertPersonalityToBinIndex (String mbti) {
		int c;
		char ch;
		char[] mbti_ons = {'E', 'N', 'F', 'P'};
		
		int mbti_index = 0;
		
		for (c = 0; c < 4; c++) { // We are assuming a 4-letter Myers-Briggs Type Indicator code!
			ch = mbti.charAt(c);
			
			if (ch == mbti_ons[c]) {
				// This trait is "on" (or "full").
				mbti_index += Math.pow(2,(3-c));		// Again, we are assuming 4 characters.. This '3' is hard-coded under that assumption.
			} else {
				// This trait is "off". Do nothing.
			}
			
		} // end for c (characters in mbti code)
		
		
		return mbti_index;
	} // end convertPersonalityToBinIndex()
	
	
	public static ArrayList createOpeningsTable (int Pop_Size) {
		// By default, if no careers list is provided, then use the static list previously loaded in from file.
		return createOpeningsTable(AllCareers, Pop_Size);
	}
	
	public static ArrayList createOpeningsTable (ArrayList careersDatabase, int Pop_Size) {
		ArrayList carTable = new ArrayList();
		ArrayList carRow;
		
		//DebugTools.printArray(careersDatabase);
		
		ArrayList career;
		String carID;
		double carPC;
		double NumOpeningsInPop;
		
		double Pop_Size_d = (double)Pop_Size;
		
		int i;
		for (i = 0; i < careersDatabase.size(); i++) {
			career = (ArrayList)careersDatabase.get(i);
			carID = (String)career.get(Career_ID);
			carPC = Double.parseDouble((String)career.get(Career_Percent));
			
			NumOpeningsInPop = carPC * Pop_Size_d / 100.0;			// Divide by 100 because they are percents. If they change to decimals (0.0-1.0), then remove this division.
			NumOpeningsInPop = Math.round(NumOpeningsInPop);		// Round to an integer.
			
			carRow = new ArrayList();
			carRow.add(CareerTable_ID, carID);
			carRow.add(CareerTable_Num, new Integer((int)NumOpeningsInPop));
			
			//System.out.println(carPC + "  " + career.get(Career_Percent));
			
			carTable.add(carRow);
		} // end for i (rows in array)

		return carTable;
	} // end createOpeningsTable()
	
	
	
	public static ArrayList getCareerById (ArrayList careerList, String searchID) {
		// This function will search through the given table of career openings, and return the entry with the ID matching the given searchID.
		// param careerList: a table of career openings, as created by the above createOpeningsTable(), but could be modified before being sent here.
		// param searchID: the career ID of interest
		// returns: ArrayList containing the entry at the given ID. This ArrayList contains just two elements: the ID and the number of openings for the career.

		int i;
		int numElements = careerList.size();
		ArrayList carEntry;
		String carID;
		
		for (i = 0; i < numElements; i++) {
			carEntry = (ArrayList)careerList.get(i);
			carID = (String)carEntry.get(Career_ID);
			
			if (carID.equals(searchID)) {
				return carEntry;
			} // end if (checking it given ID matches the current item ID)
			
		} // end for i (looping through all element in array)
		
		return null;
	} // end getCareerById()
	
	public static String getCareerTitleById (ArrayList careerList, String searchID) {
		// This function will search through the given table of career openings, and return the entry with the ID matching the given searchID.
		// param careerList: a table of career openings, as created by the above createOpeningsTable(), but could be modified before being sent here.
		// param searchID: the career ID of interest
		// returns: ArrayList containing the entry at the given ID. This ArrayList contains just two elements: the ID and the number of openings for the career.

		int i;
		int numElements = careerList.size();
		ArrayList carEntry;
		String careerID;
		
		for (i = 0; i < numElements; i++) {
			carEntry = (ArrayList)careerList.get(i);
			careerID = (String)carEntry.get(Career_ID);
			
			if (careerID.equals(searchID)) {
				return (String)carEntry.get(Career_Title);
			} // end if (checking it given ID matches the current item ID)
			
		} // end for i (looping through all element in array)
		
		return null;
	} // end getCareerTitleById()
	
	
	public static String[] getWorkplaceById (String searchID) {
		// Get a list of workplaces that have work for the given career ID.
		
		ArrayList careerInfo = getCareerById(getFullCareersDatabase(), searchID);

		ArrayList workplacesForCareer = (ArrayList)careerInfo.get(Career_Workplaces);

		
		return ArrayTools.arrayListToStringArray(workplacesForCareer);
	} // end getWorkplaceById()
	
	

	
	
	
} // end Careers class
