package org.noMoon.ArtificalSociety.NetworkGenerator;

/**
 * Created by noMoon on 2015-08-21.
 */
//@ContextConfiguration(locations = {"classpath:spring-service.xml"})
public class DyadicNetworkGenerator {

//    @Autowired

    public static void generateDyadicNetwork(){
        LoadAllConfigurations();
    }

    public static void LoadAllConfigurations() {
        Configuration.LoadConfigValuesFromFile("SocietyConfig.xml");
        //Configuration.LoadConfigValuesFromFile("SocietyConfig_Genesis.xml");
        //Configuration.LoadConfigValuesFromFile("SocietyConfig_TEST.xml");
        //Configuration.LoadConfigValuesFromFile("SocietyConfigSmall.xml");

        // Load Configuration parameters.
        //Configuration.SetHardcodedValues();


        // Load all institutions (schools, temples, etc.) - * NOTE * this must be loaded BEFORE the careers/workplaces are loaded!
//        InstitutionSet.loadAllInstitutions();
//        SchoolSet.initSchools();                // Initialize Schools.
//        TempleSet.initTemples();                // Initialize Temples.
//
//        Club.loadFiles();
//
//        // Load Careers.
//        Careers.loadFiles();                            // Load the career files.
//        AttributeAssigner.initializeCareersTables();    // Create the table of career openings.
//
//
//        GroupGenerator.generateGroups();


    } // end LoadAllConfigurations()


}
