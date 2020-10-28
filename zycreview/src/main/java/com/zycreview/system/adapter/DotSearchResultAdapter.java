package com.zycreview.system.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zycreview.system.R;
import com.zycreview.system.entity.DotSearchResultEntity;

import java.util.List;

/**
 * 查询结果信息显示适配器
 */
public class DotSearchResultAdapter extends AbsBaseAdapter<DotSearchResultEntity>{

    private List<DotSearchResultEntity> mData;
    private LayoutInflater inflater;
    private String dataType;

    public  DotSearchResultAdapter(List<DotSearchResultEntity> data,Context context){
        mData = data;
        inflater = LayoutInflater.from(context.getApplicationContext());
    }

    @Override
    public void setData(List<DotSearchResultEntity> infos) {
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
    public DotSearchResultEntity getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_dot_search_result_info, null);
            holder = new ViewHolder();
            holder.tv_result_area = (TextView) convertView.findViewById(R.id.tv_item_dot_search_result_area);
            holder.tv_result_type = (TextView) convertView.findViewById(R.id.tv_item_dot_search_result_type);
            holder.tv_result_name = (TextView) convertView.findViewById(R.id.tv_item_dot_search_result_name);
            holder.tv_result_area.setTextColor(Color.parseColor("#666666"));
            holder.tv_result_type.setTextColor(Color.parseColor("#666666"));
            holder.tv_result_name.setTextColor(Color.parseColor("#666666"));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DotSearchResultEntity entity = getItem(position);
        holder.tv_result_name.setText(entity.entp_name);
        if(dataType.equals("1")){
            holder.tv_result_type.setText(entity.link_mobile);
            holder.tv_result_area.setText(entity.link_man);
        }else{
            holder.tv_result_type.setText(entity.entp_type_name);
            holder.tv_result_area.setText(entity.p_index_name);
        }
        return convertView;
    }

    public  void setDataType(String type){
        this.dataType= type;
    }

    private class ViewHolder {
        TextView tv_result_area, tv_result_type , tv_result_name;
    }

}
