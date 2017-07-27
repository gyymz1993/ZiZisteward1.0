package com.lsjr.zizisteward.activity.login.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class LgFragAdapter extends FragmentPagerAdapter {
	private List<Fragment> fragments;

	public LgFragAdapter(FragmentManager fm) {
		super(fm);
	}

	public LgFragAdapter(FragmentManager supportFragmentManager, List<Fragment> fragments) {
		super(supportFragmentManager);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

}
