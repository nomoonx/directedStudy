package org.noMoon.ArtificalSociety.person.services.impl;

import org.noMoon.ArtificalSociety.career.services.CareerService;
import org.noMoon.ArtificalSociety.commons.Enums.SequenceEnum;
import org.noMoon.ArtificalSociety.commons.services.SequenceService;
import org.noMoon.ArtificalSociety.commons.utils.Configuration;
import org.noMoon.ArtificalSociety.commons.utils.Distribution;
import org.noMoon.ArtificalSociety.commons.utils.ValidationTools;
import org.noMoon.ArtificalSociety.group.DTO.GroupDTO;
import org.noMoon.ArtificalSociety.group.Services.GroupService;
import org.noMoon.ArtificalSociety.history.DTO.HometownHistoryDTO;
import org.noMoon.ArtificalSociety.history.DTO.SchoolHistoryDTO;
import org.noMoon.ArtificalSociety.history.DTO.WorkHistoryDTO;
import org.noMoon.ArtificalSociety.history.Records.HistoryRecord;
import org.noMoon.ArtificalSociety.history.Records.SchoolHistoryRecord;
import org.noMoon.ArtificalSociety.history.Records.WorkHistoryRecord;
import org.noMoon.ArtificalSociety.history.services.HistoryService;
import org.noMoon.ArtificalSociety.person.DAO.FriendshipMapper;
import org.noMoon.ArtificalSociety.person.DAO.PersonMapper;
import org.noMoon.ArtificalSociety.person.DO.Friendship;
import org.noMoon.ArtificalSociety.person.DO.PersonWithBLOBs;
import org.noMoon.ArtificalSociety.person.DTO.PersonDTO;
import org.noMoon.ArtificalSociety.person.Enums.GenderEnum;
import org.noMoon.ArtificalSociety.person.Enums.PositionEnum;
import org.noMoon.ArtificalSociety.person.Enums.RelationStatusEnum;
import org.noMoon.ArtificalSociety.person.services.PersonService;
import org.noMoon.ArtificalSociety.person.utils.AttributeAssigner;
import org.noMoon.ArtificalSociety.person.utils.GroupAdder;
import org.noMoon.ArtificalSociety.person.utils.RelationshipCalculator;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noMoon on 2015-10-17.
 */
public class PersonServiceImpl implements PersonService {

    private PersonMapper personMapper;
    private SequenceService sequenceService;
    private CareerService careerService;
    private GroupService groupService;
    private FriendshipMapper friendshipMapper;
    private HistoryService historyService;

    public void generatePerson(int singleNumber, int coupleNumber, int datingNumber, String societyId) {
        initializeAttributeAssigner(societyId);
        generateSinglePerson(singleNumber, societyId, 2 * coupleNumber + 2 * datingNumber + singleNumber);
        createCouples(coupleNumber, datingNumber);

        releaseAttributeAssigner();
    }

    private void initializeAttributeAssigner(String societyId) {
        AttributeAssigner.initialize(societyId);
    }

    private void releaseAttributeAssigner() {
        AttributeAssigner.getCareerList().clear();
    }

    private void generateSinglePerson(int number, String societyId, int popSize) {
        List<PersonWithBLOBs> insertList = new ArrayList<PersonWithBLOBs>();
        for (int i = 0; i < number; i++) {
            PersonDTO personDTO = new PersonDTO();
            personDTO.setSocietyId(societyId);
            AttributeAssigner.assignSex(personDTO);
            personDTO.setRelationshipStatus(RelationStatusEnum.SINGLE);
            personDTO.setAge(AttributeAssigner.assignAge());
            personDTO.setBirthYear(AttributeAssigner.assignBirthYear(personDTO.getAge()));
            AttributeAssigner.assignExpectedYearOfDeath(personDTO);
            fillSingleCulture(personDTO);
            fillBasicAttribute(personDTO);
            fillCareerAndEducation(personDTO, popSize);
            fillHistory(personDTO, null);
            GroupAdder.addToGroups(personDTO);
            insertList.add(personDTO.convertToPerson());
        }
        personMapper.insertList(insertList);
        insertList.clear();
    }

    private void createCouples(int numMarriedCouples, int numDatingCouples) {
        // Create the couples (people in a relationship) in the population.

        int i;

        // --------------------------------------------------
        // MARRIED.
        // --------------------------------------------------
        for (i = 0; i < numMarriedCouples; i++) {
            generateCouple(RelationStatusEnum.MARRIED);
        } // end for i (creating married couples of people)

        // --------------------------------------------------
        // DATING.
        // --------------------------------------------------
        for (i = 0; i < numDatingCouples; i++) {
            generateCouple(RelationStatusEnum.DATING);
        } // end for i (creating dating couples of people)

    } // end createCouples()

