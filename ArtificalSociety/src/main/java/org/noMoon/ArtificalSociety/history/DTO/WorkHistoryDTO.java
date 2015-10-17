package org.noMoon.ArtificalSociety.history.DTO;

import com.alibaba.fastjson.JSON;
import org.noMoon.ArtificalSociety.history.DO.History;
import org.noMoon.ArtificalSociety.history.Enums.HistoryTypeEnum;
import org.noMoon.ArtificalSociety.history.Records.WorkHistoryRecord;

import java.util.ArrayList;

/**
 * Created by noMoon on 2015-10-16.
 */
public class WorkHistoryDTO extends HistoryDTO{

    private ArrayList<WorkHistoryRecord> recordList;

    public WorkHistoryDTO(){
        super();
        this.setType(HistoryTypeEnum.WORK_HISTORY);
    }

    public WorkHistoryDTO(History history){
        super(history);
        this.setType(HistoryTypeEnum.WORK_HISTORY);
        recordList= JSON.parseObject(history.getHistoryList(),ArrayList.class);
    }

    @Override
    public History convertToHistory(){
        History history=super.convertToHistory();
        history.setHistoryList(JSON.toJSONString(recordList));
        return history;
    }

    public ArrayList<WorkHistoryRecord> getRecordList() {
        return recordList;
    }

    public void setRecordList(ArrayList<WorkHistoryRecord> recordList) {
        this.recordList = recordList;
    }
}
