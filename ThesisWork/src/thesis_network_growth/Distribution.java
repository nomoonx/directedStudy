package thesis_network_growth;


import java.util.ArrayList;
import java.util.Random;


public class Distribution {
	//private static Random rand = new Random(987654321); // Set a fixed random seed!
	//private static Random rand = new Random(987654); // Set a fixed random seed!
	//private static Random rand = new Random(9876); // Set a fixed random seed!
	//private static Random rand = new Random(987654322); // Set a fixed random seed!
	private static Random rand = new Random();
	

	public static int uniform (int min, int max) {
		// Generate a uniformly distributed number in [min, max], inclusive.
		return (rand.nextInt(max-min+1) + min);
	} // end uniform()

	public static double uniform (double min, double max) {
		// Generate a uniformly distributed number in [min, max], inclusive.
		return (rand.nextDouble()*(max-min) + min);
	} // end uniform()

	public static double normal (double mean, double std) {
		// Generate a normally distributed number with the given mean and std parameters.
		return mean + std * standardNormal();
	} // end normal()
	
	
	private static boolean holdingNextNumber = false;
	private static double nextNumber = 0.0;
	private static double standardNormal () {
		// Generate a standard normally distributed number. In this method, two normally distributed numbers are generated at a time, and one
		// is returned and the other is stored for the next call to the function. Thus, every second call to this function will generate two
		// random numbers, and every other call will just return the number generated the previous time.
		// This method is taken from the book "Numerical Recipes in C" from the section "Normal (Gaussian) Deviates" on pages 289-290.

		double v1, v2, rsq, fac;


		if (holdingNextNumber) {		// If an existing number is stored from the previous calculation.
			//System.out.println("A");
			holdingNextNumber = false;
			return nextNumber;
		} else {						// If a new pair of numbers must be calculated
			// The do-while loop will ensure that the two uniformly distributed numbers are within the unit circle.
			//System.out.println("B");
			do {
				v1 = uniform(-1.0, 1.0);
				v2 = uniform(-1.0, 1.0);
				rsq = v1*v1 + v2*v2;
			} while (rsq >= 1.0 || rsq == 0.0);
			fac = Math.sqrt(-2.0*Math.log(rsq)/rsq);
			nextNumber = v1*fac;		// Store the v1 normal number for next call to this function.
			holdingNextNumber = true;
			return v2*fac;				// Return the v2 normal number.
		} // end if (whether we are calculating a new pair, or returning the second value from a previous calculation)

	} // end standardNormal()
	
	
	public static int custom (double[] distr) {
		// Generate a random number according to the given set of discrete probabilities.
		
		// This first section is to establish the percentile bins. These are the cumulative checkpoints between 0 and 1, as each subsequent
		// probability is added to the previous sum. This way, these percentile bins will be used below to determine where the random number lies.
		int i;
		double[] percentiles = new double[distr.length];
		percentiles[0] = distr[0];
		for (i = 1; i < distr.length; i++) {
			percentiles[i] = distr[i] + percentiles[i-1]; 
		}
		
		// Generate a uniformly distributed random number.
		double rndNum = rand.nextDouble();
		
		// Beginning at the 0th percentile bin, loop through until it is determined which bin the random number lies in.
		int p = 0;
		while (rndNum > percentiles[p]) {
			p++; // Increment p so that the next bin can be checked sequentially.
		} // end while (loop through percentile bins until finding the one that fits the random number)

		// Return the index p, which indicates which percentile bin (i.e. discrete case for the given attribute) the random number lies in.
		return p;
	} // end custom()
	
	
	public static Object uniformRandomObject (Object[] itemsArray) {
		// Choose a random item from an array of objects.
		int rndIndex = Distribution.uniform(0, itemsArray.length-1);
		return itemsArray[rndIndex];
	} // end uniformRandomObject()

	public static Object uniformRandomObjectObj (ArrayList<Object> itemsArray) {
		// Choose a random item from an array of objects.
		int rndIndex = Distribution.uniform(0, itemsArray.size()-1);
		return itemsArray.get(rndIndex);
	} // end uniformRandomObject()

	public static String uniformRandomObjectStr (ArrayList<String> itemsArray) {
		// Choose a random item from an array of objects.
		int rndIndex = Distribution.uniform(0, itemsArray.size()-1);
		return itemsArray.get(rndIndex);
	} // end uniformRandomObject()
	
	
	public static ArrayList<String> permutation (ArrayList<String> arr) {
		// Take an ArrayList of String elements and shuffle it all up. This works by looping while original array has elements in it, and at each iteration, select a
		// random element, put it into the new shuffled array, permArray, and remove it from the original array, origArray. Thus, no element is selected more than once, and
		// the entire array becomes shuffled.
		// param arr: the ArrayList to be shuffled
		// returns: the shuffled (or permutation) of the ArrayList

		ArrayList<Object> origArray = new ArrayList<Object>();
		int i;
		for (i = 0; i < arr.size(); i++) {
			origArray.add(arr.get(i));
		} // end for i (loop through array to make copy of the input array)
		
		
		String val;
		ArrayList<String> permArray = new ArrayList<String>();
		
		// Loop until the original array, origArray, is empty. At each iteration in the loop, a random element is selected from the remaining origArray.
		// That selected element is added to the permArray and removed from the original origArray so that it cannot be chosen again. 
		while (!origArray.isEmpty()) {
			val = (String)Distribution.uniformRandomObjectObj(origArray);	// Select a random element from the remaining array, origArray.
			permArray.add(val);											// Add element to shuffled array, permArray.
			origArray.remove(val);										// Remove element from original array, origArray.
		} // end while (loop until all elements from arr are removed and put into permArray)
		
		return permArray;
	} // end permutation()
	
	public static ArrayList<String> permutation (ArrayList<String> arr, int numElements) {
		// Take an ArrayList, get the shuffled array of its elements by calling permutation(arr), and then put the first numElements values into a new ArrayList to return.
		// param arr: the ArrayList to be shuffled
		// returns: an ArrayList of the first numElements values from the permutation of the array

		ArrayList<String> permArr = permutation(arr);

		ArrayList<String> perms = new ArrayList<String>();
		int i;
		for (i = 0; i < numElements; i++) {
			perms.add(permArr.get(i));
		} // end for i (loop through as many elements as are required)

		return perms;
	} // end permutation()
	
} // end Distribution class
