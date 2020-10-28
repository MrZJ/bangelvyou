package com.shishoureport.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.shishoureport.system.R;
import com.shishoureport.system.UIApplication;
import com.shishoureport.system.entity.FieldErrors;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.request.LoginRequest;
import com.shishoureport.system.utils.CommUtil;
import com.shishoureport.system.utils.MySharepreference;
import com.shishoureport.system.utils.StringUtil;


/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    EditText editPhone;
    EditText editPwd;
    Button btnLogin;
    private String phoneStr;
    private String pwdStr;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        editPhone = (EditText) findViewById(R.id.edit_login_phone);
        editPwd = (EditText) findViewById(R.id.edit_login_pwd);
        TextView top_title = (TextView) findViewById(R.id.top_title);
        top_title.setText("登录");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void onLogin() {
        if (checkLoginData()) {
            login(phoneStr, pwdStr);
        }
    }

    private boolean checkLoginData() {
        phoneStr = editPhone.getText().toString().trim();
        pwdStr = editPwd.getText().toString().trim();
        if (StringUtil.isEmpty(phoneStr)) {
            Toast.makeText(this, "请先输入用户名", Toast.LENGTH_SHORT).show();
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
     *
     * @param phone
     */
    private void login(String phone, String pwd) {
        String dev_token = UIApplication.getInstance().getUmengDeviceToken();
        LoginRequest request = new LoginRequest(phone, pwd, CommUtil.getDevToken(this), dev_token);
        httpPostRequest(request.getRequestUrl(), request.getRequestParams(), LoginRequest.LOGIN_REQUEST);
    }


    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case LoginRequest.LOGIN_REQUEST:
                LoginHander(json);
                break;
            default:
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        btnLogin.setClickable(true);
    }

    /**
     * 处理登录信息
     *
     * @param json
     */
    private void LoginHander(String json) {
        User user = JSONObject.parseObject(json, User.class);
        if (user != null && !StringUtil.isEmpty(user.id)) {
            showToast("登录成功");
            MySharepreference.getInstance(this).savaUser(json);
            MainActivity.startActivity(this);
            finish();
        } else {
            showToast("登录失败，请重试 ");
        }
    }


    public void onBackPressed() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editPhone.getWindowToken(), 0);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                onLogin();
                break;
        }
    }
}
