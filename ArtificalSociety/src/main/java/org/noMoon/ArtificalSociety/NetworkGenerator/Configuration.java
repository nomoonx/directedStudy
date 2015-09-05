package org.noMoon.ArtificalSociety.NetworkGenerator;

import org.noMoon.ArtificalSociety.commons.utils.XMLReader;
import org.noMoon.ArtificalSociety.society.DO.Society;
import org.noMoon.ArtificalSociety.society.services.SocietyService;

import java.util.ArrayList;
import java.util.Arrays;


public class Configuration {

    static SocietyService societyService;

    public static String Society_Id;
	/* ---------------------------------------------
		ACTIVE SIMULATION PARAMETERS
	--------------------------------------------- */
	public static int N_YearsToRun;
	public static int YearToDisallowInterbreeding;
	
	
	/* ---------------------------------------------
		SETTING
	--------------------------------------------- */
	public static String SocietyName;
	public static int SocietyYear;
	public static int MinYear;
	public static int MaxYear;

	/* ---------------------------------------------
		POPULATION SIZE
	--------------------------------------------- */
	public static int N_CouplesMarried;
	public static int N_CouplesDating;
	public static int N_Couples;
	public static int N_Singles;
	//public static int N_Immigrants;
	public static int N_Population_Size; // Total population of the above groups' sizes.

	
	/* ---------------------------------------------
		CONNECTIVITY
	--------------------------------------------- */
	public static double FriendFactor; // The decimal number used in determining the number of friendships for each agent.
	
	
	/* ---------------------------------------------
		LIFE EXPECTANCY
	--------------------------------------------- */
	public static String LifeExpectancyDistr;
	
	/* ---------------------------------------------
		AGE PARAMETERS
	--------------------------------------------- */
	public static int MinMarriedAge;
	public static int MaxMarriedAge;
	public static int MinDatingAge;
	public static int MaxDatingAge;
	public static String MarriageAgeDistr;
	public static String DatingAgeDistr;
	public static int MaxAgeHaveChildren;

	/* ---------------------------------------------
		GEOGRAPHY
	--------------------------------------------- */
	public static ArrayList<String> OtherCities;
	public static ArrayList<String> AllCities;
	public static double p_alwaysLivedInHometown;
	public static int numAvgYearsBetweenMoves;
	public static int minYearsBetweenMoves;
	
	/* ---------------------------------------------
		INTERESTS AND RELATIONSHIP STRENGTH
	--------------------------------------------- */
	public static double CoupleBreakupThresholdStrength;
	public static double CoupleGetMarriedThresholdStrength;
	
	public static int NumInterests;
	public static double[] RelStrengthWeights_PreChildren;
	public static double[] RelStrengthWeights_PostChildren;
	/*
	public static double RelStrengthWeight_PRE_Type;
	public static double RelStrengthWeight_PRE_Children;
	public static double RelStrengthWeight_PRE_Race;
	public static double RelStrengthWeight_PRE_Religion;
	public static double RelStrengthWeight_PRE_YearsMarried;
	public static double RelStrengthWeight_PRE_Education;
	public static double RelStrengthWeight_PRE_Random;
	
	public static double RelStrengthWeight_POST_Type;
	public static double RelStrengthWeight_POST_Children;
	public static double RelStrengthWeight_POST_Race;
	public static double RelStrengthWeight_POST_Religion;
	public static double RelStrengthWeight_POST_YearsMarried;
	public static double RelStrengthWeight_POST_Education;
	public static double RelStrengthWeight_POST_Random;
	*/

	/* ---------------------------------------------
		EDUCATION PARAMETERS
	--------------------------------------------- */
	public static int SchoolStartAge;
	public static int SchoolFinishAge;
	public static int DeltaNumYearsPersonSocializeWithInSchool;

	/* ---------------------------------------------
		CAREER PARAMETERS
	--------------------------------------------- */
	public static int WorkingAgeMin;
	public static int WorkingAgeMax;
	public static int SalarySTD;
	public static int IncomeClassThreshold;
	public static double p_getJobFromUnemployment;
	
