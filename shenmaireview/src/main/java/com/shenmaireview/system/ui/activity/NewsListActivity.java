package com.shenmaireview.system.ui.activity;

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
import com.shenmaireview.system.R;
import com.shenmaireview.system.adapter.NewsAdapter;
import com.shenmaireview.system.api.NewsApi;
import com.shenmaireview.system.bean.NewsEntity;
import com.shenmaireview.system.utils.StringUtil;

import butterknife.Bind;



public class NewsListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener{

    @Bind(R.id.swipeLay)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.rv_list)
    RecyclerView mRecyclerView;

    private String modId,title;
    private int page;
    private String pageNum = "12";
    private NewsAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_list;
    }

    @Override
    public void initView() {
        initTopView();
        modId = getIntent().getStringExtra("modId");
        title = getIntent().getStringExtra("title");
        setTopTitle(title);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsAdapter();
        adapter.setOnLoadMoreListener(this);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.setAdapter(adapter);
        page = 0;
        onRefresh();
        initListener();
    }

    private void initListener(){
        initTopListener();
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                NewsEntity.News item = (NewsEntity.News) adapter.getItem(position);
                Intent intent = new Intent(NewsListActivity.this, NewsDetailActivity.class);
                intent.putExtra("newsId",item.id);
                intent.putExtra("title",item.title);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh() {
        page = 0;
        adapter.setEnableLoadMore(false);
        getData();
    }

    @Override
    public void onLoadMoreRequested() {
        mSwipeRefreshLayout.setEnabled(false);
        getData();
    }

    private void getData(){
        httpGetRequest(this, NewsApi.getNewsList(page+"",modId,pageNum),NewsApi.API_NEWS_LIST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){
            case NewsApi.API_NEWS_LIST:
                newsHandler(json);
                break;
        }
    }

    private void newsHandler(String json){
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setRefreshing(false);
        adapter.setEnableLoadMore(true);
        adapter.loadMoreComplete();
        NewsEntity entity = JSON.parseObject(json,NewsEntity.class);
        if(entity != null){
            if(!StringUtil.isListEmpty(entity.list)){
                if(page == 0){
                    adapter.setNewData(entity.list);
                } else {
                    adapter.addData(entity.list);
                }
                if(entity.list.size() < Integer.parseInt(pageNum)){
                    adapter.loadMoreEnd(false);
                }
                page++;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
