package com.fuping.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fuping.system.R;
import com.fuping.system.entity.CountryEntity;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/10/10.
 * copyright easybiz.
 */

public class PeopleCountryAdapter extends AbsBaseAdapter<CountryEntity> {
    private List<CountryEntity> mData;
    private Context context;
    private String duchaFormat;
    private String inspect_state;

    public PeopleCountryAdapter(Context context, List<CountryEntity> list, String inspect_state) {
        mData = list;
        this.context = context;
        this.inspect_state = inspect_state;
        duchaFormat = context.getString(R.string.ducha_format);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_people_country, null);
            holder = new ViewHolder();
            holder.town_name_tv = (TextView) convertView.findViewById(R.id.town_name_tv);
            holder.ducha_num_tv = (TextView) convertView.findViewById(R.id.ducha_num_tv);
            holder.not_ducha_num_tv = (TextView) convertView.findViewById(R.id.not_ducha_num_tv);
            holder.ducha_layout = convertView.findViewById(R.id.ducha_layout);
            holder.weiducha_layout = convertView.findViewById(R.id.weiducha_layout);
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
        h.town_name_tv.setText(entity.village_p_name);
        if (CountryEntity.NOT_INSPECT.equals(inspect_state)) {
            h.ducha_layout.setVisibility(View.GONE);
            h.weiducha_layout.setVisibility(View.VISIBLE);
            h.not_ducha_num_tv.setText(String.format(duchaFormat, entity.poor_count));
        } else if (CountryEntity.INSPECT_POOR.equals(inspect_state)) {
            h.ducha_layout.setVisibility(View.VISIBLE);
            h.weiducha_layout.setVisibility(View.GONE);
            h.ducha_num_tv.setText(String.format(duchaFormat, entity.poor_count));
        } else if (CountryEntity.INSPECT_NOT_POOR.equals(inspect_state)) {
            h.ducha_layout.setVisibility(View.VISIBLE);
            h.weiducha_layout.setVisibility(View.GONE);
            h.ducha_num_tv.setText(String.format(duchaFormat, entity.poor_count));
        } else {
            h.ducha_layout.setVisibility(View.VISIBLE);
            h.weiducha_layout.setVisibility(View.VISIBLE);
            h.ducha_num_tv.setText(String.format(duchaFormat, entity.yes_poor_count));
            h.not_ducha_num_tv.setText(String.format(duchaFormat, entity.no_poor_count));
        }
    }

    private class ViewHolder {
        public TextView town_name_tv, ducha_num_tv, not_ducha_num_tv;
        private View ducha_layout, weiducha_layout;
    }
}
