package com.basulvyou.system.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.basulvyou.system.entity.ShopBundleEntity;
import com.basulvyou.system.ui.fragment.NearbyListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 当地列表适配器
 * Created by KevinLi on 2016/2/2.
 */
public class NearbyFragmentAdapter extends FragmentPagerAdapter {

    private List<NearbyListFragment> fragments = new ArrayList<>();
    private ArrayList<String> titles;
    private ShopBundleEntity shopBundleEntity;
    private String locationType[] = {"location", "view", "sleep", "food",};
    private String locationTypeId[] = {"0", "20890", "20888", "20894"};
    private boolean needRe = false;//是否需要更新页面
    private FragmentManager fm;


    public NearbyFragmentAdapter(FragmentManager fm, ArrayList<String> list) {
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
        NearbyListFragment fragment = (NearbyListFragment) super.instantiateItem(container, position);
        //得到tag，这点很重要
        String fragmentTag = fragment.getTag();
        if (needRe) {
            //如果这个fragment需要更新
            FragmentTransaction ft = fm.beginTransaction();
            //移除旧的fragment
            ft.remove(fragment);
            //换成新的fragment
            fragment = (NearbyListFragment) getItem(position);
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
        NearbyListFragment f = NearbyListFragment.getInstance(b);
        fragments.add(f);
        return f;
    }

    public void setShopBundle(ShopBundleEntity shopBundleEntity) {
        this.shopBundleEntity = shopBundleEntity;
    }

    /**
     * 设置需要更新
     */
    public void setNeedRe() {
        needRe = true;
    }

}
