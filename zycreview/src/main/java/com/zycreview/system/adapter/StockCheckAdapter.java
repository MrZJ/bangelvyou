package com.zycreview.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zycreview.system.R;
import com.zycreview.system.entity.StockCheckEntity;

import java.util.List;

/**
 * 库存查询适配器
 */
public class StockCheckAdapter extends  AbsBaseAdapter<StockCheckEntity>{

    private List<StockCheckEntity> mData;
    private LayoutInflater inflater;

    public StockCheckAdapter(List<StockCheckEntity> data,Context context){
        mData = data;
        inflater = LayoutInflater.from(context.getApplicationContext());
    }

    @Override
    public void setData(List<StockCheckEntity> infos) {
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
    public StockCheckEntity getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_stock_check, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_item_stock_check_name);
            holder.tv_total_weight = (TextView) convertView.findViewById(R.id.tv_item_stock_check_totalweight);
            holder.tv_weight = (TextView) convertView.findViewById(R.id.tv_item_stock_check_weight);
            holder.tv_outweight = (TextView) convertView.findViewById(R.id.tv_item_stock_check_outweight);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        StockCheckEntity entity = getItem(position);
        holder.tv_name.setText("药材名称: "+entity.drugs_name);
        holder.tv_total_weight.setText("药材总量: "+entity.drugs_number + entity.drugs_unit);
        holder.tv_weight.setText("在库量: "+entity.drugs_in + entity.drugs_unit);
        holder.tv_outweight.setText("出库量: "+entity.drugs_out + entity.drugs_unit);
        return convertView;
    }

    private class ViewHolder {
        TextView tv_name, tv_total_weight,tv_weight, tv_outweight;
    }
}
