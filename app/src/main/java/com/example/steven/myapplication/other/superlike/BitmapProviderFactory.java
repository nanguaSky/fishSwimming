package com.example.steven.myapplication.other.superlike;

import android.content.Context;

import com.example.steven.myapplication.R;


/*
 * create by hsg on 2019/3/7
 */
public class BitmapProviderFactory {
    public static BitmapProvider.Provider getHDProvider(Context context) {
        return new BitmapProvider.Builder(context)
                .setDrawableArray(new int[]{R.mipmap.emoji_1, R.mipmap.emoji_2, R.mipmap.emoji_3, R.mipmap.emoji_4, R.mipmap.emoji_5, R.mipmap.emoji_6,
                        R.mipmap.emoji_7, R.mipmap.emoji_8, R.mipmap.emoji_9, R.mipmap.emoji_10})
                .setNumberDrawableArray(new int[]{R.mipmap.emoji_1, R.mipmap.emoji_2, R.mipmap.emoji_3})
                .setLevelDrawableArray(new int[]{R.mipmap.emoji_1, R.mipmap.emoji_2, R.mipmap.emoji_3})
                .build();
    }


    public static BitmapProvider.Provider getSmoothProvider(Context context) {
        return new BitmapProvider.Builder(context).build();
    }
}
