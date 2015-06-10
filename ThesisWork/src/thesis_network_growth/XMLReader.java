package thesis_network_growth;

import java.io.File;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLReader {
	
	Dictionary<String,Object> XML;
	
	public void loadConfigFile (String filepath) throws Exception {
		File file = new File(filepath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbFactory.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();
		//System.out.println("Root element = " + doc.getDocumentElement().getNodeName());
		
		// Initialize dictionary to contain all XML elements.
		XML = new Hashtable<String, Object>();
		
		
		NodeList paramList = doc.getElementsByTagName("param");
		Node paramNode;
		Element paramElement;

		
		String structType;
		String variableType;
		String paramName;
		
		
		
		//System.out.println("num elements = " + paramList.getLength());


		int d;

		for (d = 0; d < paramList.getLength(); d++) {
		
			// --------------------------------------------------
			// Get section root.
			// --------------------------------------------------
			paramNode = paramList.item(d);									// Get parameter element as Node.
			paramElement = (Element)paramNode;								// Convert to Element.

			//System.out.println(paramElement.getAttribute("id"));
			//System.out.println("\t<" + paramElement.getChildNodes().getLength() + ">\t");
			
			
			
			//rowList = paramElement.getElementsByTagName("row");
			
			
			
			
			structType = paramElement.getAttribute("struct");
			variableType = paramElement.getAttribute("type");
			paramName = paramElement.getAttribute("id");
			
			
			if (structType.equals("element")) {
				Object x = readElement(paramElement, variableType);
				XML.put(paramName, x);
				
				//System.out.println("x = " + x);
				
			} else if (structType.equals("vector")) {
				Object[] x = readVector(paramElement, variableType);
				XML.put(paramName, x);
				
				//DebugTools.printArray(x);
				
			} else if (structType.equals("matrix")) {
				Object[][] x = readMatrix(paramElement, variableType);
				XML.put(paramName, x);
				
				//DebugTools.printArray(x);
			} else {
				System.err.println("XMLReader->loadConfigFile(); unknown node structure type found: " + structType);
			}
			

			
			
			//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			//System.out.println();


		} // end d (param loop)

	} // end loadConfigFile()
	
	
	public double getDouble (String paramName) {
		return Double.parseDouble(XML.get(paramName).toString());
	} // end getDouble()
	
	public int getInteger (String paramName) {
		return Integer.parseInt(XML.get(paramName).toString());
	} // end getInteger()
	
	public String getString (String paramName) {
		return XML.get(paramName).toString();
	} // end getString()
	
	public double[] getDoubleArray (String paramName) {
		Double[] arr = (Double[])(XML.get(paramName));
		double[] retArr = new double[arr.length];
		int i;
		for (i = 0; i < arr.length; i++) {
			retArr[i] = arr[i];
		} // end for i (loop through array elements)

		return retArr;
	} // end getDoubleArray()

	public int[] getIntegerArray (String paramName) {
		Integer[] arr = (Integer[])(XML.get(paramName));
		int[] retArr = new int[arr.length];
		int i;
		for (i = 0; i < arr.length; i++) {
			retArr[i] = arr[i];
		} // end for i (loop through array elements)

		return retArr;
	} // end getIntegerArray()

	public String[] getStringArray (String paramName) {
		return (String[])(XML.get(paramName));
	} // end getStringArray()
	
	public double[][] getDoubleMatrix (String paramName) {
		Double[][] arr = (Double[][])(XML.get(paramName));
		double[][] retArr = new double[arr.length][arr[0].length];
		int i, j;
		for (i = 0; i < arr.length; i++) {
			for (j = 0; j < arr[0].length; j++) {
				retArr[i][j] = arr[i][j];
			} // end for j (loop through array columns)
		} // end for i (loop through array rows)

		return retArr;
	} // end getDoubleArray()
	



	
	
	private Object readElement (Element element, String type) {
		
		String value = element.getChildNodes().item(0).getNodeValue();
		
		if (type.equals("integer")) {
			// Convert to integer.
			return Integer.parseInt(value);
		} else if (type.equals("double")) {
			// Convert to double.
			return Double.parseDouble(value);
		} else if (type.equals("string")) {
			// Leave as string
			return value;
		} else {
			System.err.println("XMLReader->readElement(); unknown variable type: " + type);
			return null;
		} // end if (determine the variable type)

	} // end readElement()
	
	private Object[] readVector (Element element, String type) {
		
		//Object[] vector;
		
		//NodeList rowList = element.getElementsByTagName("row");
		//Node rowNode = rowList.item(0);									// Get row element as Node.
		//Element rowElement = (Element)rowNode;								// Convert to Element.

		
		return readRow(element, type);
		
		
		/*
		NodeList elementList = rowElement.getElementsByTagName("el");
		Node elementNode;
		Element elementElement;

		int numElements = elementList.getLength();
		

		if (type.equals("integer")) {
			vector = new Integer[numElements];
		} else if (type.equals("double")) {
			vector = new Double[numElements];
		} else if (type.equals("string")) {
			vector = new String[numElements];
		} else {
			vector = new Object[numElements];
			System.err.println("XMLReader->readVector(); unknown variable type: " + type);
		} // end if (determine the variable type)
		
		

		int k;

		for (k = 0; k < numElements; k++) {
			elementNode = elementList.item(k);
			elementElement = (Element)elementNode;
			//System.err.println(elementElement.getChildNodes().item(0).getNodeValue());
			// Store in array.
			//vector[k] = elementElement.getChildNodes().item(0).getNodeValue();
			vector[k] = readElement(elementElement, type);
		} // end for k (loop through all elements in row)
		*/

		//return vector;
	} // end readVector()
	
	
	
	private Object[][] readMatrix (Element element, String type) {
		
		Object[][] matrix;
		
		NodeList rowList = element.getElementsByTagName("row");
		
		// Look at first row initially, just to get the number of columns.
		Node rowNode = rowList.item(0);
		Element rowElement = (Element)rowNode;
		Object[] x = readRow(rowElement, type);

		// Get number of rows and columns.
		int numRows = rowList.getLength();
		int numCols = x.length;

		// Initialize matrix.
		if (type.equals("integer")) {
			matrix = new Integer[numRows][numCols];
		} else if (type.equals("double")) {
			matrix = new Double[numRows][numCols];
		} else if (type.equals("string")) {
			matrix = new String[numRows][numCols];
		} else {
			matrix = new Object[numRows][numCols];
			System.err.println("XMLReader->readMatrix(); unknown variable type: " + type);
		} // end if (determine the variable type)
		


		int k;

		for (k = 0; k < numRows; k++) {
			rowNode = rowList.item(k);										// Get row element as Node.
			rowElement = (Element)rowNode;									// Convert to Element.

			x = readRow(rowElement, type);

			// Copy vector into matrix.
			matrix[k] = x;

		} // end for k (loop through all elements in row)

		return matrix;
	} // end readMatrix()

	private Object[] readRow (Element row, String type) {
		
		Object[] vector;
		
		NodeList elementList = row.getElementsByTagName("el");
		Node elementNode;
		Element elementElement;

		int numElements = elementList.getLength();
		
		
		if (type.equals("integer")) {
			vector = new Integer[numElements];
		} else if (type.equals("double")) {
			vector = new Double[numElements];
		} else if (type.equals("string")) {
			vector = new String[numElements];
		} else {
			vector = new Object[numElements];
			System.err.println("XMLReader->readVector(); unknown variable type: " + type);
		} // end if (determine the variable type)
		
		

		int k;

		for (k = 0; k < numElements; k++) {
			elementNode = elementList.item(k);
			elementElement = (Element)elementNode;
			//System.err.println(elementElement.getChildNodes().item(0).getNodeValue());
			// Store in array.
			//vector[k] = elementElement.getChildNodes().item(0).getNodeValue();
			vector[k] = readElement(elementElement, type);
		} // end for k (loop through all elements in row)
		
		return vector;
	} // end readRow()
	
	
	
} // end XMLReader class