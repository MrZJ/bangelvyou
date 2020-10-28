package com.basulvyou.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.api.LoginApi;
import com.basulvyou.system.entity.ConfigEntity;
import com.basulvyou.system.entity.FieldErrors;
import com.basulvyou.system.entity.UserEntity;
import com.basulvyou.system.util.ConfigUtil;
import com.basulvyou.system.util.StringUtil;
import com.basulvyou.system.util.TopClickUtil;
import com.umeng.message.UmengRegistrar;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 登录界面
 **/
public class LoginActivity extends BaseActivity implements View.OnClickListener, PlatformActionListener {

    private TextView forgetPwd;
    private View weiBo, qq, weiXin;
    private EditText editPhone, editPwd;
    private Button btnLogin;
    private final static int MSG_THIRD_LOGIN = 1;
    private final static int MSG_THIRD_REGISTER = 2;
    private String userId;
    private String type = "0";
    private String userNickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ShareSDK.initSDK(this);
        intiView();
        initListener();
    }

    private void intiView() {
        initTopView();
        setLeftBackButton();
        setTitle("登录");
        setTopRightTitle("注册", TopClickUtil.TOP_REG);
        editPhone = (EditText) findViewById(R.id.edit_login_phone);
        editPwd = (EditText) findViewById(R.id.edit_login_pwd);
        btnLogin = (Button) findViewById(R.id.btn_login);
        forgetPwd = (TextView) findViewById(R.id.forget_pwd);
        forgetPwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        weiBo = findViewById(R.id.rel_weibo);
        qq = findViewById(R.id.rel_qq);
        weiXin = findViewById(R.id.rel_weixin);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBtnClickable();
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        forgetPwd.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        weiBo.setOnClickListener(this);
        qq.setOnClickListener(this);
        weiXin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login://登录
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
                Intent forgetIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                forgetIntent.putExtra("forget", "forget");
                startActivity(forgetIntent);
                break;
            case R.id.rel_weibo://微博登陆
                weiBo.setClickable(false);
                type = "3";
                Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
                authorize(sina);
                break;
            case R.id.rel_qq://qq登陆
                qq.setClickable(false);
                type = "1";
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                authorize(qzone);
                break;
            case R.id.rel_weixin://微信登陆
                //微信登录
                //测试时，需要打包签名；sample测试时，用项目里面的demokey.keystore
                //打包签名apk,然后才能产生微信的登录
                weiXin.setClickable(false);
                ShareSDK.initSDK(this);
                type = "2";
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                authorize(wechat);
                break;
        }
    }

    //第三方登录授权
    private void authorize(Platform plat) {
        if (plat == null) {
            return;
        }
        plat.setPlatformActionListener(this);
        plat.SSOSetting(false);
        plat.showUser(null);
    }

    private void login(String plat, final String userId,
                       final HashMap<String, Object> userInfo) {
        this.userId = userId;
        if (userInfo == null) {
            this.userNickName = "";
        } else {
            this.userNickName = (String) userInfo.get("nickname");
        }
        //第三方登录
        mHandler.sendEmptyMessage(MSG_THIRD_LOGIN);
    }

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
        String device_token = UmengRegistrar.getRegistrationId(this);
        params.put("device_tokens", device_token);
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
        params.put("type", type);
        return params;
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case LoginApi.API_THIRD_REGISTER:
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
        weiBo.setClickable(true);
        qq.setClickable(true);
        weiXin.setClickable(true);
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
            configEntity.isLogin = true;
            ConfigUtil.saveConfig(getApplicationContext(), configEntity);
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
        }
        appManager.finishOtherActivity();
    }

    public void onCancel(Platform platform, int action) {
        if (action == Platform.ACTION_USER_INFOR) {
        }
    }

    public void onComplete(Platform platform, int action,
                           HashMap<String, Object> res) {
        if (action == Platform.ACTION_USER_INFOR) {
            login(platform.getName(), platform.getDb().getUserId(), res);
        }
    }

    public void onError(Platform platform, int action, Throwable t) {
        if (action == Platform.ACTION_USER_INFOR) {
            platform.removeAccount();
        }
        t.printStackTrace();
    }

    public void onBackPressed() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editPhone.getWindowToken(), 0);
        super.onBackPressed();
    }

    protected void processMessage(Message msg) {
        super.processMessage(msg);

        switch (msg.what) {
            case MSG_THIRD_LOGIN:
                mHandler.removeMessages(MSG_THIRD_LOGIN);
                httpPostRequest(LoginApi.getThirdLoginUrl(),
                        getThirdRequestParams(), LoginApi.API_THIRD_LOGIN);
                break;
        }
    }
}
