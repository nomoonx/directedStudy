package org.noMoon.ArtificalSociety.institution.enums;

/**
 * Created by noMoon on 2015-08-31.
 */
public enum InstitutionEnum {
    ELEMENTARY_SCHOOL("ELEMENTARY_SCHOOL"),POST_SECONDARY_SCHOOL("POST_SECONDARY_SCHOOL"),TEMPLE("TEMPLE");

    String value;

    public String getValue() {
        return value;
    }

    InstitutionEnum(String val) {
        this.value = val;
    }

    public static InstitutionEnum getEnumByValue(String val){
        for(InstitutionEnum enu:InstitutionEnum.values()){
            if(enu.getValue().equals(val)){
                return enu;
            }
        }
        return null;
    }
}
