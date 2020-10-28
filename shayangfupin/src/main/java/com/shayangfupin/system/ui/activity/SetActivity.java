package com.shayangfupin.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pgyersdk.update.PgyUpdateManager;
import com.shayangfupin.system.R;
import com.shayangfupin.system.entity.FieldErrors;
import com.shayangfupin.system.utlis.ConfigUtil;
import com.shayangfupin.system.utlis.ToastUtil;

/**
 * 设置界面
 */
public class SetActivity extends BaseActivity implements View.OnClickListener{

    private View checkUpdate, loginOut;
    private TextView appVersion;
    private TextView tvLoginState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        configEntity = ConfigUtil.loadConfig(this);
        initView();
        initListener();
    }

    private void initView(){
        initTopView();
        setTitle("设置");
        setLeftBackButton();
        checkUpdate = findViewById(R.id.rel_set_checkupdate);
        appVersion = (TextView) findViewById(R.id.tv_set_appversion);
        loginOut = findViewById(R.id.rel_set_loginout);
        tvLoginState = (TextView) findViewById(R.id.tv_set_login_or_out);
        if (configEntity.isLogin) {
            tvLoginState.setText("退出");
        } else {
            tvLoginState.setText("登录");
        }
        appVersion.setText("当前版本号:" + getAppVersionName());
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        checkUpdate.setOnClickListener(this);
        loginOut.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PgyUpdateManager.unregister();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rel_set_checkupdate:
                if (ConfigUtil.IS_DOWNLOAD) {
                    Toast.makeText(getApplicationContext(), "最新安装包正在下载...", Toast.LENGTH_SHORT).show();
                } else {
                    checkUpdate.setClickable(false);
                    isHandUpdateApk = true;
                    isUpdateApk();
                }
                break;
            case R.id.rel_set_loginout:
                if (configEntity.isLogin) {
                    configEntity.isLogin = false;
                    configEntity.key = "";
                    configEntity.usertype = "";
                    configEntity.username = "";
                    ConfigUtil.saveConfig(this, configEntity);
                    ToastUtil.showToast("退出账户成功", this, ToastUtil.DELAY_SHORT);
                    tvLoginState.setText("登录");
                    configEntity = ConfigUtil.loadConfig(this);
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                }
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        setBtnUpdateClickable();
    }

    /**
     * 获取软件当前版本信息
     * @return
     */
    private String getAppVersionName() {
        try {
            String pkName = this.getPackageName();
            String versionName = this.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            return versionName;
        } catch (Exception e) {

        }
        return "1.0";
    }

    /**
     * 设置更新按钮可以点击
     */
    public void setBtnUpdateClickable() {
        checkUpdate.setClickable(true);
    }

}
