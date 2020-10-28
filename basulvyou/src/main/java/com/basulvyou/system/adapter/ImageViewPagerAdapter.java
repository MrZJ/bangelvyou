package com.basulvyou.system.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.basulvyou.system.entity.ShareImgEntity;
import com.basulvyou.system.ui.fragment.ImageFragment;

import java.util.List;


/**
 * Created by csonezp on 15-12-28.
 */
public class ImageViewPagerAdapter extends FragmentStatePagerAdapter {
    private static final String IMAGE_URL = "image";

    List<ShareImgEntity> mDatas;
    private Context context;
    
    public ImageViewPagerAdapter(FragmentManager fm, List<ShareImgEntity> data,Context ctx) {
        super(fm);
        context = ctx;
        mDatas = data;
    }

    @Override
    public Fragment getItem(int position) {
        String url = mDatas.get(position).friend_image_url;
        Fragment fragment = ImageFragment.newInstance(url, context);
        return fragment;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }
}
