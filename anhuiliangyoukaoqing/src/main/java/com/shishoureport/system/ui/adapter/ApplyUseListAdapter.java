package com.shishoureport.system.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shishoureport.system.R;
import com.shishoureport.system.entity.ApplyPurchaseEntity;
import com.shishoureport.system.entity.ApplyUseEntity;
import com.shishoureport.system.utils.TimeDateUtil;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/1.
 * copyright easybiz.
 */

public class ApplyUseListAdapter extends AbsBaseAdapter<ApplyUseEntity> {
    private Context mContext;
    private List<ApplyUseEntity> mData;

    public ApplyUseListAdapter(Context context, List<ApplyUseEntity> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {

        return mData == null ? 0 : mData.size();
    }

    @Override
    public void setData(List<ApplyUseEntity> infos) {
        mData = infos;
    }

    @Override
    public ApplyUseEntity getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_apply_worker, null);
            holder = new ViewHolder();
            holder.people_tv = (TextView) convertView.findViewById(R.id.people_tv);
            holder.num_tv = (TextView) convertView.findViewById(R.id.num_tv);
            holder.start_time_tv = (TextView) convertView.findViewById(R.id.start_time_tv);
            holder.send_time_tv = (TextView) convertView.findViewById(R.id.send_time_tv);
            holder.start_place_tv = (TextView) convertView.findViewById(R.id.start_place_tv);
            holder.end_place_tv = (TextView) convertView.findViewById(R.id.end_place_tv);
            holder.status_tv = (TextView) convertView.findViewById(R.id.status_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        initHolder(holder, mData.get(position));
        return convertView;
    }

    private void initHolder(ViewHolder holder, ApplyUseEntity entity) {
        if (holder == null || entity == null) {
            return;
        }
        holder.people_tv.setText("批次：" + entity.batch);
        holder.num_tv.setText("审核人：" + entity.auditName);
        holder.start_time_tv.setText("出库人：" + entity.outName);
        holder.send_time_tv.setText("出库状态：" + (entity.outState.equalsIgnoreCase("0") ? "待出库" : "已出库"));
        holder.start_place_tv.setText("申请人：" + entity.applyName);
        holder.end_place_tv.setText("内容：" + entity.remark);
        if ("0".equals(entity.auditState)) {
            holder.status_tv.setText("审批中");
        } else if ("1".equals(entity.auditState)) {
            holder.status_tv.setText("审核通过");
        } else {
            holder.status_tv.setText("审核不通过");
        }

    }

    private class ViewHolder {
        private TextView people_tv, num_tv, start_time_tv, send_time_tv, start_place_tv, end_place_tv, status_tv;
    }
}
