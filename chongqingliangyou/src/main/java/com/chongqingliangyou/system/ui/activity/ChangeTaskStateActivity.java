package com.chongqingliangyou.system.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.chongqingliangyou.system.R;
import com.chongqingliangyou.system.adapter.ImgGridAdapter;
import com.chongqingliangyou.system.api.TaskApi;
import com.chongqingliangyou.system.util.AsynImageUtil;
import com.chongqingliangyou.system.util.BitmapHelper;
import com.chongqingliangyou.system.util.CacheImgUtil;
import com.chongqingliangyou.system.util.StringUtil;
import com.chongqingliangyou.system.util.ToastUtil;
import com.chongqingliangyou.system.util.photo.Bimp;
import com.chongqingliangyou.system.util.photo.ImageItem;
import com.chongqingliangyou.system.view.BetterSpinner;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 修改任务状态界面
 */
public class ChangeTaskStateActivity extends BaseActivity implements View.OnClickListener{

    private View emptyView, loadView;
    private TextView location, tipTextView;
    private ProgressBar locationPro;
    private GridView imgGridView;
    private Button updata;
    private String taskId;
    private LocationClient mLocationClient;
    private String lnglatString;
    private String address;
    private boolean getLocationAgain = false;
    public static Bitmap bimap ;
    private ImgGridAdapter imgAdapter;
    private int maxNum = 3;//图片最大选取数
    private static final int TAKE_PICTURE_CA = 1;//相机获取
    private static final int LOCATION_PHOTO = 2;//相册获取
    private Uri picUri = null;
    private BetterSpinner spinnerState;
    private final String states[]=new String[]{"处理中", "已办结"};
    private final String statesCode[]=new String[]{"10", "30"};
    private List<String> imgUrl = new ArrayList<>();//保存从服务器返回的图片地址
    private String changedState = "10";//默认的状态
    private PopupWindow popupWindow;
    private Button btnPhotograph, btnLocation, btnCancel;
    private Animation mRotateAnimation;
    private boolean isLoading;
    private Callback.Cancelable cancelable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_task_state);
        taskId = getIntent().getStringExtra("id");
        bimap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_addpic_unfocused);
        initView();
        initListener();
        getLocation();
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("更新任务状态");
        initLoadingView();
        spinnerState = (BetterSpinner) findViewById(R.id.spinner_change_task_state);
        location = (TextView) findViewById(R.id.tv_change_task_state_location);
        locationPro = (ProgressBar) findViewById(R.id.tv_change_task_state_pro);
        imgGridView = (GridView) findViewById(R.id.gri_change_task_state_img);
        updata = (Button) findViewById(R.id.btn_change_task_state);
        imgAdapter = new ImgGridAdapter(this,maxNum);
        imgGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        imgGridView.setAdapter(imgAdapter);

        ArrayAdapter<String> statesAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_dropdown_item_1line, states);
        /*statesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/
        spinnerState.setAdapter(statesAdapter);
        spinnerState.setText(states[0]);
        View popupView = View.inflate(ChangeTaskStateActivity.this, R.layout.photo_more, null);
        popupWindow = new PopupWindow(ChangeTaskStateActivity.this);
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(popupView);
        btnPhotograph = (Button) popupView.findViewById(R.id.btn_photo_more_photograph);
        btnLocation = (Button) popupView.findViewById(R.id.btn_photo_more_location);
        btnCancel = (Button) popupView.findViewById(R.id.btn_photo_more_cancel);

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
        updata.setOnClickListener(this);
        location.setOnClickListener(this);
        imgGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isLoading) {
                    return;
                }
                if (position == Bimp.tempSelectBitmap.size()) {
                    popupWindow.showAtLocation(imgGridView,
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            }
        });
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changedState = statesCode[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        btnCancel.setOnClickListener(ChangeTaskStateActivity.this);
        btnLocation.setOnClickListener(ChangeTaskStateActivity.this);
        btnPhotograph.setOnClickListener(ChangeTaskStateActivity.this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_change_task_state_location:
                if(getLocationAgain){
                    getLocation();
                }
                break;
            case R.id.btn_change_task_state:
                changeState();
                break;
            case R.id.btn_photo_more_photograph:
                photograph();
                popupWindowHide();
                break;
            case R.id.btn_photo_more_location:
                locationPhoto();
                popupWindowHide();
                break;
            case R.id.btn_photo_more_cancel:
                popupWindowHide();
                break;
        }
    }

    /**
     * 走拍照的流程
     */
    private void photograph() {
        String imgPath = CacheImgUtil.PATH_CACHE + "/" + System.currentTimeMillis() + ".jpg";
        picUri = Uri.fromFile(new File(imgPath));
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE_CA);
    }

    /**
     * 走相册流程
     */
    private void locationPhoto() {
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, LOCATION_PHOTO);
    }

    /**
     * 修改任务状态
     */
    private void changeState(){
        HashMap<String,String>  params= new HashMap<>();
        params.put("id", taskId);
        params.put("key", configEntity.key);
        params.put("state", changedState);
        if(StringUtil.isEmpty(address)){
            ToastUtil.showToast("地理位置信息为空", ChangeTaskStateActivity.this, ToastUtil.DELAY_SHORT);
            return;
        }else{
            params.put("rel_addr",address);
            params.put("map_position",lnglatString);
        }
        if (null != imgUrl && imgUrl.size() > 0){
            StringBuffer imgurl = new StringBuffer();
            for (int i = 0; i <imgUrl.size() ; i++) {
                if (imgUrl.size() == 0) {
                    imgurl.append(imgUrl.get(i));
                }else if(i == imgUrl.size()-1){
                    imgurl.append(imgUrl.get(i));
                }else{
                    imgurl.append(imgUrl.get(i)+",");
                }
            }
            if(!StringUtil.isEmpty(imgurl.toString())){
                params.put("url",imgurl.toString());
            }

        }
        httpPostRequest(TaskApi.getTaskStateUrl(), params, TaskApi.API_CHANGE_TASK);
    }

    @Override
    public void httpResponse(String json, int action){
        super.httpResponse(json, action);
        switch (action)
        {
            case  TaskApi.API_CHANGE_TASK:
                Toast.makeText(this,"更新任务状态成功",Toast.LENGTH_SHORT).show();
                ChangeTaskStateActivity.this.finish();
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

    private void getLocation(){
        location.setVisibility(View.GONE);
        locationPro.setVisibility(View.VISIBLE);
        initLocation();
    }

    /**
     * 获取当前城市地理位置
     */
    private void initLocation() {
        mLocationClient = new LocationClient(ChangeTaskStateActivity.this);
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
            if(arg0.getLocType() != BDLocation.TypeNetWorkLocation) {
                ToastUtil.showToast("获取位置信息失败", ChangeTaskStateActivity.this, ToastUtil.DELAY_SHORT);
                getLocationAgain = true;
                location.setText("点击重新获取位置信息");
                location.setClickable(true);
            }else{
                lnglatString = String.valueOf(arg0.getLongitude()) +","+ String.valueOf(arg0.getLatitude());
                address = arg0.getAddrStr();
                location.setText("所在位置:"+address);
                location.setClickable(false);
            }
            location.setVisibility(View.VISIBLE);
            locationPro.setVisibility(View.GONE);
            mLocationClient.stop();
        }
    }

    /**
     * 返回方法获取到头像
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE_CA://相机获取图片
                if (Bimp.tempSelectBitmap.size() < maxNum && resultCode == RESULT_OK) {
//                  bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picUri);
                    String imgpath = CacheImgUtil.getImageAbsolutePath(ChangeTaskStateActivity.this, picUri);
                    Bitmap bitmap = BitmapHelper.compressBitmap(imgpath, BitmapHelper.width, BitmapHelper.height);
                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(bitmap);
                    Bimp.tempSelectBitmap.add(takePhoto);
                    updataImg(picUri);
                }
                break;
            case LOCATION_PHOTO://相册获取图片
                if (Bimp.tempSelectBitmap.size() < maxNum && resultCode == RESULT_OK) {
                    Bitmap bitmap1 = null;
                    try {
                        /*bitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());*/
                        String imgpath1 = CacheImgUtil.getImageAbsolutePath(ChangeTaskStateActivity.this, data.getData());
                        String imgpath = CacheImgUtil.PATH_CACHE + "/" + System.currentTimeMillis() + ".jpg";
                        if(!copyFile(imgpath1,imgpath)){
                            ToastUtil.showToast("返回图片出错", this, ToastUtil.DELAY_SHORT);
                            return;
                        }
                        picUri = Uri.fromFile(new File(imgpath));
                        File tempFile = new File(imgpath);
                        if (!tempFile.exists()) {
                            ToastUtil.showToast("图片不存在", this, ToastUtil.DELAY_SHORT);
                            return;
                        }
                        bitmap1 = BitmapHelper.compressBitmap(imgpath, BitmapHelper.width, BitmapHelper.height);
                        if (null == bitmap1) {
                            ToastUtil.showToast("图片压缩失败", this, ToastUtil.DELAY_SHORT);
                            return;
                        }
                        ImageItem takePhoto = new ImageItem();
                        takePhoto.setBitmap(bitmap1);
                        Bimp.tempSelectBitmap.add(takePhoto);
                        updataImg(picUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    /**
     * 处理图片并上传
     * @param uri
     */
    private void updataImg(Uri uri){
        showUpLoading("正在上传中...",true);
        final String imgpath = CacheImgUtil.getImageAbsolutePath(ChangeTaskStateActivity.this, uri);
        File tempFile = new File(imgpath);
        if (!tempFile.exists()) {
            ToastUtil.showToast("图片不存在", this, ToastUtil.DELAY_SHORT);
            hiddenUpLoading();
            return;
        }
        final Bitmap bps = BitmapHelper.compressBitmap(imgpath, BitmapHelper.width, BitmapHelper.height);
        if (null == bps) {
            ToastUtil.showToast("图片压缩失败", this, ToastUtil.DELAY_SHORT);
            hiddenUpLoading();
            return;
        }
        String tmppath = BitmapHelper.compressBitmap(ChangeTaskStateActivity.this, imgpath, bps, true);
        final File tempFilemin = new File(tmppath);
        if (!tempFilemin.exists()) {
            ToastUtil.showToast("图片不存在", this, ToastUtil.DELAY_SHORT);
            hiddenUpLoading();
            return;
        }
        RequestParams params = new RequestParams(TaskApi.getUpdataTaskImgUrl());
        params.setMultipart(true);
        params.addQueryStringParameter("key", configEntity.key);
        params.addQueryStringParameter("dir", "image");
        params.addBodyParameter(tempFilemin.getPath().replace("/", ""), tempFilemin);
        cancelable = x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String s) {
                String text = s;
                if (StringUtil.isEmpty(text)) {
                    ToastUtil.showToast("上传失败，没有返回数据", ChangeTaskStateActivity.this, ToastUtil.DELAY_SHORT);
                    hiddenUpLoading();
                    return;
                }
                JSONObject json = JSON.parseObject(text);
                String ret = json.getString("code");
                String msg = json.getString("msg");
                String url = json.getString("url");
                if (!StringUtil.isEmpty(url)) {
                    imgUrl.add(url);
                }
                tempFilemin.delete();
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                if (!StringUtil.isEmpty(throwable.getMessage())) {
                    Toast.makeText(ChangeTaskStateActivity.this, "上传图片失败", Toast.LENGTH_SHORT).show();
                    tempFilemin.delete();
                    Bimp.tempSelectBitmap.remove(Bimp.tempSelectBitmap.size() - 1);
                    imgAdapter.notifyDataSetChanged();
                }
                hiddenUpLoading();
            }

            @Override
            public void onCancelled(CancelledException e) {
                tempFilemin.delete();
                Bimp.tempSelectBitmap.remove(Bimp.tempSelectBitmap.size() - 1);
                imgAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFinished() {
                hiddenUpLoading();
            }
        });
    }

    /**显示加载布局*/
    public void showUpLoading(final String msg, final boolean load) {
        isLoading = true;
        spinnerState.isNotShow = true;
        updata.setClickable(false);
        location.setClickable(false);
        mHandler.post(new Runnable() {

            public void run() {
                if (null != emptyView) {
                    tipTextView.setText(msg);
                    emptyView.setVisibility(View.VISIBLE);
                    loadView.setVisibility(View.VISIBLE);
                    loadView.startAnimation(mRotateAnimation);
//                    if (load) {
//                        loadView.setVisibility(View.VISIBLE);
//                        loadView.startAnimation(mRotateAnimation);
//                    } else {
//                        loadView.clearAnimation();
//                        loadView.setVisibility(View.INVISIBLE);
//                    }
                }
            }
        });

    }

    /**dimiss加载布局*/
    public void hiddenUpLoading() {
        mHandler.post(new Runnable() {
            public void run() {
                if (null != emptyView) {
                    emptyView.setVisibility(View.GONE);
                    loadView.clearAnimation();
                }

            }
        });
        updata.setClickable(true);
        location.setClickable(true);
        isLoading = false;
        spinnerState.isNotShow = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mLocationClient && mLocationClient.isStarted()){
            mLocationClient.stop();
        }
        Bimp.tempSelectBitmap.clear();
        imgAdapter = null;
        if(null != imgUrl && imgUrl.size() > 0){
            imgUrl.clear();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if (popupWindow!=null && popupWindow.isShowing()){
                popupWindowHide();
                return true;
            }
            if(cancelable!=null && !cancelable.isCancelled()){
                cancelable.cancel();
                return true;
            }
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
     * 复制文件
     *
     * @param oldPath 原路径
     * @param newPath 新路径
     * @return
     */
    public boolean copyFile(String oldPath, String newPath){
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (!oldfile.exists() || !oldfile.isFile() || !oldfile.canRead()) {
                return false;
            }
            inStream = new FileInputStream(oldPath); //读入原文件
            fs = new FileOutputStream(newPath);
            byte[] buffer = new byte[1444];
            while ( (byteread = inStream.read(buffer)) != -1) {
                fs.write(buffer, 0, byteread);
            }
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(inStream != null){
                try {
                    inStream.close();
                    inStream = null;
                }catch (Exception e){

                }
            }
            if(fs != null){
                try {
                    fs.close();
                    fs = null;
                }catch (Exception e){

                }
            }
        }
        return false;
    }
}
