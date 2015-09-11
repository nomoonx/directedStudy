package org.noMoon.ArtificalSociety.career;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noMoon.ArtificalSociety.career.DTO.CareerDTO;
import org.noMoon.ArtificalSociety.career.services.CareerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by noMoon on 2015-09-11.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-service.xml"})
@TransactionConfiguration(defaultRollback = false)
@Transactional
public class CareerServiceTest {

    @Autowired
    CareerService careerService;

    @Test
    public void testInsert(){
        CareerDTO careerDTO=new CareerDTO();
        careerDTO.setTitle("testTitle");
        careerDTO.setCareerId("testCareerId");
        careerService.insertNewCareer(careerDTO);
        System.out.println(careerDTO.getId());
    }

}
