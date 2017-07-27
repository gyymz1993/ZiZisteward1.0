package com.lsjr.base;

import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;

import com.lsjr.zizisteward.R;
import com.ymz.baselibrary.mvp.BasePresenter;
import com.ymz.baselibrary.widget.SwipeBackLayout;

/**
 * Created by PandaQ on 2017/3/20.
 * 所有侧滑返回的activity的父类
 */

public abstract class SwipeBackActivity<P extends BasePresenter> extends MvpActivity implements SwipeBackLayout.SwipeListener {
    protected SwipeBackLayout layout;
    private ArgbEvaluator argbEvaluator;
    public static int currentStatusColor;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
                R.layout.swipeback_base, null);
        layout.attachToActivity(this);
        argbEvaluator = new ArgbEvaluator();
        layout.addSwipeListener(this);
        if (Build.VERSION.SDK_INT >= 23) {
            currentStatusColor = getResources().getColor(R.color.transparent, null);
        } else {
            currentStatusColor = getResources().getColor(R.color.transparent);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initTitle() {

    }

    @Override
    public int getFragmentContentId() {
        return 0;
    }

    public void addViewPager(ViewPager pager) {
        layout.addViewPager(pager);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void swipeValue(double value) {
        int statusColor = (int) argbEvaluator.evaluate((float) value, currentStatusColor,
                ContextCompat.getColor(this, R.color.transparent));
        getWindow().setStatusBarColor(statusColor);
    }

}