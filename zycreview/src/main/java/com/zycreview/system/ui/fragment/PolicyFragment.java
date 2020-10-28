package com.zycreview.system.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zycreview.system.R;
import com.zycreview.system.adapter.PolicyAdapter;
import com.zycreview.system.api.PolicyApi;
import com.zycreview.system.entity.PolicyEntity;
import com.zycreview.system.entity.PolicyList;
import com.zycreview.system.ui.activity.NoticeAndPolicyActivity;
import com.zycreview.system.ui.activity.PolicySearchResultActivity;
import com.zycreview.system.utils.StringUtil;
import com.zycreview.system.utils.TopClickUtil;

import java.util.List;

/**
 * 药材认知页面
 */
public class PolicyFragment extends AbsLoadMoreFragment<ListView, PolicyEntity> implements AdapterView.OnItemClickListener,View.OnClickListener {

    private View mView;
    private PullToRefreshListView mPullToRefreshListView;
    private View rszs, ddyc, ycbk;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_policy, container, false);
        initView();
        initListener();
        /*setAdapter();
        loadData();*/
        return mView;
    }

    private void initView(){
        initTopView(mView);
        setTitle("药材认知");
        hideBackBtn();
        setTopRightImg(R.mipmap.top_ss, TopClickUtil.TOP_POLI_SEA);
       /* mPullToRefreshListView = (PullToRefreshListView) mView.findViewById(R.id.policy_list);*/
        rszs = mView.findViewById(R.id.rel_rszs);
        ddyc = mView.findViewById(R.id.rel_ddyc);
        ycbk = mView.findViewById(R.id.rel_ycbk);
    }

    private void setAdapter(){
        mAdapter = new PolicyAdapter(null,getActivity());
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    public void initListener(){
        initTopListener();
        /*mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        mPullToRefreshListView.setOnItemClickListener(this);*/
        rszs.setOnClickListener(this);
        ddyc.setOnClickListener(this);
        ycbk.setOnClickListener(this);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
//        httpGetRequest(PolicyApi.getPolicyList(mPager.getPage() + "",null),PolicyApi.API_POLICY_LIST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){
            case PolicyApi.API_POLICY_LIST:
                policyHander(json);
                break;
        }
    }

    private void policyHander(String json){
        final long start = System.currentTimeMillis();
        if(!StringUtil.isEmpty(json)){
            PolicyList policyList = JSON.parseObject(json,PolicyList.class);
            if(null != policyList){
                List<PolicyEntity> infos = policyList.list;
                appendData(infos,start);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mAdapter != null && mAdapter.getCount() > (position-1) ) {
            PolicyEntity entity = mAdapter.getItem(position-1);
            Intent detailIntent = new Intent(getActivity(), NoticeAndPolicyActivity.class);
            detailIntent.putExtra("title",entity.title);
            detailIntent.putExtra("uid",entity.uuid);
            startActivity(detailIntent);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), PolicySearchResultActivity.class);
        switch (v.getId()){
            case R.id.rel_rszs:
                intent.putExtra("title","认识追溯");
                intent.putExtra("modId","1003009110");
                startActivity(intent);
                break;
            case R.id.rel_ddyc:
                intent.putExtra("title","道地药材");
                intent.putExtra("modId","1003009300");
                startActivity(intent);
                break;
            case R.id.rel_ycbk:
                intent.putExtra("title","药材百科");
                intent.putExtra("modId","1003009400");
                startActivity(intent);
                break;
        }
    }
}
