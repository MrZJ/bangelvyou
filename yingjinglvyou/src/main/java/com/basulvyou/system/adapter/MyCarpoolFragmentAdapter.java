package com.basulvyou.system.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.basulvyou.system.ui.fragment.MyCarpoolFragment;

import java.util.ArrayList;

/**
 * 我的拼车、物流fragment适配器
 */
public class MyCarpoolFragmentAdapter extends FragmentPagerAdapter{

    private ArrayList<String> titles;
    private String rescueType[] ={"yd","fb"};//预定、发布
    private String orderType;
    private FragmentManager fm;

    public MyCarpoolFragmentAdapter(FragmentManager fm,ArrayList<String> list,String orderType) {
        super(fm);
        this.titles = list;
        this.fm = fm;
        this.orderType = orderType;
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
        MyCarpoolFragment fragment = (MyCarpoolFragment) super.instantiateItem(container, position);
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
        b.putString("orderType", orderType);
        return MyCarpoolFragment.getInstance(b);
    }
}
