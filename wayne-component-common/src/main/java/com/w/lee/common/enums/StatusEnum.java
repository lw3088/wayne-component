package com.w.lee.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;


@Getter
@AllArgsConstructor
public enum StatusEnum {

    ENABLE(1, "启用"),

    DISABLE(-1, "禁用"),

    ;


    /**
     * 编码
     */
    private final Integer code;

    /**
     * 描述
     */
    private final String desc;


    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static StatusEnum getByCode(String code) {
        return Arrays.stream(StatusEnum.values()).filter(it -> it.code.equals(code)).findFirst().orElse(null);
    }


    public static StatusEnum getByDesc(String desc) {
        return Arrays.stream(StatusEnum.values()).filter(it -> it.desc.equals(desc)).findFirst().orElse(null);
    }

    public static String getDescByCode(Integer code) {
        StatusEnum[] statusEnums = StatusEnum.values();
        for (int i = 0; i < statusEnums.length; i++) {
            if (Objects.equals(statusEnums[i].getCode(), code)) {
                return statusEnums[i].getDesc();
            }
        }
        return "";
    }


}
