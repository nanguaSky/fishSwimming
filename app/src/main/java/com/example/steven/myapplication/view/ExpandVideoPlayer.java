package com.example.steven.myapplication.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;


import com.example.steven.myapplication.JZMediaIjk;

import java.lang.reflect.Constructor;

import cn.jzvd.JZDataSource;
import cn.jzvd.JZMediaInterface;
import cn.jzvd.JZMediaSystem;
import cn.jzvd.JZTextureView;
import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * 来自 伍玉南 的装逼小尾巴 on 18/2/8.
 * 自定义UI可以说是用处最多的，比如标题的显示与否，添加分享按钮，全屏显示电量等任何的DEMO UI不一样的修改都可以总结为自定义UI。
 * 和自定义相关的工作，最主要是先继承JCVideoPlayerStandard！！！
 * 修改xml
 * <p>
 * 在R.layout.jc_layout_standard的基础上，添加自己想要的控制，不需要的控件不能删除，如果删除代码findViewById找不见会报错，只能隐藏。
 * <p>
 * 取得新控件的引用
 * <p>
 * 复写getLayoutId函数，设置自己的xml布局，全屏和非全屏是一个xml布局，只是有的控件全屏显示，非全屏隐藏。
 * <p>
 * 复写init函数，findViewById找到自己添加的控件。
 * <p>
 * 操作控件
 * <p>
 * 根据自己的需要，复写进入状态的函数，代码中应该是不厌其烦的分别控制每个状态的控件，这样做思路清晰，不会出现遗漏。
 * <p>
 * onStateNormal
 * onStatePreparing
 * onStatePreparingChangingUrl
 * onStatePlaying
 * onStatePause
 * onStatePlaybackBufferingStart
 * onStateError
 * onStateAutoComplete
 */

public class ExpandVideoPlayer extends JzvdStd {


    public ExpandVideoPlayer(Context context) {
        super(context);
    }


    public ExpandVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void init(Context context) {
        super.init(context);
        initView();
    }



    /**
     * 根据项目初始化view状态
     */
    private void initView() {
        if (batteryTimeLayout != null) {
            batteryTimeLayout.setVisibility(GONE);
        }

        if (screen == SCREEN_TINY) {
            bottomContainer.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }

        startButton.setVisibility(GONE);
        topContainer.setVisibility(View.GONE);
        bottomContainer.setVisibility(View.GONE);
        loadingProgressBar.setVisibility(View.GONE);
        mRetryLayout.setVisibility(View.GONE);

        fullscreenButton.setOnClickListener(this);
    }


    public static void startFullscreenDirectly(Context context, Class _class, String url) {
        JZUtils.hideStatusBar(context);
        JZUtils.hideSystemUI(context);

        ViewGroup vp = (ViewGroup) JZUtils.scanForActivity(context).getWindow().getDecorView();
        try {
            Constructor<Jzvd> constructor = _class.getConstructor(Context.class);
            final Jzvd jzvd = constructor.newInstance(context);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            vp.addView(jzvd, lp);

            jzvd.setUp(new JZDataSource(url, ""), JzvdStd.SCREEN_FULLSCREEN, JZMediaIjk.class);
            jzvd.startVideo();
            Jzvd.CURRENT_JZVD = jzvd;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
