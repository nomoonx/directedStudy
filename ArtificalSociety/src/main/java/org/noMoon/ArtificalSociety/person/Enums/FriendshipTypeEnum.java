package org.noMoon.ArtificalSociety.person.Enums;

/**
 * Created by noMoon on 2015-11-14.
 */
public enum FriendshipTypeEnum {

    FAMILY("FAMILY"),PARTNER("PERTNER"),FRIEND("FRIEND");

    String value;

    FriendshipTypeEnum(String val){
        this.value=val;
    }

    public FriendshipTypeEnum getEnumByValue(String val){
        for(FriendshipTypeEnum enu:FriendshipTypeEnum.values()){
            if(enu.getValue().equals(val)){
                return enu;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}
