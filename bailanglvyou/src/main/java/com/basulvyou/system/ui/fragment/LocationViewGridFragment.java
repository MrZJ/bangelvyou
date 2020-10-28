package com.basulvyou.system.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.adapter.LocationViewGridAdapter;
import com.basulvyou.system.api.LocationApi;
import com.basulvyou.system.entity.LocationEntity;
import com.basulvyou.system.entity.ShopBundleEntity;
import com.basulvyou.system.entity.ShopEntity;
import com.basulvyou.system.entity.ShopList;
import com.basulvyou.system.ui.activity.LocationDetailActivity;
import com.basulvyou.system.util.ListUtils;
import com.basulvyou.system.util.StringUtil;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * 当地景点Grid样式Fragment
 */
public class LocationViewGridFragment extends AbsLoadMoreFragment<GridView, LocationEntity> implements AdapterView.OnItemClickListener {

    private PullToRefreshGridView mPullToRefreshGridView;
    private int type;
    private ShopBundleEntity shopBundleEntity;
    private String locationType, locationTypeId;
    private View empty_tv;

    public static Fragment getInstance(Bundle bundle) {
        LocationViewGridFragment fragment = new LocationViewGridFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location_view_grid, container, false);
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = getArguments().getInt("position");
        locationType = getArguments().getString("type");
        locationTypeId = getArguments().getString("typeId");
        shopBundleEntity = (ShopBundleEntity) getArguments().getSerializable("shopBundle");
        initView(view);
        initListener();
        setAdapter();
        mPager.setPage(0);
        loadData();
    }

    public void initListener() {
        mPullToRefreshGridView.setOnRefreshListener(this);
        mPullToRefreshGridView.setScrollingWhileRefreshingEnabled(true);
        mPullToRefreshGridView.setOnItemClickListener(this);
    }

    private void initView(View view) {
        empty_tv = view.findViewById(R.id.empty_tv);
        mPullToRefreshGridView = (PullToRefreshGridView) view.findViewById(R.id.location_view_grid_list_frag);
    }

    private void setAdapter() {
        mAdapter = new LocationViewGridAdapter(null, getActivity());
        mPullToRefreshGridView.setAdapter(mAdapter);
    }


    @Override
    protected PullToRefreshAdapterViewBase<GridView> getRefreshView() {
        return mPullToRefreshGridView;
    }

    @Override
    protected void loadData() {
        httpGetRequest(LocationApi.getVirtualShopListUrl(shopBundleEntity.key, mPager.rows + "", mPager.getPage() + ""
                , locationTypeId, shopBundleEntity.keyword, null, shopBundleEntity.order, null, null, null), LocationApi.API_GET_LOCATION_LIST);
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
        ShopList shopList = JSON.parseObject(json, ShopList.class);
        if (shopList != null) {
            List<ShopEntity> shop = shopList.goods_list;
            locationList = new ArrayList<>();
            for (int i = 0; i < shop.size(); i++) {
                ShopEntity shopEntity = shop.get(i);
                LocationEntity location = new LocationEntity();
                location.location_brief = shopEntity.goods_content;
                //location.location_date = shopEntity.n_add_date;
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
        if (mAdapter != null && mAdapter.getCount() > (position)) {
            LocationEntity location = mAdapter.getItem(position);
            if (location != null) {
                Intent intent = new Intent(getActivity(), LocationDetailActivity.class);
                intent.putExtra("type", locationType);
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