    private void generateCouple(RelationStatusEnum relationStatusEnum) {
        PersonDTO personA, personB;
        int personAAge = 0, personBAge = 0;
        int yearStartedRelationship = 0;
        HometownHistoryDTO coupleHometownCheckpoints = new HometownHistoryDTO();

        if (relationStatusEnum.equals(RelationStatusEnum.MARRIED)) {

            personAAge = Distribution.uniform(Configuration.MinMarriedAge, Configuration.MaxMarriedAge);
            personBAge = personAAge + Distribution.uniform(-5, 5);                    // personB's age is within 5 years of personA's age.
            yearStartedRelationship = AttributeAssigner.AssignCoupleRelationshipStart(personAAge, personBAge, RelationStatusEnum.MARRIED);
            coupleHometownCheckpoints = AttributeAssigner.AssignCoupleHometownHistories(RelationStatusEnum.MARRIED, yearStartedRelationship);

        } else if (relationStatusEnum.equals(RelationStatusEnum.DATING)) {

            personAAge = Distribution.uniform(Configuration.MinDatingAge, Configuration.MaxDatingAge);
            personBAge = personAAge + Distribution.uniform(-5, 5);                    // personB's age is within 5 years of personA's age.
            yearStartedRelationship = AttributeAssigner.AssignCoupleRelationshipStart(personAAge, personBAge, RelationStatusEnum.DATING);
            coupleHometownCheckpoints = AttributeAssigner.AssignCoupleHometownHistories(RelationStatusEnum.DATING, yearStartedRelationship);
        }
        // MALE.
        personA = new PersonDTO();
        personA.setSex(GenderEnum.MALE);
        personA.setSocietyId(Configuration.Society_Id);
        personA.setRelationshipStatus(relationStatusEnum); // The relationship status must be set before some other attributes are assigned (career/education for example!).
        personA.setAge(personAAge);
        personA.setBirthYear(AttributeAssigner.assignBirthYear(personAAge));
        AttributeAssigner.assignExpectedYearOfDeath(personA);

        //System.out.println("A | " + personA.getAge() + " : " + personA.getExpectedDeathYear());
        personA.setRelationshipStartYear(yearStartedRelationship);
        fillSingleCulture(personA);
        fillBasicAttribute(personA);
        fillCareerAndEducation(personA, Configuration.N_Population_Size);
        fillHistory(personA, coupleHometownCheckpoints);
        GroupAdder.addToGroups(personA);


        // FEMALE.
        personB = new PersonDTO();
        personB.setSex(GenderEnum.FEMALE);
        personB.setSocietyId(Configuration.Society_Id);
        personB.setRelationshipStatus(relationStatusEnum);
        personB.setAge(personBAge);
        personB.setBirthYear(AttributeAssigner.assignBirthYear(personBAge));
        AttributeAssigner.assignExpectedYearOfDeath(personB);
        //System.out.println("B | " + personB.getAge() + " : " + personB.getExpectedDeathYear());
        personB.setRelationshipStartYear(yearStartedRelationship);
        fillBasicAttribute(personB);
        fillCoupleCulture(personA, personB);
        fillCareerAndEducation(personB, Configuration.N_Population_Size);
        fillHistory(personB, coupleHometownCheckpoints);
        GroupAdder.addToGroups(personB);


        // Create relationship between the couple.
        GroupAdder.createRelationship(personA, personB, relationStatusEnum);


        if (relationStatusEnum.equals(RelationStatusEnum.MARRIED)) {
            //TMP_COUPLES++;
            //System.err.println("MARRIED COUPLES | " + person.getID() + " & " + partner.getID());

            // Create children. In CreateChildren(), it checks for the greater ID to ensure children are only created once.

            int numOfChild = RelationshipCalculator.DetermineNumberOfChildren(personA, personB);
            List<PersonDTO> children = new ArrayList<PersonDTO>();
            for (int i = 0; i < numOfChild; i++) {
                PersonDTO child = createChild(personA, personB, true);

                children.add(child);
            }
            createChildrenConnections(personA, personB, children);
            List<PersonWithBLOBs> childrenInsertList = new ArrayList<PersonWithBLOBs>();
            for (PersonDTO child : children) {
                childrenInsertList.add(child.convertToPerson());
            }
            personMapper.insertList(childrenInsertList);
            children.clear();
            childrenInsertList.clear();

            // Re-evaluate relationship strength AFTER the parents have children.
            RelationshipCalculator.CalculateAndSetRelationshipStrength(personA, personB, 1);
        }

        personMapper.insert(personA.convertToPerson());
        personMapper.insert(personB.convertToPerson());
    }

