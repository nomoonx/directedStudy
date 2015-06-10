package thesis_network_growth;

import java.util.ArrayList;

public class Population extends ArrayList<Person> {
	
	public Population () {
		super();
	} // end Population() constructor
	
	
	/*
	public Person get (int i) {
		// THIS IS VERY IMPORTANT! This overridden get is used so that each person's real ID is obtained from the PersonIndexTable
		// rather than directly using i as their ID. This is important for when people die and are removed from the global array
		// and thus the indices shift but their IDs don't shift, so ID != index anymore!
		//
		// param i: the index to a person in the global population array
		//
		// return: the Person
		//System.out.println("In Population->get()...");
		return super.get(i);
	} // end overridden get()
	*/

} // end Population class
