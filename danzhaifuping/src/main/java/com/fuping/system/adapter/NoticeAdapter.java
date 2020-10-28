package com.fuping.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuping.system.R;
import com.fuping.system.entity.NoticeEntity;
import com.fuping.system.utils.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 最新公告适配器
 */
public class NoticeAdapter extends AbsBaseAdapter<NoticeEntity> {

    private List<NoticeEntity> mData;
    private LayoutInflater inflater;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public NoticeAdapter(List<NoticeEntity> data,Context context){
        mData = data;
        inflater = LayoutInflater.from(context.getApplicationContext());
    }

    @Override
    public void setData(List<NoticeEntity> infos) {
        mData = infos;
    }

    @Override
    public int getCount() {
        if (null == mData) {
            return 0;
        }
        return mData.size();
    }

    @Override
    public NoticeEntity getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_notice_info, null);
            holder = new ViewHolder();
            holder.img_notice = (ImageView) convertView.findViewById(R.id.img_item_notice_info_img);
            holder.tv_notice_title = (TextView) convertView.findViewById(R.id.tv_item_notice_info_title);
            holder.tv_notice_sender = (TextView) convertView.findViewById(R.id.tv_item_notice_info_sender);
            holder.tv_notice_send_date = (TextView) convertView.findViewById(R.id.tv_item_notice_info_senddate);
            holder.tv_notice_read_num = (TextView) convertView.findViewById(R.id.tv_item_notice_info_readnum);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        NoticeEntity entity = getItem(position);
        holder.tv_notice_title.setText(entity.title);
        holder.tv_notice_send_date.setText(entity.pub_time);
        if(!StringUtil.isEmpty(entity.source)){
            holder.tv_notice_sender.setText("来源:"+entity.source);
        }
        holder.tv_notice_read_num.setText("阅读"+entity.view_count);
        return convertView;
    }

    private class ViewHolder {
        TextView tv_notice_title, tv_notice_sender , tv_notice_send_date, tv_notice_read_num;
        ImageView img_notice;
    }

}