    private PersonDTO createChild(PersonDTO parentA, PersonDTO parentB, boolean isBackFilling) {
        PersonDTO child = new PersonDTO();
        fillBasicAttribute(child);
        child.setRelationshipStatus(RelationStatusEnum.SINGLE);
        assignChildAttributes(child, parentA, parentB, isBackFilling);

        return child;
    }

    private void assignChildAttributes(PersonDTO child, PersonDTO parentA, PersonDTO parentB, boolean isBackFilling) {

        // NOTE: the age is now assigned separately because back-filled children are given ages differently than live newborns!
        AttributeAssigner.assignChildAge(parentA, parentB, child, isBackFilling);
        AttributeAssigner.assignChildRace(parentA, parentB, child);
        AttributeAssigner.assignChildReligion(parentA, parentB, child);

        HometownHistoryDTO hometownHistoryDTO = AttributeAssigner.assignChildHometowns(child, parentA, parentB);
        AttributeAssigner.assignHometownHistory_CP(child, hometownHistoryDTO);
        fillCareerAndEducation(child, Configuration.N_Population_Size);
        AttributeAssigner.assignSchoolHistory(child);
        AttributeAssigner.assignWorkHistory(child);


    } // end AssignChildAttributes()

    public void createChildrenConnections(PersonDTO parentA, PersonDTO parentB, List<PersonDTO> children) {
        // Add family connections for parents and children, including parent, children, and siblings connections.
        // param parentA: the first parent in the family
        // param parentB: the second parent in the family
        // param children: an array of the children in the family


        if (parentA.getFamilyId() == parentB.getFamilyId()) {
            // Check if parents belong to the same family. This should always be the case, unless the simulation includes family separations and re-marriages.

            // Loop through all children from the given couple.
            for (PersonDTO child : children) {

                // Add both parents to child's parent_ids list.
                child.getParentIds().add(parentA.getId());
                child.getParentIds().add(parentB.getId());

                // Add child to each parent's children_ids list.
                parentA.getChildrenIds().add(child.getId());
                parentB.getChildrenIds().add(child.getId());

                for (PersonDTO sibling : children) {
                    if (!sibling.getId().equals(child.getId())) {
                        child.getSiblingsIds().add(sibling.getId());
                    }
                }

            }
        } // end if (two parents are in the same family)

    } // end createRelationship()


    private void fillBasicAttribute(PersonDTO person) {
        if (StringUtils.isEmpty(person.getId())) {
            person.setId(sequenceService.generateIdByEnum(SequenceEnum.PERSON_ID_SEQUENCE));
        }


        AttributeAssigner.assignPersonality(person);

        // Extra Traits/Attributes.
        AttributeAssigner.assignIntelligence(person);
        AttributeAssigner.assignAthleticism(person);

        // Culture.
        AttributeAssigner.assignNationality(person);

        // Interests.
        AttributeAssigner.assignInterest(person);
        AttributeAssigner.assignInterestWeight(person);

    }

    private void fillSingleCulture(PersonDTO person) {
        AttributeAssigner.assignRace(person);
        AttributeAssigner.assignReligion(person);
    }

    private void fillCoupleCulture(PersonDTO person, PersonDTO newSpouse) {
        AttributeAssigner.assignSpouseRace(person, newSpouse);
        AttributeAssigner.assignSpouseReligion(person, newSpouse);
    }

    private void fillCareerAndEducation(PersonDTO person, int popSize) {
        AttributeAssigner.assignCareer(person, popSize);
        AttributeAssigner.assignEducation(person);
    }

    private void fillHistory(PersonDTO person, HometownHistoryDTO hometownHistoryDTO) {
        AttributeAssigner.assignHometownHistory_CP(person, hometownHistoryDTO);
        AttributeAssigner.assignSchoolHistory(person);
        AttributeAssigner.assignWorkHistory(person);
    }

    public List<String> getAllIds(String societyId) {
        return personMapper.selectIdsBySocietyId(societyId);
    }

    public PersonDTO selectPerosonDTOById(String id) {

        PersonWithBLOBs personDO = personMapper.selectById(id);
        return new PersonDTO(personDO);
    }

