package com.shishoureport.system.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shishoureport.system.R;
import com.shishoureport.system.entity.TaskEntity;
import com.shishoureport.system.utils.TimeDateUtil;

import java.util.List;

import static com.shishoureport.system.entity.TaskEntity.TASK_COMPLET;

/**
 * Created by jianzhang.
 * on 2017/9/1.
 * copyright easybiz.
 */

public class FileAdapter extends AbsBaseAdapter<TaskEntity> {
    private Context mContext;
    private List<TaskEntity> mData;

    public FileAdapter(Context context, List<TaskEntity> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public void setData(List<TaskEntity> infos) {
        mData = infos;
    }

    @Override
    public TaskEntity getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_file_fragment, null);
            holder = new ViewHolder();
            holder.title_tv = (TextView) convertView.findViewById(R.id.title_tv);
            holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.status_tv = (TextView) convertView.findViewById(R.id.status_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        initHolder(holder, mData.get(position));
        return convertView;
    }

    private void initHolder(ViewHolder holder, TaskEntity entity) {
        if (holder == null || entity == null) return;
        holder.title_tv.setText(entity.task_name);
        holder.time_tv.setText(TimeDateUtil.date(entity.add_date));
        if (TASK_COMPLET == entity.task_audit) {
            holder.status_tv.setText("完成");
        } else {
            holder.status_tv.setText("未完成");
        }
    }

    class ViewHolder {
        TextView title_tv, time_tv, status_tv;
    }
}
