package org.noMoon.ArtificalSociety.commons.Enums;

/**
 * Created by noMoon on 2015-08-20.
 */
public enum TraitTypeEnum {
    MBTI("mbti"),AGE("age"),SEX("sex"),RACE("race"),RELIGION("religion"),REL_STATUS("rel_status"),
    CAREER_ID("careerID"),WORK_ID("workID"),EXTRA("extra"),NUM_PS_YEARS("num_ps_years");

    String value;

    public String getValue() {
        return value;
    }

    TraitTypeEnum(String value) {

        this.value = value;
    }
}
