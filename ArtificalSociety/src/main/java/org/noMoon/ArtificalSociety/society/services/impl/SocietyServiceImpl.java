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

    public int insertNewSociety(Society society) {
        society.setId(sequenceService.generateIdByEnum(SequenceEnum.SOCIETY_ID_SEQUENCE));
        return societyMapper.insertSelective(society);
    }

    public void setSocietyMapper(SocietyMapper societyMapper) {
        this.societyMapper = societyMapper;
    }

    public void setSequenceService(SequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }
}
