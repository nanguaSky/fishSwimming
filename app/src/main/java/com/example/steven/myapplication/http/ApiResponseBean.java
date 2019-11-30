package com.example.steven.myapplication.http;

import com.google.gson.annotations.SerializedName;

public class ApiResponseBean<T> {


    @SerializedName("status")
    private int status;

    @SerializedName("errorCode")
    private String errorCode;

    @SerializedName("result")
    private T result;

    @SerializedName("message")
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return status == 1;
    }


}
