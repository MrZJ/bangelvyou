package com.basulvyou.system.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.basulvyou.system.R;
import com.basulvyou.system.entity.PassengerEntity;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/6/21.
 * copyright easybiz.
 */

public class OrderStatusAdapter extends BaseAdapter {
    public interface ListItemClick {
        void onItemClick(int p);
    }

    public ListItemClick listItemClick;
    private Context mContext;
    private List<PassengerEntity> mData;
    private String resType;//是车主还是乘客
    private boolean isComfrim;//是否为待确认栏目
    private boolean isRobbed;
    private int color_evaluate;
    private int color_not_evaluate;
    private String userName;

    public OrderStatusAdapter(ListItemClick listItemClick, Context mContext, List<PassengerEntity> mData, String resType, boolean isComfrim, boolean isRobbed, String userName) {
        this.listItemClick = listItemClick;
        this.mContext = mContext;
        this.mData = mData;
        this.resType = resType;
        this.isComfrim = isComfrim;
        this.isRobbed = isRobbed;
        this.userName = userName;
        color_evaluate = Color.parseColor("#82C182");
        color_not_evaluate = Color.parseColor("#FFBC78");
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData == null ? 0 : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.order_status_item, null);
            holder.order_status = (TextView) convertView.findViewById(R.id.order_status);
            holder.button = (TextView) convertView.findViewById(R.id.button);
            holder.passenger_name = (TextView) convertView.findViewById(R.id.passenger_name);
            holder.name_tip = (TextView) convertView.findViewById(R.id.name_tip);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PassengerEntity entity = mData.get(position);
        if (entity != null) {
            if (isComfrim) {
                holder.button.setText("确定");
                if ("0".equals(entity.order_state)) {
                    holder.order_status.setTextColor(color_not_evaluate);
                    holder.order_status.setText("待确定");
                } else {
                    holder.order_status.setTextColor(color_evaluate);
                    holder.order_status.setText("已确定");
                }
            } else {
                holder.button.setText("去评价");
                if ("102".equals(resType)) {//乘客
                    if ("0".equals(entity.is_pj_ck)) {
                        holder.order_status.setTextColor(color_not_evaluate);
                        holder.order_status.setText("未评论");
                    } else {
                        holder.order_status.setTextColor(color_evaluate);
                        holder.order_status.setText("已评论");
                    }
                } else {//车主
                    if ("0".equals(entity.is_pj_zz)) {
                        holder.order_status.setTextColor(color_not_evaluate);
                        holder.order_status.setText("未评论");
                    } else {
                        holder.order_status.setTextColor(color_evaluate);
                        holder.order_status.setText("已评论");
                    }
                }
            }
            if ("102".equals(resType)) {//乘客
                holder.name_tip.setText("车主信息：");
            } else {
                holder.name_tip.setText("乘客信息：");
            }
            if (isRobbed) {
                holder.passenger_name.setText(userName);
            } else {
                holder.passenger_name.setText(entity.rel_name);
            }
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listItemClick != null) {
                        listItemClick.onItemClick(position);
                    }
                }
            });
        }
        return convertView;
    }

    public class ViewHolder {
        public TextView order_status, button, passenger_name, name_tip;
    }
}
