package org.noMoon.ArtificalSociety.NetworkGenerator;

import org.noMoon.ArtificalSociety.institution.services.InstitutionService;

/**
 * Created by noMoon on 2015-08-21.
 */
//@ContextConfiguration(locations = {"classpath:spring-service.xml"})
public class DyadicNetworkGenerator {

    static InstitutionService institutionService;

//    @Autowired

    public static void generateDyadicNetwork(){
        LoadAllConfigurations();
    }

    public static void LoadAllConfigurations() {
        System.out.println(123);
        Configuration.LoadConfigValuesFromFile("SocietyConfig.xml");
        System.out.println(321);
        institutionService.loadAllInstitutions("Schools.xml","PostSecondarySchools.xml","Temples.xml");
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



    public void setInstitutionService(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }
}
