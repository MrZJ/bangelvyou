package com.shishoureport.system.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.User;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/5.
 * copyright easybiz.
 */

public class ProcessAdapter extends BaseAdapter {
    private Context mContext;
    private List<User> mData;

    public ProcessAdapter(Context context, List<User> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_approve_process, null);
            holder = new ViewHolder();
            holder.photo_sd = (SimpleDraweeView) convertView.findViewById(R.id.photo_sd);
            holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder {
        SimpleDraweeView photo_sd;
        TextView name_tv, time_tv;

    }
}
