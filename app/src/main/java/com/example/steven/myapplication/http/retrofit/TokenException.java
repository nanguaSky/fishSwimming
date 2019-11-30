package com.example.steven.myapplication.http.retrofit;

import java.io.IOException;

/**
 * 来自 伍玉南 的装逼小尾巴 on 18/11/1.
 */
public class TokenException extends IOException {

    private String msg;
    private String code;

    public TokenException(String msg, String code) {
        this.msg = msg;
        this.code = code;
    }

    public TokenException(String message, String msg, String code) {
        super(message);
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
