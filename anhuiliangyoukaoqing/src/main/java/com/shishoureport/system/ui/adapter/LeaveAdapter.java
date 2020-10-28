package com.shishoureport.system.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.AttendanceLeave;
import com.shishoureport.system.utils.TimeDateUtil;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/1.
 * copyright easybiz.
 */

public class LeaveAdapter extends AbsBaseAdapter<AttendanceLeave> {
    private Context mContext;
    private List<AttendanceLeave> mData;
    private String leave_type_tip;
    private String start_time_tip;
    private String end_time_tip;
    private String title_tip;
    private int mType;

    public LeaveAdapter(Context context, List<AttendanceLeave> data, int type) {
        this.mContext = context;
        this.mData = data;
        leave_type_tip = context.getString(R.string.leave_type_tip);
        start_time_tip = context.getString(R.string.start_time_tip);
        end_time_tip = context.getString(R.string.end_time_tip);
        title_tip = context.getString(R.string.title_tip);
        this.mType = type;
    }

    @Override
    public int getCount() {

        return mData == null ? 0 : mData.size();
    }

    @Override
    public void setData(List<AttendanceLeave> infos) {
        mData = infos;
    }

    @Override
    public AttendanceLeave getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.leave_item, null);
            holder = new ViewHolder();
            holder.photo_sd = (SimpleDraweeView) convertView.findViewById(R.id.photo_sd);
            holder.title_tv = (TextView) convertView.findViewById(R.id.title_tv);
            holder.type_tv = (TextView) convertView.findViewById(R.id.type_tv);
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

    private void initHolder(ViewHolder holder, AttendanceLeave leave) {
        if (holder != null && leave != null) {
//            FrescoHelper.loadImage(,holder.photo_sd);
            holder.type_tv.setText(leave_type_tip + leave.leave_type_name);
            holder.title_tv.setText(leave.add_uname + title_tip);
            holder.start_time_tv.setText(start_time_tip + TimeDateUtil.dateTime(leave.start_time));
            holder.end_time_tv.setText(end_time_tip + TimeDateUtil.dateTime(leave.end_time));
//            if (mType != TYPE_LEAVE_APP_WAITE_APPROVE) {
            if (leave.audit_state == AttendanceLeave.SUBMIT_OK) {
                holder.approve_status_tv.setText("审批通过");
            } else if (leave.audit_state == 0) {
                holder.approve_status_tv.setText("审批中");
            } else {
                holder.approve_status_tv.setText("审批未通过");
            }
//            } else {
//                holder.approve_status_tv.setVisibility(View.GONE);
//            }
        }
    }

    class ViewHolder {
        public SimpleDraweeView photo_sd;
        TextView title_tv, type_tv, start_time_tv, end_time_tv, approve_status_tv;
    }
}