package org.noMoon.ArtificalSociety.history.Records;

/**
 * Created by noMoon on 2015-10-16.
 */
public class WorkHistoryRecord extends HistoryRecord {
    private Long careerId;

    public WorkHistoryRecord(int sYear,int eYear, Long careerId, String loc){
        super(loc,sYear,eYear);
        this.careerId=careerId;
    }

    public WorkHistoryRecord(){
        super();
    }

    public Long getCareerId() {
        return careerId;
    }

    public void setCareerId(Long careerId) {
        this.careerId = careerId;
    }
}
