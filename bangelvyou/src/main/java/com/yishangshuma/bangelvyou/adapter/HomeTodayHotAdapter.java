package com.yishangshuma.bangelvyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.entity.LocationEntity;
import com.yishangshuma.bangelvyou.util.AsynImageUtil;
import com.yishangshuma.bangelvyou.wibget.RoundedImage.RoundedImageView;

import java.util.List;

/**
 * Created by KevinLi on 2016/1/28.
 */
public class HomeTodayHotAdapter extends AbsBaseAdapter<LocationEntity> {

    private List<LocationEntity> mData;
    private LayoutInflater inflater;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public HomeTodayHotAdapter(List<LocationEntity> branchInfos, Context context) {
        mData = branchInfos;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {

        if (null == mData) {
            return 0;
        }
        return mData.size();
    }

    public LocationEntity getItem(int position) {

        return mData.get(position);
    }

    public long getItemId(int position) {

        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_home_today_hot, null);
            holder = new ViewHolder();
            holder.divider_today_hot = (View) convertView
                    .findViewById(R.id.divider_today_hot);
            holder.ll_today_hot = (View) convertView
                    .findViewById(R.id.ll_today_hot);
            holder.tv_today_hot_num = (TextView) convertView
                    .findViewById(R.id.tv_today_hot_num);
            holder.tv_today_hot_name = (TextView) convertView
                    .findViewById(R.id.tv_today_hot_name);
            holder.img_today_hot = (RoundedImageView) convertView
                    .findViewById(R.id.img_today_hot);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        LocationEntity locationEntity = getItem(position);
        imageLoader.displayImage(locationEntity.location_img, holder.img_today_hot,
                AsynImageUtil.getImageOptions(R.mipmap.banner), null);
        if(position > 0){
            holder.divider_today_hot.setVisibility(View.GONE);
            holder.ll_today_hot.setVisibility(View.GONE);
        } else {
            holder.divider_today_hot.setVisibility(View.VISIBLE);
            holder.ll_today_hot.setVisibility(View.VISIBLE);
        }
        holder.tv_today_hot_num.setText(locationEntity.location_collect_count);
        holder.tv_today_hot_name.setText(locationEntity.location_title);
        return convertView;
    }

    private class ViewHolder {
        View divider_today_hot, ll_today_hot, ll_today_hot_love;
        TextView tv_today_hot_num, tv_today_hot_name;
        RoundedImageView img_today_hot;

    }

    public void setData(List<LocationEntity> shopEntity) {
        mData = shopEntity;
    }
}
