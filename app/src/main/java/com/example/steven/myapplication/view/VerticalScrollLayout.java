package com.example.steven.myapplication.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.steven.myapplication.ScrollDataSetAdapter;
import com.example.steven.myapplication.utils.LogUtil;

public class VerticalScrollLayout extends ViewGroup {

    private int displayCount = 3;   //显示在屏幕的个数
    private int itemHeigth = 0;     //单行的高度
    private boolean isRunning;

    private ScrollDataSetAdapter mAdapter;

    private ValueAnimator valueAnimator;


    public VerticalScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        int height = 0;

        if(mAdapter != null){
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);

                measureChild(child, widthMeasureSpec, heightMeasureSpec);

                width = child.getMeasuredWidth() > width ? child.getMeasuredWidth() : width;
                int childHeight = child.getMeasuredHeight();

                if(i < displayCount){
                    height += childHeight;
                }

                itemHeigth = child.getMeasuredHeight();
            }
        }

        LogUtil.i("width   : " + width + "    height : " + height);
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            int lc = getPaddingLeft();
            int tc = i * itemHeigth + getPaddingTop();
            int rc = child.getMeasuredWidth() + getPaddingRight();
            int bc = i * itemHeigth + child.getMeasuredHeight() + getPaddingTop();

            child.layout(lc, tc, rc, bc);
        }
    }


    public void startScroll(){
        if(!isRunning){
            isRunning = true;

            valueAnimator = ValueAnimator.ofInt(0, itemHeigth);
            valueAnimator.setDuration(500);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scrollTo(0, (Integer) animation.getAnimatedValue());
                }
            });

            valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if(getChildCount() > displayCount){
                        swapText();
                    }
                    isRunning = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            valueAnimator.start();
        }
    }


    public void stopScroll(){

    }


    //设置适配器
    public void setAdapter(ScrollDataSetAdapter adapter) {
        this.mAdapter = adapter;

        if(adapter != null){
            adapter.setAttachView(this);
            displayCount = adapter.getDisPlayCount();

            int viewCount = adapter.count() > displayCount ? displayCount : adapter.count();

            for (int i = 0; i < viewCount; i++) {
                View child = createView();
                mAdapter.bindView(child, mAdapter.getDatas().get(i), i);
                addView(child);
            }
            invalidate();
        }
    }


    public <T> void addData(T t){
        if(mAdapter != null){

            if(mAdapter.count() < displayCount + 1){
                View child = createView();
                mAdapter.bindView(child, t, mAdapter.count());
                addView(child);

            } else {
                View child = getChildAt(getChildCount() - 1);
                mAdapter.bindView(child, t, mAdapter.count());

                if(mAdapter.count() > displayCount + 1){
                    mAdapter.getDatas().remove(0);
                }
            }

            mAdapter.getDatas().add(t);
            if(mAdapter.count() > displayCount){
                startScroll();
            }
        }
    }


    private View createView(){
        View view = View.inflate(getContext(), mAdapter.getLayoutId(), null);
        return view;
    }


    //动画结束后要交换数据
    private void swapText(){
        if(getChildCount() > displayCount){
            View first = getChildAt(0);

            removeView(first);
            addView(first);
            scrollTo(0, 0);
        }
    }

}
