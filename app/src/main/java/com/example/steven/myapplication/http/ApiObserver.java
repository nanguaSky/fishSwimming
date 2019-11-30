package com.example.steven.myapplication.http;

import com.example.steven.myapplication.http.retrofit.DataResultException;
import com.example.steven.myapplication.http.retrofit.TokenException;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;

/**
 * Created by Steven on 17/1/18.
 */

public class ApiObserver<T> implements Observer<T> {


    public static final String NET_ERROR           = "400";
    public static final String RESOLVE_ERROR       = "401";
    public static final String SERVER_ERROR        = "500";
    public static final String UNKONW_SERVER_ERROR = "302";
    public static final String SERVER_TIME_OUT     = "303";
    public static final String APP_EXCEPTION       = "110";
    public static final String SUCCESS             = "00000";
    public static final String SERVER_ERROR_99999  = "99999";//后台接口出错返回系统错误

    private int responseCode = 0;

    private ApiCallBack callBack;

    public ApiObserver(ApiCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onCompleted() {
        callBack.onCompleted();
    }

    @Override
    public void onNext(T t) {
        if (t instanceof ApiResponseBean) {
            ApiResponseBean apiResponseBean = (ApiResponseBean) t;

            if (!"00000".equals(apiResponseBean.getErrorCode())) {
                responseCode = -1;

                //未实名认证弹窗
                if ("10256".equals(apiResponseBean.getErrorCode())) {
                    callBack.onError(apiResponseBean.getErrorCode(), apiResponseBean.getMessage());
                    return;
                }

                return;
            }
            responseCode = 0;
            callBack.onSuccess(apiResponseBean.getResult());
            return;
        }

        callBack.onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof UnknownHostException) {
            callBack.onError(UNKONW_SERVER_ERROR, "无法连接服务器");
            return;
        }

        //连接网络超时
        if (e instanceof SocketTimeoutException || e instanceof SocketException) {
            callBack.onError(SERVER_TIME_OUT, "网络连接超时，请稍后再试");
            return;
        }

        //有网情况服务器错误
        if (e instanceof HttpException) {
            callBack.onError(SERVER_ERROR, "网络异常，请稍后再试");
            return;
        }

        //捕获服务器返回的不规范json
        if (e instanceof TokenException) {
            responseCode = -1;
            return;
        }

        if (e instanceof DataResultException) {
            responseCode = -1;
            callBack.onError(SERVER_ERROR_99999, "服务器开小差，请稍后..");
            return;
        }

        if (responseCode == -1) {
            callBack.onError(SERVER_ERROR, "获取失败");

        //有网络情况，返回响应正常，内部app异常
        } else if (responseCode == 0) {
            callBack.onError(APP_EXCEPTION, "app出错了..");

        } else {
            callBack.onError(NET_ERROR, "无法连接服务器");
        }

    }
}
