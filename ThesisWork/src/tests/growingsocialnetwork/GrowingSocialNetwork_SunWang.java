package tests.growingsocialnetwork;

public class GrowingSocialNetwork_SunWang {
	
	public GrowingSocialNetwork_SunWang() {
		TestAgent Adam = new TestAgent(new double[] {15.0, 0.0, 46.0, 32.0, 0.0}, new double[] {0.4, 0.7, 0.8, 0.3, 0.5});
		TestAgent Eve = new TestAgent(new double[] {16.0, 0.0, 45.0, 36.0, 0.0}, new double[] {0.4, 0.6, 0.7, 0.4, 0.5});
		
		TestAgent Serpent = new TestAgent(new double[] {16.0, 0.0, 45.0, 34.0, 0.5}, new double[] {0.4, 0.6, 0.7, 0.4, 0.5});
		
		
		
		double IS_AE = calculateSimilarity(Adam, Eve);
		System.out.println("IS of Adam and Eve:  " + IS_AE);
		
		double IS_EA = calculateSimilarity(Eve, Adam);
		System.out.println("IS of Eve and Adam:  " + IS_EA);
		
		//double IS_AA = calculateSimilarity(Adam, Adam);
		//System.out.println("IS of Adam and Adam:  " + IS_AA);
		
		//double IS_EE = calculateSimilarity(Eve, Eve);
		//System.out.println("IS of Eve and Eve:  " + IS_EE);
		
		double IS_ES = calculateSimilarity(Eve, Serpent);
		System.out.println("IS of Eve and Serpent:  " + IS_ES);
		
		double IS_SE = calculateSimilarity(Serpent, Eve);
		System.out.println("IS of Serpent and Eve:  " + IS_SE);
		
		
	}
	
	public void createRandomAgent () {
		TestAgent agent;
		
		double[] interestValues = new double[TestAgent.numInterests];
		double[] interestWeights = new double[TestAgent.numInterests];
		
		// Randomize numbers for Values and Weights ...
		
		agent = new TestAgent(interestValues, interestWeights);
		
	}
	
	public void Rule1 () {
		TestAgent Adam = new TestAgent(new double[] {15.0, 0.0, 46.0, 32.0, 0.0}, new double[] {0.4, 0.7, 0.8, 0.3, 0.5});
	}
	
	
	
	public double calculateSimilarity (TestAgent agentA, TestAgent agentB) {
		// Calculate the IS (interest similarity) between two agents, as per Eq. 1 in 'Modeling Adaptive Behaviors on Growing Social Networks'.
		//
		// param agentA: the first agent from which the comparison will take place.
		// param agentB: the second agent, who will be compared to agentA.
		// output: calculated IS value.

		double IS;
		
		int i;
		
		double tmp;
		double i_diff;
		double w_sqr;
		
		double sumNumerator = 0.0;
		double sumDenominator = 0.0;
		
		double[] agentAWeights = agentA.getWeightVector();
		double[] agentAInterests = agentA.getInterestVector();
		double[] agentBInterests = agentB.getInterestVector();
		
		for (i = 0; i < TestAgent.numInterests; i++) {

			// Pre-compute because this is used both in the numerator and denominator.
			w_sqr = agentAWeights[i] * agentAWeights[i];

			// Perform summation for numerator of equation.
			i_diff = agentAInterests[i] - agentBInterests[i];
			tmp = w_sqr * (i_diff * i_diff);
			sumNumerator += tmp;
			
			// Perform summation for denominator of equation.
			sumDenominator += w_sqr;
		} // end for i
		
		//System.out.println(sumNumerator + " / " + sumDenominator);
		
		IS = 1.0 - Math.sqrt(sumNumerator / sumDenominator);
		
		return IS;
	}
	
	
	
	public static void main (String[] args) {
		new GrowingSocialNetwork_SunWang();
	}
}
