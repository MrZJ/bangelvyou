package com.shishoureport.system.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.ContactsGroup;
import com.shishoureport.system.entity.User;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2018/4/17.
 * copyright easybiz.
 */
public class ContactsExpandAdapter extends BaseExpandableListAdapter {

    private List<ContactsGroup> mData;
    private Context mContext;

    public ContactsExpandAdapter(List<ContactsGroup> list, Context context) {
        this.mData = list;
        this.mContext = context;
    }

    @Override
    public int getGroupCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mData == null) {
            return 0;
        } else if (mData.get(groupPosition) == null) {
            return 0;
        } else if (mData.get(groupPosition).userInfoList == null) {
            return 0;
        } else {
            return mData.get(groupPosition).userInfoList.size();
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mData != null ? mData.get(groupPosition) : 0;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (mData == null) {
            return null;
        } else if (mData.get(groupPosition) == null) {
            return null;
        } else if (mData.get(groupPosition).userInfoList == null) {
            return null;
        } else {
            return mData.get(groupPosition).userInfoList.get(childPosition);
        }
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
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ParentHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_user_dept, null);
            holder = new ParentHolder();
            holder.group_text = (TextView) convertView.findViewById(R.id.group_text);
            convertView.setTag(holder);
        } else {
            holder = (ParentHolder) convertView.getTag();
        }
        holder.group_text.setText(mData.get(groupPosition).dept_name);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHoder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_contacts, null);
            holder = new ChildHoder();
            holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.photo_sd = (SimpleDraweeView) convertView.findViewById(R.id.photo_sd);
            convertView.setTag(holder);
        } else {
            holder = (ChildHoder) convertView.getTag();
        }
        User user = mData.get(groupPosition).userInfoList.get(childPosition);
        holder.name_tv.setText(user.real_name);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private static class ParentHolder {
        TextView group_text;
    }

    private static class ChildHoder {
        SimpleDraweeView photo_sd;
        TextView name_tv;
    }
}
