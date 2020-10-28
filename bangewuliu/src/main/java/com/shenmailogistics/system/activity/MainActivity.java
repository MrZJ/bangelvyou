package com.shenmailogistics.system.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.hss01248.dialog.StyledDialog;
import com.shenmailogistics.system.R;
import com.shenmailogistics.system.api.UpdataLocationApi;
import com.shenmailogistics.system.utils.StatusBarUtil;
import com.shenmailogistics.system.utils.StringUtil;
import com.shenmailogistics.system.utils.UIUtils;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;


public class MainActivity extends BaseActivity implements AMap.OnMyLocationChangeListener{

    @Bind(R.id.map)
    MapView mapView;
    @Bind(R.id.img_statr)
    ImageView start;
    @Bind(R.id.img_list)
    ImageView list;
    @Bind(R.id.tv_user_name)
    TextView userName;
    @Bind(R.id.tv_end)
    TextView end;
    @Bind(R.id.lin_system_bar)
    LinearLayout systemBar;
    @Bind(R.id.tv_record_state)
    TextView recordTip;

    private AMap aMap;
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private Timer timer;
    private StringBuffer locationStr = new StringBuffer();
    private final static int MSG_IS_UPDATE = 1;
    String recordId;
    String state = "0";
    private boolean isEnd = false;
    private boolean isStart = false;
    private String startDate;
    float distance = 0;//总距离
    private HashMap<String,LatLng> firstLatLng = new HashMap();
    private PowerManager.WakeLock mWakeLock;//设置屏幕常亮

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
    }

    @Override
    public void initView() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "MIAN");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int sysbarHeight = StatusBarUtil.getStatusBarHeight(this);
            LinearLayout.LayoutParams sysBarParams = (LinearLayout.LayoutParams) systemBar.getLayoutParams();
            sysBarParams.height = sysbarHeight;
            systemBar.setLayoutParams(sysBarParams);
        }
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        aMap.setOnMyLocationChangeListener(this);
        updateApk();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        userName.setText(configEntity.username);
        mWakeLock.acquire();
    }

    private void updataLocation(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(MSG_IS_UPDATE);
            }
        },1000*60,1000*60);
    }

    @OnClick(R.id.img_statr)
    void startRecord(){
        if(isStart){
            UIUtils.showToast("路径正在记录中");
        }else{
            isStart = true;
            isEnd = false;
            state = "0";
            startDate = StringUtil.getCurrentData();
            recordId = null;//刷新记录id
            distance = 0;//清空距离
            UIUtils.showToast("开始记录路径");
            recordTip.setVisibility(View.VISIBLE);
            updataLocation();
        }
    }

    @OnClick(R.id.tv_end)
    void finishRecord(){
        if(isStart){
            isStart = false;
            isEnd = true;
            state = "1";
            timer.cancel();
            timer = null;
            StyledDialog.buildMdLoading("数据上传中..").setCancelable(false,false).show();
            httpPostRequest(this,UpdataLocationApi.updataUrl(),getRequestParams(),UpdataLocationApi.API_UPDATA);
            recordTip.setVisibility(View.GONE);
        }else{
            UIUtils.showToast("还未开始记录");
        }

    }

    @OnClick(R.id.img_list)
    void toList(){
        Intent intent = new Intent(this,TraceListActivity.class);
        startActivity(intent);
    }

    protected void processMessage(Message msg) {
        super.processMessage(msg);
        switch (msg.what) {
            case MSG_IS_UPDATE:
                mHandler.removeMessages(MSG_IS_UPDATE);
                httpPostRequest(this, UpdataLocationApi.updataUrl(),getRequestParams(),UpdataLocationApi.API_UPDATA);
                break;
        }
    }

    @Override
    public void httpResponse(String json, int action){
        super.httpResponse(json, action);
        switch (action) {
            case UpdataLocationApi.API_UPDATA:
                recordId = JSON.parseObject(json).getString("app_record_id");
                if(isEnd){
                    StyledDialog.dismissLoading();
                    UIUtils.showToast("路径记录成功");
                    isEnd = false;
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void viewCanClick() {
        super.viewCanClick();
        StyledDialog.dismissLoading();
    }

    private HashMap<String,String> getRequestParams(){
        HashMap<String,String> params = new HashMap<>();
        if(!StringUtil.isEmpty(recordId)){
            params.put("app_record_id",recordId);
        }
        params.put("app_type","android");
        params.put("app_site", locationStr.toString());
        locationStr = new StringBuffer();
        params.put("app_add_user_id",configEntity.key);
        params.put("app_start_date",startDate);
        if(isEnd){
            params.put("app_end_date",StringUtil.getCurrentData());
            params.put("app_mileage",String.valueOf(distance));
        }
        params.put("app_state",state);
        return params;
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.getUiSettings().setScaleControlsEnabled(true);
        aMap.getUiSettings().setCompassEnabled(true);
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        setupLocationStyle();
    }

    private void setupLocationStyle(){
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.drawable.gps_point));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(STROKE_COLOR);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(5);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(FILL_COLOR);
        //设置跟随模式
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
        myLocationStyle.interval(1000*5);
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        mWakeLock.release();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if(timer != null){
            timer.cancel();
            timer = null;
        }
//        OkGo.getInstance().cancelAll();
    }

    @Override
    public void onMyLocationChange(Location location) {
        if(location != null) {
//            Log.e("amap", "onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());
            if(isStart){
                double betweenDis = 0;//两次定位的距离
                locationStr.append(location.getLongitude()+","+location.getLatitude()+",");
                LatLng secondLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                if(firstLatLng.get("preLatLag") == null){
                    betweenDis = AMapUtils.calculateLineDistance(secondLatLng, secondLatLng);
                }else{
                    betweenDis = AMapUtils.calculateLineDistance(firstLatLng.get("preLatLag"), secondLatLng);
                }
                distance = (float) (distance + betweenDis);
                firstLatLng.put("preLatLag",new LatLng(location.getLatitude(), location.getLongitude()));
            }
//            Bundle bundle = location.getExtras();
            aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
            /*if(bundle != null) {
                int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
                String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
                // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
                int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);
                Log.e("amap", "定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType );
            } else {
                Log.e("amap", "定位信息， bundle is null ");
            }*/
        } else {
            UIUtils.showToast("定位失败");
        }
    }
}
