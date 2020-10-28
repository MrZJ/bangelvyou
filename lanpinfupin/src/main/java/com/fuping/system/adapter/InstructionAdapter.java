package com.fuping.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fuping.system.R;
import com.fuping.system.entity.InstructionEntity;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/10/10.
 * copyright easybiz.
 */

public class InstructionAdapter extends AbsBaseAdapter<InstructionEntity> {
    private List<InstructionEntity> mData;
    private Context context;

    public InstructionAdapter(Context context, List<InstructionEntity> list) {
        mData = list;
        this.context = context;
    }

    @Override
    public void setData(List<InstructionEntity> infos) {
        mData = infos;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public InstructionEntity getItem(int position) {
        return mData == null ? new InstructionEntity() : mData.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_instruction, null);
            holder = new ViewHolder();
            holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.detail_tv = (TextView) convertView.findViewById(R.id.detail_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        initHolder(holder, mData.get(position));
        return convertView;
    }

    private void initHolder(ViewHolder h, InstructionEntity entity) {
        if (h == null || entity == null) {
            return;
        }
        h.time_tv.setText(entity.instructions_date);
        h.detail_tv.setText(entity.instructions_content);
    }

    private class ViewHolder {
        public TextView time_tv, detail_tv;
    }
}
