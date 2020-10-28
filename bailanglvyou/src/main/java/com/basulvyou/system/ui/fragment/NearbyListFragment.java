package com.basulvyou.system.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONObject;
import com.basulvyou.system.R;
import com.basulvyou.system.UIApplication;
import com.basulvyou.system.adapter.NearbyAdapter;
import com.basulvyou.system.api.LocationApi;
import com.basulvyou.system.entity.NearbyEntity;
import com.basulvyou.system.entity.NearbyList;
import com.basulvyou.system.ui.activity.LocationDetailActivity;
import com.basulvyou.system.util.StringUtil;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * 当地列表界面
 * Created by KevinLi on 2016/2/2.
 */
public class NearbyListFragment extends AbsLoadMoreFragment<ListView, NearbyEntity> implements AdapterView.OnItemClickListener {

    private PullToRefreshListView mPullToRefreshListView;
    private int type;
    private String locationType, locationTypeId;
    private double longitude, latitude;

    public static NearbyListFragment getInstance(Bundle bundle) {
        NearbyListFragment fragment = new NearbyListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nearby_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = getArguments().getInt("position");
        locationType = getArguments().getString("type");
        locationTypeId = getArguments().getString("typeId");
        initView(view);
        initListener();
        setAdapter();
        mPager.setPage(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        clearData();
        longitude = UIApplication.getInstance().getLongitude();
        latitude = UIApplication.getInstance().getLatitude();
        onPullUpToRefresh(mPullToRefreshListView);
    }

    public void initListener() {
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        mPullToRefreshListView.setOnItemClickListener(this);
    }

    private void initView(View view) {
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.location_list);
    }

    private void setAdapter() {
        mAdapter = new NearbyAdapter(getActivity());
        mPullToRefreshListView.setBackgroundColor(getResources().getColor(R.color.bg_main_color));
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        httpGetRequest(LocationApi.getNearbyUrl(mPager.rows + "", mPager.getPage() + "", longitude, latitude, locationTypeId), LocationApi.API_GET_LOCATION_LIST);
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
        List<NearbyEntity> locationList = null;
        if (!StringUtil.isEmpty(json)) {
            locationList = getLocationEntities(json, locationList);
        }
        appendData(locationList, start);
    }

    private List<NearbyEntity> getLocationEntities(String json, List<NearbyEntity> locationList) {
        try {
            NearbyList nearbyList = JSONObject.parseObject(json, NearbyList.class);
            if (nearbyList != null) {
                locationList = nearbyList.goods_list;
                return locationList;
            }
        } catch (Exception e) {

        }
        return locationList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mAdapter != null && mAdapter.getCount() > (position - 1)) {
            NearbyEntity entity = mAdapter.getItem(position - 1);
            if (entity != null) {
                if ("**20890".equals(entity.cls_type)) {
                    Intent intent = new Intent(getActivity(), LocationDetailActivity.class);
                    intent.putExtra("type", "view");
                    if (!StringUtil.isEmpty(entity.goods_image_url1)) {
                        intent.putExtra("image_url", entity.goods_image_url1);
                    }
                    intent.putExtra("name", entity.goods_name);
                    intent.putExtra("goods_id", entity.goods_id);
                    startActivity(intent);
                } else {//其他
                    Intent intent = new Intent(getActivity(), LocationDetailActivity.class);
                    if ("!!20888".equals(entity.cls_type)) {
                        intent.putExtra("type", "sleep");
                    } else {
                        intent.putExtra("type", "food");
                    }
                    if (!StringUtil.isEmpty(entity.goods_image_url1)) {
                        intent.putExtra("image_url", entity.goods_image_url1);
                    }
                    intent.putExtra("name", entity.goods_name);
                    intent.putExtra("goods_id", entity.goods_id);
                    startActivity(intent);
                }
            }
        }
    }
}
