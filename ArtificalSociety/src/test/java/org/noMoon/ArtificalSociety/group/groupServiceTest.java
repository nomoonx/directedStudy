package org.noMoon.ArtificalSociety.group;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noMoon.ArtificalSociety.group.DTO.GroupDTO;
import org.noMoon.ArtificalSociety.group.Services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noMoon on 2015-10-11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-service.xml"})
@TransactionConfiguration(defaultRollback = false)
@Transactional
public class groupServiceTest {

    @Autowired
    GroupService groupService;

    @Test
    public void testInsert(){
        GroupDTO groupDTO=new GroupDTO();
        groupDTO.setSocietyId("testSocietyId");
        groupDTO.setGroupLabel("hoho");
        List<String> ids=new ArrayList<String>();
        ids.add("id1");
        ids.add("id2");
        ids.add("id3");
        groupDTO.setMemberList(ids);
        int id=groupService.insertNewGroup(groupDTO);
        System.out.println(groupDTO.getId());

    }
}
