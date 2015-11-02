package org.noMoon.ArtificalSociety.group.Services.impl;

import org.noMoon.ArtificalSociety.career.DO.Workplace;
import org.noMoon.ArtificalSociety.career.services.CareerService;
import org.noMoon.ArtificalSociety.group.DAO.GroupMapper;
import org.noMoon.ArtificalSociety.group.DO.Group;
import org.noMoon.ArtificalSociety.group.DTO.GroupDTO;
import org.noMoon.ArtificalSociety.group.Services.GroupService;
import org.noMoon.ArtificalSociety.institution.DO.Club;
import org.noMoon.ArtificalSociety.institution.DO.Institution;
import org.noMoon.ArtificalSociety.institution.services.ClubService;
import org.noMoon.ArtificalSociety.institution.services.InstitutionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by noMoon on 2015-10-10.
 */
public class GroupServiceImpl implements GroupService {

    GroupMapper groupMapper;
    InstitutionService institutionService;
    ClubService clubService;
    CareerService careerService;

    public long insertNewGroup(GroupDTO groupDTO) {
        Group insertDO=groupDTO.convertToGroupDO();
        groupMapper.insert(insertDO);
        groupDTO.setId(insertDO.getId());
        return groupDTO.getId();
    }

    public void generateGroups(String societyId) {
        HashMap<String, List<GroupDTO>> groupMap=new HashMap<String, List<GroupDTO>>();

        //add institution(including schools and religion) groups
        List<Institution> institutionList=institutionService.selectInstitutionBySocietyId(societyId);
        for(Institution inst:institutionList){
            String title=inst.getTitle();
            if(!groupMap.containsKey(title)){
                List<GroupDTO> groupList=new ArrayList<GroupDTO>();
                for(int i=inst.getStartingYear();i<=inst.getEndingYear();i++){
                    GroupDTO groupDTO=new GroupDTO();
                    groupDTO.setSocietyId(societyId);
                    groupDTO.setGroupLabel(title);
                    groupDTO.setMemberList(new ArrayList<String>());
                    groupDTO.setGroupYear(i);
                    groupList.add(groupDTO);
                }
                groupMap.put(title,groupList);
            }
        }
        institutionList.clear();

        //add club groups
        List<Club> clubList=clubService.selectClubBySocietyId(societyId);
        for(Club club:clubList){
            String title=club.getTitle();
            if(!groupMap.containsKey(title)){
                List<GroupDTO> tempList=new ArrayList<GroupDTO>();
                GroupDTO groupDTO=new GroupDTO();
                groupDTO.setSocietyId(societyId);
                groupDTO.setGroupLabel(title);
                groupDTO.setMemberList(new ArrayList<String>());
                groupDTO.setGroupYear(0);
                tempList.add(groupDTO);
                groupMap.put(title,tempList);
            }
        }
        clubList.clear();

        //add workplace groups
        List<Workplace> workplaceList=careerService.listWorkplaceWithSocietyId(societyId);
        for(Workplace workplace:workplaceList){
            String title=workplace.getTitle();
            if(!groupMap.containsKey(title)){
                List<GroupDTO> groupList=new ArrayList<GroupDTO>();
                for(int i=workplace.getStartingYear();i<=workplace.getEndingYear();i++){
                    GroupDTO groupDTO=new GroupDTO();
                    groupDTO.setSocietyId(societyId);
                    groupDTO.setGroupLabel(title);
                    groupDTO.setMemberList(new ArrayList<String>());
                    groupDTO.setGroupYear(i);
                    groupList.add(groupDTO);
                }
                groupMap.put(title,groupList);

            }
        }

        //should do transaction insert
        for(List<GroupDTO> list:groupMap.values()){
            for(GroupDTO groupDTO:list){
                groupMapper.insert(groupDTO.convertToGroupDO());
            }
        }

    }

    public GroupDTO getGroupDTOByNameAndYear(String societyId, String name, int year) {
        Group query=new Group();
        query.setSocietyId(societyId);
        query.setGroupLabel(name);
        query.setGroupYear(year);
        Group result=groupMapper.selectByNameAndYear(query);
        if(null==result)
        {
            System.out.println("societyId:"+societyId+"   groupname:"+name+"  year:"+String.valueOf(year)+"    couldnt find");
            return null;
        }
        return new GroupDTO(result);
    }

    public void updateGroupById(GroupDTO groupDTO) {
        groupMapper.updateByPrimaryKeySelective(groupDTO.convertToGroupDO());
    }

    public void setClubService(ClubService clubService) {
        this.clubService = clubService;
    }

    public void setInstitutionService(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    public void setCareerService(CareerService careerService) {
        this.careerService = careerService;
    }

    public void setGroupMapper(GroupMapper groupMapper) {
        this.groupMapper = groupMapper;
    }
}
