package org.noMoon.ArtificalSociety.history.DTO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.noMoon.ArtificalSociety.history.DO.History;
import org.noMoon.ArtificalSociety.history.Enums.HistoryTypeEnum;
import org.noMoon.ArtificalSociety.history.Records.HistoryRecord;
import org.noMoon.ArtificalSociety.history.Records.WorkHistoryRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by noMoon on 2015-10-16.
 */
public class WorkHistoryDTO extends HistoryDTO{

    private ArrayList<WorkHistoryRecord> recordList;

    public WorkHistoryDTO(){
        super();
        this.setType(HistoryTypeEnum.WORK_HISTORY);
        this.recordList=new ArrayList<WorkHistoryRecord>();
    }

    public WorkHistoryDTO(History history){
        super(history);
        this.setType(HistoryTypeEnum.WORK_HISTORY);
        recordList= JSON.parseObject(history.getHistoryList(),new TypeReference<ArrayList<WorkHistoryRecord>>(){});
    }

    @Override
    public History convertToHistory(){
        History history=super.convertToHistory();
        history.setHistoryList(JSON.toJSONString(recordList));
        return history;
    }

    public void patchSequentialEntries() {
        Collections.sort(recordList, new Comparator<HistoryRecord>() {
            public int compare(HistoryRecord o1, HistoryRecord o2) {
                if (o1.getStartYear() > o2.getStartYear()) {
                    return 1;
                } else if (o1.getStartYear() < o2.getStartYear()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        ArrayList<WorkHistoryRecord> tempList=new ArrayList<WorkHistoryRecord>();
        for(WorkHistoryRecord record:recordList){
            if(tempList.size()==0){
                tempList.add(record);
            }else{
                HistoryRecord tempRecord=tempList.get(tempList.size()-1);
                if(tempRecord.getLocation().equals(record.getLocation())&&
                        tempRecord.getEndYear()==record.getStartYear()){
                    tempRecord.setEndYear(record.getEndYear());
                }else{
                    tempList.add(record);
                }
            }
        }
        recordList.clear();
        recordList=tempList;
    }

    public ArrayList<WorkHistoryRecord> getRecordList() {
        return recordList;
    }

    public void setRecordList(ArrayList<WorkHistoryRecord> recordList) {
        this.recordList = recordList;
    }
}
