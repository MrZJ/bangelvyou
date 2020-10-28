package com.fuping.system.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fuping.system.ui.fragment.BaseFragment;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/10/18.
 * copyright easybiz.
 */

public class MyPagerAdaper extends FragmentPagerAdapter {
    private List<BaseFragment> fragments;

    public MyPagerAdaper(FragmentManager manager, List<BaseFragment> fragments) {
        super(manager);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
