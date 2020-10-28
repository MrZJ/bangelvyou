package com.shishoureport.system.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shishoureport.system.R;
import com.shishoureport.system.entity.WorkOverTimeEntity;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/1.
 * copyright easybiz.
 */

public class WorkOverTimeAdapter extends BaseAdapter {
    private Context mContext;
    private List<WorkOverTimeEntity> mData;

    public WorkOverTimeAdapter(Context context, List<WorkOverTimeEntity> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public WorkOverTimeEntity getItem(int position) {
        return mData == null ? new WorkOverTimeEntity() : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.work_over_time_item, null);
            holder = new ViewHolder();
            holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.time_lenth_tv = (TextView) convertView.findViewById(R.id.time_lenth_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        initHolder(holder, mData.get(position));
        return convertView;
    }

    private void initHolder(ViewHolder holder, WorkOverTimeEntity entity) {
        if (holder == null || entity == null) return;
        holder.name_tv.setText(entity.name);
        holder.time_lenth_tv.setText(entity.time_lenth);
        holder.time_tv.setText(entity.time);
    }

    class ViewHolder {
        TextView name_tv, time_tv, time_lenth_tv;
    }

    public void addData(WorkOverTimeEntity entity) {
        mData.add(entity);
        notifyDataSetChanged();
    }

    public List<WorkOverTimeEntity> getData() {
        return mData;
    }
}
