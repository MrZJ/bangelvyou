package com.yishangshuma.bangelvyou.ui.activity;

import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.yishangshuma.bangelvyou.R;

/**
 * 地址地图页面
 * Created by KevinLi on 2016/2/18.
 */
public class MapActivity  extends BaseActivity{

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LatLng cenpt;
    private double lat, lng;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        lat = getIntent().getExtras().getDouble("lat");
        lng = getIntent().getExtras().getDouble("lng");
        name = getIntent().getExtras().getString("name");
        initView();
        initListener();
    }

    private  void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("景点");
        mMapView = (MapView) findViewById(R.id.mapView);
        setMapCenter();
        setTextOverLay();
    }

    /**
     * 设置显示地图中心点
     */
    private void setMapCenter() {
        mBaiduMap = mMapView.getMap();
        //设定中心点坐标
        cenpt = new LatLng(lat, lng);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(17)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    /**
     * 设置地点文字覆盖
     */
    private void setTextOverLay(){
        //构建文字Option对象，用于在地图上添加文字
        OverlayOptions textOption = new TextOptions()
                .bgColor(0xAAFFFF00)
                .fontSize(28)
                .fontColor(0xFFFF00FF)
                .text(name)
                .rotate(0)
                .position(cenpt);
        //在地图上添加该文字对象并显示
        mBaiduMap.addOverlay(textOption);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
    }

    @Override
    protected void onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

}
