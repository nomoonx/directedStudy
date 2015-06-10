package thesis_network_growth.m1_dyadic;


import java.util.ArrayList;

import thesis_network_growth.ArtificialSociety;
import thesis_network_growth.AttributeAssigner;
import thesis_network_growth.Careers;
import thesis_network_growth.Club;
import thesis_network_growth.Configuration;
import thesis_network_growth.CreateJUNGGraph;
import thesis_network_growth.DebugTools;
import thesis_network_growth.Distribution;
import thesis_network_growth.FriendshipCalculator;
import thesis_network_growth.Group;
import thesis_network_growth.GroupGenerator;
import thesis_network_growth.InputSocietyCompactly;
import thesis_network_growth.InstitutionSet;
import thesis_network_growth.Logger;
import thesis_network_growth.MemoryUsage;
import thesis_network_growth.OutputResults;
import thesis_network_growth.OutputSocietyCompactly;
import thesis_network_growth.OutputTrialResults;
import thesis_network_growth.Person;
import thesis_network_growth.PersonGenerator;
import thesis_network_growth.PersonGroupAdder;
import thesis_network_growth.RelationshipCalculator;
import thesis_network_growth.SchoolSet;
import thesis_network_growth.SimulationStepper;
import thesis_network_growth.SocietyQuery;
import thesis_network_growth.TempleSet;
import thesis_network_growth.Tree;
import thesis_network_growth.ValidationTools;
import static thesis_network_growth.Configuration.*;

