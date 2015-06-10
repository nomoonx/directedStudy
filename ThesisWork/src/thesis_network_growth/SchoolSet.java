package thesis_network_growth;

import java.util.ArrayList;


public class SchoolSet extends InstitutionSet {

	

	static double[][] schoolProbsByYear;

	

	public SchoolSet (String filepath, String elementName, String treeRootName) {
		super(filepath, elementName, treeRootName);
	} // end SchoolSet() constructor

	public static void initSchools () {
		// Create the large table of school probabilities for each year.
		createSchoolProbabilityTable();
	} // end initSchools()
	
	
	
	
	
	public static double[] getSchoolProbabilityTable (int year) {
		// Return the array of school probabilities corresponding to the given year. First subtract the absolute MinYear from the given year to
		// get the index for the array (starting at 0).
		// param year: the year, not as an index, but as the actual year for which to get the probabilities
		// returns: the array of probabilities for the given year
		int yearIndex = year - Configuration.MinYear;
		return schoolProbsByYear[yearIndex];
	}
	
	public static void createSchoolProbabilityTable () {
		// Loop through all schools and store each probability into a double array, and return that array.
		// returns: a double array containing all of the schools' probabilities
		int yearSpan = Configuration.MaxYear - Configuration.MinYear + 1;

		int numElementarySchools = InstitutionSet.ElementarySchoolsList.size();
		
		schoolProbsByYear = new double[yearSpan][numElementarySchools];
		double[][] schoolPopulationsByYear = new double[yearSpan][numElementarySchools];
		double[] TotalSchoolPopSumByYear = new double[yearSpan];
		
		


		int s, y;
		ArrayList school;
		
		int yi;
		
		int sYear = 0;
		int eYear = 0;
		int schPop;
		
		// Initialize all probabilities to 0.0.
		for (s = 0; s < InstitutionSet.ElementarySchoolsList.size(); s++) {
			for (y = 0; y < yearSpan; y++) {
				schoolProbsByYear[y][s] = 0.0;
			} // end y (looping through all years of this school's service)
		} // end for s (looping through schools)
		
		
		// Keep track of all school populations for the years that the school was in service (population is one fixed number for each school).
		for (s = 0; s < numElementarySchools; s++) {
				
			school = (ArrayList)InstitutionSet.ElementarySchoolsList.get(s);
			sYear = Integer.parseInt((String)school.get(Institution_StYr));
			eYear = Integer.parseInt((String)school.get(Institution_EndYr));
			schPop = Integer.parseInt((String)school.get(Institution_Pop));
			
			// Loop through from sYear to eYear for this school.
			for (y = sYear, yi = sYear-Configuration.MinYear; y <= eYear; y++, yi++) {
				schoolPopulationsByYear[yi][s] = schPop;
			} // end y,yi (looping through all years of this school's service)
				
				//schoolProbsByYear[y][s] = 0.25;
		} // end for s (looping through schools)
		
		
		
		// Calculate total school population sums per year. These sums will be used later in calculating the school probabilities.
		for (y = 0; y < yearSpan; y++) {
			TotalSchoolPopSumByYear[y] = 0;

			for (s = 0; s < numElementarySchools; s++) {
				TotalSchoolPopSumByYear[y] += schoolPopulationsByYear[y][s];
			} // end for s (looping through schools)
		} // end y (looping through all years of this school's service)
		
		
		// Calculate the school probabilities for each year, based on the school's population and the sum of all schools' populations in that year.
		double schPop_d;
		for (s = 0; s < numElementarySchools; s++) {
				
			school = (ArrayList)InstitutionSet.ElementarySchoolsList.get(s);
			sYear = Integer.parseInt((String)school.get(Institution_StYr));
			eYear = Integer.parseInt((String)school.get(Institution_EndYr));
			schPop = Integer.parseInt((String)school.get(Institution_Pop));
			schPop_d = (double)schPop;
			
			// Loop through from sYear to eYear for this school.
			for (y = sYear, yi = sYear-Configuration.MinYear; y <= eYear; y++, yi++) {
				schoolProbsByYear[yi][s] = schPop_d / (double)TotalSchoolPopSumByYear[yi];
			} // end y,yi (looping through all years of this school's service)

		} // end for s (looping through schools)
		
		
		
		//System.out.println("===========================================");
		//System.out.println(schoolProbsByYear.length + " x " + schoolProbsByYear[0].length);
		//System.out.println(sYear + " --> " + eYear);
		//DebugTools.printArray(schoolProbsByYear);
		//System.out.println("");
		//System.out.println("===========================================");

	} // end getSchoolProbabilityTable()

	
} // end SchoolSet class
