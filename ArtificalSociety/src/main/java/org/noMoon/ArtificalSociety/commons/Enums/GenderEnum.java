package org.noMoon.ArtificalSociety.commons.Enums;

/**
 * Created by noMoon on 2015-08-06.
 */
public enum GenderEnum {
    MALE("MALE"),FEMALE("FEMALE"),TRANSFER("TRANSFER");
    private String value;

    public String getValue() {
        return value;
    }

    GenderEnum(String val){
        value=val;
    }

    public static GenderEnum getEnumByValue(String val){
        for(GenderEnum enu:GenderEnum.values()){
            if(val.equals(enu.getValue())){
                return enu;
            }
        }
        return null;
    }
}
