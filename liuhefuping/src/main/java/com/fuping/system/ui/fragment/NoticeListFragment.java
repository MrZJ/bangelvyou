package com.fuping.system.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.fuping.system.R;
import com.fuping.system.adapter.NoticeAdapter;
import com.fuping.system.api.NoticeApi;
import com.fuping.system.entity.NoticeEntity;
import com.fuping.system.entity.NoticeList;
import com.fuping.system.ui.activity.NoticeActivity;
import com.fuping.system.utils.StringUtil;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * 最新资讯列表数据展示界面
 */
public class NoticeListFragment extends AbsLoadMoreFragment<ListView, NoticeEntity> implements AdapterView.OnItemClickListener {

    private PullToRefreshListView mPullToRefreshListView;
    private String noticeType,noticeTypeId;
    private int type;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notice_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = getArguments().getInt("position");
        noticeType = getArguments().getString("type");
        noticeTypeId = getArguments().getString("typeId");
        initView(view);
        initListener();
        setAdapter();
        mPager.setPage(0);//防止切换tab时page增加
        loadData();
    }

    public static Fragment getInstance(Bundle bundle) {
        NoticeListFragment fragment = new NoticeListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initView(View view){
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.notice_list);
    }

    private void setAdapter(){
        mAdapter = new NoticeAdapter(null,getActivity());
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    public void initListener(){
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
        httpGetRequest(NoticeApi.getNoticeList(mPager.getPage()+"",noticeTypeId,null),NoticeApi.API_NOTICE_LIST);
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
            Intent detailIntent = new Intent(getActivity(), NoticeActivity.class);
            detailIntent.putExtra("title",entity.title);
            detailIntent.putExtra("uid",entity.uuid);
            startActivity(detailIntent);
        }
    }


}
