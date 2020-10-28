package com.fuping.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.fuping.system.R;
import com.fuping.system.adapter.HelpAdapter;
import com.fuping.system.entity.HelpEntity;
import com.fuping.system.entity.HelpListEntity;
import com.fuping.system.entity.TaskTypeEntity;
import com.fuping.system.request.HelpListRequest;
import com.fuping.system.utils.Constant;
import com.fuping.system.utils.TimeDateUtil;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jianzhang.
 * on 2017/10/9.
 * copyright easybiz.
 */

public class HelpActivity extends AbsLoadMoreActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private PullToRefreshListView mPullToRefreshListView;
    private View submit_task, search, search0_layout, search1_layout, search2_layout, search3_layout, search4_layout,
            type0_view, type1_view, type2_view, type3_view, type4_view;
    private TextView type0_tv, type1_tv, type2_tv, type3_tv, type4_tv;
    private EditText search_et;
    private int mCurSearchType;
    public List<TaskTypeEntity> mTaskType;
    private boolean isSetType;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, HelpActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        initTopView();
        initListener();
        setLeftBackButton();
        initSideBarListener();
        setTitle("督查任务");
        initView();
    }

    private void initView() {
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.listview);
        mData = new ArrayList();
        mAdapter = new HelpAdapter(this, mData);
        mPullToRefreshListView.setAdapter(mAdapter);
        search_et = (EditText) findViewById(R.id.search_et);
        search_et.setHint("请输入村名搜索");
        search0_layout = findViewById(R.id.search0_layout);
        search1_layout = findViewById(R.id.search1_layout);
        search2_layout = findViewById(R.id.search2_layout);
        search3_layout = findViewById(R.id.search3_layout);
        search4_layout = findViewById(R.id.search4_layout);
        type0_tv = (TextView) findViewById(R.id.type0_tv);
        type1_tv = (TextView) findViewById(R.id.type1_tv);
        type2_tv = (TextView) findViewById(R.id.type2_tv);
        type3_tv = (TextView) findViewById(R.id.type3_tv);
        type4_tv = (TextView) findViewById(R.id.type4_tv);
        type0_view = findViewById(R.id.type0_view);
        type1_view = findViewById(R.id.type1_view);
        type2_view = findViewById(R.id.type2_view);
        type3_view = findViewById(R.id.type3_view);
        type4_view = findViewById(R.id.type4_view);
        search0_layout.setOnClickListener(this);
        search1_layout.setOnClickListener(this);
        search2_layout.setOnClickListener(this);
        search3_layout.setOnClickListener(this);
        search4_layout.setOnClickListener(this);
        search = findViewById(R.id.search);
        search.setOnClickListener(this);
        submit_task = findViewById(R.id.submit_task);
        submit_task.setOnClickListener(this);
        mPullToRefreshListView.setOnItemClickListener(this);
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setRefreshing(true);
        if (Constant.USER_TYPE_XIAN_ADMIN.equals(configEntity.usertype)
                || Constant.USER_TYPE_DUCHA_RENYUAN.equals(configEntity.usertype)
                || Constant.USER_TYPE_DUCHA_LINGDAO.equals(configEntity.usertype)
                || Constant.USER_TYPE_DUCHA_GUANLIYUAN.equals(configEntity.usertype)) {
            submit_task.setVisibility(View.VISIBLE);
        } else {
            submit_task.setVisibility(View.GONE);
        }
        loadData();
    }

    @Override
    protected PullToRefreshAdapterViewBase getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        HelpListRequest request = new HelpListRequest(mPager.getPage(), mCurSearchType, search_et.getText().toString(), configEntity.key);
        httpGetRequest(request.getRequestUrl(), HelpListRequest.HELP_REQUEST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case HelpListRequest.HELP_REQUEST:
                handleResponse(json);
                break;
        }
    }

    private void handleResponse(String str) {
        try {
            HelpListEntity entity = JSONObject.parseObject(str, HelpListEntity.class);
            if (entity != null && entity.villagetaskList != null) {
                appendData(entity.villagetaskList, TimeDateUtil.time());
            }
            if (entity != null && entity.basedataList != null && !isSetType) {
                mTaskType = entity.basedataList;
                isSetType = true;
                type0_tv.setText(mTaskType.get(0).type_name);
                type1_tv.setText(mTaskType.get(1).type_name);
                type2_tv.setText(mTaskType.get(2).type_name);
                type3_tv.setText(mTaskType.get(3).type_name);
                type4_tv.setText(mTaskType.get(4).type_name);

                type0_view.setBackgroundColor(Color.parseColor(mTaskType.get(0).remark));
                type1_view.setBackgroundColor(Color.parseColor(mTaskType.get(1).remark));
                type2_view.setBackgroundColor(Color.parseColor(mTaskType.get(2).remark));
                type3_view.setBackgroundColor(Color.parseColor(mTaskType.get(3).remark));
                type4_view.setBackgroundColor(Color.parseColor(mTaskType.get(4).remark));
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        if (submit_task == v) {
            AddTaskActivity.startActivity(this);
        } else if (search == v) {
            mPager.setPage(0);
            mCurSearchType = 0;
            clearData();
            mPullToRefreshListView.setRefreshing(true);
        } else if (search0_layout == v) {
            mPager.setPage(0);
            mCurSearchType = mTaskType.get(0).baseDataId;
            clearData();
            mPullToRefreshListView.setRefreshing(true);
        } else if (search1_layout == v) {
            mPager.setPage(0);
            mCurSearchType = mTaskType.get(1).baseDataId;
            clearData();
            mPullToRefreshListView.setRefreshing(true);
        } else if (search2_layout == v) {
            mPager.setPage(0);
            mCurSearchType = mTaskType.get(2).baseDataId;
            clearData();
            mPullToRefreshListView.setRefreshing(true);
        } else if (search3_layout == v) {
            mPager.setPage(0);
            mCurSearchType = mTaskType.get(3).baseDataId;
            clearData();
            mPullToRefreshListView.setRefreshing(true);
        } else if (search4_layout == v) {
            mPager.setPage(0);
            mCurSearchType = mTaskType.get(4).baseDataId;
            clearData();
            mPullToRefreshListView.setRefreshing(true);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            HelpEntity entity = (HelpEntity) mData.get(position - 1);
            TaskDetailActivity.startActivity(this, entity.id);
        } catch (Exception e) {

        }
    }
}
