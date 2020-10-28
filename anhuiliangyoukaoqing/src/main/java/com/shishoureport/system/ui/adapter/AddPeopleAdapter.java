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
import com.shishoureport.system.entity.User;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/1.
 * copyright easybiz.
 */

public class AddPeopleAdapter extends BaseAdapter {
    private Context mContext;
    private List<User> mData;

    public AddPeopleAdapter(Context context, List<User> list) {
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
        if (position == (mData.size() - 1)) {
            holder.arrow.setVisibility(View.GONE);
            holder.photo.setImageResource(R.mipmap.add_people);
            holder.name.setVisibility(View.GONE);
        } else {
            holder.photo.setImageResource(R.mipmap.default_photo_pic);
            holder.arrow.setVisibility(View.VISIBLE);
            holder.name.setText(mData.get(position).user_name);
            holder.name.setVisibility(View.VISIBLE);
        }
        return convertView;
    }


    class ViewHolder {
        public ImageView arrow;
        public ImageView photo;
        public TextView name;
    }

    public String getIds() {
        StringBuilder ids = new StringBuilder();
        for (int i = 0; i < mData.size() - 1; i++) {
            User user = mData.get(i);
            if (mData.size() - 2 == i) {
                ids.append(user.id);
            } else {
                ids.append(user.id + ",");
            }
        }
        return ids.toString();
    }

    public List<User> getData() {
        return mData;
    }
}
