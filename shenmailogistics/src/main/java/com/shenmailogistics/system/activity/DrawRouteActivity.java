package com.shenmailogistics.system.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.trace.TraceOverlay;
import com.lzy.okgo.OkGo;
import com.shenmailogistics.system.R;
import com.shenmailogistics.system.api.UpdataLocationApi;
import com.shenmailogistics.system.bean.LocationEntity;
import com.shenmailogistics.system.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class DrawRouteActivity extends BaseActivity {

    @Bind(R.id.map)
    MapView mMapView;

    private AMap mAMap;
    private String id;

    @Override
    public int getLayoutId() {
        return R.layout.activity_draw_route;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        id = getIntent().getStringExtra("traceId");
    }

    @Override
    public void initView() {
        initTopView();
        setTopTitle("行程轨迹");
        if (mAMap == null) {
            mAMap = mMapView.getMap();
        }
        mAMap.getUiSettings().setRotateGesturesEnabled(false);
        mAMap.getUiSettings().setZoomControlsEnabled(true);
        mAMap.getUiSettings().setScaleControlsEnabled(true);
        initTopListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        getTraceDetail();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
        OkGo.getInstance().cancelTag(this);
    }

    private void getTraceDetail(){
        httpGetRequest(this, UpdataLocationApi.getTraceDetail(id),UpdataLocationApi.API_GET_DETIAL);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){
            case UpdataLocationApi.API_GET_DETIAL:
                detailHandler(json);
                break;
        }
    }

    private void detailHandler(String json){
        LocationEntity entity = JSON.parseObject(json,LocationEntity.class);
        if(entity != null){
            if(!StringUtil.isListEmpty(entity.list)){
                TraceOverlay mTraceOverlay = new TraceOverlay(mAMap);
                List<LatLng> latLngs = new ArrayList<LatLng>();
                for (int i = 0; i < entity.list.size() ; i++) {
                    latLngs.add(new LatLng(entity.list.get(i).lat,entity.list.get(i).lon));
                }
                /*mAMap.addPolyline(new PolylineOptions().color(
                        Color.BLUE).addAll(latLngs).width(10));*/
                mAMap.addMarker(new MarkerOptions().position(latLngs.get(0)).icon(
                        BitmapDescriptorFactory.fromResource(R.drawable.start)));
                mAMap.addMarker(new MarkerOptions().position(latLngs.get(latLngs.size()-1)).icon(
                        BitmapDescriptorFactory.fromResource(R.drawable.end)));
                mTraceOverlay.setTraceStatus(TraceOverlay.TRACE_STATUS_PROCESSING);
                mTraceOverlay.add(latLngs);
                mTraceOverlay.setProperCamera(latLngs);
                mAMap.moveCamera(CameraUpdateFactory.zoomTo(17));
            }else{
                Toast.makeText(this,"轨迹绘制失败",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"轨迹绘制失败",Toast.LENGTH_SHORT).show();
        }
    }


}
