package com.basulvyou.system.ui.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.adapter.PointDetailAdapter;
import com.basulvyou.system.api.MemberApi;
import com.basulvyou.system.entity.PointDetailList;
import com.basulvyou.system.entity.PointEntity;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.HashMap;
import java.util.List;

/**
 * 积分明细界面
 */
public class PointDetailActivity extends AbsLoadMoreActivity<ListView, PointEntity> {

    private PullToRefreshListView mPullToRefreshListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_detail);
        initView();
        initListener();
        setData();
        loadData();
    }

    private void initView(){
        initTopView();
        setTitle("积分明细");
        setLeftBackButton();
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.point_detail_list);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
    }

    private void setData() {
        mAdapter = new PointDetailAdapter(null, this);
        mPullToRefreshListView.getRefreshableView().setAdapter(mAdapter);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        httpPostRequest(MemberApi.getMemberPointUrl(), getRequestParams(), MemberApi.API_MEMBER_POINT);
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
            case MemberApi.API_MEMBER_POINT:
                pointHander(json);
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
    private void pointHander(String json) {
        final long start = System.currentTimeMillis();
        List<PointEntity> pointList;
        PointDetailList pointDetailList = JSON.parseObject(json, PointDetailList.class);
        if (null != pointDetailList) {
            pointList = pointDetailList.goods_list;
            appendData(pointList, start);
        }
    }

}
