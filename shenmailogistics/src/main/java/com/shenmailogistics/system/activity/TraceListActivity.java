package com.shenmailogistics.system.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lzy.okgo.OkGo;
import com.shenmailogistics.system.R;
import com.shenmailogistics.system.adapter.TraceAdapter;
import com.shenmailogistics.system.api.UpdataLocationApi;
import com.shenmailogistics.system.bean.TraceEntity;
import com.shenmailogistics.system.utils.ConfigUtil;
import com.shenmailogistics.system.utils.StringUtil;
import com.shenmailogistics.system.utils.UIUtils;

import butterknife.Bind;

public class TraceListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener{
    @Bind(R.id.swipeLay)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.rv_list)
    RecyclerView mRecyclerView;

    private int page;
    private String pageNum = "12";
    private TraceAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_loaction_info;
    }

    @Override
    public void initView() {
        initTopView();
        setTopTitle("历史行程记录");
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(92, 172, 238));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TraceAdapter();
        adapter.setOnLoadMoreListener(this);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.setAdapter(adapter);
        configEntity = ConfigUtil.loadConfig(this);
        page = 1;
        onRefresh();
        initListener();
    }

    private void initListener(){
        initTopListener();
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                TraceEntity.TraceItemEntity item = (TraceEntity.TraceItemEntity) adapter.getItem(position);
                if(StringUtil.isEmpty(item.mileage) || item.mileage.equals("0.0")){
                    UIUtils.showToast("行程轨迹无法绘制");
                }else{
                    Intent intent = new Intent(TraceListActivity.this, DrawRouteActivity.class);
                    intent.putExtra("traceId",item.record_id);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        page = 1;
        adapter.setEnableLoadMore(false);
        getData();
    }

    @Override
    public void onLoadMoreRequested() {
        mSwipeRefreshLayout.setEnabled(false);
        getData();
    }

    private void getData(){
        httpGetRequest(this, UpdataLocationApi.getTraceList(page+"",configEntity.key,pageNum),UpdataLocationApi.API_GET_LIST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){
            case UpdataLocationApi.API_GET_LIST:
                listHandler(json);
                break;
        }
    }

    private void listHandler(String json){
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setRefreshing(false);
        adapter.setEnableLoadMore(true);
        adapter.loadMoreComplete();
        TraceEntity entity = JSON.parseObject(json,TraceEntity.class);
        if(entity != null){
            if(!StringUtil.isListEmpty(entity.list)){
                if(page == 1){
                    adapter.setNewData(entity.list);
                } else {
                    adapter.addData(entity.list);
                }
                if(entity.list.size() < Integer.parseInt(pageNum)){
                    adapter.loadMoreEnd(false);
                }
                page++;
            }else{
                adapter.loadMoreEnd(false);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