public class Dyadic_NetworkGenerator {
	
	
	
	
	//public static ArrayList AllPersons;
	//private static ArrayList AllInstitutions;
	
	
	//////////////////////////////////////////////////////
	// Program entry.
	//////////////////////////////////////////////////////
	public static void main (String[] args) {


		SimulationStepper sim;
		
		int TRIAL_NUM = 10;
		int t;
		
		OutputTrialResults trialResults = new OutputTrialResults();
		
		double genTime, loadTime;
		
		long genMemory, loadMemory;

		double q1Time, q2Time, q3Time, q4Time, q5Time, q6Time, q7Time;
		
		//int i;
		int numQueryCalls = 10;
		
		// -----------------------------------------------------------------
		// Loop through to run multiple trials.
		// -----------------------------------------------------------------
		for (t = 0; t < TRIAL_NUM; t++) {

			
			//trialResults = new OutputTrialResults();
			
			
			
			
			genTime = StartSimulation_Scratch();
			//System.out.println("Gen Time = " + genTime);
			//sim = new SimulationStepper( ArtificialSociety.getSociety(), t );
			//sim.runToPopulationThreshold( 100 );
			//sim.begin( N_YearsToRun );
			
			//genMemory = MemoryUsage.GetMemoryUsage();
			//System.out.println(ArtificialSociety.getSociety().size() + "," + genTime + "," + genMemory);
			
			//sim = new SimulationStepper( ArtificialSociety.getSociety(), t );
			//sim.begin( 5 );
			
			// Genesis populations & fate.
			//System.out.println("Year: " + Configuration.SocietyYear + "\tPopulation size = " + ArtificialSociety.getSociety().size());
			//trialResults.appendFileContents(  Configuration.SocietyYear + "," + ArtificialSociety.getSociety().size() );
			

	
	
			//sim = new SimulationStepper( ArtificialSociety.getSociety(), t );
			//sim.begin( N_YearsToRun );

			// Free memory.
			//MemoryUsage.cleanup();
			
			
			
			//MemoryUsage.GetMemoryUsage();
			
			
			
			// ------------------------------------
			// LOAD SOCIETY FROM FILE
			// ------------------------------------
			//loadTime = StartSimulation_Load();
			//System.out.println("Load Time = " + loadTime);
			//sim = new SimulationStepper( ArtificialSociety.getSociety(), t );
			//sim.begin( N_YearsToRun );
			
			//loadMemory = MemoryUsage.GetMemoryUsage();
			//System.out.println(loadMemory);
			
			
			
			//sim = new SimulationStepper( ArtificialSociety.getSociety(), t );
			//sim.begin( 5 );
			
			//trialResults.appendFileContents(  Configuration.SocietyYear + "," + ArtificialSociety.getSociety().size() + "," + genTime + "," + loadTime ); // Population and timing!
			
			
			//trialResults.appendFileContents(  Configuration.SocietyYear + "," + ArtificialSociety.getSociety().size() + "," + genMemory + "," + loadMemory ); // Memory usage.
			
			
			// Comparing generation and loading times.
			//trialResults.writeToFile("Trial Results/Times/TimesToCreateOrLoad_A_" + t + ".txt");	// Population times. (A => 10 marriages init)
			//trialResults.writeToFile("Trial Results/Times/TimesToCreateOrLoad_B_" + t + ".txt");	// Population times. (B => 30 marriages init)
			//trialResults.writeToFile("Trial Results/Times/TimesToCreateOrLoad_C_" + t + ".txt");	// Population times. (C => 50 marriages init)
			//trialResults.writeToFile("Trial Results/Times/TimesToCreateOrLoad_D_" + t + ".txt");	// Population times. (D => 100 marriages init)
			//trialResults.writeToFile("Trial Results/Times/TimesToCreateOrLoad_E_" + t + ".txt");	// Population times. (E => 75 marriages init)
			//trialResults.writeToFile("Trial Results/Times/TimesToCreateOrLoad_F_" + t + ".txt");	// Population times. (F => 200 marriages init)
			//trialResults.writeToFile("Trial Results/Times/TimesToCreateOrLoad_G_" + t + ".txt");	// Population times. (G => 150 marriages init)
			//trialResults.writeToFile("Trial Results/Times/TimesToCreateOrLoad_H_" + t + ".txt");	// Population times. (G => 120 marriages init)
			//trialResults.writeToFile("Trial Results/Times/TimesToCreateOrLoad_I_" + t + ".txt");	// Population times. (G => 175 marriages init)

			// Comparing generation and loading times.
			//trialResults.writeToFile("Trial Results/Times (larger populations)/TimesToCreateOrLoad_A_" + t + ".txt");	// Population times. (A => 50 marriages init)
			//trialResults.writeToFile("Trial Results/Times (larger populations)/TimesToCreateOrLoad_B_" + t + ".txt");	// Population times. (B => 100 marriages init)
			//trialResults.writeToFile("Trial Results/Times (larger populations)/TimesToCreateOrLoad_C_" + t + ".txt");	// Population times. (C => 200 marriages init)
			//trialResults.writeToFile("Trial Results/Times (larger populations)/TimesToCreateOrLoad_D_" + t + ".txt");	// Population times. (D => 300 marriages init)
			//trialResults.writeToFile("Trial Results/Times (larger populations)/TimesToCreateOrLoad_E_" + t + ".txt");	// Population times. (E => 400 marriages init)
			//trialResults.writeToFile("Trial Results/Times (larger populations)/TimesToCreateOrLoad_F_" + t + ".txt");	// Population times. (F => 150 marriages init)
			//trialResults.writeToFile("Trial Results/Times (larger populations)/TimesToCreateOrLoad_G_" + t + ".txt");	// Population times. (G => 250 marriages init)
			//trialResults.writeToFile("Trial Results/Times (larger populations)/TimesToCreateOrLoad_H_" + t + ".txt");	// Population times. (H => 350 marriages init)
			
			
			// Comparing generation times among different models.
			//trialResults.appendFileContents(  Configuration.SocietyYear + "," + ArtificialSociety.getSociety().size() + "," + genTime); // Population and timing!
			//trialResults.writeToFile("Trial Results/Models - Connectedness/test_Dyadic_" + t + ".txt");

			// IV - Population.
			//trialResults.appendFileContents(  Configuration.SocietyYear + "," + ArtificialSociety.getSociety().size() ); // Population for measuring internal validity.
	
			// IV - Total number of friendships.
			//trialResults.appendFileContents(  Configuration.SocietyYear + "," + SocietyQuery.GetTotalFriendships() ); // Total number of friendships for measuring internal validity.

			// IV - Total number of local people.
			//trialResults.appendFileContents(  Configuration.SocietyYear + "," + ArtificialSociety.getSociety().size() + "," + SocietyQuery.GetTotalLocalPeople() ); // Total number of local persons for measuring internal validity.
			
			
			// --------------------
			// Queries.
			// --------------------
			//q1Time = SocietyQuery.executeQuery1_AddPerson(numQueryCalls);
			//q2Time = SocietyQuery.executeQuery2_AddCouple(numQueryCalls);
			//q3Time = SocietyQuery.executeQuery3_KillPerson(numQueryCalls);
			//q4Time = SocietyQuery.executeQuery4_GetRaceNumbers(numQueryCalls);
			//q5Time = SocietyQuery.executeQuery5_GetTotalFriendships(numQueryCalls);
			//q6Time = SocietyQuery.executeQuery6_GetTotalLocalPeople(numQueryCalls);
			//q7Time = SocietyQuery.executeQuery7_GetNumberOfSingleFriends(numQueryCalls);
			
			
			//trialResults.appendFileContents(  Configuration.SocietyYear + "," + ArtificialSociety.getSociety().size() + "," + q1Time ); // Q1
			//trialResults.appendFileContents(  Configuration.SocietyYear + "," + ArtificialSociety.getSociety().size() + "," + q2Time ); // Q2
			//trialResults.appendFileContents(  Configuration.SocietyYear + "," + ArtificialSociety.getSociety().size() + "," + q3Time ); // Q3
			//trialResults.appendFileContents(  Configuration.SocietyYear + "," + ArtificialSociety.getSociety().size() + "," + q4Time ); // Q4
			//trialResults.appendFileContents(  Configuration.SocietyYear + "," + ArtificialSociety.getSociety().size() + "," + q5Time ); // Q5
			//trialResults.appendFileContents(  Configuration.SocietyYear + "," + ArtificialSociety.getSociety().size() + "," + q6Time ); // Q6
			//trialResults.appendFileContents(  Configuration.SocietyYear + "," + ArtificialSociety.getSociety().size() + "," + q7Time ); // Q7
			
			// Free memory.
			//MemoryUsage.cleanup();
			
			
			
			
			
			
			// test
			//genTime = StartSimulation_Scratch();
			//genMemory = MemoryUsage.GetMemoryUsage();
			
		} // end for t (loop through trials)

		
		//trialResults.writeToFile("Trial Results/IV - Population/PopulationTrials.txt"); // IV - Population
		//trialResults.writeToFile("Trial Results/IV - Connectedness/ConnectednessTrials.txt"); // IV - Total Connections
		//trialResults.writeToFile("Trial Results/IV - Locals/PeopleInSocietyTrials.txt"); // IV - Total Locals
		
		
		// Genesis model & fate.
		//trialResults.writeToFile("Trial Results/Model Populations/GenesisFate.txt");	// Population from Genesis model.
		//trialResults.writeToFile("Trial Results/Model Populations/MonadicFate2.txt");	// Population from Monadic model (50 singles; 50 year sim).
		//trialResults.writeToFile("Trial Results/Model Populations/DyadicFate.txt");	// Population from Dyadic model (50 married couples; 50 year sim).
		//trialResults.writeToFile("Trial Results/Model Populations/MonadicDyadicFate.txt");	// Population from MonadicDyadic model.
		
		
		// MEMORY
		//trialResults.writeToFile("Additional Trial Results/Memory/MemoryUsage.txt"); // Memory Usage
		
		

		//trialResults.writeToFile("Additional Trial Results/Queries/Q1_AddPerson_" + Configuration.N_CouplesMarried + "M.txt"); // Query 1
		//trialResults.writeToFile("Additional Trial Results/Queries/Q2_AddCouple_" + Configuration.N_CouplesMarried + "M.txt"); // Query 2
		//trialResults.writeToFile("Additional Trial Results/Queries/Q3_KillPerson_" + Configuration.N_CouplesMarried + "M.txt"); // Query 3
		//trialResults.writeToFile("Additional Trial Results/Queries/Q4_GetRaceNumbers_" + Configuration.N_CouplesMarried + "M.txt"); // Query 4
		//trialResults.writeToFile("Additional Trial Results/Queries/Q5_GetTotalFriendships_" + Configuration.N_CouplesMarried + "M.txt"); // Query 5
		trialResults.writeToFile("Additional Trial Results/Queries/Q6_GetTotalLocalPeople_" + Configuration.N_CouplesMarried + "M.txt"); // Query 6
		//trialResults.writeToFile("Additional Trial Results/Queries/Q7_GetNumberOfSingleFriends_" + Configuration.N_CouplesMarried + "M.txt"); // Query 7
		
		//trialResults.writeToFile("Trial Results/Queries/Q2_AddCouple.txt"); // Query 2
		//trialResults.writeToFile("Trial Results/Queries/Q3_KillPerson.txt"); // Query 3
		//trialResults.writeToFile("Trial Results/Queries/Q4_GetRaceNumbers.txt"); // Query 4
		//trialResults.writeToFile("Trial Results/Queries/Q5_GetTotalFriendships.txt"); // Query 5
		//trialResults.writeToFile("Trial Results/Queries/Q6_GetTotalLocalPeople.txt"); // Query 6
		//trialResults.writeToFile("Trial Results/Queries/Q7_GetNumberOfSingleFriends.txt"); // Query 7
		


		
		
		
		
		
		// ----------------------------------------------
		// Run queries.
		// ----------------------------------------------
		/*
		System.out.println("Query 1 took: " + SocietyQuery.executeQuery1_AddPerson(10) + " s");
		System.out.println("Query 2 took: " + SocietyQuery.executeQuery2_AddCouple(10) + " s");
		System.out.println("Query 3 took: " + SocietyQuery.executeQuery3_KillPerson(10) + " s");
		System.out.println("Query 4 took: " + SocietyQuery.executeQuery4_GetRaceNumbers(10) + " s");
		System.out.println("Query 5 took: " + SocietyQuery.executeQuery5_GetTotalFriendships(10) + " s");
		System.out.println("Query 6 took: " + SocietyQuery.executeQuery6_GetTotalLocalPeople(10) + " s");
		System.out.println("Query 7 took: " + SocietyQuery.executeQuery7_GetNumberOfSingleFriends(10) + " s");
		*/
		
		
		/*
		SocietyQuery.executeQuery1_AddPerson();
		SocietyQuery.executeQuery2_AddCouple();
		SocietyQuery.executeQuery3_KillPerson();
		SocietyQuery.executeQuery4_GetRaceNumbers();
		SocietyQuery.executeQuery5_GetTotalFriendships();
		SocietyQuery.executeQuery6_GetTotalLocalPeople();
		SocietyQuery.executeQuery7_GetNumberOfSingleFriends();
		*/
		
		

		

		



	} // end main (program entry)
	
	
	public static double StartSimulation_Scratch () {
		long sTime, eTime;
		double totalTime;
		
		// Create new world.
		sTime = System.nanoTime();
			new Dyadic_NetworkGenerator();
		eTime = System.nanoTime();
		OutputSocietyCompactly.WriteSocietyToBinary("societyMar30_GN.bin");
		totalTime = (eTime - sTime) / 1000000000.0;
		
		//System.err.println("Population (generated) Size = " + ArtificialSociety.getSociety().size());

		//MemoryUsage.cleanup();
		
		return totalTime;
		
	} // end StartSimulation_Scratch()
	
	
	public static double StartSimulation_Load () {
		long sTime, eTime;
		double totalTime;
		
		// Load in world.
		sTime = System.nanoTime();
			new Dyadic_NetworkGenerator(1);
		eTime = System.nanoTime();
		totalTime = (eTime - sTime) / 1000000000.0;
		//System.out.println("Loading society from file took:\t\t\t" + totalTime + " seconds.");
		OutputSocietyCompactly.WriteSocietyToBinary("societyMar30_FF.bin");
		//System.err.println("Population (loaded) Size = " + ArtificialSociety.getSociety().size());
		
		//MemoryUsage.cleanup();
		
		return totalTime;
	} // end StartSimulation_Load()

	
	public Dyadic_NetworkGenerator (int x) {
		
		// Load society from binary file. 
		
		
		// Initialize new artificial society.
		ArtificialSociety.createNewSociety();
		// Initialize arrays.
		
		
		//AllPersons = new ArrayList();
		//AllInstitutions = new ArrayList();

		
		//InputSocietyCompactly.ReadSocietyFromBinary("societyMar27_GN.bin");
		InputSocietyCompactly.readFromFileEfficiently("societyMar30_GN.bin");
		//OutputSocietyCompactly.WriteSocietyToBinary("societyMar30_FF.bin");
		
		//System.out.println(AllPersons.size());
		
	} // end Dyadic_NetworkGenerator() constructor
	
	
	
	
	
	
	
