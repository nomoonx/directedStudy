package tests;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLLoader {
	
	ArrayList[] raceLists;
	
	public XMLLoader (String filepath) throws Exception {
		raceLists = loadWorldFile1(filepath);
	}
	
	public ArrayList[] getResultList () {
		return raceLists;
	}

	public ArrayList[] loadWorldFile1 (String filepath) throws Exception {
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
		ArrayList[] resLists = new ArrayList[2];
		resLists[0] = new ArrayList();
		resLists[1] = new ArrayList();
		
		NodeList demoList = doc.getElementsByTagName("demographics");
		int d;
		
		for (d = 0; d < demoList.getLength(); d++) {
		
			// --------------------------------------------------
			// Get section root.
			// --------------------------------------------------
			Node demoNode = demoList.item(d);								// Get demographics element as Node.
			Element demoElement = (Element)demoNode;						// Convert to Element.

			// --------------------------------------------------
			// Extract 'race' component.
			// --------------------------------------------------
			NodeList demoRaceCat = demoElement.getElementsByTagName("race");	// Extract race component as NodeList.
			Element demoRaceCatElement = (Element)demoRaceCat.item(0);				// Convert to element.
			NodeList demoRaceCatChildren = demoRaceCatElement.getChildNodes();		// Retrieve child nodes from this race element.

			// --------------------------------------------------
			// Extract 'percent' component.
			// --------------------------------------------------
			NodeList demoPercentCat = demoElement.getElementsByTagName("percent");	// Extract race component as NodeList.
			Element demoPercentCatElement = (Element)demoPercentCat.item(0);				// Convert to element.
			NodeList demoPercentCatChildren = demoPercentCatElement.getChildNodes();		// Retrieve child nodes from this race element.
			
			System.out.println("Demographic: " + demoRaceCatChildren.item(0).getNodeValue() + ":  " + demoPercentCatChildren.item(0).getNodeValue());
			
			// Add items to arrays to be returned.
			resLists[0].add(demoRaceCatChildren.item(0).getNodeValue());
			resLists[1].add(demoPercentCatChildren.item(0).getNodeValue());
			
			//resultsLists[0] = demoRaceCatChildren;
			//resultsLists[1] = demoPercentCatChildren;
			
		} // end d (demographics loop)
	
		return resLists;
	
	}
	
	
	
	
}
