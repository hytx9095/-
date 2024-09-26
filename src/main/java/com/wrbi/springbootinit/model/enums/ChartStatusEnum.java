package com.wrbi.springbootinit.model.enums;

import lombok.Data;

public enum ChartStatusEnum {

    WAIT("wait", "等待"),
    SUCCEED("succeed", "成功"),
    RUNNING("running", "运行中"),
    FAILED("failed", "失败");

    private String value;
    private String desc;

    private ChartStatusEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }
}
