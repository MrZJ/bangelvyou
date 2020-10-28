package com.fuping.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fuping.system.R;
import com.fuping.system.entity.MenberEntity;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/10/10.
 * copyright easybiz.
 */

public class RelationAdapter extends BaseAdapter {
    private List<MenberEntity> mData;
    private Context context;

    public RelationAdapter(Context context, List<MenberEntity> list) {
        mData = list;
        this.context = context;
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
    public MenberEntity getItem(int position) {
        return mData == null ? new MenberEntity() : mData.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_relation, null);
            holder = new ViewHolder();
            holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.relation_tv = (TextView) convertView.findViewById(R.id.relation_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        initHolder(holder, mData.get(position));
        return convertView;
    }

    private void initHolder(ViewHolder h, MenberEntity entity) {
        if (h == null || entity == null) {
            return;
        }
        h.name_tv.setText(entity.family_name);
        h.relation_tv.setText(entity.relation_ship);
    }

    private class ViewHolder {
        public TextView name_tv, relation_tv;
    }
}
