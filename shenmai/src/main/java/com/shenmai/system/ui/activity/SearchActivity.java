package com.shenmai.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shenmai.system.R;
import com.shenmai.system.adapter.GoodsListAdapter;
import com.shenmai.system.api.GoodsApi;
import com.shenmai.system.entity.GoodsEntity;
import com.shenmai.system.entity.GoodsList;
import com.shenmai.system.utlis.ListUtils;
import com.shenmai.system.utlis.StringUtil;
import com.shenmai.system.utlis.TopClickUtil;

import java.util.HashMap;
import java.util.List;

/**
 * 搜索页面
 */
public class SearchActivity extends AbsLoadMoreActivity<ListView,GoodsEntity> implements AdapterView.OnItemClickListener, View.OnClickListener,GoodsListAdapter.onAddShopListener {

    private PullToRefreshListView mPullToRefreshListView;
    private String keyWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        setAdapter();
        initListener();
    }

    private void initView(){
        initTopView();
        setTitle("");
        setLeftBackButton();
        setTopRightTitle("搜索", TopClickUtil.TOP_SEARCH);
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.search_list_refer);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        mPullToRefreshListView.setOnItemClickListener(this);
        top_title_search.setOnClickListener(this);
        top_right_title.setOnClickListener(this);
        ((GoodsListAdapter)mAdapter).setOnAddShopListener(this);
    }

    private void setAdapter(){
        mAdapter = new GoodsListAdapter(null, this);
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        httpGetRequest(GoodsApi.getGoodsListUrl(mPager.getPage() + "", keyWord, null, null, null), GoodsApi.API_GET_SHOP_LIST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){
            case GoodsApi.API_GET_SHOP_LIST:
                SearchHander(json);
                break;
            case GoodsApi.API_ADD_GOODS_TOSHOP:
                Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void SearchHander(String json){
        final long start = System.currentTimeMillis();
        List<GoodsEntity> goodsList = null;
        if(!StringUtil.isEmpty(json)){
            GoodsList goods = JSON.parseObject(json, GoodsList.class);
            if(goods != null){
                goodsList = goods.goodsList;
                if(ListUtils.isEmpty(goodsList) && mPager.getPage() == 0){
                    Toast.makeText(this,"暂无数据",Toast.LENGTH_SHORT).show();
                }
            }
        }
        appendData(goodsList, start);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        GoodsEntity goodsEntity = mAdapter.getItem(position-1);
        if(goodsEntity != null){
            Intent detailIntent = new Intent(this, GoodsDetailActivity.class);
            detailIntent.putExtra("id",goodsEntity.goods_id);
            detailIntent.putExtra("name",goodsEntity.goods_name);
            detailIntent.putExtra("image",goodsEntity.goods_image_url);
            detailIntent.putExtra("des",goodsEntity.sub_name);
            startActivity(detailIntent);
        }
    }

    @Override
    public void onClick(View v) {
        String word = top_title_search.getText().toString().trim();
        switch (v.getId()){
            case R.id.top_title_search:
                if(!StringUtil.isEmpty(word)){
                    keyWord = word;
                    clearData();
                    loadData();
                }
                break;
            case R.id.tv_top_right_text:
                if(!StringUtil.isEmpty(word)){
                    keyWord = word;
                    clearData();
                    loadData();
                } else {
                    Toast.makeText(this,"请输入商品名称",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void addShop(int position) {
        GoodsEntity goodsEntity = mAdapter.getItem(position);
        if(goodsEntity != null){
            if(goodsEntity.is_has_kc){
                addGoodsToShop(goodsEntity.goods_id);
            } else {
                Toast.makeText(this,"该商品无货",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addGoodsToShop(String goodsId){
        httpPostRequest(GoodsApi.addGoodsToShopUrl(), getRequestParams(goodsId), GoodsApi.API_ADD_GOODS_TOSHOP);
    }

    private HashMap<String, String> getRequestParams(String goodsId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("key", configEntity.key);
        params.put("goods_id", goodsId);
        return params;
    }
}
