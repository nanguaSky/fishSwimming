package com.example.steven.myapplication.http;


import android.os.Build;
import android.webkit.WebSettings;

import com.example.steven.myapplication.Constant;
import com.example.steven.myapplication.MainActivity;
import com.example.steven.myapplication.MyApplication;
import com.example.steven.myapplication.http.retrofit.CustomGsonConverterFactory;
import com.example.steven.myapplication.test.Retrofit2ConverterFactory;
import com.example.steven.myapplication.utils.LogUtil;
import com.example.steven.myapplication.utils.MD5Util;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

public class ApiBuild {

    private static Retrofit retrofit;
    private static Retrofit wxRetrofit;
    private static Retrofit baseRetrofit;

    private final static int    CONNECT_TIMEOUT = 30;    //连接超时时间
    private final static int    READ_TIMEOUT    = 30;       //读取超时时间
    private final static int    WRITE_TIMEOUT   = 60 * 2;      //写的超时时间
    public static        String Tag             = "ApiBuild";
    public static        String USER_AGENT      = "";

    private ApiBuild() {

    }

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (ApiBuild.class) {
                if (retrofit == null) {
                    USER_AGENT = getUserAgent();

                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    OkHttpClient client = builder
                            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)        //设置读取超时时间
                            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                            .connectTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                            .addInterceptor(new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    String t    = System.currentTimeMillis() + "";
                                    String sign = MD5Util.MD5Encode(t + Constant.KEY, "");
                                    Request request = chain.request()
                                            .newBuilder()
                                            .removeHeader("User-Agent")
                                            .addHeader("User-Agent", USER_AGENT)
                                            .addHeader("t", t)
                                            .addHeader("sign", sign)
                                            .addHeader("from", "android")
                                            .addHeader("appType", "0")
                                            .addHeader("version", "47")
                                            .build();
                                    return chain.proceed(request);
                                }
                            })
                            .addNetworkInterceptor(new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Request request = chain.request();
                                    Response response = chain.proceed(request);

                                    MediaType contentType = null;
                                    String bodyString = null;
                                    if (response.body() != null) {
                                        contentType = response.body().contentType();
                                        bodyString = response.body().string();
                                    }

                                    LogUtil.i(bodyString);
                                    LogUtil.i("\n\n======================================\n\n");

                                    if (response.body() != null) {
                                        // 深坑！
                                        // 打印body后原ResponseBody会被清空，需要重新设置body
                                        ResponseBody body = ResponseBody.create(contentType, bodyString);
                                        return response.newBuilder().body(body).build();
                                    } else {
                                        return response;
                                    }
                                }
                            })
                            .build();

                    retrofit = new Retrofit.Builder()
                            //.baseUrl(TextUtils.isEmpty(BuildConfig.HOST_URL) ? BuildConfig.API_URL : BuildConfig.HOST_URL)
                            .baseUrl(Constant.Url.DEBUG_153)
                            .addConverterFactory(CustomGsonConverterFactory.create())
                            //.addConverterFactory(new Retrofit2ConverterFactory())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(client)
                            .build();
                }
            }
        }
        return retrofit;
    }


    private static String getUserAgent() {
        String userAgent = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(MyApplication.app);
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

}
