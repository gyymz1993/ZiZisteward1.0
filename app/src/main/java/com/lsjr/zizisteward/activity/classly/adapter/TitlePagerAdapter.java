package com.lsjr.zizisteward.activity.classly.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.ymz.baselibrary.ABaseFragment;

import java.util.List;

public class TitlePagerAdapter extends FragmentPagerAdapter {

    private List<ABaseFragment> fragments;
    private String[] titles;

    public TitlePagerAdapter(FragmentManager fm, List<ABaseFragment> fragments, String[] titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public ABaseFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles == null ? "" : titles[position];
    }

}