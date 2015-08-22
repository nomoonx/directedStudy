package org.noMoon.ArtificalSociety.commons.Enums;

/**
 * Created by noMoon on 2015-08-21.
 */
public enum SequenceEnum {
    SOCIETY_ID_SEQUENCE("SOCIETY_ID_SEQUENCE",7,"S");
    private String keyName;
    private int digit;
    private String prefix;

    public String getKeyName() {
        return keyName;
    }

    public int getDigit() {
        return digit;
    }

    public String getPrefix() {
        return prefix;
    }

    SequenceEnum(String keyName, int digit, String prefix) {
        this.keyName = keyName;
        this.digit = digit;
        this.prefix = prefix;
    }
}
