package com.yishangshuma.bangelvyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.entity.PointEntity;

import java.util.List;

/**
 * 积分明细适配器
 */
public class PointDetailAdapter extends  AbsBaseAdapter<PointEntity>{

    private List<PointEntity> mData;
    private LayoutInflater inflater;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public PointDetailAdapter(List<PointEntity> branchInfos, Context context) {
        mData = branchInfos;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {

        if (null == mData) {
            return 0;
        }
        return mData.size();
    }

    public PointEntity getItem(int position) {

        return mData.get(position);
    }

    public long getItemId(int position) {

        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_point_detail, null);
            holder = new ViewHolder();
            holder.tv_point_title = (TextView) convertView
                    .findViewById(R.id.item_point_title);
            holder.tv_point_date = (TextView) convertView
                    .findViewById(R.id.item_point_date);
            holder.tv_point_num = (TextView) convertView
                    .findViewById(R.id.item_point_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PointEntity entity = getItem(position);
        holder.tv_point_title.setText(entity.record_name);
        holder.tv_point_date.setText(entity.hd_date);
        holder.tv_point_num.setText(entity.hd_score);
        return convertView;
    }

    private class ViewHolder {
        TextView tv_point_title, tv_point_date,tv_point_num;
    }

    public void setData(List<PointEntity> entity) {
        mData = entity;
    }

}

