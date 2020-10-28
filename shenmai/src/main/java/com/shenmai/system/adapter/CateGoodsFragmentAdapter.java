package com.shenmai.system.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.shenmai.system.ui.fragment.GoodsFragment;
import com.shenmai.system.ui.fragment.GoodsListFragment;

import java.util.ArrayList;

/**
 * 分类页面切换适配器
 */
public class CateGoodsFragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<String> titles;
    private ArrayList<String> ids;
    private boolean needRe = false;//是否需要更新页面
    private FragmentManager fm;

    public CateGoodsFragmentAdapter(FragmentManager fm, ArrayList<String> list,ArrayList<String> ids) {
        super(fm);
        this.titles = list;
        this.fm = fm;
        this.ids = ids;
    }

    public void setData(ArrayList<String> list,ArrayList<String> ids){
        this.titles = list;
        this.ids = ids;
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
        if(position == 0){
            GoodsFragment fragment = (GoodsFragment) super.instantiateItem(container,position);
            return fragment;
        }else{
            GoodsListFragment fragment = (GoodsListFragment) super.instantiateItem(container,position);
            return fragment;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle b = new Bundle();
        b.putString("cateId", ids.get(position));
        if(position == 0){
            return GoodsFragment.getInstance(b);
        } else {
            return GoodsListFragment.getInstance(b);
        }
    }
}
