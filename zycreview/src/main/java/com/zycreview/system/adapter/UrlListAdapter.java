package com.zycreview.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zycreview.system.R;
import com.zycreview.system.entity.UrlListBean;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2018/4/4.
 * copyright easybiz.
 */

public class UrlListAdapter extends BaseAdapter {
    private Context mContext;
    private List<UrlListBean> mData;

    public UrlListAdapter(Context context, List<UrlListBean> list) {
        this.mContext = context;
        this.mData = list;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Viewholder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_url_list, null);
            holder = new Viewholder();
            holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            convertView.setTag(holder);
        } else {
            holder = (Viewholder) convertView.getTag();
        }
        holder.name_tv.setText(mData.get(position).name);
        return convertView;
    }

    private static class Viewholder {
        protected TextView name_tv;
    }
}
