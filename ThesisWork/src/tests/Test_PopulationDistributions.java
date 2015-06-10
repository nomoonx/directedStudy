package tests;

import java.util.ArrayList;
import java.util.Random;



public class Test_PopulationDistributions {
	
	public Test_PopulationDistributions () {
		try {
			//XMLLoader xml = new XMLLoader("London.xml");
			XMLLoader2 xml = new XMLLoader2("London 2.xml");

			ArrayList[] raceDemographics = xml.getResultList(XMLLoader2.RACE_LIST);
			ArrayList[] ageDemographics = xml.getResultList(XMLLoader2.AGE_LIST);
			
			int[] racePercentiles = getPercentiles(raceDemographics[1]);
			int[] agePercentiles = getPercentiles(ageDemographics[1]);
			
			
			/*
			int j;
			System.out.println("\n----------------------------------------");
			for (j = 0; j < racePercentiles.length; j++) {
				System.out.print(racePercentiles[j] + "  ");
			}
			System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			for (j = 0; j < agePercentiles.length; j++) {
				System.out.print(agePercentiles[j] + "  ");
			}
			System.out.println("\n----------------------------------------");
			*/
			
			String[] labelsR = new String[raceDemographics[0].size()];
			int[] percentsR = new int[raceDemographics[1].size()];
			String[] labelsA = new String[ageDemographics[0].size()];
			int[] percentsA = new int[ageDemographics[1].size()];
			
			
			int i;
			//System.out.println(raceDemographics[0].size());
			for (i = 0; i < raceDemographics[0].size(); i++) {
				labelsR[i] = (String)raceDemographics[0].get(i);
				percentsR[i] = Integer.parseInt((String)raceDemographics[1].get(i));
				System.out.println(labelsR[i] + " -> " + percentsR[i]);
			}
			for (i = 0; i < ageDemographics[0].size(); i++) {
				labelsA[i] = (String)ageDemographics[0].get(i);
				percentsA[i] = Integer.parseInt((String)ageDemographics[1].get(i));
				System.out.println(labelsA[i] + " -> " + percentsA[i]);
			}

			

			createPopulation(1000, racePercentiles, agePercentiles);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int[] getPercentiles (ArrayList percentages) {
		int[] percList = new int[percentages.size()];

		percList[0] = Integer.parseInt((String)percentages.get(0));
		int i;
		for (i = 1; i < percentages.size(); i++) {
			percList[i] = Integer.parseInt((String)percentages.get(i)) + percList[i-1];
		}

		return percList;
	}


	public void createPopulation(int N, int[] pc_Race, int[] pc_Age) {
		//System.out.println("Preparing to create population of " + N + " with " + labels[0] + " = " + percents[0] + ", etc. ...");
		int i;
		Random random = new Random();
		int rnd_Race, rnd_Age;
		int Race, Age;
		
		//int[] randomDemographicFigures = new int[labels.length];
		
		int[] RaceCounters = new int[pc_Race.length];
		int[] AgeCounters = new int[pc_Age.length];
		
		// Initialize all demographic figures to 0.
		for (i = 0; i < RaceCounters.length; i++) {
			RaceCounters[i] = 0;
		} // end i (initializing stats to 0)
		for (i = 0; i < AgeCounters.length; i++) {
			AgeCounters[i] = 0;
		} // end i (initializing stats to 0)
		
		
		// Create population (no actual objects created) to see how well it matches the given demographics.
		for (i = 0; i < N; i++) {
			rnd_Race = random.nextInt(101);
			rnd_Age = random.nextInt(101);
			
			Race = determineDemographicBucket(pc_Race, rnd_Race);
			Age = determineDemographicBucket(pc_Age, rnd_Age);
			
			RaceCounters[Race]++;
			AgeCounters[Age]++;
			
			//System.out.println("New person of race " + Race + " and age " + Age + ".");
			
			
		} // end i (population size being created)
		
		System.out.println("RACE");
		for (i = 0; i < RaceCounters.length; i++) {
			System.out.println(i + ":  " + RaceCounters[i] + " (" + ((double)RaceCounters[i]/(double)N*100.0) + ").");
		} // end i (initializing stats to 0)
		System.out.println("AGE");
		for (i = 0; i < AgeCounters.length; i++) {
			System.out.println(i + ":  " + AgeCounters[i] + " (" + ((double)AgeCounters[i]/(double)N*100.0) + ").");
		} // end i (initializing stats to 0)
		
		//for (i = 0; i < labels.length; i++) {
			//System.out.println(labels[i] + " ~~~ " + randomDemographicFigures[i] + " (" + (double)randomDemographicFigures[i] / (double)N * 100 + ").");
		//} // end i (printing out stats)
	}
	
	public int determineDemographicBucket (int[] bins, int num) {
		int bucket = 0;
		
		while (num > bins[bucket]) {
			bucket++;
		}
		
		
		return bucket;
	}


	public static void main (String[] args) {
		new Test_PopulationDistributions();
	}

}