    public void checkDeath() {
        List<String> alivePersonIdList = personMapper.selectAliveIdsBySocietyId(Configuration.Society_Id);
        for (String alivePersonId : alivePersonIdList) {

            PersonWithBLOBs personDO = personMapper.selectById(alivePersonId);
            PersonDTO person = new PersonDTO(personDO);
            if (person.getExpDeathYear() <= Configuration.SocietyYear) {
                removePerson(person);
            }
        }

    }

    public void removePerson(PersonDTO person) {
        person.setIsAlive(false);
        personMapper.updateById(person.convertToPerson());

        // =============================
        // SPOUSE / PARTNER
        // =============================
        // If in relationship, update partner.
        if (person.getRelationshipStatus().equals(RelationStatusEnum.MARRIED) || person.getRelationshipStatus().equals(RelationStatusEnum.DATING)) {
            PersonDTO connPerson = new PersonDTO(personMapper.selectById(person.getPartnerId()));

            // Update partner's partner_id, and change the partner's status to be widowed.
            connPerson.setPartnerId("");
            connPerson.setRelationshipStatus(RelationStatusEnum.WIDOWED);
            personMapper.updateById(connPerson.convertToPerson());
        } // end if (check if couple is dating or married)

        // =============================
        // PARENTS.
        // =============================
        for (String parentId : person.getParentIds()) {
            PersonDTO parent = new PersonDTO(personMapper.selectById(parentId));
            parent.getChildrenIds().remove(person.getId());
            personMapper.updateById(parent.convertToPerson());
        } // end for i (loop through person's parents)

        // =============================
        // CHILDREN.
        // =============================
        for (String childId : person.getChildrenIds()) {
            PersonDTO child = new PersonDTO(personMapper.selectById(childId));
            child.getParentIds().remove(person.getId());
            personMapper.updateById(child.convertToPerson());
//                    connPerson.getParentIDs().remove(new Integer(personID)); // The children would store this person in their parents array.
        } // end for i (loop through person's children)

        // =============================
        // SIBLINGS.
        // =============================
        for (String siblingId : person.getSiblingsIds()) {
            PersonDTO sibling = new PersonDTO(personMapper.selectById(siblingId));
            sibling.getSiblingsIds().remove(person.getId());
            personMapper.updateById(sibling.convertToPerson());
//                    connPerson.getSiblingIDs().remove(new Integer(personID));
        } // end for i (loop through person's siblings)

        // =============================
        // FRIENDS.
        // =============================

        Friendship friendshipQuery = new Friendship();
        friendshipQuery.setSocietyId(person.getSocietyId());
        friendshipQuery.setPersonAId(person.getId());
        List<Friendship> deleteListOne = friendshipMapper.selectByUserOneId(friendshipQuery);
        friendshipQuery.setPersonBId(person.getId());
        List<Friendship> deleteListTwo = friendshipMapper.selectByUserTwoId(friendshipQuery);
        friendshipMapper.deleteList(deleteListOne);
        friendshipMapper.deleteList(deleteListTwo);

        // =============================
        // POTENTIAL FRIENDS.
        // * IMPORTANT NOTE * This is different than all the above removals of references to the deceased person.
        // For all the others, the connections are reflexive, but for potential friends, it is NOT reflexive so the entire
        // society is being looped through and anyone who has the deceased as a potential friend will remove them.
        // =============================
//            int j;
//            int numTotalPersons = getSociety().size();
//            for (i = 0; i < numTotalPersons; i++) { // Loop through everyone because potential friends are not reflexive!
//                connPerson = getPersonByIndex(i);
//
//                int numOccurrences = ArrayTools.countOccurrencesInArray(connPerson.getFriendProbIDs(), personID);
//                if (numOccurrences > 1) {
//                    System.err.println("ArtificialSociety->removePersonByID(); multiple occurences of this person as a potential friend. Add loop to remove ALL matches!");
//                }
//
//                // Since friendProbIDs, friendProbabilities, and friendProbRoles are parallel arrays, we must find the index of this
//                // person in the connPerson's friendProbIDs list, and then remove from all arrays the element at that index.
//                int friendProbIndex = ArrayTools.getElementIndex(connPerson.getFriendProbIDs(), personID); // Get index in array.
//
//                if (numOccurrences == 1) {
//                    connPerson.getFriendProbIDs().remove(friendProbIndex);
//                    connPerson.getFriendProbabilities().remove(friendProbIndex);
//                    connPerson.getFriendProbRoles().remove(friendProbIndex);
//                } // end if (check if a match was found)
//
//            } // end for i (loop through person's friends)

        // =============================
        // GROUPS.
        // =============================

        for (Long groupId : person.getGroupIds().keySet()) {
            GroupDTO group = groupService.getGroupDTOById(groupId);
            group.getMemberList().remove(person.getId());
            groupService.updateGroupById(group);
        } // end for i (loop through person's groups)
    }

