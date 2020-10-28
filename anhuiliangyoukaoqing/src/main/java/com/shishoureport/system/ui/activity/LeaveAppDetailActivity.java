package com.shishoureport.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.shishoureport.system.R;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.ui.adapter.AddPeopleAdapter;
import com.shishoureport.system.ui.adapter.ProcessAdapter;
import com.shishoureport.system.wibget.HorizontalListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/5.
 * copyright easybiz.
 */

public class LeaveAppDetailActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private HorizontalListView mCopyListView;
    private ListView mProcessListView;
    private AddPeopleAdapter addPeopleAdapter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LeaveAppDetailActivity.class);
        context.startActivity(intent);
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_leave_app_detail;
    }

    public void initView() {
        mCopyListView = (HorizontalListView) findViewById(R.id.copy_listview);
        addPeopleAdapter = new AddPeopleAdapter(this, getData1());
        mCopyListView.setAdapter(addPeopleAdapter);
        mCopyListView.setOnItemClickListener(this);
        mProcessListView = (ListView) findViewById(R.id.process_listview);
        mProcessListView.setAdapter(new ProcessAdapter(this, getData1()));
    }

    List<User> list2;

    private List<User> getData1() {
        list2 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User p = new User();
            p.user_name = "name" + i;
            list2.add(p);
        }
        return list2;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
