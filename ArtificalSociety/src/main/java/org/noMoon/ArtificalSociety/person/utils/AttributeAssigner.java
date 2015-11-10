package org.noMoon.ArtificalSociety.person.utils;

import org.noMoon.ArtificalSociety.career.DO.Career;
import org.noMoon.ArtificalSociety.career.DTO.CareerDTO;
import org.noMoon.ArtificalSociety.career.services.CareerService;
import org.noMoon.ArtificalSociety.commons.utils.Configuration;
import org.noMoon.ArtificalSociety.commons.utils.Distribution;
import org.noMoon.ArtificalSociety.commons.utils.DistributionParser;
import org.noMoon.ArtificalSociety.commons.utils.ValidationTools;
import org.noMoon.ArtificalSociety.history.DTO.HometownHistoryDTO;
import org.noMoon.ArtificalSociety.history.DTO.SchoolHistoryDTO;
import org.noMoon.ArtificalSociety.history.DTO.WorkHistoryDTO;
import org.noMoon.ArtificalSociety.history.Records.HistoryRecord;
import org.noMoon.ArtificalSociety.history.Records.SchoolHistoryRecord;
import org.noMoon.ArtificalSociety.history.Records.WorkHistoryRecord;
import org.noMoon.ArtificalSociety.history.services.HistoryService;
import org.noMoon.ArtificalSociety.institution.DO.Institution;
import org.noMoon.ArtificalSociety.institution.enums.InstitutionEnum;
import org.noMoon.ArtificalSociety.institution.services.InstitutionService;
import org.noMoon.ArtificalSociety.person.DTO.PersonDTO;
import org.noMoon.ArtificalSociety.person.Enums.GenderEnum;
import org.noMoon.ArtificalSociety.person.Enums.PositionEnum;
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

    static HashMap<Integer, HashMap<String, Double>> schoolProbsByYear;

    private static CareerService careerService;
    private static HistoryService historyService;
    private static InstitutionService institutionService;

    public static void initialize(String societyId) {
        AttributeAssigner.setCareerList(careerService.listCareerWithSocietyId(societyId));
        schoolProbsByYear = institutionService.getSchoolProbTable(societyId);
    }

    public static void assignSex (PersonDTO attr) {
        // Assign a random sex to the person. Use a uniform distribution in [0, 1].
        int v = Distribution.uniform(0, 1);
        if(0==v){
            attr.setSex(GenderEnum.FEMALE);
        }else{
            attr.setSex(GenderEnum.MALE);
        }
    } // end assignSex()

    public static void assignChildAge (PersonDTO parentA, PersonDTO parentB, PersonDTO child, boolean isBackFilled) {
        // Assign the age to the child. If isBackFilled is True, then the child is not a newborn baby, and thus the age
        // will be determined based on the parents' marriage. Otherwise, the child IS a baby so the age will be 0.
        //
        // param child: the Person instance of the child being created
        // param parentA: one of the parents of the child
        // param parentB: the other parent of the child
        // param isBackFilled: the boolean flag indicating whether or not the child is being created after the fact

        if (isBackFilled) {
            // Create child after the fact (not a newborn baby!)
            assignChildAge_BackFilled(parentA, parentB, child);

        } else {
            // Create a newborn baby during a live simulation!
            assignChildAge_New(child);

        } // end if (check if the child is a newborn baby or a back-filled older person)


    } // end AssignChildAge()



    private static void assignChildAge_New (PersonDTO child) {
        // This is called when a new baby is born, and the current society year. This is necessary so that the child isn't
        // given a random age, as is the case when creating back-filled children.
        //
        // param child: the new baby who was just born!
        //
        // return: the baby with the assigned age and year of birth


        // Child was born in projectedChildBirthYear, so now just compute their current age.


        // Set the child's age.
        child.setAge(0);
        child.setBirthYear(assignBirthYear(0));

    } // end AssignChildAge()

    private static void assignChildAge_BackFilled (PersonDTO A, PersonDTO B, PersonDTO child) {
        // This is called when a back-filled child is created from a married couple in an initial population.
        // In this case, the child is given a random age and birth year based upon the parents' marriage.
        //
        // param A: one of the parents who had the child
        // param B: the other parent who had the child
        // param child: the child of parents A and B
        //
        // return: the child with an assigned age and year of birth


        //System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        //System.out.println(A.getAge());
        //System.out.println(B.getAge());
        //System.out.println(A.getRelationshipStartYear() + " | " + B.getRelationshipStartYear());

        // For simplicity, children's ages are randomly taken from a distribution over the years following the parents' marriage.
        int numYearsIntoMarriage = (int)Math.round(DistributionParser.parseStatisticalDistribution(Configuration.ParentHaveChildYearDistr));
        numYearsIntoMarriage = ValidationTools.clipValueToRange(numYearsIntoMarriage, Configuration.ParentHaveChildYearMin, Configuration.ParentHaveChildYearMax);

        // In case the chosen year for the child's birth is later than the current society year, clip it to the current year.
        int projectedChildBirthYear = A.getRelationshipStartYear() + numYearsIntoMarriage;
        if (projectedChildBirthYear > Configuration.SocietyYear) {
            // Limit the child's birth to be in current year.
            projectedChildBirthYear = Configuration.SocietyYear;
        } // end if (check if projected child's birth would be beyond current year)

        // Child was born in projectedChildBirthYear, so now just compute their current age.
        int childAge = Configuration.SocietyYear - projectedChildBirthYear;

        // Set the child's age.
        child.setAge(childAge);
        // Call method to assign birth year.
        child.setBirthYear(assignBirthYear(childAge));
    } // end AssignChildAge()

    public static void assignChildRace (PersonDTO A, PersonDTO B, PersonDTO child) {
        // Assign a race to a new child.
        // For simplicity, we will use one random parent's race as the child's race, if the parents are interracial.
        // param A: one of the parents of the child
        // param B: the other parent of the child
        // param child: the new child from parents A and B

        int childRace;

        if (A.getRaceIndex() == B.getRaceIndex()) {	// Same race.
            childRace= A.getRaceIndex();
        } else {							// Interracial.
            // Select a random parent, and use their race as the child's race.
            int rndParentIndex = Distribution.uniform(0, 1);
            if (rndParentIndex == 0) {		// If 0, use parent A's race.
                childRace = A.getRaceIndex();
            } else {						// If 1, use parent B's race.
                childRace = B.getRaceIndex();
            } // end if (determine which parent was randomly chosen)

        } // end if (check if parents are of same race)


        child.setRaceIndex(childRace);

    } // end AssignChildRace()

    public static void assignChildReligion (PersonDTO A, PersonDTO B, PersonDTO child) {
        // Assign a religion to a new child.
        // For simplicity, we will use one random parent's religion as the child's religion, if the parents are of different religions.
        // param A: one of the parents of the child
        // param B: the other parent of the child
        // param child: the new child from parents A and B

        int childReligion;

        if (A.getReligionIndex() == B.getReligionIndex()) {	// Same religion.
            childReligion= A.getReligionIndex();
        } else {									// Different religions.
            // Select a random parent, and use their religion as the child's religion.
            int rndParentIndex = Distribution.uniform(0, 1);
            if (rndParentIndex == 0) {		// If 0, use parent A's religion.
                childReligion = A.getReligionIndex();
            } else {						// If 1, use parent B's religion.
                childReligion = B.getReligionIndex();
            } // end if (determine which parent was randomly chosen)

        } // end if (check if parents are of same religion)


        child.setReligionIndex(childReligion);
    } // end AssignChildReligion()

    public static HometownHistoryDTO assignChildHometowns (PersonDTO attr, PersonDTO parentA, PersonDTO parentB) {

        HometownHistoryDTO hometownHistoryDTO=new HometownHistoryDTO();

        //System.out.println(".................... In AttributeAssigner->assignChildHometowns() ....................");

        //System.out.println("child born in " + attr.getYearBorn());

        int y;

        int finalYear;
        //System.out.println("How old is the child?? " + attr.getAge() + ", and he/she must finish at " + Configuration.SchoolFinishAge);
        // If the person is still in school, then add years between their birth and the current year, otherwise, fill from birht until the year they finished school.
        if (attr.getAge() < Configuration.SchoolFinishAge) {
            // The current year.
            finalYear = Configuration.SocietyYear;
        } else {
            // The year this person finished elementary (secondary) school.
            finalYear = attr.getBirthYear()+Configuration.SchoolFinishAge;
        } // end if (determine the final year for the child living with their parents - either the current year or their last school year)

        ArrayList<Object> actEntry;
        int[] tmpActYears;
        int tmpEntryBeginYear;
        int tmpEntryFinalYear;
        HometownHistoryDTO parentAHometownHistory=historyService.getHometownHistoryById(parentA.getHometownHistoryId());
        // Loop through all childhood years from birth until the end of elementary (secondary) school.
        for (y = attr.getBirthYear(); y < finalYear; y++) {
            //System.out.println("> " + y + " | " + parentA.getHometownHistory().getActivityAtYear( y ));

            HistoryRecord record = parentAHometownHistory.getActivityByYear(y);

            tmpActYears = new int[]{record.getStartYear(),record.getEndYear()};

            // ADJUST STARTING YEAR OF ENTRY.
            // If the year, in this loop iteration, is the first one of that entry, then indicate that first year.
            if (y == tmpActYears[0]+1) {
                tmpEntryBeginYear = y - 1;
            } else {
                tmpEntryBeginYear = y;
            } // end if (check if the loop year is the first year of the given activity entry)

            // ADJUST ENDING YEAR OF ENTRY.
            // If the year, in this loop iteration, is the final one of that entry, then indicate that final year.
            if (y == tmpActYears[1]) {
                tmpEntryFinalYear = y;
            } else {
                tmpEntryFinalYear = y+1;
            } // end if (check if the loop year is the final year of the given activity entry)

            //tmpChildArchive.addEntry(actEntry.get(0), tmpActYears[0], tmpActYears[1]);
            //tmpChildArchive.addEntry(actEntry.get(0), y, y+1);
            hometownHistoryDTO.getRecordList().add(new HistoryRecord(record.getLocation(),tmpEntryBeginYear,tmpEntryFinalYear));

        } // end for y (loop through years that child lives with parents)

        // Ensure the last hometown was added properly. If there was a move in the current societal year, then it won't add
        // properly initially. This will ensure that it is appended to the child's archives.
        HistoryRecord lastRecord=parentAHometownHistory.getLastActivity();

        if (lastRecord.getStartYear() == Configuration.SocietyYear) {
            hometownHistoryDTO.getRecordList().add(new HistoryRecord(lastRecord.getLocation(),lastRecord.getStartYear(),lastRecord.getEndYear()));
        } // end if (check if last activity begins in current year, and adds it to the child's archive)

        // Add last entry separately.

        hometownHistoryDTO.patchSequentialEntries();

        return hometownHistoryDTO;

    } // end assignChildHometowns()

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
        if (null != tempHometownHistoryDTO) {
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

    public static void assignSchoolHistory(PersonDTO attr) {
        // Determine when person was living in the society during school years, and add each of those periods to the school archive.
        // Modifies attributes: schoolHistory, soc_SchoolHistory

        SchoolHistoryDTO schoolHistory = new SchoolHistoryDTO();        // Entire archive.
        SchoolHistoryDTO socSchoolHistory = new SchoolHistoryDTO();    // Only in society.
        schoolHistory.setPersonId(attr.getId());
        socSchoolHistory.setPersonId(attr.getId());
        schoolHistory.setSocietyId(attr.getSocietyId());
        socSchoolHistory.setSocietyId(attr.getSocietyId());

        // Ensure that the person's birth year has been set.
//System.out.println(ValidationTools.empty(attr.yearBorn) + " || " + ValidationTools.empty(attr.age) + " || " + ValidationTools.empty(attr.hometownHistory)  + " || " + ValidationTools.empty(attr.education) + " || " + ValidationTools.empty(attr.educationPSYears));
        if (attr.getBirthYear() == 0 || 0 == attr.getAge() || 0 == attr.getHometownHistoryId() || StringUtils.isEmpty(attr.getEducation()) || 0 == attr.getEducationPsYear()) {
            System.err.println("Attempting to assign schoolHistory but birthYear, age, hometownHistory, education, or eduPSYears have not been set.");
            return;
        } // end if (ensure person has prerequisite information assigned)

        ArrayList<Integer> schoolYearsAnywhere = new ArrayList<Integer>();
        ArrayList<Integer> schoolYearsInSociety = new ArrayList<Integer>();

        // Check if person is old enough to have already started school.
        if (attr.getAge() < Configuration.SchoolStartAge) {
            //System.err.println("Attempting to assign schoolHistory but person is too young to attend school.");
            historyService.insertNewHistoryDTO(schoolHistory);
            historyService.insertNewHistoryDTO(socSchoolHistory);
            attr.setSchoolHistoryId(schoolHistory.getId()); // This is important so the child at least has an initialized archive.
            attr.setSocSchoolHistoryId(socSchoolHistory.getId()); // This is important so the child at least has an initialized archive.
            return;
        } // end if (check if old enough to at least have started school)

        // Determine age of person in their final year of school - or current age if they are still in school.
        int finalSchoolYearAge;
        if (attr.getAge() > Configuration.SchoolFinishAge) {
            finalSchoolYearAge = Configuration.SchoolFinishAge;
        } else {
            finalSchoolYearAge = attr.getAge();
        } // end if (determining if currently in school or finished school)

        //System.out.println(Configuration.SchoolStartAge + " -- " + finalSchoolYearAge);

        int y;
        int sch_year;

        HometownHistoryDTO hometownHistoryDTO = historyService.getHometownHistoryById(attr.getHometownHistoryId());


        for (y = Configuration.SchoolStartAge; y < finalSchoolYearAge; y++) {

            // Get the actual year number.
            sch_year = attr.getBirthYear() + y;

            // Get activity from archive for the given year.
            // *** NOTE ***	This should be checked with someone who moves cities during their school years.
            // 				Should both locations be added or just one of the two?!
            HistoryRecord hometown = hometownHistoryDTO.getActivityByYear(sch_year);

            // Determine if person was in the current society during this school year.
            if (hometown.getLocation().equals(Configuration.SocietyName)) {
                // If the person lived in this society, then add year to array.
                schoolYearsInSociety.add(new Integer(sch_year));
            } // end if (person lived in this society)
            schoolYearsAnywhere.add(new Integer(sch_year));
        } // end for y (looping through all school years)


        String schoolName = "Elementary";
        // If array of schoolYearsInSociety is empty now, then exit function, because the person is either too young to attend school or lived in a different city during their school years.
        if (!schoolYearsInSociety.isEmpty()) {
            // Now that at this point, the person spent at least one year in this society during school years, determine what school(s) they attended.

            // +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
            // *** NOTE *** THIS IS ASSUMING THE SCHOOL WAS IN SERVICE THROUGHOUT THEIR ENTIRE CHILDHOOD. THIS WILL NOT ALWAYS BE TRUE!
            // 				CURRENTLY ALL SCHOOLS ARE SET TO GO FROM 1900 TO PRESENT TO AVOID THIS PROBLEM, BUT THIS SHOULD BE MODIFIED
            //				SO IF A SCHOOL SHUTS DOWN, A NEW RANDOM SCHOOL IS CHOSEN FOR THE STUDENT.
            // +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
            Integer firstSchoolYear = (Integer) schoolYearsInSociety.get(0);
            //System.out.println(firstSchoolYear);
            HashMap<String, Double> schoolProb = schoolProbsByYear.get(firstSchoolYear);
            String[] titles = schoolProb.keySet().toArray(new String[schoolProb.size()]);
            double[] schoolProbs = new double[titles.length];
            for (int i = 0; i < titles.length; i++) {
                schoolProbs[i] = schoolProb.get(titles[i]);
            }

            // Assume each person only went to one school while living in this society. This could be expanded to allow school changes, but this is
            // not an important feature, as the school history is really only used in putting the person into groups for possible friend networking.
            int rndSchoolIndex = Distribution.custom(schoolProbs);
            //addConsecutiveActivitySequences(schoolYearsInSociety, socSchoolHistory, Schools.getSchoolTitleAt(rndSchoolIndex));
            schoolName = titles[rndSchoolIndex];
            addConsecutiveActivitySequences(schoolYearsInSociety, socSchoolHistory, schoolName);

        } // end if (check if array is empty)

        if (!schoolYearsAnywhere.isEmpty()) {
            //addConsecutiveActivitySequences(schoolYearsAnywhere, schoolHistory, "Elementary");
            addConsecutiveActivitySequences(schoolYearsAnywhere, schoolHistory, schoolName);
        } // end if (check if socSchoolhistory is empty)


        // ------------------------------------------------------------------------------------
        // Post-Secondary Education
        // ------------------------------------------------------------------------------------

        if (attr.getAge() >= Configuration.SchoolFinishAge) {

            int PSStartYear, PSEndYear;
            PSStartYear = attr.getYearStartedPsSchool();

            // Check if person is finished or currently enrolled in post-secondary school.
            if (attr.getYearFinishedPsSchool() < Configuration.SocietyYear) {
                PSEndYear = attr.getYearFinishedPsSchool();
            } else {
                PSEndYear = Configuration.SocietyYear;
            } // end if (check if person has started post-secondary school already)


            // * NOTE * This assumes that the person stayed at ONE post-secondary and lived in ONE city during that period. If the program is modified to allow
            //			moves to other cities and transfers to other institutions, then this part will have to be modified to allow those changes.
            //String PSLocation = (String)attr.hometownHistory.getActivityAtYear(PSStartYear+1); // Add 1 to start year because that year is also in the archive as the final year of elementary school.
            String PSLocation = hometownHistoryDTO.getActivityByYear(PSStartYear + 1).getLocation(); // Add 1 to start year because that year is also in the archive as the final year of elementary school.

            //System.out.println(PSLocation);

            // * NOTE * This assumes the institutions were/are in service the whole time this simulation takes place. If this needs to be changed, then the school's time
            //			period would have to be examined to see if it was/is in existence during the person's required time in post-secondary school.
            //String[] schoolsByType = ArrayTools.arrayListToStringArray(Schools.getSchoolsByFilters(Schools.getFullPostSecondarySchoolsDatabase(), new int[] {Schools.School_Type, Schools.School_City}, new Object[] {attr.education, PSLocation}, new int[] {Schools.School_Name}));
            List<String> psSchoolsNames = institutionService.selectPSSchoolNamesBySocietyId(Configuration.Society_Id, attr.getEducation(), PSLocation);
            String[] schoolsByType = psSchoolsNames.toArray(new String[psSchoolsNames.size()]);

            // Check if there were no post-secondary schools found for the person (either because they didn't require any or if they are too young yet).
            if (schoolsByType.length == 0) {
                ///////
                // NOTE: the following inner if-statement block can be removed. It was a simple debugging check, but not necessary.
                ///////
                if (attr.getEducationPsYear() > 0) {
                    //System.err.println("In AttributeAssigner->assignSchoolHistory(), there were no post-secondary institutions for this person!");
                    // Add post-secondary school to archive. Assume that attr.education is the type of school this school is (i.e. U or C)
                    schoolHistory.getRecordList().add(new SchoolHistoryRecord("External Institution", PSStartYear, PSEndYear, attr.getEducation()));
                } else {
                    // Do nothing. These people did not require post-secondary, so it won't find any for them!
                } // end if (check if person who did not receive an institution was supposed to attend one or not)
            } else {
                String institution = (String) Distribution.uniformRandomObject(schoolsByType);
                //System.out.println(institution);

                // Add post-secondary school to archive. Assume that attr.education is the type of school this school is (i.e. U or C)
                schoolHistory.getRecordList().add(new SchoolHistoryRecord(institution, PSStartYear, PSEndYear, attr.getEducation()));


                // If the person's post-secondary institution is in the society, then add it to the local archive.
                if (PSLocation.equals(Configuration.SocietyName)) {
                    socSchoolHistory.getRecordList().add(new SchoolHistoryRecord(institution, PSStartYear, PSEndYear, attr.getEducation()));
                    //socSchoolHistory.addEntry(institution, PSStartYear, PSEndYear);
                } // end if (institution location is in the local society)

            } // end if (no institutions were found for the given criteria)

        } // end if (person is done elementary school, and thus post-secondary school is assigned)

        //DebugTools.printArray(schoolHistory);
        schoolHistory.patchSequentialEntries();
        socSchoolHistory.patchSequentialEntries();
        historyService.insertNewHistoryDTO(schoolHistory);
        historyService.insertNewHistoryDTO(socSchoolHistory);

        attr.setSchoolHistoryId(schoolHistory.getId());
        attr.setSocSchoolHistoryId(socSchoolHistory.getId());

    } // end assignSchoolHistory()

    private static void addConsecutiveActivitySequences(ArrayList<Integer> yearsInSociety, SchoolHistoryDTO archive, String schoolName) {
        // Loop through all years listed in schoolYearsInSociety array, and check for consecutive years so that each sequence of consecutive
        // years can be added to the schoolHistory archive.
        // param yearsInSociety: the arraylist of all individual years that person was in society as a student

        int i;
        int yr, prevYr;
        int consecutiveSequenceStartYear;
        int currLastYearInSequence;
        int numSchoolYearsInSociety = yearsInSociety.size();
        prevYr = yearsInSociety.get(0);
        consecutiveSequenceStartYear = prevYr;        // This will hold the start year of a new sequence.
        currLastYearInSequence = prevYr;            // This will hold the final year in the current sequence, which will be updated on at each iteration through the loop. At the end of a sequence, this will be hold the final year of that sequence.
        // If person was in society for one than one year.
        if (numSchoolYearsInSociety > 1) {


            // Start at index 1, because the index 0 year is dealt with before this loop as an initialization.
            for (i = 1; i < numSchoolYearsInSociety; i++) {
                yr = Integer.parseInt(yearsInSociety.get(i).toString());

                if (yr == prevYr + 1) {
                    // If this year at index i is the consecutive year after the one at index i-1, then update the "finalYear" in the sequence.
                    currLastYearInSequence = yr;
                } else {
                    // End previous sequence. The last sequence is finished now, so add that entry to the array.
                    archive.getRecordList().add(new SchoolHistoryRecord(schoolName, consecutiveSequenceStartYear, currLastYearInSequence, "Elementary"));

                    // Create new sequence.
                    consecutiveSequenceStartYear = yr;
                    currLastYearInSequence = yr;
                } // end if (two years are consecutive)

                // Keep track of previous year.
                prevYr = yr;
            } // end for i (loop through schoolYearsInSociety array)

            archive.getRecordList().add(new SchoolHistoryRecord(schoolName, consecutiveSequenceStartYear, currLastYearInSequence, "Elementary"));

        } // end if (more than 1 year in array)

    } // end addConsecutiveActivitySequences()


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
/*
    public static void createRelationship (PersonDTO personA, PersonDTO personB, RelationStatusEnum relType) {
        // Indicate the relationship between the two persons, personA and personB.
        // param personA: one person in the relationship
        // param personB: the other person in the relationship
        // param relType: an integer representing the relationship type {0 = Single, 1 = Married, 2 = Dating}

        personA.setRelationshipStatus(relType);
        personB.setRelationshipStatus(relType);

        personA.setPartnerId(personB.getId());
        personB.setPartnerId(personA.getId());

        RelationshipCalculator.calculateAndSetInterestSimilarity(personA, personB);


        RelationshipCalculator.CalculateAndSetRelationshipStrength(personA, personB, 0); // Before children are created, so pass = 0.

        // DELETED assignRelationshipStart() on Feb. 16.
        // Determine the year this relationship began (if married, then it's the year that they became married, not the year they started dating).
        //AttributeAssigner.assignRelationshipStart(personA, personB, relType);

        // Calculate relationship strength between couple.
        //double relStrength = RelationshipCalculator.CalculateRelationshipStrength(personA, personB);
        //personA.relationshipStrength = relStrength;
        //personB.relationshipStrength = relStrength;

        int familyID = PersonGroupAdder.nextFamilyID;
        personA.setFamilyID(familyID);
        personB.setFamilyID(familyID);

        // Increment static variable for nextFamilyID, so each new relationship creates a new family ID.
        PersonGroupAdder.nextFamilyID++;

    } // end createRelationship()
*/

    public static void assignWorkHistory(PersonDTO attr) {
        // Determine all places the person has worked throughout their life, and add each of those work periods to the work archive.
        // Modifies attributes: workHistory, soc_WorkHistory

        // * NOTE: For now, just put person into current career, such that they began after finishing school and held same job to present.
        // This will change eventually so that they have a series of different jobs throughout their life.

        WorkHistoryDTO workHistory = new WorkHistoryDTO();
        WorkHistoryDTO societalWorkHistory = new WorkHistoryDTO();
        workHistory.setPersonId(attr.getId());
        workHistory.setSocietyId(attr.getSocietyId());
        societalWorkHistory.setPersonId(attr.getId());
        societalWorkHistory.setSocietyId(attr.getSocietyId());

//        if (ValidationTools.empty(attr.getAge()) || ValidationTools.empty(attr.getCareer()) || ValidationTools.empty(attr.getSchoolHistory())) {
//            // NOTE: Do not bother displaying message, because young children will not have school history yet, so no need to display error messages for them!
//            //System.err.println("Attempting to assign workHistory but age, careerID, or schoolHistory have not been set.");
//            return;
//        } // end if (check if person has the prerequisite information to get a work history)

        // Determine if person is currently in school. Assume they have no work until after done school (part-time work would have to be different. Ignore that for now!)
        if (attr.getIsInSchool()) {
            // Rather than having a null archive, we want to set the empty (but initialized!) archives here before returning out.
            attr.setIncome(0);
            attr.setCurrentPosition(PositionEnum.STUDENT);
            historyService.insertNewHistoryDTO(workHistory);
            historyService.insertNewHistoryDTO(societalWorkHistory);
            attr.setWorkHistoryId(workHistory.getId());
            attr.setSocWorkHistoryId(societalWorkHistory.getId());
            return;
        } // end if (person is in school currently)

        // Get the year in which the person finished all their school.
        //int finishedSchoolYear = attr.getSchoolHistory().getLastActivityPeriod()[1];


        HometownHistoryDTO hometownHistoryDTO = historyService.getHometownHistoryById(attr.getHometownHistoryId());
        SchoolHistoryDTO schoolHistoryDTO = historyService.getSchoolHistoryById(attr.getSchoolHistoryId());

        int finishedSchoolYear = schoolHistoryDTO.getRecordList().get(schoolHistoryDTO.getRecordList().size() - 1).getEndYear();
        //if (attr.getID() == 15) System.out.println("finishedSchoolYear = " + finishedSchoolYear);

        // Loop through all ENTRIES in the hometownHistory (not looping through individual years!).
        int i;
        String hometownName;
        List<HistoryRecord> hometownRecordList = hometownHistoryDTO.getRecordList();
        CareerDTO careerDTO = new CareerDTO(careerService.selectCareerById(attr.getCareerId()));

        for (i = 0; i < hometownRecordList.size(); i++) {
            HistoryRecord hometownHistoryRecord = hometownRecordList.get(i);
            hometownName = hometownHistoryRecord.getLocation(); // 0th element is location name.
            int startYear = hometownHistoryRecord.getStartYear();
            int endYear = hometownHistoryRecord.getEndYear();
            //System.out.println(hometownName + " in [" + hometownYears[0] + "," + hometownYears[1] + "].");

            // =========================================================
            // CHECK WHICH ENTRIES ARE DURING PERSON's WORKING LIFE.
            // =========================================================


            // -------------------------------------------------------------
            // Hometown period DURING which the person become working age.
            // -------------------------------------------------------------
            if (ValidationTools.numberIsWithin(finishedSchoolYear, startYear, endYear) && (finishedSchoolYear != endYear)) { // The second condition is to prevent the end of an entry being used.

                if (hometownName.equals(Configuration.SocietyName)) {
                    //if (attr.getID() == 15) System.out.println("Need to find DETAILED work for [" + finishedSchoolYear + "," + hometownYears[1] + "]. (PARTIAL)");
                    assignLocalWorkplaces(attr, careerDTO, workHistory, societalWorkHistory, finishedSchoolYear, endYear);
                } else {
                    //if (attr.getID() == 15) System.out.println("Need to find external work for [" + finishedSchoolYear + "," + hometownYears[1] + "]. (PARTIAL)");
                    assignExternalWorkplaces(attr, careerDTO,workHistory,finishedSchoolYear, endYear);
                } // end if (check if hometown for this period is local society)

            } // end if (determine the hometown period DURING which the person becomes working age)

            // -------------------------------------------------------------
            // Hometown periods AFTER person is working age.
            // -------------------------------------------------------------
            if (startYear > finishedSchoolYear) {

                if (hometownName.equals(Configuration.SocietyName)) {
                    //if (attr.getID() == 15) System.out.println("Need to find DETAILED work for [" + hometownYears[0] + "," + hometownYears[1] + "]. (FULL)");
                    assignLocalWorkplaces(attr, careerDTO, workHistory, societalWorkHistory, startYear, endYear);
                } else {
                    //if (attr.getID() == 15) System.out.println("Need to find external work for [" + hometownYears[0] + "," + hometownYears[1] + "]. (FULL)");
                    assignExternalWorkplaces(attr,careerDTO,workHistory,startYear, endYear);
                } // end if (check if hometown for this period is local society)

            } // end if (determining the hometown periods completely AFTER the peson is working age)

        } // end for i (loop through all entries in hometown history)

        workHistory.patchSequentialEntries();
        societalWorkHistory.patchSequentialEntries();
        historyService.insertNewHistoryDTO(workHistory);
        historyService.insertNewHistoryDTO(societalWorkHistory);
        attr.setWorkHistoryId(workHistory.getId());
        attr.setSocWorkHistoryId(societalWorkHistory.getId());

    } // end assignWorkHistory()

    private static void assignLocalWorkplaces(PersonDTO attr, CareerDTO career, WorkHistoryDTO workHistory, WorkHistoryDTO socWorkHistory, int startYear, int endYear) {
        // This method will find a workplace in the local society for the person over the given period.
        //
        // param attr: the given Person who is being given a work history
        // param startYear: the integer of the year that begins this period of work
        // param endYear: the integer of the year that ends this period of work

        // Get list of possible workplaces person could work with the pre-determined career.
        List<Long> workplaceIds = career.getWorkplaceId();


        // If the peron's career does not match any workplace options, then stop now. (This should only ever be the case for "Unemployed, id="00000").
        if (workplaceIds.size() == 0) {
            //System.err.println("Person does not have a career that any workplace is interested in.");
            // Rather than having a null archive, we want to set the empty (but initialized!) archives here before returning out.
            //attr.setWorkHistory(workHistory);
            //attr.setSocietalWorkHistory(localWorkHistory);
            return;
        } // end if (check if any possible work locations were found)

        int workplaceIndex = Distribution.uniform(0, workplaceIds.size() - 1);
        String workplaceId = String.valueOf(workplaceIds.get(workplaceIndex));


        // Add career to archive to start from the year they finished school until present.
//        Workplace workplace = careerService.selectWorkplaceById(workplaceId);
        // Note the array is of the format [workplaceID, careerID].
        workHistory.getRecordList().add(new WorkHistoryRecord(startYear, endYear, career.getId(), workplaceId));
        socWorkHistory.getRecordList().add(new WorkHistoryRecord(startYear, endYear, career.getId(), workplaceId));

        // Update person's current position to WORKIN
        attr.setCurrentPosition(PositionEnum.WORKING);


        // --------------------------------------------------------------------------------------------------------
        // FROM assignIncome()
        //
        // Get the base income associated with the person's career.
        int careerIncome = (int) Math.round(career.getSalaryMean());

        // Randomly choose an amount (positive or negative) to offset the person's income from the base.
        int salarySTD = Distribution.uniform(-Configuration.SalarySTD, Configuration.SalarySTD);
        attr.setIncome(careerIncome + salarySTD);
        //
        // FROM assignIncome()
        // --------------------------------------------------------------------------------------------------------

    } // end assignLocalWorkplaces()

    private static void assignExternalWorkplaces(PersonDTO attr, CareerDTO career, WorkHistoryDTO workHistory, int startYear, int endYear) {
        // This method will find a workplace in an external location for the person over the given period.
        //
        // param attr: the given Person who is being given a work history
        // param startYear: the integer of the year that begins this period of work
        // param endYear: the integer of the year that ends this period of work


        String chosenWorkplace = "External Company";

        // Add career to archive over the given period.
        workHistory.getRecordList().add(new WorkHistoryRecord(startYear,endYear,career.getId(),chosenWorkplace));


        // Update person's current position to WORKING.
        attr.setCurrentPosition(PositionEnum.WORKING);


        // --------------------------------------------------------------------------------------------------------
        // FROM assignIncome()
        //

        // Get the base income associated with the person's career.
        int careerIncome = (int)Math.round(career.getSalaryMean());
        // Randomly choose an amount (positive or negative) to offset the person's income from the base.
        int salarySTD = Distribution.uniform(-Configuration.SalarySTD, Configuration.SalarySTD);
        attr.setIncome(careerIncome + salarySTD);
        //
        // FROM assignIncome()
        // --------------------------------------------------------------------------------------------------------


    } // end assignExternalWorkplaces()

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
