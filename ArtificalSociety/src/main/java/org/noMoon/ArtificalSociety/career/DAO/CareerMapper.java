package org.noMoon.ArtificalSociety.career.DAO;

import org.noMoon.ArtificalSociety.career.DO.Career;

import java.util.List;

public interface CareerMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CAREER
     *
     * @mbggenerated Thu Sep 10 03:30:13 EDT 2015
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CAREER
     *
     * @mbggenerated Thu Sep 10 03:30:13 EDT 2015
     */
    int insert(Career record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CAREER
     *
     * @mbggenerated Thu Sep 10 03:30:13 EDT 2015
     */
    int insertSelective(Career record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CAREER
     *
     * @mbggenerated Thu Sep 10 03:30:13 EDT 2015
     */
    Career selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CAREER
     *
     * @mbggenerated Thu Sep 10 03:30:13 EDT 2015
     */
    int updateByPrimaryKeySelective(Career record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CAREER
     *
     * @mbggenerated Thu Sep 10 03:30:13 EDT 2015
     */
    int updateByPrimaryKey(Career record);

    List<Career> selectByCareerId(Career record);

    List<Career> listBySocietyId(Career record);
}