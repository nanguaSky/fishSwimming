package com.example.steven.myapplication.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.View;

import com.example.steven.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by 伍玉南 on 16/12/15.
 * 求签view
 * setOnShakeSkewerListener  只需要设置这个监听就好
 */
public class AskSkewerView extends View implements SensorEventListener{

    private static final int MAX_ROTATING = 16;   //最大可旋转角度
    private SensorManager sensor;       //传感器

    private Random random;

    private Bitmap bitTop;

    private Bitmap bitTong;
    private Bitmap bitSkewer;
    private int width;

    private int maxLeft = 0;
    private int maxRight = 0;
    private int height;
    private static int rotating = 0;
    private int skewerNum = 11;

    private List<Point> points;

    private Paint mPaint;

    public AskSkewerView(Context context) {
        this(context, null);
    }

    public AskSkewerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AskSkewerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        
        init();
    }



    private int INTERVAL = 20;
    private static boolean flag = false;
    private boolean auto = false;

    //自动摇签
    public void AutoShake(){
        unregisterSensor();

        auto = true;
        invalidate();

    }

    public void registerSensor(){
        sensor.registerListener(this,sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_UI);
    }

    public void unregisterSensor(){
        if(sensor != null){
            sensor.unregisterListener(this);
        }
    }

    //停止摇签
    public void stopShake(){
        unregisterSensor();
    }



    private OnShakeSkewerListener skewerListener;
    /**
     * 摇签监听
     */
    public interface OnShakeSkewerListener{
        //摇签完成回调
        void onShakeFinish();
    }

    private void init() {
        random = new Random();

        bitTop = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.qiuqian_gaizi);
        bitTong = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.qiuqian_tong);
        bitSkewer = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.qiuqian_qian);

        width = bitTong.getWidth();
        height = bitSkewer.getHeight() / 2 + bitTong.getHeight();


        int temp = 0;
        points = new ArrayList<Point>();
        for (int i = 0; i < skewerNum; i++) {
            int ran = random.nextInt(10);
            temp += 5 + ran;
            points.add(new Point(temp,0));
        }

        mPaint = new Paint();

        sensor = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        registerSensor();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTop(canvas);
        drawSkewer(canvas);
        drawTong(canvas);


    }

    // TODO: 16/12/16
//    private void autoShake() {
//        if(!flag && rotating > -MAX_ROTATING){
//            rotating --;
//            postInvalidateDelayed(INTERVAL);
//        }
//
//        if(!flag && rotating <= -MAX_ROTATING){
//            flag = true;
//        }
//
//        if(flag && rotating >= MAX_ROTATING){
//            flag = false;
//        }
//
//        if(flag && rotating < MAX_ROTATING){
//            rotating ++;
//            postInvalidateDelayed(INTERVAL);
//        }
//
//        Log.d("AskSkewerView", "rotating   :  " + rotating);
//    }

    private void drawTop(Canvas canvas){
        int left = width / 2 - bitTong.getWidth() / 2;
        int top = height - bitTong.getHeight() - bitTop.getHeight() / 2;
        canvas.drawBitmap(bitTop,left,top,mPaint);
    }

    private void drawTong(Canvas canvas) {
        int left = width / 2 - bitTong.getWidth() / 2;
        int top = height - bitTong.getHeight();
        canvas.drawBitmap(bitTong,left,top,mPaint);
    }

    private void drawSkewer(Canvas canvas) {
        int top = - bitSkewer.getHeight() + bitSkewer.getWidth();

        int rand = random.nextInt(10);
        for (int i=0 ; i<skewerNum ; i++) {
            if(rand != i){
                Point point = points.get(i);
                int left = width / 2 - bitTong.getWidth() / 2 + bitSkewer.getWidth() * 2 - rotating * 3 + point.x;
                canvas.save();
                if(left > maxRight){
                    left = maxRight;
                }
                if(left < maxLeft){
                    left = maxLeft;
                }

                canvas.translate(left,bitSkewer.getHeight());
                canvas.rotate(- rotating );
                canvas.drawBitmap(bitSkewer,0 ,top,mPaint);
                canvas.restore();
            }


        }

    }

    private int lastRot;
    private int TriggerValue = 15;      // 触发监听值
    private int TriggerCount = 2;      // 触发次数
    private int count = 0;
    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();

        float[] values = event.values;          //values[0]:X轴，values[1]：Y轴，values[2]：Z轴
        lastRot =  (int) values[0];

        if (sensorType == Sensor.TYPE_ACCELEROMETER && !auto) {
            if(lastRot != rotating){
                rotating = lastRot;
                if (Math.abs(-rotating * 2) <= MAX_ROTATING) {
                    invalidate();
                }


                if(Math.abs(rotating) > TriggerValue){
                    count ++;
                    if(count == TriggerCount && skewerListener != null){
                        count = 0;
                        skewerListener.onShakeFinish();
                    }
                }

            }
        }


    }

    //当传感器精度改变时回调该方法，Do nothing.
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = bitTong.getWidth();
        maxLeft = width / 2 - bitTong.getWidth() / 2 + bitSkewer.getWidth() * 2;
        maxRight = width / 2 + bitTong.getWidth() / 2 - bitSkewer.getWidth() * 3;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unregisterSensor();

    }

    public void setOnShakeSkewerListener(OnShakeSkewerListener skewerListener) {
        this.skewerListener = skewerListener;
    }
}
