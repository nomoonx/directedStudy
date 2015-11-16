package org.noMoon.ArtificalSociety.person;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noMoon.ArtificalSociety.commons.utils.Configuration;
import org.noMoon.ArtificalSociety.person.DAO.PersonMapper;
import org.noMoon.ArtificalSociety.person.DTO.PersonDTO;
import org.noMoon.ArtificalSociety.person.Enums.GenderEnum;
import org.noMoon.ArtificalSociety.person.Enums.PositionEnum;
import org.noMoon.ArtificalSociety.person.Enums.RelationStatusEnum;
import org.noMoon.ArtificalSociety.person.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by noMoon on 2015-10-19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-service.xml"})
@TransactionConfiguration(defaultRollback = false)
@Transactional
public class PersonServiceTest {

    @Autowired
    PersonService personService;

    @Autowired
    PersonMapper personMapper;

    @Test
    public void testInsertSinglePerson(){
        Configuration.LoadConfigValuesFromFile("SocietyConfig.xml");
        Configuration.Society_Id="S201510281000035";
        personService.generatePerson(1,0,0,Configuration.Society_Id);
    }

    @Test
    public void testInsertMarriedPerson(){
        Configuration.LoadConfigValuesFromFile("SocietyConfig.xml");
        Configuration.Society_Id="S201510281000035";
        personService.generatePerson(0,1,0,Configuration.Society_Id);
    }

    @Test
    public void testSelectIdsBySocietyId(){
        List<String> ids=personMapper.selectIdsBySocietyId("S201510281000035");
        System.out.println(ids.size());
    }

    @Test
    public void testSelectPersonDTOById(){
        Configuration.Society_Id="S201510281000035";
        PersonDTO person=personService.selectPerosonDTOById("P20151101013232000000028");
        System.out.println(person.getCareerId());
    }

    @Test
    public void testIsAlive(){
        PersonDTO person=new PersonDTO();
        person.setSex(GenderEnum.FEMALE);
        person.setId("testPersonId");
        person.setSocietyId("testSocietyId");
        person.setRelationshipStatus(RelationStatusEnum.SINGLE);
        person.setCurrentPosition(PositionEnum.STUDENT);
        personMapper.insert(person.convertToPerson());
    }

}
