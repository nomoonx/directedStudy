package org.noMoon.ArtificalSociety.history.Records;

/**
 * Created by noMoon on 2015-10-16.
 */
public class SchoolHistoryRecord extends HistoryRecord {
    String schoolType;

    public SchoolHistoryRecord(String location,int sYear,int eYear,String sType){
        super(location,sYear,eYear);
        this.schoolType=sType;
    }

    public SchoolHistoryRecord(){
        super();
    }

    public String getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(String schoolType) {
        this.schoolType = schoolType;
    }
}
