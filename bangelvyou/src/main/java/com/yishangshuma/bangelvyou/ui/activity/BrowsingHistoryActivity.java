package com.yishangshuma.bangelvyou.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.adapter.BrowsingHistoryAdapter;
import com.yishangshuma.bangelvyou.api.BrowsingHistoryApi;
import com.yishangshuma.bangelvyou.entity.BrowsingList;
import com.yishangshuma.bangelvyou.entity.CollectEntity;
import com.yishangshuma.bangelvyou.util.ConfigUtil;
import com.yishangshuma.bangelvyou.util.ListUtils;

import java.util.HashMap;
import java.util.List;

/**
 * 浏览历史界面
 * Created by KevinLi on 2016/3/17.
 */
public class BrowsingHistoryActivity extends AbsLoadMoreActivity<ListView, CollectEntity> implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private PullToRefreshListView mPullToRefreshListView;
    private int delPosition;//要删除的浏览历史

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browsing_history);
        initView();
        initListener();
        setAdapter();
        loadData();
    }

    private void initView(){
        initTopView();
        setTitle("浏览历史");
        setLeftBackButton();
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.collect_list);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        mPullToRefreshListView.setOnItemClickListener(this);
        mPullToRefreshListView.getRefreshableView().setOnItemLongClickListener(this);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    /**
     * 获取浏览历史列表信息
     */
    @Override
    protected void loadData() {
        showLoading(getResources().getString(R.string.load_text), true);
        httpGetRequest(BrowsingHistoryApi.getBrowsingHistoryListUrl(mPager.rows + "",
                mPager.getPage() + "", ConfigUtil.phoneIMEI), BrowsingHistoryApi.API_GET_HISTORY_LIST);
    }

    /**
     * 删除浏览历史信息
     */
    private void deleteCollect(int position){
        httpPostRequest(BrowsingHistoryApi.getDeleteHistoryUrl(),
                getDeleteRequestParams(position), BrowsingHistoryApi.API_GET_DELETE_HISTORY);
    }

    /**
     * 获取删除浏览历史参数
     *
     * @return
     */
    private HashMap<String,String> getDeleteRequestParams(int position){
        CollectEntity collectEntity = new CollectEntity();
        if(!ListUtils.isEmpty(mData) && mData.size() > position){
            collectEntity = mData.get(position);
        }
        HashMap<String,String> params = new HashMap<>();
        params.put("key", configEntity.key);
        params.put("fav_id", collectEntity.fav_id);
        return params;
    }

    /**
     * 设置adapter数据
     */
    private void setAdapter(){
        mAdapter = new BrowsingHistoryAdapter(null, this);
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    @Override
    public void httpResponse(String json, int action){
        super.httpResponse(json, action);
        switch (action){
            case BrowsingHistoryApi.API_GET_HISTORY_LIST:
                browsingHistoryHander(json);
                break;
            case BrowsingHistoryApi.API_GET_DELETE_HISTORY:
                if(mData == null || mData.size() <= delPosition){
                    return;
                }
                Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                mData.removeAll(mData);
                mAdapter.notifyDataSetChanged();
                mPager.setPage(0);
                loadData();
                break;
            default:
                break;
        }
    }

    /**
     * 处理浏览历史列表信息
     * @param json
     */
    private void browsingHistoryHander(String json){
        BrowsingList collect = JSON.parseObject(json, BrowsingList.class);
        final long start = System.currentTimeMillis();
        List<CollectEntity> collectlist = null;
        if(collect != null){
            collectlist = collect.goods_list;
        }
        appendData(collectlist, start);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position = position - 1;
        if(!ListUtils.isEmpty(mData) && mData.size() > position){
            CollectEntity collectEntity = mData.get(position);
            Intent intent = new Intent(BrowsingHistoryActivity.this, GoodsDetailActivity.class);
            intent.putExtra("goods_id", collectEntity.goods_id);
            intent.putExtra("image_url", collectEntity.goods_image_url);
            intent.putExtra("goods_name", collectEntity.goods_name);
            startActivity(intent);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        new AlertDialog.Builder(this).setTitle("").setMessage("确认删除该历史商品？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        delPosition = position - 1;
                        deleteCollect(position - 1);
                    }})
                .setNegativeButton("取消",null)
                .show();
        return true;
    }
}
