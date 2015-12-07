package org.noMoon.ArtificalSociety.NetworkGenerator;

import org.noMoon.ArtificalSociety.commons.utils.Configuration;
import org.noMoon.ArtificalSociety.person.DTO.PersonDTO;
import org.noMoon.ArtificalSociety.person.Enums.PositionEnum;
import org.noMoon.ArtificalSociety.person.Enums.RelationStatusEnum;
import org.noMoon.ArtificalSociety.person.services.FriendshipService;
import org.noMoon.ArtificalSociety.person.services.PersonService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noMoon on 2015-11-17.
 */
public class SimulationStepper {

    private static PersonService personService;
    private static FriendshipService friendshipService;


    public static void begin (int n) {

        int i;
        for (i = 0; i < n; i++) {
            Configuration.SocietyYear += 1;
            evaluateSociety();

        } // end for i (loop through simulation steps)


    } // end begin()


    public static void runToPopulationThreshold (int popThreshold) {


        List<String> allPersonId=personService.getAllIds(Configuration.Society_Id);

        while (allPersonId.size() < popThreshold) {
            Configuration.SocietyYear += 1;
            evaluateSociety();

            allPersonId=personService.getAllIds(Configuration.Society_Id);
            if (allPersonId.size() == 0) {
                break;
            }

        } // end for i (loop through simulation steps)

    } // end begin()


    public static void evaluateSociety () {

        List<String> allAlivePersonId=personService.getAllIds(Configuration.Society_Id);
        List<PersonDTO> deadPersonIdList=new ArrayList<PersonDTO>();
        for(String alivePersonId:allAlivePersonId){
            //get person
            PersonDTO person=personService.selectPerosonDTOById(alivePersonId);

            // Evaluate if person has died.
            boolean isPersonDying = evaluatePerson_Death(person);

            // Handle person dying this year. Put them into the array, so that they will die at the end of the year.
            if (isPersonDying) {
                deadPersonIdList.add(person);
            } // end if (check if person is supposed to die this year)

            // -----------------------------------------------------------------------------------------------
            // Evaluate all elements for persons.
            // -----------------------------------------------------------------------------------------------

            // Evaluate person's basic information.
            evaluatePerson_Basic(person);
            // Evaluate person's relationship status.
            evaluatePerson_Relationship(person);
            // Evaluate person's current position (student, working, etc).
            evaluatePerson_CurrentPosition(person);

            // Evaluate person's hometown.
            evaluatePerson_Hometown(person);
            // Evaluate person's school/work involvement.
            evaluatePerson_PositionInvolvement(person);
            // Evaluate person's hometown.
            //evaluatePerson_Hometown(person);


            // Evaluate person's groups (school, work, temple, and clubs groups).
            evaluatePerson_Groups(person);
            // Evaluate person's friendships.
            evaluatePerson_Friends(person);
            personService.updatePersonDTOById(person);

        } // end for i (loop through all persons)


        // -----------------------------------------------------------------------------------------------
        // At the end of the year, all people who were to die this year are now going to die.
        // -----------------------------------------------------------------------------------------------
        int d;
        for(PersonDTO person:deadPersonIdList){
            personService.removePerson(person);
        }
        //if (Configuration.SocietyYear >= 2056) {
        //System.out.println("At the end of year " + Configuration.SocietyYear);
        //ArtificialSociety.DisplayIndexTable();
        //}

    } // end evaluateSociety()


    private static void evaluatePerson_Basic (PersonDTO person) {

        person.setAge(Configuration.SocietyYear-person.getBirthYear());

    } // end evaluatePerson_Basic()


    private static boolean evaluatePerson_Death (PersonDTO person) {

        // Determine if person has reached their life expectancy.
        if (Configuration.SocietyYear >= person.getExpDeathYear()) {
            //if (Configuration.SocietyYear == 2058) {
            //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>> PERSON #" + person.getID() + " JUST DIED! :(");
            //}
            //person.die();
            // Return true to indicate person having to die this year.
            return true;
        } // end if (check if person's life expectancy has been reached)

        // Person does not die now.
        return false;

    } // end evaluatePerson_Death()

    private static void evaluatePerson_Relationship (PersonDTO person) {
        PersonDTO partner;
        if(person.getRelationshipStatus().equals(RelationStatusEnum.SINGLE)){
            personService.evaluateRelationship_Single(person);
        }else if(person.getRelationshipStatus().equals(RelationStatusEnum.MARRIED)){
            partner = personService.selectPerosonDTOById(person.getPartnerId());
            personService.evaluateRelationship_Couple(person, partner, RelationStatusEnum.MARRIED);
        } else if(person.getRelationshipStatus().equals(RelationStatusEnum.DATING)) {
            partner = personService.selectPerosonDTOById(person.getPartnerId());
            personService.evaluateRelationship_Couple(person, partner, RelationStatusEnum.DATING);
        } // end switch (checking the person's relationship status)
    } // end evaluatePerson_Relationship()

