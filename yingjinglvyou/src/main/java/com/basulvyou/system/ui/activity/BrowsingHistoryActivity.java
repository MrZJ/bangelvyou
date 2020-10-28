package com.basulvyou.system.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.adapter.BrowsingHistoryAdapter;
import com.basulvyou.system.api.BrowsingHistoryApi;
import com.basulvyou.system.entity.BrowsingList;
import com.basulvyou.system.entity.CollectEntity;
import com.basulvyou.system.util.ConfigUtil;
import com.basulvyou.system.util.ListUtils;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

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
        showLoading(getResources().getString(R.string.load_text), true);
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
                mData.remove(delPosition);
                mAdapter.notifyDataSetChanged();
//                mPager.setPage(0);
//                loadData();
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
            divide(collectEntity.cls_type);
        }
    }

    /**
     * 检查字串，按规则分离字串，goto不同界面
     * @param keyStr
     */
    private void divide(String keyStr){
        if(keyStr != null && !"".equals(keyStr)){
            String detailStr = keyStr.substring(0, 2);
            String goodsId = keyStr.substring(2);
            //详情规则
            Intent detailIntent = new Intent(this, LocationDetailActivity.class);
            detailIntent.putExtra("goods_id", goodsId);
            if("**".equals(detailStr)){//景点详情
                detailIntent.putExtra("type", "view");
            } else if("##".equals(detailStr)){//美食详情
                detailIntent.putExtra("type", "food");
            } else if("!!".equals(detailStr)){//酒店详情
                detailIntent.putExtra("type", "sleep");
            }else if("~~".equals(detailStr)){//体验详情
                detailIntent.putExtra("type", "live");
            }else if("$$".equals(detailStr)){//实物详情
                Intent goodsDetailIntent = new Intent(this,GoodsDetailActivity.class);
                goodsDetailIntent.putExtra("goods_id", goodsId);
                startActivity(goodsDetailIntent);
                return;
            }else{
                return;
            }
            startActivity(detailIntent);
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