    public void evaluateRelationship_Single(PersonDTO person) {
        RelationshipCalculator.SearchForPartner(person);
    }

    public void evaluateRelationship_Couple(PersonDTO person, PersonDTO partner, RelationStatusEnum relationshipType) {
        // Examine the couple from time t-1, and determine the relationship strength at time t.
        // This also allows couples to have children at different times.
        // In future work, this could indicate if the couple separates, but for now, we just calculate strength but disallow separation.

        //int TMP_D_COUPLES = 0;
        //int TMP_M_COUPLES = 0;


        // Ensure a couple's evaluation is only done once, by only using the parent with the greater ID.
        if (person.getId().compareTo(partner.getId()) > 0) { // Only create children at the person with the greater ID. This way children won't be created twice for the same couple.

            //System.out.println("Before:\t" + person.getRelationshipStrength() + " | " + partner.getRelationshipStrength());

            if (person.getChildrenIds().isEmpty()) {
                // If couple has not had any children.
                RelationshipCalculator.CalculateAndSetRelationshipStrength(person, partner, 0);
            } else {
                // If couple has children.
                RelationshipCalculator.CalculateAndSetRelationshipStrength(person, partner, 1);
            } // end if (check whether or not couple has previously had children)

            // DATING.
            if (relationshipType.equals(RelationStatusEnum.DATING)) {
                // The couple may get married, may break up, or continue dating.
                RelationshipCalculator.DetermineFateOfDatingCouple(person, partner);

                // MARRIED.
            } else if (relationshipType.equals(RelationStatusEnum.MARRIED)) {
                int numOfChild = RelationshipCalculator.DetermineCurrentChildBirth(person, partner);
                List<PersonDTO> children = new ArrayList<PersonDTO>();
                for (int i = 0; i < numOfChild; i++) {
                    PersonDTO child = createChild(person, partner, false);

                    children.add(child);
                }
                createChildrenConnections(person, partner, children);

            } // end if (determine whether couple is dating or married)
            personMapper.updateById(person.convertToPerson());
            personMapper.updateById(partner.convertToPerson());


        } // end if (compare couple's IDs to ensure the evaluation is done only once per couple)


        //System.out.println("Married: " + TMP_M_COUPLES + " || Dating: " + TMP_D_COUPLES);

    } // end evaluateRelationship_Couple()


    public void evaluatePerson_Hometown(PersonDTO person) {


        if (person.getHometownHistoryId() != null) {
            HometownHistoryDTO hometownHistoryDTO = historyService.getHometownHistoryById(person.getHometownHistoryId());
            hometownHistoryDTO.updateSameLastEntry();
            historyService.updateHistoryDTO(hometownHistoryDTO);
            // If person lives locally, then update the societal archive too.
            if (person.getSocHometownHistoryId() != null) {
                HometownHistoryDTO socHometownHistory = historyService.getHometownHistoryById(person.getSocHometownHistoryId());
                if (socHometownHistory.getLastActivity().getLocation().equals(Configuration.SocietyName)) {
                    socHometownHistory.updateSameLastEntry();
                } // end if (check if person lives locally)
                historyService.updateHistoryDTO(socHometownHistory);
            } // end if (check if person's societal hometown history has been initialized)

        } // end if (check if hometown history has been initialized) -- THIS IS TEMPORARY!!!

    } // end evaluatePerson_Hometown()

