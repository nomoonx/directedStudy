package org.noMoon.ArtificalSociety.institution.DAO;

import org.noMoon.ArtificalSociety.institution.DO.Institution;

import java.util.List;

public interface InstitutionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table INSTITUTION
     *
     * @mbggenerated Wed Sep 02 00:15:00 EDT 2015
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table INSTITUTION
     *
     * @mbggenerated Wed Sep 02 00:15:00 EDT 2015
     */
    int insert(Institution record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table INSTITUTION
     *
     * @mbggenerated Wed Sep 02 00:15:00 EDT 2015
     */
    int insertSelective(Institution record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table INSTITUTION
     *
     * @mbggenerated Wed Sep 02 00:15:00 EDT 2015
     */
    Institution selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table INSTITUTION
     *
     * @mbggenerated Wed Sep 02 00:15:00 EDT 2015
     */
    int updateByPrimaryKeySelective(Institution record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table INSTITUTION
     *
     * @mbggenerated Wed Sep 02 00:15:00 EDT 2015
     */
    int updateByPrimaryKey(Institution record);

    List<Institution> selectByTitle(Institution record);

    List<Institution> selectBySocietyId(Institution record);

    List<Institution> selectByDO(Institution record);

    List<String> selectCityByType(Institution record);

    List<String> selectPSSchoolNameByType(Institution record);

}