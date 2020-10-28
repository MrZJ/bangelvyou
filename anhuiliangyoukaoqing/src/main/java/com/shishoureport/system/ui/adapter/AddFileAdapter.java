package com.shishoureport.system.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shishoureport.system.R;
import com.shishoureport.system.entity.SelFileBean;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/1.
 * copyright easybiz.
 */

public class AddFileAdapter extends BaseAdapter {
    private Context mContext;
    private List<SelFileBean> mData;

    public AddFileAdapter(Context context, List<SelFileBean> list) {
        this.mContext = context;
        this.mData = list;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData == null ? new SelFileBean() : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Log.e("jianzhang", "getView");
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.add_file_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.del_tv = (TextView) convertView.findViewById(R.id.del_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(mData.get(position).file_path);
        holder.del_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }


    class ViewHolder {
        public TextView name, del_tv;
    }

    public void addData(String path) {
        SelFileBean bean = new SelFileBean();
        bean.file_path = path;
        mData.add(bean);
        notifyDataSetChanged();
    }

    public List<SelFileBean> getData() {
        return mData;
    }

}
