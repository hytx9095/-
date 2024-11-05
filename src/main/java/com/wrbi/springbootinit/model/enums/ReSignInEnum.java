package com.wrbi.springbootinit.model.enums;

public enum ReSignInEnum {
    NO_REPAIR_SIGN_IN(0, "未补签"),
    REPAIR_SIGN_IN(1, "已补签"),
    NO_REPAIR_SIGN_IN_DATE(2, "无补签日期"),
    ;

    private final Integer value;
    private final String text;

    ReSignInEnum(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    public Integer getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
