package com.basulvyou.system.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.basulvyou.system.entity.OrderTypeEntity;
import com.basulvyou.system.ui.fragment.OrderTypeFragment;

import java.util.ArrayList;

/**
 * 订单分类Frag适配器
 */
public class OrderTypeFragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<OrderTypeEntity> titles;

    public OrderTypeFragmentAdapter(FragmentManager fm, ArrayList<OrderTypeEntity> list) {
        super(fm);
        this.titles = list;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position).orderTypeName;
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle b = new Bundle();
        b.putString("key", titles.get(position).orderTypeKey);
        return OrderTypeFragment.getInstance(b);
    }
}

