package com.basulvyou.system.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.basulvyou.system.entity.ShopBundleEntity;
import com.basulvyou.system.ui.fragment.CarpoolingListFragment;

import java.util.ArrayList;

/**
 * 拼车信息页面适配器
 */
public class CarpoolingFragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<String> titles;
    private ShopBundleEntity shopBundleEntity;
    private String locationType[] ={"view","sleep","food","live","news"};
    private String locationTypeId[] ={"20890","20888","20894","20892","news"};
    private boolean needRe = false;//是否需要更新页面
    private FragmentManager fm;

    public CarpoolingFragmentAdapter(FragmentManager fm, ArrayList<String> list) {
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
        CarpoolingListFragment fragment = (CarpoolingListFragment) super.instantiateItem(container, position);
        //得到tag，这点很重要
        String fragmentTag = fragment.getTag();
        if (needRe) {
            //如果这个fragment需要更新
            FragmentTransaction ft = fm.beginTransaction();
            //移除旧的fragment
            ft.remove(fragment);
            //换成新的fragment
            fragment = (CarpoolingListFragment) getItem(position);
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
        b.putString("type", locationType[position]);
        b.putString("typeId", locationTypeId[position]);
        b.putSerializable("shopBundle", shopBundleEntity);
        return CarpoolingListFragment.getInstance(b);
    }

    public void setShopBundle(ShopBundleEntity shopBundleEntity){
        this.shopBundleEntity = shopBundleEntity;
    }

    /**
     * 设置需要更新
     */
    public void setNeedRe(){
        needRe = true;
    }

}