	public Dyadic_NetworkGenerator () {
		
		
		
		// Initialize arrays.
		//AllPersons = new ArrayList();
		//AllInstitutions = new ArrayList();
		
		// Initialize new artificial society.
		ArtificialSociety.createNewSociety();
		
		// Load all configuration parameters before anything else.
		LoadAllConfigurations();

		
		
		//System.err.println("N_POP_SIZE = " + N_Population_Size);
		
		/*
System.err.println("Things to be fixed, modified, or completed later:");
System.err.println("\t- In AttributeAssigner->assignWorkHistory(), the person's hometowns need to be taken into account and only the local work be assigned in detail.");
System.err.println("\t- Allow multiple passes for assigning people to clubs and/or other groups; and also for friendship assignments");
System.err.println("\t- Calculate relationship strength in PersonGroupAdder class, and then assign children based on relationship strength and/or other factors.");
System.err.println("\t- Assuming child did not move during first few years. Be sure to modify this based on parents' hometowns! It should also apply to students up to 17 years old!");
		 */

// Not necessary - something to discuss as future work 
//System.err.println("\t- Schools and workplaces should be able to start and end at different times, and people re-locate when one ends.");

//Not necessary - something to discuss as future work
//System.err.println("\t- Allow complex trait combinations for career or club assignments, rather than just singular traits.");


	//System.out.println(DistributionParser.parseStatisticalDistribution("normal(4.0,5.3)"));
	//System.out.println(DistributionParser.parseStatisticalDistribution("normal(12,6)"));




		// Create population for society.
		createPopulation();

		
		

		
		
		// Create social network.
		createNetwork();
		
		// Check for deaths in initial population.
		checkForDeaths();
				
				
		// Create entire social network.
		//FriendshipCalculator.displayNetwork();
		
		//System.err.println("Population Size = " + ArtificialSociety.getSociety().size());
		
		Person p0;
		Person p1;

		
		int x;
		int y = 0;
		int z = 1;
		/*
		for (x = 0; x < N_CouplesDating; x++) {
			
			p0 = ArtificialSociety.getPersonByID(y);
			p1 = ArtificialSociety.getPersonByID(z);
			System.out.println("Started dating in " + p0.getRelationshipStartYear());
			System.out.println("Born in " + p0.getYearBorn() + " and " + p1.getYearBorn());
			DebugTools.printArray(p0.getHometownHistory());
			DebugTools.printArray(p1.getHometownHistory());
			System.out.println("---------------------------------------------------");
			y += 2;
			z += 2;
		}
		*/
		
				

		// Save network to file.
		OutputResults.writePopulationToFile(ArtificialSociety.getSociety(), "Output/nodeList.txt", "Output/edgeList.txt");
		
	} // end Dyadic_NetworkGenerator() constructor
	
	
	public void LoadAllConfigurations () {
		
		
		Configuration.LoadConfigValuesFromFile("SocietyConfig.xml");
		//Configuration.LoadConfigValuesFromFile("SocietyConfig_Genesis.xml");
		//Configuration.LoadConfigValuesFromFile("SocietyConfig_TEST.xml");
		//Configuration.LoadConfigValuesFromFile("SocietyConfigSmall.xml");
		
		// Load Configuration parameters.
		//Configuration.SetHardcodedValues();
		

		// Load all institutions (schools, temples, etc.) - * NOTE * this must be loaded BEFORE the careers/workplaces are loaded!
		InstitutionSet.loadAllInstitutions();
		SchoolSet.initSchools();				// Initialize Schools.
		TempleSet.initTemples();				// Initialize Temples.
		
		Club.loadFiles();

		// Load Careers.
		Careers.loadFiles();							// Load the career files.
		AttributeAssigner.initializeCareersTables();	// Create the table of career openings.
		

		
		
		GroupGenerator.generateGroups();
			

		
	} // end LoadAllConfigurations()
	
	
	//public static void addPersonToArray (Person person) {
		// Keep track of all Person instances in one global array. This function will add the given person to that global array.
		//ArtificialSociety.addPersonToSociety(person);
	//} // end addPersonToArray()
	
	
	
	
	

	
	


	
	
