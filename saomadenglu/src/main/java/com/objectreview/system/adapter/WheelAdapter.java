package com.objectreview.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.objectreview.system.R;
import com.objectreview.system.entity.LogisticEntity;
import com.wx.wheelview.adapter.BaseWheelAdapter;

/**
 * 物流信息适配
 */
public class WheelAdapter extends BaseWheelAdapter {

    private  Context context;

    public WheelAdapter(Context ctx){
        context =  ctx;
    }

    @Override
    protected View bindView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_logistics, null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.item_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LogisticEntity entity = (LogisticEntity) getItem(position);
        viewHolder.textView.setText(entity.wl_name);
        return convertView;
    }

    static class ViewHolder {
        TextView textView;
    }

}
