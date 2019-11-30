package com.example.steven.myapplication.other.tarot;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.steven.myapplication.R;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Steven on 16/12/6.
 *
 * dealCard ();  发牌
 * foldCard ();  收牌
 */
public class TarotView extends FrameLayout implements Animation.AnimationListener, View.OnClickListener {

    private static final int HANDLE_REFRSH = 1;     //刷新布局
    private static final int HANDLE_RORATION = 2;     //旋转动画
    private final int INTERVAL = 15;                //重绘间隔时间

    private static final double SPEED = 0.007;
    private double progress = 0;

    private int selectFlag = 0;         // 0表示洗牌 1 表示放置第一张牌   2 表示放置第二张牌   3 表示放置第三张牌

    private int width = 0 ;
    private int height = 0;

    private int cardWidth;
    private int cardHeight;
    private int cardInterval = 0;               //牌的间距

    private boolean isInit = false;             //初始化标志
    private boolean isStart = false;             //开始标志
    private boolean isDealingCard = true;       //用于判断是发牌还是收牌

    private int x;
    private int y;
    private int lastX;
    private int lastY;

    private int dx ;
    private int dy ;

    private TarotManager mManager;

    private List<TarotCardView> cards;

    private Path mPath;

    private Paint mPaint;

    private PathMeasure pMeasure;

    private TarotCardView dragCard;     //被拖拽的牌
    private TarotCardView animCard;     //用于动画效果

    private ViewGroup selectLayou;
    private TarotCardView cardlOne;
    private TarotCardView cardlTwo;
    private TarotCardView cardlThree;

    private TextView tv1 ;
    private TextView tv2 ;
    private TextView tv3 ;


    /**
     * 牌的动画
     */
    private Animation selectCardAnim;
    private Animation outAnim;

    private MyHandler mHandler;




    private static class MyHandler extends Handler {

        WeakReference<TarotView> weakView;

        public MyHandler(TarotView view){
            weakView = new WeakReference<TarotView>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            TarotView v = weakView.get();
            if(v == null)
                return;

            switch (msg.what){
                //刷新界面
                case HANDLE_REFRSH:
                    v.requestLayout();
                    break;

                //旋转动画
                case HANDLE_RORATION:


                    break;
            }
        }
    }


    // TODO: 16/12/13
    private Subscription mSub;
    private int index = 0;
    private int animFlag = 0;
    private final int ANIM_FLAG_CIRCLE = 0;         //牌转圈圈
    private final int ANIM_FLAG_LEFT_RIGHT = 1;     //部分牌左右移
    private final int ANIM_FLAG_CENTER = 2;         //中间牌左移或右移
    private final int ANIM_FLAG_BACK_LEFT = 3;      //左边牌先回来
    private final int ANIM_FLAG_BACK_RIGHT = 4;      //右边牌回来

    //开始洗牌
    public void startShuffleAnim() {
        mSub = Observable.interval(50,50, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if(index < cards.size()){
                            TarotCardView card = cards.get(index);
                            RotateAnimation ra;
                            if(index % 2 == 0){
                                ra = new RotateAnimation(0,720,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,2.0f);
                                ra.setDuration(3000);
                            }else{
                                ra = new RotateAnimation(0,-720,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,2.0f);
                                ra.setDuration(3000);
                            }
                            if(index == cards.size() - 1)
                                ra.setAnimationListener(TarotView.this);

                            card.startAnimation(ra);
                            index ++;
                        }else{
                            mSub.unsubscribe();
                        }
                    }
                });


    }

    /**
     * 发牌
     */
    public void dealCard(){
        selectFlag = 1;
        isStart = true;
        isDealingCard = true;
        animCard.setVisibility(VISIBLE);
        requestLayout();
    }

    /**
     * 收牌
     */
    public void foldCard(){
        selectFlag = 0;
        isDealingCard = false;
        requestLayout();
    }


    public TarotView(Context context) {
        this(context, null);
    }

    public TarotView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TarotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {

        mHandler = new MyHandler(this);

        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.colorAccent));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
