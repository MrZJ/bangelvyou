package com.zycreview.system.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zycreview.system.R;
import com.zycreview.system.entity.CheckTaskList;
import com.zycreview.system.ui.activity.CheckDetailActivity;
import com.zycreview.system.ui.activity.CheckManageActivity;
import com.zycreview.system.utils.StringUtil;

import java.util.List;

/**
 * 检验管理适配器
 */

public class CheckManageAdapter extends AbsBaseAdapter<CheckTaskList.CheckTaskEntity> {

    private List<CheckTaskList.CheckTaskEntity> mData;
    private LayoutInflater inflater;
    private String checkType;
    private Context context;

    public CheckManageAdapter(List<CheckTaskList.CheckTaskEntity> data, Context context, String checkType){
        this.checkType = checkType;
        mData = data;
        inflater = LayoutInflater.from(context.getApplicationContext());
        this.context = context;
    }

    @Override
    public void setData(List<CheckTaskList.CheckTaskEntity> infos) {
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
    public CheckTaskList.CheckTaskEntity getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_check_list, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_weight = (TextView) convertView.findViewById(R.id.tv_weight);
            holder.tv_base = (TextView) convertView.findViewById(R.id.tv_base);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.bt_check = (Button) convertView.findViewById(R.id.bt_check);
            holder.tv_person = (TextView) convertView.findViewById(R.id.tv_person);
            if (checkType.equals("已检药材")){
                holder.bt_check.setText("查看");
                holder.tv_person.setVisibility(View.VISIBLE);
                holder.tv_weight.setVisibility(View.GONE);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CheckTaskList.CheckTaskEntity entity = getItem(position);
        if (entity != null) {
            if (checkType.equals("已检药材")) {
                if (!StringUtil.isEmpty(entity.test_name)) {
                    holder.tv_name.setText("检验名称:  " + entity.test_name);
                } else {
                    holder.tv_name.setText("检验名称:  无");
                }
                if (!StringUtil.isEmpty(entity.test_date)) {
                    holder.tv_time.setText("检验时间:  "+entity.test_date);
                } else {
                    holder.tv_time.setText("检验时间:  无");
                }
                if (!StringUtil.isEmpty(entity.test_man)) {
                    holder.tv_person.setText("检验人员:  "+entity.test_man);
                } else {
                    holder.tv_person.setText("检验人员:  无");
                }
            } else {
                if (!StringUtil.isEmpty(entity.drugs_name)){
                    holder.tv_name.setText("药材名称:  "+entity.drugs_name);
                } else {
                    holder.tv_name.setText("药材名称:  无");
                }
                if (!StringUtil.isEmpty(entity.receive_date)) {
                    holder.tv_time.setText("采收时间:  " + entity.receive_date);
                } else {
                    holder.tv_time.setText("采收时间:  无");
                }
                if (!StringUtil.isEmpty(entity.receive_weight)) {
                    holder.tv_weight.setText("采收重量:  "+entity.receive_weight);
                } else {
                    holder.tv_weight.setText("采收重量:  无");
                }
            }
            if (!StringUtil.isEmpty(entity.baseName)) {
                holder.tv_base.setText("基地名称:  "+entity.baseName);
            } else {
                holder.tv_base.setText("基地名称:  无");
            }
        }
        holder.bt_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (checkType.equals("已检药材")) {
                    intent = new Intent(context, CheckDetailActivity.class);
                    intent.putExtra("id", entity.test_id);
                } else {
                    intent = new Intent(context, CheckManageActivity.class);
                    intent.putExtra("id", entity.receive_id);
                }
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        public TextView tv_name,tv_time,tv_base,tv_weight,tv_person;
        public Button bt_check;
    }

}
