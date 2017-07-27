package com.lsjr.zizisteward.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.lsjr.base.MvpActivity;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.coustom.NoScrollViewPager;
import com.ymz.baselibrary.mvp.BasePresenter;
import com.ymz.baselibrary.widget.NavigationBarView;
import com.ys.uilibrary.tab.BottomTabView;


import java.util.List;

import butterknife.BindView;


public abstract class BottomTabBaseActivity<P extends BasePresenter> extends MvpActivity {

    FragmentPagerAdapter adapter;
    public int currentPosition=-1;
    @BindView(R.id.id_nativgation_view)
    public NavigationBarView naView;
    @BindView(R.id.viewPager)
    public NoScrollViewPager viewPager;
    @BindView(R.id.bottomTabView)
    public BottomTabView bottomTabView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_bottom_tab);
        initParams();
        initTitle();
    }


    public  void initParams(){
        viewPager.setOffscreenPageLimit(4);//设置ViewPager的缓存界面数,默认缓存为2
        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return getFragments().get(position);
            }

            @Override
            public int getCount() {
                return getFragments().size();
            }
        };
        viewPager.setAdapter(adapter);
        if (getCenterView() == null) {
            bottomTabView.setTabItemViews(getTabViews());
        } else {
            bottomTabView.setTabItemViews(getTabViews(), getCenterView());
        }

        if (getOnTabItemSelectListener() != null) {
            bottomTabView.setOnTabItemSelectListener(getOnTabItemSelectListener());
        } else {
            bottomTabView.setOnTabItemSelectListener(new BottomTabView.OnTabItemSelectListener() {
                @Override
                public void onTabItemSelect(int position) {


                }
            });
        }
        bottomTabView.setOnSecondSelectListener(new BottomTabView.OnSecondSelectListener() {
            @Override
            public void onSecondSelect(int position) {

            }
        });
        if (getOnPageChangeListener() != null) {
            viewPager.addOnPageChangeListener(getOnPageChangeListener());
        } else {
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

    }



    protected abstract List<BottomTabView.TabItemView> getTabViews();

    protected abstract List<Fragment> getFragments();

    protected abstract ViewPager.OnPageChangeListener getOnPageChangeListener();

    protected abstract BottomTabView.OnTabItemSelectListener getOnTabItemSelectListener();

    protected abstract String getInitTitle();

    protected View getCenterView() {
        return null;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
