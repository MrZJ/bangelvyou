package com.fuping.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuping.system.R;
import com.fuping.system.entity.MyIntentEntity;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/10/10.
 * copyright easybiz.
 */

public class HomeTabAdapter extends BaseAdapter {
    private List<MyIntentEntity> mData;
    private Context context;

    public HomeTabAdapter(Context context, List<MyIntentEntity> list) {
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
    public MyIntentEntity getItem(int position) {
        return mData == null ? new MyIntentEntity() : mData.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.home_button_item, null);
            holder = new ViewHolder();
            holder.rootview = convertView.findViewById(R.id.rootview);
            holder.img = (ImageView) convertView.findViewById(R.id.img_home);
            holder.title = (TextView) convertView.findViewById(R.id.tv_home);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        initHolder(holder, mData.get(position));
        return convertView;
    }

    private void initHolder(ViewHolder h, MyIntentEntity entity) {
        if (h == null || entity == null) {
            return;
        }
        try {
            h.img.setImageResource(entity.img);
            h.title.setText(entity.title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<MyIntentEntity> getmData() {
        return mData;
    }

    private class ViewHolder {
        public TextView title;
        public ImageView img;
        public View rootview;
    }
}
