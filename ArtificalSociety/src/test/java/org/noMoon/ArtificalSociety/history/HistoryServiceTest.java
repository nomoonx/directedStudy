package org.noMoon.ArtificalSociety.history;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noMoon.ArtificalSociety.history.DTO.SchoolHistoryDTO;
import org.noMoon.ArtificalSociety.history.Records.SchoolHistoryRecord;
import org.noMoon.ArtificalSociety.history.services.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * Created by noMoon on 2015-10-17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-service.xml"})
@TransactionConfiguration(defaultRollback = false)
@Transactional
public class HistoryServiceTest {

    @Autowired
    HistoryService historyService;

    @Test
    public void testInsert(){
        SchoolHistoryDTO historyDTO=new SchoolHistoryDTO();
        historyDTO.setSocietyId("testSocietyId");
        historyDTO.setRecordList(new ArrayList<SchoolHistoryRecord>());
        Long id=historyService.insertNewHistoryDTO(historyDTO);
        System.out.println(id);
    }
}
