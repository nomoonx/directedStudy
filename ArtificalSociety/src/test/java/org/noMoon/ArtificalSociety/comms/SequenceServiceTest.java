package org.noMoon.ArtificalSociety.comms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noMoon.ArtificalSociety.commons.Enums.SequenceEnum;
import org.noMoon.ArtificalSociety.commons.services.SequenceService;
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
public class SequenceServiceTest {

    @Autowired
    private SequenceService sequenceService;

    @Test
    public void testSelectId(){
        System.out.println(sequenceService.generateIdByEnum(SequenceEnum.SOCIETY_ID_SEQUENCE));
    }

    @Test
    public void testPersonId(){
        System.out.println(sequenceService.generateIdByEnum(SequenceEnum.PERSON_ID_SEQUENCE));
    }

    @Test
    public void testFamilyId(){
        System.out.println(sequenceService.generateIdByEnum(SequenceEnum.FAMILY_ID_SEQUENCE));
    }
}
