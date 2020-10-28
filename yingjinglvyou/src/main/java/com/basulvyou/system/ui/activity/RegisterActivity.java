package com.basulvyou.system.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.api.CodeApi;
import com.basulvyou.system.entity.CodeEntity;
import com.basulvyou.system.entity.FieldErrors;
import com.basulvyou.system.util.ConfigUtil;
import com.basulvyou.system.util.StringUtil;
import com.basulvyou.system.util.TopClickUtil;
import com.basulvyou.system.util.checkMobile;

import java.util.HashMap;

/**
 * 注册界面
 **/
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText userPhone;
    private Button getCode;
    private String whereCome;//判断是注册还是找回密码
    private TextView tips;
    private String phoneTemp;
    private String phoneIMEI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        whereCome = getIntent().getStringExtra("forget");
        intiView();
        initListener();
    }

    private void intiView() {
        initTopView();
        setLeftBackButton();
        setTopRightTitle("登录", TopClickUtil.TOP_LOG);
        userPhone = (EditText) findViewById(R.id.register_phone);
        getCode = (Button) findViewById(R.id.register_getcode);
        tips = (TextView) findViewById(R.id.tv_register_tip);
        if (null != whereCome && "forget".equals(whereCome)) {
            setTitle("找回密码");
            tips.setVisibility(View.GONE);
        } else {
            setTitle("注册");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        phoneIMEI = ConfigUtil.getPhoneIMEI(this);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        getCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_getcode:
                String phoneStr = userPhone.getText().toString().trim();
                if (!StringUtil.isEmpty(phoneStr)) {
                    if (!checkMobile.isMobileNO(phoneStr)) {
                        Toast.makeText(this, "您输入的手机号码不正确", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    getCode.setClickable(false);
                    getVerifyCode(phoneStr);
                } else {
                    Toast.makeText(this, "请先输入手机号", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
        params.put("unique_id", phoneIMEI);
        return params;
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case CodeApi.API_GET_CODE:
                codeHander(json);
                break;
            default:
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        getCode.setClickable(true);
    }

    /**
     * 处理验证码信息
     *
     * @param json
     */
    private void codeHander(String json) {
        getCode.setClickable(true);
        CodeEntity codeEntity = JSON.parseObject(json, CodeEntity.class);
        if (codeEntity != null) {
            codeEntity.username = phoneTemp;
            Intent intent = new Intent(this, InputRegisterCodeActivity.class);
            if (null != whereCome && "forget".equals(whereCome)) {
                intent.putExtra("type", "2");
            }
            intent.putExtra("phone", phoneTemp);
            intent.putExtra("code", codeEntity);
            startActivity(intent);
        }
    }
}
