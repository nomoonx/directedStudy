package org.noMoon.ArtificalSociety.person.DAO;

import org.noMoon.ArtificalSociety.person.DO.PersonWithBLOBs;

import java.util.List;

public interface PersonMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PERSON
     *
     * @mbggenerated Sat Oct 17 04:57:19 EDT 2015
     */
    int insert(PersonWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PERSON
     *
     * @mbggenerated Sat Oct 17 04:57:19 EDT 2015
     */
    int insertSelective(PersonWithBLOBs record);

    void insertList(List<PersonWithBLOBs> list);

    List<String> selectIdsBySocietyId(String societyId);

    List<String> selectAliveIdsBySocietyId(String societyId);

    PersonWithBLOBs selectById(String id);

    void updateById(PersonWithBLOBs record);

}