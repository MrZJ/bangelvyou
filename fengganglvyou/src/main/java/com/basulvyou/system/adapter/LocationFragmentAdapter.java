package com.basulvyou.system.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.basulvyou.system.entity.ShopBundleEntity;
import com.basulvyou.system.ui.fragment.LocationListFragment;
import com.basulvyou.system.ui.fragment.LocationViewGridFragment;

import java.util.ArrayList;

/**
 * 当地列表适配器
 * Created by KevinLi on 2016/2/2.
 */
public class LocationFragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<String> titles;
    private ShopBundleEntity shopBundleEntity;
    private String locationType[] = {"view", "food", "sleep", "location", "live", "culture", "culture"};
    private String locationTypeId[] = {"20890", "20894", "20888", "loca", "20892", "21062", "21062"};
    private boolean needRe = false;//是否需要更新页面
    private FragmentManager fm;

    public LocationFragmentAdapter(FragmentManager fm, ArrayList<String> list) {
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
        if (position == 0) {
            LocationViewGridFragment fragment = (LocationViewGridFragment) super.instantiateItem(container, position);
            String fragmentTag = fragment.getTag();
            if (needRe) {
                //如果这个fragment需要更新
                FragmentTransaction ft = fm.beginTransaction();
                //移除旧的fragment
                ft.remove(fragment);
                //换成新的fragment
                fragment = (LocationViewGridFragment) getItem(position);
                //添加新fragment时必须用前面获得的tag，这点很重要
                ft.add(container.getId(), fragment, fragmentTag);
                ft.attach(fragment);
                ft.commit();
                //复位更新标志
                needRe = false;
            }
            return fragment;
        } else {
            //得到缓存的fragment
            LocationListFragment fragment = (LocationListFragment) super.instantiateItem(container, position);
            //得到tag，这点很重要
            String fragmentTag = fragment.getTag();
            if (needRe) {
                //如果这个fragment需要更新
                FragmentTransaction ft = fm.beginTransaction();
                //移除旧的fragment
                ft.remove(fragment);
                //换成新的fragment
                fragment = (LocationListFragment) getItem(position);
                //添加新fragment时必须用前面获得的tag，这点很重要
                ft.add(container.getId(), fragment, fragmentTag);
                ft.attach(fragment);
                ft.commit();
                //复位更新标志
                needRe = false;
            }
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
        b.putInt("position", position);
        b.putString("type", locationType[position]);
        b.putString("typeId", locationTypeId[position]);
        b.putSerializable("shopBundle", shopBundleEntity);
        if (position == 0) {
            return LocationViewGridFragment.getInstance(b);
        } else {
            if (position == 5) {
                b.putString("mod_id", "2020002000");
            } else if (position == 6) {
                b.putInt("position", 5);
                b.putString("mod_id", "2020001000");
            }
            return LocationListFragment.getInstance(b);
        }
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

//    public void setLocationType(String locationType){
//
//        this.locationType = locationType;
//    }
}
