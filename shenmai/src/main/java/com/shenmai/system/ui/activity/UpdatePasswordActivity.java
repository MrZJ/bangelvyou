package com.shenmai.system.ui.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.shenmai.system.R;
import com.shenmai.system.api.CodeApi;
import com.shenmai.system.utlis.StringUtil;
import com.shenmai.system.utlis.TopClickUtil;

import java.util.HashMap;

/**
 * 修改密码
 */
public class UpdatePasswordActivity extends BaseActivity {

    private EditText oldPassw, newPassw, newPasswA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        initView();
        initListener();
    }

    private void initView(){
        initTopView();
        setTitle("修改密码");
        setLeftBackButton();
        setTopRightTitle("保存", TopClickUtil.TOP_UPDATE_PASSWORD);
        oldPassw = (EditText) findViewById(R.id.edt_update_password_oldpassword);
        newPassw = (EditText) findViewById(R.id.edt_update_password_newpassword);
        newPasswA = (EditText) findViewById(R.id.edt_update_password_newpasswordagin);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
    }

    /**
     * 修改密码
     */
    public void updatePassword(){
        if(checkPassword()){
            resetPassword();
        }
    }

    private boolean checkPassword() {
        if (StringUtil.isEmpty(oldPassw.getText().toString().trim())) {
            Toast.makeText(this, "请输入原密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtil.isEmpty(newPassw.getText().toString()) || StringUtil.isEmpty(newPasswA.getText().toString())) {
            Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!(newPassw.getText().toString().equals(newPasswA.getText().toString()))) {
            Toast.makeText(this, "两次输入的新密码不一致", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void resetPassword(){
        httpPostRequest(CodeApi.getModifyPwdUrl(), getRequestParams(),CodeApi.API_MODIFY_PWD);
    }

    private HashMap<String, String> getRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("key", configEntity.key);
        params.put("oldPassword", oldPassw.getText().toString());
        params.put("newPassword", newPassw.getText().toString());
        return params;
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){
            case CodeApi.API_MODIFY_PWD:
                Toast.makeText(this,"修改密码成功",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