	public void playInSandbox () {
	}
	
	public void createPopulation () {
		createSingles(N_Singles);
		//createImmigrants(N_Immigrants);
		createCouples(N_CouplesMarried, N_CouplesDating);
		
		
		//Logger.LogRelationshipStrength();
		
		//System.err.println("FIRST TIME THROUGH:\n.............................................");
		createChildren();
		
		//System.err.println(".............................................");
		//createChildren();
		//Logger.LogRelationshipStrength();
		
		
		// DEBUG TESTING
		/*
		int p;
		Person perP, perQ;
		for (p = 0; p < AllPersons.size(); p++) {
			perP = (Person)AllPersons.get(p);
			if (!ValidationTools.empty(perP.partner_id)) {
				perQ = (Person)AllPersons.get(perP.partner_id);
				RelationshipCalculator.CalculateRelationshipStrength(perP, perQ);
			}
			//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		}
		*/
		// DEBUG TESTING		
		
		// Once all people are created, add each one to a variety groups based on the attributes already assigned to them.
		//addAllPeopleToGroups();
		
	} // end createPopulation()
	
	public void createNetwork () {
		// Create the entire social network. This assumes that the population has been created and stored in AllPersons.
		FriendshipCalculator.CreateEntireFriendshipNetwork();
	} // end createNetwork()
	
	
	public void createSingles (int numSingles) {
		// Create the single people (not in a relationship) in the population.

		int i;
		Person p;

		for (i = 0; i < numSingles; i++) {
			p = PersonGenerator.GeneratePerson(1, null);
			PersonGenerator.complete(p);
		} // end for i (creating single people)
		
		
		
	} // end createSingles()

