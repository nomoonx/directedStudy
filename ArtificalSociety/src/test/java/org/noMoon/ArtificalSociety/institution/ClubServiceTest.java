package org.noMoon.ArtificalSociety.institution;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noMoon.ArtificalSociety.institution.DTO.ClubDTO;
import org.noMoon.ArtificalSociety.institution.services.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

/**
 * Created by noMoon on 2015-09-08.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-service.xml"})
@TransactionConfiguration(defaultRollback = false)
@Transactional
public class ClubServiceTest {

    @Autowired
    ClubService clubService;

    @Test
    public void testInsert(){
        ClubDTO clubDTO=new ClubDTO();
        clubDTO.setCity("city");
        clubDTO.setPopulation(1233321);
        HashMap<String,String> map=new HashMap<String, String>();
        map.put("key1","value1");
        map.put("key2","value2");
        clubDTO.setTrait(map);
        clubService.insertClubDTO(clubDTO);
    }

}
