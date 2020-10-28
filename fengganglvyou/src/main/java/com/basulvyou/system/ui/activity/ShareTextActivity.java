package com.basulvyou.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.basulvyou.system.R;
import com.basulvyou.system.adapter.ImgGridAdapter;
import com.basulvyou.system.api.ShareTextApi;
import com.basulvyou.system.entity.FieldErrors;
import com.basulvyou.system.util.AsynImageUtil;
import com.basulvyou.system.util.BitMapUtil;
import com.basulvyou.system.util.DensityUtil;
import com.basulvyou.system.util.ListUtils;
import com.basulvyou.system.util.StringUtil;
import com.basulvyou.system.util.ToastUtil;
import com.basulvyou.system.util.TopClickUtil;
import com.basulvyou.system.util.photo.Bimp;
import com.basulvyou.system.util.photo.ImageItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 分享界面
 */
public class ShareTextActivity extends BaseActivity implements View.OnClickListener {

    private View emptyView, loadView;
    private TextView location, tipTextView;
    private EditText shareText;
    private ProgressBar locationPro;
    private GridView imgGridView;
    private LocationClient mLocationClient;
    private String lnglatString;
    private String address;
    private boolean getLocationAgain = false;
    public static Bitmap bimap;
    private ImgGridAdapter imgAdapter;
    private int maxNum = 9;//图片最大选取数
    private static final int TAKE_PICTURE_CA = 1;//相机获取
    private PopupWindow popupWindow;
    private View btnPhotograph, btnLocation, btnCancel;
    private Animation mRotateAnimation;
    private boolean isLoading;
    private ArrayList<String> poisName = new ArrayList<>();
    public static String backAddress;
    private String detailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_text);
        bimap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_addpic_unfocused);
        initView();
        initListener();
        getLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(backAddress)) {
            location.setText("所在位置:"+backAddress);
        }
    }

    private void initView() {
        initTopView();
        setTitle("消息发布");
        setLeftBackButton();
        setTopRightTitle("发送", TopClickUtil.TOP_SEND_SHARE);
        initLoadingView();
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        location = (TextView) findViewById(R.id.tv_share_text_location);
        shareText = (EditText) findViewById(R.id.et_share_text);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,DensityUtil.px2dip(this, dm.heightPixels / 2));
        shareText.setLayoutParams(params);
        locationPro = (ProgressBar) findViewById(R.id.tv_share_text_state_pro);
        imgGridView = (GridView) findViewById(R.id.gri_share_text_img);
        Bimp.tempSelectBitmap.clear();
        imgAdapter = new ImgGridAdapter(this, maxNum);
        imgGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        imgGridView.setAdapter(imgAdapter);

        View popupView = View.inflate(ShareTextActivity.this, R.layout.pop_user_choseicon, null);
        RelativeLayout pop_layout=(RelativeLayout) popupView.findViewById(R.id.pop_parent);
        pop_layout.getBackground().setAlpha(60);//设置背景透明度
        popupWindow = new PopupWindow(ShareTextActivity.this);
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(popupView);
        btnPhotograph = popupView.findViewById(R.id.use_camera);
        btnLocation = popupView.findViewById(R.id.use_local);
        btnCancel = popupView.findViewById(R.id.item_cancel);

    }

    /**
     * 初始化数据加载布局
     */
    private void initLoadingView() {
        /**数据加载布局*/
        emptyView = findViewById(R.id.empty_view_mini);
        tipTextView = (TextView) findViewById(R.id.tip_text_mini);
        loadView = findViewById(R.id.loading_img_mini);

        if (null != emptyView) {
            View view = findViewById(R.id.loading_layout_mini);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();

            if (null != params) {
                DisplayMetrics displayMetrics = getResources()
                        .getDisplayMetrics();

                int height = displayMetrics.heightPixels;
                params.topMargin = (int) (height * 0.25);
                view.setLayoutParams(params);
            }
            mRotateAnimation = AsynImageUtil.mRotateAnimation;

            mRotateAnimation
                    .setInterpolator(AsynImageUtil.ANIMATION_INTERPOLATOR);
            mRotateAnimation
                    .setDuration(AsynImageUtil.ROTATION_ANIMATION_DURATION_SHORT);
            mRotateAnimation.setRepeatCount(Animation.INFINITE);
            mRotateAnimation.setRepeatMode(Animation.RESTART);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        location.setOnClickListener(this);
        imgGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isLoading) {
                    return;
                }
                inputMethodHide();
                if (position == Bimp.tempSelectBitmap.size()) {
                    popupWindow.showAtLocation(imgGridView,
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                } else {
                    Intent intent = new Intent(ShareTextActivity.this, GalleryActivity.class);
                    intent.putExtra("ID", position);
                    intent.putExtra("num", maxNum);
                    startActivity(intent);
                }
            }
        });
        btnCancel.setOnClickListener(ShareTextActivity.this);
        btnLocation.setOnClickListener(ShareTextActivity.this);
        btnPhotograph.setOnClickListener(ShareTextActivity.this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_share_text_location:
                if (getLocationAgain) {
                    getLocation();
                } else {
                    Intent intent = new Intent(this, ChooserAddress.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("pois", poisName);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.use_camera:
                photograph();
                popupWindowHide();
                break;
            case R.id.use_local:
                locationPhoto();
                popupWindowHide();
                break;
            case R.id.item_cancel:
                popupWindowHide();
                break;
        }
    }



    /**
     * 走拍照的流程
     */
    private void photograph() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCameraIntent, TAKE_PICTURE_CA);
    }

    /**
     * 走相册流程
     */
    private void locationPhoto() {
        Intent intent = new Intent(this, AlbumActivity.class);
        intent.putExtra("id", "1");
        intent.putExtra("num", maxNum);
        startActivity(intent);
    }

    /**
     * 上传并分享
     */
    public void saveAndShareText() {
        if (!configEntity.isLogin) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        if (shareText == null || TextUtils.isEmpty(shareText.getText())) {
            Toast.makeText(this, "请写下你想要发送的内容", Toast.LENGTH_SHORT).show();
            return;
        }
        params.put("title", shareText.getText().toString());
        params.put("summary", shareText.getText().toString());
        params.put("key", configEntity.key);
        StringBuffer img = new StringBuffer();
        if (!ListUtils.isEmpty(Bimp.tempSelectBitmap)) {
            if (Bimp.tempSelectBitmap.size() < 1) {
                Toast.makeText(this, "请上传至少一张图片", Toast.LENGTH_SHORT).show();
                return;
            }
            for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
                if (i == Bimp.tempSelectBitmap.size() - 1) {
                    img.append(BitMapUtil.convertIconToString(Bimp.tempSelectBitmap.get(i).getBitmap()));
                } else {
                    img.append(BitMapUtil.convertIconToString(Bimp.tempSelectBitmap.get(i).getBitmap()) + ",");
                }
            }
            if (!StringUtil.isEmpty(img.toString())) {
                params.put("main_pic", img.toString());
            }
        } else {
            Toast.makeText(this, "请上传至少一张图片", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isEmpty(address)) {
            ToastUtil.showToast("地理位置信息为空", ShareTextActivity.this, ToastUtil.DELAY_SHORT);
            return;
        } else {
            params.put("addr", address);
            params.put("addr_latlng", lnglatString);
        }
        showUpLoading("正在上传中...", true);
        httpPostRequest(ShareTextApi.getShareTextUrl(), params, ShareTextApi.API_SHARE_TEXT);
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        hiddenUpLoading();
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case ShareTextApi.API_SHARE_TEXT:
                hiddenUpLoading();
                Toast.makeText(this, "发送成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        imgAdapter.update();
    }

    private void getLocation() {
        location.setVisibility(View.GONE);
        locationPro.setVisibility(View.VISIBLE);
        initLocation();
    }

    /**
     * 获取当前城市地理位置
     */
    private void initLocation() {
        mLocationClient = new LocationClient(ShareTextActivity.this);
        MyLocationListener listener = new MyLocationListener();
        mLocationClient.registerLocationListener(listener);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setIsNeedLocationPoiList(true);
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
            if (arg0.getLocType() != BDLocation.TypeNetWorkLocation) {
                ToastUtil.showToast("获取位置信息失败", ShareTextActivity.this, ToastUtil.DELAY_SHORT);
                getLocationAgain = true;
                location.setText("点击重新获取位置信息");
                location.setClickable(true);
            } else {
                lnglatString = String.valueOf(arg0.getLongitude()) + "," + String.valueOf(arg0.getLatitude());
                detailAddress = arg0.getAddrStr();//当前位子详细地址
                address = arg0.getProvince() + arg0.getCity();
                if(!TextUtils.isEmpty(detailAddress)) {
                    location.setText("所在位置:" + detailAddress);
                }
                final List<Poi> pois = arg0.getPoiList();
                if(!ListUtils.isEmpty(pois)){
                    new Thread(){
                        @Override
                        public void run() {
                            for (Poi poi : pois) {
                                poisName.add(poi.getName());
                            }
                        }
                    }.start();
                }
            }
            location.setVisibility(View.VISIBLE);
            locationPro.setVisibility(View.GONE);
            mLocationClient.stop();
        }

    }

    /**
     * 返回方法获取到头像
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE_CA://相机获取图片
                if (Bimp.tempSelectBitmap.size() < maxNum && resultCode == RESULT_OK) {
                    String fileName = String.valueOf(System.currentTimeMillis());
                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(bm);
                    Bimp.tempSelectBitmap.add(takePhoto);
                }
                break;
        }
    }

    /**
     * 显示加载布局
     */
    public void showUpLoading(final String msg, final boolean load) {
        isLoading = true;
        location.setClickable(false);
        top_right_title.setClickable(false);
        btn_top_goback.setClickable(false);
        shareText.setEnabled(false);
        mHandler.post(new Runnable() {

            public void run() {
                if (null != emptyView) {
                    tipTextView.setText(msg);
                    emptyView.setVisibility(View.VISIBLE);
                    loadView.setVisibility(View.VISIBLE);
                    loadView.startAnimation(mRotateAnimation);
                }
            }
        });

    }

    /**
     * dimiss加载布局
     */
    public void hiddenUpLoading() {
        mHandler.post(new Runnable() {
            public void run() {
                if (null != emptyView) {
                    emptyView.setVisibility(View.GONE);
                    loadView.clearAnimation();
                }

            }
        });
        location.setClickable(true);
        isLoading = false;
        top_right_title.setClickable(true);
        btn_top_goback.setClickable(true);
        shareText.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mLocationClient && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
        Bimp.tempSelectBitmap.clear();
        imgAdapter = null;
        backAddress = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindowHide();
                return true;
            }
            if (isLoading) {
                Toast.makeText(this, "正在上传中，请稍后...", Toast.LENGTH_SHORT).show();
                return true;
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 隐藏弹出窗口
     */
    private void popupWindowHide() {
        popupWindow.dismiss();
    }

    /**
     * 隐藏输入法
     */
    private void inputMethodHide(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isActive()){
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
