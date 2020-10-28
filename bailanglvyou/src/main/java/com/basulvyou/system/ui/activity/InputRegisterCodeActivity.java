package com.basulvyou.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.api.CodeApi;
import com.basulvyou.system.entity.CodeEntity;
import com.basulvyou.system.util.StringUtil;
import com.basulvyou.system.util.TopClickUtil;

import java.util.HashMap;

/**输入验证码界面**/
public class InputRegisterCodeActivity extends BaseActivity implements View.OnClickListener{

    private TextView userPhone, tips;
    private Button next;
    private Button getCodeAgin;
    private final int DELAY = 60;
    private final int MSG_REGET = 100;
    private int count = DELAY;
    private String phoneStr, type;//type:1表示注册， 2表示忘记密码
    private CodeEntity codeEntity;
    private EditText inputCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_register_code);
        type = getIntent().getExtras().getString("type");
        phoneStr = getIntent().getExtras().getString("phone");
        codeEntity = (CodeEntity) getIntent().getExtras().getSerializable("code");
        intiView();
        initListener();
        regetDelay();
    }

    private void intiView(){
        initTopView();
        setLeftBackButton();
        setTitle("注册新用户");
        setTopRightTitle("登录", TopClickUtil.TOP_LOG);
        userPhone = (TextView) findViewById(R.id.tv_user_phone);
        inputCode = (EditText) findViewById(R.id.input_code);
        getCodeAgin = (Button) findViewById(R.id.btn_get_code);
        tips = (TextView) findViewById(R.id.tv_input_code_tip);
        next = (Button) findViewById(R.id.btn_next);
        userPhone.setText(phoneStr);
        if("2".equals(type)){
            tips.setVisibility(View.GONE);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        next.setOnClickListener(this);
        getCodeAgin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputCode.getWindowToken(), 0);
        switch (v.getId()){
            case R.id.btn_next:
                String codeStr = inputCode.getText().toString().trim();
                if(!StringUtil.isEmpty(codeStr)){
                    //检查输入验证码是否正确
                    if(codeEntity != null && codeStr.equals(codeEntity.veriCode)){
                        setPassword();
                    } else {
                        Toast.makeText(this, "验证码输入错误", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请先输入验证码", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_get_code://再次请求验证码
                getCode();
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getCode(){
        regetDelay();
        httpPostRequest(CodeApi.getCodeUrl(), getRequestParams(), CodeApi.API_GET_CODE);
    }

    /**
     * 获取验证码参数
     *
     * @return
     */
    private HashMap<String,String> getRequestParams(){
        HashMap<String,String> params = new HashMap<>();
        params.put("mobile", phoneStr);
        return params;
    }

    @Override
    public void httpResponse(String json, int action){
        super.httpResponse(json, action);
        switch (action)
        {
            case CodeApi.API_GET_CODE:
                codeHander(json);
                break;
            default:
                break;
        }
    }

    /**
     * 处理首页信息
     * @param json
     */
    private void codeHander(String json){
        codeEntity = JSON.parseObject(json, CodeEntity.class);
        //reset();
    }

    /**
     * goto设置密码界面
     */
    private void setPassword(){
        Intent intent = new Intent(InputRegisterCodeActivity.this, SetPasswordActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("username", phoneStr);
        startActivity(intent);
    }

    /**
     * 倒计时按钮
     */
    private void regetDelay() {
        getCodeAgin.setClickable(false);
        mHandler.sendEmptyMessageDelayed(MSG_REGET, 1000);
        getCodeAgin.setText("重新获取验证码(" + count + ")");
        getCodeAgin.setBackgroundResource(R.mipmap.yzm_code_bg);
    }

    /**
     * 倒计时结束重置按钮
     */
    private void reset() {
        count = DELAY;
        getCodeAgin.setClickable(true);
        getCodeAgin.setText("获取验证码");
        getCodeAgin.setTextColor(Color.parseColor("#ffffff"));
        getCodeAgin.setBackgroundResource(R.mipmap.login_bg);
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
                    getCodeAgin.setText("重新获取验证码(" + count + ")");
                    mHandler.sendEmptyMessageDelayed(MSG_REGET, 1000);
                } else {
                    reset();
                }
                break;
            }

        }
    }
}
