package org.noMoon.ArtificalSociety.group.DTO;

import com.alibaba.fastjson.JSON;
import org.noMoon.ArtificalSociety.group.DO.Group;

import java.util.Date;
import java.util.List;

/**
 * Created by noMoon on 2015-10-10.
 */
public class GroupDTO {
    private Long id;

    private Date gmtCreate;

    private Date gmtModified;

    private String societyId;

    private String groupLabel;

    private Integer groupYear;

    private List<String> memberList;

    public GroupDTO(Group groupDO){
        this.id=groupDO.getId();
        this.gmtCreate=groupDO.getGmtCreate();
        this.gmtModified=groupDO.getGmtModified();
        this.societyId=groupDO.getSocietyId();
        this.groupLabel=groupDO.getGroupLabel();
        this.groupYear=groupDO.getGroupYear();
        this.memberList= JSON.parseObject(groupDO.getMemberList(),List.class);
    }

    public GroupDTO(){}

    public Group convertToGroupDO(){
        Group group=new Group();
        group.setId(this.getId());
        group.setSocietyId(this.getSocietyId());
        group.setGroupLabel(this.getGroupLabel());
        group.setGroupYear(this.getGroupYear());
        group.setMemberList(JSON.toJSONString(this.getMemberList()));
        return group;
    }

    public int getNumMembers(){
        return memberList.size();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public String getSocietyId() {
        return societyId;
    }

    public void setSocietyId(String societyId) {
        this.societyId = societyId;
    }

    public String getGroupLabel() {
        return groupLabel;
    }

    public void setGroupLabel(String groupLabel) {
        this.groupLabel = groupLabel;
    }

    public Integer getGroupYear() {
        return groupYear;
    }

    public void setGroupYear(Integer groupYear) {
        this.groupYear = groupYear;
    }

    public List<String> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<String> memberList) {
        this.memberList = memberList;
    }
}
