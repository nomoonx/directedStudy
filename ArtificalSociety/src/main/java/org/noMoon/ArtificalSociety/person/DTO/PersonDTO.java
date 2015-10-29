package org.noMoon.ArtificalSociety.person.DTO;

import com.alibaba.fastjson.JSON;
import org.noMoon.ArtificalSociety.person.DO.PersonWithBLOBs;
import org.noMoon.ArtificalSociety.person.Enums.GenderEnum;
import org.noMoon.ArtificalSociety.person.Enums.PositionEnum;
import org.noMoon.ArtificalSociety.person.Enums.RelationStatusEnum;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by noMoon on 2015-10-17.
 */
public class PersonDTO {

    private String id;

    private Date gmtCreate;

    private Date gmtModified;

    private String societyId;

    private GenderEnum sex;

    private Integer age;

    private Integer birthYear;

    private Integer expDeathYear;

    private Integer raceIndex;

    private Double intelligence;

    private double[] personality;

    private Double athleticism;

    private Integer religionIndex;

    private String nationality;

    private String templeAttending;

    //interest
    private double[] interest;

    private double[] interestWeight;


    //relationship
    private RelationStatusEnum relationshipStatus;

    private String partnerId;

    private Double interestSimilarity;

    private Double relationshipStrength;

    private Integer relationshipStartYear;

    //career
    private Long careerId;

    private PositionEnum currentPosition;

    private Integer income;

    //education
    private String education;

    private Integer educationPsYear;

    private Boolean isInSchool;

    private Integer yearStartedPsSchool;

    private Integer yearFinishedPsSchool;

    //history
    private Long hometownHistoryId;

    private Long schoolHistoryId;

    private Long workHistoryId;

    private Long socHometownHistoryId;

    private Long socSchoolHistoryId;

    private Long socWorkHistoryId;

    //family
    private Long familyId;

    private ArrayList<String> parentIds;

    private ArrayList<String> childrenIds;

    private ArrayList<String> siblingsIds;

    //club
    private ArrayList<Long> clubIds;

    //group
    private ArrayList<Long> groupIds;

    public PersonDTO(PersonWithBLOBs personDO) {
        this.id=personDO.getId();
                this.gmtCreate=personDO.getGmtCreate();
                this.gmtModified=personDO.getGmtModified();
                this.societyId=personDO.getSocietyId();
                this.sex=GenderEnum.getEnumByValue(personDO.getSex());
                this.age=personDO.getAge();
                this.birthYear=personDO.getBirthYear();
                this.expDeathYear=personDO.getExpDeathYear();
                this.raceIndex=personDO.getRaceIndex();
                this.intelligence=personDO.getIntelligence();
                this.personality= JSON.parseObject(personDO.getPersonality(),double[].class);
                this.athleticism=personDO.getAthleticism();
                this.religionIndex=personDO.getReligionIndex();
                this.nationality=personDO.getNationality();
                this.templeAttending=personDO.getTempleAttending();
                this.interest=JSON.parseObject(personDO.getInterest(),double[].class);
                this.interestWeight=JSON.parseObject(personDO.getInterestWeight(),double[].class);
                this.relationshipStatus=RelationStatusEnum.getEnumByValue(personDO.getRelationshipStatus());
                this.partnerId=personDO.getPartnerId();
                this.interestSimilarity=personDO.getInterestSimilarity();
                this.relationshipStrength=personDO.getRelationshipStrength();
                this.relationshipStartYear=personDO.getRelationshipStartYear();
                this.careerId=personDO.getCareerId();
                this.currentPosition=PositionEnum.getEnumByValue(personDO.getCurrentPosition());
                this.income=personDO.getIncome();
                this.education=personDO.getEducation();
                this.educationPsYear=personDO.getEducationPsYear();
                this.isInSchool=personDO.getIsInSchool();
                this.yearStartedPsSchool=personDO.getYearStartedPsSchool();
                this.yearFinishedPsSchool=personDO.getYearFinishedPsSchool();
                this.hometownHistoryId=personDO.getHometownHistoryId();
                this.schoolHistoryId=personDO.getSchoolHistoryId();
                this.workHistoryId=personDO.getWorkHistoryId();
                this.socHometownHistoryId=personDO.getSocHometownHistoryId();
                this.socSchoolHistoryId=personDO.getSocSchoolHistoryId();
                this.socWorkHistoryId=personDO.getSocWorkHistoryId();
                this.familyId=personDO.getFamilyId();
                this.parentIds=JSON.parseObject(personDO.getParentIds(),ArrayList.class);
                this.childrenIds=JSON.parseObject(personDO.getChildrenIds(),ArrayList.class);
                this.siblingsIds=JSON.parseObject(personDO.getSiblingsIds(),ArrayList.class);
                this.clubIds=JSON.parseObject(personDO.getClubIds(),ArrayList.class);
                this.groupIds=JSON.parseObject(personDO.getGroupIds(),ArrayList.class);
    }

