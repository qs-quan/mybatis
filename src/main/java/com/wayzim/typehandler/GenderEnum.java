package com.wayzim.typehandler;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-10-20 9:10
 */
public enum GenderEnum {
    MALE("0", "男"),
    FEMALE("1", "女"),
    UNKONW("2", "未知");

    GenderEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    private String value;
    private String description;

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
