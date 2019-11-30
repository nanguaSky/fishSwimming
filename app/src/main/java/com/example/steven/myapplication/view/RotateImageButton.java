package com.example.steven.myapplication.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

/**
 * Created by Steven on 17/3/10.
 * 带旋转动画的按钮
 */
public class RotateImageButton extends CircleImageView{

    private static final int DURATION = 6 * 1000;

    private Animation animation;

    private boolean isRotate = false;

    public RotateImageButton(Context context) {
        this(context,null);
    }

    public RotateImageButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RotateImageButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public boolean isRotate() {
        return isRotate;
    }

    public void setRotate(boolean rotate) {
        if(rotate){
            startRotate();
        }else{
            stopRotate();
        }
        isRotate = rotate;
    }

    //开始旋转
    private void startRotate(){
        if(animation != null && !isRotate){
            startAnimation(animation);
        }
    }

    //停止旋转
    private void stopRotate(){
        if(animation != null && isRotate){
            animation.cancel();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        animation = new RotateAnimation(0,360,w/ 2, h / 2);
        animation.setDuration(DURATION);
        animation.setRepeatMode(Animation.INFINITE);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setInterpolator(new LinearInterpolator());
    }
}
