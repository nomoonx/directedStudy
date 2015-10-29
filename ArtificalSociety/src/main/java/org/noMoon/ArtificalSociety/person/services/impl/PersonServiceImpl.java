package org.noMoon.ArtificalSociety.person.services.impl;

import org.noMoon.ArtificalSociety.career.services.CareerService;
import org.noMoon.ArtificalSociety.commons.Enums.SequenceEnum;
import org.noMoon.ArtificalSociety.commons.services.SequenceService;
import org.noMoon.ArtificalSociety.commons.utils.Configuration;
import org.noMoon.ArtificalSociety.commons.utils.Distribution;
import org.noMoon.ArtificalSociety.history.DTO.HometownHistoryDTO;
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
        createCouples(coupleNumber,datingNumber);

        releaseAttributeAssigner();
    }

    private void initializeAttributeAssigner(String societyId){
        AttributeAssigner.initialize(societyId);
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
            personDTO.setAge(AttributeAssigner.assignAge());
            personDTO.setBirthYear(AttributeAssigner.assignBirthYear(personDTO.getAge()));
            AttributeAssigner.assignExpectedYearOfDeath(personDTO);
            fillSingleCulture(personDTO);
            fillBasicAttribute(personDTO);
            fillCareerAndEducation(personDTO,popSize);
            fillHistory(personDTO,null);
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

    private void generateCouple(RelationStatusEnum relationStatusEnum){
        PersonDTO personA, personB;
        int personAAge=0, personBAge=0;
        int yearStartedRelationship=0;
        HometownHistoryDTO coupleHometownCheckpoints=new HometownHistoryDTO();

        if (relationStatusEnum.equals(RelationStatusEnum.MARRIED)) {

            personAAge = Distribution.uniform(Configuration.MinMarriedAge, Configuration.MaxMarriedAge);
            personBAge = personAAge + Distribution.uniform(-5, 5);					// personB's age is within 5 years of personA's age.
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
        fillHistory(personA,coupleHometownCheckpoints);



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
        fillCoupleCulture(personA,personB);
        fillCareerAndEducation(personB, Configuration.N_Population_Size);
        fillHistory(personB,coupleHometownCheckpoints);


        // Create relationship between the couple.
//        PersonGroupAdder.createRelationship(personA, personB, relType);
        personMapper.insert(personA.convertToPerson());
        personMapper.insert(personB.convertToPerson());
    }


    private void fillBasicAttribute(PersonDTO person){
        if(StringUtils.isEmpty(person.getId())){person.setId(sequenceService.generateIdByEnum(SequenceEnum.PERSON_ID_SEQUENCE));}


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

    private void fillSingleCulture(PersonDTO person){
        AttributeAssigner.assignRace(person);
        AttributeAssigner.assignReligion(person);
    }

    private void fillCoupleCulture(PersonDTO person,PersonDTO newSpouse){
        AttributeAssigner.assignSpouseRace(person, newSpouse);
        AttributeAssigner.assignSpouseReligion(person, newSpouse);
    }

    private void fillCareerAndEducation(PersonDTO person, int popSize){
        AttributeAssigner.assignCareer(person,popSize);
        AttributeAssigner.assignEducation(person);
    }

    private void fillHistory(PersonDTO person, HometownHistoryDTO hometownHistoryDTO){
        AttributeAssigner.assignHometownHistory_CP(person,hometownHistoryDTO);
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
}
