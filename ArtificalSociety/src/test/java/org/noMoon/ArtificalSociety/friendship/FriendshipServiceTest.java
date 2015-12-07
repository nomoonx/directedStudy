package org.noMoon.ArtificalSociety.friendship;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noMoon.ArtificalSociety.commons.utils.Configuration;
import org.noMoon.ArtificalSociety.person.DO.Friendship;
import org.noMoon.ArtificalSociety.person.services.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by noMoon on 2015-11-12.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-service.xml"})
@TransactionConfiguration(defaultRollback = false)
@Transactional
public class FriendshipServiceTest {

    @Autowired
    FriendshipService friendshipService;

    @Test
    public void testInsert(){
        Friendship friendship=new Friendship();
        friendship.setSocietyId("testSocietyId");
        friendshipService.insertNewFriendship(friendship);
        System.out.println(friendship.getId());
    }

    @Test
    public void testGenerateFriendship(){
        Configuration.LoadConfigValuesFromFile("SocietyConfig.xml");
        Configuration.Society_Id="S201512061000086";
        friendshipService.CreateEntireFriendshipNetwork();
    }
}
