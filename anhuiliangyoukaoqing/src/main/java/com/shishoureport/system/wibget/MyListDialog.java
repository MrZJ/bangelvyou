package com.shishoureport.system.wibget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.shishoureport.system.R;
import com.shishoureport.system.ui.adapter.DialogAdapter;

/**
 * Created by jianzhang.
 * on 2017/9/4.
 * copyright easybiz.
 */

public class MyListDialog extends Dialog implements AdapterView.OnItemClickListener {
    public interface OnListItemClick {
        void onItemclick(String str);
    }

    private Context mContext;
    private String[] mData;
    private ListView mListView;
    private OnListItemClick listItemClick;

    public MyListDialog(@NonNull Context context, String[] data, OnListItemClick onListItemClick) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = context;
        this.mData = data;
        this.listItemClick = onListItemClick;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_list, null);
        mListView = (ListView) view.findViewById(R.id.listview);
        DialogAdapter adapter = new DialogAdapter(mContext, mData);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        setContentView(view);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dismiss();
        if (listItemClick != null) {
            try {
                listItemClick.onItemclick(mData[position]);
            } catch (Exception e) {
            }
        }
    }
}
