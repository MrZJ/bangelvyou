package com.shishoureport.system.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shishoureport.system.R;
import com.shishoureport.system.entity.BaseDataEntity;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/5.
 * copyright easybiz.
 */

public class BaseDataDialogAdapter extends BaseAdapter {
    private Context mContext;
    private List<BaseDataEntity> mData;

    public BaseDataDialogAdapter(Context context, List<BaseDataEntity> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.dialog_item, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(mData.get(position).type_name);
        return convertView;
    }

    public class ViewHolder {
        public TextView textView;
    }
}
