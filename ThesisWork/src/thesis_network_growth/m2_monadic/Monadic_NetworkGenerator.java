package thesis_network_growth.m2_monadic;


import static thesis_network_growth.Configuration.N_Singles;

import thesis_network_growth.ArtificialSociety;
import thesis_network_growth.AttributeAssigner;
import thesis_network_growth.Careers;
import thesis_network_growth.Club;
import thesis_network_growth.FriendshipCalculator;
import thesis_network_growth.GroupGenerator;
import thesis_network_growth.InstitutionSet;
import thesis_network_growth.OutputResults;
import thesis_network_growth.OutputSocietyCompactly;
import thesis_network_growth.Person;
import thesis_network_growth.PersonGenerator;
import thesis_network_growth.SchoolSet;
import thesis_network_growth.SimulationStepper;
import thesis_network_growth.TempleSet;
import thesis_network_growth.Configuration;
import thesis_network_growth.m1_dyadic.Dyadic_NetworkGenerator;

public class Monadic_NetworkGenerator {

	//public static ArrayList AllPersons;
	
	
	public static void main (String[] args) {
		
		long sTime, eTime;
		double totalTime;
		
		
		
		
		
		// Create new world.
		sTime = System.nanoTime();
			new Monadic_NetworkGenerator();
		eTime = System.nanoTime();
		OutputSocietyCompactly.WriteSocietyToBinary("societyFeb18_GN.bin");
		totalTime = (eTime - sTime) / 1000000000.0;
		
		System.out.println("Generating society from scratch took:\t\t" + totalTime + " seconds.");
		System.out.println("POP SIZE (gen) = " + ArtificialSociety.getSociety().size());
		
		
		
		
		
		
		
		/*
		// Load in world.
		sTime = System.nanoTime();
			new Monadic_NetworkGenerator(1);
		eTime = System.nanoTime();
		totalTime = (eTime - sTime) / 1000000000.0;
		System.out.println("Loading society from file took:\t\t\t" + totalTime + " seconds.");
		OutputSocietyCompactly.WriteSocietyToBinary("societyFeb18_FF.bin");
		System.out.println("POP SIZE (load) = " + ArtificialSociety.getSociety().size());
		*/
		
		//new Monadic_NetworkGenerator();

	} // end main (program entry)
	
	
	public Monadic_NetworkGenerator () {

		
		// Initialize new artificial society.
		ArtificialSociety.createNewSociety();

		
		// Load all configuration parameters before anything else.
		LoadAllConfigurations();
		



		// Create population for society.
		createPopulation();

		// Create social network.
		createNetwork();
		
		// Check for deaths in initial population.
		checkForDeaths();
		
		//SimulationStepper sim = new SimulationStepper( ArtificialSociety.getSociety() );
		//sim.begin(10);
		
		
		// Save network to file.
		OutputResults.writePopulationToFile(ArtificialSociety.getSociety(), "Output/nodeList.txt", "Output/edgeList.txt");
		
	} // end Monadic_NetworkGenerator() constructor
	
	
	
	public void LoadAllConfigurations () {
		
		
		
		
		// Load Configuration parameters.
		Configuration.LoadConfigValuesFromFile("SocietyConfig.xml");
		//Configuration.SetHardcodedValues();
		
		
		// THIS IS A TEMPORARY WAY TO ENSURE THAT ONLY SINGLES ARE CREATED FOR THIS MODEL!
		// EVENTUALLY THERE WILL BE SEPARATE CONFIG FILES FOR THE DIFFERENT MODEL TYPES.
		Configuration.N_CouplesMarried = 0;	// Temp
		Configuration.N_CouplesDating = 0;	// Temp
		Configuration.N_Couples = 0;		// Temp
		
		
		
		

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
	
	
	public void createPopulation () {
		createSingles(N_Singles);
	
	
		//createChildren();
	
		
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
	
	
	
	
	
} // end Monadic_NetworkGenerator()
