package org.noMoon.ArtificalSociety.group.DAO;

import org.noMoon.ArtificalSociety.group.DO.Group;

public interface GroupMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AS_GROUP
     *
     * @mbggenerated Sat Oct 10 22:45:10 EDT 2015
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AS_GROUP
     *
     * @mbggenerated Sat Oct 10 22:45:10 EDT 2015
     */
    int insert(Group record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AS_GROUP
     *
     * @mbggenerated Sat Oct 10 22:45:10 EDT 2015
     */
    int insertSelective(Group record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AS_GROUP
     *
     * @mbggenerated Sat Oct 10 22:45:10 EDT 2015
     */
    Group selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AS_GROUP
     *
     * @mbggenerated Sat Oct 10 22:45:10 EDT 2015
     */
    int updateByPrimaryKeySelective(Group record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AS_GROUP
     *
     * @mbggenerated Sat Oct 10 22:45:10 EDT 2015
     */
    int updateByPrimaryKeyWithBLOBs(Group record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AS_GROUP
     *
     * @mbggenerated Sat Oct 10 22:45:10 EDT 2015
     */
    int updateByPrimaryKey(Group record);

    Group selectByNameAndYear(Group record);
}