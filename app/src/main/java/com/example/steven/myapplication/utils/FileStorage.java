package com.example.steven.myapplication.utils;

import android.os.Environment;
import android.support.annotation.Nullable;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class FileStorage {
    public final static String appName = "xmzj";
    public final static String imgPathName = "xmzj_pictures";
    public final static String videoPathName = "Video";
    public final static String filePathName = "File";
    public final static String ScreenShotsPathName = "ScreenShots";
    public final static String tempPathName = "temp" ;     //临时文件夹  即用即删的那种

    public FileStorage() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File external = Environment.getExternalStorageDirectory();
            String rootDir = "/" + appName;
        }
    }

    //获取APP缓存文件路径
    @Nullable
    public static File getAppPath(){
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File external = Environment.getExternalStorageDirectory();
            String rootDir = "/" + appName;

            File appFile = new File(external,rootDir);
            if(!appFile.exists()){
                appFile.mkdirs();
            }
            return appFile;
        }
        return null;
    }

    //获取图片缓存路径
    @Nullable
    public static File getImgCacheFile(){
        File appFile = getAppPath();
        if(appFile != null){
            File imgFile = new File(appFile,"/" + imgPathName);
            if(!imgFile.exists()){
                imgFile.mkdirs();
            }

            return imgFile;
        }
        return null;
    }

    //获取视频缓存路径
    public static File getVideoCacheFile(){
        File appFile = getAppPath();
        if(appFile != null){
            File videoFile = new File(appFile,"/" + videoPathName);
            if(!videoFile.exists()){
                videoFile.mkdirs();
            }

            return videoFile;
        }
        return null;
    }

    //获取屏幕截图路径
    public static File getScreenShotsFile(){
        File appFile = getAppPath();
        if(appFile != null){
            File videoFile = new File(appFile,"/" + ScreenShotsPathName);
            if(!videoFile.exists()){
                videoFile.mkdirs();
            }

            return videoFile;
        }
        return null;
    }


    //获取文件缓存路径
    public static File getCacheFile(){
        File appFile = getAppPath();
        if(appFile != null){
            File cache = new File(appFile,"/" + filePathName);
            if(!cache.exists()){
                cache.mkdirs();
            }

            return cache;
        }
        return null;
    }

    public static String getTempPathName(){
        return  getImgCacheFile().getAbsolutePath() + File.separator + "temp.png";
    }

    public static String getTempVideoName(){
        return  "Video-" + getCurTime("MM-dd-HH-mm-ss")+".jpg";
    }


    /**
     * 根据当前时间生成一个临时文件名
     * @return
     */
    public static String getImageTempName(){
        return "IMG-" + getCurTime("MM-dd-HH-mm-ss")+".jpg";
    }



    public static String getCurTime(String formatType) {
        Calendar         calendar = new GregorianCalendar();
        Date             date     = calendar.getTime();
        SimpleDateFormat format   = new SimpleDateFormat(formatType);
        // SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(date);
    }

}