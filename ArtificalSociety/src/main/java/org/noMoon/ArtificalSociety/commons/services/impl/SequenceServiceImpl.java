package org.noMoon.ArtificalSociety.commons.services.impl;

import org.noMoon.ArtificalSociety.commons.DAO.SequenceMapper;
import org.noMoon.ArtificalSociety.commons.DO.Sequence;
import org.noMoon.ArtificalSociety.commons.Enums.SequenceEnum;
import org.noMoon.ArtificalSociety.commons.services.SequenceService;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by noMoon on 2015-08-21.
 */
public class SequenceServiceImpl implements SequenceService {

    SequenceMapper sequenceMapper;

    public String generateIdByEnum(SequenceEnum sequenceEnum) {
        if (StringUtils.isEmpty(sequenceEnum)){
            return null;
        }
        Sequence rightnowSequence=sequenceMapper.selectBySequenceName(sequenceEnum.getKeyName());
        int rightnowIndex=rightnowSequence.getSequenceValue()+1;
        String index=String.valueOf(rightnowIndex);
        int length=index.length();
        rightnowSequence.setSequenceValue(rightnowIndex);
        if(length<sequenceEnum.getDigit()){
            for(int i=0;i<sequenceEnum.getDigit()-length;i++){
               index="0"+index;
            }
        }else if(length>sequenceEnum.getDigit()){
            index="";
            for(int i=0;i<sequenceEnum.getDigit();i++){
                index+="0";
            }
            rightnowSequence.setSequenceValue(0);
        }
        sequenceMapper.updateByPrimaryKey(rightnowSequence);

        Date today=new Date();
        if(sequenceEnum.equals(SequenceEnum.SOCIETY_ID_SEQUENCE)){
            SimpleDateFormat formatter=new SimpleDateFormat("yyyyMMdd");
            index=sequenceEnum.getPrefix()+formatter.format(today)+index;
        }

        return index;
    }

    public void setSequenceMapper(SequenceMapper sequenceMapper) {
        this.sequenceMapper = sequenceMapper;
    }


}
