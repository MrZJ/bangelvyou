package com.basulvyou.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.basulvyou.system.R;
import com.basulvyou.system.entity.NearbyEntity;
import com.basulvyou.system.util.AsynImageUtil;
import com.basulvyou.system.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/6/8.
 * copyright easybiz.
 */

public class NearbyAdapter extends AbsBaseAdapter<NearbyEntity> {
    private Context mContext;
    private List<NearbyEntity> mData;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public NearbyAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<NearbyEntity> viewEntity) {
        mData = viewEntity;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public NearbyEntity getItem(int position) {
        return mData.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.nearby_item, null);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.des = (TextView) convertView.findViewById(R.id.des);
            holder.distance = (TextView) convertView.findViewById(R.id.distance);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        NearbyEntity entity = mData.get(position);
        if (entity != null) {
            holder.title.setText(entity.goods_name);
            holder.content.setText(entity.goods_content);
            StringBuilder builder = new StringBuilder();
            builder.append(entity.cls_type_name).append("|").append(entity.p_name);
            holder.des.setText(builder);
            holder.distance.setText(entity.goods_distance);
            holder.image.setImageResource(0);//清空背景图片
            holder.image.setImageBitmap(null);//清空背景图片
            String picUrl = entity.goods_image_url1;
            if (!StringUtil.isEmpty(picUrl)) {
                imageLoader.displayImage(picUrl, holder.image,
                        AsynImageUtil.getImageOptions(R.mipmap.location_view), null);
            } else {
                holder.image.setImageResource(R.mipmap.location_view);
            }
        }
        return convertView;
    }

    class ViewHolder {
        public ImageView image;
        public TextView title, content, des, distance;
    }
}
