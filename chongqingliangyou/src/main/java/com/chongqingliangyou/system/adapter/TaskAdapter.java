package com.chongqingliangyou.system.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chongqingliangyou.system.R;
import com.chongqingliangyou.system.entity.TaskEntity;
import com.chongqingliangyou.system.ui.activity.ChangeTaskStateActivity;

import java.util.List;

/**
 * 任务列表适配器
 */
public class TaskAdapter extends AbsBaseAdapter<TaskEntity> {

    private Context context;
    private List<TaskEntity> mData;
    private LayoutInflater inflater;
    private boolean isShowChange = true;

    public TaskAdapter(List<TaskEntity> branchInfos, Context context) {
        this.context = context;
        mData = branchInfos;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        if (null == mData) {
            return 0;
        }
        return mData.size();
    }

    public TaskEntity getItem(int position) {
        return mData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_task_info, null);
            holder = new ViewHolder();
            holder.tv_task_sender = (TextView) convertView
                    .findViewById(R.id.tv_item_task_sender);
            holder.tv_task_date = (TextView) convertView
                    .findViewById(R.id.tv_item_task_send_date);
            holder.tv_task_content = (TextView) convertView
                    .findViewById(R.id.tv_item_task_content);
            holder.tv_task_change = (TextView) convertView
                    .findViewById(R.id.tv_item_task_change);
            holder.rel_change = convertView
                    .findViewById(R.id.rel_change);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(isShowChange){
            holder.rel_change.setVisibility(View.VISIBLE);
        }else{
            holder.rel_change.setVisibility(View.GONE);
        }
        final TaskEntity taskEntity = getItem(position);
        holder.tv_task_sender.setText("发布人:"+taskEntity.sub_man);
        holder.tv_task_date.setText("发布时间:"+taskEntity.sub_date);
        holder.tv_task_content.setText(taskEntity.work_content);
        holder.tv_task_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changeIntent = new Intent(context, ChangeTaskStateActivity.class);
                changeIntent.putExtra("id", taskEntity.id);
                context.startActivity(changeIntent);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView tv_task_sender, tv_task_date, tv_task_content, tv_task_change;
        View rel_change;
    }

    public void setData(List<TaskEntity> taskEntity) {
        mData = taskEntity;
    }

    /**
     * 设置是否显示更新状态按钮
     * @param isShow
     */
    public void setShowChangeBtn(boolean isShow){
        this.isShowChange = isShow;
    }

}
