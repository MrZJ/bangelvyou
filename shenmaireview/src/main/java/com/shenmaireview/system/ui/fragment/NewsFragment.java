package com.shenmaireview.system.ui.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.shenmaireview.system.ui.activity.NewsDetailActivity;
import com.shenmaireview.system.utils.StringUtil;

import butterknife.Bind;


/**
 * 新闻列表页
 */
public class NewsFragment extends NoTopBaseFragment implements SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener{

    @Bind(R.id.swipeLay)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.rv_list)
    RecyclerView mRecyclerView;

    private View view;
    private String modId;
    private int page;
    private String pageNum = "12";
    private NewsAdapter adapter;

    public static Fragment getInstance(Bundle bundle) {
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NewsAdapter();
        adapter.setOnLoadMoreListener(this);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.setAdapter(adapter);
        getData();
        initListener();
    }

    private void initListener(){
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                NewsEntity.News item = (NewsEntity.News) adapter.getItem(position);
                Intent intent = new Intent(getContext(), NewsDetailActivity.class);
                intent.putExtra("newsId",item.id);
                intent.putExtra("title",item.title);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        modId = getArguments().getString("modId");
        page = 0;
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
        httpGetRequest(getContext(), NewsApi.getNewsList(page+"",modId,pageNum),NewsApi.API_NEWS_LIST);
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
    public void onDestroyView() {
        super.onDestroyView();
        OkGo.getInstance().cancelTag(getContext());
    }
}
