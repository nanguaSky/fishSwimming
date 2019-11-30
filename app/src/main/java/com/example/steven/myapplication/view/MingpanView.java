package com.example.steven.myapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.example.steven.myapplication.R;

/**
 * Created by Steven on 16/11/22.
 * 紫微命盘 中间的大盘显示
 */
public class MingpanView extends FrameLayout{

    public static final int VIEW_INFO = 1;
    public static final int VIEW_PAIPAN = 2;

    private int mState = 1;

    private Paint mPaint;

    private Point [] points;

    private Point onePoi;
    private Point twoPoi;
    private Point threePoi;
    private Point fourPoi;

    private int lineColor;      //连线颜色
    private int lineWidth = 2;  //连线宽度

    private int width;          //父宽度
    private int height;         //父高度
    private int meanWidth;      //均宽
    private int meanHeight;     //均高

    private View infoView;      //用户信息
    private View ppView;        //排盘信息


    public MingpanView(Context context) {
        this(context, null);
    }

    public MingpanView(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public MingpanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MingpanView);

        if(a != null){
            lineColor = a.getColor(R.styleable.MingpanView_mp_lineColor, Color.parseColor("#999999"));
        }

        setWillNotDraw(false);
        width = dip2px(2);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(lineWidth);
        mPaint.setColor(lineColor);
        mPaint.setStyle(Paint.Style.STROKE);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path = new Path();

        if(onePoi != null && mState == VIEW_PAIPAN){
            path.moveTo(onePoi.x,onePoi.y);
            path.lineTo(twoPoi.x,twoPoi.y);
            path.lineTo(threePoi.x,threePoi.y);
            path.lineTo(onePoi.x,onePoi.y);
            path.lineTo(fourPoi.x,fourPoi.y);
            canvas.drawPath(path,mPaint);

        }
    }


    /**
     * 设置命盘连线顺序
     */
    public void setOrder(int onePoi,int twoPoi,int threePoi,int fourPoi){
        this.onePoi = points[onePoi-1];
        this.twoPoi = points[twoPoi-1];
        this.threePoi = points[threePoi-1];
        this.fourPoi = points[fourPoi-1];
        invalidate();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;

        meanWidth = width / 4;
        meanHeight = height / 4;

        points = new Point[12];
        points[0] = new Point(0,0);
        points[1] = new Point(meanWidth,0);
        points[2] = new Point(meanWidth*3,0);
        points[3] = new Point(width,0);
        points[4] = new Point(width,meanHeight);
        points[5] = new Point(width,meanHeight*3);
        points[6] = new Point(width,height);
        points[7] = new Point(meanWidth*3,height);
        points[8] = new Point(meanWidth,height);
        points[9] = new Point(0,height);
        points[10] = new Point(0,meanHeight*3);
        points[11] = new Point(0,meanHeight);
    }


    @Override
    protected void onFinishInflate() {
        infoView = findViewById(R.id.ziwei_info);
        ppView = findViewById(R.id.ziwei_paipan);
    }


    public void switchView(int tag){
        mState = tag;
        if(mState == VIEW_INFO){
            if(infoView.getVisibility() == View.INVISIBLE){
                infoView.setVisibility(View.VISIBLE);
                ppView.setVisibility(View.INVISIBLE);
                invalidate();
            }
        }else{
            if(ppView.getVisibility() == View.INVISIBLE){
                ppView.setVisibility(View.VISIBLE);
                infoView.setVisibility(View.INVISIBLE);
                invalidate();
            }
        }

    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public View getInfoView() {
        return infoView;
    }

    public View getPpView() {
        return ppView;
    }
}
