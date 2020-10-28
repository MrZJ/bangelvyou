package com.shayangfupin.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shayangfupin.system.R;
import com.shayangfupin.system.adapter.NoticeAdapter;
import com.shayangfupin.system.api.NoticeApi;
import com.shayangfupin.system.entity.FieldErrors;
import com.shayangfupin.system.entity.NoticeEntity;
import com.shayangfupin.system.entity.NoticeList;
import com.shayangfupin.system.utlis.ListUtils;
import com.shayangfupin.system.utlis.StringUtil;

import java.util.List;

/**
 * 公告搜索结果界面
 */
public class NoticeSearchResultActivity extends AbsLoadMoreActivity<ListView, NoticeEntity> implements AdapterView.OnItemClickListener {

    private PullToRefreshListView mPullToRefreshListView;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_searh_result);
        key = getIntent().getStringExtra("key");
        initView();
        initListener();
        setAdapter();
        loadData();
    }

    private void initView() {
        initTopView();
        setTitle("搜索结果");
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.list_result);
    }

    private void setAdapter() {
        mAdapter = new NoticeAdapter(null, this);
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
        httpGetRequest(NoticeApi.getNoticeList(mPager.getPage() + "", null, key),NoticeApi.API_NOTICE_LIST);
    }


    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
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
                if (ListUtils.isEmpty(infos) && mPager.getPage() == 1) {
                    Toast.makeText(this, "当前没有搜索到内容!", Toast.LENGTH_SHORT).show();
                }
                appendData(infos,start);
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
            NoticeEntity entity = mAdapter.getItem(position-1);
            Intent detailIntent = new Intent(this, NoticeActivity.class);
            detailIntent.putExtra("title",entity.title);
            detailIntent.putExtra("uid",entity.uuid);
            startActivity(detailIntent);
        }
    }
}