	// TODO
	/* ---------------------------------------------
		RELATIONSHIP PARAMETERS
		{0 = Single; 1 = Married; 2 = Dating; 3 = Widowed}
	--------------------------------------------- */

	// Marriage Locations.
	public static double p_likelihoodMarriedCoupleHasChildren;
	public static String marriageChildrenDistr;
	public static int marriageChildrenMin;
	public static int marriageChildrenMax;
	
	public static int MinNumYearsMarriedBeforeChildren;
	
	// How many years into marriage the parents have children
	public static String ParentHaveChildYearDistr;
	public static int ParentHaveChildYearMin;
	public static int ParentHaveChildYearMax;
	
	//public static double LDRel_MainlyLiveInSociety;	// This indicates the probability that a relationship containing a long-distance period will be mainly set in the local society (for all their married life, except the one period when one person lives elsewhere).
	
	/* ---------------------------------------------
		AGE DEMOGRAPHICS
	--------------------------------------------- */
	public static int AdultAgeMin;
	public static int AdultAgeMax;
	
	/* ---------------------------------------------
		RACE DEMOGRAPHICS
	--------------------------------------------- */
	public static String[] RaceLabels;
	public static double[] RaceDistr;
	public static double[][] p_same_race_for_spouse;
	public static double[][] p_same_race_for_child;
	public static double[] race_interest_value;

		
	/* ---------------------------------------------
		RELIGION DEMOGRAPHICS
	--------------------------------------------- */
	public static String[] ReligionLabels;
	public static double[] ReligionDistr;
	public static double[][] p_same_religion_for_spouse;
	//public static double[][] xp_same_religion_for_child;
	public static double[] religion_interest_value;
	//public static double[] xreligion_marital_strength;
		
		
		
		
	// ------------------------------------------------------------------------------------
	// 		METHODS
	// ------------------------------------------------------------------------------------

//	public static void SetHardcodedValues () {
//
//		N_YearsToRun = 15;
//		YearToDisallowInterbreeding = 2015;
//
//		SocietyName = "London";
//		SocietyYear = 2015;
//		MinYear = 1900;
//		MaxYear = 2200; // This will depend on the number of simulation steps!
//
//		N_CouplesMarried = 3;
//		N_CouplesDating = 0;
//		N_Couples = N_CouplesMarried + N_CouplesDating;	// May not really need this parameter any more.
//		N_Singles = 0;
//		//N_Immigrants = 0;
//		N_Population_Size = 2*N_Couples + N_Singles;
//
//		LifeExpectancyDistr = "normal(80,4.0)";
//
//		MinMarriedAge = 20;
//		MaxMarriedAge = 90;
//		MinDatingAge = 15;
//		MaxDatingAge = 35;
//		MarriageAgeDistr = "normal(28.0,1.0)";
//		DatingAgeDistr = "normal(25.0,2.0)";
//		MaxAgeHaveChildren = 45;
//
//		//N_ImmigrationRate = 0;
//		//N_EmigrationRate = 0;
//		//OtherCities = new String[] {"Toronto", "Ottawa", "New York", "Nairobi", "Sydney", "Paris", "Rome", "Cairo", "Auckland", "Shanghai"};
//		//AllCities = new String[] {"London", "Toronto", "Ottawa", "New York", "Nairobi", "Sydney", "Paris", "Rome", "Cairo", "Auckland", "Shanghai"};
//		String[] OtherCities_str = new String[] {"Toronto", "Ottawa", "New York", "North Bay", "Montreal", "Vancouver", "Buffalo", "Chicago", "Winnipeg", "Windsor"};
//		String[] AllCities_str = new String[] {"London", "Toronto", "Ottawa", "New York", "North Bay", "Montreal", "Vancouver", "Buffalo", "Chicago", "Winnipeg", "Windsor"};
//		OtherCities = ArrayTools.stringArrayToArrayList(OtherCities_str);
//		AllCities = ArrayTools.stringArrayToArrayList(AllCities_str);
//
//		p_alwaysLivedInHometown = 0.7;
//		//timesMovedMean = 2;
//		//timesMovedStd = 0.7;
//		//timesMovedMin = 1;
//		//timesMovedMax = 4;
//		numAvgYearsBetweenMoves = 10;
//		minYearsBetweenMoves = 1;
//
//		CoupleBreakupThresholdStrength = 0.4;
//		CoupleGetMarriedThresholdStrength = 0.9;
//
//		NumInterests = 7;
//
//		RelStrengthWeights_PreChildren = new double[10];
//		RelStrengthWeights_PreChildren[RelationshipCalculator.REL_STRENGTH_TYPE] = 0.1;
//		RelStrengthWeights_PreChildren[RelationshipCalculator.REL_STRENGTH_CHILDREN] = 0;
//		RelStrengthWeights_PreChildren[RelationshipCalculator.REL_STRENGTH_RACE] = 0.1;
//		RelStrengthWeights_PreChildren[RelationshipCalculator.REL_STRENGTH_RELIGION] = 0.2;
//		RelStrengthWeights_PreChildren[RelationshipCalculator.REL_STRENGTH_YEARSMARRIED] = 0.1;
//		RelStrengthWeights_PreChildren[RelationshipCalculator.REL_STRENGTH_EDUCATION] = 0.1;
//		RelStrengthWeights_PreChildren[RelationshipCalculator.REL_STRENGTH_RANDOM] = 0.2;
//		RelStrengthWeights_PreChildren[RelationshipCalculator.REL_STRENGTH_INCOME] = 0.0;
//		RelStrengthWeights_PreChildren[RelationshipCalculator.REL_STRENGTH_INTEREST] = 0.1;
//		RelStrengthWeights_PreChildren[RelationshipCalculator.REL_STRENGTH_LOCATION] = 0.1;
//
//
//		RelStrengthWeights_PostChildren = new double[10];
//		RelStrengthWeights_PostChildren[RelationshipCalculator.REL_STRENGTH_TYPE] = 0.1;
//		RelStrengthWeights_PostChildren[RelationshipCalculator.REL_STRENGTH_CHILDREN] = 0;
//		RelStrengthWeights_PostChildren[RelationshipCalculator.REL_STRENGTH_RACE] = 0.1;
//		RelStrengthWeights_PostChildren[RelationshipCalculator.REL_STRENGTH_RELIGION] = 0.2;
//		RelStrengthWeights_PostChildren[RelationshipCalculator.REL_STRENGTH_YEARSMARRIED] = 0.1;
//		RelStrengthWeights_PostChildren[RelationshipCalculator.REL_STRENGTH_EDUCATION] = 0.1;
//		RelStrengthWeights_PostChildren[RelationshipCalculator.REL_STRENGTH_RANDOM] = 0.2;
//		RelStrengthWeights_PostChildren[RelationshipCalculator.REL_STRENGTH_INCOME] = 0.0;
//		RelStrengthWeights_PostChildren[RelationshipCalculator.REL_STRENGTH_INTEREST] = 0.1;
//		RelStrengthWeights_PostChildren[RelationshipCalculator.REL_STRENGTH_LOCATION] = 0.1;
//		/*
//		RelStrengthWeights_PostChildren[RelationshipCalculator.REL_STRENGTH_TYPE] = 0.15;
//		RelStrengthWeights_PostChildren[RelationshipCalculator.REL_STRENGTH_CHILDREN] = 0.15;
//		RelStrengthWeights_PostChildren[RelationshipCalculator.REL_STRENGTH_RACE] = 0.15;
//		RelStrengthWeights_PostChildren[RelationshipCalculator.REL_STRENGTH_RELIGION] = 0.15;
//		RelStrengthWeights_PostChildren[RelationshipCalculator.REL_STRENGTH_YEARSMARRIED] = 0.15;
//		RelStrengthWeights_PostChildren[RelationshipCalculator.REL_STRENGTH_EDUCATION] = 0.15;
//		RelStrengthWeights_PostChildren[RelationshipCalculator.REL_STRENGTH_RANDOM] = 0.1;
//		RelStrengthWeights_PostChildren[RelationshipCalculator.REL_STRENGTH_INTEREST] = 0.0;
//		*/
//
//
//		SchoolStartAge = 4;
//		SchoolFinishAge = 18;
//		DeltaNumYearsPersonSocializeWithInSchool = 1;
//
//		WorkingAgeMin = 17;
//		WorkingAgeMax = 65;
//		SalarySTD = 10000;
//		IncomeClassThreshold = 30000;
//		p_getJobFromUnemployment = 0.6;
//
//		//xp_one_sahp = 0.85;
//		//xp_sahp_is_mother = 0.9;
//
//		//xp_marriedCoupleAlwaysLivedTogether = 0.8; // This indicates the probability that the couple always lives together throughout their entire marriage.
//		//xp_datingCoupleAlwaysLivedTogether = 0.1; // This indicates the probability that the couple always lives together throughout their entire marriage.
//		p_likelihoodMarriedCoupleHasChildren = 0.5;
//
//
//		//marriageChildrenDistr = "normal(1.0,0.5)";
//		//marriageChildrenMin = 1; // Not zero because this is assuming we already decided there WILL be children.
//		//marriageChildrenMax = 2; // Should this be 4?
//		marriageChildrenDistr = "normal(2.0,1.4)";
//		marriageChildrenMin = 1; // Not zero because this is assuming we already decided there WILL be children.
//		marriageChildrenMax = 5; // Should this be 4?
//
//		MinNumYearsMarriedBeforeChildren = 1;
//
//		ParentHaveChildYearDistr = "normal(5.0,1.25)";
//		ParentHaveChildYearMin = 0;
//		ParentHaveChildYearMax = 10;
//
//		AdultAgeMin = 25;
//		AdultAgeMax = 70;
//
//		RaceLabels = new String[] {"Caucasian", "African American", "Aboriginal", "Indian", "East Asian", "Hispanic"};
//		RaceDistr = new double[] {0.4, 0.15, 0.05, 0.1, 0.2, 0.1};
//		p_same_race_for_spouse = new double[][] {
//			{.70, .09, .05, .01, .05, .10},
//			{.09, .60, .01, .15, .05, .10},
//			{.05, .01, .80, .00, .04, .10},
//			{.01, .15, .00, .70, .04, .10},
//			{.05, .05, .04, .04, .72, .10},
//			{.10, .10, .10, .10, .10, .50}
//		};
//		race_interest_value = new double[] {0.5, 0.7, 0.9, 0.6, 0.6, 0.8};
//
//
//
//
//		/*
//		RaceLabels = new String[] {"Caucasian", "African American", "Aboriginal", "Indian", "East Asian"};
//		RaceDistr = new double[] {0.5, 0.15, 0.05, 0.1, 0.2};
//		p_same_race_for_spouse = new double[][] {
//			{.80, .09, .05, .01, .05},
//			{.09, .70, .01, .15, .05},
//			{.05, .01, .90, .00, .04},
//			{.01, .15, .00, .80, .04},
//			{.05, .05, .04, .04, .82}
//		};
//		p_same_race_for_child = new double[][] {
//				{.80, .09, .05, .01, .05},
//				{.09, .70, .01, .15, .05},
//				{.05, .01, .90, .00, .04},
//				{.01, .15, .00, .80, .04},
//				{.05, .05, .04, .04, .82}
//			};
//		race_interest_value = new double[] {0.5, 0.7, 0.9, 0.6, 0.6};
//		*/
//
//		ReligionLabels = new String[] {"Catholicism", "Protestantism", "Judaism", "Islam", "Buddhism", "Hinduism", "None"};
//		ReligionDistr = new double [] {0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0};
//		//ReligionDistr = new double [] {0.4, 0.2, 0.1, 0.1, 0.05, 0.05, 0.1};
//		p_same_religion_for_spouse = new double[][] {
//				{.50, .20, .05, .00, .05, .05, .15},
//				{.20, .70, .07, .00, .01, .01, .01},
//				{.05, .07, .85, .00, .00, .01, .02},
//				{.00, .00, .00, 1.0, .00, .00, .00},
//				{.05, .01, .00, .00, .54, .20, .20},
//				{.05, .01, .01, .00, .20, .70, .03},
//				{.15, .01, .02, .00, .20, .03, .59}
//		};
//		/*
//		xp_same_religion_for_child = new double[][] {
//				{.50, .20, .05, .00, .05, .05, .15},
//				{.20, .70, .07, .00, .01, .01, .01},
//				{.05, .07, .85, .00, .00, .01, .02},
//				{.00, .00, .00, 1.0, .00, .00, .00},
//				{.05, .01, .00, .00, .54, .20, .20},
//				{.05, .01, .01, .00, .20, .70, .03},
//				{.15, .01, .02, .00, .20, .03, .59}
//		};
//		*/
//		religion_interest_value = new double[] {0.6, 0.9, 0.9, 0.9, 0.6, 0.7, 0.2};
//		//xreligion_marital_strength = new double[] {0.8, 0.9, 0.8, 0.9, 0.7, 0.7, 0.0};
//	}