    public void evaluatePerson_Student(PersonDTO person) {
        // This method will assign a school to the person to attend.

        if (ValidationTools.numberIsWithin(person.getAge(), Configuration.SchoolStartAge, Configuration.SchoolFinishAge)) {
            HometownHistoryDTO hometownHistoryDTO = historyService.getHometownHistoryById(person.getHometownHistoryId());
            String PSLocation = hometownHistoryDTO.getActivityByYear(Configuration.SocietyYear).getLocation();

            // ---------------------------------------
            // ELEMENTARY/SECONDARY SCHOOL.
            // ---------------------------------------
            if (person.getSchoolHistoryId() == null) {

                // Person is just starting school, so create new entry for them.
                AttributeAssigner.FindSchoolForNewStudent(person, PSLocation);

            } else {
                // Person is already in school, so update their archives.

                // Get the person's location in the previous year to determine whether or not they are in the same location now.
                String prevLocation = hometownHistoryDTO.getActivityByYear(Configuration.SocietyYear - 1).getLocation();
                boolean sameHometownAsLastYear;
                if (prevLocation.equals(PSLocation)) {
                    sameHometownAsLastYear = true;
                } else {
                    sameHometownAsLastYear = false;
                } // end if (check if person is in same location this year as last year)

                if (sameHometownAsLastYear) {        // Person is in same hometown as they were in the previous year.
                    SchoolHistoryDTO schoolHistoryDTO = historyService.getSchoolHistoryById(person.getSchoolHistoryId());
                    schoolHistoryDTO.updateSameLastEntry();
                    historyService.updateHistoryDTO(schoolHistoryDTO);
                    if (PSLocation.equals(Configuration.SocietyName)) {
                        SchoolHistoryDTO socSchoolHistoryDTO = historyService.getSchoolHistoryById(person.getSocSchoolHistoryId());
                        socSchoolHistoryDTO.updateSameLastEntry();
                        historyService.updateHistoryDTO(socSchoolHistoryDTO);
                    } // end if (check if new location is in local society)

                } else {                            // Person is in a new hometown this year.
                    AttributeAssigner.FindSchoolForNewStudent(person, PSLocation);
                } // end if (check if hometown this year is the same as the previous year)

            } // end if (check if person has already been assigned into a school)


        } else if (ValidationTools.numberIsWithin(Configuration.SocietyYear, person.getYearStartedPsSchool(), person.getYearFinishedPsSchool())) {
            // ---------------------------------------
            // POST-SECONDARY SCHOOL.
            // ---------------------------------------
            // Do nothing.
            SchoolHistoryDTO schoolHistoryDTO = historyService.getSchoolHistoryById(person.getSchoolHistoryId());
            SchoolHistoryDTO socSchoolHistoryDTO = historyService.getSchoolHistoryById(person.getSocSchoolHistoryId());


            String lastSchoolType = ((SchoolHistoryRecord) schoolHistoryDTO.getLastActivity()).getSchoolType(); // The school entries are of the format [schoolType, schoolName].


            String PSLocation = schoolHistoryDTO.getActivityByYear(Configuration.SocietyYear).getLocation();

            if (lastSchoolType.equals("Elementary")) {    // Person is just starting PS education. (Last entry is for elementary school).

                // Find new post-secondary school for person.
                AttributeAssigner.FindPostSecondarySchoolForNewStudent(person, PSLocation, person.getEducation());

            } else {

                // Get the person's location in the previous year to determine whether or not they are in the same location now.
                String prevLocation = schoolHistoryDTO.getActivityByYear(Configuration.SocietyYear - 1).getLocation();
                boolean sameHometownAsLastYear;
                if (prevLocation.equals(PSLocation)) {
                    sameHometownAsLastYear = true;
                } else {
                    sameHometownAsLastYear = false;
                } // end if (check if person is in same location this year as last year)


                if (sameHometownAsLastYear) {            // Person lives in same location as they did last year.
                    // Person has already begun at post-secondary school, so just update their current year there.
                    schoolHistoryDTO.updateSameLastEntry();
                    historyService.updateHistoryDTO(schoolHistoryDTO);
                    if (PSLocation.equals(Configuration.SocietyName)) {
                        // Update societal school history since person has already been attending there locally.
                        socSchoolHistoryDTO.updateSameLastEntry();
                        historyService.updateHistoryDTO(socSchoolHistoryDTO);
                    } // end if (check if person lives locally, so local history can also be updated)

                } else {                                // Person lives in a new location, different from last year.

                    // Find new post-secondary school for person.
                    AttributeAssigner.FindPostSecondarySchoolForNewStudent(person, PSLocation, person.getEducation());

                } // end if (check if person is in same location as new year or new location)

            } // end if (check if person is just beginning PS school or has already begun)


        } else {
            // ---------------------------------------
            // FINISHED ALL SCHOOL.
            // ---------------------------------------
            // Do nothing.

        } // end if (determine which age phase this person is in)

    } // end evaluatePerson_Student()

