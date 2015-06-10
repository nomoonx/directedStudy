package tests;

public class DeterministicReweightingAlgorithm {
	
	int N = 100;
	int C = 5; int M = 3; // Assume 5 attributes, each with 3 possible options.
	double[] weights;
	
	public DeterministicReweightingAlgorithm () {
		
		int j;
		
		// Step 1
		weights = new double[N];
		for (j = 0; j < N; j++) {
			weights[j] = 1.0;
		}
		
		// Step 2
		int k;
		for (k = 0; k < C; k++) {

			// Step 3
			double T[][] = new double[C][M];
			for (j = 0; j < N; j++) {
				//T[k][m]
			}
			
			
		}
		
		
	}
	
	public static void main (String[] args) {
		new DeterministicReweightingAlgorithm();
	}
}
