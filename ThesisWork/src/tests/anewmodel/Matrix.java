package tests.anewmodel;

public class Matrix {
	// This will include just a few (very limited) operations, only for SQUARE MATRICES! These are for the community division algorithm.
	
	
	public static double[][] transpose (double[][] M) {
		int size = M.length; // Only square matrices work here!
		int i, j;
		double tmp;
		
		double[][] Mt = shallowCopy(M);

		for (i = 0; i < size; i++) {
			for (j = 0; j < i; j++) {
				tmp = Mt[i][j];
				Mt[i][j] = Mt[j][i];
				Mt[j][i] = tmp;
			}
		}
	
		return Mt;
	}
	
	public static double[][] add (double[][] A, double[][] B) {
		int size = A.length; // Only two square matrices of the same size work here!
		int i, j;
		
		double[][] C = new double[size][size];

		for (i = 0; i < size; i++) {
			for (j = 0; j < size; j++) {
				C[i][j] = A[i][j] + B[i][j];
			}
		}

		return C;
	}
	
	public static double[][] shallowCopy(double[][] M) {
		int size = M.length; // Only two square matrices of the same size work here!
		int i, j;
		
		double[][] B = new double[size][size];

		for (i = 0; i < size; i++) {
			for (j = 0; j < size; j++) {
				B[i][j] = M[i][j];
			}
		}
		
		return B;
	}
	
	public static double sum (double[][] M) {
		int size = M.length; // Only square matrices work here!
		int i, j;

		double currSum = 0.0;
		for (i = 0; i < size; i++) {
			for (j = 0; j < size; j++) {
				currSum += M[i][j];
			}
		}
		
		return currSum;
	}
	
	public static double[] sumAllColumns (double[][] M) {
		int size = M.length; // Only square matrices work here!
		int i, j;

		double[] colSums = new double[size];
		for (i = 0; i < size; i++) {
			colSums[i] = 0.0;
			for (j = 0; j < size; j++) {
				colSums[i] += M[j][i];
			}
		}
		
		return colSums;
	}
	
	public static double diag (double[][] M) {
		int size = M.length; // Only square matrices work here!
		int i;

		double currSum = 0.0;
		for (i = 0; i < size; i++) {
			currSum += M[i][i];
		}
		
		return currSum;
	}
	
	
	
	
	
	
	public static void display (double[][] M) {
		int size = M.length; // Only square matrices work here!
		int i, j;

		for (i = 0; i < size; i++) {
			for (j = 0; j < size; j++) {
				System.out.print(M[i][j] + "  ");
			}
			System.out.println();
		}
		System.out.println();
	}
	public static void display (double[] M) {
		int size = M.length; // Only square matrices work here!
		int i;

		for (i = 0; i < size; i++) {
			System.out.print(M[i] + "  ");
		}
		System.out.println();
	}
	public static void display (int[] M) {
		int size = M.length; // Only square matrices work here!
		int i;

		for (i = 0; i < size; i++) {
			System.out.print(M[i] + "  ");
		}
		System.out.println();
	}
}
