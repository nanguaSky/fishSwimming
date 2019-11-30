package com.example.steven.myapplication.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.steven.myapplication.R;
import com.example.steven.myapplication.bean.WishTreeBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Steven on 16/12/5.
 * 许愿树view
 *
 * addView(WishTreeBean bean)
 */
public class WishTreeView extends FrameLayout implements View.OnClickListener {

    private int width;
    private int height;

    private int viewWidth;
    private int viewheight;

    private List<ImageView> views;

    private Random mRandom;

    private Drawable lingfuBg;

    private OnWishTreeListener treeListener;

    public WishTreeView(Context context) {
        this(context, null);
    }

    public WishTreeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WishTreeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        views = new ArrayList<ImageView>();
        mRandom = new Random();

        lingfuBg = getResources().getDrawable(R.mipmap.wish_lingfu);
        viewWidth = lingfuBg.getMinimumWidth();
        viewheight = lingfuBg.getMinimumHeight();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(views != null && views.size() != 0){
            for(ImageView img : views){
                int x = getXRandom();
                int y = getYRandom();
                img.layout(x,y,x + viewWidth, y + viewheight);
            }
        }
    }

    //添加一个许愿符
    public void addView(WishTreeBean bean){
        ImageView img = new ImageView(getContext());
        img.setTag(bean);
        img.setOnClickListener(this);
        img.setBackground(lingfuBg);
        views.add(img);
        addView(img);
        requestLayout();
    }

    public void addAllView(List<WishTreeBean> beans){
        if(beans != null){
            for(WishTreeBean bean : beans){
                addView(bean);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
    }

    public int getXRandom(){
        int x = mRandom.nextInt(width);
        return x;
    }

    public int getYRandom(){
        int y = mRandom.nextInt((int) (height  * 0.75));
        return y;
    }

    @Override
    public void onClick(View v) {
        WishTreeBean bean = (WishTreeBean) v.getTag();
        if(treeListener != null){
            treeListener.onWishtTreeClick(v,bean);
        }
    }


    public interface OnWishTreeListener{

        void onWishtTreeClick(View v, WishTreeBean bean);
    }

    public void setOnWishTreeListener(OnWishTreeListener treeListener) {
        this.treeListener = treeListener;
    }
}
