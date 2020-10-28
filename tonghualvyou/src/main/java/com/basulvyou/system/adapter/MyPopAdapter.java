package com.basulvyou.system.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.basulvyou.system.R;
import com.basulvyou.system.entity.LocationClassEntity;
import com.basulvyou.system.util.StringUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/9/8 0008.
 */
public class MyPopAdapter extends BaseAdapter {
    private Context ctx;
    private List<LocationClassEntity> classStrs;

    public MyPopAdapter(Context ctx, List<LocationClassEntity> classStrs) {
        this.ctx = ctx;
        this.classStrs = classStrs;
    }

    @Override
    public int getCount() {
        return classStrs.size();
    }

    @Override
    public LocationClassEntity getItem(int position) {
        return classStrs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            convertView = View.inflate(ctx, R.layout.item_pop_class, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_tesexiaochi = (TextView) convertView.findViewById(R.id.tv_tesexiaochi);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (getItem(position) != null && !StringUtil.isEmpty(getItem(position).getClassName())) {
            viewHolder.tv_tesexiaochi.setText(getItem(position).getClassName());
        }
        return convertView;
    }

    private class ViewHolder{
        TextView tv_tesexiaochi;
    }
}
