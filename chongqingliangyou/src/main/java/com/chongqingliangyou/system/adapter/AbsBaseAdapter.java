package com.chongqingliangyou.system.adapter;

import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by KevinLi on 2016/1/28.
 */
public abstract class AbsBaseAdapter<T> extends BaseAdapter {

    public abstract void setData(List<T> infos);

    public T getItem(int position) {

        return null;
    }

}
