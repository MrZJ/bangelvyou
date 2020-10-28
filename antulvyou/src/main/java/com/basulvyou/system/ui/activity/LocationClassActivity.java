package com.basulvyou.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.adapter.LocationGoodsAdapter;
import com.basulvyou.system.api.LocationApi;
import com.basulvyou.system.api.ShopListApi;
import com.basulvyou.system.entity.LocationClassEntity;
import com.basulvyou.system.entity.LocationEntity;
import com.basulvyou.system.entity.ShopEntity;
import com.basulvyou.system.entity.ShopList;
import com.basulvyou.system.util.ListUtils;
import com.basulvyou.system.util.StringUtil;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 当地特产分类列表页
 */
public class LocationClassActivity extends AbsLoadMoreActivity<ListView, LocationEntity> implements AdapterView.OnItemClickListener {

    private PullToRefreshListView mPullToRefreshListView;
    private LocationClassEntity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        entity = (LocationClassEntity) getIntent().getSerializableExtra("entity");
        initView();
        initListener();
        setAdapter();
        loadData();
    }

    private void initView() {
        initTopView();
        setLeftBackButton();
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.location_List);
        setTitle(entity.gc_name);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
//        mPullToRefreshListView.setOnItemClickListener(this);
    }

    private void setAdapter() {
        mAdapter = new LocationGoodsAdapter(null,this);
        ((LocationGoodsAdapter)mAdapter).isCateList(true);
        mPullToRefreshListView.setBackgroundColor(getResources().getColor(R.color.bg_main_color));
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        httpGetRequest(ShopListApi.getShopListUrl("1", mPager.rows + "", mPager.getPage() + "", null, null, null, null,""+entity.getClassId()), LocationApi.API_GET_LOCATION_LIST);
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case LocationApi.API_GET_LOCATION_LIST:
                locationHander(json);
                break;
            default:
                break;
        }
    }

    /**
     * 处理列表信息
     *
     * @param json
     */
    private void locationHander(String json) {
        final long start = System.currentTimeMillis();
        List<LocationEntity> locationList = null;
        if (!StringUtil.isEmpty(json)) {
            locationList = getLocationEntities(json, locationList);
        }
        appendData(locationList, start);
    }

    private List<LocationEntity> getLocationEntities(String json, List<LocationEntity> locationList) {
        ShopList shop = JSON.parseObject(json, ShopList.class);
        if (shop != null && !ListUtils.isEmpty(shop.goods_list)) {
            List<ShopEntity> shopList = shop.goods_list;
            locationList = new ArrayList();
            for (int i = 0; i < shopList.size(); i++) {
                ShopEntity shopEntity = shopList.get(i);
                LocationEntity location = new LocationEntity();
                location.location_price = "￥" + shopEntity.goods_price;
                location.location_brief = shopEntity.goods_content;
                location.location_title = shopEntity.goods_name;
                location.location_id = shopEntity.goods_id;
                location.location_img_one = shopEntity.goods_image_url1;
                location.location_collect_count = shopEntity.sccomm;
                location.location_evaluation_count = shopEntity.evaluation_count;
                locationList.add(location);
            }
        }
        return locationList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mAdapter != null && mAdapter.getCount() > (position - 1)) {
            LocationEntity location = mAdapter.getItem(position - 1);
            if (location != null) {
                Intent intent = new Intent(this, GoodsDetailActivity.class);
                intent.putExtra("url", location.location_url);
                if (!StringUtil.isEmpty(location.location_img_one)) {
                    intent.putExtra("image_url", location.location_img_one);
                } else {
                    if (!ListUtils.isEmpty(location.location_img)) {
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
