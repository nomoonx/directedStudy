package org.noMoon.ArtificalSociety.NetworkGenerator;

import org.noMoon.ArtificalSociety.career.services.CareerService;
import org.noMoon.ArtificalSociety.commons.utils.Configuration;
import org.noMoon.ArtificalSociety.group.Services.GroupService;
import org.noMoon.ArtificalSociety.institution.services.ClubService;
import org.noMoon.ArtificalSociety.institution.services.InstitutionService;
import org.noMoon.ArtificalSociety.person.services.FriendshipService;
import org.noMoon.ArtificalSociety.person.services.PersonService;

/**
 * Created by noMoon on 2015-08-21.
 */
//@ContextConfiguration(locations = {"classpath:spring-service.xml"})
public class DyadicNetworkGenerator {

    static InstitutionService institutionService;
    static ClubService clubService;
    static CareerService careerService;
    static GroupService groupService;
    static PersonService personService;
    static FriendshipService friendshipService;

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
        personService.generatePerson(Configuration.N_Singles,Configuration.N_Couples,Configuration.N_CouplesDating,Configuration.Society_Id);
        System.out.println("seed persons generated");
        friendshipService.CreateEntireFriendshipNetwork();
        System.out.println("friendship created");
        System.out.println("Memory: "+String.valueOf(GetMemoryUsage())+"MB");
        personService.checkDeath();

        SimulationStepper.begin(Configuration.N_YearsToRun);


    } // end LoadAllConfigurations()

    public static long GetMemoryUsage () {

        System.gc();

        int mb = 1024*1024;

        Runtime runtime = Runtime.getRuntime();

        return (runtime.totalMemory() - runtime.freeMemory()) / mb;
    } // end GetMemoryUsage()

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

    public void setPersonService(PersonService personService) {
        DyadicNetworkGenerator.personService = personService;
    }

    public void setFriendshipService(FriendshipService friendshipService) {
        DyadicNetworkGenerator.friendshipService = friendshipService;
    }
}
