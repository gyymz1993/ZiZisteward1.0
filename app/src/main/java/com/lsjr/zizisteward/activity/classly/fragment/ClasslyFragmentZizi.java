package com.lsjr.zizisteward.activity.classly.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.google.gson.Gson;
import com.lsjr.base.MvpFragment;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.classly.adapter.GridViewAdapter;
import com.lsjr.zizisteward.activity.classly.adapter.SpacesItemDecoration;
import com.lsjr.zizisteward.activity.classly.presenter.ClasslyPresenter;
import com.lsjr.zizisteward.activity.classly.view.IClasslyView;
import com.lsjr.zizisteward.bean.Commodity;
import com.lsjr.zizisteward.coustom.anythingpull.AnythingPullLayout;
import com.ymz.baselibrary.utils.T_;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;


public class ClasslyFragmentZizi extends MvpFragment implements IClasslyView,AnythingPullLayout.OnPullListener{
    @BindView(R.id.id_recyview)
    RecyclerView idRecyview;
    @BindView(R.id.pull_layout)
    AnythingPullLayout pullLayout;
    GridViewAdapter gridViewAdapter;
    private int cuPager=1;
    private static final int ON_REFRESH=1;
    private static final int ON_LOAD=2;
    private int pullStatus;
    List<Commodity.FamousShopBean> famousShop;

    @Override
    protected ClasslyPresenter createPresenter() {
        return new ClasslyPresenter(this);
    }

    public void loadNetData(){
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "425");
        map.put("currPage", String.valueOf(cuPager));
        createPresenter().getClasslyData(map);
    }

    @Override
    protected void initView() {
        pullLayout.autoRefresh();
        famousShop=new ArrayList<>();
        gridViewAdapter=new GridViewAdapter(getActivity());
        GridLayoutManager mgr=new GridLayoutManager(getActivity(),2);
        //设置item间距，5dp
        idRecyview.addItemDecoration(new SpacesItemDecoration(8));
        idRecyview.setLayoutManager(mgr);
        idRecyview.setAdapter(gridViewAdapter);
        pullLayout.setOnPullListener(this);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_new;
    }

    @Override
    protected void afterCreate(Bundle bundle) {

    }




    @Override
    public void onLoadDataSucceed(String result) {
        T_.showToastReal("请求成功");
        famousShop.clear();
        Commodity commodity = new Gson().fromJson(result, Commodity.class);
        famousShop = commodity.getFamousShop();
        endNetRequse();
    }


    @Override
    public void onRefreshStart(final AnythingPullLayout pullLayout) {
        cuPager=1;
        pullStatus=ON_REFRESH;
        loadNetData();
    }

    @Override
    public void onLoadStart(final AnythingPullLayout pullLayout) {
        cuPager++;
        pullStatus=ON_LOAD;
        loadNetData();
    }


    public void endNetRequse() {

        if (pullStatus==ON_LOAD){
            gridViewAdapter.addmFamousShop(famousShop);
            pullLayout.responseload(true);
        }
        if (pullStatus==ON_REFRESH){
            gridViewAdapter.setmFamousShop(famousShop);
            pullLayout.responseRefresh(true);
        }
        pullStatus=0;
    }

}
