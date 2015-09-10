package org.noMoon.ArtificalSociety.institution;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.noMoon.ArtificalSociety.NetworkGenerator.Configuration;
import org.noMoon.ArtificalSociety.institution.DTO.ClubDTO;
import org.noMoon.ArtificalSociety.institution.services.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        HashMap<String,List<String>> map=new HashMap<String, List<String>>();
        List<String> list1=new ArrayList<String>();
        list1.add("value1");
        list1.add("value2");
        List<String> list2=new ArrayList<String>();
        list2.add("value3");
        map.put("key1", list1);
        map.put("key2",list2);
        clubDTO.setTrait(map);
        clubService.insertClubDTO(clubDTO);
        Assert.assertNotNull(clubDTO.getId());
    }

    @Test
    public void testSelect(){
        ClubDTO clubDTO=new ClubDTO();
        clubDTO.setCity("city");
        clubDTO.setPopulation(1233321);
        HashMap<String,List<String>> map=new HashMap<String, List<String>>();
        List<String> list1=new ArrayList<String>();
        list1.add("value1");
        list1.add("value2");
        List<String> list2=new ArrayList<String>();
        list2.add("value3");
        map.put("key1", list1);
        map.put("key2", list2);
        clubDTO.setTrait(map);
        clubService.insertClubDTO(clubDTO);
        ClubDTO resultDTO=clubService.selectClubDTOByPk(clubDTO.getId());
        map=resultDTO.getTrait();
        System.out.println(map.size());
        for(List<String> list:map.values()){
            for(String val:list){
                System.out.println(val);
            }
        }
    }

    @Test
    public void testLoadFile(){
        String filepath="Clubs.xml";
        Configuration.Society_Id="SocietyIdForTest";
        clubService.loadClubsFromFile(filepath);
    }

}
