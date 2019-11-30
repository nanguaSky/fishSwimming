package com.example.steven.myapplication.http.intercept;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TestIntercept implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        if(request.url().toString().endsWith("group/listInform")){
            ResponseBody responseBody = response.body();
            String json = responseBody.string();

            try {
                JSONObject jsonObject = new JSONObject(json);
                jsonObject.put("result", " ");

                ResponseBody newBody = ResponseBody.create(null, jsonObject.toString());
                return new Response.Builder()
                        .protocol(response.protocol())
                        .request(request)
                        .headers(response.headers())
                        .code(response.code())
                        .message(response.message())
                        .body(newBody)
                        .build();

            } catch (JSONException e) {
                e.printStackTrace();
                return response;
            }
        }

        return response;
    }
}
