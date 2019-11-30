package com.example.steven.myapplication.other.tarot;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.steven.myapplication.R;

/**
 * Created by Steven on 16/12/6.
 * 塔罗牌
 */
public class TarotCardView extends ImageView {

    private Drawable bg;    //牌的背面
    private Drawable openBg;    //牌的正面

    private TarotBean mBean;

    private float [] pos;
    private float [] tan;

    private int cardWidth;
    private int cardHeight;

    private boolean isOpen = false;

    public TarotCardView(Context context) {
        this(context,null);
    }

    public TarotCardView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TarotCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        
        init();
    }

    private void init() {
        bg = getResources().getDrawable(R.drawable.triangle_card);
        cardWidth = bg.getMinimumWidth();
        cardHeight = bg.getMinimumHeight();

        FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(param);
        setImageDrawable(bg);

    }

    /**
     * 翻牌
     */
    public void switchBg(){
        if(!isOpen){
            isOpen = true;
            setImageDrawable(openBg);
        }else{
            isOpen = false;
            setImageDrawable(bg);
        }
        invalidate();
    }

    public void setCardBean(TarotBean bean){
        mBean = bean;
        openBg = getResources().getDrawable(bean.getImageId());
    }

    //根据正切值 旋转卡牌
    public void resumeCard(){
        float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI);
        setRotation(degrees);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                setSelected(true);
                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:
                break;
        }

        return super.onTouchEvent(ev);
    }

    public int getCardWidth() {
        return cardWidth;
    }

    public int getCardHeight() {
        return cardHeight;
    }

    public float[] getTan() {
        return tan;
    }

    public void setTan(float[] tan) {
        this.tan = tan;
        resumeCard();
    }

    public float[] getPos() {
        return pos;
    }

    public void setPos(float[] pos) {
        this.pos = pos;
    }

    public TarotBean getTarotBean() {
        return mBean;
    }

    public void setTarotBean(TarotBean mBean) {
        this.mBean = mBean;
    }

    public boolean isOpen() {
        return isOpen;
    }
}
