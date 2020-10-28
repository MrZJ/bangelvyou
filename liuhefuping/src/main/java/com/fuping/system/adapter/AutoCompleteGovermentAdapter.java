package com.fuping.system.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.fuping.system.R;
import com.fuping.system.entity.GovermentEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/10/18.
 * copyright easybiz.
 */

public class AutoCompleteGovermentAdapter extends BaseAdapter implements Filterable {
    public static final int COUNTRY = 1;
    public static final int PEOPLE = 2;

    public interface OnItemClick {
        void onItemClick(GovermentEntity entity);
    }

    private Context context;
    private ArrayFilter mFilter;
    private List<GovermentEntity> mOriginalValues;//所有的Item
    private List<GovermentEntity> mObjects;//过滤后的item
    private final Object mLock = new Object();
    private int maxMatch = 10;//最多显示多少个选项,负数表示全部
    private OnItemClick onItemClick;

    public AutoCompleteGovermentAdapter(Context context, List<GovermentEntity> mOriginalValues, OnItemClick onItemClick) {
        this.context = context;
        this.mOriginalValues = mOriginalValues;
        this.maxMatch = -1;
        this.onItemClick = onItemClick;
    }

    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    private class ArrayFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            // TODO Auto-generated method stub
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<GovermentEntity>(mObjects);//
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (mLock) {
                    Log.i("tag", "mOriginalValues.size=" + mOriginalValues.size());
                    ArrayList<GovermentEntity> list = new ArrayList<GovermentEntity>(mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                    return results;
                }
            } else {
                String prefixString = prefix.toString().toLowerCase();

                final int count = mOriginalValues.size();

                final ArrayList<GovermentEntity> newValues = new ArrayList<GovermentEntity>(count);

                for (int i = 0; i < count; i++) {
                    String value = mOriginalValues.get(i).link_unit_name;
                    final String valueText = value.toLowerCase();

                    if (valueText.contains(prefixString)) {//匹配所有
                        newValues.add(mOriginalValues.get(i));
                    }
//                    // First match against the whole, non-splitted value
//                    if (valueText.startsWith(prefixString)) {  //源码 ,匹配开头
//                        newValues.add(mOriginalValues.get(i));
//                    } else {
//                        final String[] words = valueText.split(" ");//分隔符匹配，效率低
//                        final int wordCount = words.length;
//
//                        for (int k = 0; k < wordCount; k++) {
//                            if (words[k].startsWith(prefixString)) {
//                                newValues.add(value);
//                                break;
//                            }
//                        }
//                    }
                    if (maxMatch > 0) {//有数量限制
                        if (newValues.size() > maxMatch - 1) {//不要太多
                            break;
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            // TODO Auto-generated method stub
            mObjects = (List<GovermentEntity>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mObjects.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        //此方法有误，尽量不要使用
        return mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_search_text, null);
            holder.tv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.rootView = convertView.findViewById(R.id.rootview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(mObjects.get(position).link_unit_name);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onItemClick(mObjects.get(position));
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv;
        View rootView;
    }

    public List<GovermentEntity> getAllItems() {
        return mOriginalValues;
    }

    public List<GovermentEntity> getFilteData() {
        return mObjects;
    }

}
