package com.wrbi.springbootinit.model.enums;

public enum IntegralTypeEnum {
    SIGN_IN(1, "签到"),
    CONTINUOUS_SIGN_IN(2, "连续签到"),
    WELFARE(3, "福利"),
    REPAIR_SIGN_IN(4, "补签"),
    CONSUME(5, "生成图表"),
    BUY(6, "购买积分"),
    ;

    private final Integer value;
    private final String text;

    IntegralTypeEnum(Integer value, String text) {
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
