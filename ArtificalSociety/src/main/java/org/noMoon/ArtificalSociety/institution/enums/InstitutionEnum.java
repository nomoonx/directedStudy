package org.noMoon.ArtificalSociety.institution.enums;

/**
 * Created by noMoon on 2015-08-31.
 */
public enum InstitutionEnum {
    ELEMENTARY_SCHOOL("ELEMENTARY_SCHOOL","school"),POST_SECONDARY_SCHOOL("POST_SECONDARY_SCHOOL","school"),TEMPLE("TEMPLE","temple");

    String value;
    String elementName;

    public String getElementName() {
        return elementName;
    }

    public String getValue() {
        return value;
    }

    InstitutionEnum(String val,String elementName) {
        this.value=val;
        this.elementName=elementName;
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
