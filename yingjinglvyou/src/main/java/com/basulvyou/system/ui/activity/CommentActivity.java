package com.basulvyou.system.ui.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.adapter.CommentAdapter;
import com.basulvyou.system.api.CommentApi;
import com.basulvyou.system.entity.CommentInfoEntity;
import com.basulvyou.system.entity.CommentList;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.HashMap;
import java.util.List;

/**
 * 当地商品评论列表
 */
public class CommentActivity extends AbsLoadMoreActivity<ListView, CommentInfoEntity>{

    private PullToRefreshListView mPullToRefreshListView;
    private String type;//类型1商品评论、2我的评论
    private String id;//查询商品评论时商品id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        type = getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("id");
        initView();
        initListener();
        setData();
        loadData();
    }

    private void initView(){
        initTopView();
        if(null != type &&  "2".equals(type)){
            setTitle("我的评论");
        }else{
            setTitle("更多评论");
        }
        setLeftBackButton();
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.comment_list);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
    }

    private void setData(){
        mAdapter = new CommentAdapter(null, this);
        mPullToRefreshListView.getRefreshableView().setAdapter(mAdapter);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        if(null != type && "1".equals(type)){
            httpPostRequest(CommentApi.goodsCommentListUrl(), getGoodsRequestParams(), CommentApi.API_GOODS_COMMENT);
        }else{
            httpPostRequest(CommentApi.commentListUrl(), getRequestParams(), CommentApi.API_COMMENT_LIST);
        }
    }

    /**
     * 获取评论列表
     * @return
     */
    private HashMap getRequestParams(){
        HashMap<String,String> params = new HashMap<>();
        params.put("key", configEntity.key);
        params.put("page", mPager.rows + "");
        params.put("curpage", mPager.getPage() + "");
        return params;
    }

    /**
     * 获取商品评论列表
     * @return
     */
    private HashMap getGoodsRequestParams(){
        HashMap<String,String> params = new HashMap<>();
        params.put("id", id);//商品id
        params.put("page", mPager.rows + "");
        params.put("curpage", mPager.getPage() + "");
        return params;
    }

    @Override
    public void httpResponse(String json, int action){
        super.httpResponse(json, action);
        switch (action)
        {
            case  CommentApi.API_COMMENT_LIST:
            case  CommentApi.API_GOODS_COMMENT:
                commentHander(json);
                break;
            default:
                break;
        }
    }

    /**
     * 整理评论数据
     * @param json
     */
    private void commentHander(String json){
        final long start = System.currentTimeMillis();
        List<CommentInfoEntity> commentInfoEntityList;
        CommentList commentList = JSON.parseObject(json,CommentList.class);
        if(null != commentList){
            commentInfoEntityList = commentList.commentInfoList;
            appendData(commentInfoEntityList, start);
        }
    }

}
