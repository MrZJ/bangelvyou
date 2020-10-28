package com.fuping.system.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.fuping.system.ui.fragment.NoticeListFragment;

import java.util.ArrayList;

/**
 * 当地列表适配器
 * Created by KevinLi on 2016/2/2.
 */
public class NoticeFragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<String> titles;
    private String noticeType[] = {"扶贫资讯", "案例分析", "政策解读", "贫困户公示"};
    private String noticeTypeId[] = {"1001010200", "1001010300", "1001010400", "1001010500"};
    private boolean needRe = false;//是否需要更新页面
    private FragmentManager fm;

    public NoticeFragmentAdapter(FragmentManager fm, ArrayList<String> list) {
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
        NoticeListFragment fragment = (NoticeListFragment) super.instantiateItem(container, position);
        String fragmentTag = fragment.getTag();
        if (needRe) {
            //如果这个fragment需要更新
            FragmentTransaction ft = fm.beginTransaction();
            //移除旧的fragment
            ft.remove(fragment);
            //换成新的fragment
            fragment = (NoticeListFragment) getItem(position);
            //添加新fragment时必须用前面获得的tag，这点很重要
            ft.add(container.getId(), fragment, fragmentTag);
            ft.attach(fragment);
            ft.commit();
            //复位更新标志
            needRe = false;
        }
        return fragment;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle b = new Bundle();
        b.putInt("position", position);
        b.putString("type", noticeType[position]);
        b.putString("typeId", noticeTypeId[position]);
        return NoticeListFragment.getInstance(b);
    }

    /**
     * 设置需要更新
     */
    public void setNeedRe() {
        needRe = true;
    }

}
