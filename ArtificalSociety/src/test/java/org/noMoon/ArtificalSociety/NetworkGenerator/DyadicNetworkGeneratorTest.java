package org.noMoon.ArtificalSociety.NetworkGenerator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noMoon.ArtificalSociety.commons.utils.Configuration;
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
public class DyadicNetworkGeneratorTest {

    @Test
    public void testGenerate(){
        DyadicNetworkGenerator.generateDyadicNetwork();
    }

    @Test
    public void testSimulation(){
        Configuration.LoadConfigValuesFromFile("SocietyConfig.xml");
        Configuration.Society_Id="S201512061000093";
        SimulationStepper.begin(5);
    }
}