	public void createCouples (int numMarriedCouples, int numDatingCouples) {
		// Create the couples (people in a relationship) in the population.

		int i;

		// --------------------------------------------------
		// MARRIED.
		// --------------------------------------------------
		for (i = 0; i < numMarriedCouples; i++) {
			PersonGenerator.GenerateCouple(RelationshipCalculator.REL_TYPE_MARRIED);
		} // end for i (creating married couples of people)

		// --------------------------------------------------
		// DATING.
		// --------------------------------------------------
		for (i = 0; i < numDatingCouples; i++) {
			PersonGenerator.GenerateCouple(RelationshipCalculator.REL_TYPE_DATING);
		} // end for i (creating dating couples of people)

	} // end createCouples()
	
	
	
	public void createChildren () {
		// Create children from those people in relationships in the population.
		int i;
		Person person;
		Person partner;

		//int TMP_COUPLES = 0;

		// Loop through every person in entire population.
		for (i = 0; i < ArtificialSociety.getSociety().size(); i++) {
			//person = (Person)ArtificialSociety.getSociety().get(i); // BS_Feb18
			person = (Person)ArtificialSociety.getPersonByIndex(i);
			// Check if person does not have a partner. If not, then exit function now (no children to be created).
			if (!ValidationTools.empty(person.getPartnerID())) {

				// Get person's spouse.
				partner = ArtificialSociety.getPersonByID(person.getPartnerID());

				// For simplicity, only married couples will have children. Otherwise, this condition could be removed so that dating couples can have kids too.
				if (person.getRelationshipStatus() == RelationshipCalculator.REL_TYPE_MARRIED && partner.getRelationshipStatus() == RelationshipCalculator.REL_TYPE_MARRIED) {
			
				
					//TMP_COUPLES++;
					//System.err.println("MARRIED COUPLES | " + person.getID() + " & " + partner.getID());
				
					// Create children. In CreateChildren(), it checks for the greater ID to ensure children are only created once.
					RelationshipCalculator.CreateChildren(person, partner, true);
			
					// Re-evaluate relationship strength AFTER the parents have children.
					RelationshipCalculator.CalculateAndSetRelationshipStrength(person, partner, 1);
			
				} // end if (check if the couple is married {only one person really needs to be checked, but to be safe, both are checked now})

			} // end if (check if person is in a relationship)
				
		} // end for i (looping through all persons)

		//System.out.println("AFTER NUM MARRIED COUPLES ==> " + TMP_COUPLES);

	} // end createChildren()


