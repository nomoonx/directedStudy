package org.noMoon.ArtificalSociety.history.DTO;

import com.alibaba.fastjson.JSON;
import org.noMoon.ArtificalSociety.history.DO.History;
import org.noMoon.ArtificalSociety.history.Enums.HistoryTypeEnum;
import org.noMoon.ArtificalSociety.history.Records.HistoryRecord;

import java.util.ArrayList;

/**
 * Created by noMoon on 2015-10-16.
 */
public class HometownHistoryDTO extends HistoryDTO {
    private ArrayList<HistoryRecord> recordList;

    public HometownHistoryDTO(){
        super();
        this.setType(HistoryTypeEnum.HOMETOWN_HISTORY);
        recordList=new ArrayList<HistoryRecord>();
    }

    public HometownHistoryDTO(History history){
        super(history);
        this.setType(HistoryTypeEnum.HOMETOWN_HISTORY);
        recordList=JSON.parseObject(history.getHistoryList(),ArrayList.class);
    }

    @Override
    public History convertToHistory(){
        History history=super.convertToHistory();
        history.setHistoryList(JSON.toJSONString(recordList));
        return history;
    }

    public ArrayList<HistoryRecord> getRecordList() {
        return recordList;
    }

    public void setRecordList(ArrayList<HistoryRecord> recordList) {
        this.recordList = recordList;
    }
}
