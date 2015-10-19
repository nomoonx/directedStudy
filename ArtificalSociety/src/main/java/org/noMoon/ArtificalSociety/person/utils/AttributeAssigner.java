package org.noMoon.ArtificalSociety.person.utils;

import org.noMoon.ArtificalSociety.career.DO.Career;
import org.noMoon.ArtificalSociety.career.DTO.CareerDTO;
import org.noMoon.ArtificalSociety.career.services.CareerService;
import org.noMoon.ArtificalSociety.commons.utils.Configuration;
import org.noMoon.ArtificalSociety.commons.utils.Distribution;
import org.noMoon.ArtificalSociety.commons.utils.DistributionParser;
import org.noMoon.ArtificalSociety.commons.utils.ValidationTools;
import org.noMoon.ArtificalSociety.person.DTO.PersonDTO;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by noMoon on 2015-10-18.
 */
public class AttributeAssigner {

    static List<Career> careerList;

    private static CareerService careerService;

    public static int assignAge () {
        int v = Distribution.uniform(Configuration.AdultAgeMin, Configuration.AdultAgeMax);
        return v;
    } // end assignInitAge()

    public static int assignBirthYear (int age) {
        // Assign the birth year to the person. First a random initial age is generated, then the birth year is calculated
        // from the age and current society year. This is then set, so that as time passes in the simulation, the person's
        // age can be re-computed each year.
        // Note that this may not be precise, as month or day is not in the equation. But it should be close enough for the simulation.



        //int age = Distribution.uniform(Configuration.AdultAgeMin, Configuration.AdultAgeMax);
        //System.out.println(age);
        int v = Configuration.SocietyYear - age;
        return v;
        //System.out.println(birthYear);
    } // end assignBirthYear()

    public static void assignExpectedYearOfDeath (PersonDTO attr) {
        // Compute the expected year that this person will die of natural causes. In future work, person could die earlier but
        // this is the maximum year that they would stay alive.



        int ageOfDeath = (int)Math.round(DistributionParser.parseStatisticalDistribution(Configuration.LifeExpectancyDistr));

        int yearOfDeath = ageOfDeath + attr.getBirthYear();

        //System.out.println("expected life expectancy ==> " + ageOfDeath + ", year ==> " + yearOfDeath);

        //System.err.println("In AttributeAssigner->assignExpectedYearOfDeath(); year born -> " + attr.getYearBorn() + " || " + yearOfDeath + " [" + attr.getID() + "].");

        attr.setExpDeathYear(yearOfDeath);

    } // end assignExpectedYearOfDeath()

    public static void assignPersonality (PersonDTO attr) {
        // Assign personality traits to this person. Currently use the MBTI model with 4 traits, and use ternary values (0, 0.5, or 1).
        //
        // 0	1
        // ------
        // I	E
        // S	N
        // T	F
        // J	P

        int numTraits = 4;
        int t;
        double[] traits = new double[numTraits];
        for (t = 0; t < numTraits; t++) {
            traits[t] = (double)(Distribution.uniform(1, 3) - 1.0) / 2.0; // Choose 0, 0.5, or 1 uniformly.
        }
        attr.setPersonality(traits);
    } // end assignPersonality()

    public static void assignIntelligence (PersonDTO attr) {
        // Generate a random intelligence value for the person. Currently using a Gaussian distribution.
        double intelligence_val = ValidationTools.clipValueToRange(Distribution.normal(0.5, 0.1), 0.0, 1.0);
        attr.setIntelligence(intelligence_val);
    } // end assignIntelligence()

    public static void assignAthleticism (PersonDTO attr) {
        // Generate a random athletic value for the person. Currently using a Gaussian distribution.
        double ath_val = ValidationTools.clipValueToRange(Distribution.normal(0.5, 0.1), 0.0, 1.0);
        attr.setAthleticism(ath_val);
    } // end assignAthleticism()

    public static void assignRace (PersonDTO attr) {
        // Assign a random race to the person. Use the custom distribution as defined in the Configuration file.
        int v = Distribution.custom(Configuration.RaceDistr);
        attr.setRaceIndex(v);
    } // end assignRace()

    public static void assignReligion (PersonDTO attr) {
        // Assign a random religion to the person. Use the custom distribution as defined in the Configuration file.
        int v = Distribution.custom(Configuration.ReligionDistr);
        attr.setReligionIndex(v);
    } // end assignReligion()

