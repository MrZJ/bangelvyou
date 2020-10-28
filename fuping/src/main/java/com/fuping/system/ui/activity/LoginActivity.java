package com.fuping.system.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.fuping.system.R;
import com.fuping.system.api.LoginApi;
import com.fuping.system.entity.ConfigEntity;
import com.fuping.system.entity.FieldErrors;
import com.fuping.system.entity.UserEntity;
import com.fuping.system.utils.ConfigUtil;
import com.fuping.system.utils.StringUtil;

import java.util.HashMap;


/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText editPhone,editPwd;
    private Button btnLogin;

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
        /*params.put("client", "android");
        String device_token = ConfigUtil.phoneIMEI;
        params.put("device_tokens", device_token);*/
        return params;
    }

    @Override
    public void httpResponse(String json, int action){
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
     * @param json
     */
    private void LoginHander(String json){
        setBtnClickable();
        UserEntity userEntity = JSON.parseObject(json, UserEntity.class);
        if(userEntity != null){
            ConfigEntity configEntity = new ConfigEntity();
            configEntity.username = userEntity.user_name;
            configEntity.usertype = userEntity.user_type;
            configEntity.key = userEntity.key;
            configEntity.isLogin = true;
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
