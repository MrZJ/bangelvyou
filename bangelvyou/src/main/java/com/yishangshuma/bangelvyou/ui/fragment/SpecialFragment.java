package com.yishangshuma.bangelvyou.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.adapter.SpecialAdapter;
import com.yishangshuma.bangelvyou.api.ShopListApi;
import com.yishangshuma.bangelvyou.entity.ShopEntity;
import com.yishangshuma.bangelvyou.entity.ShopList;
import com.yishangshuma.bangelvyou.util.TopClickUtil;

import java.util.List;

/**
 * 特产界面
 * Created by Administrator on 2016/1/25.
 */
public class SpecialFragment extends AbsLoadMoreFragment<GridView, ShopEntity> implements AdapterView.OnItemClickListener{

    private View mView;
    private PullToRefreshGridView mPullToRefreshGridView;
    public boolean hasmore = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_special, container, false);
        initView();
        initListener();
        setAdapter();
        showLoading(getResources().getString(R.string.load_text), true);
        loadData();
        return mView;
    }

    private void initView(){
        initTopView(mView);
        setLogoShow(R.mipmap.tc_logo, TopClickUtil.TOP_IMG);
        mPullToRefreshGridView = (PullToRefreshGridView) mView.findViewById(R.id.special_grid_list);
    }

    private void initListener(){
        initTopListener();
        mPullToRefreshGridView.setOnRefreshListener(this);
        mPullToRefreshGridView.setScrollingWhileRefreshingEnabled(true);
        mPullToRefreshGridView.setOnItemClickListener(this);
    }

    private void setAdapter(){
        mAdapter = new SpecialAdapter(null,getActivity());
        mPullToRefreshGridView.setAdapter(mAdapter);
    }

    @Override
    protected PullToRefreshAdapterViewBase<GridView> getRefreshView() {
        return mPullToRefreshGridView;
    }

    /**获取上拉刷新网络数据*/
    @Override
    protected void loadData() {
        httpGetRequest(ShopListApi.getShopListUrl("1", mPager.rows + "", mPager.getPage() + "",null,null,null,null), ShopListApi.API_GET_SHOP_LIST);
    }

    @Override
    public void httpResponse(String json, int action){
        super.httpResponse(json, action);
        switch (action)
        {
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
    private void shopHander(String json){
        if(hasmore){
            final long start = System.currentTimeMillis();
            List<ShopEntity> shopList = null;
            ShopList shop = JSON.parseObject(json, ShopList.class);
            if(shop != null){
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
        /*if(null == getActivity()){
            return;
        }
        if(mAdapter != null && mAdapter.getCount() > position){
            ShopEntity shopEntity = mAdapter.getItem(position);
            Intent goodsDetailIntent = new Intent(getActivity(), GoodsDetailActivity.class);
            goodsDetailIntent.putExtra("goods_id",shopEntity.goods_id);
            goodsDetailIntent.putExtra("goods_name", shopEntity.goods_name);
            goodsDetailIntent.putExtra("image_url",shopEntity.goods_image_url);
            startActivity(goodsDetailIntent);
        }*/
    }

}
