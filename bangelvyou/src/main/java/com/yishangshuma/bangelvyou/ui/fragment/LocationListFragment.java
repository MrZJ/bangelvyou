package com.yishangshuma.bangelvyou.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.adapter.LocationTodayHotAdapter;
import com.yishangshuma.bangelvyou.api.LocationApi;
import com.yishangshuma.bangelvyou.entity.LocationEntity;
import com.yishangshuma.bangelvyou.entity.NewsEntity;
import com.yishangshuma.bangelvyou.entity.NewsListEntity;
import com.yishangshuma.bangelvyou.entity.ShopBundleEntity;
import com.yishangshuma.bangelvyou.entity.ShopEntity;
import com.yishangshuma.bangelvyou.entity.ShopList;
import com.yishangshuma.bangelvyou.ui.activity.LocationDetailActivity;
import com.yishangshuma.bangelvyou.ui.activity.NewsDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 当地列表界面
 * Created by KevinLi on 2016/2/2.
 */
public class LocationListFragment extends AbsLoadMoreFragment<ListView, LocationEntity> implements AdapterView.OnItemClickListener {

    private PullToRefreshListView mPullToRefreshListView;
    private int type;
    private ShopBundleEntity shopBundleEntity;
    private String locationType,locationTypeId;

    public static Fragment getInstance(Bundle bundle) {
        LocationListFragment fragment = new LocationListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = getArguments().getInt("position");
        locationType = getArguments().getString("type");
        locationTypeId = getArguments().getString("typeId");
        shopBundleEntity = (ShopBundleEntity) getArguments().getSerializable("shopBundle");
        initView(view);
        initListener();
        setAdapter();
        loadData();
    }

    public void initListener(){
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        mPullToRefreshListView.setOnItemClickListener(this);
    }

    private void initView(View view) {
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.location_list);
    }

    private void setAdapter(){
        mAdapter = new LocationTodayHotAdapter(null, getActivity());
        ((LocationTodayHotAdapter) mAdapter).setIfList(true);
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        if(type == 4){
            httpGetRequest(LocationApi.getNewsUrl(mPager.rows + "",
                    mPager.getPage() + "", shopBundleEntity.keyword, shopBundleEntity.key,null), LocationApi.API_GET_LOCATION_LIST);
        } else if(type == 5){
            httpGetRequest(LocationApi.getNewsUrl(mPager.rows + "",
                    mPager.getPage() + "", shopBundleEntity.keyword, shopBundleEntity.key,"1"), LocationApi.API_GET_LOCATION_LIST);
        } else {
            httpGetRequest(LocationApi.getVirtualShopListUrl(shopBundleEntity.key, mPager.rows + "", mPager.getPage() + ""
                    , locationTypeId, shopBundleEntity.keyword, null, shopBundleEntity.order, null, null, null), LocationApi.API_GET_LOCATION_LIST);
        }
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
        if(!"".equals(json)){
            locationList = getLocationEntities(json, locationList);
        }
        appendData(locationList, start);
    }

    private List<LocationEntity> getLocationEntities(String json, List<LocationEntity> locationList) {
        if(type == 4 || type == 5){
            NewsListEntity newsList = JSON.parseObject(json, NewsListEntity.class);
            if(newsList != null){
                List<NewsEntity> news = newsList.news;
                locationList = new ArrayList<>();
                for(int i = 0; i < news.size(); i++){
                    NewsEntity newsEntity = news.get(i);
                    LocationEntity location = new LocationEntity();
                    location.location_brief = newsEntity.n_brief;
                    location.location_date = newsEntity.n_add_date;
                    location.location_id = newsEntity.n_id;
                    location.location_img = newsEntity.n_main_img;
                    location.location_title = newsEntity.n_title;
                    location.location_visit_count = newsEntity.n_visit_count;
                    locationList.add(location);
                }
            }
        } else {
            ShopList shopList = JSON.parseObject(json, ShopList.class);
            if(shopList != null){
                List<ShopEntity> shop = shopList.goods_list;
                locationList = new ArrayList<>();
                for(int i = 0; i < shop.size(); i++){
                    ShopEntity shopEntity = shop.get(i);
                    LocationEntity location = new LocationEntity();
                    location.location_brief = shopEntity.goods_content;
                    //location.location_date = shopEntity.n_add_date;
                    location.location_id = shopEntity.goods_id;
                    location.location_img = shopEntity.goods_image_url;
                    location.location_title = shopEntity.goods_name;
                    location.location_visit_count = shopEntity.goods_salenum;
                    location.location_collect_count = shopEntity.sccomm;
                    location.location_evaluation_count = shopEntity.evaluation_count;
                    locationList.add(location);
                }
            }
        }
        return locationList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mAdapter != null && mAdapter.getCount() > (position - 1)){
            LocationEntity location = mAdapter.getItem(position - 1);
            if(location != null){
                Intent intent = null;
                if(type == 4 || type == 5){
                    intent = new Intent(getActivity(), NewsDetailActivity.class);
                } else {
                    intent = new Intent(getActivity(), LocationDetailActivity.class);
                    intent.putExtra("type", locationType);
                }
                intent.putExtra("image_url", location.location_img);
                intent.putExtra("name", location.location_title);
                intent.putExtra("id", location.location_id);
                startActivity(intent);
            }
        }
    }
}
