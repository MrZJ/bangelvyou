package com.yishangshuma.bangelvyou.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.adapter.LocationTodayHotAdapter;
import com.yishangshuma.bangelvyou.api.LocationApi;
import com.yishangshuma.bangelvyou.entity.LocationEntity;
import com.yishangshuma.bangelvyou.entity.ShopEntity;
import com.yishangshuma.bangelvyou.entity.ShopList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 附近更多
 */
public class NearbyMoreActivity extends AbsLoadMoreActivity<ListView, LocationEntity> implements AdapterView.OnItemClickListener{

    private PullToRefreshListView mPullToRefreshListView;
    private String id;//商品ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_more);
        id = getIntent().getExtras().getString("id");
        initView();
        initListener();
        setData();
        loadData();
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("附近相关");
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.nearby_more_list);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        mPullToRefreshListView.setOnItemClickListener(this);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    public void setData(){
        mAdapter = new LocationTodayHotAdapter(null, NearbyMoreActivity.this);
        ((LocationTodayHotAdapter) mAdapter).setIfList(true);
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    @Override
    protected void loadData() {
        httpPostRequest(LocationApi.getNearbyMoreList(), requestParams(), LocationApi.API_GET_NEARBY_LIST);
    }

    private HashMap requestParams(){
        HashMap<String,String> params = new HashMap<>();
        params.put("comm_id", id);//商品id
        params.put("page", mPager.rows + "");
        params.put("curpage", mPager.getPage() + "");
        return params;
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){
            case LocationApi.API_GET_NEARBY_LIST:
                nearbyHander(json);
                break;
        }
    }

    private void nearbyHander(String json){
        final long start = System.currentTimeMillis();
        List<LocationEntity> locationList = null;
        if(!"".equals(json)){
            ShopList shopList = JSON.parseObject(json, ShopList.class);
            if(shopList != null){
                List<ShopEntity> shop = shopList.goods_list;
                locationList = new ArrayList<>();
                for(int i = 0; i < shop.size(); i++){
                    ShopEntity shopEntity = shop.get(i);
                    LocationEntity location = new LocationEntity();
                    location.location_brief = shopEntity.goods_content;
                    location.location_id = shopEntity.goods_id;
                    location.location_img = shopEntity.goods_image_url;
                    location.location_title = shopEntity.goods_name;
                    location.location_visit_count = shopEntity.goods_salenum;
                    location.location_evaluation_count = shopEntity.evaluation_count;
                    location.location_type = shopEntity.cls_type;
                    locationList.add(location);
                }
                appendData(locationList, start);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mAdapter != null && mAdapter.getCount() > (position - 1)){
            LocationEntity location = mAdapter.getItem(position - 1);
            if(location != null){
                String type = location.location_type;
                String goods_id = location.location_id;
                divide(type,goods_id);
            }
        }
    }

    /**
     * 检查字串，按规则分离字串，goto不同界面
     * @param keyStr
     */
    private void divide(String keyStr,String id){
        if(keyStr != null && !"".equals(keyStr)){
            String detailStr = keyStr.substring(0, 2);
            //详情规则
            Intent detailIntent = new Intent(this, LocationDetailActivity.class);
            detailIntent.putExtra("id", id);
            if("**".equals(detailStr)){//景点详情
                detailIntent.putExtra("type", "sleep");
            } else if("##".equals(detailStr)){//美食详情
                detailIntent.putExtra("type", "food");
            } else if("!!".equals(detailStr)){//酒店详情
                detailIntent.putExtra("type", "sleep");
            }else if("~~".equals(detailStr)){//体验详情
                detailIntent.putExtra("type", "live");
            }else{
                return;
            }
            startActivity(detailIntent);
        }
    }
}
