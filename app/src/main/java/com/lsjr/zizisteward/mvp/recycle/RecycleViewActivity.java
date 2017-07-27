package com.lsjr.zizisteward.mvp.recycle;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lsjr.base.MvpActivity;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.classly.adapter.SpacesItemDecoration;
import com.ymz.baselibrary.mvp.BasePresenter;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by admin on 2017/5/22.
 */

public class RecycleViewActivity extends MvpActivity {

    private List<RecycleBean> mData = new ArrayList<>();
    @BindView(R.id.id_re_home)
    RecyclerView idReHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    @Override
    protected void initView() {
        super.initView();

        /*数据类型1*/
        RecycleBean recycleBean1 = new RecycleBean();
        recycleBean1.setType(RecycleBean.TYPE_1);
        mData.add(recycleBean1);


         /*数据类型2*/
        RecycleBean recycleBean2 = new RecycleBean();
        recycleBean2.setType(RecycleBean.TYPE_2);
        mData.add(recycleBean2);

        /*数据类型3*/
        RecycleBean recycleBean3 = new RecycleBean();
        recycleBean3.setType(RecycleBean.TYPE_3);
        mData.add(recycleBean3);

        RecycleViewTypeAdapter recycleViewTypeAdapter = new RecycleViewTypeAdapter(this, mData);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(10);
        idReHome.addItemDecoration(spacesItemDecoration);
        idReHome.setLayoutManager(linearLayoutManager);
        idReHome.setAdapter(recycleViewTypeAdapter);


    }

    @Override
    protected int getLayoutId() {
        return R.layout.test_activity_recycle;
    }

    @Override
    protected void afterCreate(Bundle bundle) {

    }

    @Override
    public int getFragmentContentId() {
        return 0;
    }

}
