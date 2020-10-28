package com.shishoureport.system.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shishoureport.system.R;
import com.shishoureport.system.entity.AttendanceItem;
import com.shishoureport.system.entity.ParentItem;
import com.shishoureport.system.utils.TimeDateUtil;

import java.util.List;


/**
 * Created by jianzhang.
 * on 2017/8/31.
 * copyright easybiz.
 */

public class CountExpandableAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<ParentItem> group;
    private List<List<AttendanceItem>> child;

    public CountExpandableAdapter(Context context, List<ParentItem> group, List<List<AttendanceItem>> child) {
        this.mContext = context;
        this.group = group;
        this.child = child;
    }

    @Override
    public int getGroupCount() {
        return group == null ? 0 : group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child == null || child.get(groupPosition) == null ? 0 : child.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group == null ? null : group.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return child == null || child.get(groupPosition) == null ? null : child.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.expandabel_parent_item, null);
            holder = new GroupHolder();
            holder.groupName = (TextView) convertView.findViewById(R.id.group_name);
            holder.arrow = (ImageView) convertView.findViewById(R.id.arrow);
            holder.time = (TextView) convertView.findViewById(R.id.time_count_Tv);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        if (isExpanded) {
            holder.arrow.setImageResource(R.mipmap.arrow_up);
        } else {
            holder.arrow.setImageResource(R.mipmap.arrow_down);
        }
        ParentItem parentItem = group.get(groupPosition);
        holder.groupName.setText(parentItem.name);
        holder.time.setText(parentItem.size);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.expandabel_child_item, null);
            holder = new ChildHolder();
            holder.childName = (TextView) convertView.findViewById(R.id.info_tv);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }
        AttendanceItem info = child.get(groupPosition).get(childPosition);
        holder.childName.setText(TimeDateUtil.dateTime(info.attendance_time));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class GroupHolder {
        public TextView groupName;
        public ImageView arrow;
        public TextView time;
    }

    class ChildHolder {
        public TextView childName;
    }
}
