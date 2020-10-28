package com.shishoureport.system.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shishoureport.system.R;
import com.shishoureport.system.entity.AuditEntity;
import com.shishoureport.system.request.AttendanceAuditListRequest;
import com.shishoureport.system.utils.TimeDateUtil;

import java.util.List;

import static com.shishoureport.system.request.AttendanceAuditListRequest.TYPE_LEAVE_APP;

/**
 * Created by jianzhang.
 * on 2017/9/7.
 * copyright easybiz.
 */

public class AuditListAdapter extends AbsBaseAdapter<AuditEntity> {
    private List<AuditEntity> mData;
    private String mod_id;
    private Context mContext;
    private String audit_date;
    private String audit_name;
    private String add_date;

    public AuditListAdapter(Context context, List<AuditEntity> data, String mod_id) {
        mContext = context;
        mData = data;
        this.mod_id = mod_id;
        audit_date = mContext.getString(R.string.audit_date);
        audit_name = mContext.getString(R.string.audit_name);
        add_date = mContext.getString(R.string.add_date);
    }

    @Override
    public void setData(List<AuditEntity> infos) {
        mData = infos;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public AuditEntity getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_audit_list, null);
            holder = new ViewHolder();
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

    private void initHolder(ViewHolder holder, AuditEntity entity) {
        if (holder == null || entity == null) return;
        holder.start_time_tv.setText(String.format(add_date, TimeDateUtil.dateTime(entity.add_date)));
        holder.end_time_tv.setText(String.format(audit_date, TimeDateUtil.dateTime(entity.audit_date)));
        holder.type_tv.setText(String.format(audit_name, entity.audit_name));
        if (entity.map != null) {
            if (TYPE_LEAVE_APP.equals(mod_id)) {
                holder.title_tv.setText(entity.map.myName + "的请假单");
            } else if (AttendanceAuditListRequest.TYPE_BUSINESS_TRAVEL.equals(mod_id)) {
                holder.title_tv.setText(entity.map.myName + "的出差单");
            } else if (AttendanceAuditListRequest.TYPE_OVERT_TIME.equals(mod_id)) {
                holder.title_tv.setText(entity.map.myName + "的加班单");
            } else if (AttendanceAuditListRequest.TYPE_CAR_MANAGE.equals(mod_id)) {
                holder.title_tv.setText(entity.map.myName + "的用车单");
            }
        }
        Log.e("jianzhang", "" + entity.audit_state);
        if (entity.audit_state == 0) {
            holder.approve_status_tv.setText("审批中");
        } else if (entity.audit_state == -1) {
            holder.approve_status_tv.setText("审核不通过");
        } else if (entity.audit_state == 1) {
            holder.approve_status_tv.setText("审核通过");
        }
    }

    class ViewHolder {
        public TextView type_tv, start_time_tv, end_time_tv, approve_status_tv, title_tv;
    }
}
