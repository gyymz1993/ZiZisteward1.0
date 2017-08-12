package com.lsjr.zizisteward.activity.home.ui;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.lsjr.base.MvpActivity;
import com.lsjr.zizisteward.Config;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.group.ui.GroupActivity;
import com.lsjr.zizisteward.activity.home.fragment.HomeFragmentZizi;
import com.lsjr.zizisteward.activity.home.presenter.HomePresenter;
import com.lsjr.zizisteward.activity.home.view.IHomeView;
import com.lsjr.zizisteward.activity.me.MeFragmentZizi;
import com.lsjr.zizisteward.activity.scope.ui.ScopeFragmentZizi;
import com.lsjr.zizisteward.bean.AddressBookBean;
import com.lsjr.zizisteward.coustom.NoScrollViewPager;
import com.ymz.baselibrary.utils.L_;
import com.ymz.baselibrary.utils.T_;
import com.ymz.baselibrary.view.PermissionListener;
import com.ymz.baselibrary.widget.NavigationBarView;
import com.ys.uilibrary.tab.BottomTabView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;


public class HomeActivity extends MvpActivity<HomePresenter> implements IHomeView {

    FragmentPagerAdapter adapter;
    public int currentPosition = -1;
    @BindView(R.id.id_nativgation_view)
    public NavigationBarView naView;
    @BindView(R.id.viewPager)
    public NoScrollViewPager viewPager;
    @BindView(R.id.bottomTabView)
    public BottomTabView bottomTabView;
    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<BottomTabView.TabItemView> tabItemViews = new ArrayList<>();
    private String[] titles = new String[]{"首页", "视界", "圈子", "个人"};

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }


    public void initParams() {
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
        }
        bottomTabView.setOnSecondSelectListener(new BottomTabView.OnSecondSelectListener() {
            @Override
            public void onSecondSelect(int position) {

            }
        });
        if (getOnPageChangeListener() != null) {
            viewPager.addOnPageChangeListener(getOnPageChangeListener());
        }

    }

    protected List<BottomTabView.TabItemView> getTabViews() {
        tabItemViews.add(new BottomTabView.TabItemView(this, "首页", R.color.tab_normal, R.color.tab_selected, R.drawable.home_page_off, R.drawable.home_page_on));
        tabItemViews.add(new BottomTabView.TabItemView(this, "视界", R.color.tab_normal, R.color.tab_selected, R.drawable.data_off, R.drawable.data_on));
        tabItemViews.add(new BottomTabView.TabItemView(this, "圈子", R.color.tab_normal, R.color.tab_selected, R.drawable.recommend_off, R.drawable.recommend_on));
        tabItemViews.add(new BottomTabView.TabItemView(this, "个人", R.color.tab_normal, R.color.tab_selected, R.drawable.tribe_off, R.drawable.tribe_on));
        return tabItemViews;
    }

    Fragment homeFrg, scopeFrg, groupFrg, meFrg;

    protected List<Fragment> getFragments() {
        fragments = new ArrayList<>();
        fragments.add(homeFrg);
        fragments.add(scopeFrg);
        fragments.add(groupFrg);
        fragments.add(meFrg);
        return fragments;
    }

    protected ViewPager.OnPageChangeListener getOnPageChangeListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                bottomTabView.updatePosition(position);
                naView.setTitleText(titles[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }

    protected BottomTabView.OnTabItemSelectListener getOnTabItemSelectListener() {
        return new BottomTabView.OnTabItemSelectListener() {
            @Override
            public void onTabItemSelect(int position) {
                if (getFragments().get(position) instanceof HomeFragmentZizi ||
                        getFragments().get(position) instanceof ScopeFragmentZizi) {
                    naView.setVisibility(View.GONE);
                } else {
                    naView.setVisibility(View.VISIBLE);
                    naView.getLeftimageView().setVisibility(View.VISIBLE);
                }
                currentPosition = position;
                naView.setTitleText(titles[position]);
                viewPager.setCurrentItem(position, true);
            }
        };
    }

    protected String getInitTitle() {
        return titles[0];
    }

    protected View getCenterView() {
        ImageView centerView = new ImageView(this);
        centerView.setImageResource(R.drawable.ic_launcher_round);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
        layoutParams.leftMargin = 60;
        layoutParams.rightMargin = 60;
        layoutParams.bottomMargin = 40;
        centerView.setLayoutParams(layoutParams);
        centerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return null;
    }


    @Override
    public void initView() {
        homeFrg = new HomeFragmentZizi();
        scopeFrg = new ScopeFragmentZizi();
        groupFrg = new MeFragmentZizi();
        meFrg = new MeFragmentZizi();
        requestPermissions(new String[]{ Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE }, new PermissionListener() {
            @Override
            public void onGranted() {
                T_.showToastReal("申请权限成功");
            }

            @Override
            public void onDenied(List<String> deniedPermissions) {
                T_.showToastReal("申请权限失败");
            }
        });

    }


    @Override
    protected void initTitle() {
        if (naView != null) {
            naView.setTitleText(titles[0]);
            naView.getLeftimageView().setVisibility(View.GONE);
            naView.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void afterCreate(Bundle bundle) {
        // SystemBarHelper.setStatusBarDarkMode(this);
        //SystemBarHelper.tintStatusBar(this, UIUtils.getColor(R.color.red));
        //transparencyBar(this);
        //UIUtils.setStatusBarColor(this,UIUtils.getColor(R.color.white));
        //setColor(this,UIUtils.getColor(R.color.white)); // 设置沉浸式状态栏的代码，布局里面需要添加android:fitsSystemWindows="true"

        //StatusBarCompat.setupStatusBarView(this, (ViewGroup) getWindow().getDecorView(), true, R.color.colorPrimary);
        initParams();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bottomTabView != null && currentPosition != -1) {
            bottomTabView.updatePosition(currentPosition);
            viewPager.setCurrentItem(currentPosition);
        }
    }


    @Override
    public void easeLoignSucceed(String result) {
        /* 得到朋友列表*/
        AddressBookBean abBean = new Gson().fromJson(result, AddressBookBean.class);
        L_.e(abBean.toString());
        Bundle bundle = new Bundle();
        bundle.putSerializable(Config.SP_ADRESS_BOOK_KEY, abBean);
        openActivity(GroupActivity.class, bundle);
    }

    @Override
    public void getPageDataSucceed(String result) {

    }
}
