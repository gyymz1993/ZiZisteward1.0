package com.lsjr.zizisteward.activity.classly.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.lsjr.base.MvpActivity;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.classly.fragment.ClasslyFragmentZizi;
import com.lsjr.zizisteward.activity.group.ui.GroupActivity;
import com.ymz.baselibrary.ABaseFragment;
import com.ymz.baselibrary.mvp.BasePresenter;
import com.ymz.baselibrary.widget.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by admin on 2017/5/13.
 */

public class ClassifyActivity extends MvpActivity implements GroupActivity.GetDataOnclickListener {
    private String[] titles = new String[]{"匠品", "奢品", "出行", "首页", "美食", "健康", "家族"};

    @BindView(R.id.id_bar_view)
    NavigationBarView idBarView;

   // @BindView(R.id.tab)
   // ColorTrackTabViewIndicator indicatroViewp;
    @BindView(R.id.id_viewpager)
    ViewPager mViewPager;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        List<ABaseFragment> fragments = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            ClasslyFragmentZizi fragment = new ClasslyFragmentZizi();
            fragments.add(fragment);
        }
//        indicatroViewp.setTitles(titles, new ColorTrackTabViewIndicator.CorlorTrackTabBack() {
//            @Override
//            public void onClickButton(Integer position, ColorTrackView colorTrackView) {
//                mViewPager.setCurrentItem(position,false);
//            }
//        });
//        mViewPager.setOffscreenPageLimit(fragments.size());
//        mViewPager.setAdapter(new TitlePagerAdapter(getSupportFragmentManager(), fragments, titles));
//        indicatroViewp.setupViewPager(mViewPager);
    }

    @Override
    protected void initTitle() {
        //SystemBarHelper.tintStatusBar(this, UIUtils.getColor(R.color.colorBlack));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_classly;
    }

    @Override
    protected void afterCreate(Bundle bundle) {

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getFragmentContentId() {
        return 0;
    }

    @Override
    public void setData(String data) {

    }
}
