package org.noMoon.ArtificalSociety.group.Services.impl;

import org.noMoon.ArtificalSociety.group.DAO.GroupMapper;
import org.noMoon.ArtificalSociety.group.DO.Group;
import org.noMoon.ArtificalSociety.group.DTO.GroupDTO;
import org.noMoon.ArtificalSociety.group.Services.GroupService;

/**
 * Created by noMoon on 2015-10-10.
 */
public class GroupServiceImpl implements GroupService {

    GroupMapper groupMapper;

    public long insertNewGroup(GroupDTO groupDTO) {
        Group insertDO=groupDTO.convertToGroupDO();
        groupMapper.insert(insertDO);
        groupDTO.setId(insertDO.getId());
        return groupDTO.getId();
    }

    public void setGroupMapper(GroupMapper groupMapper) {
        this.groupMapper = groupMapper;
    }
}
