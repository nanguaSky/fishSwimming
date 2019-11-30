package com.example.steven.myapplication.other.fish;

import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by Steven on 16/11/29.
 */
public class FishModel extends ImageView {

    private static final double SPEED = 0.001;
    private double progress = 0;

    private int fishWidth ;
    private int fishHeight ;

    private float pos [] = new float[2] ;
    private float tan [] = new float[2] ;

    private Path path;

    private AnimationDrawable anim; //鱼动画

    private PathMeasure pMeasure;

    public FishModel(Context context) {
        this(context, null, 0);

    }

    public FishModel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FishModel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(param);

    }

    //获取鱼下一个移动的点坐标
    public Point getNextMovePoi(){
        progress = progress < 1 ? progress + SPEED : 1;
        if(progress < 1){
            pMeasure.getPosTan((int) (pMeasure.getLength() * progress), pos, tan);
            float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI);
            setRotation(degrees);
            return new Point((int)pos[0], (int)pos[1]);
        }else{

            return null;
        }
    }

    //给鱼设置行走路线
    public void setPath(Path path) {
        this.path = path;
        progress = 0;
        pMeasure = new PathMeasure(path,false);
    }


    public void start(){
        if(anim != null){
            anim.start();
        }
    }

    public void stop(){
        if(anim != null){
            anim.stop();
        }
    }



    public Path getPath() {
        return path;
    }

    public int getFishWidth() {
        return fishWidth;
    }

    public int getFishHeight() {
        return fishHeight;
    }

    public void setAnim(AnimationDrawable anim) {
        this.anim = anim;
        setAdjustViewBounds(true);
        setImageDrawable(anim);
        fishWidth = anim.getMinimumWidth();
        fishHeight = anim.getMinimumHeight();
    }
}
