package com.shishoureport.system.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.AttendanceAllEntity;
import com.shishoureport.system.entity.AttendanceEntity;
import com.shishoureport.system.entity.Map;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.request.AttandenceRequest;
import com.shishoureport.system.request.AttendanceDetailRequest;
import com.shishoureport.system.utils.MySharepreference;
import com.shishoureport.system.wibget.CustomDialog;

/**
 * Created by jianzhang.
 * on 2017/9/6.
 * copyright easybiz.
 */

public class LocationActivity extends BaseActivity implements View.OnClickListener, CustomDialog.CustomInterface {
    private TextView loc_place_status_tv, loc_place_tv, mark_tv, status_tv;
    private Button lock_in_btn;
    private LocationClient mLocationClient;
    protected BaiduMap baiduMap;
    protected MapView mapView;
    private int mRadius = 1000;
    private double mLat, mLng;//公司经纬度
    private AttendanceEntity mEntity;
    private BDLocation mLocation;
    private boolean isFirstLocation = true;
    private boolean isCommitData = false;
    private Marker mCurrentMarker;
    /**
     * 公司坐标
     */
    private LatLng company_latlng;

    public static void startActivityForResult(Activity context, int requestCode) {
        Intent intent = new Intent(context, LocationActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_location;
    }

    @Override
    public void initView() {
        Map map = MySharepreference.getInstance(this).getUser().map;
        if (map != null) {
            mLat = map.lat;
            mLng = map.lng;
        }
        company_latlng = new LatLng(mLat, mLng);
        lock_in_btn = (Button) findViewById(R.id.lock_in_btn);
        loc_place_status_tv = (TextView) findViewById(R.id.loc_place_status_tv);
        loc_place_tv = (TextView) findViewById(R.id.loc_place_tv);
        status_tv = (TextView) findViewById(R.id.status_tv);
        mark_tv = (TextView) findViewById(R.id.mark_tv);
        lock_in_btn.setOnClickListener(this);
        mapView = (MapView) findViewById(R.id.bmapView);
        baiduMap = mapView.getMap();    // 获取地图控制器
        // 1.   隐藏缩放按钮、比例尺
        mapView.showScaleControl(false); // 隐藏比例按钮，默认是显示的
        mapView.showZoomControls(false); // 隐藏缩放按钮，默认是显示的
        // 2.   获取获取最小（3）、最大缩放级别（20）
        float maxZoomLevel = baiduMap.getMaxZoomLevel(); // 获取地图最大缩放级别
        float minZoomLevel = baiduMap.getMinZoomLevel(); // 获取地图最小缩放级别
        Log.i(TAG, "minZoomLevel = " + minZoomLevel + ", maxZoomLevel" + maxZoomLevel);
        // 3.   设置地图中心点为公司
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(company_latlng);
        baiduMap.setMapStatus(mapStatusUpdate);
        // 4.   设置地图缩放为15
        mapStatusUpdate = MapStatusUpdateFactory.zoomTo(15);
        baiduMap.setMapStatus(mapStatusUpdate);
        //5. 获取地图Ui控制器：隐藏指南针
        UiSettings uiSettings = baiduMap.getUiSettings();
        uiSettings.setCompassEnabled(false); //  不显示指南针
        CircleOptions options = new CircleOptions();    // 创建一个圆形覆盖物的参数
        options.center(company_latlng)   // 圆心
                .radius(mRadius)   // 半径
                .stroke(new Stroke(4, Color.parseColor("#38ACFF")))// 线条宽度、颜色
                .fillColor(Color.parseColor("#00E2F0F3")); // 圆的填充颜色
        baiduMap.addOverlay(options);   // 添加一个覆盖物
        MarkerOptions options2 = new MarkerOptions();
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.company_loc);
        options2.position(company_latlng)
                .icon(bitmapDescriptor)//设置图标样式
                .zIndex(9)// 设置marker所在层级
                .draggable(false); // 设置手势拖拽;
        //添加marker
        Marker marker = (Marker) baiduMap.addOverlay(options2);
        initLocation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lock_in_btn://打卡
                if (mLocation != null) {
                    isCommitData = true;
                    getAttendanceDetail(mLocation.getLatitude(), mLocation.getLongitude());
                } else {
                    showToast("获取定位信息失败，请重试");
                }
                break;
        }
    }

    /**
     * 获取当前城市地理位置
     */
    private void initLocation() {
        showDialog();
        baiduMap.setMyLocationEnabled(true);// 开启定位图层
        mLocationClient = new LocationClient(this);
        MyLocationListener listener = new MyLocationListener();
        mLocationClient.registerLocationListener(listener);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(10000);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, null));
    }


    /**
     * 百度地图监听
     *
     * @author Administrator
     */
    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation arg0) {
            // map view 销毁后不在处理新接收的位置
            if (arg0 == null || baiduMap == null) {
                return;
            }
            mLocation = arg0;
            double longtitude = arg0.getLongitude();
            double latitude = arg0.getLatitude();
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(arg0.getRadius())
                    .latitude(arg0.getLatitude())
                    .longitude(arg0.getLongitude()).build();
            baiduMap.setMyLocationData(locData);
            Log.e("jianzhang", "longtitude = " + longtitude + ",latitude = " + latitude);
            if (isFirstLocation) {
                isFirstLocation = false;
                loc_place_tv.setText(arg0.getAddrStr());
                getAttendanceDetail(arg0.getLatitude(), arg0.getLongitude());
                LatLng latLng = new LatLng(latitude, longtitude);
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(latLng).zoom(18.0f);
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }

    private void setData(AttendanceEntity mEntity) {
        if (mEntity.is_complete == AttendanceEntity.UNNORMAL) {
            if (mEntity.is_zt == AttendanceEntity.UNNORMAL) {
                status_tv.setText("异常");
            } else {
                status_tv.setText("正常");
            }
        } else {
            if (mEntity.is_cd == AttendanceEntity.UNNORMAL) {
                status_tv.setText("正常");
            } else {
                status_tv.setText("异常");
            }
        }
        if (mEntity.in_or_out == AttendanceEntity.UNNORMAL) {
            showToast("不在考勤范围内");
            lock_in_btn.setText("外勤打卡");
            loc_place_status_tv.setText("（不在考勤范围内）");
        } else {
            loc_place_status_tv.setText("（在考勤范围内）");
            showToast("已进入考勤范围");
            lock_in_btn.setText("正常打卡");
        }
    }

    private void showComfirmDialog() {
        CustomDialog dialog = new CustomDialog(this, "确认打卡", "取消", "确认", "当前为外勤打卡，是否打卡？", this);
        dialog.show();
    }

    @Override
    public void okClick() {
        commitData();
    }

    @Override
    public void cancelClick() {

    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        closeDialog();
        switch (action) {
            case AttendanceDetailRequest.ATTENDANCE_DETAIL_REQUEST:
                AttendanceAllEntity entity = JSONObject.parseObject(json, AttendanceAllEntity.class);
                if (entity == null || entity.map == null) {
                    showToast("获取打卡信息失败，请重试");
                } else {
                    mEntity = entity.map;
                    setData(mEntity);
                    if (isCommitData) {
                        if (mEntity.in_or_out == AttendanceEntity.UNNORMAL) {
                            showComfirmDialog();
                            lock_in_btn.setText("外勤打卡");
                        } else {
                            commitData();
                            lock_in_btn.setText("正常打卡");
                        }
                    } else {

                    }
                }
                break;
            case AttandenceRequest.ATTANDENCE_REQUEST:
                showToast("打卡成功");
                setResult(Activity.RESULT_OK);
                finish();
                break;
        }
    }

    private void commitData() {
        User user = MySharepreference.getInstance(this).getUser();
        AttandenceRequest request = new AttandenceRequest(mEntity.id, user.id, mLocation.getLatitude(), mLocation.getLongitude(), mEntity.is_no, mEntity.is_cd, mEntity.is_zt, mEntity.in_or_out, mLocation.getAddrStr(), mark_tv.getText().toString());
        httpPostRequest(request.getRequestUrl(), request.getRequestParams(), AttandenceRequest.ATTANDENCE_REQUEST);
    }

    public void getAttendanceDetail(double lat, double lng) {
        AttendanceDetailRequest request = new AttendanceDetailRequest(MySharepreference.getInstance(this).getUser().id, lat, lng);
        httpGetRequest(request.getRequestUrl(), AttendanceDetailRequest.ATTENDANCE_DETAIL_REQUEST);
    }
}
