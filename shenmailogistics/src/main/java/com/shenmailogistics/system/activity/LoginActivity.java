package com.shenmailogistics.system.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.shenmailogistics.system.R;
import com.shenmailogistics.system.api.LoginApi;
import com.shenmailogistics.system.bean.ConfigEntity;
import com.shenmailogistics.system.bean.UserEntity;
import com.shenmailogistics.system.utils.ConfigUtil;
import com.shenmailogistics.system.utils.StringUtil;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity{

    @Bind(R.id.edit_login_phone)
    EditText editPhone;
    @Bind(R.id.edit_login_pwd)
    EditText editPwd;
    @Bind(R.id.btn_login)
    Button btnLogin;
    private String phoneStr;
    private String pwdStr;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        initTopView();
        setLeftBackButton(true);
        setTopTitle("登录");
        initTopListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewCanClick();
    }

    @OnClick(R.id.btn_login)
    void onLogin(){
        if(checkLoginData()){
            btnLogin.setClickable(false);
            login(phoneStr, pwdStr);
        }
    }

    private boolean checkLoginData(){
        phoneStr = editPhone.getText().toString().trim();
        pwdStr = editPwd.getText().toString().trim();
        if (StringUtil.isEmpty(phoneStr)) {
            Toast.makeText(this, "请先输入用户名或手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtil.isEmpty(pwdStr)) {
            Toast.makeText(this, "请先输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 登录
     * @param phone
     */
    private void login(String phone, String pwd){
        httpPostRequest(this,LoginApi.getLoginUrl(),
                getRequestParams(phone, pwd), LoginApi.API_LOGIN);
    }

    /**
     * 获取登录参数
     *
     * @return
     */
    private HashMap<String,String> getRequestParams(String phone, String pwd){
        HashMap<String,String> params = new HashMap<>();
        params.put("app_user_name",phone);
        params.put("app_password", pwd);
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
    protected void viewCanClick() {
        super.viewCanClick();
        btnLogin.setClickable(true);
    }


    /**
     * 处理登录信息
     * @param json
     */
    private void LoginHander(String json){
        viewCanClick();
        Log.e("ss",json);
        UserEntity userEntity = JSON.parseObject(json, UserEntity.class);
        if(userEntity != null){
            ConfigEntity configEntity = new ConfigEntity();
            configEntity.username = userEntity.user_name;
            configEntity.usertype = userEntity.user_type;
            configEntity.key = userEntity.user_id;
//            configEntity.isLogin = true;
            ConfigUtil.saveConfig(getApplicationContext(), configEntity);
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
        }
        startActivity(new Intent(this,MainActivity.class));
        appManager.finishOtherActivity();
    }


    public void onBackPressed() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editPhone.getWindowToken(), 0);
        super.onBackPressed();
    }

}
