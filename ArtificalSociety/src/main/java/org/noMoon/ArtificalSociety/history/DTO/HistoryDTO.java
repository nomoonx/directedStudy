package org.noMoon.ArtificalSociety.history.DTO;

import org.noMoon.ArtificalSociety.history.DO.History;
import org.noMoon.ArtificalSociety.history.Enums.HistoryTypeEnum;
import org.noMoon.ArtificalSociety.history.Records.HistoryRecord;

import java.util.Date;

/**
 * Created by noMoon on 2015-10-15.
 */
public class HistoryDTO {

    private Long id;

    private Date gmtCreate;

    private Date gmtModified;
    private String societyId;
    private String personId;
    private HistoryTypeEnum type;

    public History convertToHistory(){
        History history=new History();
        history.setId(id);
        history.setSocietyId(societyId);
        history.setPersonId(personId);
        history.setType(type.getValue());
        return history;
    }

    public HistoryDTO(History history){
        this.setId(history.getId());
        this.setGmtCreate(history.getGmtCreate());
        this.setGmtModified(history.getGmtModified());
        this.setSocietyId(history.getSocietyId());
        this.setPersonId(history.getPersonId());
        this.setType(HistoryTypeEnum.getEnumByValue(history.getType()));
    }

    public HistoryDTO(){}

    public HistoryRecord getActivityByYear(int year){return null;}

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

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getSocietyId() {
        return societyId;
    }

    public void setSocietyId(String societyId) {
        this.societyId = societyId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public HistoryTypeEnum getType() {
        return type;
    }

    public void setType(HistoryTypeEnum type) {
        this.type = type;
    }

}
