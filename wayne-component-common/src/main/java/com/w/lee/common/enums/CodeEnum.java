

package com.w.lee.common.enums;

public enum CodeEnum {
    SUCCESS(200, "success"),
    FAIL(500, "internal error"),
   ;

    private int code;
    private String msg;

    private CodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
