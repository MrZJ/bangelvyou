package com.fuping.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fuping.system.R;
import com.fuping.system.entity.PeopleEntity;
import com.fuping.system.utils.Constant;

import java.util.List;

import static com.fuping.system.entity.CountryEntity.IS_INSPECT;

/**
 * Created by jianzhang.
 * on 2017/10/10.
 * copyright easybiz.
 */

public class PeopleSearchAdapter extends AbsBaseAdapter<PeopleEntity> {
    private List<PeopleEntity> mData;
    private Context context;
    private int colorInspect, colorNotInspect;

    public PeopleSearchAdapter(Context context, List<PeopleEntity> list) {
        mData = list;
        this.context = context;
        colorInspect = context.getResources().getColor(R.color.deep_blue);
        colorNotInspect = context.getResources().getColor(R.color.red);
    }

    @Override
    public void setData(List<PeopleEntity> infos) {
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
    public PeopleEntity getItem(int position) {
        return mData == null ? new PeopleEntity() : mData.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_people, null);
            holder = new ViewHolder();
            holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.loc_tv = (TextView) convertView.findViewById(R.id.loc_tv);
            holder.status_tv = (TextView) convertView.findViewById(R.id.status_tv);
            holder.reason_tv = (TextView) convertView.findViewById(R.id.reason_tv);
            holder.progress_bar = (ProgressBar) convertView.findViewById(R.id.progress_bar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        initHolder(holder, mData.get(position));
        return convertView;
    }

    private void initHolder(ViewHolder h, PeopleEntity entity) {
        if (h == null || entity == null) {
            return;
        }
        h.name_tv.setText(entity.poor_name);
        h.loc_tv.setText(entity.poor_addr);
        if (IS_INSPECT.equals(entity.is_inspection)) {
            h.status_tv.setText("已督查");
            h.status_tv.setTextColor(colorInspect);
        } else {
            h.status_tv.setText("未督查");
            h.status_tv.setTextColor(colorNotInspect);
        }
        h.reason_tv.setText(entity.poor_reson);
        if (entity.process_percent == null) {
            h.progress_bar.setProgress(0);
        } else {
            try {
                float f = Float.valueOf(entity.process_percent);
                int progress = (int) f;
                h.progress_bar.setProgressDrawable(Constant.getProgressDrawble(context, progress));
                h.progress_bar.setProgress(progress);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ViewHolder {
        public TextView name_tv, loc_tv, status_tv, reason_tv;
        public ProgressBar progress_bar;
    }
}
