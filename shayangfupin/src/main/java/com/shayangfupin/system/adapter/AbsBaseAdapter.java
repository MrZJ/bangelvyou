package com.shayangfupin.system.adapter;

import android.widget.BaseAdapter;

import java.util.List;

/**
 * 下拉加载适配器
 */
public abstract class AbsBaseAdapter<T> extends BaseAdapter {

    public abstract void setData(List<T> infos);

    public T getItem(int position) {

        return null;
    }

}
