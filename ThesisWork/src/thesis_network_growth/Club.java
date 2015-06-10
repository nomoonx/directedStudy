package thesis_network_growth;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class Club {
	static ArrayList<ArrayList<Object>> AllClubs;		// This array will keep track of all clubs.



	public static int Club_ID = 0;
	public static int Club_Name = 1;
	public static int Club_Type = 2;
	public static int Club_Traits = 3;
	public static int Club_City = 4;
	public static int Club_Pop = 5;
	public static int Club_StYr = 6;
	public static int Club_EndYr = 7;
	
	public static void loadFiles () {

		//System.err.println("Clubs->loadFiles()");
		
		try {
			AllClubs = loadClubs("Clubs.xml");
		} catch (Exception e) {
			e.printStackTrace();
		} // end try-catch (load schools file)

		
		
		// Display all clubs.
		//int i;
		//for (i = 0; i < AllClubs.size(); i++) {
			//DebugTools.printArray((ArrayList)AllClubs.get(i));
		//}
		
		
	} // end loadFiles()
	
	public static ArrayList<ArrayList<Object>> getAllClubs () {
		return AllClubs;
	} // end getAllClubs()
	
	
	
	
	public static ArrayList<ArrayList<Object>> loadClubs (String filepath) throws Exception {
		// Load the list of clubs and related info (title, corresponding traits, etc.) from the XML file.
		// param filepath: The string of the file name, assuming the file is in the root directory of this project.
		// returns: arraylist containing arraylists for each of the temples' information

		File file = new File(filepath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbFactory.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();

		// ----------------------------------------------------------------------------------------------------
		// Get all club nodes.
		// ----------------------------------------------------------------------------------------------------
		NodeList clubList = doc.getElementsByTagName("club");

		int i, d;
		ArrayList<ArrayList<Object>> ClubsList = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> Club;
		String tmp;
		
		
		
		Node clubNode;
		Element clubElement;
		NodeList SchInfo;
		NodeList nodeList;
		Element clubCategoryElement;
		Element trait;
		
		String traitType;
		String traitTypeValue;
		String traitReq;
		NodeList traitList;
		ArrayList<String[]> traitSet;
		String[] traitPair;

		for (d = 0; d < clubList.getLength(); d++) {

			Club = new ArrayList<Object>();
			
			// --------------------------------------------------
			// Get section root.
			// --------------------------------------------------
			clubNode = clubList.item(d);									// Get element as Node.
			clubElement = (Element)clubNode;								// Convert to Element.

			

			// ----- ID -----
			SchInfo = clubElement.getElementsByTagName("id");				// Extract component as NodeList.
			for (i = 0; i < SchInfo.getLength(); i++) {
				clubCategoryElement = (Element)SchInfo.item(i);				// Convert to element.
				nodeList = clubCategoryElement.getChildNodes();
				Club.add(nodeList.item(0).getNodeValue());					// Add this info to the array for this element.
			} // end i (loop through elements)

			// ----- Title -----
			SchInfo = clubElement.getElementsByTagName("title");			// Extract  component as NodeList.
			for (i = 0; i < SchInfo.getLength(); i++) {
				clubCategoryElement = (Element)SchInfo.item(i);				// Convert to element.
				nodeList = clubCategoryElement.getChildNodes();
				Club.add(nodeList.item(0).getNodeValue());					// Add this info to the array for this element.
			} // end i (loop through elements)

			// ----- Type -----
			SchInfo = clubElement.getElementsByTagName("type");				// Extract component as NodeList.
			for (i = 0; i < SchInfo.getLength(); i++) {
				clubCategoryElement = (Element)SchInfo.item(i);				// Convert to element.
				nodeList = clubCategoryElement.getChildNodes();
				Club.add(nodeList.item(0).getNodeValue());					// Add this info to the array for this element.
			} // end i (loop through elements)

			// ----- Traits -----
			traitList = clubElement.getElementsByTagName("trait");			// Extract component as NodeList.
			traitSet = new ArrayList<String[]>();
			
			for (i = 0; i < traitList.getLength(); i++) {
				trait = (Element)traitList.item(i);
				traitType = trait.getAttribute("type");						// Get attribute "type".
				traitTypeValue = trait.getFirstChild().getNodeValue();		// Get value.
				if (trait.hasAttribute("req")) {
					traitReq = trait.getAttribute("req");						// Get attribute "req".
				} else {
					traitReq = "";
				} // end if (check if club has "req" attribute)
				// Add both the type and value to a string array and add that array to the club's arraylist.
				traitPair = new String[] {traitType, traitTypeValue, traitReq};
				traitSet.add(traitPair);									// Add this info to the array for this element.
			} // end i (loop through elements)
			Club.add(traitSet);

			// ----- City -----
			SchInfo = clubElement.getElementsByTagName("city");				// Extract component as NodeList.
			for (i = 0; i < SchInfo.getLength(); i++) {
				clubCategoryElement = (Element)SchInfo.item(i);				// Convert to element.
				nodeList = clubCategoryElement.getChildNodes();
				Club.add(nodeList.item(0).getNodeValue());					// Add this info to the array for this element.
			} // end i (loop through elements)

			// ----- School Population -----
			SchInfo = clubElement.getElementsByTagName("pop");				// Extract component as NodeList.
			for (i = 0; i < SchInfo.getLength(); i++) {
				clubCategoryElement = (Element)SchInfo.item(i);				// Convert to element.
				nodeList = clubCategoryElement.getChildNodes();
				Club.add(nodeList.item(0).getNodeValue());					// Add this info to the array for this element.
			} // end i (loop through elements)

			// ----- Year Started -----
			SchInfo = clubElement.getElementsByTagName("yearStarted");		// Extract component as NodeList.
			for (i = 0; i < SchInfo.getLength(); i++) {
				clubCategoryElement = (Element)SchInfo.item(i);				// Convert to element.
				nodeList = clubCategoryElement.getChildNodes();
				Club.add(nodeList.item(0).getNodeValue());					// Add this info to the array for this element.
			} // end i (loop through elements)

			// ----- Year Ended -----
			SchInfo = clubElement.getElementsByTagName("yearEnded");		// Extract component as NodeList.
			for (i = 0; i < SchInfo.getLength(); i++) {
				clubCategoryElement = (Element)SchInfo.item(i);				// Convert to element.
				nodeList = clubCategoryElement.getChildNodes();
				tmp = nodeList.item(0).getNodeValue();

				// Determine actual endYear
				if (tmp.equals("-")) {
					// If a "-" is given in file, then school is currently running, so set endYear as society's current year.
					Club.add(String.valueOf(Configuration.SocietyYear));	// Add this info to the array for this element.
				} else {
					// Parse int year given in file.
					Club.add(nodeList.item(0).getNodeValue());				// Add this info to the array for this element.
				} // end if (checking if endYear == "-")

				
			} // end i (loop through elements)

			// Add this club array to large array.
			ClubsList.add(Club);

		} // end d (all clubs loop)

		return ClubsList;
	} // end loadClubs()

} // end Club class

