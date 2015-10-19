package org.noMoon.ArtificalSociety.person.Enums;

/**
 * Created by noMoon on 2015-08-10.
 */
public enum RelationStatusEnum {

    SINGLE("SINGLE"),MARRIED("MARRIED"),DATING("DATING"),DIVORCED("DIVORCED");

    private String value;

    RelationStatusEnum(String val) {
        value = val;
    }

    public String getValue() {
        return value;
    }

    public static RelationStatusEnum getEnumByValue(String val){
        for(RelationStatusEnum enu:RelationStatusEnum.values()){
            if(enu.getValue().equals(val)){
                return enu;
            }
        }
        return null;
    }
}
