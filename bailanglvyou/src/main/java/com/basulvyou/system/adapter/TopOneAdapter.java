package com.basulvyou.system.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.basulvyou.system.R;
import com.basulvyou.system.entity.TopInfo;
import com.basulvyou.system.util.AsynImageUtil;
import com.basulvyou.system.util.CacheImgUtil;
import com.basulvyou.system.util.ListUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


public class TopOneAdapter extends RecyclingPagerAdapter {

    private Context ctx;
    private List<TopInfo> topInfos;
    private int size;
    private boolean isInfiniteLoop;
    private OnModelClickListener listener;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private String whereUse;
    private boolean isImgOnClick = true;//默认图片可点击

    public TopOneAdapter(Context ctx, String whereUse) {
        this.ctx = ctx;
        this.whereUse = whereUse;
    }

    public void setData(List<TopInfo> topInfos) {
        this.topInfos = topInfos;
        this.size = ListUtils.getSize(topInfos);
        isInfiniteLoop = false;
    }

    public int getCount() {
        // Infinite loop
        return isInfiniteLoop ? Integer.MAX_VALUE : ListUtils.getSize(topInfos);
    }

    public TopInfo getData(int position) {
        if (topInfos == null || topInfos.size() <= position) {
            return null;
        } else {
            return topInfos.get(position);
        }
    }

    /**
     * get really position
     *
     * @param position
     * @return
     */
    private int getPosition(int position) {
        return isInfiniteLoop ? position % size : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = holder.imageView = new ImageView(ctx);
            holder.imageView.setScaleType(ScaleType.FIT_XY);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final TopInfo topInfo = topInfos.get(getPosition(position));
        if ("home".equals(whereUse)) {
            AsynImageUtil.promotionFirstListener.setPath(CacheImgUtil.getInstance(ctx).adv_top);
        } else if ("storeHome".equals(whereUse)) {
            //AsynImageUtil.promotionFirstListener.setPath(CacheImgUtil.getInstance(ctx).store_adv_top);
        }
        imageLoader.displayImage(topInfo.pciture, holder.imageView,
                AsynImageUtil.getImageOptions(R.mipmap.banner),
                AsynImageUtil.promotionFirstListener);

        if (isImgOnClick) {
            holder.imageView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.gotoShop(topInfo.picUrl, topInfo);
                }

            });
        }
        return convertView;
    }

    private static class ViewHolder {

        ImageView imageView;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public void setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
    }

    public interface OnModelClickListener {

        void gotoShop(String key, TopInfo topInfo);
    }

    public void setOnModelClickListener(OnModelClickListener listener) {
        this.listener = listener;
    }

    /**
     * 设置图片是否可点击，默认可点击
     *
     * @param isImgOnClick
     */
    public void setIsImgOnClick(boolean isImgOnClick) {
        this.isImgOnClick = isImgOnClick;
    }
}

