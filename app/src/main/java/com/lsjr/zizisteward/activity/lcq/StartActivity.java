package com.lsjr.zizisteward.activity.lcq;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.lsjr.zizisteward.R;

public class StartActivity extends Activity {
    private static final int ANIMATION_DURATION = 1000;
    private static final int sleepTime = 1500;
    private ImageView splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        splash = (ImageView) findViewById(R.id.splash);
        playAnimation();
        initStart();
    }

    /*逻辑跳转*/
    private void initStart() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 200);


    }


    private void playAnimation() {
        AnimationSet animationSet = new AnimationSet(true);
        // 缩放的动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(ANIMATION_DURATION);
        // 透明度动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(ANIMATION_DURATION);

        // 把两个动画效果装到一起
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        animationSet.setAnimationListener(animationListener);

        // 同时播放两种动画效果
        splash.startAnimation(animationSet);
    }

    private AnimationListener animationListener = new AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        // 当动画结束时，会调用此方法
        @Override
        public void onAnimationEnd(Animation animation) {

        }
    };

}
