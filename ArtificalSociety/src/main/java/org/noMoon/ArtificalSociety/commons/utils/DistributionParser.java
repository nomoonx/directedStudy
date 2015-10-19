package org.noMoon.ArtificalSociety.commons.utils;



public class DistributionParser {

	public static double parseStatisticalDistribution (String distr) {
		
		double rndDistrValue = 0.0;
		
		
		//normal(28,3)
		//normal(2.0, 1.4)
		
		
		int openParPos = distr.indexOf("(");
		int closeParPos = distr.indexOf(")");
		
		
		String distrModel = distr.substring(0, openParPos);
		String paramString = distr.substring(openParPos+1, closeParPos);
		String[] params=paramString.split(",");
		
		//System.out.println(distrModel);
		//System.out.println(paramString);
		//DebugTools.printArray(distrParams);
		

		// Dispatch to the correct Distribution functions.
		if (distrModel.equals("normal")) {
			rndDistrValue = Distribution.normal(Double.valueOf(params[0]), Double.valueOf(params[1]));
			//System.out.println(rndDistrValue);
		//} else if (distrModel.equals("poisson")) {
			// ...
		//} else if (distrModel.equals("chi2")) {
			// ...
			
		} else {
			System.err.println("In DistributionParser->parseStatisticalDistribution(); invalid distribution requested: " + distrModel);
		} // end if (determining which distribution is being used)
		
		//Distribution.normal(mean, std);
		
		
		return rndDistrValue;
		
	} // end parseStatisticalDistribution()
	
} // end DistributionParser class
