package com.basulvyou.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.basulvyou.system.R;
import com.basulvyou.system.entity.CommentItem;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/6/8.
 * copyright easybiz.
 */

public class EvaluateAdapter extends AbsBaseAdapter<CommentItem> {
    private List<CommentItem> mData;
    private Context mContext;
    private String comm_score;

    public EvaluateAdapter(List<CommentItem> mData, Context context) {
        this.mContext = context;
        this.mData = mData;
    }

    @Override
    public void setData(List<CommentItem> infos) {
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
    public CommentItem getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.evaluate_item, null);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.tag_tv = (TextView) convertView.findViewById(R.id.tag_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CommentItem item = mData.get(position);
        if (item != null) {
            holder.content.setText(item.content);
            holder.time.setText(item.date);
        }
        if ("5".equals(comm_score)) {
            holder.tag_tv.setText("好评");
        } else if ("3".equals(comm_score)) {
            holder.tag_tv.setText("中评");
        } else if ("1".equals(comm_score)) {
            holder.tag_tv.setText("差评");
        }
        return convertView;
    }

    class ViewHolder {
        TextView content, time, tag_tv;
    }

    public void setComm_score(String comm_score) {
        this.comm_score = comm_score;
    }
}
