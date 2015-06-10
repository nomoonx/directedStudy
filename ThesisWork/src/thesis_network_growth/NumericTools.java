package thesis_network_growth;

public class NumericTools {

	
	public static int convertBinIntArrayToDecInt (Integer[] arr) {
		// This function takes in an Integer array of values 0 or 1, and calculates the decimal representation of the number.
		// (i.e. {1,0,1,0} as an input would yield a return value of 10.)
		int decNum = 0;
		int b;
		
		for (b = 0; b < arr.length; b++) {
			decNum += arr[b].intValue() * (int)(Math.pow(2, arr.length-b-1));
		} // end for b (each binary digit in the personality code)
		
		return decNum;
	}
	
}
