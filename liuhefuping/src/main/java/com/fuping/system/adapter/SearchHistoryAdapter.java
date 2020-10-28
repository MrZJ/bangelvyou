package com.fuping.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fuping.system.R;
import com.fuping.system.entity.SearchEntity;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/10/18.
 * copyright easybiz.
 */

public class SearchHistoryAdapter extends BaseAdapter {
    private List<SearchEntity> mData;
    private Context context;

    public SearchHistoryAdapter(List<SearchEntity> list, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search_history, null);
            holder = new ViewHolder();
            holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.rootvie = convertView.findViewById(R.id.del_iv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        initHolder(holder, mData.get(position), position);
        return convertView;
    }

    private void initHolder(ViewHolder holder, SearchEntity entity, final int pos) {
        if (holder == null || entity == null) {
            return;
        }
        holder.name_tv.setText(entity.villageSearch_desc);
        holder.rootvie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.remove(pos);
                notifyDataSetChanged();
            }
        });
    }

    public void addData(SearchEntity entity) {
        mData.add(0, entity);
        notifyDataSetChanged();
    }

    public List<SearchEntity> getData() {
        return mData;
    }

    private class ViewHolder {
        private TextView name_tv;
        private View rootvie;
    }
}
