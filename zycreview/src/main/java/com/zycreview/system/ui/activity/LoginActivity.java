package com.zycreview.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.umeng.message.PushAgent;
import com.zycreview.system.R;
import com.zycreview.system.api.LoginApi;
import com.zycreview.system.entity.ConfigEntity;
import com.zycreview.system.entity.FieldErrors;
import com.zycreview.system.entity.UserEntity;
import com.zycreview.system.utils.ConfigUtil;
import com.zycreview.system.utils.StringUtil;

import java.util.HashMap;


/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private TextView forgetPwd, tvRegister;
    private EditText editPhone, editPwd;
    private Button btnLogin;

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
        editPhone = (EditText) findViewById(R.id.edit_login_phone);
        editPwd = (EditText) findViewById(R.id.edit_login_pwd);
        btnLogin = (Button) findViewById(R.id.btn_login);
        forgetPwd = (TextView) findViewById(R.id.forget_pwd);
        tvRegister = (TextView) findViewById(R.id.tv_register);
        /*forgetPwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);*/
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
        tvRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
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
                /*Intent forgetIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                forgetIntent.putExtra("forget","forget");
                startActivity(forgetIntent);*/
                break;
            case R.id.tv_register://注册
                Intent registerIntent = new Intent(LoginActivity.this, RegisterUserActivity.class);
                startActivity(registerIntent);
                break;
        }
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
        params.put("user_type", "2");
        params.put("login_name", phone);
        params.put("password", pwd);
        params.put("platform", "android");
        params.put("device_token", PushAgent.getInstance(this).getRegistrationId());
        return params;
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
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
            configEntity.username = userEntity.entp_name.trim();
            configEntity.usertype = userEntity.user_type_name;
            configEntity.key = userEntity.key;
            configEntity.entpId = userEntity.entpId.trim();
            configEntity.isLogin = true;
            configEntity.urlList = userEntity.urlList;
            ConfigUtil.saveConfig(getApplicationContext(), configEntity);
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
        }
        appManager.finishOtherActivity();
    }


    public void onBackPressed() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editPhone.getWindowToken(), 0);
        super.onBackPressed();
    }

}
