package org.noMoon.ArtificalSociety.society.services.impl;

import org.noMoon.ArtificalSociety.commons.Enums.SequenceEnum;
import org.noMoon.ArtificalSociety.commons.services.SequenceService;
import org.noMoon.ArtificalSociety.society.DAO.SocietyMapper;
import org.noMoon.ArtificalSociety.society.DO.Society;
import org.noMoon.ArtificalSociety.society.services.SocietyService;

/**
 * Created by noMoon on 2015-08-21.
 */
public class SocietyServiceImpl implements SocietyService {

    SequenceService sequenceService;
    SocietyMapper societyMapper;

    public String insertNewSociety(Society society) {
        String societyId=sequenceService.generateIdByEnum(SequenceEnum.SOCIETY_ID_SEQUENCE);
        society.setId(societyId);
        societyMapper.insertSelective(society);
        return societyId;
    }

    public void setSocietyMapper(SocietyMapper societyMapper) {
        this.societyMapper = societyMapper;
    }

    public void setSequenceService(SequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }
}
