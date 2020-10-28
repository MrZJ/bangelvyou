package com.yishangshuma.bangelvyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.entity.ShopEntity;
import com.yishangshuma.bangelvyou.util.AsynImageUtil;

import java.util.List;

/**
 * 附近景点适配器
 */
public class NearbyAttractionAdapter extends AbsBaseAdapter<ShopEntity> {

    private List<ShopEntity> mData;
    private LayoutInflater inflater;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private boolean isShowMore = true;

    public NearbyAttractionAdapter(List<ShopEntity> commentInfos, Context context) {
        mData = commentInfos;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {

        if (null == mData) {
            return 0;
        }
        return mData.size();
    }

    public ShopEntity getItem(int position) {

        return mData.get(position);
    }

    public long getItemId(int position) {

        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_nearby_attraction, null);
            holder = new ViewHolder();
            holder.img_nearby_attraction = (ImageView) convertView.findViewById(R.id.item_nearby_attraction_img);
            holder.dis_nearby_attraction = (TextView) convertView.findViewById(R.id.item_nearby_attraction_dis);
            holder.name_nearby_attraction = (TextView) convertView.findViewById(R.id.item_nearby_attraction_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ShopEntity shopEntity = getItem(position);
        imageLoader.displayImage(shopEntity.goods_image_url, holder.img_nearby_attraction, AsynImageUtil.getImageOptions(R.mipmap.nearby_attraction_pic), null);
        holder.dis_nearby_attraction.setText(shopEntity.goods_distance);
        holder.name_nearby_attraction.setText(shopEntity.goods_name);
        return convertView;
    }

    private class ViewHolder {
        TextView name_nearby_attraction, dis_nearby_attraction;
        ImageView img_nearby_attraction;
    }

    public void setData(List<ShopEntity> viewEntity) {
        mData = viewEntity;
    }
}