	public static void LoadConfigValuesFromFile (String filename) {
		// ...

		XMLReader xmlReader = new XMLReader();

		try {
			xmlReader.loadConfigFile(filename);

			N_YearsToRun = xmlReader.getInteger("N_YearsToRun");
			YearToDisallowInterbreeding = xmlReader.getInteger("YearToDisallowInterbreeding");
			
			SocietyName = xmlReader.getString("SocietyName");
			SocietyYear = xmlReader.getInteger("SocietyYear");
            Society newSociety=new Society();
            newSociety.setSocietyYear(SocietyYear);
            newSociety.setSocietyName(SocietyName);
            Society_Id=societyService.insertNewSociety(newSociety);

			MinYear = xmlReader.getInteger("MinYear");
			MaxYear = xmlReader.getInteger("MaxYear");
			
			N_CouplesMarried = xmlReader.getInteger("N_CouplesMarried");
			N_CouplesDating = xmlReader.getInteger("N_CouplesDating");
			N_Singles = xmlReader.getInteger("N_Singles");
			N_Couples = N_CouplesMarried + N_CouplesDating; 			// * NOT DIRECTLY IN FILE. *
			N_Population_Size = 2*N_Couples + N_Singles; 				// * NOT DIRECTLY IN FILE. *
			
			FriendFactor = xmlReader.getDouble("FriendFactor");
			
			LifeExpectancyDistr = xmlReader.getString("LifeExpectancyDistr");

			MinMarriedAge = xmlReader.getInteger("MinMarriedAge");
			MaxMarriedAge = xmlReader.getInteger("MaxMarriedAge");
			MinDatingAge = xmlReader.getInteger("MinDatingAge");
			MaxDatingAge = xmlReader.getInteger("MaxDatingAge");
			MarriageAgeDistr = xmlReader.getString("MarriageAgeDistr");
			DatingAgeDistr = xmlReader.getString("DatingAgeDistr");
			MaxAgeHaveChildren = xmlReader.getInteger("MaxAgeHaveChildren");
			AdultAgeMin = xmlReader.getInteger("AdultAgeMin");
			AdultAgeMax = xmlReader.getInteger("AdultAgeMax");

			OtherCities= new ArrayList<String>(Arrays.asList(xmlReader.getStringArray("OtherCities")));
			AllCities = new ArrayList<String>(Arrays.asList(xmlReader.getStringArray("AllCities")));
			
			p_alwaysLivedInHometown = xmlReader.getDouble("p_alwaysLivedInHometown");
			numAvgYearsBetweenMoves = xmlReader.getInteger("numAvgYearsBetweenMoves");
			minYearsBetweenMoves = xmlReader.getInteger("minYearsBetweenMoves");
			
			CoupleBreakupThresholdStrength = xmlReader.getDouble("CoupleBreakupThresholdStrength");
			CoupleGetMarriedThresholdStrength = xmlReader.getDouble("CoupleGetMarriedThresholdStrength");
			NumInterests = xmlReader.getInteger("NumInterests");

			RelStrengthWeights_PreChildren = xmlReader.getDoubleArray("RelStrengthWeights_PreChildren");
			RelStrengthWeights_PostChildren = xmlReader.getDoubleArray("RelStrengthWeights_PostChildren");

			SchoolStartAge = xmlReader.getInteger("SchoolStartAge");
			SchoolFinishAge = xmlReader.getInteger("SchoolFinishAge");
			DeltaNumYearsPersonSocializeWithInSchool = xmlReader.getInteger("DeltaNumYearsPersonSocializeWithInSchool");

			WorkingAgeMin = xmlReader.getInteger("WorkingAgeMin");
			WorkingAgeMax = xmlReader.getInteger("WorkingAgeMax");
			SalarySTD = xmlReader.getInteger("SalarySTD");
			IncomeClassThreshold = xmlReader.getInteger("IncomeClassThreshold");
			p_getJobFromUnemployment = xmlReader.getDouble("p_getJobFromUnemployment");
			
			p_likelihoodMarriedCoupleHasChildren = xmlReader.getDouble("p_likelihoodMarriedCoupleHasChildren");
			marriageChildrenDistr = xmlReader.getString("marriageChildrenDistr");
			marriageChildrenMin = xmlReader.getInteger("marriageChildrenMin");
			marriageChildrenMax = xmlReader.getInteger("marriageChildrenMax");
			MinNumYearsMarriedBeforeChildren = xmlReader.getInteger("MinNumYearsMarriedBeforeChildren");
			ParentHaveChildYearDistr = xmlReader.getString("ParentHaveChildYearDistr");
			ParentHaveChildYearMin = xmlReader.getInteger("ParentHaveChildYearMin");
			ParentHaveChildYearMax = xmlReader.getInteger("ParentHaveChildYearMax");
			
			RaceLabels = xmlReader.getStringArray("RaceLabels");
			RaceDistr = xmlReader.getDoubleArray("RaceDistr");
			p_same_race_for_spouse = xmlReader.getDoubleMatrix("p_same_race_for_spouse");
			race_interest_value = xmlReader.getDoubleArray("race_interest_value");
			
			ReligionLabels = xmlReader.getStringArray("ReligionLabels");
			ReligionDistr = xmlReader.getDoubleArray("ReligionDistr");
			p_same_religion_for_spouse = xmlReader.getDoubleMatrix("p_same_religion_for_spouse");
			religion_interest_value = xmlReader.getDoubleArray("religion_interest_value");
			
			
			//double x = xmlReader.getDouble("CoupleGetMarriedThresholdStrength");
			
			//System.out.println();
			
		} catch (Exception e) {
			System.err.println("Configuration->LoadConfigFilesFromFile(); could not read the XML configuration file.");			
			//e.printStackTrace();
		}
	}

	
	
	public static void DestroyConfiguration () {
		// Release memory from configuration variables.
		
		OtherCities = null;
		AllCities = null;
		RelStrengthWeights_PreChildren = null;
		RelStrengthWeights_PostChildren = null;
		RaceLabels = null;
		RaceDistr = null;
		p_same_race_for_spouse = null;
		p_same_race_for_child = null;
		race_interest_value = null;
		ReligionLabels = null;
		ReligionDistr = null;
		p_same_religion_for_spouse = null;
		religion_interest_value = null;
		
	} // end DestroyConfiguration()


    public void setSocietyService(SocietyService societyService) {
        this.societyService = societyService;
    }
}
