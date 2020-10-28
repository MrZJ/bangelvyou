package com.basulvyou.system.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.basulvyou.system.R;
import com.basulvyou.system.adapter.CarpoolingAdapter;
import com.basulvyou.system.api.CarpoolingApi;
import com.basulvyou.system.entity.CarpoolingInfoEntity;
import com.basulvyou.system.entity.CarpoolingList;
import com.basulvyou.system.entity.ShopBundleEntity;
import com.basulvyou.system.ui.activity.CarpoolingDetailActivity2;
import com.basulvyou.system.ui.activity.LoginActivity;
import com.basulvyou.system.ui.activity.OrderCarpoolingActivity;
import com.basulvyou.system.util.ListUtils;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * 拼车列表信息显示
 */
public class CarpoolingListFragment extends AbsLoadMoreFragment<ListView, CarpoolingInfoEntity> implements AdapterView.OnItemClickListener {

    public static final String COMM_ID = "comm_id";
    private PullToRefreshListView mPullToRefreshListView;
    private ProgressBar progressBar;
    private View empty_tv;
    private int type;
    private ShopBundleEntity shopBundleEntity;
    private String locationType, key;
    private String lnglatString;
    private LocationClient mLocationClient;
    private String resType;

    public static Fragment getInstance(Bundle bundle) {
        CarpoolingListFragment fragment = new CarpoolingListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carpooling_list, null);
        initView(view);
        initListener();
        setAdapter();
        type = getArguments().getInt("position");
        locationType = getArguments().getString("type");
        key = getArguments().getString("typeId");
        resType = getArguments().getString("resType");
        shopBundleEntity = (ShopBundleEntity) getArguments().getSerializable("shopBundle");
        initLocation();
        return view;
    }

    public void initListener() {
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        mPullToRefreshListView.setOnItemClickListener(this);
    }

    private void initView(View view) {
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.carpooling_list);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        empty_tv = view.findViewById(R.id.empty_tv);
    }

    private void setAdapter() {
        mAdapter = new CarpoolingAdapter(null, getActivity(), type);
        mPullToRefreshListView.setAdapter(mAdapter);
        clearData();
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        httpGetRequest(CarpoolingApi.getCarpoolingInfo(configEntity.key, mPager.rows + "", mPager.getPage() + ""
                , key, lnglatString, "21056", resType), CarpoolingApi.API_GET_CARPOOLING_INFO_LIST);
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
                loadData();
            }
            mLocationClient.stop();
        }
    }

    private void dismissProgress() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void showEmptyTip() {
        if (mPager != null && mPager.getPage() == 0) {
            empty_tv.setVisibility(View.VISIBLE);
        }
    }

    private void dismissEmptyTip() {
        empty_tv.setVisibility(View.GONE);
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        dismissProgress();
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
        if (carpoolingList != null && !ListUtils.isEmpty(carpoolingList.goods_list)) {
            carpoolingInfoEntities = carpoolingList.goods_list;
            appendData(carpoolingInfoEntities, start);
            dismissEmptyTip();
        } else {
            showEmptyTip();
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
                if ("20890".equals(key)) {
                    Intent intent = new Intent(getActivity(), OrderCarpoolingActivity.class);
                    intent.putExtra("locationType", locationType);
                    intent.putExtra("carpoolingInfo", location);
                    startActivity(intent);
                } else if ("20888".equals(key)) {
                    Intent intent = new Intent(getActivity(), CarpoolingDetailActivity2.class);
                    intent.putExtra(CarpoolingDetailActivity2.TYPE, CarpoolingDetailActivity2.TYPE_COMFIRM);
                    intent.putExtra(COMM_ID, location.goods_id);
                    intent.putExtra("resType", resType);
                    startActivity(intent);
                } else if ("20894".equals(key)) {
                    Intent intent = new Intent(getActivity(), CarpoolingDetailActivity2.class);
                    intent.putExtra(CarpoolingDetailActivity2.TYPE, CarpoolingDetailActivity2.TYPE_GRAB);
                    intent.putExtra(COMM_ID, location.goods_id);
                    intent.putExtra("resType", resType);
                    startActivity(intent);
                } else if ("20892".equals(key)) {
                    Intent intent = new Intent(getActivity(), CarpoolingDetailActivity2.class);
                    intent.putExtra(CarpoolingDetailActivity2.TYPE, CarpoolingDetailActivity2.TYPE_ROBBED);
                    intent.putExtra(COMM_ID, location.goods_id);
                    intent.putExtra("resType", resType);
                    startActivity(intent);
                }
            }
        }
    }
}
