package com.fuping.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fuping.system.R;
import com.fuping.system.entity.CountryDuChaEntity;
import com.fuping.system.utils.Constant;
import com.fuping.system.utils.TimeDateUtil;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/10/10.
 * copyright easybiz.
 */

public class DuChaListAdapter extends AbsBaseAdapter<CountryDuChaEntity> {
    private List<CountryDuChaEntity> mData;
    private Context context;

    public DuChaListAdapter(Context context, List<CountryDuChaEntity> list) {
        mData = list;
        this.context = context;
    }

    @Override
    public void setData(List<CountryDuChaEntity> infos) {
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
    public CountryDuChaEntity getItem(int position) {
        return mData == null ? new CountryDuChaEntity() : mData.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_ducha, null);
            holder = new ViewHolder();
            holder.monitor_tv = (TextView) convertView.findViewById(R.id.monitor_tv);
            holder.menber_tv = (TextView) convertView.findViewById(R.id.menber_tv);
            holder.progress_tv = (TextView) convertView.findViewById(R.id.progress_tv);
            holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        initHolder(holder, mData.get(position));
        return convertView;
    }

    private void initHolder(ViewHolder h, CountryDuChaEntity entity) {
        if (h == null || entity == null) {
            return;
        }
        h.monitor_tv.setText(entity.inspection_manage);
        h.menber_tv.setText(entity.inspection_person);
        try {
            h.progress_tv.setText(entity.process_percent + "%");
            h.progress_tv.setTextColor(Constant.getProgressColor(context, entity.process_percent));
            h.time_tv.setText(TimeDateUtil.dateTime(entity.inspection_date));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ViewHolder {
        public TextView monitor_tv, menber_tv, progress_tv, time_tv;
    }
}
