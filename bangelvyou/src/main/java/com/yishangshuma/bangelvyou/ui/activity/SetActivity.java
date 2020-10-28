package com.yishangshuma.bangelvyou.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.autoupdatesdk.AppUpdateInfo;
import com.baidu.autoupdatesdk.AppUpdateInfoForInstall;
import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.CPCheckUpdateCallback;
import com.baidu.autoupdatesdk.UICheckUpdateCallback;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.util.CacheImgUtil;
import com.yishangshuma.bangelvyou.util.ConfigUtil;
import com.yishangshuma.bangelvyou.util.ToastUtil;

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
        if(configEntity.isLogin){
            tvLoginState.setText("退出");
        }else{
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
                BDAutoUpdateSDK.cpUpdateCheck(SetActivity.this, new MyCPCheckUpdateCallback());
                break;
            case R.id.rel_set_loginout:
                if(configEntity.isLogin){
                    configEntity.isLogin = false;
                    ConfigUtil.saveConfig(this, configEntity);
                    ToastUtil.showToast("退出账户成功", this, ToastUtil.DELAY_SHORT);
                    tvLoginState.setText("登录");
                    configEntity = ConfigUtil.loadConfig(this);
                }else{
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                }
                break;
        }
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
        return "1.0.0";
    }

    private class MyCPCheckUpdateCallback implements CPCheckUpdateCallback {

        @Override
        public void onCheckUpdateCallback(AppUpdateInfo info, AppUpdateInfoForInstall infoForInstall) {
            if(infoForInstall == null && info == null){
                Toast.makeText(getApplicationContext(), "当前已是最新版本!", Toast.LENGTH_SHORT).show();
            } else {
                BDAutoUpdateSDK.uiUpdateAction(SetActivity.this, new MyUICheckUpdateCallback());
            }
        }

    }

    private class MyUICheckUpdateCallback implements UICheckUpdateCallback {

        @Override
        public void onCheckComplete() {

        }
    }
}
