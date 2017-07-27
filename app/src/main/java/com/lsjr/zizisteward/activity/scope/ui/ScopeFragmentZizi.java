package com.lsjr.zizisteward.activity.scope.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.lsjr.base.MvpFragment;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.home.fragment.ShepinFragment;
import com.lsjr.zizisteward.coustom.IndicatorView;
import com.ymz.baselibrary.mvp.BasePresenter;
import com.ymz.baselibrary.utils.L_;
import com.ymz.baselibrary.utils.UIUtils;
import com.ys.uilibrary.tab.BottomTabView;

import java.util.ArrayList;
import java.util.List;

/**
 * 视界
 */

public class ScopeFragmentZizi extends MvpFragment {


    ArrayList<BottomTabView.TabItemView> tabItemViews = new ArrayList<>();
    public BottomTabView bottomTabView;
    public ViewPager viewPager;
    ViewPagerAdapter adapter;
    IndicatorView tabStrip;
    List<Fragment> fagments = new ArrayList<>();


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void initView() {
        ShepinFragment ziShangFrag = new ShepinFragment();
        ShepinFragment wordFrag = new ShepinFragment();
        fagments.add(ziShangFrag);
        fagments.add(wordFrag);
        adapter = new ViewPagerAdapter(getChildFragmentManager());//最关键的地方是这里
        viewPager.setAdapter(adapter);


        if (getCenterView() == null) {
            bottomTabView.setTabItemViews(getTabViews());
        } else {
            bottomTabView.setTabItemViews(getTabViews(), getCenterView());
        }
        bottomTabView.setOnTabItemSelectListener(new BottomTabView.OnTabItemSelectListener() {
            @Override
            public void onTabItemSelect(int position) {
                viewPager.setCurrentItem(position, true);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabStrip.setTranslationX((position + positionOffset) * UIUtils.WHD()[0] / fagments.size());
            }

            @Override
            public void onPageSelected(int position) {
                bottomTabView.updatePosition(position);
                tabStrip.setTranslationX(position * UIUtils.WHD()[0] / fagments.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_scope;
    }

    @Override
    protected void afterCreate(Bundle bundle) {
        viewPager = (ViewPager) mView.findViewById(R.id.viewPager);
        tabStrip = (IndicatorView) mView.findViewById(R.id.tab_strip);
        bottomTabView = (BottomTabView) mView.findViewById(R.id.bottomTabView);
    }


    public class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fagments.get(position);
        }

        @Override
        public int getCount() {
            return fagments.size();
        }
    }

    protected List<BottomTabView.TabItemView> getTabViews() {
        tabItemViews.add(new BottomTabView.TabItemView(getActivity(), "孜赏", R.color.white, R.color.colorAccent, -1, -1));
        tabItemViews.add(new BottomTabView.TabItemView(getActivity(), "视界", R.color.white, R.color.colorAccent, -1, -1));
        return tabItemViews;
    }


    protected View getCenterView() {
        return null;
    }


}
