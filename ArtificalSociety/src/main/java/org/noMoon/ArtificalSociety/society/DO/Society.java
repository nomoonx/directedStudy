package org.noMoon.ArtificalSociety.society.DO;

import java.util.Date;

public class Society {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SOCIETY.ID
     *
     * @mbggenerated Fri Aug 21 02:57:35 EDT 2015
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SOCIETY.GMT_CREATE
     *
     * @mbggenerated Fri Aug 21 02:57:35 EDT 2015
     */
    private Date gmtCreate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SOCIETY.GMT_MODIFIED
     *
     * @mbggenerated Fri Aug 21 02:57:35 EDT 2015
     */
    private Date gmtModified;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SOCIETY.SOCIETY_NAME
     *
     * @mbggenerated Fri Aug 21 02:57:35 EDT 2015
     */
    private String societyName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SOCIETY.SOCIETY_YEAR
     *
     * @mbggenerated Fri Aug 21 02:57:35 EDT 2015
     */
    private Integer societyYear;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SOCIETY.ID
     *
     * @return the value of SOCIETY.ID
     *
     * @mbggenerated Fri Aug 21 02:57:35 EDT 2015
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SOCIETY.ID
     *
     * @param id the value for SOCIETY.ID
     *
     * @mbggenerated Fri Aug 21 02:57:35 EDT 2015
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SOCIETY.GMT_CREATE
     *
     * @return the value of SOCIETY.GMT_CREATE
     *
     * @mbggenerated Fri Aug 21 02:57:35 EDT 2015
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SOCIETY.GMT_MODIFIED
     *
     * @return the value of SOCIETY.GMT_MODIFIED
     *
     * @mbggenerated Fri Aug 21 02:57:35 EDT 2015
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SOCIETY.SOCIETY_NAME
     *
     * @return the value of SOCIETY.SOCIETY_NAME
     *
     * @mbggenerated Fri Aug 21 02:57:35 EDT 2015
     */
    public String getSocietyName() {
        return societyName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SOCIETY.SOCIETY_NAME
     *
     * @param societyName the value for SOCIETY.SOCIETY_NAME
     *
     * @mbggenerated Fri Aug 21 02:57:35 EDT 2015
     */
    public void setSocietyName(String societyName) {
        this.societyName = societyName == null ? null : societyName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SOCIETY.SOCIETY_YEAR
     *
     * @return the value of SOCIETY.SOCIETY_YEAR
     *
     * @mbggenerated Fri Aug 21 02:57:35 EDT 2015
     */
    public Integer getSocietyYear() {
        return societyYear;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SOCIETY.SOCIETY_YEAR
     *
     * @param societyYear the value for SOCIETY.SOCIETY_YEAR
     *
     * @mbggenerated Fri Aug 21 02:57:35 EDT 2015
     */
    public void setSocietyYear(Integer societyYear) {
        this.societyYear = societyYear;
    }
}