    public PersonDTO(){}

    public PersonWithBLOBs convertToPerson(){
        PersonWithBLOBs person=new PersonWithBLOBs();
        person.setId(this.getId());
        person.setGmtCreate(this.getGmtCreate());
        person.setGmtModified(this.getGmtModified());
        person.setSocietyId(this.getSocietyId());
        person.setSex(this.getSex().getValue());
        person.setAge(this.getAge());
        person.setBirthYear(this.getBirthYear());
        person.setExpDeathYear(this.getExpDeathYear());
        person.setRaceIndex(this.getRaceIndex());
        person.setIntelligence(this.getIntelligence());
        person.setPersonality(JSON.toJSONString(this.getPersonality()));
        person.setAthleticism(this.getAthleticism());
        person.setReligionIndex(this.getReligionIndex());
        person.setNationality(this.getNationality());
        person.setTempleAttending(this.getTempleAttending());
        person.setInterest(JSON.toJSONString(this.getInterest()));
        person.setInterestWeight(JSON.toJSONString(this.getInterestWeight()));
        person.setRelationshipStatus(this.getRelationshipStatus().getValue());
        person.setPartnerId(this.getPartnerId());
        person.setInterestSimilarity(this.getInterestSimilarity());
        person.setRelationshipStrength(this.getRelationshipStrength());
        person.setRelationshipStartYear(this.getRelationshipStartYear());
        person.setCareerId(this.getCareerId());
        person.setCurrentPosition(this.getCurrentPosition().getValue());
        person.setIncome(this.getIncome());
        person.setEducation(this.getEducation());
        person.setEducationPsYear(this.getEducationPsYear());
        person.setIsInSchool(this.getIsInSchool());
        person.setYearStartedPsSchool(this.getYearStartedPsSchool());
        person.setYearFinishedPsSchool(this.getYearFinishedPsSchool());
        person.setHometownHistoryId(this.getHometownHistoryId());
        person.setSchoolHistoryId(this.getSchoolHistoryId());
        person.setWorkHistoryId(this.getWorkHistoryId());
        person.setSocHometownHistoryId(this.getSocHometownHistoryId());
        person.setSocSchoolHistoryId(this.getSocSchoolHistoryId());
        person.setSocWorkHistoryId(this.getSocWorkHistoryId());
        person.setFamilyId(this.getFamilyId());
        person.setParentIds(JSON.toJSONString(this.getParentIds()));
        person.setChildrenIds(JSON.toJSONString(this.getChildrenIds()));
        person.setSiblingsIds(JSON.toJSONString(this.getSiblingsIds()));
        person.setClubIds(JSON.toJSONString(this.getClubIds()));
        person.setGroupIds(JSON.toJSONString(this.getGroupIds()));


        return person;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
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

    public GenderEnum getSex() {
        return sex;
    }

    public void setSex(GenderEnum sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public Integer getExpDeathYear() {
        return expDeathYear;
    }

    public void setExpDeathYear(Integer expDeathYear) {
        this.expDeathYear = expDeathYear;
    }

    public Integer getRaceIndex() {
        return raceIndex;
    }

    public void setRaceIndex(Integer raceIndex) {
        this.raceIndex = raceIndex;
    }

    public Double getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(Double intelligence) {
        this.intelligence = intelligence;
    }

    public double[] getPersonality() {
        return personality;
    }

    public void setPersonality(double[] personality) {
        this.personality = personality;
    }

    public Double getAthleticism() {
        return athleticism;
    }

    public void setAthleticism(Double athleticism) {
        this.athleticism = athleticism;
    }

    public Integer getReligionIndex() {
        return religionIndex;
    }

    public void setReligionIndex(Integer religionIndex) {
        this.religionIndex = religionIndex;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getTempleAttending() {
        return templeAttending;
    }

    public void setTempleAttending(String templeAttending) {
        this.templeAttending = templeAttending;
    }

    public double[] getInterest() {
        return interest;
    }

    public void setInterest(double[] interest) {
        this.interest = interest;
    }

    public double[] getInterestWeight() {
        return interestWeight;
    }

    public void setInterestWeight(double[] interestWeight) {
        this.interestWeight = interestWeight;
    }

    public RelationStatusEnum getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(RelationStatusEnum relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public Double getInterestSimilarity() {
        return interestSimilarity;
    }

    public void setInterestSimilarity(Double interestSimilarity) {
        this.interestSimilarity = interestSimilarity;
    }

    public Double getRelationshipStrength() {
        return relationshipStrength;
    }

    public void setRelationshipStrength(Double relationshipStrength) {
        this.relationshipStrength = relationshipStrength;
    }

    public Integer getRelationshipStartYear() {
        return relationshipStartYear;
    }

    public void setRelationshipStartYear(Integer relationshipStartYear) {
        this.relationshipStartYear = relationshipStartYear;
    }

    public Long getCareerId() {
        return careerId;
    }

    public void setCareerId(Long careerId) {
        this.careerId = careerId;
    }

    public PositionEnum getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(PositionEnum currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Integer getIncome() {
        return income;
    }

    public void setIncome(Integer income) {
        this.income = income;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public Integer getEducationPsYear() {
        return educationPsYear;
    }

    public void setEducationPsYear(Integer educationPsYear) {
        this.educationPsYear = educationPsYear;
    }

    public Boolean getIsInSchool() {
        return isInSchool;
    }

    public void setIsInSchool(Boolean isInSchool) {
        this.isInSchool = isInSchool;
    }

    public Integer getYearStartedPsSchool() {
        return yearStartedPsSchool;
    }

    public void setYearStartedPsSchool(Integer yearStartedPsSchool) {
        this.yearStartedPsSchool = yearStartedPsSchool;
    }

    public Integer getYearFinishedPsSchool() {
        return yearFinishedPsSchool;
    }

    public void setYearFinishedPsSchool(Integer yearFinishedPsSchool) {
        this.yearFinishedPsSchool = yearFinishedPsSchool;
    }

    public Long getHometownHistoryId() {
        return hometownHistoryId;
    }

    public void setHometownHistoryId(Long hometownHistoryId) {
        this.hometownHistoryId = hometownHistoryId;
    }

    public Long getSchoolHistoryId() {
        return schoolHistoryId;
    }

    public void setSchoolHistoryId(Long schoolHistoryId) {
        this.schoolHistoryId = schoolHistoryId;
    }

    public Long getWorkHistoryId() {
        return workHistoryId;
    }

    public void setWorkHistoryId(Long workHistoryId) {
        this.workHistoryId = workHistoryId;
    }

    public Long getSocHometownHistoryId() {
        return socHometownHistoryId;
    }

    public void setSocHometownHistoryId(Long socHometownHistoryId) {
        this.socHometownHistoryId = socHometownHistoryId;
    }

    public Long getSocSchoolHistoryId() {
        return socSchoolHistoryId;
    }

    public void setSocSchoolHistoryId(Long socSchoolHistoryId) {
        this.socSchoolHistoryId = socSchoolHistoryId;
    }

    public Long getSocWorkHistoryId() {
        return socWorkHistoryId;
    }

    public void setSocWorkHistoryId(Long socWorkHistoryId) {
        this.socWorkHistoryId = socWorkHistoryId;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public ArrayList<String> getParentIds() {
        return parentIds;
    }

    public void setParentIds(ArrayList<String> parentIds) {
        this.parentIds = parentIds;
    }

    public ArrayList<String> getChildrenIds() {
        return childrenIds;
    }

    public void setChildrenIds(ArrayList<String> childrenIds) {
        this.childrenIds = childrenIds;
    }

    public ArrayList<String> getSiblingsIds() {
        return siblingsIds;
    }

    public void setSiblingsIds(ArrayList<String> siblingsIds) {
        this.siblingsIds = siblingsIds;
    }

    public ArrayList<Long> getClubIds() {
        return clubIds;
    }

    public void setClubIds(ArrayList<Long> clubIds) {
        this.clubIds = clubIds;
    }

    public ArrayList<Long> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(ArrayList<Long> groupIds) {
        this.groupIds = groupIds;
    }
}