//        measureChild(selectLayou,widthMeasureSpec,heightMeasureSpec);
//        measureChild(tv1,widthMeasureSpec,heightMeasureSpec);
//        measureChild(tv2,widthMeasureSpec,heightMeasureSpec);
//        measureChild(tv3,widthMeasureSpec,heightMeasureSpec);
//        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath,mPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if(isStart && selectFlag == 1)
            NextAnimPos();

        if(!isInit && selectFlag == 1){
            layoutChild();
            isInit = true;
        }

        //检查是否让牌显示或隐藏
        if(selectFlag == 1)
            checkIsVisible();

        //初始化洗牌位置
        if(selectFlag == 0)
            initShufflePosition();


    }

    private void layoutChild(){
        for (TarotCardView card : cards) {
            int x = (int) card.getPos()[0];
            int y = (int) card.getPos()[1];
            card.layout(x - cardWidth / 2,y - cardHeight / 2,x + cardWidth / 2, y + cardHeight / 2);
        }

        LayoutParams lp = (LayoutParams) selectLayou.getLayoutParams();
        int left = lp.leftMargin;
        int top = (int)(height * 0.45);
        int right = left + width - lp.leftMargin * 2;
        int bottom = top + selectLayou.getMeasuredHeight() + cardHeight;

        selectLayou.layout(left,top,right,bottom);

        int w = (right - left) / 3;

        int left2 = width / 2 - cardWidth / 2;
        int top2 = top + (bottom - top - cardHeight) / 2;
        int right2 = left2 + cardWidth;
        int bottom2 = top2 + cardHeight;
        cardlTwo.layout(left2,top2,right2,bottom2);

        Rect rect = new Rect();
        cardlTwo.getGlobalVisibleRect(rect);

        int left1 = rect.left - w + cardWidth / 3;
        int top1 = top + (bottom - top - cardHeight) / 2;
        int right1 = left1 + cardWidth;
        int bottom1 = top1 + cardHeight;
        cardlOne.layout(left1,top1,right1,bottom1);

        int left3 = rect.left + w - cardWidth / 3;
        int top3 = top1;
        int right3 = left3 + cardWidth;
        int bottom3 = top3 + cardHeight;
        cardlThree.layout(left3,top3,right3,bottom3);
    }

    //检查是否让牌显示或隐藏
    private void checkIsVisible() {
        for (int i = 0; i < cards.size() - 1; i++) {
            TarotCardView card = cards.get(i);
            int posX = (int) card.getPos()[0];
            int animPosX = (int) animCard.getPos()[0];

            if(posX <= animPosX + 4 && posX >= animPosX - 4){

                //如果是发牌就显示
                if(isDealingCard){
                    if(card.getVisibility() == INVISIBLE)
                        card.setVisibility(VISIBLE);
                //收牌就隐藏
                }else{
                    if(card.getVisibility() == VISIBLE)
                        card.setVisibility(INVISIBLE);
                }
            }
        }
    }

    boolean temp = false;
    //将动画效果的那个牌移动到下一个点
    private void NextAnimPos(){
        float pos [] = new float[2];
        float tan [] = new float[2];

        if(isDealingCard){
            progress = progress < 1 ? progress + SPEED : 1;
            if(progress < 1){
                pMeasure.getPosTan((int) (pMeasure.getLength() * progress), pos, tan);
                float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI);
                animCard.setRotation(degrees);

                int x = (int) pos[0];
                int y = (int) pos[1];
                animCard.setPos(pos);
                animCard.layout(x - cardWidth / 2,y - cardHeight / 2,x + cardWidth / 2, y + cardHeight / 2);

                mHandler.sendEmptyMessageDelayed(HANDLE_REFRSH,INTERVAL);

            //发完牌了
            }else{
                if(!temp){
                    temp = true;
                    startSelectCardAnim(null,cardlOne);
                    ObjectAnimator.ofFloat(selectLayou,"Alpha",1.0f).setDuration(500).start();
                }
            }

        }else{
            progress = progress > 0 ? progress - SPEED : 0;
            if(progress > 0){
                pMeasure.getPosTan((int) (pMeasure.getLength() * progress), pos, tan);
                float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI);
                animCard.setRotation(degrees);

                int x = (int) pos[0];
                int y = (int) pos[1];
                animCard.setPos(pos);
                animCard.layout(x - cardWidth / 2,y - cardHeight / 2,x + cardWidth / 2, y + cardHeight / 2);

                mHandler.sendEmptyMessageDelayed(HANDLE_REFRSH,INTERVAL);
            }else{
                animCard.setVisibility(INVISIBLE);
            }
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                isChildClick(ev);
                lastX = x;
                lastY = y;
                return true;

            case MotionEvent.ACTION_MOVE:
               dx = x - lastX;
               dy = y - lastY;

                changeDragViewPosition(ev);

                break;

            case MotionEvent.ACTION_UP:
                if(dragCard != null){
                    dragCard.resumeCard();
                }

                //判断牌是否到达指定区域
                if(isReachPosition(dragCard)){
                    dragCard.setVisibility(INVISIBLE);
                    swipeSelectCardData(dragCard);
                }
                resumeCardPosition(dragCard);

                break;
        }

        lastX = x;
        lastY = y;

        return super.onTouchEvent(ev);
    }


    //判断子view是否被点击
    private void isChildClick(MotionEvent ev) {
        boolean flag = false;
        for(TarotCardView card : cards){
            if(card.isSelected()){
                card.setSelected(false);
                dragCard = card;
                flag = true;
            }
        }
        if(!flag){
            dragCard = null;
        }

        if(dragCard != null){
            dragCard.setRotation(0);
        }
    }

    //是否到达牌停放的位置
    private boolean isReachPosition(View view){
        if (view == null)
            return false;

        if(selectFlag == 1){
            return isOverlap(view,cardlOne);
        }

        if(selectFlag == 2){
            return isOverlap(view,cardlTwo);
        }

        if(selectFlag == 3){
            return isOverlap(view,cardlThree);
        }
        return false;
    }


    //两个区域是否重叠
    private boolean isOverlap(View touchView , View targetView){
        Rect touchR = new Rect();
        Rect targetR = new Rect();
        touchView.getGlobalVisibleRect(touchR);
        targetView.getGlobalVisibleRect(targetR);
        return targetR.contains(touchR.centerX(),touchR.centerY());
    }


    //交换两张牌的数据
    private void swipeSelectCardData(TarotCardView touchCard){
        if (touchCard == null)
            return ;

        TarotCardView targetView = null;
        if(selectFlag == 1){
            targetView = cardlOne;
            nextLogicProcess();
            swipeData(touchCard,targetView);
            return;
        }

        if(selectFlag == 2){
            targetView = cardlTwo;
            nextLogicProcess();
            swipeData(touchCard,targetView);
            return;
        }

        if(selectFlag == 3){
            targetView = cardlThree;
            nextLogicProcess();
            swipeData(touchCard,targetView);
            return;
        }


    }

    private void swipeData(TarotCardView touchCard , TarotCardView targetView){
        //交换两张牌的数据
        if(targetView != null){
            TarotBean bean = touchCard.getTarotBean();
            targetView.setCardBean(bean);

            TarotCardView lastTag = (TarotCardView) targetView.getTag();
            //如果没有选择牌
            if(lastTag == null){
                targetView.setTag(touchCard);

                //如果选择了牌  将原来的牌显示出来
            }else{
                lastTag.setVisibility(VISIBLE);
                targetView.setTag(touchCard);
            }
        }
    }


    //改变牌位置
    private void changeDragViewPosition(MotionEvent ev){

        if(dragCard != null){
            int l = dragCard.getLeft() + dx;
            int t = dragCard.getTop() + dy;
            int r = l + cardWidth;
            int b = t + cardHeight;
            dragCard.layout(l,t,r, b);
        }

    }

    //恢复牌原来的位置
    private void resumeCardPosition(TarotCardView card){
        if(card == null)
            return;

        int x = (int) card.getPos()[0];
        int y = (int) card.getPos()[1];
        card.layout(x - cardWidth / 2,y - cardHeight / 2,x + cardWidth / 2, y + cardHeight / 2);
    }


    //开始选牌的动画
    private void startSelectCardAnim(View lastSelectCard , View currSelectCard){
        if(selectCardAnim == null){
            selectCardAnim = AnimationUtils.loadAnimation(getContext(),R.anim.alpha_tarot_anim);
        }
        if(lastSelectCard != null){
            Animation lastAnim = lastSelectCard.getAnimation();
            if(lastAnim != null){
                lastAnim.cancel();
                lastSelectCard.clearAnimation();
            }

        }

        if(currSelectCard != null){
            if(currSelectCard.getVisibility() == INVISIBLE){
                currSelectCard.setVisibility(VISIBLE);
            }
            currSelectCard.startAnimation(selectCardAnim);
        }
    }


    //所有多余的牌退出动画
    private void outAllCardAnim(){
        outAnim = AnimationUtils.loadAnimation(getContext(),R.anim.alpha_tarot_out_anim);
        outAnim.setAnimationListener(this);
        for(TarotCardView card : cards){
            card.startAnimation(outAnim);
        }
    }

    //隐藏完了所有的牌，让三张牌整体上移(属性动画)
    private void outSelectLayoutAnim() {
        ObjectAnimator oba = ObjectAnimator
                                .ofFloat(selectLayou,"translationY",-(int)(height * 0.3))
                                .setDuration(1000);
        oba.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                selectLayou.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                nextLogicProcess();
            }
        });
        oba.start();
        upTranslationAnim(cardlOne,"translationY",-(int)(height * 0.3));
        upTranslationAnim(cardlTwo,"translationY",-(int)(height * 0.3));
        upTranslationAnim(cardlThree,"translationY",-(int)(height * 0.3));
    }

    //向上移动动画
    private void upTranslationAnim(View view, String animType, float ... value){
        ObjectAnimator
            .ofFloat(view,animType,value)
            .setDuration(1000)
            .start();
    }

    //第一张被选中的牌下移
    private void placeTriangle(){
        ObjectAnimator oba = ObjectAnimator
                .ofFloat(cardlOne,"translationY",-(int)(height * 0.3) + cardHeight + cardHeight / 2)
                .setDuration(1000);
        oba.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                moveLeftCardTwo();
            }
        });
        oba.start();

    }

    //将第二张选中的牌左移,第一张被选中的牌右移
    private void moveLeftCardTwo(){
        int oldCardTwoLeft = cardlTwo.getLeft();
        int offsetX = oldCardTwoLeft - cardlOne.getLeft();
        ObjectAnimator oba = ObjectAnimator
                .ofFloat(cardlTwo,"translationX",- offsetX)
                .setDuration(1000);
        oba.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                showText();
            }
        });
        oba.start();

        int oldPos = cardlOne.getLeft();
        int oneOffset = width / 2 - cardWidth / 2 - oldPos;
        ObjectAnimator obaOne = ObjectAnimator
                .ofFloat(cardlOne,"translationX",oneOffset)
                .setDuration(1000);
        obaOne.start();
    }


    private void showText(){
        tv1.setText("过去");
        tv2.setText("现在");
        tv3.setText("未来");

        Rect r1 = new Rect();
        cardlOne.getGlobalVisibleRect(r1);

        Rect r2 = new Rect();
        cardlTwo.getGlobalVisibleRect(r2);

        Rect r3 = new Rect();
        cardlThree.getGlobalVisibleRect(r3);

        int l = r1.centerX() - tv1.getMeasuredWidth() / 2;
        int t = r1.top+ 5;
        int r = l + tv1.getMeasuredWidth();
        int b = t + tv1.getMeasuredHeight();
        tv1.layout(l,t,r,b);

        l = r2.centerX() - tv2.getMeasuredWidth() / 2;
        t = r2.top + 5;
        r = l + tv2.getMeasuredWidth();
        b = t + tv2.getMeasuredHeight();
        tv2.layout(l,t,r,b);

        l = r3.centerX() - tv3.getMeasuredWidth() / 2;
        t = r3.top + 5;
        r = l + tv3.getMeasuredWidth();
        b = t + tv3.getMeasuredHeight();
        tv3.layout(l,t,r,b);

    }


    /**
     * ======================
     * 塔罗牌逻辑流程
     * ======================
     */
    private void nextLogicProcess(){

        //抽完第一张牌
        if(selectFlag == 1){
            selectFlag = 2;
            startSelectCardAnim(cardlOne,cardlTwo);
            return ;
        }

        //抽完第二张牌
        if(selectFlag == 2){
            selectFlag = 3;
            startSelectCardAnim(cardlTwo,cardlThree);
            return ;
        }

        //抽完第三张牌
        if(selectFlag == 3){
            selectFlag = 4;
            startSelectCardAnim(cardlThree,null);
        }

        //隐藏所有多余的牌
        if(selectFlag == 4){
            selectFlag = 5;
            outAllCardAnim();
            return ;
        }

        //隐藏完了所有的牌，让三张牌整体上移(属性动画)
        if(selectFlag == 5){
            selectFlag = 6;
            outSelectLayoutAnim();
            return ;
        }

        //上移完成所有的牌,让牌摆三角形
        if(selectFlag == 6){
            selectFlag = 7;
            placeTriangle();
        }

    }


    // TODO: 16/12/13  
    /**
     * =======================
     * 动画监听
     * =======================
     */
    @Override
    public void onAnimationStart(Animation animation) {}

    @Override
    public void onAnimationRepeat(Animation animation) {}

    @Override
    public void onAnimationEnd(Animation animation) {

        //隐藏完了所有的牌，让三张牌整体上移(属性动画)
        if(outAnim instanceof Animation){
            for(View v : cards){
                v.setVisibility(INVISIBLE);
            }
            nextLogicProcess();
        }

        switch (animFlag){
            //牌转圈圈
            case ANIM_FLAG_CIRCLE:
                //隐藏所有牌先
                for(TarotCardView card : cards){
                    card.setVisibility(INVISIBLE);
                }
                animLeftOrRight();
                break;
        }

    }

    //部分牌左右移
    private void animLeftOrRight(){
        animFlag = 1;
        TarotCardView leftCard = cards.get(0);
        leftCard.setVisibility(VISIBLE);
        TarotCardView rightCard = cards.get(1);
        rightCard.setVisibility(VISIBLE);
        final TarotCardView center = cards.get(2);
        center.setVisibility(VISIBLE);

        ObjectAnimator.ofFloat(leftCard,"translationX",-cardHeight)
        .setDuration(1000)
        .start();

        ObjectAnimator obj = ObjectAnimator.ofFloat(rightCard,"translationX",cardHeight)
                .setDuration(1000);
        obj.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                centerLeftOrRight(center);
            }
        });
        obj.start();


    }

    private int centerFlag;
    //中间牌左移或右移
    private void centerLeftOrRight(TarotCardView center){
        centerFlag = new Random().nextInt(2);
        ObjectAnimator obj;
        if(centerFlag % 2 ==0){
            obj = ObjectAnimator.ofFloat(center,"translationX",-cardHeight)
                    .setDuration(1000);
        }else{
            obj = ObjectAnimator.ofFloat(center,"translationX",cardHeight)
                    .setDuration(1000);
        }

        obj.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                leftBack();
            }
        });
        obj.start();
    }

    //左边牌先回来
    private void leftBack(){
        TarotCardView leftCard = cards.get(0);
        ObjectAnimator obj = ObjectAnimator.ofFloat(leftCard,"translationX",0)
                .setDuration(1000);
        obj.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                rightBack();
            }
        });
        obj.start();

        if(centerFlag % 2 == 0){
            TarotCardView center = cards.get(2);
            ObjectAnimator.ofFloat(center,"translationX",0)
                    .setDuration(1000)
                    .start();
        }
    }


    //右边牌回来
    private void rightBack(){
        TarotCardView rightCard = cards.get(1);
        ObjectAnimator obj = ObjectAnimator.ofFloat(rightCard,"translationX",0)
                .setDuration(1000);
        obj.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                moveToInitLocation();
            }
        });
        obj.start();

        if(centerFlag % 2 != 0){
            TarotCardView center = cards.get(2);
            ObjectAnimator.ofFloat(center,"translationX",0)
                    .setDuration(1000)
                    .start();
        }
    }

    //将牌移动到发牌位置
    private void moveToInitLocation() {
        for (int i = 0; i < 3; i++) {
            cards.get(i).setVisibility(INVISIBLE);
        }
        float [] pos = new float[2];
        float [] tan = new float[2];
        pMeasure.getPosTan(0,pos,tan);

        TarotCardView card = cards.get(0);
        card.setVisibility(VISIBLE);

        int offsetX = (int) (card.getLeft() - pos[0]) + cardWidth / 2;
        int offsetY = (int) (card.getTop() - pos[1]) + cardHeight / 2;
        float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI);
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator objR = ObjectAnimator.ofFloat(card,"Rotation",degrees).setDuration(500);
        ObjectAnimator objY = ObjectAnimator.ofFloat(card,"translationY",-offsetY).setDuration(1000);
        ObjectAnimator objX = ObjectAnimator.ofFloat(card,"translationX",-offsetX).setDuration(1000);
        objR.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //初始化发牌位置
                initAllCardPosition();
                dealCard();
            }
        });
        set.playSequentially(objX,objY,objR);
        set.start();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
        mPath = getPath();

    }

    private Path getPath(){
        int startX = 0;
        int startY = (int) (height * 0.15);

        Path path = new Path();
        RectF rectF = new RectF(startX,startY,width,width + startY);
        path.addArc(rectF,-140,100);
        pMeasure = new PathMeasure(path,false);
        cardInterval = (int) (pMeasure.getLength() / (cards.size() - 1));
        return path;
    }

    //初始化发牌的位置
    private void initAllCardPosition(){
        for (int i = 0; i < cards.size(); i++) {
            TarotCardView card = cards.get(i);
            float pos [] = new float[2];
            float tan [] = new float[2];
            pMeasure.getPosTan(i * cardInterval, pos, tan);

            card.setPos(pos);
            card.setTan(tan);
        }

    }

    //初始化洗牌位置
    private void initShufflePosition(){

        int left = width / 2 - cardWidth / 2;
        int top = (int) (height * 0.15 - cardHeight / 2);
        for(TarotCardView card : cards){
            card.layout(left,top,left + cardWidth,top + cardHeight);
        }
    }


    @Override
    protected void onFinishInflate() {
        //selectLayou = (ViewGroup) findViewById(R.id.rl_tarot_select);
        cardlOne = new TarotCardView(getContext());
        cardlTwo = new TarotCardView(getContext());
        cardlThree = new TarotCardView(getContext());

//        tv1 = (TextView) findViewById(R.id.tv_tarot_one);
//        tv2 = (TextView) findViewById(R.id.tv_tarot_two);
//        tv3 = (TextView) findViewById(R.id.tv_tarot_three);

        selectLayou.setAlpha(0);

        cardlOne.setVisibility(INVISIBLE);
        cardlTwo.setVisibility(INVISIBLE);
        cardlThree.setVisibility(INVISIBLE);

        addView(cardlOne);
        addView(cardlTwo);
        addView(cardlThree);

        cardlOne.setOnClickListener(this);
        cardlTwo.setOnClickListener(this);
        cardlThree.setOnClickListener(this);

        mManager = new TarotManager(getContext());
        cards = mManager.createTarotCard();

        for (int i = 0; i < cards.size(); i++) {
            TarotCardView card = cards.get(i);
            addView(card);
        }
        animCard = cards.get(cards.size() - 1);
        cardWidth = animCard.getCardWidth();
        cardHeight = animCard.getCardHeight();
    }

    @Override
    protected void onDetachedFromWindow() {
        if(mHandler != null){
            mHandler.removeMessages(HANDLE_REFRSH);
            mHandler = null;
        }

        if(selectCardAnim != null){
            selectCardAnim.cancel();
            selectCardAnim = null;
        }

        if(outAnim != null){
            outAnim.cancel();
            outAnim = null;
        }

        if(mSub != null){
            mSub.unsubscribe();
        }
        super.onDetachedFromWindow();
    }

    @Override
    public void onClick(View v) {
        TarotCardView card = (TarotCardView) v;
        if(selectFlag != 7)
            return;

        if(tarotListener != null){
            if(card == cardlOne)
                card.getTarotBean().setPaiyi("代表过去状况");

            if(card == cardlTwo)
                card.getTarotBean().setPaiyi("代表现在状况");

            if(card == cardlThree)
                card.getTarotBean().setPaiyi("代表未来状况");

            tarotListener.onTarotClick(card);
        }

    }

    private OnTarotClickListener tarotListener;

    public interface OnTarotClickListener{
        void onTarotClick(TarotCardView card);
    }


    public void setOnTarotClickListener(OnTarotClickListener tarotListener) {
        this.tarotListener = tarotListener;
    }
}
