package com.shenmai.system.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.shenmai.system.R;
import com.shenmai.system.api.CodeApi;
import com.shenmai.system.api.RegisterApi;
import com.shenmai.system.entity.CodeEntity;
import com.shenmai.system.utlis.CheckMobile;
import com.shenmai.system.utlis.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 绑定手机号
 */
public class BingPhoneActivity extends BaseActivity implements View.OnClickListener{

    private EditText userPhone, edtCode;
    private Button getCode, commit;
    private final int DELAY = 60;
    private final int MSG_REGET = 100;
    private int count = DELAY;
    private String phoneTemp;//获取验证码的手机号
    private boolean isGetCode;//是否获得验证码
    private String code;//验证码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bing_phone);
        initView();
        initListener();
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("绑定手机号");
        userPhone = (EditText) findViewById(R.id.edt_bing_userphone);
        edtCode = (EditText) findViewById(R.id.edt_bing_code);
        getCode = (Button) findViewById(R.id.btn_bing_getcode);
        commit = (Button) findViewById(R.id.btn_bing_phone);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        getCode.setOnClickListener(this);
        commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_bing_getcode://获取验证码
                String phoneStr = userPhone.getText().toString().trim();
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
            case R.id.btn_bing_phone://绑定手机号
                if (checkString()) {
                    saveCommit();
                }
                break;
        }
    }

    /**
     * 保存并提交
     */
    private void saveCommit() {
        httpPostRequest(RegisterApi.getBingPhoneUrl(), getBingPhoneParams(), RegisterApi.API_BING_PHONE);
    }

    public Map<String,String> getBingPhoneParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", phoneTemp);
        params.put("key", configEntity.key);
        return params;
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
     * 获取验证码参数
     *
     * @return
     */
    private HashMap<String, String> getRequestParams(String phoneStr) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", phoneStr);
        params.put("type", "3");
        return params;
    }

    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case CodeApi.API_GET_CODE:
                codeHander(json);
                break;
            case RegisterApi.API_BING_PHONE:
                Toast.makeText(this,"手机号绑定成功",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
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

    private boolean checkString() {
        String phoneStr = userPhone.getText().toString().trim();
        if (StringUtil.isEmpty(phoneStr)) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isGetCode) {
            Toast.makeText(this, "请先获取验证码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtil.isEmpty(edtCode.getText().toString())) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!phoneStr.equals(phoneTemp)) {
            Toast.makeText(this, "手机号码与验证码不匹配", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!code.equals(edtCode.getText().toString())) {
            Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 倒计时按钮
     */
    private void regetDelay() {
        getCode.setClickable(false);
        mHandler.sendEmptyMessageDelayed(MSG_REGET, 1000);
        getCode.setText("重新获取验证码(" + count + ")");
        getCode.setBackgroundResource(R.drawable.bg_button_gray);
    }

    /**
     * 倒计时结束重置按钮
     */
    private void reset() {
        count = DELAY;
        getCode.setClickable(true);
        getCode.setText("获取验证码");
        getCode.setTextColor(Color.parseColor("#ffffff"));
        getCode.setBackgroundResource(R.color.top_bar_color);
        mHandler.removeMessages(MSG_REGET);
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
                    getCode.setText("重新获取验证码(" + count + ")");
                    mHandler.sendEmptyMessageDelayed(MSG_REGET, 1000);
                } else {
                    reset();
                }
                break;
            }
        }
    }
}
