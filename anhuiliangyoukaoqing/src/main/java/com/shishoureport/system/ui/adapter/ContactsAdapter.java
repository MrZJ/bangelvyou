package com.shishoureport.system.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.utils.FrescoHelper;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/7.
 * copyright easybiz.
 */

public class ContactsAdapter extends AbsBaseAdapter<User> {
    private List<User> mData;
    private Context mContext;

    public ContactsAdapter(Context context, List<User> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public void setData(List infos) {
        mData = infos;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public User getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_contacts, null);
            holder = new ViewHolder();
            holder.photo_sd = (SimpleDraweeView) convertView.findViewById(R.id.photo_sd);
            holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        initHolder(holder, mData.get(position));
        return convertView;
    }

    private void initHolder(ViewHolder holder, User user) {
        if (holder == null || user == null) return;
        holder.name_tv.setText(user.user_name);
        FrescoHelper.loadImage(user.positions, holder.photo_sd);
    }

    class ViewHolder {
        public SimpleDraweeView photo_sd;
        public TextView name_tv;
    }
}
