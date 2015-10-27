package org.noMoon.ArtificalSociety.person.utils;

import org.noMoon.ArtificalSociety.career.DO.Career;
import org.noMoon.ArtificalSociety.career.DTO.CareerDTO;
import org.noMoon.ArtificalSociety.career.services.CareerService;
import org.noMoon.ArtificalSociety.commons.utils.Configuration;
import org.noMoon.ArtificalSociety.commons.utils.Distribution;
import org.noMoon.ArtificalSociety.commons.utils.DistributionParser;
import org.noMoon.ArtificalSociety.commons.utils.ValidationTools;
import org.noMoon.ArtificalSociety.history.DTO.HometownHistoryDTO;
import org.noMoon.ArtificalSociety.history.Records.HistoryRecord;
import org.noMoon.ArtificalSociety.history.services.HistoryService;
import org.noMoon.ArtificalSociety.institution.DO.Institution;
import org.noMoon.ArtificalSociety.institution.enums.InstitutionEnum;
import org.noMoon.ArtificalSociety.institution.services.InstitutionService;
import org.noMoon.ArtificalSociety.person.DTO.PersonDTO;
import org.noMoon.ArtificalSociety.person.Enums.RelationStatusEnum;
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
    private static HistoryService historyService;
    private static InstitutionService institutionService;

    public static int assignAge() {
        int v = Distribution.uniform(Configuration.AdultAgeMin, Configuration.AdultAgeMax);
        return v;
    } // end assignInitAge()

    public static int assignBirthYear(int age) {
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

    public static void assignExpectedYearOfDeath(PersonDTO attr) {
        // Compute the expected year that this person will die of natural causes. In future work, person could die earlier but
        // this is the maximum year that they would stay alive.


        int ageOfDeath = (int) Math.round(DistributionParser.parseStatisticalDistribution(Configuration.LifeExpectancyDistr));

        int yearOfDeath = ageOfDeath + attr.getBirthYear();

        //System.out.println("expected life expectancy ==> " + ageOfDeath + ", year ==> " + yearOfDeath);

        //System.err.println("In AttributeAssigner->assignExpectedYearOfDeath(); year born -> " + attr.getYearBorn() + " || " + yearOfDeath + " [" + attr.getID() + "].");

        attr.setExpDeathYear(yearOfDeath);

    } // end assignExpectedYearOfDeath()

    public static void assignPersonality(PersonDTO attr) {
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
            traits[t] = (double) (Distribution.uniform(1, 3) - 1.0) / 2.0; // Choose 0, 0.5, or 1 uniformly.
        }
        attr.setPersonality(traits);
    } // end assignPersonality()

    public static void assignIntelligence(PersonDTO attr) {
        // Generate a random intelligence value for the person. Currently using a Gaussian distribution.
        double intelligence_val = ValidationTools.clipValueToRange(Distribution.normal(0.5, 0.1), 0.0, 1.0);
        attr.setIntelligence(intelligence_val);
    } // end assignIntelligence()

    public static void assignAthleticism(PersonDTO attr) {
        // Generate a random athletic value for the person. Currently using a Gaussian distribution.
        double ath_val = ValidationTools.clipValueToRange(Distribution.normal(0.5, 0.1), 0.0, 1.0);
        attr.setAthleticism(ath_val);
    } // end assignAthleticism()

    public static void assignRace(PersonDTO attr) {
        // Assign a random race to the person. Use the custom distribution as defined in the Configuration file.
        int v = Distribution.custom(Configuration.RaceDistr);
        attr.setRaceIndex(v);
    } // end assignRace()

    public static void assignReligion(PersonDTO attr) {
        // Assign a random religion to the person. Use the custom distribution as defined in the Configuration file.
        int v = Distribution.custom(Configuration.ReligionDistr);
        attr.setReligionIndex(v);
    } // end assignReligion()

    public static void assignSpouseRace(PersonDTO personA, PersonDTO personB) {
        double[] race_probs = Configuration.p_same_race_for_spouse[personA.getRaceIndex()]; // Extract the appropriate row of race probabilities.
        personB.setRaceIndex(Distribution.custom(race_probs));
    } // end assignSpouseRace()

    public static void assignSpouseReligion(PersonDTO personA, PersonDTO personB) {
        double[] religion_probs = Configuration.p_same_religion_for_spouse[personA.getReligionIndex()]; // Extract the appropriate row of religion probabilities.
        personB.setReligionIndex(Distribution.custom(religion_probs));
    } // end assignSpouseReligion()


    public static void assignNationality(PersonDTO attr) {
        // Assign the nationality of the person. use the current society name.
        attr.setNationality(Configuration.SocietyName);
    } // end assignNationality()

    public static void assignInterest(PersonDTO attr) {
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
        interests[i] = ValidationTools.clipValueToRange((raceInt + Distribution.uniform(-10.0, 10.0)), 0.0, 100.0);

        // 1: RELIGION.
        // Scale religion interest to [0,100] and add a random number from [-10,10]. Clip the result onto [0,100].
        i = 1;
        double religionInt = Configuration.religion_interest_value[attr.getReligionIndex()] * 100.0;
        interests[i] = ValidationTools.clipValueToRange((religionInt + Distribution.uniform(-10.0, 10.0)), 0.0, 100.0);

        // 2: ATHLETICISM.
        i = 2;
        interests[i] = attr.getAthleticism() * 100.0; // Scale from [0,1] to [0,100].

        // 3-7: RANDOM EXTRA INTERESTS.
        for (i = 3; i < Configuration.NumInterests; i++) {
            interests[i] = Distribution.normal(50.0, 10.0);
        } // end for i (interest options)

        //DebugTools.printArray(interests);

        attr.setInterest(interests);
    } // end assignInterests()


    public static void assignInterestWeight(PersonDTO attr) {
        // Assign a set of interest weights to the person. These are each in [0,1] and they must sum to 1.0.

        double[] wgts = new double[Configuration.NumInterests];
        int i;
        double equalFraction = 1.0 / Configuration.NumInterests;
        for (i = 0; i < Configuration.NumInterests; i++) {
            wgts[i] = equalFraction;
        } // end for i (interest options)

        attr.setInterestWeight(wgts);
    } // end assignInterestWeights()

    public static void assignCareer(PersonDTO attr, int popSize) {
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
        if (attr.getAge() == 0 || attr.getPersonality().length <= 0) {
            System.err.println("Attempting to assign career but personality has not been set.");
            return;
        } // end if (check if age or personality is not set)

        // ----------------------------------------------------------------------------------------------------------------------------------------------------
        // NEW
        // ----------------------------------------------------------------------------------------------------------------------------------------------------

        // ------------------------------------------------
        // CAREERS
        // ------------------------------------------------
        int t;

        double p_inCareer;


        boolean hasUnmatchedRequiredTrait = false;
        double[] careerProbs = new double[careerList.size()];

        double[] numOpenings = new double[careerList.size()];
        double[] weightedProbabilities = new double[careerList.size()];
        double totalWeight = 0.0;

        for (int c = 0; c < careerList.size(); c++) {
            CareerDTO career = new CareerDTO(careerList.get(c));

            // Get set of traits that correspond to career.
            HashMap<String, List<String>> careerTraits = career.getTrait();
            // Create array to store all calculated trait matches.
            List<Double> traitMatches = new ArrayList<Double>();
            // Get number of openings for this career.

            numOpenings[c] = (double) popSize * career.getNumPercnet() / 100.0;

            hasUnmatchedRequiredTrait = false;

            for (String key : careerTraits.keySet()) {
                List<String> traitValueList = careerTraits.get(key);
                for (String traitValue : traitValueList) {
                    String trait = "";
                    String operator = "";
                    String threshold = "";
                    String[] reqVal = traitValue.split("\\|");
                    if (reqVal.length == 1) {
                        trait = traitValue;
                    } else {
                        trait = reqVal[0];
                        operator = reqVal[1];
                        threshold = reqVal[2];
                    }
                    double traitMatch = TraitMatcher.calculateTraitMatch(attr, key, trait);
                    traitMatches.add(traitMatch);
                    if (!StringUtils.isEmpty(operator) && !ValidationTools.parseAndCheckCondition(traitMatch, operator + "|" + threshold)) {
                        hasUnmatchedRequiredTrait = true;
                        break; // Don't bother looping through following traits for this club - since this required one is false, it is pointless to check other conditions)
                    }
                }
            }

            p_inCareer = TraitMatcher.calculateTotalProbability(traitMatches, hasUnmatchedRequiredTrait);
            careerProbs[c] = p_inCareer; // Store probability in array.
            // Multiply number of career openings by the probability of this person having the career.
            weightedProbabilities[c] = numOpenings[c] * careerProbs[c];
            totalWeight += weightedProbabilities[c];
        } // end for c (loop through all careers)


        // For just choosing the maximum probability career, not taking into account the number of openings.
        //double[] maxCareer = ArrayTools.getMaximumItemAndIndex(careerProbs);		// Currently returns an array: [maxProbability, maxProbIndex].
        //int careerIndex = (int)maxCareer[1];
        //ArrayList selectedCareer = (ArrayList)Careers.getFullCareersDatabase().get(careerIndex);
        //System.out.println("Found maxValue = " + maxCareer[0] + " at position = " + maxCareer[1] + " .. " + careerIndex + " | " + selectedCareer.get(1));

        for (int i = 0; i < weightedProbabilities.length; i++) {
            weightedProbabilities[i] /= totalWeight;
        }

        int randCareer = Distribution.custom(weightedProbabilities);

        // Retrieve career ID from selected career, and assign to person's careerPathID member.
        attr.setCareerId(careerList.get(randCareer).getId());


        // ----------------------------------------------------------------------------------------------------------------------------------------------------
        // END NEW
        // ----------------------------------------------------------------------------------------------------------------------------------------------------


    } // end assignCareer()

    public static void assignEducation(PersonDTO attr) {
        // Assign the appropriate education level to the person, based on the career or societal role they are given. This will be important
        // when determining work history, and especially for keeping track of education for immigrants who did not go to school in this society.
        // Modifies attributes: isInSchool, education, educationPSYears

        // Ensure that the person's birth year has been set.
        if (attr.getAge() == 0 || attr.getCareerId() == 0) {
            System.err.println("Attempting to assign education but age or careerID have not been set.");
            return;
        }

        // Determine from hometownHistory when the person went to post-secondary school.
        CareerDTO career = new CareerDTO(careerService.selectCareerById(attr.getCareerId()));

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


        int year_startPSSchool = attr.getBirthYear() + Configuration.SchoolFinishAge;        // NOTE - randomize to add possible year "off" before starting P.S. school.
        int year_finishPSSchool = year_startPSSchool + reqEduYears;

        attr.setEducation(reqEduType);
        attr.setEducationPsYear(reqEduYears);
        attr.setYearStartedPsSchool(year_startPSSchool);
        attr.setYearFinishedPsSchool(year_finishPSSchool);

    } // end assignEducation()

    public static int AssignCoupleRelationshipStart(int pAAge, int pBAge, RelationStatusEnum relType) {
        // Determine the year that the couple got married. This is obtained from an age distribution, and the couples' ages.
        //
        // param pAAge: the age of person A
        // param pBAge: the age of person B
        // param relType: the relationship type (Dating, Marriage, etc.)
        //
        // return: the integer representing the year that the couple's relationship began

        int relStartYear;

        if (relType.equals(RelationStatusEnum.MARRIED)) {
            // Married.

            int NormMarriedAge = (int) Math.round(DistributionParser.parseStatisticalDistribution(Configuration.MarriageAgeDistr));
            int pAMarriedAge = ValidationTools.clipValueToRange(NormMarriedAge, Configuration.MinMarriedAge, pAAge);
            int pABirthYear = Configuration.SocietyYear - pAAge;

            relStartYear = pABirthYear + pAMarriedAge;

            //System.out.println(pAAge + "," + pBAge + " . " + pAMarriedAge + " | got married in year " + relStartYear + " at ages (" + (relStartYear-pABirthYear) + "," + (relStartYear-pBBirthYear) + ")");

        } else if (relType.equals(RelationStatusEnum.DATING)) {
            // Dating.


            int NormDatingAge = (int) Math.round(DistributionParser.parseStatisticalDistribution(Configuration.DatingAgeDistr));
            int pAMarriedAge = ValidationTools.clipValueToRange(NormDatingAge, Configuration.MinDatingAge, pAAge);
            int pABirthYear = Configuration.SocietyYear - pAAge;

            relStartYear = pABirthYear + pAMarriedAge;


        } else {
            // Handle error in case an unknown relationship type is mistakenly sent in.
            System.err.println("In PersonGenerator->GenerateCouple(); unknown relationship type: " + relType);
            relStartYear = -1;
        } // end if (checking the relationship type)

        return relStartYear;

    } // end AssignCoupleRelationshipStart()

    public static HometownHistoryDTO AssignCoupleHometownHistories(RelationStatusEnum relType, int startYear) {
        // Determine the hometowns of a couple who are created as a dyadic unit.
        //
        // param relType: the relationship type between the couple (either MARRIED or DATING)
        // param startYear: the year that the couple formed this relationship
        // param p_alwaysLiveTogether: the probability that the couple always lived together (during marriage!) - NOTE: this is deprecated!
        //
        // return: the array of the ActivityArchive objects for the couple
        HometownHistoryDTO tempHometownHistory = new HometownHistoryDTO();
        HometownHistoryDTO tempLocalHometownHistory = new HometownHistoryDTO();

        // Determine what type of relationship the couple are in.
        if (relType.equals(RelationStatusEnum.MARRIED)) {


            // Create a list of hometowns during the period that the couple is married.
            //System.err.println(startYear);
            historyService.addHometownsForPeriod(tempHometownHistory, tempLocalHometownHistory, startYear, Configuration.SocietyYear, Configuration.AllCities);

        } else if (relType.equals(RelationStatusEnum.DATING)) {
            //System.err.println("In PersonGenerator->AssignCoupleHometownHistories(); should dating couples live in the same hometown?!?!");

        } else {
            System.err.println("In PersonGenerator->AssignCoupleHometownHistories(); unknwon relationship type found: " + relType);
        } // end if (determine the relationship type)

        // Return the new set of hometown histories for the couple.
        return tempHometownHistory;

    } // end AssignCoupleHometownHistories()

    public static void assignHometownHistory_CP(PersonDTO attr, HometownHistoryDTO tempHometownHistoryDTO) {
        // Determine where the person lived throughout their life, and add each of the location periods to the hometown archive.
        // This method for assigning hometowns uses "checkpoints" which the other assignHometownHistory() does not do.
        // NOTE:	This is a simplified version of checkpoints. There is at most one period of time where the person is not already
        //			given a hometown. The length of years varies, but there will still only be one period. Thus, find the start and
        //			end year of that period, and then fill in that gaps with the appropriate age range methods.
        // Modifies attributes: yearStartedPSSchool, yearFinishedPSSchool, hometownHistory, soc_HometownHistory

        HometownHistoryDTO homeHistory = new HometownHistoryDTO();    // Entire archive.
        HometownHistoryDTO socHomeHistory = new HometownHistoryDTO();    // Only in society.
        homeHistory.setPersonId(attr.getId());
        socHomeHistory.setPersonId(attr.getId());
        homeHistory.setSocietyId(attr.getSocietyId());
        socHomeHistory.setSocietyId(attr.getSocietyId());

        // Ensure that the person's birth year has been set.
        if (0 == attr.getBirthYear() || 0 == attr.getAge() || StringUtils.isEmpty(attr.getNationality()) || StringUtils.isEmpty(attr.getEducation()) || 0 == attr.getEducationPsYear()) {
            System.err.println("Attempting to assign hometownHistory but birthYear, age, nationality, education, or educationPSYears have not been set.");
            return;
        } // end if (check if prerequiste information has been set)

        // Flags for determining which section(s) to fill in. Default all to True because if no checkpoints are set, then all sections
        // must be filled in. If there are checkpoints, then some flags will be set to False based on those checkpointed periods.
        boolean[] gapsNeedFilling = new boolean[]{true, true, true}; // There are 3 different periods: {Childhood, Post-Seconday, and After All School}

//        boolean copyCheckpointsToEnd = true;

        // If person has some checkpointed hometowns from marriage (living with spouse) or as child (living with parents).
        if (null != tempHometownHistoryDTO) {


            HistoryRecord homeAtBirth = tempHometownHistoryDTO.getActivityByYear(attr.getBirthYear());
            //System.out.println(homeAtBirth);
            if (homeAtBirth == null) {
                // Check if person has no recorded hometown at birth (i.e. this is a MARRIED COUPLE).
                //System.err.println("Married couple. Assign early years to them. [" + attr.getYearBorn() + "," + attr.getRelationshipStartYear() + "].");
//                fillInAllChildhoodHometowns = true;
                //System.out.println("before: " + gapsNeedFilling[0] + "," + gapsNeedFilling[1] + "," + gapsNeedFilling[2] + "]");
                determineCheckpointPeriodGapYears(gapsNeedFilling, false, attr);

//                copyCheckpointsToEnd = true; // This must be True for people who need their latter years (married folk) appended to their main hometown history.

                //System.out.println("person age AS MARRIED " + attr.getAge());

                //System.out.println("after:  " + gapsNeedFilling[0] + "," + gapsNeedFilling[1] + "," + gapsNeedFilling[2] + "]");
            } else {
                // Check if person has a recorded hometown at birth (i.e. this is a CHILD).

                determineCheckpointPeriodGapYears(gapsNeedFilling, true, attr);

//                copyCheckpointsToEnd = false;
            } // end if (check whether or not person needs early years filled in or later years filled in)

        } // end if (check if person has checkpoints)

        // -----------------------------------------------------------------------------------------
        // Childhood.
        // -----------------------------------------------------------------------------------------
        if (gapsNeedFilling[0]) {
            assignHometown_ChildhoodYears(attr, homeHistory, socHomeHistory);
        }

        // -----------------------------------------------------------------------------------------
        // Post-Secondary.
        // -----------------------------------------------------------------------------------------
        if (gapsNeedFilling[1]) {
            assignHometown_PostSecondaryYears(attr, homeHistory, socHomeHistory);
        }


        // -----------------------------------------------------------------------------------------
        // After all school.
        // -----------------------------------------------------------------------------------------

        // gapsNeedFilling[2] is the flag for the working period (after all school). If True, fill it completely to current year.
        // If False, fill it until year married, since the rest is already filled in from the marriage.
        if (gapsNeedFilling[2]) {
            assignHometown_AfterAllSchoolYears(attr, homeHistory, socHomeHistory, Configuration.SocietyYear);
        } else {
            assignHometown_AfterAllSchoolYears(attr, homeHistory, socHomeHistory, attr.getRelationshipStartYear());
        } // end if (check whether or not person is married, to indicate whether or not the entirety of this period needs to be filled)


        // -----------------------------------------------------------------------------------------
        // Copy checkpointed hometowns to main hometown archive.
        // -----------------------------------------------------------------------------------------


        // Append checkpoint hometowns (from marriage) to end of main hometown array.
        if (null!=tempHometownHistoryDTO) {
            List<HistoryRecord> recordList = tempHometownHistoryDTO.getRecordList();
            for (HistoryRecord record : recordList) {

                homeHistory.getRecordList().add(record);

                if (record.getLocation().equals(Configuration.SocietyName)) {

                    socHomeHistory.getRecordList().add(record);
                } // end if (check if local)
            } // end for i (loop through checkpoint activities to copy to main archive)

        }
        // Merge consecutive hometown entries of the same location.
        homeHistory.patchSequentialEntries();
        socHomeHistory.patchSequentialEntries();

        historyService.insertNewHistoryDTO(homeHistory);
        historyService.insertNewHistoryDTO(socHomeHistory);

        // Assign person attribute values.
        attr.setHometownHistoryId(homeHistory.getId());
        attr.setSocHometownHistoryId(socHomeHistory.getId());


    } // end assignHometownHistory_CP()

    private static void assignHometown_ChildhoodYears(PersonDTO attr, HometownHistoryDTO homeHistory, HometownHistoryDTO socHomeHistory) {

        int finalElemSchoolYear;
        if (Configuration.SocietyYear > attr.getBirthYear() + Configuration.SchoolFinishAge) {
            finalElemSchoolYear = attr.getBirthYear() + Configuration.SchoolFinishAge;
        } else {
            finalElemSchoolYear = attr.getBirthYear() + attr.getAge();
            //System.err.println("Assuming child did not move during first few years. Be sure to modify this based on parents' hometowns! It should also apply to students up to 17 years old!");
        } // end if (check if person has finished elementary school)

        //System.out.println(Configuration.SocietyYear + "  " + (attr.yearBorn+Configuration.SchoolFinishAge) + " || " + finalElemSchoolYear);

        //BSActivityArchive.addHometownsForPeriod("", homeHistory, socHomeHistory, attr.getNationality(), 0.7, 1, 2, attr.getYearBorn(), finalElemSchoolYear, Configuration.AllCities);
        historyService.addHometownsForPeriod(homeHistory, socHomeHistory, attr.getBirthYear(), finalElemSchoolYear, Configuration.AllCities);

    } // end assignHometown_ElementaryYears()

    private static void assignHometown_PostSecondaryYears(PersonDTO attr, HometownHistoryDTO homeHistory, HometownHistoryDTO socHomeHistory) {
        //System.err.println("MADE IT TO assignHometownHistory()");
        // If person is not finished elementary school yet, then skip this portion.
        if (attr.getAge() >= Configuration.SchoolFinishAge) {

            //System.err.print("MADE IT ***INSIDE*** assignHometownHistory() -- ");
            //System.err.println(attr.getAge() + " | " + attr.getCareer() + " <> "+ attr.getPSEducationYears() + " || " + attr.getEducation());

            int year_startPSSchool = 0;
            int year_finishPSSchool = 0;

            if (attr.getEducation().equals("C") || attr.getEducation().equals("U")) {    // If person needs college or university education.
                year_startPSSchool = attr.getBirthYear() + Configuration.SchoolFinishAge;        // NOTE - randomize to add possible year "off" before starting P.S. school.

                // If person has not yet finished post-secondary school, then use current year as current final year.
                if (attr.getAge() < (Configuration.SchoolFinishAge + attr.getEducationPsYear())) {
                    year_finishPSSchool = Configuration.SocietyYear;
                } else {
                    year_finishPSSchool = year_startPSSchool + attr.getEducationPsYear();
                } // end if (person is currently in post-secondary school)

                //System.err.println("> MADE IT *WITHIN* TO assignHometownHistory() <");

                String psSchoolType = attr.getEducation();
                //String[] citiesWithSchoolsByType = ArrayTools.arrayListToStringArray(Schools.getSchoolsByFilters(Schools.getFullPostSecondarySchoolsDatabase(), new int[] {Schools.School_Type}, new Object[] {psSchoolType}, new int[] {Schools.School_City}));
                //String[] citiesWithSchoolsByType = ArrayTools.arrayListToStringArray(InstitutionSet.getInstitutionsByFilters(InstitutionSet.PostSecondarySchoolsList, new int[] {InstitutionSet.Institution_Subtype}, new Object[] {psSchoolType}, new int[] {InstitutionSet.Institution_City}));
                Institution query = new Institution();
                query.setSocietyId(Configuration.Society_Id);
                query.setInstitutionType(InstitutionEnum.POST_SECONDARY_SCHOOL);
                query.setType(psSchoolType);
                ArrayList<String> citiesWithSchoolsByType = new ArrayList<String>();
                citiesWithSchoolsByType.addAll(institutionService.selectCityByType(query));

                //BSActivityArchive.addHometownsForPeriod("", homeHistory, socHomeHistory, (String)homeHistory.getLastActivityName(), 0.7, 1, 1, year_startPSSchool, year_finishPSSchool, citiesWithSchoolsByType);
                historyService.addHometownsForPeriod(homeHistory, socHomeHistory, year_startPSSchool, year_finishPSSchool, citiesWithSchoolsByType);
            } // end if (check if post-secondary school is required)


            //int actualFinalSchoolYear = year_startPSSchool + attr.getPSEducationYears();


        } // ?????
    } // end assignHometown_PostSecondaryYears()

    private static void assignHometown_AfterAllSchoolYears(PersonDTO attr, HometownHistoryDTO homeHistory, HometownHistoryDTO socHomeHistory, int lastYearToAdd) {

        // If person is not finished elementary school yet, then skip this portion.
        if (attr.getAge() >= Configuration.SchoolFinishAge) {

            int year_startPSSchool = 0;
            int actualFinalSchoolYear = 0;

            if (!attr.getIsInSchool()) {

                year_startPSSchool = attr.getBirthYear() + Configuration.SchoolFinishAge;        // NOTE - randomize to add possible year "off" before starting P.S. school.
                actualFinalSchoolYear = year_startPSSchool + attr.getEducationPsYear();

                int yearStartingWork;
                if (attr.getEducationPsYear() == 0) {
                    // If the person did not attend post-secondary education, then the year they finished elementary (secondary) school is the year they can begin working.
                    yearStartingWork = attr.getBirthYear() + Configuration.SchoolFinishAge;
                } else {
                    // If person went to post-secondary school, then the year they finished that education is the year they can begin working.
                    yearStartingWork = actualFinalSchoolYear;
                } // end if (determining when the person starting working)
                //int numYearsWorking = Configuration.SocietyYear - yearStartingWork;
                //System.out.println(year_startPSSchool + "/" + actualFinalSchoolYear + "  Working for " + numYearsWorking + " years.");


                //BSActivityArchive.addHometownsForPeriod("", homeHistory, socHomeHistory, (String)homeHistory.getLastActivityName(), 0.3, 1, 3, yearStartingWork, lastYearToAdd, Configuration.AllCities);
                historyService.addHometownsForPeriod(homeHistory, socHomeHistory, yearStartingWork, lastYearToAdd, Configuration.AllCities);


                // Set years started and finished for Post-Secondary school.
                //attr.setPSStartYear(year_startPSSchool);
                // Rather than use the year_finishPSSchool, go back to the original year they actually finish, so that running the simulation, it is recorded when the person will graduate (rather than being clipped to the current year).
                //attr.setPSFinishYear(actualFinalSchoolYear);


            } // end if (check if person is done school)


        } // end if (person is finished elementary school)

    } // end assignHometown_PostSecondaryYears()


    private static void determineCheckpointPeriodGapYears(boolean[] gapsNeedFilling, boolean isChild, PersonDTO attr) {
        // This method will set the boolean flags in the gapsNeedFilling array to indicate which time periods need to be
        // examined and filled with hometowns. Both children and married people will have checkpoints, so both will use
        // this method to determine which periods need to be used.
        //
        // param gapsNeedFilling: a boolean array of size 3, with flags for each of the time periods. These get set in here!
        // param isChild: a boolean flag indicating True if this is a child and False if this is a married person (the only two options).
        // param attr: the Person object for whom the work is being done
        //
        // No return, but the gapsNeedFilling array gets assigned values in here.

        //System.err.println("AttributeAssigner->determineCheckpointPeriodGapYears(); fill in the method!!!");
        //childhoodYears = true;

        int CHILDHOOD = 0;
        int POST_SECONDARY = 1;
        int WORKING = 2;


        if (isChild) {
            // * CHILD * with given childhood years - fill in later years

            // None of the childhood needs to be filled in, as this is all copied directly from the parents.
            gapsNeedFilling[CHILDHOOD] = false;

            // If child has finished secondary school, then the post-secondary portion needs to be worked on.
            if (attr.getAge() >= Configuration.SchoolFinishAge) {
                gapsNeedFilling[POST_SECONDARY] = true;
            } // end if (check if child is finished elementary (including secondary) school)

            // If child is not in school (i.e. finished all education), then fill in the work portion too.
            if (!attr.getIsInSchool()) {
                gapsNeedFilling[WORKING] = true;
            } else {
                gapsNeedFilling[WORKING] = false;
            } // end if (check if child is beyond all education years)


            //System.out.println("THIS IS A CHILD!!!!!!!!!!!!!!!");

        } else {
            // * MARRIED PERSON* with given marriage years - fill in early years
            gapsNeedFilling[CHILDHOOD] = true;
            gapsNeedFilling[POST_SECONDARY] = true;
            gapsNeedFilling[WORKING] = false;
            //fillChildhoodYears = true;
            //fillPSYears = true;

        }

        //childhoodYears[0] = 1900;
        //childhoodYears[1] = 1905;
    } // end determineCheckpointPeriodGapYears()


    public static List<Career> getCareerList() {
        return careerList;
    }

    public static void setCareerList(List<Career> careerList) {
        AttributeAssigner.careerList = careerList;
    }

    public void setCareerService(CareerService careerService) {
        AttributeAssigner.careerService = careerService;
    }

    public void setHistoryService(HistoryService historyService) {
        AttributeAssigner.historyService = historyService;
    }

    public void setInstitutionService(InstitutionService institutionService) {
        AttributeAssigner.institutionService = institutionService;
    }
}
