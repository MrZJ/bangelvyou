package com.shishoureport.system.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shishoureport.system.R;
import com.shishoureport.system.entity.AuditEntity;
import com.shishoureport.system.entity.MedicalEntity;
import com.shishoureport.system.request.AttendanceAuditListRequest;
import com.shishoureport.system.utils.TimeDateUtil;

import java.util.List;

import static com.shishoureport.system.request.AttendanceAuditListRequest.TYPE_LEAVE_APP;

/**
 * Created by jianzhang.
 * on 2017/9/7.
 * copyright easybiz.
 */

public class MedicalListAdapter extends AbsBaseAdapter<MedicalEntity> {
    private List<MedicalEntity> mData;
    private Context mContext;
    private boolean isReserve;

    public MedicalListAdapter(Context context, List<MedicalEntity> data, boolean isReserve) {
        this.isReserve = isReserve;
        mContext = context;
        mData = data;
    }

    @Override
    public void setData(List<MedicalEntity> infos) {
        mData = infos;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public MedicalEntity getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_medical_list, null);
            holder = new ViewHolder();
            holder.reagentName = (TextView) convertView.findViewById(R.id.reagentName);
            holder.code = (TextView) convertView.findViewById(R.id.code);
            holder.supplierName = (TextView) convertView.findViewById(R.id.supplierName);
            holder.quantity = (TextView) convertView.findViewById(R.id.quantity);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        initHolder(holder, mData.get(position));
        return convertView;
    }

    private void initHolder(ViewHolder holder, MedicalEntity entity) {
        if (holder == null || entity == null) return;
        holder.reagentName.setText("试剂名称：" + (isReserve ? entity.reagentName : entity.name));
        holder.code.setText("规格型号：" + entity.code);
        holder.supplierName.setText("供应商：" + entity.supplierName);
        if (!TextUtils.isEmpty(entity.quantity)) {
            holder.quantity.setText("库存数量：" + entity.quantity);
        } else {
            holder.quantity.setVisibility(View.GONE);
        }
    }

    static class ViewHolder {
        TextView reagentName, code, supplierName, quantity;
    }
}
