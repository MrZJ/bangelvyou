package com.zycreview.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zycreview.system.R;
import com.zycreview.system.entity.MedicInstockEntity;

import java.util.List;

/**
 * 交易单药材适配器
 */
public class TradedDrugsAdapter extends  AbsBaseAdapter<MedicInstockEntity>{

    private List<MedicInstockEntity> mData;
    private LayoutInflater inflater;

    public TradedDrugsAdapter(List<MedicInstockEntity> data,Context context){
        mData = data;
        inflater = LayoutInflater.from(context.getApplicationContext());

    }

    @Override
    public void setData(List<MedicInstockEntity> infos) {
        mData = infos;
    }

    @Override
    public int getCount() {
        if (null == mData) {
            return 0;
        }
        return mData.size();
    }

    @Override
    public MedicInstockEntity getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_traded_drugs, null);
            holder = new ViewHolder();
            holder.tv_medic_name = (TextView) convertView.findViewById(R.id.tv_item_traded_drugs_name);
            holder.tv_weight = (TextView) convertView.findViewById(R.id.tv_item_traded_drugs_weight);
            holder.tv_traded_date = (TextView) convertView.findViewById(R.id.tv_item_traded_drugs_date);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_item_traded_drugs_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MedicInstockEntity entity = getItem(position);
        holder.tv_medic_name.setText("药材名称: "+entity.ingred_name);
        holder.tv_weight.setText("交易重量: "+entity.trade_num + entity.trade_unit);
        holder.tv_traded_date.setText("交易时间: "+ entity.add_date);
        holder.tv_price.setText("药材单价: "+entity.price);
        return convertView;
    }

    private class ViewHolder {
        TextView tv_medic_name, tv_traded_date , tv_weight,tv_price;
    }
}
