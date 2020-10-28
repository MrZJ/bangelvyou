package com.fuping.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fuping.system.R;
import com.fuping.system.entity.TongJiEntity;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/10/18.
 * copyright easybiz.
 */

public class TongJiAdapter extends BaseAdapter {
    private List<TongJiEntity> mData;
    private Context context;

    public TongJiAdapter(List<TongJiEntity> list, Context context) {
        this.context = context;
        this.mData = list;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_tongji, null);
            holder = new ViewHolder();
            holder.title_tv = (TextView) convertView.findViewById(R.id.title_tv);
            holder.percent_tv = (TextView) convertView.findViewById(R.id.percent_tv);
            holder.num_tv = (TextView) convertView.findViewById(R.id.num_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        initHolder(holder, mData.get(position));
        return convertView;
    }

    private void initHolder(ViewHolder holder, TongJiEntity entity) {
        if (holder == null || entity == null) {
            return;
        }
        holder.title_tv.setText(entity.title);
        holder.num_tv.setText(entity.count);
        holder.percent_tv.setText(entity.percent + "%");
    }

    private class ViewHolder {
        private TextView title_tv, percent_tv, num_tv;
    }
}
