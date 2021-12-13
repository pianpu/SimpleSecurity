package io.github.pianpu.security.utils;

import java.io.Serializable;
import java.util.HashMap;

public class Result<T> extends HashMap<String, Object> implements Serializable {
    //200是正常，非200表示异常
    private int code;
    private String msg;
    private T data;


    public Result() {

    }

    public void setCode(int code) {
        this.code = code;
        this.put("code", this.code);
    }

    public void setMsg(String msg) {
        this.msg = msg;
        this.put("msg", this.msg);
    }

    public Result(int code, String msg, T data) {
        this.setCode(code);
        this.setMsg(msg);
        this.setData(data);
    }

    public void setData(T data) {
        this.data = data;
        this.put("data", this.data);

    }


    public static Result<Object> success(int code, String msg, Object data) {
        return new Result<Object>(code, msg, data);
    }

    public static Result<Object> success(Object data) {
        return success(200, "请求成功", data);
    }

    public static Result<Object> success(String msg) {
        return success(200, msg, null);
    }

    public static Result<Object> fail(int code, String msg, Object data) {
        return new Result<Object>(code, msg, data);
    }

    public static Result<Object> fail(String msg, Object data) {
        return fail(400, msg, data);
    }

    public static Result<Object> fail(int code, String msg) {
        return fail(code, msg, null);
    }

    public static Result<Object> fail(String msg) {
        return fail(400, msg, null);
    }
}