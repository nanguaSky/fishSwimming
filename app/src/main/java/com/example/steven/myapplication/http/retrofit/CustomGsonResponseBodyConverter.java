package com.example.steven.myapplication.http.retrofit;

import com.example.steven.myapplication.http.ApiResponseBean;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by laoshiren on 2018/8/21.
 * 解决服务器同一字段返回不同类型数据问题
 */

public class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private Gson gson;
    private Type type;

    public CustomGsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        try {
            ApiResponseBean baseBean = gson.fromJson(response, ApiResponseBean.class);

            if ("10001".equals(baseBean.getErrorCode()) ||
                    "10002".equals(baseBean.getErrorCode()) ||
                    "10007".equals(baseBean.getErrorCode()) ||
                    "10316".equals(baseBean.getErrorCode())) {
                throw new TokenException(baseBean.getMessage(), baseBean.getErrorCode());
            }

            if ("99999".equals(baseBean.getErrorCode())) {
                throw new DataResultException(baseBean.getMessage(), baseBean.getErrorCode());
            }
            return gson.fromJson(response, type);

        } finally {
            value.close();
        }
    }


}
