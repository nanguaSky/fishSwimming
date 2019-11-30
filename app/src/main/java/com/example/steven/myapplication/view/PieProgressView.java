package com.example.steven.myapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.steven.myapplication.R;

/**
 * 来自 伍玉南 的装逼小尾巴 on 17/12/16.
 * 饼状进度条
 */
public class PieProgressView extends View{

    private int color;

    private Paint circlrPaint;      //外圈圆的画笔

    private Paint piePaint;         //饼状图的画笔

    private RectF mRectF;

    private int mXCenter;

    private int mYCenter;

    private int mProgress;

    private int radius;

    private int width;

    private int height;

    public PieProgressView(Context context) {
        this(context, null);
    }

    public PieProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    private void init(AttributeSet attrs){
        TypedArray t = getContext().obtainStyledAttributes(attrs, R.styleable.PieProgress, 0, 0);
        color = getResources().getColor(R.color.colorAccent);

        if(attrs != null){
            color = t.getColor(R.styleable.PieProgress_pieColor, color);
            radius = (int) t.getDimension(R.styleable.PieProgress_pieRadius, 20);
            mProgress = t.getInt(R.styleable.PieProgress_pieValue, 0);

        }else{

        }

        piePaint = new Paint();
        piePaint.setAntiAlias(true);
        piePaint.setColor(color);
        piePaint.setStyle(Paint.Style.STROKE);
        piePaint.setStrokeWidth(5);

        circlrPaint = new Paint();
        circlrPaint.setColor(color);
        circlrPaint.setAntiAlias(true);

        mRectF = new RectF();

        t.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mProgress > 0 ) {
            mRectF.left = (mXCenter - radius);
            mRectF.top = (mYCenter - radius);
            mRectF.right = radius * 2 + (mXCenter - radius);
            mRectF.bottom = radius * 2 + (mYCenter - radius);
            canvas.drawArc(mRectF, -90, ((float)mProgress / 100) * 360, true, piePaint); //
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        if(radius == 0){
            radius = Math.max(w, h) / 2;
        }

        mXCenter = getWidth() / 2;
        mYCenter = getHeight() / 2;
    }

    public void setProgress(int progress) {
        mProgress = progress;
        invalidate();
    }
}
