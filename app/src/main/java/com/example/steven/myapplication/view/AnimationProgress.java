package com.example.steven.myapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.steven.myapplication.R;

/**
 * 动画过渡效果的进度条
 * Created by 伍玉南 on 16/10/25.
 */

public class AnimationProgress extends View {

    private final int TIME_INTERVAL = 20;  //重绘时间间隔



    private boolean start = false;

    private int viewWidth;          //控件宽度

    private int viewHeight;         //控件高度

    /**
     * 进度条相关
     */
    private Paint mPaint;

    private int proColor;         //进度条颜色

    private int roundRadius;      //圆角半径

    private int proWidth;         //进度条宽度

    private int value;

    private int currValue;

    private int currRatio = 0;


    /**
     * 文本相关
     */
    private Paint textPaint;

    private Paint numPaint;

    private String text;          //进度条的文字描述

    private String numText;       //百分比文本

    private int textInteRval;     //百分比文本 跟 进度条之间的间隔

    private int textSize;

    private int textHeight;

    private int textWidth;

    private int textColor;

    private int numColor;




    public AnimationProgress(Context context) {
        this(context, null);
    }

    public AnimationProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimationProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    private void init(AttributeSet attrs) {

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AnimationProgress);

        if(a != null){
            value = a.getInt(R.styleable.AnimationProgress_ap_Value,0);
            roundRadius = a.getDimensionPixelSize(R.styleable.AnimationProgress_ap_roundRadius,0);
            proColor = a.getColor(R.styleable.AnimationProgress_ap_Color,getContext().getResources().getColor(R.color.colorAccent));
            textSize = a.getDimensionPixelSize(R.styleable.AnimationProgress_ap_textSize,14);
            textColor = a.getColor(R.styleable.AnimationProgress_ap_textColor, Color.parseColor("#000000"));
            numColor = a.getColor(R.styleable.AnimationProgress_ap_numColor, Color.parseColor("#000000"));
            text = a.getString(R.styleable.AnimationProgress_ap_text);
        }


        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(proColor);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);

        numPaint = new Paint();
        numPaint.setAntiAlias(true);
        numPaint.setColor(numColor);
        numPaint.setTextSize(textSize);

        String temp = "100%";
        Rect rect = new Rect();
        textPaint.getTextBounds(temp,0,temp.length(),rect);
        textInteRval = (viewWidth - proWidth)/2 + 10;

        if(value != 0){
            start = true;
        }


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if(value == 0) return;

        canvas.save();
        canvas.restore();

        RectF rectF = new RectF(getPaddingLeft(),getPaddingTop(),currRatio+getPaddingRight(),viewHeight+getPaddingBottom());
        canvas.drawRoundRect(rectF,roundRadius,roundRadius,mPaint);


        measureTextSize();
        canvas.drawText(text,20,viewHeight/2 + textHeight /2,textPaint);
        numText = String.valueOf(currValue)+"%";
        canvas.drawText(numText,currRatio + textInteRval,viewHeight/2 + textHeight /2,numPaint);

        if(currValue < value && start){
            currValue += 1;
            float temp = (float) currValue;
            currRatio = (int) (proWidth * (temp / 100));
            postInvalidateDelayed(TIME_INTERVAL);
        }

    }


    //测量文字宽高
    private void measureTextSize(){
        Rect rect = new Rect();
        textPaint.getTextBounds(text,0,text.length(),rect);
        textHeight = rect.height();
        textWidth = rect.width();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        viewWidth = w;
        viewHeight = h;

        proWidth = (int) (viewWidth * 0.7);

    }



    public int getValue() {
        return value;
    }

    public int getCurrValue(){
        return currValue;
    }

    public void setValue(int value) {
        currValue = 0;
        currRatio = 0;
        this.value = value;
        start = true;
        invalidate();
    }


    public String getText() {
        return text;
    }

    public void setText(String text){
        this.text = text;
    }
}
