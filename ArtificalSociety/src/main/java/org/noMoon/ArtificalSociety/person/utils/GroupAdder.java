package org.noMoon.ArtificalSociety.person.utils;

import org.noMoon.ArtificalSociety.career.DO.Workplace;
import org.noMoon.ArtificalSociety.career.DTO.CareerDTO;
import org.noMoon.ArtificalSociety.career.services.CareerService;
import org.noMoon.ArtificalSociety.commons.Enums.SequenceEnum;
import org.noMoon.ArtificalSociety.commons.services.SequenceService;
import org.noMoon.ArtificalSociety.commons.utils.Configuration;
import org.noMoon.ArtificalSociety.commons.utils.Distribution;
import org.noMoon.ArtificalSociety.commons.utils.ValidationTools;
import org.noMoon.ArtificalSociety.group.DTO.GroupDTO;
import org.noMoon.ArtificalSociety.group.Services.GroupService;
import org.noMoon.ArtificalSociety.history.DTO.HometownHistoryDTO;
import org.noMoon.ArtificalSociety.history.DTO.SchoolHistoryDTO;
import org.noMoon.ArtificalSociety.history.DTO.WorkHistoryDTO;
import org.noMoon.ArtificalSociety.history.Records.HistoryRecord;
import org.noMoon.ArtificalSociety.history.Records.SchoolHistoryRecord;
import org.noMoon.ArtificalSociety.history.Records.WorkHistoryRecord;
import org.noMoon.ArtificalSociety.history.services.HistoryService;
import org.noMoon.ArtificalSociety.institution.DO.Club;
import org.noMoon.ArtificalSociety.institution.DO.Institution;
import org.noMoon.ArtificalSociety.institution.DTO.ClubDTO;
import org.noMoon.ArtificalSociety.institution.enums.InstitutionEnum;
import org.noMoon.ArtificalSociety.institution.services.ClubService;
import org.noMoon.ArtificalSociety.institution.services.InstitutionService;
import org.noMoon.ArtificalSociety.person.DTO.PersonDTO;
import org.noMoon.ArtificalSociety.person.Enums.RelationStatusEnum;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by noMoon on 2015-10-31.
 */
public class GroupAdder {

    private static HistoryService historyService;
    private static GroupService groupService;
    private static CareerService careerService;
    private static InstitutionService institutionService;
    private static ClubService clubService;
    private static SequenceService sequenceService;

    public static void createRelationship (PersonDTO personA, PersonDTO personB, RelationStatusEnum relType) {
        // Indicate the relationship between the two persons, personA and personB.
        // param personA: one person in the relationship
        // param personB: the other person in the relationship
        // param relType: an integer representing the relationship type {0 = Single, 1 = Married, 2 = Dating}

        personA.setRelationshipStatus(relType);
        personB.setRelationshipStatus(relType);

        personA.setPartnerId(personB.getId());
        personB.setPartnerId(personA.getId());

        RelationshipCalculator.calculateAndSetInterestSimilarity(personA, personB);


        RelationshipCalculator.CalculateAndSetRelationshipStrength(personA, personB, 0); // Before children are created, so pass = 0.

        // DELETED assignRelationshipStart() on Feb. 16.
        // Determine the year this relationship began (if married, then it's the year that they became married, not the year they started dating).
        //AttributeAssigner.assignRelationshipStart(personA, personB, relType);

        // Calculate relationship strength between couple.
        //double relStrength = RelationshipCalculator.CalculateRelationshipStrength(personA, personB);
        //personA.relationshipStrength = relStrength;
        //personB.relationshipStrength = relStrength;

        long familyID = Long.parseLong(sequenceService.generateIdByEnum(SequenceEnum.FAMILY_ID_SEQUENCE));
        personA.setFamilyId(familyID);
        personB.setFamilyId(familyID);

    } // end createRelationship()

    public static void addToGroups(PersonDTO person) {
        // Schools.
        AddToSchoolGroups(person);

        // Work.
        AddToWorkGroups(person);

        // Temples.
        AddToTempleGroups(person);

        // Clubs.
        AddToClubGroups(person);
    }

