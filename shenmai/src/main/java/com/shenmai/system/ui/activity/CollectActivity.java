package com.shenmai.system.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.shenmai.system.R;
import com.shenmai.system.adapter.CollectAdapter;
import com.shenmai.system.api.GoodsApi;
import com.shenmai.system.entity.CollectList;
import com.shenmai.system.entity.GoodsEntity;
import com.shenmai.system.utlis.ListUtils;
import com.shenmai.system.utlis.StringUtil;
import com.shenmai.system.widget.MyListView;

import java.util.HashMap;
import java.util.List;

/**
 * 我的收藏界面
 */
public class CollectActivity extends AbsScrollLoadMoreActivity<GoodsEntity> implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{

    private PullToRefreshScrollView mPullToRefreshScrollView;
    private MyListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        initView();
        initListener();
        setAdapter();
        mPager.setPage(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        clearData();
        loadData();
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("我的收藏");
        mPullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.collect_list_refer);
        listView = (MyListView) findViewById(R.id.list_goods_list);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        mPullToRefreshScrollView.setOnRefreshListener(this);
        mPullToRefreshScrollView.setScrollingWhileRefreshingEnabled(true);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    private void setAdapter(){
        mAdapter = new CollectAdapter(null, this);
        listView.setAdapter(mAdapter);
    }


    @Override
    protected PullToRefreshScrollView getRefreshView() {
        return mPullToRefreshScrollView;
    }

    @Override
    protected void loadData() {
        httpGetRequest(GoodsApi.getCollectGoodsListUrl(configEntity.key, mPager.rows + "", mPager.getPage() + ""), GoodsApi.API_GET_SHOP_COLLECT_LIST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){
            case GoodsApi.API_GET_SHOP_COLLECT_LIST:
                collectHander(json);
                break;
            case GoodsApi.API_GET_SHOP_DEL_COLLECT:
                Toast.makeText(getApplicationContext(),"删除成功",Toast.LENGTH_SHORT).show();
                clearData();
                loadData();
                break;
        }
    }

    private void collectHander(String json){
        final long start = System.currentTimeMillis();
        List<GoodsEntity> goodsList = null;
        if(!StringUtil.isEmpty(json)){
            CollectList goods = JSON.parseObject(json, CollectList.class);
            if(goods != null){
                goodsList = goods.dataList;
                if(ListUtils.isEmpty(goodsList) && mPager.getPage() == 0){
                    Toast.makeText(getApplicationContext(),"暂无数据",Toast.LENGTH_SHORT).show();
                }
            }
        }
        appendData(goodsList, start);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        GoodsEntity goodsEntity = mAdapter.getItem(position);
        if(goodsEntity != null){
            Intent detailIntent = new Intent(getApplicationContext(), GoodsDetailActivity.class);
            detailIntent.putExtra("id",goodsEntity.goods_id);
            detailIntent.putExtra("name",goodsEntity.goods_name);
            detailIntent.putExtra("image",goodsEntity.goods_image_url);
            startActivity(detailIntent);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
        new AlertDialog.Builder(this).setTitle("").setMessage("确认删除该收藏商品？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCollect(position);
                    }})
                .setNegativeButton("取消",null)
                .show();
        return true;
    }
    /**
     * 删除收藏信息
     */
    private void deleteCollect(int position){
        httpPostRequest(GoodsApi.delCollectGoodsUrl(),
                getDeleteRequestParams(position), GoodsApi.API_GET_SHOP_DEL_COLLECT);
    }

    private HashMap<String, String> getDeleteRequestParams(int position) {
        HashMap<String, String> params = new HashMap<>();
        GoodsEntity collectEntity = mAdapter.getItem(position);
        params.put("key", configEntity.key);
        params.put("fav_id", collectEntity.fav_id);
        return params;
    }

}
