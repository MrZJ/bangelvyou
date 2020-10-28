package com.basulvyou.system.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.basulvyou.system.entity.ShopBundleEntity;

import java.util.ArrayList;
import java.util.List;

// 社区
public class MyViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList = new ArrayList<>();
    ArrayList<String> mData = new ArrayList<>();
    private ShopBundleEntity shopBundleEntity;
    private String locationType[] = {"info", "ready_to_go", "appointment", "live", "news"};
    private String locationTypeId[] = {"20890", "20888", "20894", "20892", "news"};

    public MyViewPagerAdapter(FragmentManager fm, ArrayList<String> list, String type) {
        super(fm);
        this.mData = list;
        for (int i = 0; i < 4; i++) {
            Bundle b = new Bundle();
            b.putInt("position", i);
            b.putString("type", locationType[i]);
            b.putString("typeId", locationTypeId[i]);
            b.putSerializable("shopBundle", shopBundleEntity);
            b.putString("resType", type);
        }
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mData.get(position);
    }
}