    public static void assignNationality (PersonDTO attr) {
        // Assign the nationality of the person. use the current society name.
        attr.setNationality(Configuration.SocietyName);
    } // end assignNationality()

    public static void assignInterest (PersonDTO attr) {
        // Assign a set of interests to the person. These are each in [0,100].
        // We are using these interests: [race, religion, athleticism, interest3, interest4, interest5, interest6, interest7]
        // The [race, religion, and athleticism] interests are determined from the person's respective's attribute values.
        // The other interests are taken randomly from a normal distribution.


        // INTERESTS: [race, religion, athleticism, interest3, interest4, interest5, interest6, interest7]

        double[] interests = new double[Configuration.NumInterests];
        int i;

        // 0: RACE.
        // Scale race interest to [0,100] and add a random number from [-10,10]. Clip the result onto [0,100].
        i = 0;
        double raceInt = Configuration.race_interest_value[attr.getRaceIndex()] * 100.0;
        interests[i] = ValidationTools.clipValueToRange((raceInt+Distribution.uniform(-10.0, 10.0)), 0.0, 100.0);

        // 1: RELIGION.
        // Scale religion interest to [0,100] and add a random number from [-10,10]. Clip the result onto [0,100].
        i = 1;
        double religionInt = Configuration.religion_interest_value[attr.getReligionIndex()] * 100.0;
        interests[i] = ValidationTools.clipValueToRange((religionInt+Distribution.uniform(-10.0, 10.0)), 0.0, 100.0);

        // 2: ATHLETICISM.
        i = 2;
        interests[i] = attr.getAthleticism()*100.0; // Scale from [0,1] to [0,100].

        // 3-7: RANDOM EXTRA INTERESTS.
        for (i = 3; i < Configuration.NumInterests; i++) {
            interests[i] = Distribution.normal(50.0, 10.0);
        } // end for i (interest options)

        //DebugTools.printArray(interests);

        attr.setInterest(interests);
    } // end assignInterests()



    public static void assignInterestWeight (PersonDTO attr) {
        // Assign a set of interest weights to the person. These are each in [0,1] and they must sum to 1.0.

        double[] wgts = new double[Configuration.NumInterests];
        int i;
        double equalFraction = 1.0 / Configuration.NumInterests;
        for (i = 0; i < Configuration.NumInterests; i++) {
            wgts[i] = equalFraction;
        } // end for i (interest options)

        attr.setInterestWeight(wgts);
    } // end assignInterestWeights()

