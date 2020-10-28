package com.yishangshuma.bangelvyou.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.umeng.message.UmengRegistrar;
import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.api.RegisterApi;
import com.yishangshuma.bangelvyou.entity.ConfigEntity;
import com.yishangshuma.bangelvyou.entity.UserEntity;
import com.yishangshuma.bangelvyou.util.ConfigUtil;
import com.yishangshuma.bangelvyou.util.TopClickUtil;

import java.util.HashMap;

/**
 * 设置密码界面
 */
public class SetPasswordActivity extends BaseActivity implements View.OnClickListener{

    private EditText edtPwd, edtPwdAgin;
    private CheckBox showPwd;
    private String username, type;//type:1表示注册， 2表示忘记密码
    private Button commitPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        type = getIntent().getExtras().getString("type");
        username = getIntent().getExtras().getString("username");
        intiView();
        initListener();
    }

    private void intiView(){
        initTopView();
        setLeftBackButton();
        setTitle("设置密码");
        setTopRightTitle("登录", TopClickUtil.TOP_LOG);
        edtPwd = (EditText) findViewById(R.id.edt_pwd);
        edtPwdAgin = (EditText) findViewById(R.id.edt_pwd_agin);
        showPwd = (CheckBox) findViewById(R.id.check_show_pwd);
        commitPwd = (Button) findViewById(R.id.btn_complete);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        showPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edtPwd.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
                    edtPwdAgin.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
                } else {
                    edtPwd.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());
                    edtPwdAgin.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());
                }
            }
        });
        commitPwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_complete:
                String pwd = edtPwd.getText().toString().trim();
                String pwdAgain = edtPwdAgin.getText().toString().trim();
                if(!pwd.equals(pwdAgain)){
                    Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pwd != null && pwd.length() > 0){
                    if(pwd.length() > 5 || pwd.length() < 21){
                        register(pwd);
                    } else {
                        Toast.makeText(this, "密码输入长度不正确，请重新输入", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请先输入密码", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void register(String pwd){
        if("2".equals(type)){
            httpPostRequest(RegisterApi.getUpdatePwdUrl(),
                    getModifyPwdRequestParams(pwd), RegisterApi.API_UPDATE_PWD);
        } else {
            httpPostRequest(RegisterApi.getRegisterUrl(),
                    getRequestParams(pwd), RegisterApi.API_REGISTER);
        }
    }

    /**
     * 获取注册参数
     *
     * @return
     */
    private HashMap<String,String> getRequestParams(String pwd){
        HashMap<String,String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", pwd);
//        params.put("password_confirm", pwd);
        params.put("mobile", username);
        params.put("apply_code", "android");
//        params.put("client", "android");
        String device_token = UmengRegistrar.getRegistrationId(this);
//        params.put("device_tokens", device_token);
        return params;
    }

    /**
     * 获取修改密码参数
     *
     * @return
     */
    private HashMap<String,String> getModifyPwdRequestParams(String pwd){
        HashMap<String,String> params = new HashMap<>();
        params.put("mobile", username);
        params.put("newPassword", pwd);
//       params.put("code", codeEntity.veriCode);
        return params;
    }

    @Override
    public void httpResponse(String json, int action){
        super.httpResponse(json, action);
        switch (action)
        {
            case RegisterApi.API_REGISTER:
                RegisterHander(json);
                break;
            case RegisterApi.API_UPDATE_PWD:
                updatePwdHander(json);
                break;
            default:
                break;
        }
    }

    /**
     * 处理注册信息
     * @param json
     */
    private void RegisterHander(String json){
        UserEntity userEntity = JSON.parseObject(json, UserEntity.class);
        if(userEntity != null){
            ConfigEntity configEntity = new ConfigEntity();
            configEntity.username = userEntity.username;
            configEntity.key = userEntity.key;
            ConfigUtil.saveConfig(SetPasswordActivity.this, configEntity);
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
        }
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    /**
     * 修改密码
     */
    private void updatePwdHander(String json){
        String flag = JSON.parseObject(json).getString("flag");
        if(null !=flag && "0".equals(flag)){
            Toast.makeText(this, "设置密码成功", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }else{
            Toast.makeText(this, "设置密码失败", Toast.LENGTH_SHORT).show();
        }
    }
}
