package com.shenmai.system.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shenmai.system.R;
import com.shenmai.system.adapter.GoodsListAdapter;
import com.shenmai.system.api.GoodsApi;
import com.shenmai.system.entity.GoodsEntity;
import com.shenmai.system.entity.GoodsList;
import com.shenmai.system.ui.activity.GoodsDetailActivity;
import com.shenmai.system.utlis.CacheObjUtils;
import com.shenmai.system.utlis.ListUtils;
import com.shenmai.system.utlis.StringUtil;

import java.util.HashMap;
import java.util.List;

/**
 *商品list显示
 */
public class GoodsListFragment extends AbsLoadMoreFragment<ListView, GoodsEntity> implements AdapterView.OnItemClickListener,GoodsListAdapter.onAddShopListener,View.OnClickListener{

    private PullToRefreshListView mPullToRefreshListView;
    private String cateId;
    private TextView[] orderBy = new TextView[3];
    private String orderString = null;

    public static Fragment getInstance(Bundle bundle) {
        GoodsListFragment fragment = new GoodsListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_goods_list, container, false);
        cateId = getArguments().getString("cateId");
        initView(mView, inflater);
        orderBySelect(0);
        setAdapter();
        initListenter();
        setCacheData();
        mPager.setPage(0);
        loadData();
        return mView;
    }

    private void initView(View view, LayoutInflater inflater) {
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.goods_list_refer);
        View headView = inflater.inflate(R.layout.list_goods_head, null);
        orderBy[0] = (TextView) headView.findViewById(R.id.tv_list_goods_head_sale);
        orderBy[1] = (TextView) headView.findViewById(R.id.tv_list_goods_head_hot);
        orderBy[2] = (TextView) headView.findViewById(R.id.tv_list_goods_head_reback);
        mPullToRefreshListView.getRefreshableView().addHeaderView(headView);
    }

    /**
     * 设置缓存数据
     */
    private void setCacheData() {
        final long start = System.currentTimeMillis();
        GoodsList goods = null;
        try {
            if(!StringUtil.isEmpty(cateId)){
                goods = (GoodsList)  CacheObjUtils.getObj(getActivity(), cateId);
            } else {
                goods = (GoodsList)  CacheObjUtils.getObj(getActivity(), "AllGoodsList");
            }
        } catch (Exception e) {
        }
        if (goods != null && !ListUtils.isEmpty(goods.goodsList)) {
            List<GoodsEntity> goodsList = goods.goodsList;
            clearData();
            appendData(goodsList, start,true);
        }
    }

    private void setAdapter(){
        mAdapter = new GoodsListAdapter(null,getActivity());
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    private void initListenter(){
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        mPullToRefreshListView.setOnItemClickListener(this);
        ((GoodsListAdapter)mAdapter).setOnAddShopListener(this);
        for (int i = 0; i < orderBy.length ; i++) {
            orderBy[i].setOnClickListener(this);
        }
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        if(!StringUtil.isEmpty(cateId)){
            httpGetRequest(GoodsApi.getGoodsListUrl(mPager.getPage() + "", null, orderString, cateId,null), GoodsApi.API_GET_SHOP_LIST);
        } else {
            httpGetRequest(GoodsApi.getGoodsListUrl(mPager.getPage() + "", null, orderString, null, null), GoodsApi.API_GET_SHOP_LIST);
        }
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){
            case GoodsApi.API_GET_SHOP_LIST:
                goodsHander(json);
                break;
            case GoodsApi.API_ADD_GOODS_TOSHOP:
                Toast.makeText(getActivity(),"添加成功",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void goodsHander(String json){
        final long start = System.currentTimeMillis();
        List<GoodsEntity> goodsList = null;
        if(!StringUtil.isEmpty(json)){
            GoodsList goods = JSON.parseObject(json, GoodsList.class);
            if(goods != null){
                goodsList = goods.goodsList;
                try {
                    if (mPager.getPage() == 0) {
                        if(!StringUtil.isEmpty(cateId)){
                            CacheObjUtils.saveObj(getActivity(), "AllGoodsList", goods);
                        } else {
                            CacheObjUtils.saveObj(getActivity(), cateId, goods);
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        if(mPager.getPage() == 0){
            clearData();
        }
        appendData(goodsList, start, false);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if(getActivity() == null){
            return;
        }
        if(null != mAdapter.getItem(position-2)){
            GoodsEntity goodsEntity = mAdapter.getItem(position-2);
            if(goodsEntity != null){
                Intent detailIntent = new Intent(getActivity(), GoodsDetailActivity.class);
                detailIntent.putExtra("id",goodsEntity.goods_id);
                startActivity(detailIntent);
            }
        }
    }

    @Override
    public void addShop(int position) {
        GoodsEntity goodsEntity = mAdapter.getItem(position);
        if(goodsEntity != null){
            if(goodsEntity.is_has_kc){
                addGoodsToShop(goodsEntity.goods_id);
            } else {
                Toast.makeText(getActivity(),"该商品无货",Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_list_goods_head_sale:
                orderBySelect(0);
                orderString = "orderBySaleDesc";
                orderGetGoodsList();
                break;
            case R.id.tv_list_goods_head_hot:
                orderBySelect(1);
                orderString = "orderByViewCountDesc";
                orderGetGoodsList();
                break;
            case R.id.tv_list_goods_head_reback:
                orderBySelect(2);
                orderString = "orderByPriceDesc";
                orderGetGoodsList();
                break;

        }
    }

    private void orderGetGoodsList(){
        clearData();
        loadData();
    }

    private void orderBySelect(int position){
        for (int i = 0; i < orderBy.length; i++) {
            if(position == i){
                orderBy[i].setTextAppearance(getContext(),R.style.GoodsOrderP);
                orderBy[i].setBackgroundResource(R.drawable.bg_list_order);
            }else{
                orderBy[i].setTextAppearance(getContext(),R.style.GoodsOrderN);
                orderBy[i].setBackgroundResource(R.drawable.bg_list_order_nomal);
            }
        }
    }

}
