package com.lsjr.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.ymz.baselibrary.mvp.BaseMvpActivity;
import com.ymz.baselibrary.mvp.BasePresenter;

/**
  * @author: gyymz1993
  * 创建时间：2017/5/3 21:53
  * @version
  *
 **/
public abstract class MvpActivity<P extends BasePresenter> extends BaseMvpActivity<P> {

    @Override
    protected void initTitle() {
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public int getFragmentContentId() {
        return 0;
    }

}
