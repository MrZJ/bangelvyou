package com.basulvyou.system.ui.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.basulvyou.system.R;
import com.basulvyou.system.adapter.NoticeAdapter;
import com.basulvyou.system.entity.NoticeInfoEntity;
import com.basulvyou.system.util.TopClickUtil;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 站内信界面
 */
public class NoticeActivity extends AbsLoadMoreActivity<ListView, NoticeInfoEntity>{

    private PullToRefreshListView mPullToRefreshListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        initView();
        initListener();
        setAdapter();
        loadData();
    }

    public void initView() {
        initTopView();
        setLeftBackButton();
        setTitle("站内信");
        String notice = 1+"封未读";
        setTopRightTitle(notice, TopClickUtil.TOP_NOT);
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.notice_list);
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
    }

    private void  setAdapter(){
        mAdapter = new NoticeAdapter(null,this);
        mPullToRefreshListView.getRefreshableView().setAdapter(mAdapter);
    }


    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {

    }
}