    public void evaluatePerson_Unemployed(PersonDTO person) {
        // This method helps an unemployed person to search for an appropriate job. They may not end up finding one or getting
        // the job they find, but this will potentially get them employed.

        //System.out.println("Unemployed person looking for work... Hometown = " + person.getHometownHistory().getLastActivityName());


        //if (person.getID() == 42) { System.err.println("Debugging 42 in " + Configuration.SocietyYear + " | " + person.getCurrentPosition() + " ^ evaluatePerson_Unemployed()"); }

        HometownHistoryDTO hometownHistoryDTO = historyService.getHometownHistoryById(person.getHometownHistoryId());
        boolean jobSuccess, extJobSuccess;

        // DETERMINE WHERE THE PERSON CURRENTLY LIVES.
        if (hometownHistoryDTO.getLastActivity().getLocation().equals(Configuration.SocietyName)) {
            // IF THE PERSON LIVES LOCALLY.

            jobSuccess = AttributeAssigner.SearchForJob(person);


            // If person did not get a job locally, then try to get employment externally.
            if (jobSuccess) {
                // Person found a job in the local society.
                // Do nothing. The job has been added to the work archive from within SearchForJob().
                //System.err.println("AAA Person " + person.getID() + " just got a job!!");
                //if (person.getID() == 36) { System.err.println("AAA Person " + person.getID() + " just got a job!!"); }
            } else {
                // Person could not find job in the local society, so search externally now for a job.
                extJobSuccess = AttributeAssigner.SearchForExternalJob(person);


                // If local person found work externally, then move them to the other society. And their family must move too!
                if (extJobSuccess) {
                    // TODO MOVE OUT OF LONDON! AND FAMILY TOO!
                    String newCity = Distribution.uniformRandomObjectStr(Configuration.OtherCities);
                    //System.err.println("AAA Person " + person.getID() + " just got a job externally!! ... Moving to: " + newCity);
                    //if (person.getID() == 36) { System.err.println("AAA Person " + person.getID() + " just got a job externally!! ... Moving to: " + newCity + " in year " + Configuration.SocietyYear); }
                    moveFamilyToCity(person, newCity, false);
                } // end if (check if external person found work in this simulation society)

            } // end if (check if person is still unemployed)


        } else {
            // IF THE PERSON DOES NOT LIVE LOCALLY.

            // Begin by looking for a job in their current hometown (which is external because they do not live in our society)
            extJobSuccess = AttributeAssigner.SearchForExternalJob(person);

            if (extJobSuccess) {
                // Person found a job in their own hometown (not in this simulation's society)
                // TODO Add External Entry to work archive
                //System.err.println("BBB Person " + person.getID() + " just got a job in their hometown!!");
                //if (person.getID() == 36) { System.err.println("BBB Person " + person.getID() + " just got a job in their hometown!!"); }
            } else {
                // Person could not find job in their hometown, so search now in this society for a job.
                jobSuccess = AttributeAssigner.SearchForJob(person);

                // If external person found work in the local society, then move them to this society.
                if (jobSuccess) {
                    // TODO MOVE TO LONDON! AND FAMILY TOO!
                    //System.err.println("BBB Person " + person.getID() + " just got a job in London!!");
                    //if (person.getID() == 36) { System.err.println("BBB Person " + person.getID() + " just got a job in London!!"); }
                    moveFamilyToCity(person, Configuration.SocietyName, true);

                } // end if (check if external person found work in this simulation society)

            } // end if (check if external person could not find employment in their current hometown)


        } // end if (check if person lives in local society)

    } // end evaluatePerson_Unemployed()

    public void evaluatePerson_Working(PersonDTO person) {
        // Evaluate person's workplace placement. Use a randomizer to see if the person loses their job this year.
        // The randomization will be based onthe number of years the person has worked at the current place. The longer they have
        // worked there, the less likely they are to lose that job.
        // If person loses their job, they are considered UNEMPLOYED again. Otherwise, they keep the same job they already have.

        //if (person.getID() == 42) { System.err.println("Debugging 42 in " + Configuration.SocietyYear + " | " + person.getCurrentPosition() + " ^ evaluatePerson_Working()"); }


        WorkHistoryDTO workHistoryDTO = historyService.getWorkHistoryById(person.getWorkHistoryId());
        WorkHistoryRecord lastRecord = (WorkHistoryRecord) workHistoryDTO.getLastActivity();

        //System.out.println("Person #" + person.getID() + " has been working at " + work[0] +
        //		" on the period, [" + currWorkplacePeriod[0] + "," + currWorkplacePeriod[1] + "].");
        int numYearsWorking = lastRecord.getEndYear() - lastRecord.getStartYear();


        //System.out.println("Person worked at current place for " + numYearsWorking + " years.");

        double rndKeepJob = Distribution.uniform(0.0, 1.0);


        // We calculate the probability of keeping their job as p = 0.5^n, where n is the number of years they've worked there.
        double p_keepJob;
        if (numYearsWorking <= 1) {
            // If person has worked at workplace for 0 years, let them stay on. Give them a chance!
            p_keepJob = 1.0;
        } else {
            // Calculate probability of keeping job as 0.5^n.
            p_keepJob = Math.pow(0.5, numYearsWorking);
        } // end if (check if number of years at workplace is 0)

        if (rndKeepJob > p_keepJob) {
            // Lose job.

            //System.err.println("Working person chilling (SimStepper)... major layoffs");
            person.setCurrentPosition(PositionEnum.UNEMPLOYED);
            person.setIncome(0);


            // This seems incorrect, but it is necessary.
            // We update the last work history because we assume the person will work into the new year. The UNEMPLOYED status
            // will stop this from continually updating for more years.
            workHistoryDTO.updateSameLastEntry();

            //System.err.println("Person " + person.getID() + " just lost their job in year " + Configuration.SocietyYear);


            //person.getWorkHistory().endActivityInCurrentYear();
            //System.out.println("person.getCurrentPosition() = " + person.getCurrentPosition());
            //System.err.println("Person " + person.getID() + " just LOST their job.");


        } else {
            // Person still has same career at same workplace, so just update the work archive to include the new year!
            workHistoryDTO.updateSameLastEntry();


        } // end if (check if person keeps their job)

        historyService.updateHistoryDTO(workHistoryDTO);

    } // end evaluatePerson_Working()