	public void checkForDeaths () {
		// This method will ensure that anyone from the initial population who was created after their expected year of death,
		// is removed from the society immediately.

		int i = 0;
		Person person;

		// Loop through the population and check if person has already died.
		while (i < ArtificialSociety.getSociety().size()) {
			// Get person at index i.
			person = ArtificialSociety.getPersonByIndex(i);
			if (person.getExpectedDeathYear() <= Configuration.SocietyYear) {
				person.die();
				i--; // Decrement counter so shifted elements are not skipped over.
			} // end if (check if person is dead)
			
			i++; // Increment counter to move on to next person.
		} // end while (loop through entire population)
		
		

	} // end checkForDeaths()
	
	
	
	
	

	/*
	public void createChildren () {
		// Create children from those people in relationships in the population.
		int i;
		Person person;
		Person spouse;
		Person child;
		Person[] children;
		

		
		
		

		// Loop through every person in entire population.
		for (i = 0; i < AllPersons.size(); i++) {
			person = (Person)AllPersons.get(i);
			// Check if person does not have a partner. If not, then exit function now (no children to be created).
			if (ValidationTools.empty(person.partner_id)) {
				return;
			} // end if (check if person is single)
			spouse = (Person)AllPersons.get(person.partner_id);
			
			
			// Ensure a couple's children are only from one parent's pass through the loop.
			if (person.getID() > spouse.getID()) { // Only create children at the person with the greater ID. This way children won't be created twice for the same couple.
				

				
				double rndChildren = Distribution.uniform(0.0, 1.0);
				
				if (rndChildren < p_likelihoodMarriedCoupleHasChildren) {
					int numChildren = ValidationTools.clipValueToRange((int)Math.round(DistributionParser.parseStatisticalDistribution(marriageChildrenDistr)), marriageChildrenMin, marriageChildrenMax);
					children = new Person[numChildren];
					int c;
					
					//System.out.println(person.getID() + " " + spouse.getID() + " gave birth to " + numChildren + " children.");

					for (c = 0; c < numChildren; c++) {
						child = PersonGenerator.GeneratePerson(6, new Person[] {person, spouse});	// CHILD
						child = PersonGenerator.complete(child); // complete() fills in the missing attributes for the person.
						//child.addPersonToGroups();
							
						children[c] = child;

					} // end for c (creating children for couple)

						

					PersonGroupAdder.createChildrenConnections(person, spouse, children);
						
				} // end if (random number indicates the couple has children)

				
			} // end if (compare couple's IDs to ensure children are only created once per couple)
			
		} // end for i (looping through all persons)

	} // end createChildren()
	*/
	
	
	

	public void addAllPeopleToGroups () {
		// This function loops through all persons in the population and calls the person's method to add them to the groups in which they belong.
		int i;
		Person person;
		
		// Loop through every person in entire population.
		for (i = 0; i < ArtificialSociety.getSociety().size(); i++) {
			//person = (Person)ArtificialSociety.getSociety().get(i); // BS_Feb18
			person = (Person)ArtificialSociety.getPersonByIndex(i);
			// Call addPersonToGroups() for person so they will be added to different groups.
			person.addPersonToGroups();
		} // end for i (loop through all persons)
	} // end addAllPeopleToGroups()
	
	

	
} // end Dyadic_NetworkGenerator class
