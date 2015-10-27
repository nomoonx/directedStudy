package org.noMoon.ArtificalSociety.history.Records;

/**
 * Created by noMoon on 2015-10-16.
 */
public class HistoryRecord {
    private String location;
    private int startYear;
    private int endYear;

    public HistoryRecord(String loc,int sYear,int eYear){
        this.location=loc;
        this.startYear=sYear;
        this.endYear=eYear;
    }

    public HistoryRecord(){}

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }
}