    public void moveFamilyToCity(PersonDTO person, String location, boolean isInSociety) {

        // MOVE THIS PERSON.
        moveToCity(person, location, isInSociety);
        //this.getHometownHistory().addEntry(location, Configuration.SocietyYear);
        //if (isInSociety) {
        //	this.getSocietalHometownHistory().addEntry(location, Configuration.SocietyYear);
        //} // end if (check if person is moving to local society)

        // MOVE SPOUSE (only in marriage, not dating relationship).
        if (person.getRelationshipStatus().equals(RelationStatusEnum.MARRIED)) {
            PersonDTO spouse = new PersonDTO(personMapper.selectById(person.getPartnerId()));
            moveToCity(spouse, location, isInSociety);
            //spouse.getHometownHistory().addEntry(location, Configuration.SocietyYear);
            //if (isInSociety) {
            //	spouse.getSocietalHometownHistory().addEntry(location, Configuration.SocietyYear);
            //} // end if (check if person is moving to local society)
        } // end if (check if person is married)

        // MOVE CHILDREN.
        List<String> childrenIds = person.getChildrenIds();
        int numChildren = childrenIds.size();


        if (numChildren > 0) {
            for (String childId : childrenIds) {
                PersonDTO child = new PersonDTO(personMapper.selectById(childId));
                // Only move children who are young (elementary/secondary school age or younger)
                if (child.getAge() < Configuration.SchoolFinishAge) {
                    moveToCity(child, location, isInSociety);
                } // check if child is young enough that they would move with the parents)
            } // end for i (loop through all children)

        } // end if (check if person has children)


    } // end moveFamilyToCity

    private void moveToCity(PersonDTO person, String location, boolean isInSociety) {
        // This method will move the given person to the new city given as the location parameter.
        // Note that this method is called from moveFamilyToCity(), so one Person will invoke that method which will then
        // call this sub-routine for that person and their family members individually.
        //
        // param location: the name of the city to which the person is moving
        // param isInSociety: a boolean flag indicating whether or not the person is moving into the simulation society
        HometownHistoryDTO hometownHistoryDTO = historyService.getHometownHistoryById(person.getHometownHistoryId());
        hometownHistoryDTO.getRecordList().add(new HistoryRecord(location, Configuration.SocietyYear, Configuration.SocietyYear));
        historyService.updateHistoryDTO(hometownHistoryDTO);
        if (isInSociety) {
            HometownHistoryDTO socHometownHistoryDTO = historyService.getHometownHistoryById(person.getSocHometownHistoryId());
            socHometownHistoryDTO.getRecordList().add(new HistoryRecord(location, Configuration.SocietyYear, Configuration.SocietyYear));
            historyService.updateHistoryDTO(socHometownHistoryDTO);
        } // end if (check if person is moving to local society)

    } // end moveToCity()

    public void evaluatePerson_Groups (PersonDTO person){
        GroupAdder.UpdatePersonInAllGroups(person);
    }

    public void updatePersonDTOById(PersonDTO person){
        personMapper.updateById(person.convertToPerson());
    }

    public void setPersonMapper(PersonMapper personMapper) {
        this.personMapper = personMapper;
    }

    public void setSequenceService(SequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    public void setCareerService(CareerService careerService) {
        this.careerService = careerService;
    }

    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    public void setFriendshipMapper(FriendshipMapper friendshipMapper) {
        this.friendshipMapper = friendshipMapper;
    }

    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }
}
