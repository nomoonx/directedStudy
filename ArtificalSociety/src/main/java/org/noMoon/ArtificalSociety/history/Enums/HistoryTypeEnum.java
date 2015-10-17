package org.noMoon.ArtificalSociety.history.Enums;

/**
 * Created by noMoon on 2015-10-16.
 */
public enum HistoryTypeEnum {
    HOMETOWN_HISTORY("HOMETOWN_HISTORY"),SCHOOL_HISTORY("SCHOOL_HISTORY"),WORK_HISTORY("WORK_HISTORY");
    private String value;

    HistoryTypeEnum(String val){
        this.value=val;
    }

    public String getValue() {
        return value;
    }

    public static HistoryTypeEnum getEnumByValue(String val){
        for(HistoryTypeEnum enu:HistoryTypeEnum.values()){
            if(enu.getValue().equals(val)){
                return enu;
            }
        }
        return null;
    }
}
