package tests.growingsocialnetwork;

public class TestAgent {
	
	static int numInterests = 5;
	double[] InterestValues;
	double[] InterestWeights;
	
	public TestAgent (double[] iValues, double[] iWeights) {
		
		InterestValues = iValues;
		InterestWeights = iWeights;
	}

	public double[] getInterestVector () {
		return InterestValues;
	}
	
	public double[] getWeightVector () {
		return InterestWeights;
	}
	
}