    public static void AddToClubGroups (PersonDTO person) {
        // This function adds the person to club groups.
        // param person: the Person instance for whom to add to clubs based on their trait attributes


        // ------------------------------------------------
        // CLUBS
        // ------------------------------------------------
        int c, t;

        ArrayList clubTraits;


        double p_inClub;
        double rnd_inClub;

        boolean hasUnmatchedRequiredTrait = false;

        List<Club> clubList=clubService.selectClubBySocietyId(person.getSocietyId());

        for(Club clubDO:clubList){

            ClubDTO club=new ClubDTO(clubDO);
            HashMap<String, List<String>> traits=club.getTrait();


            // Create array to store all calculated trait matches.
            List<Double> traitMatches = new ArrayList<Double>();
            // Get number of openings for this career.


            hasUnmatchedRequiredTrait = false;

            for (String key : traits.keySet()) {
                List<String> traitValueList = traits.get(key);
                for (String traitValue : traitValueList) {
                    String trait = "";
                    String operator = "";
                    String threshold = "";
                    String[] reqVal = traitValue.split("\\|");
                    if (reqVal.length == 1) {
                        trait = traitValue;
                    } else {
                        trait = reqVal[0];
                        operator = reqVal[1];
                        threshold = reqVal[2];
                    }
                    double traitMatch = TraitMatcher.calculateTraitMatch(person, key, trait);
                    traitMatches.add(traitMatch);
                    if (!StringUtils.isEmpty(operator) && !ValidationTools.parseAndCheckCondition(traitMatch, operator + "|" + threshold)) {
                        hasUnmatchedRequiredTrait = true;
                        break; // Don't bother looping through following traits for this club - since this required one is false, it is pointless to check other conditions)
                    }
                }
            }

            p_inClub = TraitMatcher.calculateTotalProbability(traitMatches, hasUnmatchedRequiredTrait);


            // Generate random number to determine if person is in this club or not.
            rnd_inClub = Distribution.uniform(0.0, 1.0);

            if (rnd_inClub <= p_inClub && !hasUnmatchedRequiredTrait) {
                // Add person to club group.
                GroupDTO grp=groupService.getGroupDTOByNameAndYear(person.getSocietyId(),club.getTitle(),0);

                if (grp != null) {
                    addToGroup(grp,person,"Member");
                    //person.addToGroup(grp.GroupID);
//                    person.addToGroupBeta(grp.GroupID, "Member");
                } // end if (ensure that group was found properly)

            } // end if (determine if person is assigned into club or not)

            //System.out.println("............................................");
        } // end for c (loop through all clubs)


    } // end AddToClubGroups()

    public static void AddToTempleGroups (PersonDTO person) {
        // This function adds person to religious body groups and temple groups.

        // If the person has no religion, then exit function.
        if (Configuration.ReligionLabels[person.getReligionIndex()].equals("None")) {
            return;
        } // end if (check if person has no religion)


        // Set the boundary years that person attended this temple. For now, assume they always went to this temple while living in t
        int yearLastAttendedTemple = Configuration.SocietyYear;
        int yearFirstAttendedTemple = person.getBirthYear();
        HometownHistoryDTO socHometownHistory=historyService.getHometownHistoryById(person.getSocHometownHistoryId());

        // Determine all years that person lived in society and were attending the temple.
        int y;

        ArrayList<Integer> localReligiousYears = new ArrayList();
        // Loop through all years that person was religious (currently their whole life but that could be modified).
        for(HistoryRecord record:socHometownHistory.getRecordList()){
            for(int i=record.getStartYear();i<=record.getEndYear();i++){
                localReligiousYears.add(i);
            }
        }

        String religion = Configuration.ReligionLabels[person.getReligionIndex()];

//        Tree religTree = (Tree)GroupGenerator.getSpecificTree(new String[] {GroupGenerator.religionsLabel, religion});
//        Group[] religGroup = religTree.groupsAtNode;

        // -------------------------------------------------------------------------------
        // Add person to group of the religious body in the society.
        // -------------------------------------------------------------------------------
        // Ensure that there is one appropriate group for the religion in the society (i.e. the body of followers of that faith).
//        if (religGroup.length == 1 && religGroup[0] != null) {
//            //person.addToGroup(religGroup[0].GroupID);
//            person.addToGroupBeta(religGroup[0].GroupID, "Spiritual Member");
//        } // end if (ensure that group was found properly)

//        ArrayList<Tree> religTemples = religTree.getChildren();

        List<Institution> religTemples=institutionService.selectInstitutionbyTypeAndSubType(person.getSocietyId(), InstitutionEnum.TEMPLE, religion);

        if (religTemples.size() == 0) {
            // If there are no temples in the society that match this person's religion, then exit function now.
            return;
        } // end if (check if there are no temples of religion)

        int rndIndex = Distribution.uniform(0, religTemples.size() - 1);

        Institution temple=religTemples.get(rndIndex);

        // Indicate this temple as the person's primary temple they attend.
        person.setTempleAttending(temple.getTitle()); // temple.data is the name/label of the temple.

        // -------------------------------------------------------------------------------
        // Add person to group for a religious temple.
        // -------------------------------------------------------------------------------

        for (int year:localReligiousYears) {
            GroupDTO grp=groupService.getGroupDTOByNameAndYear(person.getSocietyId(),temple.getTitle(),year);
            if (grp != null) {
                addToGroup(grp,person,"Spiritual Student");
            } // end if (ensure that group was found properly)
        } // end for y (loop through years that person lived locally and held this faith)

    } // end AddToTempleGroups()

