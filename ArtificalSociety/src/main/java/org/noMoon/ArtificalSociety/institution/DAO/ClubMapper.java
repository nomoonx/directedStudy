package org.noMoon.ArtificalSociety.institution.DAO;

import org.noMoon.ArtificalSociety.institution.DO.Club;

public interface ClubMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CLUB
     *
     * @mbggenerated Fri Sep 04 21:56:34 EDT 2015
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CLUB
     *
     * @mbggenerated Fri Sep 04 21:56:34 EDT 2015
     */
    int insert(Club record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CLUB
     *
     * @mbggenerated Fri Sep 04 21:56:34 EDT 2015
     */
    int insertSelective(Club record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CLUB
     *
     * @mbggenerated Fri Sep 04 21:56:34 EDT 2015
     */
    Club selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CLUB
     *
     * @mbggenerated Fri Sep 04 21:56:34 EDT 2015
     */
    int updateByPrimaryKeySelective(Club record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CLUB
     *
     * @mbggenerated Fri Sep 04 21:56:34 EDT 2015
     */
    int updateByPrimaryKey(Club record);
}