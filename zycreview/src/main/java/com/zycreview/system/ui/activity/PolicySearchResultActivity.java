package com.zycreview.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zycreview.system.R;
import com.zycreview.system.adapter.PolicyAdapter;
import com.zycreview.system.api.PolicyApi;
import com.zycreview.system.entity.FieldErrors;
import com.zycreview.system.entity.PolicyEntity;
import com.zycreview.system.entity.PolicyList;
import com.zycreview.system.utils.ListUtils;
import com.zycreview.system.utils.StringUtil;

import java.util.List;

/**
 * 政策搜索结果界面
 */
public class PolicySearchResultActivity extends AbsLoadMoreActivity<ListView, PolicyEntity> implements AdapterView.OnItemClickListener {

    private PullToRefreshListView mPullToRefreshListView;
    private String key, title,searchType,modId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_searh_result);
        key = getIntent().getStringExtra("key");
        modId = getIntent().getStringExtra("modId");
        searchType = getIntent().getStringExtra("searchType");
        title = getIntent().getStringExtra("title");
        initView();
        initListener();
        setAdapter();
        loadData();
    }

    private void initView() {
        initTopView();
        if(!StringUtil.isEmpty(title)){
            setTitle(title);
        } else {
            setTitle("搜索结果");
        }
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.list_result);
    }

    private void setAdapter() {
        mAdapter = new PolicyAdapter(null, this);
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
        httpGetRequest(PolicyApi.getPolicyList(mPager.getPage() + "",modId,searchType,key), PolicyApi.API_POLICY_LIST);
    }


    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case PolicyApi.API_POLICY_LIST:
                policyHander(json);
                break;
        }
    }

    private void policyHander(String json) {
        final long start = System.currentTimeMillis();
        if (!StringUtil.isEmpty(json)) {
            PolicyList policyList = JSON.parseObject(json, PolicyList.class);
            if (null != policyList) {
                List<PolicyEntity> infos = policyList.list;
                if (ListUtils.isEmpty(infos) && mPager.getPage() == 0) {
                    Toast.makeText(this, "暂无数据", Toast.LENGTH_SHORT).show();
                }
                appendData(infos, start,null);
            }
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mAdapter != null && mAdapter.getCount() > (position - 1)) {
            PolicyEntity entity = mAdapter.getItem(position - 1);
            Intent detailIntent = new Intent(this, NoticeAndPolicyActivity.class);
            detailIntent.putExtra("title", entity.title);
            detailIntent.putExtra("uid", entity.uuid);
            startActivity(detailIntent);
        }
    }
}
