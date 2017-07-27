package com.lsjr.zizisteward.activity.steward.ui;

import android.os.Bundle;

import com.lsjr.base.MvpActivity;
import com.lsjr.zizisteward.R;
import com.ymz.baselibrary.mvp.BasePresenter;


/**
 * Created by admin on 2017/5/19.
 */

public class StewardActivity extends MvpActivity {
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    @Override
    protected void initTitle() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_steward;
    }

    @Override
    protected void afterCreate(Bundle bundle) {

    }

    @Override
    public int getFragmentContentId() {
        return 0;
    }


}
