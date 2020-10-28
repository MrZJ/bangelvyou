package com.yishangshuma.bangelvyou.ui.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.adapter.MyCommentAdapter;
import com.yishangshuma.bangelvyou.api.CommentApi;
import com.yishangshuma.bangelvyou.entity.CommentInfoEntity;
import com.yishangshuma.bangelvyou.entity.CommentList;

import java.util.HashMap;
import java.util.List;

/**
 * 我的评论界面
 */
public class MyCommentActivity extends AbsLoadMoreActivity<ListView, CommentInfoEntity> {

    private PullToRefreshListView mPullToRefreshListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comment);
        initView();
        initListener();
        setData();
        loadData();
    }

    private void initView() {
        initTopView();
        setTitle("我的评论");
        setLeftBackButton();
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.my_comment_list);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
    }

    private void setData() {
        mAdapter = new MyCommentAdapter(null, this);
        mPullToRefreshListView.getRefreshableView().setAdapter(mAdapter);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        httpPostRequest(CommentApi.commentListUrl(), getRequestParams(), CommentApi.API_COMMENT_LIST);
    }

    /**
     * 获取评论列表
     *
     * @return
     */
    private HashMap getRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("key", configEntity.key);
        params.put("page", mPager.rows + "");
        params.put("curpage", mPager.getPage() + "");
        return params;
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case CommentApi.API_COMMENT_LIST:
                commentHander(json);
                break;
            default:
                break;
        }
    }

    /**
     * 整理评论数据
     *
     * @param json
     */
    private void commentHander(String json) {
        final long start = System.currentTimeMillis();
        List<CommentInfoEntity> commentInfoEntityList;
        CommentList commentList = JSON.parseObject(json, CommentList.class);
        if (null != commentList) {
            commentInfoEntityList = commentList.commentInfoList;
            appendData(commentInfoEntityList, start);
        }
    }
}
