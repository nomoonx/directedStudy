package org.noMoon.ArtificalSociety.career.DAO;

import org.noMoon.ArtificalSociety.career.DO.Workplace;

public interface WorkplaceMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table WORKPLACE
     *
     * @mbggenerated Sat Sep 12 21:30:00 EDT 2015
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table WORKPLACE
     *
     * @mbggenerated Sat Sep 12 21:30:00 EDT 2015
     */
    int insert(Workplace record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table WORKPLACE
     *
     * @mbggenerated Sat Sep 12 21:30:00 EDT 2015
     */
    int insertSelective(Workplace record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table WORKPLACE
     *
     * @mbggenerated Sat Sep 12 21:30:00 EDT 2015
     */
    Workplace selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table WORKPLACE
     *
     * @mbggenerated Sat Sep 12 21:30:00 EDT 2015
     */
    int updateByPrimaryKeySelective(Workplace record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table WORKPLACE
     *
     * @mbggenerated Sat Sep 12 21:30:00 EDT 2015
     */
    int updateByPrimaryKey(Workplace record);
}