    public static void AddToWorkGroups(PersonDTO person) {
        // This function adds the person to workplace groups for each and every year they worked there.
        //

        // ------------------------------------------------
        // WORK
        // ------------------------------------------------
        WorkHistoryDTO socWorkHistory=historyService.getWorkHistoryById(person.getSocWorkHistoryId());

        for(WorkHistoryRecord record:socWorkHistory.getRecordList()){
            Workplace workplace=careerService.selectWorkplaceById(Long.parseLong(record.getLocation()));
            String workTitle=workplace.getTitle();
            CareerDTO career=new CareerDTO(careerService.selectCareerById(record.getCareerId()));
            String careerTitle=career.getTitle();
            for(int i=record.getStartYear();i<=record.getEndYear();i++){
                GroupDTO grp = groupService.getGroupDTOByNameAndYear(person.getSocietyId(), workTitle, i);
                addToGroup(grp, person, "Worker (" + careerTitle + ")");
            }
        }

    } // end AddToWorkGroups()

    public static void AddToSchoolGroups(PersonDTO person) {
        // This function adds the person to school groups for the school they attended, within a small range around the years they attended school.

        SchoolHistoryDTO schoolHistoryDTO = historyService.getSchoolHistoryById(person.getSocSchoolHistoryId());
        // ------------------------------------------------------------------------------------------------
        // Elementary schools.
        // ------------------------------------------------------------------------------------------------
        if (person.getAge() >= Configuration.SchoolFinishAge) {
            // When person reaches the end of their elementary school, add them into the proper groups.

            int yearFinishedElementary = person.getBirthYear() + Configuration.SchoolFinishAge - 1;

            SchoolHistoryRecord schoolInFinalYear = (SchoolHistoryRecord) schoolHistoryDTO.getActivityByYear(yearFinishedElementary);

            if (schoolInFinalYear != null) {

                // ADD GROUPS!
                addSchoolInfo(person, schoolInFinalYear);

            } // end if (check if local school information was found for the person's graduating year)

        } // end if (check if person is at the end of their elementary school currently)


        // ------------------------------------------------------------------------------------------------
        // Post-secondary schools.
        // ------------------------------------------------------------------------------------------------
        if (Configuration.SocietyYear >= person.getYearFinishedPsSchool()) {
            //System.out.println("Person " + person.getID() + " is finished post-secondary school at age = " + person.getAge() + ". Current year = " + Configuration.SocietyYear);

            //DebugTools.printArray(person.getSocietalSchoolHistory());

            int yearFinishedPS = person.getYearFinishedPsSchool();
            SchoolHistoryRecord schoolInFinalYear = (SchoolHistoryRecord) schoolHistoryDTO.getActivityByYear(yearFinishedPS);
            if (schoolInFinalYear != null) {

                // ADD GROUPS!
                addSchoolInfo(person, schoolInFinalYear);
                //System.out.println("After adding school groups...");
                //DisplayStudentGroups_TEMP(person);

            } // end if (check if local school information was found for the person's graduating year)


        } // end if (check if person is at the end of their post-secondary school currently)

    } // end AddToSchoolGroups()

    private static void addSchoolInfo(PersonDTO person, SchoolHistoryRecord schInfo) {

        String schoolName = schInfo.getLocation();

        int endYear = schInfo.getEndYear();
        int y;

        for (y = endYear - Configuration.DeltaNumYearsPersonSocializeWithInSchool; y <= endYear + Configuration.DeltaNumYearsPersonSocializeWithInSchool; y++) {
            GroupDTO grp = groupService.getGroupDTOByNameAndYear(person.getSocietyId(), schoolName, y);

            if (grp != null) {

                addToGroup(grp, person, "Student");
            } // end if (ensure that group was found properly)
        } // end for y (looping from FinalYear-Delta to FinalYear+Delta)
    }

    private static void addToGroup(GroupDTO group, PersonDTO person, String role) {
        if (!person.isInGroup(group.getId(), role)) {
            if (person.getGroupIds().containsKey(group.getId())) {
                ArrayList<String> roles = person.getGroupIds().get(group.getId());
                roles.add(role);
            } else {
                ArrayList<String> roles = new ArrayList<String>();
                roles.add(role);
                person.getGroupIds().put(group.getId(), roles);
            }
        }
        group.getMemberList().add(person.getId());
        groupService.updateGroupById(group);
    }

    public void setGroupService(GroupService groupService) {
        GroupAdder.groupService = groupService;
    }

    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }

    public void setCareerService(CareerService careerService) {
        GroupAdder.careerService = careerService;
    }

    public void setInstitutionService(InstitutionService institutionService) {
        GroupAdder.institutionService = institutionService;
    }

    public void setClubService(ClubService clubService) {
        GroupAdder.clubService = clubService;
    }

    public void setSequenceService(SequenceService sequenceService) {
        GroupAdder.sequenceService = sequenceService;
    }
}
