package com.basulvyou.system.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.basulvyou.system.R;
import com.basulvyou.system.entity.CarpoolingInfoEntity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 我的拼车列表信息适配器
 */
public class MyCarpoolingAdapter extends  AbsBaseAdapter<CarpoolingInfoEntity>{

    private List<CarpoolingInfoEntity> mData;
    private LayoutInflater inflater;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public MyCarpoolingAdapter(List<CarpoolingInfoEntity> branchInfos, Context context) {
        mData = branchInfos;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {

        if (null == mData) {
            return 0;
        }
        return mData.size();
    }

    public CarpoolingInfoEntity getItem(int position) {

        return mData.get(position);
    }

    public long getItemId(int position) {

        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_my_carpooling, null);
            holder = new ViewHolder();
            holder.tv_carpooling_date = (TextView) convertView
                    .findViewById(R.id.tv_item_carpooling_date);
            holder.tv_carpooling_status = (TextView) convertView
                    .findViewById(R.id.tv_item_carpooling_status);
            holder.tv_carpooling_start = (TextView) convertView
                    .findViewById(R.id.tv_item_carpooling_start);
            holder.tv_carpooling_end = (TextView) convertView
                    .findViewById(R.id.tv_item_carpooling_end);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CarpoolingInfoEntity entity = getItem(position);
        if(entity.comm_type.equals("104")){
            holder.tv_carpooling_date.setText(entity.up_date);
        }else{
            holder.tv_carpooling_date.setText(entity.down_date);
        }
        if(entity.order_state.equals("0")){
            holder.tv_carpooling_status.setText("进行中");
            holder.tv_carpooling_status.setTextColor(Color.GREEN);
        }if(entity.order_state.equals("-10")){
            holder.tv_carpooling_status.setText("已取消");
            holder.tv_carpooling_status.setTextColor(Color.GRAY);
        }if(entity.order_state.equals("90")){
            holder.tv_carpooling_status.setText("已完成");
            holder.tv_carpooling_status.setTextColor(Color.GRAY);
        }
        holder.tv_carpooling_start.setText(entity.pcqd);
        holder.tv_carpooling_end.setText(entity.pczd);
        return convertView;
    }

    private class ViewHolder {
        TextView tv_carpooling_date, tv_carpooling_status,tv_carpooling_start,tv_carpooling_end;
    }

    public void setData(List<CarpoolingInfoEntity> entity) {
        mData = entity;
    }
}
