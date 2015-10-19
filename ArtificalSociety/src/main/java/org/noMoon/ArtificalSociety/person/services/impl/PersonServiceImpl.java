package org.noMoon.ArtificalSociety.person.services.impl;

import org.noMoon.ArtificalSociety.career.services.CareerService;
import org.noMoon.ArtificalSociety.commons.Enums.SequenceEnum;
import org.noMoon.ArtificalSociety.commons.services.SequenceService;
import org.noMoon.ArtificalSociety.person.DAO.PersonMapper;
import org.noMoon.ArtificalSociety.person.DTO.PersonDTO;
import org.noMoon.ArtificalSociety.person.Enums.GenderEnum;
import org.noMoon.ArtificalSociety.person.Enums.RelationStatusEnum;
import org.noMoon.ArtificalSociety.person.services.PersonService;
import org.noMoon.ArtificalSociety.person.utils.AttributeAssigner;
import org.springframework.util.StringUtils;

import java.util.Random;

/**
 * Created by noMoon on 2015-10-17.
 */
public class PersonServiceImpl implements PersonService {

    private PersonMapper personMapper;
    private SequenceService sequenceService;
    private CareerService careerService;

    public void generatePerson(int singleNumber, int coupleNumber, int datingNumber, String societyId) {
        initializeAttributeAssigner(societyId);
        generateSinglePerson(singleNumber,societyId,2*coupleNumber+2*datingNumber+singleNumber);
        releaseAttributeAssigner();
    }

    private void initializeAttributeAssigner(String societyId){
        AttributeAssigner.setCareerList(careerService.listCareerWithSocietyId(societyId));
    }

    private void releaseAttributeAssigner(){
        AttributeAssigner.getCareerList().clear();
    }

    private void generateSinglePerson(int number,String societyId,int popSize) {
        Random rnd=new Random();
        for(int i=0;i<number;i++){
            PersonDTO personDTO=new PersonDTO();
            personDTO.setSocietyId(societyId);
            if(rnd.nextInt()%2==0){
                personDTO.setSex(GenderEnum.MALE);
            }else{
                personDTO.setSex(GenderEnum.FEMALE);
            }
            personDTO.setRelationshipStatus(RelationStatusEnum.SINGLE);
            fillBasicAttribute(personDTO);
            fillCareerAndEducation(personDTO,popSize);
            personMapper.insert(personDTO.convertToPerson());
        }
    }
/*
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

    private void generateCouple(RelationStatusEnum relationStatusEnum){
        PersonDTO personA, personB;
        int personAAge, personBAge;
        int yearStartedRelationship;
        ActivityArchive[] coupleHometownCheckpoints;

        if (relationStatusEnum.equals(RelationStatusEnum.MARRIED)) {

            personAAge = Distribution.uniform(Configuration.MinMarriedAge, Configuration.MaxMarriedAge);
            personBAge = personAAge + Distribution.uniform(-5, 5);					// personB's age is within 5 years of personA's age.
            yearStartedRelationship = AssignCoupleRelationshipStart(personAAge, personBAge, relType);
            coupleHometownCheckpoints = AssignCoupleHometownHistories(relType, yearStartedRelationship);

        } else if (relationStatusEnum.equals(RelationStatusEnum.DATING)) {

            personAAge = Distribution.uniform(Configuration.MinDatingAge, Configuration.MaxDatingAge);
            personBAge = personAAge + Distribution.uniform(-5, 5);					// personB's age is within 5 years of personA's age.
            yearStartedRelationship = AssignCoupleRelationshipStart(personAAge, personBAge, relType);
            coupleHometownCheckpoints = AssignCoupleHometownHistories(relType, yearStartedRelationship);

    }
    */

    private void fillBasicAttribute(PersonDTO person){
        if(StringUtils.isEmpty(person.getId())){person.setId(sequenceService.generateIdByEnum(SequenceEnum.PERSON_ID_SEQUENCE));}

        person.setAge(AttributeAssigner.assignAge());
        person.setBirthYear(AttributeAssigner.assignBirthYear(person.getAge()));
        AttributeAssigner.assignExpectedYearOfDeath(person);
        AttributeAssigner.assignPersonality(person);

        // Extra Traits/Attributes.
        AttributeAssigner.assignIntelligence(person);
        AttributeAssigner.assignAthleticism(person);

        // Culture.
        AttributeAssigner.assignRace(person);
        AttributeAssigner.assignReligion(person);
        AttributeAssigner.assignNationality(person);

        // Interests.
        AttributeAssigner.assignInterest(person);
        AttributeAssigner.assignInterestWeight(person);

    }

    private void fillCareerAndEducation(PersonDTO person, int popSize){
        AttributeAssigner.assignCareer(person,popSize);
        AttributeAssigner.assignEducation(person);
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
}
