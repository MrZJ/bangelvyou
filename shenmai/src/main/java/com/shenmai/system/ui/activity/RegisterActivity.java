package com.shenmai.system.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.shenmai.system.R;
import com.shenmai.system.api.CodeApi;
import com.shenmai.system.api.RegisterApi;
import com.shenmai.system.entity.CodeEntity;
import com.shenmai.system.entity.ConfigEntity;
import com.shenmai.system.entity.FieldErrors;
import com.shenmai.system.entity.UserEntity;
import com.shenmai.system.utlis.CheckMobile;
import com.shenmai.system.utlis.ConfigUtil;
import com.shenmai.system.utlis.StringUtil;
import com.shenmai.system.utlis.TopClickUtil;

import java.util.HashMap;

/**
 * 注册界面
 **/
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText register_phone;
    private EditText input_code;
    private Button btn_get_code;
    private EditText register_password, hoster;
    private TextView tv_register_agreement;
    private TextView tv_register_ty;
    private Button btn_register;
    private CheckBox check_agree;
    private String phoneTemp;//获取验证码的手机号
    private boolean isGetCode;//是否获得验证码
    private String code;//验证码
    private final int DELAY = 60;
    private final int MSG_REGET = 100;
    private int count = DELAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        intiView();
        initListener();
    }

    private void intiView() {
        initTopView();
        setLeftBackButton();
        setTitle("注册");
        setTopRightTitle("登录", TopClickUtil.TOP_LOG);
        register_phone = (EditText) findViewById(R.id.register_phone);
        input_code = (EditText) findViewById(R.id.input_code);
        btn_get_code = (Button) findViewById(R.id.btn_get_code);
        register_password = (EditText) findViewById(R.id.register_password);
        hoster = (EditText) findViewById(R.id.edt_register_host);
        tv_register_agreement = (TextView) findViewById(R.id.tv_register_agreement);
        tv_register_ty = (TextView) findViewById(R.id.tv_register_ty);
        btn_register = (Button) findViewById(R.id.btn_register);
        check_agree = (CheckBox) findViewById(R.id.check_agree);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        btn_get_code.setOnClickListener(this);
        tv_register_agreement.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register://注册
                if (checkString()) {
                    register();
                }
                break;
            case R.id.tv_register_agreement://用户协议
                startActivity(new Intent(this,ProtWebActivity.class));
                break;
            case R.id.btn_get_code://获得验证码
                String phoneStr = register_phone.getText().toString().trim();
                if (phoneStr != null && phoneStr.length() > 0) {
                    if (!CheckMobile.isMobileNO(phoneStr)) {
                        Toast.makeText(this, "您输入的手机号码不正确", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    regetDelay();
                    getVerifyCode(phoneStr);
                } else {
                    Toast.makeText(this, "请先输入手机号", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void register() {
        httpPostRequest(RegisterApi.getRegisterUrl(),
                getRegisterRequestParams(), RegisterApi.API_REGISTER);
    }

    private boolean checkString() {
        if (!check_agree.isChecked()) {
            Toast.makeText(this, "请勾选同意协议", Toast.LENGTH_SHORT).show();
            return false;
        }
        String phoneStr = register_phone.getText().toString().trim();
        if (StringUtil.isEmpty(phoneStr)) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isGetCode) {
            Toast.makeText(this, "请先获取验证码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtil.isEmpty(input_code.getText().toString())) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtil.isEmpty(register_password.getText().toString())) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!phoneStr.equals(phoneTemp)) {
            Toast.makeText(this, "手机号码与验证码不匹配", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!code.equals(input_code.getText().toString())) {
            Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 倒计时按钮
     */
    private void regetDelay() {
        btn_get_code.setClickable(false);
        mHandler.sendEmptyMessageDelayed(MSG_REGET, 1000);
        btn_get_code.setText("重新获取验证码(" + count + ")");
        btn_get_code.setBackgroundResource(R.drawable.bg_button_gray);
    }

    /**
     * 倒计时结束重置按钮
     */
    private void reset() {
        count = DELAY;
        btn_get_code.setClickable(true);
        btn_get_code.setText("获取验证码");
        btn_get_code.setTextColor(Color.parseColor("#ffffff"));
        btn_get_code.setBackgroundResource(R.color.top_bar_color);
        mHandler.removeMessages(MSG_REGET);
    }

    /**
     * 请求获取验证码
     *
     * @param phone
     */
    private void getVerifyCode(String phone) {
        phoneTemp = phone;
        httpPostRequest(CodeApi.getCodeUrl(),
                getRequestParams(phone), CodeApi.API_GET_CODE);
    }

    /**
     * 获取注册参数
     *
     * @return
     */
    private HashMap<String, String> getRegisterRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        /*params.put("username", phoneTemp);*/
        params.put("mobile", phoneTemp);
        params.put("password", register_password.getText().toString());
        if(!StringUtil.isEmpty(hoster.getText().toString())){
            params.put("ym_id", hoster.getText().toString());
        }
        /*params.put("password_confirm", register_password.getText().toString());*/
        /*params.put("client", "android");
        params.put("device_tokens", ConfigUtil.phoneIMEI);*/
        return params;
    }

    /**
     * 获取验证码参数
     *
     * @return
     */
    private HashMap<String, String> getRequestParams(String phoneStr) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", phoneStr);
        params.put("type", "1");
        return params;
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case CodeApi.API_GET_CODE:
                codeHander(json);
                break;
            case RegisterApi.API_REGISTER:
                Toast.makeText(this, "注册成功,请登录", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
//                registerHander(json);
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        btn_get_code.setClickable(true);
    }

    /**
     * 处理验证码信息
     *
     * @param json
     */
    private void codeHander(String json) {
        CodeEntity codeEntity = JSON.parseObject(json, CodeEntity.class);
        if (codeEntity != null) {
            code = codeEntity.veriCode;
            isGetCode = true;
        }
    }

    /**
     * 处理注册信息
     *
     * @param json
     */
    private void registerHander(String json) {
        UserEntity userEntity = JSON.parseObject(json, UserEntity.class);
        if (userEntity != null) {
            ConfigEntity configEntity = new ConfigEntity();
            configEntity.username = userEntity.username;
            configEntity.key = userEntity.key;
            ConfigUtil.saveConfig(this, configEntity);
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            appManager.finishOtherActivity();
        }
    }

    /**
     * 倒计时按钮
     */
    protected void processMessage(Message msg) {
        super.processMessage(msg);
        switch (msg.what) {
            case MSG_REGET: {
                if (count > 0) {
                    count--;
                }
                if (count > 0) {
                    btn_get_code.setText("重新获取验证码(" + count + ")");
                    mHandler.sendEmptyMessageDelayed(MSG_REGET, 1000);
                } else {
                    reset();
                }
                break;
            }

        }
    }
}
