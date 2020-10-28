package com.shishoureport.system.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.TopInfo;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/5/25.
 * copyright easybiz.
 */

public class HomeAdapter extends BaseAdapter {
    public Context mContext;
    private List<TopInfo> mData;

    public HomeAdapter(Context context) {
        this.mContext = context;
    }

    public void reSetDataAndNotify(List<TopInfo> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int i) {
        if (mData == null) {
            return null;
        }
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View converView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (converView == null) {
            holder = new ViewHolder();
            converView = LayoutInflater.from(mContext).inflate(R.layout.home_listview_item, null);
            holder.simpleDraweeView = (SimpleDraweeView) converView.findViewById(R.id.list_sdv);
            holder.timeTv = (TextView) converView.findViewById(R.id.list_time);
            converView.setTag(holder);
        } else {
            holder = (ViewHolder) converView.getTag();
        }
        TopInfo info = mData.get(i);
        holder.simpleDraweeView.setImageURI(info.adv_pic);
        holder.timeTv.setText(info.adv_title1);
        return converView;
    }

    public class ViewHolder {
        public SimpleDraweeView simpleDraweeView;
        public TextView timeTv;
    }
}
