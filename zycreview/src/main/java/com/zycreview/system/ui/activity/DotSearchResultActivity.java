package com.zycreview.system.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zycreview.system.R;
import com.zycreview.system.adapter.DotSearchResultAdapter;
import com.zycreview.system.api.DotSearchApi;
import com.zycreview.system.entity.DotSearchResultEntity;
import com.zycreview.system.entity.DotSearchResultList;
import com.zycreview.system.utils.ListUtils;
import com.zycreview.system.utils.StringUtil;

import java.util.List;

/**
 * 网点查询结果页面
 */
public class DotSearchResultActivity extends AbsLoadMoreActivity<ListView, DotSearchResultEntity> {

    private PullToRefreshListView mPullToRefreshListView;
    private String typeName,keyWord,typeIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dot_search_result);
        keyWord = getIntent().getStringExtra("key");//搜索关键字
        typeName = getIntent().getStringExtra("type_name");//企业类型名称
        typeIndex = getIntent().getStringExtra("typeIndex");//搜索类型1网点0企业
        initView();
        initListener();
        setAdapter();
        loadData();
    }

    private void initView() {
        initTopView();
        setTitle("查询结果");
        setLeftBackButton();
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.dot_search_rseult_list);
        View titleView = LayoutInflater.from(this).inflate(R.layout.item_dot_search_result_info, null);
        TextView areaView = (TextView) titleView.findViewById(R.id.tv_item_dot_search_result_area);
        TextView typeView = (TextView) titleView.findViewById(R.id.tv_item_dot_search_result_type);
        if(typeIndex.equals("1")){
            areaView.setText("联系人");
            typeView.setText("联系方式");
        }
        mPullToRefreshListView.getRefreshableView().addHeaderView(titleView);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
    }

    private void setAdapter() {
        mAdapter = new DotSearchResultAdapter(null, this);
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        ((DotSearchResultAdapter)mAdapter).setDataType(typeIndex);
        if(typeIndex.equals("1")){
            httpGetRequest(DotSearchApi.getDotSearchList("" + mPager.getPage(), keyWord), DotSearchApi.API_DOT_SEARCH);
        }else{
            httpGetRequest(DotSearchApi.getEntpSearchList("" + mPager.getPage(), keyWord,typeName), DotSearchApi.API_DOT_SEARCH);
        }
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case DotSearchApi.API_DOT_SEARCH:
                dotSearchHander(json);
                break;
        }
    }

    private void dotSearchHander(String json) {
        final long start = System.currentTimeMillis();
        if (!StringUtil.isEmpty(json)) {
            DotSearchResultList dotSearchResultList = JSON.parseObject(json, DotSearchResultList.class);
            if (null != dotSearchResultList) {
                List<DotSearchResultEntity> infos = dotSearchResultList.list;
                if (ListUtils.isEmpty(infos) && mPager.getPage() == 0) {
                    Toast.makeText(this, "暂无数据", Toast.LENGTH_SHORT).show();
                }
                appendData(infos, start,null);
            }
        }
    }
}