    private static void evaluatePerson_CurrentPosition (PersonDTO person) {

        PositionEnum pos = person.getCurrentPosition();

        if (pos.equals( PositionEnum.CHILD )) {
            // Look for year to begin school.
            evaluatePosition_Child(person);
        } else if (pos.equals( PositionEnum.STUDENT )) {
            // Check for year finished school, then look for work.
            evaluatePosition_Student(person);
        } else if (pos.equals( PositionEnum.WORKING )) {
            // Check age range.
            evaluatePosition_Working(person);
        } else if (pos.equals( PositionEnum.UNEMPLOYED )) {
            // Look for work, if within working age range.
            evaluatePosition_Unemployed(person);
        } else if (pos.equals( PositionEnum.RETIRED )) {
            // Do nothing. Person is retired.
            evaluatePosition_Retired(person);
        } else if (pos.equals( PositionEnum.DEAD )) {
            // Do nothing. Person is dead.
        } else {
            // Do nothing.
        } // end if (determine which position person was in)

        // TODO (Do I need this? I don't think I do.. It seems to all be handled in here already!!)
        //AttributeAssigner.assignCurrentPosition(person);

    } // end evaluatePerson_CurrentPosition()

    private static void evaluatePerson_PositionInvolvement (PersonDTO person) {

        //System.out.println("person.getCurrentPosition() = " + person.getCurrentPosition());

        if (person.getCurrentPosition().equals( PositionEnum.STUDENT )) {
            // STUDENT.
            personService.evaluatePerson_Student(person);

        } else if (person.getCurrentPosition().equals( PositionEnum.UNEMPLOYED )) {
            // UNEMPLOYED.
            //System.err.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            //System.out.println("Before, person " + person.getID() + " unemployed [" + person.getCareer() + "].");
            personService.evaluatePerson_Unemployed(person);
            //System.out.println("After, person " + person.getID() + " employed with [" + person.getCareer() + "].");
            //DebugTools.printArray(person.getWorkHistory());
            //System.out.println("---------------------------------------------------------");

        } else if (person.getCurrentPosition().equals( PositionEnum.WORKING )) {
            // WORKING.
            personService.evaluatePerson_Working(person);

        } // end if (check if person is student


    } // end evaluatePerson_PositionInvolvement()

    private static void evaluatePerson_Hometown (PersonDTO person) {
        personService.evaluatePerson_Hometown(person);

    } // end evaluatePerson_Hometown()

    private static void evaluatePerson_Groups (PersonDTO person) {

        personService.evaluatePerson_Groups(person);

        //PersonGroupAdder.AddPersonToAllGroups(person);

    } // end evaluatePerson_Groups()


    private static void evaluatePerson_Friends (PersonDTO person) {

        friendshipService.createFriendshipNetwork(person);

    } // end evaluatePerson_Friends()

    // -----------------------------------------------------------------------------------------------------------------------------
    // SUB-FUNCTIONS FOR CURRENT POSITION EVALUATION
    // -----------------------------------------------------------------------------------------------------------------------------

    private static void evaluatePosition_Child (PersonDTO person) {
        // Examine child from time t-1, and determine their position at time t.
        // param person: the Person whom we are examining
        //
        // Note: possible position states that could be assigned in this method: {STUDENT, CHILD (same)}

        if (person.getAge() >= Configuration.SchoolStartAge) {
            // Child is now old enough to begin school.

            person.setIsInSchool(true);
            person.setCurrentPosition(PositionEnum.STUDENT);

        } // end if (check if child is old enough for school)

    } // end evaluatePosition_Child()

    private static void evaluatePosition_Student (PersonDTO person) {
        // Examine child from time t-1, and determine their position at time t.
        // param person: the Person whom we are examining
        //
        // Note: possible position states that could be assigned in this method: {UNEMPLOYED, STUDENT (same)}


        if (Configuration.SocietyYear >= person.getYearFinishedPsSchool()) {
            // Person is done school, so they are now unemployed and will begin searching for work.
            person.setIsInSchool(false);
            person.setCurrentPosition( PositionEnum.UNEMPLOYED );
            //if (person.getID() == 36) System.err.println("NOW UNEMPLOYED FROM SCHOOL");
        } else {
            // Do nothing. Person is still a student.
        } // end if (check if person has finished their school, based only on their age)


    } // end evaluatePosition_Student()

    private static void evaluatePosition_Working (PersonDTO person) {
        // Examine working person from time t-1, and determine their position at time t.
        // param person: the Person whom we are examining
        //
        // Note: possible position states that could be assigned in this method: {RETIRED, WORKING (same)}
        // Note: working person can also go to UNEMPLOYED option, but NOT from this method! That would occur from evaluatePerson_Working().

        if (person.getAge() > Configuration.WorkingAgeMax) {
            // Person is old enough, so let them retire.
            person.setCurrentPosition( PositionEnum.RETIRED );
        } // end if (check if person is at age for retirement)

    } // end evaluatePosition_Working()

    private static void evaluatePosition_Unemployed (PersonDTO person) {
        // Examine unemployed person from time t-1, and determine their position at time t.
        // param person: the Person whom we are examining
        //
        // Note: possible position states that could be assigned in this method: {RETIRED, UNEMPLOYED (same)}
        // Note: unemployed person can also go to WORKING option, but NOT from this method! That would occur from evaluatePerson_Unemployed().

        if (person.getAge() >= Configuration.WorkingAgeMax) {
            // Retire.
            person.setCurrentPosition( PositionEnum.RETIRED );
        } // end if (check if person is old enough that they are considered retired rather than unemployed)

    } // end evaluatePosition_Unemployed()

    private static void evaluatePosition_Retired (PersonDTO person) {
        // Examine retiree from time t-1, and determine their position at time t.
        // param person: the Person whom we are examining
        //
        // Note: possible position states that could be assigned in this method: {RETIRED (same)}

        // Do nothing. Once retired, always retired.

    } // end evaluatePosition_Retired()



    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    public void setFriendshipService(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }
}
