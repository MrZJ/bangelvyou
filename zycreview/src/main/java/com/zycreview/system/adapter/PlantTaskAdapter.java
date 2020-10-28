package com.zycreview.system.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.zycreview.system.R;
import com.zycreview.system.entity.PlantTaskEntity;
import com.zycreview.system.ui.activity.FertilizerOrPesticideActivity;
import com.zycreview.system.ui.activity.HarvestManageActivity;
import com.zycreview.system.ui.activity.PlantDetailActivity;
import com.zycreview.system.utils.StringUtil;

import java.util.HashMap;
import java.util.List;

/**
 * 种植任务适配器
 */

public class PlantTaskAdapter extends AbsBaseAdapter<PlantTaskEntity> {

    private List<PlantTaskEntity> mData;
    private LayoutInflater inflater;
    private Context context;
    private String plantType;
    private HashMap<Integer,String> isShowBottom;
    private OnDeleteListener listener;

    public PlantTaskAdapter(List<PlantTaskEntity> data, Context context,String plantType){
        mData = data;
        this.context = context;
        this.plantType = plantType;
        inflater = LayoutInflater.from(context.getApplicationContext());
    }

    @Override
    public void setData(List<PlantTaskEntity> infos) {
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
    public PlantTaskEntity getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_plant_task_info, null);
            holder = new ViewHolder();
            holder.ll_parent = convertView.findViewById(R.id.ll_parent);
            holder.tv_plant_task = (TextView) convertView.findViewById(R.id.tv_plant_task);
            holder.vs_item_add_button = (ViewStub) convertView.findViewById(R.id.vs_item_add_button);
            holder.tv_plant_time = (TextView) convertView.findViewById(R.id.tv_plant_time);
            holder.bt_delete = (Button) convertView.findViewById(R.id.bt_delete);
            holder.sl_item_plant_task = (SwipeMenuLayout)convertView.findViewById(R.id.sl_item_plant_task);
            convertView.setTag(holder);
            if (plantType.equals("历史")) {
                holder.bt_delete.setVisibility(View.GONE);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final PlantTaskEntity entity = getItem(position);
        holder.vs_item_add_button.setTag("gone");
        if (entity != null) {
            if (!StringUtil.isEmpty(entity.name)) {
                holder.tv_plant_task.setText("种植任务:  "+entity.name);
            } else {
                holder.tv_plant_task.setText("种植任务:  无");
            }
            if (!StringUtil.isEmpty(entity.plant_date)) {
                holder.tv_plant_time.setText("种植时间:  "+entity.plant_date);
            } else {
                holder.tv_plant_time.setText("种植时间:  无");
            }
        }
        if(null != isShowBottom.get(position) && isShowBottom.get(position).equals("show")){
            holder.vs_item_add_button.setVisibility(View.VISIBLE);
            holder.vs_item_add_button.setTag("visible");
        } else {
            holder.vs_item_add_button.setVisibility(View.GONE);
            holder.vs_item_add_button.setTag("gone");
        }
        final ViewHolder finalHolder = holder;
        holder.ll_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (finalHolder.vs_item_add_button.getTag().equals("visible")) {
                        finalHolder.vs_item_add_button.setVisibility(View.GONE);
                        finalHolder.vs_item_add_button.setTag("gone");
                        isShowBottom.put(position,"unShow");
                    } else {
                        isShowBottom.put(position,"show");
                        if (finalHolder.vs_item_add_button instanceof ViewStub && finalHolder.ll_bt==null) {
                            finalHolder.ll_bt = finalHolder.vs_item_add_button.inflate();
                            finalHolder.bt_nongyao = (Button) finalHolder.ll_bt.findViewById(R.id.bt_nongyao);
                            finalHolder.bt_shifei = (Button) finalHolder.ll_bt.findViewById(R.id.bt_shifei);
                            finalHolder.bt_caishou = (Button) finalHolder.ll_bt.findViewById(R.id.bt_caishou);
                            finalHolder.bt_detail = (Button) finalHolder.ll_bt.findViewById(R.id.bt_detail);
                            if (plantType.equals("历史")){
                                finalHolder.bt_caishou.setVisibility(View.GONE);
                            }
                            initOnClick(finalHolder,entity);
                        }
                        finalHolder.vs_item_add_button.setVisibility(View.VISIBLE);
                        finalHolder.vs_item_add_button.setTag("visible");
                    }
                }
        });
        holder.bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalHolder.sl_item_plant_task.quickClose();
                listener.deleteItem(entity.plant_id,position);
            }
        });
        return convertView;
    }

    private void initOnClick(ViewHolder holder, final PlantTaskEntity entity) {
        holder.bt_caishou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent harvestManageIntent = new Intent(context, HarvestManageActivity.class);
                harvestManageIntent.putExtra("plantTaskEntity", entity);
                context.startActivity(harvestManageIntent);
            }
        });
        holder.bt_shifei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FertilizerOrPesticideActivity.class);
                intent.putExtra("job_no_in", entity.job_no_in);
                intent.putExtra("job_no", entity.job_no);
                intent.putExtra("type", "施肥");
                intent.putExtra("plantType", plantType);
                context.startActivity(intent);
            }
        });
        holder.bt_nongyao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FertilizerOrPesticideActivity.class);
                intent.putExtra("job_no_in", entity.job_no_in);
                intent.putExtra("job_no", entity.job_no);
                intent.putExtra("type", "农药");
                intent.putExtra("plantType", plantType);
                context.startActivity(intent);
            }
        });
        holder.bt_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlantDetailActivity.class);
                intent.putExtra("id", entity.plant_id);
                intent.putExtra("time", entity.plant_date);
                intent.putExtra("type", plantType);
                context.startActivity(intent);
            }
        });
    }

    private class ViewHolder {
        public TextView tv_plant_task,tv_plant_time;
        public Button bt_delete,bt_nongyao,bt_shifei,bt_caishou,bt_detail;
        public View ll_bt, ll_parent;
        public ViewStub vs_item_add_button;
        public SwipeMenuLayout sl_item_plant_task;
    }

    public interface OnDeleteListener{
        public void deleteItem(String id,int position);
    }

    public void setOnDeleteClickListener(OnDeleteListener listener){
        this.listener = listener;
    }

    public void clearMap(){
        isShowBottom = new HashMap<>();
    }
}
