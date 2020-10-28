package com.basulvyou.system.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.adapter.SpecialAdapter;
import com.basulvyou.system.api.ShopListApi;
import com.basulvyou.system.entity.SearchShopEntity;
import com.basulvyou.system.entity.SearchShopList;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.util.List;

/**
 * 更多特产检索显示页面
 */
public class SpecialActivity extends AbsLoadMoreActivity<GridView, SearchShopEntity> implements AdapterView.OnItemClickListener {

    private PullToRefreshGridView mPullToRefreshGridView;
    private String keyWord;
    public boolean hasmore = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special);
        keyWord = getIntent().getExtras().getString("keyword");
        initView();
        initListener();
        setAdapter();
        loadData();
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("商品");
        mPullToRefreshGridView = (PullToRefreshGridView) findViewById(R.id.special_more_list);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        mPullToRefreshGridView.setOnRefreshListener(this);
        mPullToRefreshGridView.setScrollingWhileRefreshingEnabled(true);
        mPullToRefreshGridView.setOnItemClickListener(this);
    }

    private void setAdapter(){
        mAdapter = new SpecialAdapter(null,SpecialActivity.this);
        mPullToRefreshGridView.setAdapter(mAdapter);
    }

    @Override
    protected PullToRefreshAdapterViewBase<GridView> getRefreshView() {
        return mPullToRefreshGridView;
    }

    @Override
    protected void loadData() {
        httpGetRequest(ShopListApi.getShopListUrl("1", mPager.rows + "", mPager.getPage() + "", null, keyWord, null, null, null), ShopListApi.API_GET_SHOP_LIST);
    }

    @Override
    public void httpResponse(String json, int action){
        super.httpResponse(json, action);
        switch (action) {
            case ShopListApi.API_GET_SHOP_LIST:
                shopHander(json);
                break;
            default:
                break;
        }
    }

    /**
     * 处理商品信息
     * @param json
     */
    private void shopHander(String json) {
        if (hasmore) {
            final long start = System.currentTimeMillis();
            List<SearchShopEntity> shopList = null;
            SearchShopList shop = JSON.parseObject(json, SearchShopList.class);
            if (shop != null) {
                shopList = shop.goods_list;
            }
            appendData(shopList, start);
//          hasmore = hasmoreTemp;
        } else {
            getRefreshView().post(new Runnable() {

                public void run() {
                    getRefreshView().onRefreshComplete();
                }
            });
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if(mAdapter != null && mAdapter.getCount() > position){
//            SearchShopEntity shopEntity = mAdapter.getItem(position);
//            Intent goodsDetailIntent = new Intent(SpecialActivity.this, GoodsDetailActivity.class);
//            goodsDetailIntent.putExtra("goods_id", shopEntity.goods_id);
//            goodsDetailIntent.putExtra("goods_name", shopEntity.goods_name);
//            if (!StringUtil.isEmpty(shopEntity.goods_image_url)) {
//                goodsDetailIntent.putExtra("image_url", shopEntity.goods_image_url);
//            }
//            startActivity(goodsDetailIntent);
//        }
    }
}
