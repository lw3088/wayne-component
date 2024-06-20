
package com.w.lee.infrastructure.po;

import com.w.lee.common.enums.CodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("baseResponse")
@Data
public class R<T> implements Serializable {
    @ApiModelProperty("状态码")
    private int code;
    @ApiModelProperty("消息")
    private String msg;
    @ApiModelProperty("响应内容")
    private T data;
    @ApiModelProperty("是否成功")
    private boolean success;


    private R() {
    }

    public R(int code, String msg, boolean success) {
        this.code = code;
        this.msg = msg;
        this.success = success;
    }

    public static R success() {
        return new R(CodeEnum.SUCCESS.getCode(), CodeEnum.SUCCESS.getMsg(), true);
    }

    public static <T> R<T> success(T data) {
        R response = new R(CodeEnum.SUCCESS.getCode(), CodeEnum.SUCCESS.getMsg(), true);
        response.setData(data);
        return response;
    }

    public static R fail() {
        return new R(CodeEnum.FAIL.getCode(), CodeEnum.FAIL.getMsg(), false);
    }
    public static <T> R<T> fail(String msg) {
        return restResult(null, CodeEnum.FAIL.getCode(), msg);
    }
    public static <T> R<T> fail(int code, String msg) {
        return restResult(null, code, msg);
    }
    private static <T> R<T> restResult(T data, int code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setData(data);
        r.setMsg(msg);
        return r;
    }
}
