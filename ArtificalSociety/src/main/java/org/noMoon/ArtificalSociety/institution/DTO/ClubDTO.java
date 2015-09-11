package org.noMoon.ArtificalSociety.institution.DTO;

import com.alibaba.fastjson.JSON;
import org.noMoon.ArtificalSociety.institution.DO.Club;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by noMoon on 2015-09-06.
 */
public class ClubDTO {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column CLUB.ID
     *
     * @mbggenerated Fri Sep 04 21:56:34 EDT 2015
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column CLUB.GMT_CREATE
     *
     * @mbggenerated Fri Sep 04 21:56:34 EDT 2015
     */
    private Date gmtCreate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column CLUB.GMT_MODIFIED
     *
     * @mbggenerated Fri Sep 04 21:56:34 EDT 2015
     */
    private Date gmtModified;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column CLUB.SOCIETY_ID
     *
     * @mbggenerated Fri Sep 04 21:56:34 EDT 2015
     */
    private String societyId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column CLUB.TITLE
     *
     * @mbggenerated Fri Sep 04 21:56:34 EDT 2015
     */
    private String title;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column CLUB.TYPE
     *
     * @mbggenerated Fri Sep 04 21:56:34 EDT 2015
     */
    private String type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column CLUB.CITY
     *
     * @mbggenerated Fri Sep 04 21:56:34 EDT 2015
     */
    private String city;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column CLUB.POPULATION
     *
     * @mbggenerated Fri Sep 04 21:56:34 EDT 2015
     */
    private Integer population;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column CLUB.STARTING_YEAR
     *
     * @mbggenerated Fri Sep 04 21:56:34 EDT 2015
     */
    private Integer startingYear;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column CLUB.ENDING_YEAR
     *
     * @mbggenerated Fri Sep 04 21:56:34 EDT 2015
     */
    private Integer endingYear;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column CLUB.TRAIT
     *
     * @mbggenerated Fri Sep 04 21:56:34 EDT 2015
     */
    private HashMap<String, List<String>> trait;

    public ClubDTO(){}

    public ClubDTO(Club club){
        this.id=club.getId();
        this.city=club.getCity();
        this.endingYear=club.getEndingYear();
        this.startingYear=club.getStartingYear();
        this.gmtCreate=club.getGmtCreate();
        this.gmtModified=club.getGmtModified();
        this.population=club.getPopulation();
        this.trait= JSON.parseObject(club.getTrait(), HashMap.class);
        this.societyId=club.getSocietyId();
        this.title=club.getTitle();
        this.type=club.getType();
    }

    public Club convertToClub(){
        Club club=new Club();
        club.setCity(this.getCity());
        club.setId(this.getId());
        club.setEndingYear(this.getEndingYear());
        club.setStartingYear(this.getStartingYear());
        club.setPopulation(this.getPopulation());
        club.setTrait(JSON.toJSONString(this.getTrait()));
        club.setSocietyId(this.getSocietyId());
        club.setTitle(this.getTitle());
        club.setType(this.getType());
        return club;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSocietyId() {
        return societyId;
    }

    public void setSocietyId(String societyId) {
        this.societyId = societyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Integer getStartingYear() {
        return startingYear;
    }

    public void setStartingYear(Integer startingYear) {
        this.startingYear = startingYear;
    }

    public Integer getEndingYear() {
        return endingYear;
    }

    public void setEndingYear(Integer endingYear) {
        this.endingYear = endingYear;
    }

    public HashMap<String, List<String>> getTrait() {
        return trait;
    }

    public void setTrait(HashMap<String, List<String>> trait) {
        this.trait = trait;
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
}