package com.zycreview.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.zycreview.system.R;
import com.zycreview.system.entity.FertilizerOrPesticideEntity;
import com.zycreview.system.utils.StringUtil;

import java.util.List;

/**
 * 种植任务适配器
 */

public class FertilizerOrPesticideAdapter extends AbsBaseAdapter<FertilizerOrPesticideEntity> {

    private List<FertilizerOrPesticideEntity> mData;
    private LayoutInflater inflater;
    private String type;
    private String plantType;
    private Context context;
    private OnDeleteListener listener;

    public FertilizerOrPesticideAdapter(List<FertilizerOrPesticideEntity> data, Context context, String type, String plantType){
        this.type = type;
        this.plantType = plantType;
        mData = data;
        inflater = LayoutInflater.from(context.getApplicationContext());
        this.context = context;
    }

    @Override
    public void setData(List<FertilizerOrPesticideEntity> infos) {
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
    public FertilizerOrPesticideEntity getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_shifei_or_nongyao, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.sl_item = (SwipeMenuLayout) convertView.findViewById(R.id.sl_item);
            holder.tv_weight = (TextView) convertView.findViewById(R.id.tv_weight);
            holder.tv_person = (TextView) convertView.findViewById(R.id.tv_person);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.bt_delete = (Button) convertView.findViewById(R.id.bt_delete);
            if (plantType.equals("历史")){
                holder.bt_delete.setVisibility(View.GONE);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final FertilizerOrPesticideEntity entity = getItem(position);
        if (entity != null) {
            if (!StringUtil.isEmpty(entity.yw_type)) {
                if (type.equals("施肥")) {
                    holder.tv_name.setText("施肥名称:  " + entity.yw_type);
                } else {
                    holder.tv_name.setText("农药名称:  " + entity.yw_type);
                }
            } else {
                if (type.equals("施肥")) {
                    holder.tv_name.setText("施肥名称:  无");
                } else {
                    holder.tv_name.setText("农药名称:  无");
                }
            }
            if (!StringUtil.isEmpty(entity.work_man)) {
                    holder.tv_person.setText("    作业人:  " + entity.work_man);
            } else {
                    holder.tv_person.setText("    作业人:  无");
            }
            if (!StringUtil.isEmpty(entity.yw_weight)) {
                if (type.equals("施肥")) {
                    holder.tv_weight.setText("施肥重量:  " + entity.yw_weight);
                } else {
                    holder.tv_weight.setText("农药重量:  " + entity.yw_weight);
                }
            } else {
                if (type.equals("施肥")) {
                    holder.tv_weight.setText("施肥重量:  无");
                } else {
                    holder.tv_weight.setText("农药重量:  无");
                }
            }
            if (!StringUtil.isEmpty(entity.work_date)) {
                if (type.equals("施肥")) {
                    holder.tv_time.setText("施肥时间:  " + entity.work_date);
                } else {
                    holder.tv_time.setText("喷药时间:  " + entity.work_date);
                }
            } else {
                if (type.equals("施肥")) {
                    holder.tv_time.setText("施肥时间:  无");
                } else {
                    holder.tv_time.setText("喷药时间:  无");
                }
            }
        }
        final ViewHolder finalHolder = holder;
        holder.bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalHolder.sl_item.quickClose();
                if (type.equals("施肥")) {
                    listener.deleteItem(entity.ferti_id, position);
                } else {
                    listener.deleteItem(entity.spray_id, position);
                }
            }
        });
        return convertView;
    }

    public interface OnDeleteListener{
        public void deleteItem(String id,int position);
    }

    public void setOnDeleteClickListener(OnDeleteListener listener){
        this.listener = listener;
    }

    private class ViewHolder {
        public TextView tv_name,tv_time,tv_weight,tv_person;
        public Button bt_delete;
        public SwipeMenuLayout sl_item;
    }

}
