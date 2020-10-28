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
import com.shenmai.system.entity.CodeEntity;
import com.shenmai.system.utlis.StringUtil;

import java.util.HashMap;

/**
 * 找回密码界面
 */
public class RetrPasswordActivity extends BaseActivity implements View.OnClickListener{

    private String phone;
    private String code;
    private EditText edtCode, edtNewPassword;
    private Button getCode, commit;
    private final int DELAY = 60;
    private final int MSG_REGET = 100;
    private int count = DELAY;
    private String newPass;//新密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retr_password);
        phone = getIntent().getStringExtra("phone");
        code = getIntent().getStringExtra("code");
        initView();
        initListener();
        regetDelay();
    }

    private void initView(){
        initTopView();
        setTitle("设置新密码");
        setLeftBackButton();
        edtCode = (EditText) findViewById(R.id.edt_retr_password_inputcode);
        edtNewPassword = (EditText) findViewById(R.id.edt_retr_password_newpassword);
        getCode = (Button) findViewById(R.id.btn_retr_password_getcode);
        commit = (Button) findViewById(R.id.btn_retr_password_commit);
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
            case R.id.btn_retr_password_getcode:
                getVerifyCode();
                break;
            case R.id.btn_retr_password_commit:
                String inputCode = edtCode.getText().toString();
                newPass = edtNewPassword.getText().toString();
                if (StringUtil.isEmpty(inputCode)) {
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (StringUtil.isEmpty(newPass)) {
                    Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (!inputCode.equals(code)) {
                    Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
                    break;
                }
                setNewPassword();
                break;
        }
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

    private void getVerifyCode() {
        regetDelay();
        httpPostRequest(CodeApi.getCodeUrl(), getRequestParams(), CodeApi.API_GET_CODE);
    }

    private void setNewPassword(){
        httpPostRequest(CodeApi.setNewPassword(),getNewPasswordParams(),CodeApi.API_MODIFY_RETR_PASSWORD);
    }

    private HashMap<String, String> getRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        params.put("type", "2");
        return params;
    }

    private HashMap<String, String> getNewPasswordParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        params.put("password", newPass);
        return params;
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){
            case CodeApi.API_GET_CODE:
                codeHander(json);
                break;
            case CodeApi.API_MODIFY_RETR_PASSWORD:
                Toast.makeText(this,"新密码设置成功",Toast.LENGTH_SHORT).show();
                appManager.finishLoginOtherActivity();
                break;
        }
    }

    private void codeHander(String json) {
        CodeEntity codeEntity = JSON.parseObject(json, CodeEntity.class);
        if (codeEntity != null) {
            code = codeEntity.veriCode;
        }
    }

}
