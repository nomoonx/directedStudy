package org.noMoon.ArtificalSociety.NetworkGenerator;

import org.noMoon.ArtificalSociety.career.services.CareerService;
import org.noMoon.ArtificalSociety.commons.utils.Configuration;
import org.noMoon.ArtificalSociety.group.Services.GroupService;
import org.noMoon.ArtificalSociety.institution.services.ClubService;
import org.noMoon.ArtificalSociety.institution.services.InstitutionService;

/**
 * Created by noMoon on 2015-08-21.
 */
//@ContextConfiguration(locations = {"classpath:spring-service.xml"})
public class DyadicNetworkGenerator {

    static InstitutionService institutionService;
    static ClubService clubService;
    static CareerService careerService;
    static GroupService groupService;

    //    @Autowired

    public static void generateDyadicNetwork(){
        LoadAllConfigurations();
    }

    public static void LoadAllConfigurations() {
        Configuration.LoadConfigValuesFromFile("SocietyConfig.xml");
        System.out.println("Society Config loaded");
        institutionService.loadAllInstitutions("Schools.xml", "PostSecondarySchools.xml", "Temples.xml", Configuration.Society_Id);
        System.out.println("institution Config loaded");
        clubService.loadClubsFromFile("Clubs.xml", Configuration.Society_Id);
        System.out.println("club Config loaded");
        careerService.loadCareerAndWorkplace("CareersNew.xml","Workplaces.xml",Configuration.Society_Id);
        System.out.println("career Config loaded");
        groupService.generateGroups(Configuration.Society_Id);
        System.out.println("Group Config loaded");
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

    public void setGroupService(GroupService groupService) {
        DyadicNetworkGenerator.groupService = groupService;
    }

    public void setInstitutionService(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    public void setClubService(ClubService clubService) {
        DyadicNetworkGenerator.clubService = clubService;
    }

    public void setCareerService(CareerService careerService) {
        DyadicNetworkGenerator.careerService = careerService;
    }
}
