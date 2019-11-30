package com.example.steven.myapplication.http;


public abstract class ApiCallBack<T>{

    public void onCompleted(){}

    public abstract void onSuccess(T data);

    public abstract void onError(String errorCode, String message);
}
