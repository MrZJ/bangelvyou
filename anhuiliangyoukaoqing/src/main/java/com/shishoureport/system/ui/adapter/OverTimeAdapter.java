package com.shishoureport.system.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.OverTimeEntity;
import com.shishoureport.system.utils.StringUtil;
import com.shishoureport.system.utils.TimeDateUtil;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/1.
 * copyright easybiz.
 */

public class OverTimeAdapter extends AbsBaseAdapter<OverTimeEntity> {
    private Context mContext;
    private List<OverTimeEntity> mData;
    private String leave_type_tip;
    private String start_time_tip;
    private String end_time_tip;
    private String title_tip;
    private boolean isApprove;

    public OverTimeAdapter(Context context, List<OverTimeEntity> data, boolean isApprove) {
        this.mContext = context;
        this.mData = data;
        leave_type_tip = context.getString(R.string.leave_type_tip);
        start_time_tip = context.getString(R.string.start_time_tip);
        end_time_tip = context.getString(R.string.end_time_tip);
        title_tip = context.getString(R.string.title_tip);
        this.isApprove = isApprove;
    }

    @Override
    public int getCount() {

        return mData == null ? 0 : mData.size();
    }

    @Override
    public void setData(List<OverTimeEntity> infos) {
        mData = infos;
    }

    @Override
    public OverTimeEntity getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_over_time, null);
            holder = new ViewHolder();
            holder.photo_sd = (SimpleDraweeView) convertView.findViewById(R.id.photo_sd);
            holder.title_tv = (TextView) convertView.findViewById(R.id.title_tv);
            holder.start_time_tv = (TextView) convertView.findViewById(R.id.start_time_tv);
            holder.end_time_tv = (TextView) convertView.findViewById(R.id.end_time_tv);
            holder.approve_status_tv = (TextView) convertView.findViewById(R.id.approve_status_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        initHolder(holder, mData.get(position));
        return convertView;
    }

    private void initHolder(ViewHolder holder, OverTimeEntity entity) {
        if (holder != null && entity != null) {
            if ("1".equals(entity.over_time_type)) {
                holder.title_tv.setText(String.format("%s的集体加班单", entity.add_uname));
            } else {
                holder.title_tv.setText(String.format("%s的个人加班单", entity.add_uname));
            }

            if (!isApprove) {
                if (entity.map != null) {
                    holder.start_time_tv.setText(String.format("开始时间：%s", entity.map.overtime_start));
                    holder.end_time_tv.setText(String.format("结束时间：%s", entity.map.overtime_end));
                }
            } else {
                String time = TimeDateUtil.dateTime(entity.overtime_date);
                if (!StringUtil.isEmpty(time)) {
                    holder.start_time_tv.setText(String.format("开始时间：%s", time));
                }
                holder.end_time_tv.setVisibility(View.GONE);
            }
            if (entity.audit_state == 1) {
                holder.approve_status_tv.setText("审批通过");
            } else if (entity.audit_state == 0) {
                holder.approve_status_tv.setText("审批中");
            } else {
                holder.approve_status_tv.setText("审批未通过");
            }
        }
    }

    class ViewHolder {
        public SimpleDraweeView photo_sd;
        TextView title_tv, start_time_tv, end_time_tv, approve_status_tv;
    }
}
