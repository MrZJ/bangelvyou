package com.basulvyou.system.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.basulvyou.system.R;
import com.basulvyou.system.adapter.LogisticAdapter;
import com.basulvyou.system.api.CarpoolingApi;
import com.basulvyou.system.entity.CarpoolingInfoEntity;
import com.basulvyou.system.entity.CarpoolingList;
import com.basulvyou.system.entity.ShopBundleEntity;
import com.basulvyou.system.ui.activity.LoginActivity;
import com.basulvyou.system.ui.activity.OrderCarpoolingActivity;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * 物流信息
 */
public class LogisticListFragment extends AbsLoadMoreFragment<ListView, CarpoolingInfoEntity> implements AdapterView.OnItemClickListener {

    private PullToRefreshListView mPullToRefreshListView;
    private int type;
    private ShopBundleEntity shopBundleEntity;
    private String locationType, key;
    private String lnglatString;
    private LocationClient mLocationClient;

    public static Fragment getInstance(Bundle bundle) {
        LogisticListFragment fragment = new LogisticListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_logistic_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLocation();
        type = getArguments().getInt("position");
        locationType = getArguments().getString("type");
        key = getArguments().getString("typeId");
        shopBundleEntity = (ShopBundleEntity) getArguments().getSerializable("shopBundle");
        initView(view);
        initListener();
        setAdapter();
        loadData();
    }

    public void initListener() {
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        mPullToRefreshListView.setOnItemClickListener(this);
    }

    private void initView(View view) {
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.logistic_list);
    }

    private void setAdapter() {
        mAdapter = new LogisticAdapter(null, getActivity(), LogisticAdapter.TYPE_1);
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        httpGetRequest(CarpoolingApi.getCarpoolingList(configEntity.key, mPager.rows + "", mPager.getPage() + ""
                , key, lnglatString, "21058"), CarpoolingApi.API_GET_CARPOOLING_INFO_LIST);
    }

    /**
     * 获取当前城市地理位置
     */
    private void initLocation() {
        mLocationClient = new LocationClient(getActivity());
        MyLocationListener listener = new MyLocationListener();
        mLocationClient.registerLocationListener(listener);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    /**
     * 百度地图监听
     *
     * @author Administrator
     */
    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation arg0) {
            if (arg0.getLocType() == BDLocation.TypeNetWorkLocation) {
                lnglatString = String.valueOf(arg0.getLongitude()) + "," + String.valueOf(arg0.getLatitude());
            }
            mLocationClient.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mLocationClient && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case CarpoolingApi.API_GET_CARPOOLING_INFO_LIST:
                carpoolingHander(json);
            default:
                break;
        }
    }

    /**
     * 处理列表信息
     *
     * @param json
     */
    private void carpoolingHander(String json) {
        final long start = System.currentTimeMillis();
        CarpoolingList carpoolingList = JSON.parseObject(json, CarpoolingList.class);
        List<CarpoolingInfoEntity> carpoolingInfoEntities = null;
        if (null != carpoolingList) {
            carpoolingInfoEntities = carpoolingList.goods_list;
            appendData(carpoolingInfoEntities, start);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!configEntity.isLogin) {
            Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            return;
        }
        if (mAdapter != null && mAdapter.getCount() > (position - 1)) {
            CarpoolingInfoEntity location = mAdapter.getItem(position - 1);
            if (location != null) {
                Intent intent = new Intent(getActivity(), OrderCarpoolingActivity.class);
                intent.putExtra("carpoolingInfo", location);
                startActivity(intent);
            }
        }
    }
}