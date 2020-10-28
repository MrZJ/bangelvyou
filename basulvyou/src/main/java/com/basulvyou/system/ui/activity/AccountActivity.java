package com.basulvyou.system.ui.activity;

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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.basulvyou.system.R;
import com.basulvyou.system.api.SaveUserApi;
import com.basulvyou.system.entity.FieldErrors;
import com.basulvyou.system.util.AsynImageUtil;
import com.basulvyou.system.util.BitMapUtil;
import com.basulvyou.system.util.CacheImgUtil;
import com.basulvyou.system.util.ConfigUtil;
import com.basulvyou.system.util.TopClickUtil;
import com.basulvyou.system.util.photo.Bimp;
import com.basulvyou.system.util.photo.ImageItem;
import com.basulvyou.system.wibget.CircleImageView;

import java.io.File;
import java.util.HashMap;

/**
 * 账户管理中心
 */
public class AccountActivity extends BaseActivity implements View.OnClickListener{

    private View imgItem;
    private CircleImageView myImg;// 我的头像
    private TextView myName;// 我的用户名
    private PopupWindow pop = null;// 选择图片来源
    private View use_camera;//使用相机
    private View use_local;//使用本地照片
    private Button cancel;//取消
    private View parentView;
    private static final int TAKE_PICTURE_Ca = 1;//相机获取
    private static final int TAKE_PICTURE_Lo = 2;//本地获取
    private Bitmap icon;
    private String path;//头像路径
    private Animation mRotateAnimation;//加载动画
    private boolean isLoading;//是否在加载
    private String firstUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView=getLayoutInflater().inflate(R.layout.activity_account,null);
        setContentView(parentView);
        firstUserName=configEntity.username;
        initView();
        initListener();
    }

    private void initView(){
        initTopView();
        setTitle("账户中心");
        setLeftBackButton();
        setTopRightTitle("保存", TopClickUtil.TOP_SAVE);
        initLoadingView();
        imgItem = findViewById(R.id.rel_account_selecticon);
        myImg = (CircleImageView) findViewById(R.id.img_account_usericon);
        myName = (TextView) findViewById(R.id.tv_account_username);
        myName.setText(configEntity.username);//设置用户名
        pop = new PopupWindow(AccountActivity.this);
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
        cancel = (Button) view.findViewById(R.id.item_cancel);
        File iconFile = new File(CacheImgUtil.user_icon);
        if (iconFile.exists()) {
            String path = iconFile.getAbsolutePath();
            icon = BitmapFactory.decodeFile(path);
            myImg.setImageBitmap(icon);
        }else{
            myImg.setImageResource(R.mipmap.touxiang);
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
                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(openCameraIntent, TAKE_PICTURE_Ca);
                pop.dismiss();
                break;
            case R.id.use_local://打开本地图库
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, TAKE_PICTURE_Lo);
                pop.dismiss();
                break;
            case R.id.item_cancel://取消
                pop.dismiss();
                break;
            default:
                break;
        }
    }

    /**
     * 上传用户信息
     */
    public void saveAndUploading() {
        String name=myName.getText().toString();
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
                ConfigUtil.saveConfig(AccountActivity.this, configEntity);
                showUpLoading("正在上传中...");
                httpPostRequest(SaveUserApi.getSaveUserUrl(), getRequestParams(), SaveUserApi.API_SAVE_USER);
            }
        }

    }

    /**
     * 修改用户信息上传参数
     *
     * @return
     */
    private HashMap<String,String> getRequestParams(){
        HashMap<String,String> params = new HashMap<>();
        params.put("key", configEntity.key);
        if (Bimp.oneSelectBitmap != null
                && Bimp.oneSelectBitmap.getBitmap() != null) {
            String imgString = BitMapUtil.convertIconToString(Bimp.oneSelectBitmap.getBitmap());
            params.put("user_logo", imgString);
        }
        params.put("real_name", configEntity.username);
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
                    icon = (Bitmap) data.getExtras().get("data");
                    takePhoto.setBitmap(icon);
                    Bimp.oneSelectBitmap = takePhoto;
                    myImg.setImageBitmap(icon);
                }
                break;
            case TAKE_PICTURE_Lo://本地图片
                if (resultCode == RESULT_OK) {
                    if (data == null || "".equals(data)) {
                        return;
                    }
                    Uri thisUri = data.getData();
                    String imgpath = CacheImgUtil.getImageAbsolutePath(this, thisUri);
                    icon = BitMapUtil.compressBitmap(imgpath, 480, 800);
                    takePhoto.setBitmap(icon);
                    Bimp.oneSelectBitmap = takePhoto;
                    myImg.setImageBitmap(icon);
                }
                break;
        }
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
        myName.setEnabled(false);
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
        myName.setEnabled(true);
        isLoading=false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if (isLoading){
                Toast.makeText(this,"正在上传中...",Toast.LENGTH_SHORT).show();
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
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
