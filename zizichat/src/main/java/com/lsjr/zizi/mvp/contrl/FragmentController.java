package com.lsjr.zizi.mvp.contrl;

import android.support.v4.app.Fragment;


import com.lsjr.zizi.mvp.frag.FindFragment;
import com.lsjr.zizi.mvp.home.ContactsFragment;
import com.lsjr.zizi.mvp.home.MessageFragment;

import java.util.ArrayList;


/**
 * 主界面Fragment控制器
 */
public class FragmentController {


    private ArrayList<Fragment> fragments;
    private static FragmentController controller;
    private MessageFragment mAddressListFragment;
    private FindFragment mMessageFragment;
    private FindFragment mTopicFragment;
    private ContactsFragment contactsFragment;

    public static FragmentController getInstance() {
        if (controller == null) {
            controller = new FragmentController();
        }
        return controller;
    }

    public static void onDestroy() {
        controller = null;
    }

    private FragmentController() {
        initFragment();
    }

    private void initFragment() {
        if (fragments == null) {
            fragments = new ArrayList<>();
        }
        if (mAddressListFragment == null) {
            mAddressListFragment = new MessageFragment();
        }
        if (mMessageFragment == null) {
            mMessageFragment = new FindFragment();
        }
        if (mTopicFragment == null) {
            mTopicFragment = new FindFragment();
        }
        if (contactsFragment == null) {
            contactsFragment = new ContactsFragment();
        }
        fragments.add(mAddressListFragment);
        fragments.add(mMessageFragment);
        fragments.add(mTopicFragment);
        fragments.add(contactsFragment);


    }


    public Fragment getFragment(int position) {
        return fragments.get(position);
    }
}