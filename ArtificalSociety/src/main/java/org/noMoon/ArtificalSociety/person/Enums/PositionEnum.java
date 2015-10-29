package org.noMoon.ArtificalSociety.person.Enums;

/**
 * Created by noMoon on 2015-10-28.
 */
public enum PositionEnum {

    CHILD("CHILD"),STUDENT("STUDENT"),WORKING("WORKING"),UNEMPLOYED("UNEMPLOYED"),RETIRED("RETIRED"),DEAD("DEAD");

    private String value;

    PositionEnum(String val) {
        value = val;
    }

    public String getValue() {
        return value;
    }

    public static PositionEnum getEnumByValue(String val){
        for(PositionEnum enu:PositionEnum.values()){
            if(enu.getValue().equals(val)){
                return enu;
            }
        }
        return null;
    }
}
