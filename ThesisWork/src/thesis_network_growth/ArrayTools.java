package thesis_network_growth;

import java.util.ArrayList;
import java.util.Arrays;

public class ArrayTools {
	
	public static int[] arrayListToIntArray (ArrayList<Integer> list) {
		// Loop through the ArrayList, list, and add each element into an array.
		int numElements = list.size();
		int[] arr = new int[numElements];
		Integer tmpInt;
		
		int i;
		for (i = 0; i < numElements; i++) {
			tmpInt = (Integer)list.get(i);
			arr[i] = tmpInt.intValue();
		} // end for i (looping through arraylist to convert to array)
		// Return array.
		return arr;
	} // end arrayListToIntArray()
	
	public static double[] arrayListToDoubleArray (ArrayList<Double> list) {
		// Loop through the ArrayList, list, and add each element into an array.
		int numElements = list.size();
		double[] arr = new double[numElements];
		Double tmpDbl;
		
		int i;
		for (i = 0; i < numElements; i++) {
			tmpDbl = (Double)list.get(i);
			arr[i] = tmpDbl.doubleValue();
		} // end for i (looping through arraylist to convert to array)
		// Return array.
		return arr;
	} // end arrayListToDoubleArray()
	
	public static String[] arrayListToStringArray (ArrayList<String> list) {
		// Loop through the ArrayList, list, and add each element into an array.
		int numElements = list.size();
		String[] arr = new String[numElements];

		int i;
		for (i = 0; i < numElements; i++) {
			arr[i] = (String)list.get(i);
		} // end for i (looping through arraylist to convert to array)
		// Return array.
		return arr;
	} // end arrayListToStringArray()
	
	
	public static ArrayList<String> stringArrayToArrayList (String[] arr) {
		// Loop through the String array, arr, and add each element into an ArrayList.
		ArrayList<String> list = new ArrayList<String>();
		int i;
		for (i = 0; i < arr.length; i++) {
			list.add(arr[i]);
		} // end for i (loop through all elements to convert from String array to ArrayList)
		return list;
	} // end stringArrayToArrayList()
	
	public static double[] stringArrayToDoubleArray (String[] arr) {
		// Loop through the String array, arr, and parse each as a double into a double array.
		// Note that if the elements aren't actually double values in strings, then this will throw an error.
		double[] list = new double[arr.length];
		int i;
		for (i = 0; i < arr.length; i++) {
			list[i] = Double.parseDouble(arr[i]);
		} // end for i (loop through all elements to convert from String array to ArrayList)
		return list;
	} // end stringArrayToDoubleArray()
	
	
	
	
	
	
	
	public static int[] sort (int[] arr) {
		// Sort the array. For simplicity, use the built-in sort methods in the Arrays class.
		Arrays.sort(arr);
		return arr;
	} // end sort()
	
	public static double[] sort (double[] arr) {
		// Sort the array. For simplicity, use the built-in sort methods in the Arrays class.
		Arrays.sort(arr);
		return arr;
	} // end sort()
	
	public static int[] unique (int[] sortedArray) {
		// Remove duplicate elements from the sortedArray. It is important that the input array is ALREADY sorted, or this function will not properly remove duplicates!
		//System.out.println("----- Before -----");
		//DebugTools.printArray(sortedArray);
		
		ArrayList<Integer> uniqueArrayList = new ArrayList<Integer>();
		uniqueArrayList.add(new Integer(sortedArray[0])); // Start with first element of array as a definite unique element!
		int i;
		// Now loop through the remaining elements, and if they are equal to the previous element, then do not add to unique list, otherwise do add it!
		for (i = 1; i < sortedArray.length; i++) {

			if (sortedArray[i] == sortedArray[i-1]) {					// If the two consecutive numbers are identical.
				
			} else {													// If this is the first instance of this number.
				uniqueArrayList.add(new Integer(sortedArray[i]));
			}
		}
		//System.out.println("----- After -----");
		//DebugTools.printArray(uniqueArrayList);
		
		// Return unique list as a real array.
		return arrayListToIntArray(uniqueArrayList);
	} // end unique()
	
	public static double[] getMaximumItemAndIndex (double[] arr) {
		// Find the largest element in the given array, arr, and return that maximum value. Also, set the reference parameter, index, to hold the index at which
		// the greatest element was found in the array.
		
		// Start with first element of array as default maximum value. This will be updated at any larger values found in the rest of the array during the loop below.
		double curMax = arr[0];
		int i;
		int maxIndex = 0;
		for (i = 1; i < arr.length; i++) { // Loop through from second element to end of array.
			if (arr[i] > curMax) {
				curMax = arr[i];
				maxIndex = i;
			}
		} // end for i (loop through all elements in array)

		return new double[] {curMax, (double)maxIndex};
	} // end getMaximumItemAndIndex()
	
