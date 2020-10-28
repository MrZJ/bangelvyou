package com.shenmai.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shenmai.system.R;
import com.shenmai.system.api.SaveUserApi;
import com.shenmai.system.entity.FieldErrors;
import com.shenmai.system.utlis.AsynImageUtil;
import com.shenmai.system.utlis.BitmapHelper;
import com.shenmai.system.utlis.CacheImgUtil;
import com.shenmai.system.utlis.ConfigUtil;
import com.shenmai.system.utlis.StringUtil;
import com.shenmai.system.utlis.TopClickUtil;
import com.shenmai.system.utlis.photo.Bimp;
import com.shenmai.system.utlis.photo.ImageItem;
import com.shenmai.system.widget.CircleImageView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;

/**
 * 修改个人信息
 */
public class TouxiangActivity extends BaseActivity implements View.OnClickListener{

    private View imgItem;
    private CircleImageView myImg;// 我的头像
    private PopupWindow pop = null;// 选择图片来源
    private View use_camera;//使用相机
    private View use_local;//使用本地照片
    private Button cancel;//取消
    private View ll_other;
    private View parentView;
    private EditText etName;
    private static final int TAKE_PICTURE_Ca = 1;//相机获取
    private static final int TAKE_PICTURE_Lo = 2;//本地获取
    private Bitmap icon;
    private Animation mRotateAnimation;//加载动画
    private boolean isLoading;//是否在加载
    private String firstUserName;
    private Callback.Cancelable cancelable;
    private Uri picUri = null;
    private String imageUrl;
    private String imgLocalPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView=getLayoutInflater().inflate(R.layout.activity_touxiang,null);
        setContentView(parentView);
        firstUserName=configEntity.username;
        initView();
        initListener();
    }

    private void initView(){
        initTopView();
        setTitle("修改个人资料");
        setLeftBackButton();
        setTopRightTitle("保存", TopClickUtil.TOP_SAVE_TOUXIANGANDNAME);
        initLoadingView();
        etName = (EditText) findViewById(R.id.et_name);
        etName.setText(configEntity.username);
        imgItem = findViewById(R.id.rel_account_selecticon);
        myImg = (CircleImageView) findViewById(R.id.img_account_usericon);
        pop = new PopupWindow(TouxiangActivity.this);
        View view = getLayoutInflater().inflate(R.layout.pop_user_choseicon,null);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        LinearLayout pop_layout=(LinearLayout) view.findViewById(R.id.pop_parent);
        pop_layout.getBackground().setAlpha(60);//设置背景透明度
        ll_other = view.findViewById(R.id.ll_other);
        use_camera = view.findViewById(R.id.use_camera);
        use_local = view.findViewById(R.id.use_local);
        cancel = (Button) view.findViewById(R.id.item_cancel);
        File iconFile = new File(CacheImgUtil.user_icon);
        if (iconFile.exists()) {
            String path = iconFile.getAbsolutePath();
            icon = BitmapFactory.decodeFile(path);
            myImg.setImageBitmap(icon);
        }else{
            myImg.setImageResource(R.mipmap.mine_toux);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        imgItem.setOnClickListener(this);
        use_camera.setOnClickListener(this);
        use_local.setOnClickListener(this);
        cancel.setOnClickListener(this);
        ll_other.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_account_selecticon://显示图像获取方式（拍照/本地/取消）
                if(isLoading){
                    return;
                }
                inputMethodHide();
                pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.use_camera://打开照相机
                photograph();
                pop.dismiss();
                break;
            case R.id.use_local://打开本地图库
                locationPhoto();
                pop.dismiss();
                break;
            case R.id.item_cancel://取消
            case R.id.ll_other:
                pop.dismiss();
                break;
            default:
                break;
        }
    }

    /**
     * 上传用户头像信息
     */
    public void saveAndUploading() {
        String name=etName.getText().toString();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"用户名不能为空！",Toast.LENGTH_SHORT).show();
            return;
        }else{
            if(!TextUtils.isEmpty(firstUserName)
                    && name.equals(firstUserName)
                    && (Bimp.oneSelectBitmap == null
                    || Bimp.oneSelectBitmap.getBitmap() == null)) {
                Toast.makeText(this, "未修改任何信息", Toast.LENGTH_SHORT).show();
                return;
            }else{
                configEntity.username = name;
                ConfigUtil.saveConfig(this, configEntity);
                showUpLoading("正在上传中...");
                httpPostRequest(SaveUserApi.getSaveUserUrl(), getRequestParams(), SaveUserApi.API_SAVE_USER);
            }
        }
    }

    /**
     * 处理图片并上传
     * @param uri
     */
    private void updataImg(Uri uri) {
        showUpLoading("正在上传中...");
        final String imgpath = CacheImgUtil.getImageAbsolutePath(this, uri);
        File tempFile = new File(imgpath);
        if (!tempFile.exists()) {
            Toast.makeText(this, "图片不存在", Toast.LENGTH_SHORT).show();
            hiddenUpLoading();
            return;
        }
        final Bitmap bps = BitmapHelper.compressBitmap(imgpath, BitmapHelper.width, BitmapHelper.height);
        if (null == bps) {
            Toast.makeText(this, "图片压缩失败", Toast.LENGTH_SHORT).show();
            hiddenUpLoading();
            return;
        }
        String tmppath = BitmapHelper.compressBitmap(this, imgpath, bps, true);
        final File tempFilemin = new File(tmppath);
        if (!tempFilemin.exists()) {
            Toast.makeText(this, "图片不存在", Toast.LENGTH_SHORT).show();
            hiddenUpLoading();
            return;
        }
        upLoadingNameAndIcon(tempFilemin);
    }

    private void upLoadingNameAndIcon(final File tempFilemin) {


        RequestParams params = new RequestParams(SaveUserApi.getUpdateIconUrl());
        params.setMultipart(true);
        params.addQueryStringParameter("key", configEntity.key);
        params.addQueryStringParameter("type", "image");
        params.addBodyParameter(tempFilemin.getPath().replace("/", ""), tempFilemin);
        cancelable = x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String s) {
                String text = s;
                if (StringUtil.isEmpty(text)) {
                    Toast.makeText(TouxiangActivity.this, "上传失败，没有返回数据", Toast.LENGTH_SHORT).show();
                    hiddenUpLoading();
                    return;
                }
                JSONObject json = JSON.parseObject(text);
                String ret = json.getString("code");
                String msg = json.getString("msg");
                imageUrl = json.getString("url");
                tempFilemin.delete();
                if(!StringUtil.isEmpty(imgLocalPath)) {
                    File tempFile = new File(imgLocalPath);
                    if (tempFile.exists()) {
                        tempFile.delete();
                    }
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                if (!StringUtil.isEmpty(throwable.getMessage())) {
                    Toast.makeText(TouxiangActivity.this, "上传失败，没有返回数据", Toast.LENGTH_SHORT).show();
                    tempFilemin.delete();
                    Bimp.tempSelectBitmap.remove(Bimp.tempSelectBitmap.size() - 1);
                    if(!StringUtil.isEmpty(imgLocalPath)) {
                        File tempFile = new File(imgLocalPath);
                        if (tempFile.exists()) {
                            tempFile.delete();
                        }
                    }
                }
                hiddenUpLoading();
            }

            @Override
            public void onCancelled(CancelledException e) {
                tempFilemin.delete();
                Bimp.tempSelectBitmap.remove(Bimp.tempSelectBitmap.size() - 1);
                if(!StringUtil.isEmpty(imgLocalPath)) {
                    File tempFile = new File(imgLocalPath);
                    if (tempFile.exists()) {
                        tempFile.delete();
                    }
                }
            }

            @Override
            public void onFinished() {
                hiddenUpLoading();
            }
        });
}

    /**
     * 用户上传头像参数
     *
     * @return
     */
    private HashMap<String,String> getRequestParams(){
        HashMap<String,String> params = new HashMap<>();
        params.put("key", configEntity.key);
        params.put("real_name", etName.getText().toString());
        if (!StringUtil.isEmpty(imageUrl)) {
            params.put("user_logo", imageUrl);
        }
        return params;
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        hiddenUpLoading();
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        hiddenUpLoading();
        Toast.makeText(this,"用户信息修改成功",Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * 返回方法获取到头像
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageItem takePhoto = new ImageItem();
        switch (requestCode) {
            case TAKE_PICTURE_Ca://相机获取图片
                if (resultCode == RESULT_OK) {
                    if (data == null || "".equals(data)) {
                        return;
                    }
                    String imgpath = CacheImgUtil.getImageAbsolutePath(this, picUri);
                    icon = BitmapHelper.compressBitmap(imgpath, BitmapHelper.width, BitmapHelper.height);
                    takePhoto.setBitmap(icon);
                    Bimp.oneSelectBitmap = takePhoto;
                    myImg.setImageBitmap(icon);
                    updataImg(picUri);
                }
                break;
            case TAKE_PICTURE_Lo://本地图片
                if (resultCode == RESULT_OK) {
                    try {
                        String imgpath1 = CacheImgUtil.getImageAbsolutePath(this, data.getData());
                        imgLocalPath = CacheImgUtil.PATH_CACHE + "/" + System.currentTimeMillis() + ".jpg";
                        if(!copyFile(imgpath1, imgLocalPath)){
                            Toast.makeText(this, "返回图片出错", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        picUri = Uri.fromFile(new File(imgLocalPath));
                        File tempFile = new File(imgLocalPath);
                        if (!tempFile.exists()) {
                            Toast.makeText(this, "图片不存在", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        icon = BitmapHelper.compressBitmap(imgLocalPath, BitmapHelper.width, BitmapHelper.height);
                        if (null == icon) {
                            Toast.makeText(this, "图片压缩失败", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        takePhoto.setBitmap(icon);
                        Bimp.oneSelectBitmap = takePhoto;
                        myImg.setImageBitmap(icon);
                        updataImg(picUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    myImg.setImageBitmap(icon);
                }
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
        startActivityForResult(openCameraIntent, TAKE_PICTURE_Ca);
    }

    /**
     * 走相册流程
     */
    private void locationPhoto() {
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, TAKE_PICTURE_Lo);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != icon) {
            icon.recycle();
            icon = null;
        }
        if (Bimp.oneSelectBitmap != null) {
            Bimp.oneSelectBitmap = null;
        }
        if(!StringUtil.isEmpty(imgLocalPath)) {
            File tempFile = new File(imgLocalPath);
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
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

    /**显示加载布局*/
    public void showUpLoading(final String msg) {
        btn_top_goback.setClickable(false);
        top_right_title.setClickable(false);
        isLoading=true;
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
        btn_top_goback.setClickable(true);
        top_right_title.setClickable(true);
        isLoading=false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if (isLoading){
                Toast.makeText(this,"正在上传中...",Toast.LENGTH_SHORT).show();
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
            buffer = null;
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
