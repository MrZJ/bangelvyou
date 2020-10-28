package com.basulvyou.system.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.adapter.LocationGoodsAdapter;
import com.basulvyou.system.adapter.LocationTodayHotAdapter;
import com.basulvyou.system.api.LocationApi;
import com.basulvyou.system.api.ShopListApi;
import com.basulvyou.system.entity.FieldErrors;
import com.basulvyou.system.entity.LocationEntity;
import com.basulvyou.system.entity.NewsEntity;
import com.basulvyou.system.entity.NewsListEntity;
import com.basulvyou.system.entity.ShopBundleEntity;
import com.basulvyou.system.entity.ShopEntityTeChan;
import com.basulvyou.system.entity.ShopListTeChan;
import com.basulvyou.system.ui.activity.GoodsDetailActivity;
import com.basulvyou.system.ui.activity.LocationDetailActivity;
import com.basulvyou.system.ui.activity.NewsDetailActivity;
import com.basulvyou.system.util.ListUtils;
import com.basulvyou.system.util.StringUtil;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

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
    private String locationType, locationTypeId, mod_id;
    private View empty_tv;

    public static Fragment getInstance(Bundle bundle) {
        LocationListFragment fragment = new LocationListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    private void showEmpty(boolean show){
        if (empty_tv != null) {
            if (show){
                empty_tv.setVisibility(View.VISIBLE);
            } else {
                empty_tv.setVisibility(View.GONE);
            }
        }
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
        mod_id = getArguments().getString("mod_id");
        shopBundleEntity = (ShopBundleEntity) getArguments().getSerializable("shopBundle");
        initView(view);
        initListener();
        setAdapter();
        mPager.setPage(0);
        loadData();
    }

    public void initListener() {
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        if (type != 3) {
            mPullToRefreshListView.setOnItemClickListener(this);
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        mPullToRefreshListView.onRefreshComplete();
    }

    private void initView(View view) {
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.location_list);
        empty_tv = view.findViewById(R.id.empty_tv);
    }

    private void setAdapter() {
        if (type == 3) {
            mAdapter = new LocationGoodsAdapter(null, getActivity());
            mPullToRefreshListView.setBackgroundColor(getResources().getColor(R.color.bg_main_color));
        } else {
            mAdapter = new LocationTodayHotAdapter(null, getActivity());
            ((LocationTodayHotAdapter) mAdapter).setIfList(true);//设置标签是列表数据（不显示热头部热门）
        }
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        if (type == 3) {//特产
            httpGetRequest(ShopListApi.getShopListUrl(shopBundleEntity.key, mPager.rows + "", mPager.getPage() + "", null, shopBundleEntity.keyword, null, null, null), LocationApi.API_GET_LOCATION_LIST);
        } else if (type == 5) {//文化
//            ((LocationTodayHotAdapter) mAdapter).setIsCulter(true);//设置标签是文化数据
            httpGetRequest(LocationApi.getNewsUrl(mPager.rows + "", mPager.getPage() + "", shopBundleEntity.keyword, shopBundleEntity.key, mod_id), LocationApi.API_GET_LOCATION_LIST);
        } else {//虚拟商品
            if (type == 2) {
                ((LocationTodayHotAdapter) mAdapter).setIsSleep(true);//设置标签是酒店数据
                httpGetRequest(LocationApi.getVirtualShopListUrl(shopBundleEntity.key, mPager.rows + "", mPager.getPage() + ""
                        , locationTypeId, shopBundleEntity.keyword, null, shopBundleEntity.order, null, null, null), LocationApi.API_GET_LOCATION_LIST);
            } else {
                httpGetRequest(LocationApi.getVirtualShopListUrl(null, mPager.rows + "", mPager.getPage() + ""
                        , locationTypeId, shopBundleEntity.keyword, null, null, null, null, null), LocationApi.API_GET_LOCATION_LIST);
            }

        }
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
        boolean isEmpty = ListUtils.isEmpty(locationList);
        if (mPager.getPage() == 0 && isEmpty){
            showEmpty(true);
        } else {
            showEmpty(false);
        }
        appendData(locationList, start);
    }

    private List<LocationEntity> getLocationEntities(String json, List<LocationEntity> locationList) {
        if (type == 3) {
            ShopListTeChan shop = JSON.parseObject(json, ShopListTeChan.class);
            if (shop != null) {
                List<ShopEntityTeChan> shopList = shop.goods_list;
                locationList = new ArrayList<>();
                for (int i = 0; i < shopList.size(); i++) {
                    ShopEntityTeChan shopEntity = shopList.get(i);
                    LocationEntity location = new LocationEntity();
                    location.location_price = "￥" + shopEntity.goods_price;
                    location.location_title = shopEntity.goods_name;
                    location.location_brief = shopEntity.goods_content;
                    location.location_id = shopEntity.goods_id;
                    location.location_img_one = shopEntity.goods_image_url;
                    location.location_collect_count = shopEntity.sccomm;
                    location.location_evaluation_count = shopEntity.evaluation_count;
                    locationList.add(location);
                }
            }
        } else if (type == 5) {
            NewsListEntity newsList = JSON.parseObject(json, NewsListEntity.class);
            if (newsList != null) {
                List<NewsEntity> news = newsList.goods_list;
                locationList = new ArrayList<>();
                for (int i = 0; i < news.size(); i++) {
                    NewsEntity newsEntity = news.get(i);
                    LocationEntity location = new LocationEntity();
                    location.location_brief = newsEntity.n_brief;
                    location.location_date = newsEntity.n_add_date;
                    location.location_id = newsEntity.n_id;
                    location.location_img_one = newsEntity.n_main_img;
                    location.location_title = newsEntity.n_title;
                    location.location_visit_count = newsEntity.n_visit_count;
                    location.location_url = newsEntity.n_url;
                    locationList.add(location);
                }
            }
        } else {
            ShopListTeChan shopList = JSON.parseObject(json, ShopListTeChan.class);
            if (shopList != null) {
                List<ShopEntityTeChan> shop = shopList.goods_list;
                locationList = new ArrayList<>();
                for (int i = 0; i < shop.size(); i++) {
                    ShopEntityTeChan shopEntity = shop.get(i);
                    LocationEntity location = new LocationEntity();
                    location.location_price = "￥" + shopEntity.goods_price + "起";
                    location.location_address = shopEntity.entp_addr;
                    location.location_id = shopEntity.goods_id;
                    location.location_img_one = shopEntity.goods_image_url1;
//                    location.location_img = shopEntity.goods_image_url;
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
        if (mAdapter != null && mAdapter.getCount() > (position - 1)) {
            LocationEntity location = mAdapter.getItem(position - 1);
            if (location != null) {
                Intent intent = null;
                if (type == 3) {//普通商品详情
                    intent = new Intent(getActivity(), GoodsDetailActivity.class);
                } else if (type == 5) {//文化详情
                    intent = new Intent(getActivity(), NewsDetailActivity.class);
                    intent.putExtra("url", location.location_url);
                    intent.putExtra("mod_id", mod_id);
                } else {//其他
                    intent = new Intent(getActivity(), LocationDetailActivity.class);
                    intent.putExtra("type", locationType);
                }
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
