package thesis_network_growth;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DebugTools {
	
	public static void printArray (int[] arr) {

		// If array is null, then do exit function.
		if (arr == null) {
			System.out.println("[]");
			return;
		}
		int m = arr.length;
		
		int i;
		System.out.println("[");
		for (i = 0; i < m; i++) {
			System.out.print(arr[i] + "  ");
		}
		System.out.println("\n]");
	}
	
	public static void printArray (int[][] arr) {

		// If array is null, then do exit function.
		if (arr == null) {
			System.out.println("[]");
			return;
		}
		int m = arr.length;
		int n = arr[0].length;
		
		int i, j;
		System.out.println("[");
		for (j = 0; j < n; j++) {
			for (i = 0; i < m; i++) {
				System.out.print(arr[i][j] + "  ");
			}
			System.out.println();
		}
		System.out.print("]");
	}
	
	public static void printArray (double[] arr) {

		// If array is null, then do exit function.
		if (arr == null) {
			System.out.println("[]");
			return;
		}
		int m = arr.length;
		
		int i;
		System.out.println("[");
		for (i = 0; i < m; i++) {
			System.out.print(arr[i] + "  ");
		}
		System.out.println("\n]");
	}
	
	public static void printArray (double[][] arr) {

		// If array is null, then do exit function.
		if (arr == null) {
			System.out.println("[]");
			return;
		}
		int m = arr.length;
		int n = arr[0].length;
		
		int i, j;
		System.out.println("[");
		for (j = 0; j < n; j++) {
			for (i = 0; i < m; i++) {
				System.out.print(arr[i][j] + "  ");
			}
			System.out.println();
		}
		System.out.print("]");
	}
	
	public static void printArray (Object[] arr) {

		// If array is null, then do exit function.
		if (arr == null) {
			System.out.println("[]");
			return;
		}
		int m = arr.length;
		
		int i;
		System.out.println("[");
		for (i = 0; i < m; i++) {
			System.out.print(arr[i] + "  ");
		}
		System.out.println("\n]");
	}
	
	public static void printArray (Object[][] arr) {

		// If array is null, then do exit function.
		if (arr == null) {
			System.out.println("[]");
			return;
		}
		int m = arr.length;
		int n = arr[0].length;

		int i, j;
		System.out.println("[");
		for (i = 0; i < m; i++) {
			for (j = 0; j < n; j++) {
				System.out.print(arr[i][j] + "  ");
			}
			System.out.println();
		}
		System.out.print("]");
	}
	
	public static void printArray (ArrayList<Object> arr) {

		// If array is null, then do exit function.
		if (arr == null || arr.isEmpty()) {
			System.out.println("[]");
			return;
		}
		int m = arr.size();
		
		int i;
		System.out.println("[");
		for (i = 0; i < m; i++) {
			System.out.print(arr.get(i) + "  ");
		}
		System.out.println("\n]");
	}
	
	public static void printArrayStr (ArrayList<String> arr) {

		// If array is null, then do exit function.
		if (arr == null || arr.isEmpty()) {
			System.out.println("[]");
			return;
		}
		int m = arr.size();
		
		int i;
		System.out.println("[");
		for (i = 0; i < m; i++) {
			System.out.print(arr.get(i) + "  ");
		}
		System.out.println("\n]");
	}
	
	public static void printArray (List<Object> arr) {

		// If array is null, then do exit function.
		if (arr == null || arr.isEmpty()) {
			System.out.println("[]");
			return;
		}
		int m = arr.size();
		
		int i;
		System.out.println("[");
		for (i = 0; i < m; i++) {
			System.out.print(arr.get(i) + "  ");
		}
		System.out.println("\n]");
	}
	
	public static void printArray (ActivityArchive arr) {

		// If array is null, then do exit function.
		if (arr == null || arr.isEmpty()) {
			System.out.println("[]");
			return;
		}
		int m = arr.size();
		
		int i;
		
		Hashtable<Object, int[]> dict;
		Iterator<Map.Entry<Object, int[]>> iter;
		Map.Entry<Object, int[]> entry;
		Object key;
		int[] value;
		
		System.out.println("[");
		for (i = 0; i < m; i++) {
			
			dict = (Hashtable<Object, int[]>)arr.get(i);
			iter = dict.entrySet().iterator();
			while (iter.hasNext()) {
				entry = (Entry<Object, int[]>) iter.next();
				key = entry.getKey();
				value = (int[])entry.getValue();

				if (key instanceof String) {
					//System.err.println("String");
					System.out.print("\t" + (String)key);
				} else if (key instanceof String[]) {
					//System.err.println("String[]");
					System.out.print("\t");
					DebugTools.printInlineArray((String[])key);
				} else {
					System.err.println("Difference class: " + key.getClass());
				}
				System.out.println(" -> {" + value[0] + ", " + value[1] + "}  ");
			}
		}
		System.out.println("]");
	}
	
	
	
	
	
	
	// -------------------------------------------------------------------------------------------------------------------------
	// Print Inline Arrays (no newlines around array print out)
	// -------------------------------------------------------------------------------------------------------------------------
	
	public static void printInlineArray (int[] arr) {

		// If array is null, then do exit function.
		if (arr == null) {
			System.out.println("[]");
			return;
		}
		int m = arr.length;
		
		int i;
		System.out.print("[ ");
		for (i = 0; i < m; i++) {
			if (i == m-1) {
				// If final element in array, then do not include the whitespace at the end.
				System.out.print(arr[i]);
			} else {
				System.out.print(arr[i] + "  ");
			}
		}
		System.out.print(" ]");
	}

	public static void printInlineArray (double[] arr) {

		// If array is null, then do exit function.
		if (arr == null) {
			System.out.println("[]");
			return;
		}
		int m = arr.length;
		
		int i;
		System.out.print("[ ");
		for (i = 0; i < m; i++) {
			if (i == m-1) {
				// If final element in array, then do not include the whitespace at the end.
				System.out.print(arr[i]);
			} else {
				System.out.print(arr[i] + "  ");
			}
		}
		System.out.print(" ]");
	}

	public static void printInlineArray (Object[] arr) {

		// If array is null, then do exit function.
		if (arr == null) {
			System.out.println("[]");
			return;
		}
		int m = arr.length;
		
		int i;
		System.out.print("[ ");
		for (i = 0; i < m; i++) {
			if (i == m-1) {
				// If final element in array, then do not include the whitespace at the end.
				System.out.print(arr[i]);
			} else {
				System.out.print(arr[i] + "  ");
			}
		}
		System.out.print(" ]");
	}

	public static void printInlineArray (ArrayList<Object> arr) {

		// If array is null, then do exit function.
		if (arr == null || arr.isEmpty()) {
			System.out.println("[]");
			return;
		}
		int m = arr.size();
		
		int i;
		System.out.print("[ ");
		for (i = 0; i < m; i++) {
			if (i == m-1) {
				// If final element in array, then do not include the whitespace at the end.
				System.out.print(arr.get(i));
			} else {
				System.out.print(arr.get(i) + "  ");
			}
		}
		System.out.print(" ]");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
