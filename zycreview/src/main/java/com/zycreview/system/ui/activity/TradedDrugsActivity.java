package com.zycreview.system.ui.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zycreview.system.R;
import com.zycreview.system.adapter.TradedDrugsAdapter;
import com.zycreview.system.api.TradApi;
import com.zycreview.system.entity.MedicInstockEntity;
import com.zycreview.system.entity.Pager;
import com.zycreview.system.entity.MedicList;
import com.zycreview.system.utils.StringUtil;

import java.util.List;

/**
 * 交易单药材列表
 */
public class TradedDrugsActivity extends AbsLoadMoreActivity<ListView,MedicInstockEntity>{

    private PullToRefreshListView mPullToRefreshListView;
    private String tradedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traded_drugs);
        tradedId = getIntent().getStringExtra("id");
        initView();
        initListener();
        setAdapter();
        loadData();
    }

    private void initView(){
        initTopView();
        setTitle("药材列表");
        setLeftBackButton();
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.traded_drugs_list);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
    }

    private void setAdapter(){
        mAdapter = new TradedDrugsAdapter(null,this);
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        httpGetRequest(TradApi.getTradedDrugsList(configEntity.key, mPager.getPage() + "", Pager.rows + "", tradedId), TradApi.API_TRADED_DRUGS_LIST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){
            case TradApi.API_TRADED_DRUGS_LIST:
                tradedDrugsHander(json);
                break;
        }
    }

    private void tradedDrugsHander(String json){
        final long start = System.currentTimeMillis();
        if(!StringUtil.isEmpty(json)){
            MedicList drugsList = JSON.parseObject(json, MedicList.class);
            if(null != drugsList){
                List<MedicInstockEntity> infos = drugsList.list;
                appendData(infos,start,null);
            }
        }
    }
}