    public static void assignCareer (PersonDTO attr, int popSize) {
        // This method of assigning a career loops through every possible career, as read in from the Careers XML file, and looks at which traits correspond to the
        // careers, and calculate how well the given person matches the required traits.  If a trait is required (has a "req" attribute in the file), then if the
        // person does not match this trait, the hasUnmatchedRequiredTrait flag is set to true indicating that this career is an invalid option for this person.
        // The probability (weight) of the person taking a career is the product of the trait match value for that career and the number of job openings for that career
        // in the society. After looping through all the careers, a random career is selected from the array of normalized weights (scaled so the total sum is 1).
        //
        // An alternate way to choose a career is to select the one with the highest weight. For now, the randomizer works well.
        //
        // param attr: the Person for whom to select a career matching their personality and other attributes


        // If the person's age and/or personality have not yet been set, then exit, since both are required before assigning a career.
        if (attr.getAge()==0 || attr.getPersonality().length<=0) {
            System.err.println("Attempting to assign career but personality has not been set.");
            return;
        } // end if (check if age or personality is not set)

        // ----------------------------------------------------------------------------------------------------------------------------------------------------
        // NEW
        // ----------------------------------------------------------------------------------------------------------------------------------------------------

        // ------------------------------------------------
        // CAREERS
        // ------------------------------------------------
        int  t;

        double p_inCareer;


        boolean hasUnmatchedRequiredTrait = false;
        double[] careerProbs = new double[careerList.size()];

        double[] numOpenings = new double[careerList.size()];
        double[] weightedProbabilities=new double[careerList.size()];
        double totalWeight=0.0;

        for(int c=0;c<careerList.size();c++){
            CareerDTO career = new CareerDTO(careerList.get(c));

            // Get set of traits that correspond to career.
            HashMap<String, List<String>> careerTraits = career.getTrait();
            // Create array to store all calculated trait matches.
            List<Double> traitMatches = new ArrayList<Double>();
            // Get number of openings for this career.

            numOpenings[c] = (double)popSize*career.getNumPercnet()/100.0;

            hasUnmatchedRequiredTrait = false;

            for( String key:careerTraits.keySet()){
                List<String> traitValueList=careerTraits.get(key);
                for(String traitValue:traitValueList){
                    String trait="";
                    String operator="";
                    String threshold="";
                    String[] reqVal=traitValue.split("\\|");
                    if(reqVal.length==1){
                        trait=traitValue;
                    }else{
                        trait=reqVal[0];
                        operator=reqVal[1];
                        threshold=reqVal[2];
                    }
                    double traitMatch= TraitMatcher.calculateTraitMatch(attr,key, trait);
                    traitMatches.add(traitMatch);
                    if(!StringUtils.isEmpty(operator)&& !ValidationTools.parseAndCheckCondition(traitMatch, operator+"|"+threshold)){
                        hasUnmatchedRequiredTrait = true;
                        break; // Don't bother looping through following traits for this club - since this required one is false, it is pointless to check other conditions)
                    }
                }
            }

            p_inCareer = TraitMatcher.calculateTotalProbability(traitMatches, hasUnmatchedRequiredTrait);
            careerProbs[c] = p_inCareer; // Store probability in array.
            // Multiply number of career openings by the probability of this person having the career.
            weightedProbabilities[c]=numOpenings[c]*careerProbs[c];
            totalWeight+=weightedProbabilities[c];
        } // end for c (loop through all careers)


        // For just choosing the maximum probability career, not taking into account the number of openings.
        //double[] maxCareer = ArrayTools.getMaximumItemAndIndex(careerProbs);		// Currently returns an array: [maxProbability, maxProbIndex].
        //int careerIndex = (int)maxCareer[1];
        //ArrayList selectedCareer = (ArrayList)Careers.getFullCareersDatabase().get(careerIndex);
        //System.out.println("Found maxValue = " + maxCareer[0] + " at position = " + maxCareer[1] + " .. " + careerIndex + " | " + selectedCareer.get(1));

        for(int i=0;i<weightedProbabilities.length;i++){
            weightedProbabilities[i]/=totalWeight;
        }

        int randCareer = Distribution.custom(weightedProbabilities);

        // Retrieve career ID from selected career, and assign to person's careerPathID member.
        attr.setCareerId(careerList.get(randCareer).getId());


        // ----------------------------------------------------------------------------------------------------------------------------------------------------
        // END NEW
        // ----------------------------------------------------------------------------------------------------------------------------------------------------



    } // end assignCareer()

    public static void assignEducation (PersonDTO attr) {
        // Assign the appropriate education level to the person, based on the career or societal role they are given. This will be important
        // when determining work history, and especially for keeping track of education for immigrants who did not go to school in this society.
        // Modifies attributes: isInSchool, education, educationPSYears

        // Ensure that the person's birth year has been set.
        if (attr.getAge()==0 || attr.getCareerId()==0) {
            System.err.println("Attempting to assign education but age or careerID have not been set.");
            return;
        }

        // Determine from hometownHistory when the person went to post-secondary school.
        CareerDTO career=new CareerDTO(careerService.selectCareerById(attr.getCareerId()));

        String reqEduType = career.getEducation();
        int reqEduYears = career.getNumYearPostSecondary();

        // Indicate whether or not person is currently in school.
        if (attr.getAge() < Configuration.SchoolFinishAge + reqEduYears) {
            // If peron's age is less then the age they will be in their final year of school (including P.S. school), then they are in school now.
            attr.setIsInSchool(true);
        } else {
            // Otherwise, they have finished school.
            attr.setIsInSchool(false);
        } // end if (check if person is finished all school)


        int year_startPSSchool = attr.getBirthYear()+Configuration.SchoolFinishAge;		// NOTE - randomize to add possible year "off" before starting P.S. school.
        int year_finishPSSchool = year_startPSSchool + reqEduYears;

        attr.setEducation(reqEduType);
        attr.setEducationPsYear(reqEduYears);
        attr.setYearStartedPsSchool(year_startPSSchool);
        attr.setYearFinishedPsSchool(year_finishPSSchool);

    } // end assignEducation()


    public static List<Career> getCareerList() {
        return careerList;
    }

    public static void setCareerList(List<Career> careerList) {
        AttributeAssigner.careerList = careerList;
    }

    public void setCareerService(CareerService careerService) {
        AttributeAssigner.careerService = careerService;
    }
}
