package org.noMoon.ArtificalSociety.commons.DAO;

import org.noMoon.ArtificalSociety.commons.DO.Sequence;

public interface SequenceMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SEQUENCES
     *
     * @mbggenerated Fri Aug 21 02:57:35 EDT 2015
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SEQUENCES
     *
     * @mbggenerated Fri Aug 21 02:57:35 EDT 2015
     */
    int insert(Sequence record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SEQUENCES
     *
     * @mbggenerated Fri Aug 21 02:57:35 EDT 2015
     */
    int insertSelective(Sequence record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SEQUENCES
     *
     * @mbggenerated Fri Aug 21 02:57:35 EDT 2015
     */
    Sequence selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SEQUENCES
     *
     * @mbggenerated Fri Aug 21 02:57:35 EDT 2015
     */
    int updateByPrimaryKeySelective(Sequence record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SEQUENCES
     *
     * @mbggenerated Fri Aug 21 02:57:35 EDT 2015
     */
    int updateByPrimaryKey(Sequence record);

    Sequence selectBySequenceName(String name);
}