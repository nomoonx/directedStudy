package org.noMoon.ArtificalSociety.society;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noMoon.ArtificalSociety.society.DO.Society;
import org.noMoon.ArtificalSociety.society.services.SocietyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by noMoon on 2015-08-21.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-service.xml"})
@TransactionConfiguration(defaultRollback = false)
@Transactional
public class SocietyServiceTest {

    @Autowired
    SocietyService societyService;

    @Test
    public void testInsertSociety(){
        Society society=new Society();
        society.setSocietyName("society");
        society.setSocietyYear(1234);
        System.out.println(societyService.insertNewSociety(society));
    }

}
