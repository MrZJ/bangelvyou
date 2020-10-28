package com.basulvyou.system.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.basulvyou.system.R;
import com.basulvyou.system.entity.LocationEntity;
import com.basulvyou.system.util.AsynImageUtil;
import com.basulvyou.system.util.DensityUtil;
import com.basulvyou.system.util.StringUtil;
import com.basulvyou.system.wibget.RoundedImage.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 景点数据Grid样式适配器
 */
public class LocationViewGridAdapter extends AbsBaseAdapter<LocationEntity> {

    private Context context;
    private List<LocationEntity> mData;
    private LayoutInflater inflater;
    private int width;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public LocationViewGridAdapter(List<LocationEntity> branchInfos, Context context) {
        this.context = context;
        mData = branchInfos;
        inflater = LayoutInflater.from(context);
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        width = dm.widthPixels;//宽度
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
            convertView = inflater.inflate(R.layout.item_location_view_grid, null);
            holder = new ViewHolder();
            holder.tv_item_location_view_grid_name = (TextView) convertView
                    .findViewById(R.id.tv_item_location_view_grid_name);
            holder.img_item_location_view_gird = (RoundedImageView) convertView
                    .findViewById(R.id.img_item_location_view_grid);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                    holder.img_item_location_view_gird.getLayoutParams();
            params.height = (int) ((width - DensityUtil.dip2px(context,10)) * 2 / 9);
            holder.img_item_location_view_gird.setLayoutParams(params);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        LocationEntity locationEntity = getItem(position);
        if(!StringUtil.isEmpty(locationEntity.location_img_one)){
            imageLoader.displayImage(locationEntity.location_img_one, holder.img_item_location_view_gird,
                    AsynImageUtil.getImageOptions(R.mipmap.loadfailed), null);
        }else{
            imageLoader.displayImage(locationEntity.location_img.get(0).comm_image_url, holder.img_item_location_view_gird,
                    AsynImageUtil.getImageOptions(R.mipmap.loadfailed), null);
        }
        holder.tv_item_location_view_grid_name.setText(locationEntity.location_title);
        return convertView;
    }

    private class ViewHolder {
        TextView tv_item_location_view_grid_name;
        RoundedImageView img_item_location_view_gird;

    }

    public void setData(List<LocationEntity> locationEntity) {
        mData = locationEntity;
    }

}
