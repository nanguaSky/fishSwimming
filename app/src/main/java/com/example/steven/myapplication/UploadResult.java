package com.example.steven.myapplication;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 来自 伍玉南 的装逼小尾巴 on 18/3/12.
 * 上传文件的结构
 */

public class UploadResult {

    private int currIndex;

    private List<String> urls;

    private String url;

    private String[] arr;


    public UploadResult() {
    }

    public UploadResult(int size) {
        arr = new String[size];
        urls = new ArrayList<>(size);
    }

    //加入一个返回结果, 适用于多文件上传情形
    public void addResult(int position, String url){
        arr[position] = url;
        urls.clear();
        Collections.addAll(urls, arr);
        currIndex ++;
    }

    public boolean isFinish(){
        return (arr != null && arr.length == currIndex) || !TextUtils.isEmpty(url);
    }

    public int getCurrIndex() {
        return currIndex;
    }

    public void setCurrIndex(int currIndex) {
        this.currIndex = currIndex;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
