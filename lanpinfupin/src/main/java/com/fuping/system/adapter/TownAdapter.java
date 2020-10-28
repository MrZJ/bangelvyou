package com.fuping.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fuping.system.R;
import com.fuping.system.entity.TownEntity;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/10/10.
 * copyright easybiz.
 */

public class TownAdapter extends AbsBaseAdapter<TownEntity> {
    private List<TownEntity> mData;
    private Context context;
    private String duchaFormat;

    public TownAdapter(Context context, List<TownEntity> list) {
        mData = list;
        this.context = context;
        duchaFormat = context.getString(R.string.ducha_format);
    }

    @Override
    public void setData(List<TownEntity> infos) {
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
    public TownEntity getItem(int position) {
        return mData == null ? new TownEntity() : mData.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_town, null);
            holder = new ViewHolder();
            holder.ducha_num_tv = (TextView) convertView.findViewById(R.id.ducha_num_tv);
            holder.not_ducha_num_tv = (TextView) convertView.findViewById(R.id.not_ducha_num_tv);
            holder.town_name_tv = (TextView) convertView.findViewById(R.id.town_name_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        initHolder(holder, mData.get(position));
        return convertView;
    }

    private void initHolder(ViewHolder h, TownEntity entity) {
        if (h == null || entity == null) {
            return;
        }
        h.town_name_tv.setText(entity.town_p_name);
        try {
            h.ducha_num_tv.setText(String.format(duchaFormat, entity.yes_poor_count));
            h.not_ducha_num_tv.setText(String.format(duchaFormat, entity.no_poor_count));
        } catch (Exception e) {

        }

    }

    private class ViewHolder {
        public TextView town_name_tv, not_ducha_num_tv, ducha_num_tv;
    }
}