	public static int countOccurrencesInArray (int[] arr, int val) {
		// Count how many times the given element, val, appears in the array, arr.
		int numOccurrences = 0;
		
		int i;
		for (i = 0; i < arr.length; i++) {
			if (arr[i] == val) {
				numOccurrences++;
			} // end if (element is equal to val)
		} // end for i (looping through array elements)
		
		
		// Return the number of occurrences.
		return numOccurrences;
	} // end countOccurrencesInArray()

	public static int countOccurrencesInArray (double[] arr, double val) {
		// Count how many times the given element, val, appears in the array, arr.
		int numOccurrences = 0;
		
		int i;
		for (i = 0; i < arr.length; i++) {
			if (arr[i] == val) {
				numOccurrences++;
			} // end if (element is equal to val)
		} // end for i (looping through array elements)
		
		
		// Return the number of occurrences.
		return numOccurrences;
	} // end countOccurrencesInArray()
	
	public static int countOccurrencesInArray (ArrayList<Integer> arr, int val) {
		// Count how many times the given element, val, appears in the int array, arr.
		int numOccurrences = 0;
		
		int i;
		Integer tmpInt;
		for (i = 0; i < arr.size(); i++) {
			tmpInt = (Integer)arr.get(i);
			if (tmpInt.intValue() == val) {
				numOccurrences++;
			} // end if (element is equal to val)
		} // end for i (looping through array elements)
		
		
		// Return the number of occurrences.
		return numOccurrences;
	} // end countOccurrencesInArray()
	
	public static int countOccurrencesInArrayList (ArrayList<Object> arr, int val) {
		// Count how many times the given element, val, appears in the int array, arr.
		int numOccurrences = 0;
		
		int i;
		Integer tmpInt;
		for (i = 0; i < arr.size(); i++) {
			tmpInt = (Integer)arr.get(i);
			if (tmpInt.intValue() == val) {
				numOccurrences++;
			} // end if (element is equal to val)
		} // end for i (looping through array elements)
		
		
		// Return the number of occurrences.
		return numOccurrences;
	} // end countOccurrencesInArray()
	
	public static boolean containsElement (String[] arr, String element) {
		// This function checks if the given element is found in the given array. It does not check for multiple occurrences nor for the index, but
		// just checks whether or not it is found at least once in the array.
		// Note that the loop runs to completion even after finding the element within, for safe practice. This could be changed to exit the loop.
		//
		// param arr: the String array
		// param element: the search string 
		//
		// returns: boolean flag indicating whether or not the given value, element, was found in the array, arr
		
		int i;
		boolean isFound = false;
		for (i = 0; i < arr.length; i++) {
			if (arr[i].equals(element)) {
				isFound = true;
			} // end if (check if array element matches the element of interest)
		} // end for i (loop through array)

		// Return the flag indicating whether or not the element was found.
		return isFound;
	} // end containsElement()
	
	
	public static boolean containsElement (ArrayList<String> arr, String element) {
		// This function checks if the given element is found in the given array. It does not check for multiple occurrences nor for the index, but
		// just checks whether or not it is found at least once in the array.
		// Note that the loop runs to completion even after finding the element within, for safe practice. This could be changed to exit the loop.
		//
		// param arr: the arraylist of string elements
		// param element: the search string 
		//
		// returns: boolean flag indicating whether or not the given value, element, was found in the array, arr
		int i;
		boolean isFound = false;
		for (i = 0; i < arr.size(); i++) {
			if (arr.get(i).equals(element)) {
				isFound = true;
			} // end if (check if array element matches the element of interest)
		} // end for i (loop through array)

		// Return the flag indicating whether or not the element was found.
		return isFound;
	} // end containsElement()
	
	
	
	public static double[] scaleAsWeights (double[] arr) {
		// Scale the given array, arr, by dividing all values by the total sum of the array, so that the sum of the scaled array should be 1.0.
		double[] normArray = new double[arr.length];
		
		int i;
		double totalSum = 0.0;
		for (i = 0; i < arr.length; i++) {
			totalSum += arr[i];
		} // end for i (loop through all elements)
		
		for (i = 0; i < arr.length; i++) {
			normArray[i] = arr[i] / totalSum;
		} // end for i (loop through all elements)
		
		return normArray;
	} // end normalize()
	
	
	public static double sum (double[] arr) {
		// Sum all the elements of the given array, arr.
		
		int i;
		double totalSum = 0.0;
		for (i = 0; i < arr.length; i++) {
			totalSum += arr[i];
		} // end for i (loop through all elements)

		return totalSum;
	} // end sum()
	
