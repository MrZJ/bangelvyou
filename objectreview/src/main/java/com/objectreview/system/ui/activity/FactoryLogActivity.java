package com.objectreview.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.objectreview.system.R;
import com.objectreview.system.adapter.FactoryLogAdapter;
import com.objectreview.system.api.FactoryLogApi;
import com.objectreview.system.entity.FactoryLogEntity;
import com.objectreview.system.entity.FactoryLogList;
import com.objectreview.system.utlis.StringUtil;

import java.util.List;

/**
 * 出厂记录列表
 */
public class FactoryLogActivity extends AbsLoadMoreActivity<ListView, FactoryLogEntity> implements AdapterView.OnItemClickListener {

    private PullToRefreshListView mPullToRefreshListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory_log);
        initView();
        setAdapter();
        initListener();
        loadData();
    }

    private void initView() {
        initTopView();
        setLeftBackButton();
        setTitle("产品出厂扫描记录");
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.list_out_factory_log);
    }

    private void setAdapter() {
        mAdapter = new FactoryLogAdapter(null, this);
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    public void initListener() {
        super.initListener();
        initSideBarListener();
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        mPullToRefreshListView.setOnItemClickListener(this);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        httpGetRequest(FactoryLogApi.getFactoryLogList(configEntity.key, mPager.getPage() + ""), FactoryLogApi.API_FACTORY_LOG_LIST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case FactoryLogApi.API_FACTORY_LOG_LIST:
                factoryLogHander(json);
                break;
        }
    }

    private void factoryLogHander(String json) {
        final long start = System.currentTimeMillis();
        if (!StringUtil.isEmpty(json)) {
            FactoryLogList factoryLogList = JSON.parseObject(json, FactoryLogList.class);
            if (null != factoryLogList) {
                List<FactoryLogEntity> infos = factoryLogList.list;
                appendData(infos, start);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FactoryLogEntity entity = mAdapter.getItem(position - 1);
        Intent intent = new Intent(this, FactoryLogDetailActivity.class);
        intent.putExtra("id", entity.ids);
        startActivity(intent);
    }
}
