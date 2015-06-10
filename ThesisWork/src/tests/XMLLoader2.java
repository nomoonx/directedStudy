package tests;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLLoader2 {
	
	public static int RACE_LIST = 0;
	public static int AGE_LIST = 1;
	
	ArrayList allDemographics;
	ArrayList[] raceLists;
	ArrayList[] ageLists;
	
	public XMLLoader2 (String filepath) throws Exception {
		// Initialize arraylists.
		allDemographics = new ArrayList();
		
		raceLists = new ArrayList[2];
		ageLists = new ArrayList[2];

		// Initialize element arrays.
		raceLists[0] = new ArrayList();
		raceLists[1] = new ArrayList();
		ageLists[0] = new ArrayList();
		ageLists[1] = new ArrayList();
		
		
		// Load file.
		loadWorldFile1(filepath);
		
		allDemographics.add(raceLists);
		allDemographics.add(ageLists);
	}
	
	public ArrayList[] getResultList (int type) {
		return (ArrayList[])allDemographics.get(type);
	}

	public void loadWorldFile1 (String filepath) throws Exception {
		File file = new File(filepath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbFactory.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();
		//System.out.println("Root element = " + doc.getDocumentElement().getNodeName());
		
		// ----------------------------------------------------------------------------------------------------
		// Get population.
		// ----------------------------------------------------------------------------------------------------
		NodeList popList = doc.getElementsByTagName("population");
		Node popNode = popList.item(0);								// Get demographics element as Node.
		Element popElement = (Element)popNode;
		NodeList demoPopChildren = popElement.getChildNodes();
		System.out.println("Population:  " + demoPopChildren.item(0).getNodeValue());

		// Return the list of results.
		//NodeList[] resultsLists = new NodeList[2];

		
		NodeList demoList = doc.getElementsByTagName("demographics");
		int d = 0;
		
		int i;
		//for (d = 0; d < demoList.getLength(); d++) {
		
			// --------------------------------------------------
			// Get section root.
			// --------------------------------------------------
			Node demoNode = demoList.item(d);										// Get demographics element as Node.
			Element demoElement = (Element)demoNode;								// Convert to Element.

			// --------------------------------------------------
			// Extract 'race' component.
			// --------------------------------------------------
			NodeList demoRaceCat = demoElement.getElementsByTagName("race");		// Extract race component as NodeList.

			for (i = 0; i < demoRaceCat.getLength(); i++) {
				Element demoRaceCatElement = (Element)demoRaceCat.item(i);				// Convert to element.
				NodeList demoRaceCatChildren = demoRaceCatElement.getChildNodes();		// Retrieve child nodes from this race element.

				//System.out.println(demoRaceCatElement.getAttribute("name") + ": " + demoRaceCatChildren.item(0).getNodeValue());
				raceLists[0].add(demoRaceCatElement.getAttribute("name"));
				raceLists[1].add(demoRaceCatChildren.item(0).getNodeValue());
				
			} // end i (loop through race elements)
			
			// --------------------------------------------------
			// Get section root.
			// --------------------------------------------------
			demoNode = demoList.item(1);										// Get demographics element as Node.
			demoElement = (Element)demoNode;								// Convert to Element.
			
			// --------------------------------------------------
			// Extract 'age' component.
			// --------------------------------------------------
			NodeList demoAgeCat = demoElement.getElementsByTagName("age");		// Extract race component as NodeList.
			
			//System.out.println(demoAgeCat.getLength() + "!");
			
			for (i = 0; i < demoAgeCat.getLength(); i++) {
				Element demoAgeCatElement = (Element)demoAgeCat.item(i);				// Convert to element.
				NodeList demoAgeCatChildren = demoAgeCatElement.getChildNodes();		// Retrieve child nodes from this race element.

				//System.out.println(demoAgeCatElement.getAttribute("name") + ": " + demoAgeCatChildren.item(0).getNodeValue());
				ageLists[0].add(demoAgeCatElement.getAttribute("name"));
				ageLists[1].add(demoAgeCatChildren.item(0).getNodeValue());
				
			} // end i (loop through age elements)

		//} // end d (demographics loop)

	}
	
	
	
	
}
