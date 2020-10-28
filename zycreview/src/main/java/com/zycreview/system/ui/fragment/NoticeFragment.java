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
import com.zycreview.system.adapter.NoticeAdapter;
import com.zycreview.system.api.NoticeApi;
import com.zycreview.system.entity.NoticeEntity;
import com.zycreview.system.entity.NoticeList;
import com.zycreview.system.ui.activity.NoticeAndPolicyActivity;
import com.zycreview.system.utils.StringUtil;
import com.zycreview.system.utils.TopClickUtil;

import java.util.List;

/**
 * 最新公告
 */
public class NoticeFragment extends AbsLoadMoreFragment<ListView, NoticeEntity> implements AdapterView.OnItemClickListener {

    private View mView;
    private PullToRefreshListView mPullToRefreshListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_notice, container, false);
        initView();
        initListener();
        setAdapter();
        loadData();
        return mView;
    }

    private void initView(){
        initTopView(mView);
        setTitle("政策公告");
        hideBackBtn();
        setTopRightImg(R.mipmap.top_ss, TopClickUtil.TOP_NOTC_SEA);
        mPullToRefreshListView = (PullToRefreshListView) mView.findViewById(R.id.notice_list);
    }

    private void setAdapter(){
        mAdapter = new NoticeAdapter(null,getActivity());
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    public void initListener(){
        initTopListener();
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
        httpGetRequest(NoticeApi.getNoticeList(mPager.getPage()+"",null),NoticeApi.API_NOTICE_LIST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){
            case NoticeApi.API_NOTICE_LIST:
                noticeHander(json);
                break;
        }
    }

    private void noticeHander(String json){
        final long start = System.currentTimeMillis();
        if(!StringUtil.isEmpty(json)){
            NoticeList noticeList = JSON.parseObject(json, NoticeList.class);
            if(null != noticeList){
                List<NoticeEntity> infos = noticeList.list;
                appendData(infos,start);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mAdapter != null && mAdapter.getCount() > (position-1) ) {
            NoticeEntity entity = mAdapter.getItem(position-1);
            Intent detailIntent = new Intent(getActivity(), NoticeAndPolicyActivity.class);
            detailIntent.putExtra("title",entity.title);
            detailIntent.putExtra("uid",entity.uuid);
            startActivity(detailIntent);
        }
    }

}
