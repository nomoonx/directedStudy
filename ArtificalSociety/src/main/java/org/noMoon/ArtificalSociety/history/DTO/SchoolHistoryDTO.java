package org.noMoon.ArtificalSociety.history.DTO;

import com.alibaba.fastjson.JSON;
import org.noMoon.ArtificalSociety.history.DO.History;
import org.noMoon.ArtificalSociety.history.Enums.HistoryTypeEnum;
import org.noMoon.ArtificalSociety.history.Records.SchoolHistoryRecord;

import java.util.ArrayList;

/**
 * Created by noMoon on 2015-10-16.
 */
public class SchoolHistoryDTO extends HistoryDTO {
    private ArrayList<SchoolHistoryRecord> recordList;

    public SchoolHistoryDTO(){
        super();
        this.setType(HistoryTypeEnum.SCHOOL_HISTORY);
        recordList=new ArrayList<SchoolHistoryRecord>();
    }

    public SchoolHistoryDTO(History history){
        super(history);
        this.setType(HistoryTypeEnum.SCHOOL_HISTORY);
        this.recordList= JSON.parseObject(history.getHistoryList(),ArrayList.class);
    }

    @Override
    public History convertToHistory(){
        History history=super.convertToHistory();
        history.setHistoryList(JSON.toJSONString(recordList));
        return history;
    }

    public ArrayList<SchoolHistoryRecord> getRecordList() {
        return recordList;
    }

    public void setRecordList(ArrayList<SchoolHistoryRecord> recordList) {
        this.recordList = recordList;
    }
}
