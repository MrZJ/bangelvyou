package com.shishoureport.system.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shishoureport.system.R;
import com.shishoureport.system.entity.AttendanceAuditList;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/1.
 * copyright easybiz.
 */

public class ApprovePeopleListAdapter extends BaseAdapter {
    private Context mContext;
    private List<AttendanceAuditList> mData;

    public ApprovePeopleListAdapter(Context context, List<AttendanceAuditList> list) {
        this.mContext = context;
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
        Log.e("jianzhang", "getView");
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.add_people_item, null);
            holder = new ViewHolder();
            holder.arrow = (ImageView) convertView.findViewById(R.id.arrow);
            holder.photo = (ImageView) convertView.findViewById(R.id.photo);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(mData.get(position).audit_name);
        holder.photo.setImageResource(R.mipmap.default_photo_pic);
        if (position == mData.size() - 1) {
            holder.arrow.setVisibility(View.GONE);
        } else {
            holder.arrow.setVisibility(View.VISIBLE);
        }
        return convertView;
    }


    class ViewHolder {
        public ImageView arrow;
        public ImageView photo;
        public TextView name;
    }

}
