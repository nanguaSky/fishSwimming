package com.example.steven.myapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.steven.myapplication.R;

/**
 * 带动画过渡的圆形进度条
 * Created by 伍玉南 on 16/10/24.
 * xmlns:app="http://schemas.android.com/apk/res-auto"
 *
 * 调用setValue()即可 or xml 设置 value  0<= value <= 100；
 *
 * xml中  view 控件高度取决于radius， 宽高均设置为wrap_content。
 */
public class CircularProgress extends View {

    private final int TIME_INTERVAL = 20;  //重绘时间间隔

    private final int TEXT_INTERVAL = 20;  //文字与数字间隔

    private int viewWidth;          //控件宽度

    private int viewHeight;         //控件高度


    /**
     * 背景相关
     */
    private Paint aPaint;           //最外圈半透明背景

    private Paint cPaint;           //圆形背景

    private float aRadius;

    private int alphaColor;

    private int circularColor;      //圆形颜色


    /**
     * 圆弧相关
     */
    private float proWidth = 2;       //进度条宽度

    private float radius;             //半径

    private int value;              //进度值

    private int currValue = 0;      //当前进度值

    private int currRatio = 0;      //当前进度百分比

    private int proColor;           //进度条颜色

    private Paint mPaint;           //圆弧的画笔

    private boolean start = false;


    /**
     *  文本相关
     */
    private int textSize;

    private float textWidth = -1;          //文本宽度

    private float textHeight = -1;         //文本高度

    private Paint numPaint;                //数字的画笔

    private Paint textPaint;               //文本的画笔

    private String text;

    private String fen = "分";


    public CircularProgress(Context context) {
        this(context, null);

    }public CircularProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }public CircularProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }



    private void init(AttributeSet attrs){
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CircularProgress);

        if(a != null){
            value = a.getInt(R.styleable.CircularProgress_value,0);
            radius = a.getDimensionPixelSize(R.styleable.CircularProgress_radius,viewWidth / 2);
            proWidth = a.getDimensionPixelSize(R.styleable.CircularProgress_proWidth,2);
            textSize = a.getDimensionPixelSize(R.styleable.CircularProgress_textSize,14);
            proColor = a.getColor(R.styleable.CircularProgress_proColor,getContext().getResources().getColor(R.color.colorAccent));
            circularColor = a.getColor(R.styleable.CircularProgress_circularColor,0);
        }

        if(circularColor == 0){
            circularColor = Color.parseColor("#ffffff");
        }

        aRadius = radius / 2 + proWidth;

        mPaint = new Paint();
        mPaint.setStrokeWidth(proWidth);
        mPaint.setColor(proColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);

        cPaint = new Paint();
        cPaint.setColor(circularColor);
        cPaint.setAntiAlias(true);

        aPaint = new Paint();
        aPaint.setColor(circularColor);
        aPaint.setAntiAlias(true);
        aPaint.setAlpha(150);

        numPaint = new Paint();
        numPaint.setColor(proColor);
        numPaint.setAntiAlias(true);
        numPaint.setTextSize(textSize);

        textPaint = new Paint();
        textPaint.setColor(proColor);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize / 2 + 5);

        if(value != 0){
            start = true;
        }

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension((int)(radius+ proWidth * 2), (int)(radius + proWidth * 2));
    }



    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.restore();

        //绘制背景
        canvas.drawCircle(viewWidth/2,viewWidth/2,radius/2-proWidth/2,cPaint);
        //最外层背景
        canvas.drawCircle(viewWidth/2,viewWidth/2,aRadius,aPaint);

        //绘制圆弧
        Path path = new Path();
        RectF rectF = new RectF(proWidth*2,proWidth*2,radius,radius);
        path.addArc(rectF,-92,currRatio);
        canvas.drawPath(path,mPaint);

        //绘制文本
        text = String.valueOf(currValue);
        measureTextSize();
        canvas.drawText(text,viewWidth / 2 - textWidth + TEXT_INTERVAL , viewHeight / 2 + textHeight / 2,numPaint);
        canvas.drawText(fen,viewWidth/2 + TEXT_INTERVAL * 2,viewHeight / 2 + textHeight / 2 - 5,textPaint);

        if(currValue < value && start){
            currValue += 1;
            float temp = (float) currValue;
            currRatio = (int) (360 * (temp / 100));
            postInvalidateDelayed(TIME_INTERVAL);
        }

    }



    //测量文字宽高
    private void measureTextSize(){
        Rect rect = new Rect();
        numPaint.getTextBounds(text,0,text.length(),rect);
        textHeight = rect.height();
        textWidth = rect.width();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        viewWidth = w;
        viewHeight = h;
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

}
