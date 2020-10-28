package com.zycreview.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zycreview.system.R;
import com.zycreview.system.entity.SubPackageEntity;

import java.util.List;

/**
 * 药材分包适配器
 */
public class SubPackageAdapter extends  AbsBaseAdapter<SubPackageEntity>{

    private List<SubPackageEntity> mData;
    private LayoutInflater inflater;

    public SubPackageAdapter(List<SubPackageEntity> data,Context context){
        mData = data;
        inflater = LayoutInflater.from(context.getApplicationContext());

    }

    @Override
    public void setData(List<SubPackageEntity> infos) {
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
    public SubPackageEntity getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_instock_info, null);
            holder = new ViewHolder();
            holder.tv_medic_name = (TextView) convertView.findViewById(R.id.tv_item_instock_info_medicname);
            holder.tv_instock_date = (TextView) convertView.findViewById(R.id.tv_item_instock_info_instockdate);
            holder.tv_instock_weight = (TextView) convertView.findViewById(R.id.tv_item_instock_info_medicweight);
            holder.sub_package = (Button) convertView.findViewById(R.id.btn_item_instock_info_instock);
            holder.sub_package.setText("分包");
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SubPackageEntity entity = getItem(position);
        return convertView;
    }

    private class ViewHolder {
        TextView tv_medic_name, tv_instock_date , tv_instock_weight;
        Button sub_package;
    }
}
