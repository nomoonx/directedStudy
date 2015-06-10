package tests;

public class LoadData {
	
	public static void main (String[] args) {
		try {
			//XMLLoader.loadWorldFile1("London.xml");
			XMLLoader xml = new XMLLoader("London.xml");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
