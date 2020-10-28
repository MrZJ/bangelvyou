package com.fuping.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fuping.system.R;
import com.fuping.system.entity.CountryEntity;
import com.fuping.system.utils.Constant;

import java.util.List;

import static com.fuping.system.entity.CountryEntity.IS_INSPECT;

/**
 * Created by jianzhang.
 * on 2017/10/10.
 * copyright easybiz.
 */

public class CountryAdapter extends AbsBaseAdapter<CountryEntity> {
    private List<CountryEntity> mData;
    private Context context;
    private int colorInspect, colorNotInspect;

    public CountryAdapter(Context context, List<CountryEntity> list) {
        mData = list;
        this.context = context;
        colorInspect = context.getResources().getColor(R.color.deep_blue);
        colorNotInspect = context.getResources().getColor(R.color.red);
    }

    @Override
    public void setData(List<CountryEntity> infos) {
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
    public CountryEntity getItem(int position) {
        return mData == null ? new CountryEntity() : mData.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_country, null);
            holder = new ViewHolder();
            holder.inspect_state_tv = (TextView) convertView.findViewById(R.id.inspect_state_tv);
            holder.country_tv = (TextView) convertView.findViewById(R.id.country_tv);
            holder.progress_bar = (ProgressBar) convertView.findViewById(R.id.progress_bar);
            holder.tag_tv = (TextView) convertView.findViewById(R.id.tag_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        initHolder(holder, mData.get(position));
        return convertView;
    }

    private void initHolder(ViewHolder h, CountryEntity entity) {
        if (h == null || entity == null) {
            return;
        }
        if (IS_INSPECT.equals(entity.is_inspection)) {
            h.inspect_state_tv.setText("已督查");
            h.inspect_state_tv.setTextColor(colorInspect);
        } else {
            h.inspect_state_tv.setText("未督查");
            h.inspect_state_tv.setTextColor(colorNotInspect);
        }
        h.country_tv.setText(entity.village_name);
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
        if (entity.is_poor_village == 1) {
            h.tag_tv.setText("贫");
            h.tag_tv.setBackgroundResource(R.drawable.circle_red);
        } else {
            h.tag_tv.setText("非");
            h.tag_tv.setBackgroundResource(R.drawable.circle_green);
        }
    }

    private class ViewHolder {
        public TextView inspect_state_tv, country_tv, tag_tv;
        public ProgressBar progress_bar;
    }
}
