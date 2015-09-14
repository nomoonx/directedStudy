package org.noMoon.ArtificalSociety.institution;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noMoon.ArtificalSociety.institution.DO.Institution;
import org.noMoon.ArtificalSociety.institution.services.InstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by noMoon on 2015-09-02.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-service.xml"})
@TransactionConfiguration(defaultRollback = false)
@Transactional
public class InstitutionServiceTest {

    @Autowired
    private InstitutionService institutionService;

    @Test
    public void testInsert(){
        Institution inst=new Institution();
        inst.setCity("test");
        inst.setSocietyId("testSocietyId");
        inst.setTitle("testTitle");
        institutionService.insertNewInstitution(inst);
        System.out.println(inst.getId());
    }

    @Test
    public void testSelectByTitle(){
        List<Institution> insts=institutionService.selectInstitutionByTitle("testSocietyId","testTitle");
        System.out.println(insts.size());
    }
}
