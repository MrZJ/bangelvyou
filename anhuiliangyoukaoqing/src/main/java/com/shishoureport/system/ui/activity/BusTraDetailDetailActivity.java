package com.shishoureport.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.shishoureport.system.R;
import com.shishoureport.system.entity.BusinessTravel;
import com.shishoureport.system.entity.Person;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.ui.adapter.AddPeopleAdapter;
import com.shishoureport.system.ui.adapter.ProcessAdapter;
import com.shishoureport.system.ui.adapter.TraDetailAdapter;
import com.shishoureport.system.wibget.HorizontalListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/5.
 * copyright easybiz.
 */

public class BusTraDetailDetailActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private HorizontalListView mCopyListView;
    private ListView mProcessListView, detail_listview;
    private AddPeopleAdapter addPeopleAdapter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, BusTraDetailDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bus_tra_detail;
    }

    public void initView() {
        mCopyListView = (HorizontalListView) findViewById(R.id.copy_listview);
        detail_listview = (ListView) findViewById(R.id.detail_listview);
        addPeopleAdapter = new AddPeopleAdapter(this, getData1());
        mCopyListView.setAdapter(addPeopleAdapter);
        mCopyListView.setOnItemClickListener(this);
        mProcessListView = (ListView) findViewById(R.id.process_listview);
        mProcessListView.setAdapter(new ProcessAdapter(this, getData1()));
        detail_listview.setAdapter(new TraDetailAdapter(this, getData2()));
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

    private List<BusinessTravel> getData2() {
        ArrayList list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Person p = new Person();
            p.name = "name" + i;
            list.add(p);
        }
        return list;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
