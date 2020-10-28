package com.basulvyou.system.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.basulvyou.system.ui.fragment.RescueFragment;

import java.util.ArrayList;

/**
 * 报警救援和厕所分类适配器
 */
public class RescueFragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<String> titles;
    private String rescueType[] ={"alarm","toilet","gasStation"};
    private String rescueTypeId[] ={"21054","21064","21068"};
    private FragmentManager fm;

    public RescueFragmentAdapter(FragmentManager fm, ArrayList<String> list) {
        super(fm);
        this.titles = list;
        this.fm = fm;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //得到缓存的fragment
        RescueFragment fragment = (RescueFragment) super.instantiateItem(container, position);
        return fragment;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle b = new Bundle();
        b.putString("type", rescueType[position]);
        b.putString("typeId", rescueTypeId[position]);
        return RescueFragment.getInstance(b);
    }

}
