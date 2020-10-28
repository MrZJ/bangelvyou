package com.basulvyou.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.adapter.LocationViewGridAdapter;
import com.basulvyou.system.api.LocationApi;
import com.basulvyou.system.entity.LocationEntity;
import com.basulvyou.system.entity.ShopEntity;
import com.basulvyou.system.entity.ShopList;
import com.basulvyou.system.util.ListUtils;
import com.basulvyou.system.util.StringUtil;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * 当地景点Grid展示
 */
public class LocationViewGridActivity extends AbsLoadMoreActivity<GridView, LocationEntity> implements AdapterView.OnItemClickListener {

    private PullToRefreshGridView mPullToRefreshGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_view_grid);
        initView();
        initListener();
        setAdapter();
        loadData();
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("景点");
        mPullToRefreshGridView = (PullToRefreshGridView) findViewById(R.id.location_view_grid_list);
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
        mAdapter = new LocationViewGridAdapter(null,LocationViewGridActivity.this);
        mPullToRefreshGridView.setAdapter(mAdapter);
    }


    @Override
    protected PullToRefreshAdapterViewBase<GridView> getRefreshView() {
        return mPullToRefreshGridView;
    }

    @Override
    protected void loadData() {
        httpGetRequest(LocationApi.getVirtualShopListUrl(null, mPager.rows + "", mPager.getPage() + ""
                , "20405", null, null, null, null, null, null), LocationApi.API_GET_LOCATION_LIST);
    }

    @Override
    public void httpResponse(String json, int action){
        super.httpResponse(json, action);
        switch (action)
        {
            case LocationApi.API_GET_LOCATION_LIST:
                locationHander(json);
                break;
            default:
                break;
        }
    }

    /**
     * 处理列表信息
     * @param json
     */
    private void locationHander(String json){
        final long start = System.currentTimeMillis();
        List<LocationEntity> locationList = null;
        if (!StringUtil.isEmpty(json)) {
            locationList = getLocationEntities(json, locationList);
        }
        appendData(locationList, start);
    }

    private List<LocationEntity> getLocationEntities(String json, List<LocationEntity> locationList) {
            ShopList shopList = JSON.parseObject(json, ShopList.class);
            if (shopList != null && !ListUtils.isEmpty(shopList.goods_list)) {
                List<ShopEntity> shop = shopList.goods_list;
                locationList = new ArrayList();
                for (int i = 0; i < shop.size(); i++) {
                    ShopEntity shopEntity = shop.get(i);
                    LocationEntity location = new LocationEntity();
                    location.location_brief = shopEntity.goods_content;
                    location.location_id = shopEntity.goods_id;
                    location.location_img_one = shopEntity.goods_image_url1;
                    location.location_img = shopEntity.goods_image_url;
                    location.location_title = shopEntity.goods_name;
                    location.location_visit_count = shopEntity.goods_salenum;
                    location.location_collect_count = shopEntity.sccomm;
                    location.location_evaluation_count = shopEntity.evaluation_count;
                    locationList.add(location);
                }
            }
        return locationList;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(mAdapter != null && mAdapter.getCount() > position){
                LocationEntity location = mAdapter.getItem(position);
                if(location != null){
                    Intent  intent = new Intent(this, LocationDetailActivity.class);
                    intent.putExtra("type", "view");
                    if(!StringUtil.isEmpty(location.location_img_one)){
                        intent.putExtra("image_url", location.location_img_one);
                    }else{
                        if(!ListUtils.isEmpty(location.location_img)){
                            intent.putExtra("image_url", location.location_img.get(0).comm_image_url);
                        }
                    }
                    intent.putExtra("name", location.location_title);
                    intent.putExtra("goods_id", location.location_id);
                    startActivity(intent);
            }
        }
    }
}
