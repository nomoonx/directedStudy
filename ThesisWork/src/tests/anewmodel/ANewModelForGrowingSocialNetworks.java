package tests.anewmodel;

import java.util.Random;

public class ANewModelForGrowingSocialNetworks {
	
	Random random;

	public ANewModelForGrowingSocialNetworks() {
		// This method for creating a social network is based on the algorithm proposed in "A New Model For Growing Social Networks" by Buscarino,
		// Frasca, Fortuna, and Fiore in 2012.
		
		random = new Random();
		
		
		generateSocialNetwork(0.3);
	}
	
	
	public void generateSocialNetwork (double p) {
		double rndAttachment;
		
		
		// Start with network of three connected nodes.
			// ...


		// Iterate through these steps until some condition is reached.

			// First step - Divide network into communities.
			double[][] M = new double[][] {
					new double[] {5.0, 0.0, 1.0},
					new double[] {0.0, 5.0, 0.0},
					new double[] {1.0, 0.0, 5.0},
			};
			/*double[][] M = new double[][] {
					new double[] {1.0, 2.0, 3.0},
					new double[] {4.0, 5.0, 6.0},
					new double[] {7.0, 8.0, 9.0},
			};*/
			divideIntoCommunities(M, true, false);

			// Second step - Create new node instance.
				// ...
		
			// Third step - Generate random number.
				rndAttachment = random.nextDouble();
		
			// * NOTE * only one of step 4 and step 5 will be used, based upon the rndAttachment value.

			// Fourth step - If random attachment, add random links.
				
				
			// Fifth step - If community attachment, add to community.
	}
	
	public void divideIntoCommunities (double[][] M, boolean useRecursive, boolean useSelfWeights) {
		// Calculate how to divide the network into communities, using "Fast Unfolding Of Communities In Large Networks" algorithm.
		// Converting code from the authors' Matlab code, which was obtained from online.
		
		int size = M.length; // Matrix size.
		int i;
		int Niter;

		
		// Make matrix symmetric.
		double[][] Mt = Matrix.transpose(M);
		
		M = Matrix.add(M, Mt);
		
		//Matrix.display(M);
		
		if (!useSelfWeights) {
			// Remove entries along diagonal by setting them to 0.
			for (i = 0; i < size; i++) {
				M[i][i] = 0.0;
			}
		}
		
		double[][] M2 = Matrix.shallowCopy(M);
		
		// Remove entries along diagonal by setting them to 0.
		for (i = 0; i < size; i++) {
			M2[i][i] = 0.0;
		}
		Matrix.display(M);
		//Matrix.display(M2);
		
		double m = Matrix.sum(M);
		Niter = 1;
		
		if (m == 0 || size == 1) {
			System.out.println("No more possible decomposition.");
			
			return;
		}
		
		///////////////////////////////////////
		// Main loop
		///////////////////////////////////////
		double[] K = Matrix.sumAllColumns(M);
		double[] SumTot = Matrix.sumAllColumns(M);
		double SumIn = Matrix.diag(M); /////////// ??? need vector of sums or one sum?
		System.out.println("SumIn = " + SumIn);
		
		
		int[] COM = new int[size];
		for (i = 0; i < size; i++) {
			COM[i] = i+1;
		}
		Matrix.display(COM);
		
		int k;
		double[][] Neighbours = new double[size][size];
		for (k = 0; k < size; k++) {
			//Neighbours[k] = Matrix.find(M2,new double[size];
		}
		
		
	}
	
	
	public static void main (String[] args) {
		new ANewModelForGrowingSocialNetworks();
	}
}
