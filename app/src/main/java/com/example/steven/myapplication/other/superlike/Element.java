package com.example.steven.myapplication.other.superlike;

import android.graphics.Bitmap;

/**
 * Created by Sen on 2018/3/9.
 */

public interface Element {

    int getX();

    int getY();

    int getAlpha();

    Bitmap getBitmap();

    void evaluate(int start_x, int start_y, double time);

}
