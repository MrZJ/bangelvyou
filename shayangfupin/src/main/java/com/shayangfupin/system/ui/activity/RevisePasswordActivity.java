package com.shayangfupin.system.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shayangfupin.system.R;
import com.shayangfupin.system.api.LoginApi;
import com.shayangfupin.system.entity.FieldErrors;
import com.shayangfupin.system.utlis.StringUtil;

import java.util.HashMap;

/**
 * 修改密码
 */
public class RevisePasswordActivity extends BaseActivity implements View.OnClickListener{

    private EditText oldPassW, newPassW;
    private Button comintBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_password);
        initView();
        initListener();
    }

    private void initView(){
        initTopView();
        setTitle("修改密码");
        setLeftBackButton();
        oldPassW = (EditText) findViewById(R.id.edt_revise_password_old);
        newPassW = (EditText) findViewById(R.id.edt_revise_password_new);
        comintBtn = (Button) findViewById(R.id.btn_revise_password_commint);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        comintBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBtnClickable();
    }

    /**
     * 设置按钮可以点击
     */
    private void setBtnClickable() {
        comintBtn.setClickable(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_revise_password_commint:
                String oldPassWord = oldPassW.getText().toString().trim();
                String newPassWord = newPassW.getText().toString().trim();
                if (StringUtil.isEmpty(oldPassWord)) {
                    Toast.makeText(this, "请先输入原密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtil.isEmpty(newPassWord)) {
                    Toast.makeText(this, "请先输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                comintBtn.setClickable(false);
                revisePassW(oldPassWord,newPassWord);
                break;
        }
    }

    /**
     * 修改密码
     * @param oldPass
     * @param newPass
     */
    private void revisePassW(String oldPass,String newPass){
        httpPostRequest(LoginApi.getRevisePassWUrl(), getRequestParams(oldPass, newPass), LoginApi.API_REVISE_PASSWORD);
    }

    /**
     * 设置参数
     * @return
     */
    private HashMap<String,String> getRequestParams(String oldPass, String newPass){
        HashMap<String,String> params = new HashMap<>();
        params.put("key",configEntity.key);
        params.put("old_password",oldPass);
        params.put("new_password", newPass);
        return params;
    }

    @Override
    public void httpResponse(String json, int action){
        super.httpResponse(json, action);
        switch (action) {
            case LoginApi.API_REVISE_PASSWORD:
                Toast.makeText(this,"修改成功!",Toast.LENGTH_SHORT).show();
                this.finish();
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

}
