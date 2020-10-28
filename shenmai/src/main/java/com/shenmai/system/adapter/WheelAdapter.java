package com.shenmai.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.shenmai.system.R;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bank, null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.item_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String bankName = (String) getItem(position);
        viewHolder.textView.setText(bankName);
        return convertView;
    }

    static class ViewHolder {
        TextView textView;
    }

}
