package com.shenmai.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
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
 * 忘记密码
 * 获取手机号码验证手机
 */
public class GetTestCodeActivity extends BaseActivity implements View.OnClickListener{

    private EditText editPhone;
    private Button getCode;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_test_code);
        initView();
        initListener();
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("获取验证码");
        editPhone = (EditText) findViewById(R.id.edt_get_testcode_phone);
        getCode = (Button) findViewById(R.id.btn_get_testcode_getcode);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        getCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_get_testcode_getcode:
                phone = editPhone.getText().toString();
                if(!StringUtil.isEmpty(phone)){
                    getVerifyCode();
                } else {
                    Toast.makeText(this,"手机号码不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void getVerifyCode() {
        httpPostRequest(CodeApi.getCodeUrl(), getRequestParams(), CodeApi.API_GET_CODE);
    }

    private HashMap<String, String> getRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        params.put("type", "2");
        return params;
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){
            case CodeApi.API_GET_CODE:
                codeHander(json);
                break;
        }
    }

    private void codeHander(String json) {
        CodeEntity codeEntity = JSON.parseObject(json, CodeEntity.class);
        if (codeEntity != null) {
            String code = codeEntity.veriCode;
            Intent retrPasswordIntent = new Intent(this,RetrPasswordActivity.class);
            retrPasswordIntent.putExtra("code",code);
            retrPasswordIntent.putExtra("phone",phone);
            startActivity(retrPasswordIntent);
            finish();
        }
    }
}
