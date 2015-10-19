package org.noMoon.ArtificalSociety.person;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noMoon.ArtificalSociety.commons.utils.Configuration;
import org.noMoon.ArtificalSociety.person.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

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

    @Test
    public void testInsertSinglePerson(){
        Configuration.LoadConfigValuesFromFile("SocietyConfig.xml");
        Configuration.Society_Id="S201509141000018";
        personService.generatePerson(1,0,0,Configuration.Society_Id);
    }
}
