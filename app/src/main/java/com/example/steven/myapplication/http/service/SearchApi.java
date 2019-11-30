package com.example.steven.myapplication.http.service;

import com.example.steven.myapplication.http.ApiBuild;

/**
 * Created by laoshiren on 2018/8/13.
 */
public class SearchApi {

    private static SearchApi.Service instance;

    public interface Service {


    }

    public static SearchApi.Service getInstance() {
        if (instance == null) {
            instance = ApiBuild.getRetrofit().create(SearchApi.Service.class);
        }
        return instance;
    }
}
