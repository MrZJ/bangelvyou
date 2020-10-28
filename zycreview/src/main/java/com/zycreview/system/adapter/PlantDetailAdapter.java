package com.zycreview.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zycreview.system.R;
import com.zycreview.system.entity.PlantDetailEntity;
import com.zycreview.system.utils.StringUtil;

import java.util.List;

/**
 * 种植任务详情适配器
 */

public class PlantDetailAdapter extends AbsBaseAdapter<PlantDetailEntity> {

    private List<PlantDetailEntity> mData;
    private LayoutInflater inflater;

    public PlantDetailAdapter(List<PlantDetailEntity> data, Context context){
        mData = data;
        inflater = LayoutInflater.from(context.getApplicationContext());
    }

    @Override
    public void setData(List<PlantDetailEntity> infos) {
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
    public PlantDetailEntity getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_plant_detail, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_weight = (TextView) convertView.findViewById(R.id.tv_weight);
            holder.tv_weight_reality = (TextView) convertView.findViewById(R.id.tv_weight_reality);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PlantDetailEntity entity = getItem(position);
        if (entity != null) {
            if (!StringUtil.isEmpty(entity.drugs_name)) {
                    holder.tv_name.setText("药材名称:" + entity.drugs_name);
            } else {
                    holder.tv_name.setText("药材名称:无");
            }
            if (!StringUtil.isEmpty(entity.plan_output)) {
                holder.tv_weight.setText("预计产量:" + entity.plan_output);
            } else {
                holder.tv_weight.setText("预计产量:无");
            }
            if (!StringUtil.isEmpty(entity.receive_weight)) {
                holder.tv_weight_reality.setText("实际产量:" + entity.receive_weight);
            } else {
                holder.tv_weight_reality.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    private class ViewHolder {
        public TextView tv_name,tv_weight,tv_weight_reality;
    }

}
