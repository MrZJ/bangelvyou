package com.shishoureport.system.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.TaskPersonList;
import com.shishoureport.system.utils.TimeDateUtil;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/13.
 * copyright easybiz.
 */

public class FileStatusAdapter extends BaseAdapter {
    private List<TaskPersonList> mData;
    private Context mContext;

    public FileStatusAdapter(List<TaskPersonList> list, Context context) {
        this.mData = list;
        this.mContext = context;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_file_status, null);
            holder = new ViewHolder();
            holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.status_tv = (TextView) convertView.findViewById(R.id.status_tv);
            holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.photo_sd = (SimpleDraweeView) convertView.findViewById(R.id.photo_sd);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        initholder(holder, mData.get(position));
        return convertView;
    }

    private void initholder(ViewHolder holder, TaskPersonList entity) {
        if (holder == null || entity == null) return;
        holder.name_tv.setText(entity.accpect_person_name);
        if (entity.task_audit == 1) {
            holder.status_tv.setText("已接收");
        } else {
            holder.status_tv.setText("待接收");
        }
        if (entity.complete_date == 0) {
            holder.time_tv.setText("");
        } else {
            holder.time_tv.setText(TimeDateUtil.date(entity.complete_date));
        }
    }

    class ViewHolder {
        public TextView name_tv, status_tv, time_tv;
        public SimpleDraweeView photo_sd;
    }
}
