package com.will.gps.bean;

/**
 * Created by MaiBenBen on 2019/4/14.
 */
public enum GenderEnum {
    UNKNOWN(0),
    MALE(1),
    FEMALE(2);

    private Integer value;

    GenderEnum(int value) {
        this.value = value;
    }

    public static GenderEnum genderOfValue(int status) {
        for (GenderEnum e : values()) {
            if (e.getValue() == status) {
                return e;
            }
        }
        return UNKNOWN;
    }

    public Integer getValue() {
        return value;
    }
}
