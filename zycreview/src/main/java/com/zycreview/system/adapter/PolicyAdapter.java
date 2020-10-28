package com.zycreview.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zycreview.system.R;
import com.zycreview.system.entity.PolicyEntity;
import com.zycreview.system.utils.StringUtil;

import java.util.List;

/**
 * 政策信息适配器
 */
public class PolicyAdapter extends  AbsBaseAdapter<PolicyEntity>{

    private List<PolicyEntity> mData;
    private LayoutInflater inflater;

    public PolicyAdapter(List<PolicyEntity> data,Context context){
        mData = data;
        inflater = LayoutInflater.from(context.getApplicationContext());

    }

    @Override
    public void setData(List<PolicyEntity> infos) {
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
    public PolicyEntity getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_policy_info, null);
            holder = new ViewHolder();
            holder.tv_policy_title = (TextView) convertView.findViewById(R.id.tv_item_policy_info_title);
            holder.tv_policy_sender = (TextView) convertView.findViewById(R.id.tv_item_policy_info_sender);
            holder.tv_policy_send_date = (TextView) convertView.findViewById(R.id.tv_item_policy_info_senddate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PolicyEntity entity = getItem(position);
        holder.tv_policy_title.setText(entity.title);
        if(!StringUtil.isEmpty(entity.source)){
            holder.tv_policy_sender.setText("来源:"+entity.source);
        }
        if(!StringUtil.isEmpty(entity.pub_time)){
            holder.tv_policy_send_date.setText(entity.pub_time);
        }else{
            holder.tv_policy_send_date.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tv_policy_title, tv_policy_sender , tv_policy_send_date;
    }
}
