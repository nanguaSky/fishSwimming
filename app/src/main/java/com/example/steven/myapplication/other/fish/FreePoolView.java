package com.example.steven.myapplication.other.fish;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.steven.myapplication.bean.FishBean;
import com.example.steven.myapplication.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steven on 16/11/29.
 * 放生池
 *
 * addFish(FishBean fishBean)  添加一只鱼
 */
public class FreePoolView extends ViewGroup implements View.OnClickListener{

    private static final int HANDLE_REFRSH = 1;     //刷新鱼的游动
    private static final int HANDLE_ADD = 0;        //添加新的鱼

    private final int FISH_COME_TIME = 5 * 100 ;        //鱼出场间隔
    private final int INTERVAL = 10;                //重绘间隔时间

    private int width;

    private int height;

    private static int fishW;

    private static int fishH;

    private Paint mPaint;

    private List<FishModel> fishs;

    private FishManager fManager;

    private MyHandler mHandler;

    public FreePoolView(Context context) {
        this(context, null);

    }

    public FreePoolView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public FreePoolView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setWillNotDraw(false);
        mHandler = new MyHandler(this);
        fishs = new ArrayList<FishModel>();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(getResources().getColor(R.color.colorAccent));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if(fishs != null && fishs.size()!= 0){

            for(int i = 0 ; i < fishs.size() ; i++){
                FishModel fish = fishs.get(i);
                Point poi = fish.getNextMovePoi();

                if(poi != null){
                    fish.layout(poi.x,poi.y-fishH,poi.x+fishW,poi.y);
                }else{
                    fish.setPath(fManager.getRandomPath());
                    poi = fish.getNextMovePoi();
                    fish.layout(poi.x,poi.y-fishH,poi.x+fishW,poi.y);
                }
            }
            mHandler.sendEmptyMessageDelayed(HANDLE_REFRSH,INTERVAL);
        }
    }


    /**
     * 添加一只鱼
     */
    public void addFish(FishBean fishBean){
        Message msg = Message.obtain(mHandler,HANDLE_ADD,fishBean);
        mHandler.sendMessageDelayed(msg,FISH_COME_TIME);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w,h,oldw,oldh);
        width = w;
        height = h;

        if(fManager == null){
            fManager = new FishManager(getContext(),w,h);
        }
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(), "鱼: 点你妹啊", Toast.LENGTH_SHORT).show();
    }


    private static class MyHandler extends Handler{

        WeakReference<FreePoolView> weakView;

        public MyHandler(FreePoolView view){
            weakView = new WeakReference<FreePoolView>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            FreePoolView v = weakView.get();
            if(v == null)
                return;

            switch (msg.what){
                 //刷新鱼游动
                 case HANDLE_REFRSH:
                     v.requestLayout();
                     break;

                 //往池子里面加鱼
                case HANDLE_ADD:
                    FishBean bean = (FishBean) msg.obj;
                    FishModel model = v.getfManager().createFish(bean);
                    fishW = model.getFishWidth();
                    fishH = model.getFishHeight();
                    model.setOnClickListener(v);
                    v.getFishs().add(model);
                    v.addView(model);
                    model.start();
                    break;
            }

        }
    }


    @Override
    protected void onDetachedFromWindow() {
        if(mHandler != null){
            mHandler.removeMessages(HANDLE_REFRSH);
            mHandler.removeMessages(HANDLE_ADD);
            mHandler = null;
        }
        if(fishs != null && fishs.size() != 0){
            for(FishModel fish : fishs){
                fish.stop();
            }
        }
        super.onDetachedFromWindow();
    }

    public FishManager getfManager() {
        return fManager;
    }

    public List<FishModel> getFishs() {
        return fishs;
    }
}
