package org.noMoon.ArtificalSociety.group.Services;

import org.noMoon.ArtificalSociety.group.DTO.GroupDTO;

/**
 * Created by noMoon on 2015-10-10.
 */
public interface GroupService {
    long insertNewGroup(GroupDTO groupDTO);

    void generateGroups(String societyId);

    GroupDTO getGroupDTOByNameAndYear(String societyId,String name,int year);

    void updateGroupById(GroupDTO groupDTO);
}
