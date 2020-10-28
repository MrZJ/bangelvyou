package com.fuping.system.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fuping.system.R;
import com.fuping.system.entity.HelpEntity;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/10/10.
 * copyright easybiz.
 */

public class HelpAdapter extends AbsBaseAdapter<HelpEntity> {
    private List<HelpEntity> mData;
    private Context context;

    public HelpAdapter(Context context, List<HelpEntity> list) {
        mData = list;
        this.context = context;
    }

    @Override
    public void setData(List<HelpEntity> infos) {
        mData = infos;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public HelpEntity getItem(int position) {
        return mData == null ? new HelpEntity() : mData.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_help, null);
            holder = new ViewHolder();
            holder.contry_tv = (TextView) convertView.findViewById(R.id.contry_tv);
            holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.goverment_tv = (TextView) convertView.findViewById(R.id.goverment_tv);
            holder.task_name_tv = (TextView) convertView.findViewById(R.id.task_name_tv);
            holder.rootview = convertView.findViewById(R.id.rootview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        initHolder(holder, mData.get(position));
        return convertView;
    }

    private void initHolder(ViewHolder h, HelpEntity entity) {
        if (h == null || entity == null) {
            return;
        }
        h.contry_tv.setText(entity.link_village_name);
        h.goverment_tv.setText(entity.link_unit_name);
        h.time_tv.setText(entity.pub_time);
        h.task_name_tv.setText(entity.task_name);
        try {
            h.rootview.setBackgroundColor(Color.parseColor(entity.show_color));
        } catch (Exception e) {
        }
    }

    private class ViewHolder {
        public TextView contry_tv, goverment_tv, time_tv, task_name_tv;
        public View rootview;
    }
}
