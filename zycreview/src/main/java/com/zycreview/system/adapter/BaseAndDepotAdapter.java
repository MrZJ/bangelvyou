package com.zycreview.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zycreview.system.R;
import com.zycreview.system.entity.BaseAndDepotEntity;

import java.util.List;

/**
 * 基地和仓库适配器
 */
public class BaseAndDepotAdapter extends  AbsBaseAdapter<BaseAndDepotEntity>{

    private List<BaseAndDepotEntity> mData;
    private LayoutInflater inflater;

    public BaseAndDepotAdapter(List<BaseAndDepotEntity> data,Context context){
        mData = data;
        inflater = LayoutInflater.from(context.getApplicationContext());

    }

    @Override
    public void setData(List<BaseAndDepotEntity> infos) {
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
    public BaseAndDepotEntity getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_base_and_depot, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_base_or_depot_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BaseAndDepotEntity entity = getItem(position);
        return convertView;
    }

    private class ViewHolder {
        TextView tv_name;
    }
}
