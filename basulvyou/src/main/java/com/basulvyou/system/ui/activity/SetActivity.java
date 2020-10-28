package com.basulvyou.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.basulvyou.system.R;
import com.basulvyou.system.entity.FieldErrors;
import com.basulvyou.system.util.CacheImgUtil;
import com.basulvyou.system.util.ConfigUtil;
import com.basulvyou.system.util.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pgyersdk.update.PgyUpdateManager;

/**
 * 设置界面
 */
public class SetActivity extends BaseActivity implements View.OnClickListener{

    private View accountSec, wipeCache, checkUpdate, loginOut;
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
        accountSec = findViewById(R.id.rel_set_accountsec);
        wipeCache = findViewById(R.id.rel_set_wipecache);
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
        accountSec.setOnClickListener(this);
        wipeCache.setOnClickListener(this);
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
            case R.id.rel_set_accountsec:
                Intent accountSecIntent = new Intent(this,RegisterActivity.class);
                startActivity(accountSecIntent);
                break;
            case R.id.rel_set_wipecache:
                ToastUtil.showToast("正在清除...", this);
                ImageLoader.getInstance().clearDiscCache();
                ImageLoader.getInstance().clearMemoryCache();
                CacheImgUtil.wipeCache();
                ToastUtil.setMessage("清除完成");
                ToastUtil.dismissDelay(ToastUtil.DELAY_SHORT);
                break;
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
