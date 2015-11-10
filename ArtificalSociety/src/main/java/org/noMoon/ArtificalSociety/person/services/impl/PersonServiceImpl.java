package org.noMoon.ArtificalSociety.person.services.impl;

import org.noMoon.ArtificalSociety.career.services.CareerService;
import org.noMoon.ArtificalSociety.commons.Enums.SequenceEnum;
import org.noMoon.ArtificalSociety.commons.services.SequenceService;
import org.noMoon.ArtificalSociety.commons.utils.Configuration;
import org.noMoon.ArtificalSociety.commons.utils.Distribution;
import org.noMoon.ArtificalSociety.group.Services.GroupService;
import org.noMoon.ArtificalSociety.history.DTO.HometownHistoryDTO;
import org.noMoon.ArtificalSociety.person.DAO.PersonMapper;
import org.noMoon.ArtificalSociety.person.DTO.PersonDTO;
import org.noMoon.ArtificalSociety.person.Enums.GenderEnum;
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
            personMapper.insert(personDTO.convertToPerson());
        }
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
            createChildrenConnections(personA,personB,children);
            for(PersonDTO child:children){
                personMapper.insert(child.convertToPerson());
            }

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

                for(PersonDTO sibling:children){
                    if(!sibling.getId().equals(child.getId())){
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
}
