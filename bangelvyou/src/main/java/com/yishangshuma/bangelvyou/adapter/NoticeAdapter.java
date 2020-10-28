package com.yishangshuma.bangelvyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.entity.NoticeInfoEntity;

import java.util.List;

/**
 * 站内信适配器
 */
public class NoticeAdapter extends  AbsBaseAdapter<NoticeInfoEntity>{

    private List<NoticeInfoEntity> mData;
    private LayoutInflater inflater;

    public NoticeAdapter(List<NoticeInfoEntity> branchInfos, Context context) {
        mData = branchInfos;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {

        if (null == mData) {
            return 0;
        }
        return mData.size();
    }

    public NoticeInfoEntity getItem(int position) {

        return mData.get(position);
    }

    public long getItemId(int position) {

        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_notice, null);
            holder = new ViewHolder();
            holder.tv_notice_title = (TextView) convertView
                    .findViewById(R.id.tv_item_notice_title);
            holder.tv_notice_content = (TextView) convertView
                    .findViewById(R.id.tv_item_notice_content);
            holder.tv_notice_date = (TextView) convertView
                    .findViewById(R.id.tv_item_notice_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        NoticeInfoEntity entity = getItem(position);
        holder.tv_notice_title.setText(entity.noticeTitle);
        holder.tv_notice_content.setText(entity.noticeContnet);
        holder.tv_notice_date.setText(entity.noticeDate);
        return convertView;
    }

    private class ViewHolder {
        TextView tv_notice_title, tv_notice_content,tv_notice_date;
    }

    public void setData(List<NoticeInfoEntity> entity) {
        mData = entity;
    }
}