	public static double[] multiplyElementWise (double[] arrA, double[] arrB) {
		// Multiply two arrays element-wise to produce an array of equal size as the two input arrays.
		//
		// param arrA: a double array
		// param arrB: a double array
		//
		// returns: an array containing the products of the elements multiplied element-wise from arrA and arrB

		// Ensure the arrays have the same number of elements.
		if (arrA.length != arrB.length) {
			System.err.println("In ArrayTools->multiplyElementWise(); arrA and arrB must be the same dimensionality.");
			return null;
		} // end if (check if sizes are different)
		
		
		int numElements = arrA.length;
		int i;
		double[] prodArray = new double[numElements];
		
		// Loop through arrays and multiply element-wise at each index.
		for (i = 0; i < numElements; i++) {
			prodArray[i] = arrA[i] * arrB[i];
		} // end for i (loop through elements in arrays)
		
		return prodArray;
	} // end multiplyElementWise()
	
	
	public static ArrayList<String> removeFromArray (ArrayList<String> arr, String removeVal) {
		// This function will produce an arraylist identical to the input arr except it will remove any elements
		// with the value of the given String, removeVal. This is used when choosing a city for a spouse to live
		// in when not living with their partner for a period, and thus the city options are all cities except
		// for the city in which the person's spouse lives in.
		//
		// param arr: the input ArrayList of String elements
		// param removeVal: the String of the elements to remove from (or, to not add to) the array
		//
		// returns: an ArrayList with all values from arr except any that match the given removeVal value
		
		int numElements = arr.size();
		int i;
		
		ArrayList<String> newArr = new ArrayList<String>();
		
		for (i = 0; i < numElements; i++) {
			
			if (!arr.get(i).equals(removeVal)) {
				// If the element does not match the removeVal string, then add it to the new array.
				newArr.add(arr.get(i));
			} // end if (check if element matches the string for which to remove)
			
		} // end for i (loop through original array elements)
		
		return newArr;
		
	} // end removeFromArray()
	
	public static int getElementIndex (ArrayList<Integer> array, int searchElement) {
		// Find the index of the given element for where it appears in the given array (IF it does appear).
		// NOTE: If the element appears multiple times, only the first occurrence will be found here.
		//
		// param array: the array of elements in which to search
		// param searchElement: the value to search for in the array
		//
		// return: the integer index pointing to the array location where the searchElement is found (or -1 if it is not found)

		int numElements = array.size();
		int i;
		int foundIndex = -1;
		
		// Loop through array.
		for (i = 0; i < numElements; i++) {
			// Check if array element at index i is the correct element being searched for.
			if (array.get(i) == searchElement) {
				foundIndex = i;
				break;
			} // end if (check if array element equals the element being searched for)
		} // end for i (loop through array to search for element)
		
		// Return the index at which the element was found in the array, or -1 if the loop completed and the element wasn't found.
		return foundIndex;
		
	} // end getElementIndex()
	
	public static String convertArrayToCSV (int[] arr) {
		// Convert the given array to a string of comma-separated-values.
		int i;
		String csv = "";
		for (i = 0; i < arr.length - 1; i++) {
			csv += arr[i] + ",";
		} // end for i (loop through array elements except last one)

		// Now add last element without proceeding comma.
		csv += arr[i];
		
		return csv;
	} // end convertArrayToCSV()
	
	public static String convertArrayToCSV (double[] arr) {
		// Convert the given array to a string of comma-separated-values.
		int i;
		String csv = "";
		for (i = 0; i < arr.length - 1; i++) {
			csv += arr[i] + ",";
		} // end for i (loop through array elements except last one)

		// Now add last element without proceeding comma.
		csv += arr[i];
		
		return csv;
	} // end convertArrayToCSV()
	
	
	
	
	
	
	
	
	/*
	public static ArrayList<Integer> getElementIndices (ArrayList<Integer> array, int searchElement) {
		// Find the indices of the given element for where it appears in the given array (IF it does appear).
		//
		// param array: the array of elements in which to search
		// param searchElement: the value to search for in the array
		//
		// return: the array of integer indices pointing to the array location where the searchElement is found.

		int numElements = array.size();
		int i;
		ArrayList<Integer> foundIndices = new ArrayList<Integer>();
		
		// Loop through array.
		for (i = 0; i < numElements; i++) {
			// Check if array element at index i is the correct element being searched for.
			if (array.get(i) == searchElement) {
				// Add index to the array.
				foundIndices.add(i);
			} // end if (check if array element equals the element being searched for)
		} // end for i (loop through array to search for element)
		
		// Return the array of indices at which the element was found in the array.
		return foundIndices;
		
	} // end getElementIndices()
	*/
	
	
	
	
	
	
	
	
	
	
	
	public static Object[][] fillRowWithValue (Object[][] arr, int row, Object value) {
		// This function will fill in all elements across a row of a 2D array with a given value.

		// If the array has not been initialized, then exit function.
		if (arr == null || arr[0] == null) {
			return null;
		} // end if (checking if array is null)

		int i;
		for (i = 0; i < arr.length; i++) {
			arr[i][row] = value;
		} // end for i (across row)

		return arr;
	} // end fillRowWithValue()
	
	public static Object[][] fillColWithValue (Object[][] arr, int col, Object value) {
		// This function will fill in all elements down a column of a 2D array with a given value.

		// If the array has not been initialized, then exit function.
		if (arr == null || arr[0] == null) {
			return null;
		} // end if (checking if array is null)

		int i;
		for (i = 0; i < arr[0].length; i++) {
			arr[col][i] = value;
		} // end for i (across row)

		return arr;
	} // end fillColWithValue()
	
	
	
	
	
}
