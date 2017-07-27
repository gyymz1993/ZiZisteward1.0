package com.lsjr.zizisteward.activity.home.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lsjr.base.MvpFragment;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.classly.adapter.SpacesItemDecoration;
import com.lsjr.zizisteward.activity.home.adapter.GridAdapter;
import com.lsjr.zizisteward.activity.home.presenter.HomePresenter;
import com.lsjr.zizisteward.activity.home.view.IHomeView;
import com.lsjr.zizisteward.bean.HomeBean;
import com.lsjr.zizisteward.coustom.MySwipeRefreshLayout;
import com.lsjr.zizisteward.http.AppUrl;
import com.ymz.baselibrary.utils.UIUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by admin on 2017/5/16.O
 */

public class FootFragmentZizi extends MvpFragment<HomePresenter> implements IHomeView {


    @BindView(R.id.id_swpe_refresh_lay)
    MySwipeRefreshLayout refreshLayout;
    @BindView(R.id.gridview_recommend)
    RecyclerView gridCyclerView;
    private GridAdapter adapter;
    @BindView(R.id.iv_ziwei)
    ImageView igZiwei;

    /*banner图片*/
    List<HomeBean.AdvertisementsBean> advertisements = new ArrayList<>();

    /*推荐列表 可刷新*/
    private List<HomeBean.DiligentRecommendBean> recommends = new ArrayList<>();


    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initGridCycleView() {
        adapter = new GridAdapter(getActivity(), recommends);
        LinearLayoutManager llyanager = new LinearLayoutManager(getActivity());
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(10);
        llyanager.setOrientation(LinearLayoutManager.HORIZONTAL);
        /*解决滑动缓慢 */
        gridCyclerView.addItemDecoration(spacesItemDecoration);
        gridCyclerView.setLayoutManager(llyanager);
        gridCyclerView.setAdapter(adapter);
        gridCyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        refreshLayout.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        refreshLayout.setEnabled(true);
                        break;
                }
                return false;
            }

        });
    }

    private View getHeaderView(RecyclerView view) {
        return LayoutInflater.from(getActivity()).inflate(R.layout.inculed_recycleview_head, view, false);
    }



    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        getLoadNetData();
    }


    @Override
    protected void initView() {
        initGridCycleView();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLoadNetData();
            }
        });
    }



    @Override
    protected int getLayoutId() {
        return R.layout.fragment_foot;
    }

    @Override
    protected void afterCreate(Bundle bundle) {
        UIUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showLoadingView();
            }
        },0);

        UIUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showContentView();
            }
        },3000);
    }

    private void initBanner() {
//        bannerView.setBannerAdapter(new BannerAdapter() {
//            @Override
//            public View getView(int position, View converView) {
//                if (converView == null) {
//                    converView = new ImageView(getContext());
//                }
//                // ((ImageView)converView).setScaleType(ImageView.ScaleType.CENTER_CROP);
//                ((ImageView) converView).setScaleType(ImageView.ScaleType.FIT_XY);
//                Glide.with(getActivity()).load(BaseUrl.Http + advertisements.get(position).getImage_filename()).into((ImageView) converView);
//                return converView;
//            }
//
//            @Override
//            public int getCount() {
//                return advertisements.size();
//            }
//
//            @Override
//            public String getBinnerDesc(int position) {
//                return "";
//            }
//
//            @Override
//            public boolean isCircularSliding() {
//                return true;
//            }
//        });
//        bannerView.getmBannerVp().setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                    case MotionEvent.ACTION_MOVE:
//                        refreshLayout.setEnabled(false);
//                        break;
//                    case MotionEvent.ACTION_UP:
//                    case MotionEvent.ACTION_CANCEL:
//                        refreshLayout.setEnabled(true);
//                        break;
//                }
//                return false;
//            }
//        });
//        bannerView.onStartBannerLoop();
    }



    /*更新推荐列表*/
    private void getChangeRecommend() {
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "82");
        map.put("name", "");

    }

    public void getLoadNetData() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("OPT", "24");
        map.put("currPage", "1");
        map.put("name", "");
        map.put("city_id", "420100");
        createPresenter().getHomePager(map);
    }

    HomeBean.ShouImgBean shouImg;

    @Override
    public void easeLoignSucceed(String result) {

    }

    @Override
    public void getPageDataSucceed(String result) {
        refreshLayout.setRefreshing(false);
        HomeBean bean = new Gson().fromJson(result, HomeBean.class);
        /*banner*/
        advertisements = bean.getAdvertisements();
        // 孜孜推荐
        recommends = bean.getDiligent_recommend();

        /*图片显示*/
        shouImg = bean.getShouImg();
        initBanner();
        //initGridCycleView();
        adapter.notifyDataChange(recommends);
        /*孜孜天下*/
        showImage(igZiwei, shouImg.getDiligentFood());

    }


    private void showImage(ImageView view, String picUrl) {
        RelativeLayout.LayoutParams linearParams2 = (RelativeLayout.LayoutParams) view.getLayoutParams();
        linearParams2.width = UIUtils.WHD()[0];
        linearParams2.height = UIUtils.WHD()[0] / 2;
        view.setLayoutParams(linearParams2);
        // 孜味天下图片
        Glide.with(getContext()).load(AppUrl.Http + picUrl)
                .into(view);
    }


}
