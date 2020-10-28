package com.chongqingliangyou.system.ui.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chongqingliangyou.system.R;
import com.chongqingliangyou.system.api.LoginApi;
import com.chongqingliangyou.system.entity.ConfigEntity;
import com.chongqingliangyou.system.entity.FieldErrors;
import com.chongqingliangyou.system.entity.UserEntity;
import com.chongqingliangyou.system.util.ConfigUtil;
import com.chongqingliangyou.system.util.MD5Util;
import com.chongqingliangyou.system.util.StringUtil;
import com.umeng.message.UmengRegistrar;
import com.chongqingliangyou.system.util.ToastUtil;

import java.util.HashMap;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private TextView forgetPwd;
    private EditText editPhone,editPwd;
    private Button btnLogin;
    private String pwdStr;
    //密码错误次数
    private int errorCount;
    private final static int MSG_IS_FINISH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intiView();
        initListener();
    }

    private void intiView(){
        initTopView();
        setLeftBackButton();
        setTitle("登录");
        editPhone = (EditText) findViewById(R.id.edit_login_phone);
        editPwd = (EditText) findViewById(R.id.edit_login_pwd);
        btnLogin = (Button) findViewById(R.id.btn_login);
        forgetPwd = (TextView) findViewById(R.id.forget_pwd);
        forgetPwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login://登录
                String phoneStr = editPhone.getText().toString().trim();
                pwdStr = editPwd.getText().toString().trim();
                if(phoneStr == null || phoneStr.length() == 0){
                    Toast.makeText(this, "请先输入用户名或手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pwdStr == null || pwdStr.length() == 0){
                    Toast.makeText(this, "请先输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                btnLogin.setClickable(false);
                login(phoneStr, pwdStr);
                break;
            case R.id.forget_pwd://忘记密码
//                Intent forgetIntent = new Intent(LoginActivity.this, FindPwdActivity.class);
//                startActivity(forgetIntent);
                break;
        }
    }

    /**
     * 登录
     * @param phone
     */
    private void login(String phone, String pwd){
        httpPostRequest(LoginApi.getLoginUrl(),
                getRequestParams(phone, pwd), LoginApi.API_LOGIN);
    }

    /**
     * 获取登录参数
     *
     * @return
     */
    private HashMap<String,String> getRequestParams(String phone, String pwd){
        HashMap<String,String> params = new HashMap<>();
        params.put("login_name", phone);
        params.put("password", pwd);
        params.put("client", "android");
        String device_token = UmengRegistrar.getRegistrationId(this);
        params.put("device_tokens", device_token);
        return params;
    }

    @Override
    public void httpResponse(String json, int action){
        super.httpResponse(json, action);
        switch (action)
        {
            case LoginApi.API_LOGIN:
                LoginHander(json);
                break;
            default:
                break;
        }
    }

    /**
     * 密码错误处理
     *
     * @param error
     * @param action
     */
    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        if(error!=null) {
            if (!StringUtil.isEmpty(error.msg) && error.msg.equals("密码错误！")) {
                errorCount++;
            }
        }
        if(errorCount==3){
            ToastUtil.showToast("密码输错三次,即将关闭应用", this, ToastUtil.DELAY_SHORT);
            errorCount=0;
            mHandler.sendEmptyMessageDelayed(MSG_IS_FINISH, 2000);
        }
        setBtnClickable();
    }

    protected void processMessage(Message msg) {
        super.processMessage(msg);
        switch (msg.what) {
            case MSG_IS_FINISH:
                appManager.finishActivity();
                break;
        }
    }

    /**
     * 设置按钮可以点击
     */
    private void setBtnClickable() {
        btnLogin.setClickable(true);
    }

    /**
     * 处理登录信息
     * @param json
     */
    private void LoginHander(String json){
        setBtnClickable();
        UserEntity userEntity = JSON.parseObject(json, UserEntity.class);
        if(userEntity != null){
            ConfigEntity configEntity = new ConfigEntity();
            configEntity.username = userEntity.user_name;
            configEntity.key = userEntity.key;
            configEntity.isLogin = true;
            configEntity.passwordMD5 = MD5Util.getMD5Str(pwdStr);
            ConfigUtil.saveConfig(getApplicationContext(), configEntity);
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }



}
