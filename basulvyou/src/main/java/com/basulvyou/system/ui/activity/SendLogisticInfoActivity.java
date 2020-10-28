package com.basulvyou.system.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.basulvyou.system.R;
import com.basulvyou.system.adapter.ImgGridAdapter;
import com.basulvyou.system.api.CarpoolingApi;
import com.basulvyou.system.util.BitMapUtil;
import com.basulvyou.system.util.ListUtils;
import com.basulvyou.system.util.StringUtil;
import com.basulvyou.system.util.photo.Bimp;
import com.basulvyou.system.util.photo.ImageItem;

import java.util.HashMap;

/**
 * 物流信息发布
 */
public class SendLogisticInfoActivity extends BaseActivity implements View.OnClickListener{
    private EditText startAds,endAds,tel,suggest,prices,other;
    private Button sendInfo;
    private String editStartDate;
    private ImageView getLocal;
    private ProgressBar proLocal;
    private LocationClient mLocationClient;
    private String address;
    private String lnglatString;
    private PopupWindow pop = null;// 选择图片来源
    private View use_camera;//使用相机
    private View use_local;//使用本地照片
    private Button imgCancel;//取消
    private GridView noScrollgridview;
    public static Bitmap bimap ;
    private static final int TAKE_PICTURE_Ca = 1;//相机获取
    private View parentView;
    private String imgString;
    private ImgGridAdapter imgAdapter;
    private int maxNum = 1;//图片最大选取数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = getLayoutInflater().inflate(R.layout.activity_send_logistic_info,null);
        setContentView(parentView);
        bimap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_addpic_unfocused);
        initView();
        initListener();
    }

    private void initView(){
        initTopView();
        setTitle("信息发布");
        setLeftBackButton();
        startAds = (EditText) findViewById(R.id.edt_send_logistic_info_startads);
        endAds = (EditText) findViewById(R.id.edt_send_logistic_info_endads);
        tel = (EditText) findViewById(R.id.edt_send_logistic_info_tel);
        prices = (EditText) findViewById(R.id.edt_send_logistic_info_price);
        suggest = (EditText) findViewById(R.id.edt_send_logistic_info_suggest);
        other = (EditText) findViewById(R.id.edt_send_logistic_info_other);
        sendInfo = (Button) findViewById(R.id.btn_send_logistic_info_send);
        getLocal = (ImageView) findViewById(R.id.img_send_logistic_info_getstartads);
        proLocal = (ProgressBar) findViewById(R.id.pro_send_logistic_info_getstartads);
        getLocation();

        noScrollgridview = (GridView) findViewById(R.id.gri_send_logistic_info_img);
        pop = new PopupWindow(SendLogisticInfoActivity.this);
        View view = getLayoutInflater().inflate(R.layout.pop_user_choseicon,null);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        RelativeLayout pop_layout=(RelativeLayout) view.findViewById(R.id.pop_parent);
        pop_layout.getBackground().setAlpha(60);//设置背景透明度
        use_camera = view.findViewById(R.id.use_camera);
        use_local = view.findViewById(R.id.use_local);
        imgCancel = (Button) view.findViewById(R.id.item_cancel);
        imgAdapter = new ImgGridAdapter(this,maxNum);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        noScrollgridview.setAdapter(imgAdapter);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        sendInfo.setOnClickListener(this);
        getLocal.setOnClickListener(this);
        use_camera.setOnClickListener(this);
        use_local.setOnClickListener(this);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == Bimp.tempSelectBitmap.size()) {
                    pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                } else {
                    Intent intent = new Intent(SendLogisticInfoActivity.this, GalleryActivity.class);
                    intent.putExtra("ID", position);
                    intent.putExtra("num", maxNum);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send_logistic_info_send:
                checkEdti();
                break;
            case R.id.img_send_logistic_info_getstartads:
                getLocation();
                break;
            case R.id.use_camera://打开照相机
                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(openCameraIntent, TAKE_PICTURE_Ca);
                pop.dismiss();
                break;
            case R.id.use_local://打开本地图库
                Intent intent = new Intent(this, AlbumActivity.class);
                intent.putExtra("id","1");
                intent.putExtra("num",maxNum);
                startActivity(intent);
                pop.dismiss();
                break;
            case R.id.item_cancel://关闭图片选择
                pop.dismiss();
                break;
        }
    }

    /**
     * 获取地理位置和经纬度
     */
    private void getLocation(){
        getLocal.setVisibility(View.GONE);
        proLocal.setVisibility(View.VISIBLE);
        initLocation();
    }


    /**
     * 判断字段是否为空
     */
    private void checkEdti(){
        if(StringUtil.isEmpty(startAds.getText().toString().trim())){
            Toast.makeText(this,"请输入出发地",Toast.LENGTH_SHORT).show();
            return;
        }
        if(StringUtil.isEmpty(endAds.getText().toString().trim())){
            Toast.makeText(this,"请输入目的地",Toast.LENGTH_SHORT).show();
            return;
        }
        if(StringUtil.isEmpty(tel.getText().toString().trim())){
            Toast.makeText(this,"请输入电话号码",Toast.LENGTH_SHORT).show();
            return;
        }
        if(StringUtil.isEmpty(prices.getText().toString().trim())){
            Toast.makeText(this,"请输入价格",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!ListUtils.isEmpty(Bimp.tempSelectBitmap)){
            imgString = BitMapUtil.convertIconToString(Bimp.tempSelectBitmap.get(0).getBitmap());
        }
        sendInfo();
    }

    /**
     * 发布信息
     */
    private void sendInfo(){
        httpPostRequest(CarpoolingApi.sendCarpoolingInfo(), getRequsetParams(), CarpoolingApi.API_SEND_CARPOOLING_INFO);
    }

    private HashMap<String,String> getRequsetParams(){
        HashMap<String,String> requestParams = new HashMap<>();
        requestParams.put("key",configEntity.key);
        requestParams.put("comm_type","104");
        requestParams.put("price_ref",prices.getText().toString().trim());
        requestParams.put("pcqd",startAds.getText().toString().trim());
        requestParams.put("pczd",endAds.getText().toString().trim());
        requestParams.put("comm_content",tel.getText().toString().trim());
        if(!StringUtil.isEmpty(suggest.getText().toString().trim())){
            requestParams.put("comm_paramenter", suggest.getText().toString().trim());
        }
        if(!StringUtil.isEmpty(other.getText().toString().trim())){
            requestParams.put("comm_asrc_content", other.getText().toString().trim());
        }
        if(!StringUtil.isEmpty(lnglatString)){
            requestParams.put("lnglat", lnglatString);
        }
        if(!StringUtil.isEmpty(imgString)){
            requestParams.put("main_pic",imgString);
        }
        return requestParams;
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){
            case CarpoolingApi.API_SEND_CARPOOLING_INFO:
                Toast.makeText(this,"发布成功",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    /**
     * 获取当前城市地理位置
     */
    private void initLocation() {
        mLocationClient = new LocationClient(SendLogisticInfoActivity.this);
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
     * @author Administrator
     */
    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation arg0) {
            if(arg0.getLocType() != BDLocation.TypeNetWorkLocation ){
                Toast.makeText(SendLogisticInfoActivity.this, "获取位置信息失败", Toast.LENGTH_SHORT).show();
            }else{
                lnglatString = String.valueOf(arg0.getLongitude()) +","+ String.valueOf(arg0.getLatitude());
                address = arg0.getAddrStr();
                startAds.setText(address);
            }
            getLocal.setVisibility(View.VISIBLE);
            proLocal.setVisibility(View.GONE);
            mLocationClient.stop();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        imgAdapter.update();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mLocationClient && mLocationClient.isStarted()){
            mLocationClient.stop();
        }
        Bimp.tempSelectBitmap.clear();
        imgAdapter = null;
    }

    /**
     * 返回方法获取到头像
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE_Ca://相机获取图片
                if (Bimp.tempSelectBitmap.size() < 5 && resultCode == RESULT_OK) {
                    String fileName = String.valueOf(System.currentTimeMillis());
                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(bm);
                    Bimp.tempSelectBitmap.add(takePhoto);
                }
                break;
        }
    }

}
