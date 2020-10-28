package com.shenmai.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shenmai.system.R;
import com.shenmai.system.api.LoginApi;
import com.shenmai.system.entity.ConfigEntity;
import com.shenmai.system.entity.FieldErrors;
import com.shenmai.system.entity.UserEntity;
import com.shenmai.system.utlis.AppManager;
import com.shenmai.system.utlis.AsynImageUtil;
import com.shenmai.system.utlis.CacheImgUtil;
import com.shenmai.system.utlis.ConfigUtil;
import com.shenmai.system.utlis.StringUtil;
import com.shenmai.system.utlis.TopClickUtil;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录界面
 **/
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private TextView forgetPwd;
    private EditText editPhone, editPwd;
    private Button btnLogin;
    private final static int MSG_THIRD_LOGIN = 1;
    private String userId;
    private String type = "0";
    private String userNickName;
    private String iconUrl;
    private RelativeLayout rel_weibo;
    private RelativeLayout rel_qq;
    private RelativeLayout rel_weixin;
    private Animation mRotateAnimation;//加载动画
    private ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intiView();
        initListener();
    }

    private void intiView() {
        initTopView();
        setLeftBackButton();
        setTitle("登录");
        setTopRightTitle("注册", TopClickUtil.TOP_REG);
        initLoadingView();
        editPhone = (EditText) findViewById(R.id.edit_login_phone);
        editPwd = (EditText) findViewById(R.id.edit_login_pwd);
        btnLogin = (Button) findViewById(R.id.btn_login);
        forgetPwd = (TextView) findViewById(R.id.forget_pwd);
        forgetPwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        rel_weibo = (RelativeLayout) findViewById(R.id.rel_weibo);
        rel_qq = (RelativeLayout) findViewById(R.id.rel_qq);
        rel_weixin = (RelativeLayout) findViewById(R.id.rel_weixin);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBtnClickable();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hiddenUpLoading();
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        forgetPwd.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        rel_weibo.setOnClickListener(this);
        rel_weixin.setOnClickListener(this);
        rel_qq.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login://登录
                type = "0";
                String phoneStr = editPhone.getText().toString().trim();
                String pwdStr = editPwd.getText().toString().trim();
                if (StringUtil.isEmpty(phoneStr)) {
                    Toast.makeText(this, "请先输入用户名或手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtil.isEmpty(pwdStr)) {
                    Toast.makeText(this, "请先输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                btnLogin.setClickable(false);
                login(phoneStr, pwdStr);
                break;
            case R.id.forget_pwd://忘记密码
                Intent forgetIntent = new Intent(this, GetTestCodeActivity.class);
                startActivity(forgetIntent);
                break;
            case R.id.rel_weibo:
                type = "3";
                getAute(SHARE_MEDIA.SINA);
                rel_weibo.setClickable(false);
                break;
            case R.id.rel_qq:
                if (mShareAPI.isInstall(this, SHARE_MEDIA.QQ)) {
                    type = "1";
                    getAute(SHARE_MEDIA.QQ);
                    rel_qq.setClickable(false);
                } else {
                    Toast.makeText(this, "请先安装QQ客户端", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rel_weixin:
                if (mShareAPI.isInstall(this, SHARE_MEDIA.WEIXIN)) {
                    type = "2";
                    getAute(SHARE_MEDIA.WEIXIN);
                    rel_weixin.setClickable(false);
                } else {
                    Toast.makeText(this, "请先安装微信客户端", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 第三方授权
     */
    public void getAute(SHARE_MEDIA platform) {
        showUpLoading("正在跳转第三方授权...");
        mShareAPI.getPlatformInfo(this, platform, umAuthListener);
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (!StringUtil.isEmpty(data.get("screen_name"))) {//新浪 qq 微信 name
                userNickName = data.get("screen_name");
            } else {
                Toast.makeText(LoginActivity.this, "第三方登录异常", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!StringUtil.isEmpty(data.get("openid"))) {//qq 微信 openid
                userId = data.get("openid");
            }
            if (StringUtil.isEmpty(userId) && !StringUtil.isEmpty(data.get("uid"))) {//微博
                userId = data.get("uid");
            }
            if (!StringUtil.isEmpty(data.get("profile_image_url"))) {//微博
                iconUrl = data.get("profile_image_url");
            }
            if (StringUtil.isEmpty(userId)) {
                Toast.makeText(LoginActivity.this, "第三方登录异常", Toast.LENGTH_SHORT).show();
                return;
            }
            //第三方登录
            mHandler.sendEmptyMessage(MSG_THIRD_LOGIN);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            setBtnClickable();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            setBtnClickable();
        }
    };

    /**
     * 登录
     *
     * @param phone
     */
    private void login(String phone, String pwd) {
        httpPostRequest(LoginApi.getLoginUrl(),
                getRequestParams(phone, pwd), LoginApi.API_LOGIN);
    }

    /**
     * 获取登录参数
     *
     * @return
     */
    private HashMap<String, String> getRequestParams(String phone, String pwd) {
        HashMap<String, String> params = new HashMap<>();
        params.put("username", phone);
        params.put("password", pwd);
        params.put("client", "android");
        params.put("device_tokens", ConfigUtil.phoneIMEI);
        return params;
    }

    /**
     * 获取第三方登录参数
     *
     * @return
     */
    private HashMap<String, String> getThirdRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("open_id", userId);
        params.put("nick_name", userNickName);
        params.put("user_logo", iconUrl);
        params.put("type", type);
        params.put("client", "android");
        params.put("device_tokens", ConfigUtil.phoneIMEI);
        return params;
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case LoginApi.API_THIRD_LOGIN:
            case LoginApi.API_LOGIN:
                LoginHander(json);
                break;
            default:
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        setBtnClickable();
    }

    /**
     * 设置按钮可以点击
     */
    private void setBtnClickable() {
        btnLogin.setClickable(true);
        rel_weibo.setClickable(true);
        rel_weixin.setClickable(true);
        rel_qq.setClickable(true);
    }

    /**
     * 处理登录信息
     *
     * @param json
     */
    private void LoginHander(String json) {
        setBtnClickable();
        UserEntity userEntity = JSON.parseObject(json, UserEntity.class);
        if (userEntity != null) {
            ConfigEntity configEntity = new ConfigEntity();
            configEntity.username = userEntity.username;
            configEntity.key = userEntity.key;
            configEntity.userRole = userEntity.userRole;
            if (!type.equals("0") && StringUtil.isEmpty(iconUrl)) {
                imageLoader.loadImage(iconUrl,AsynImageUtil.getImageOptions(),
                        new AsynImageUtil.PromotionNotFirstDisplayListener(CacheImgUtil.user_icon));
            }
            configEntity.isLogin = true;
            configEntity.isFirst = false;
            ConfigUtil.saveConfig(getApplicationContext(), configEntity);
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            if(userEntity.userRole.equals("1")){
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, NoticeActivity.class);
                startActivity(intent);
            }
            AppManager.getInstance().finishMNOtherActivity();
        }
    }


    public void onBackPressed() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editPhone.getWindowToken(), 0);
        super.onBackPressed();
    }

    protected void processMessage(Message msg) {
        switch (msg.what) {
            case MSG_THIRD_LOGIN://第三方登录
                mHandler.removeMessages(MSG_THIRD_LOGIN);
                httpPostRequest(LoginApi.getThirdLoginUrl(),
                        getThirdRequestParams(), LoginApi.API_THIRD_LOGIN);
                break;
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

    /**
     * 显示加载布局
     */
    public void showUpLoading(final String msg) {
        btn_top_goback.setClickable(false);
        top_right_title.setClickable(false);
        editPhone.setEnabled(false);
        editPwd.setEnabled(false);
        btnLogin.setEnabled(false);
        rel_qq.setEnabled(false);
        rel_weixin.setEnabled(false);
        rel_weibo.setEnabled(false);
        forgetPwd.setEnabled(false);
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
        btn_top_goback.setClickable(true);
        top_right_title.setClickable(true);
        editPhone.setEnabled(true);
        editPwd.setEnabled(true);
        btnLogin.setEnabled(true);
        rel_qq.setEnabled(true);
        rel_weixin.setEnabled(true);
        rel_weibo.setEnabled(true);
        forgetPwd.setEnabled(true);
    }
}
