package com.objectreview.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.objectreview.system.R;
import com.objectreview.system.api.LogisticsApi;
import com.objectreview.system.entity.LogisticsList;
import com.objectreview.system.utlis.ConfigUtil;
import com.objectreview.system.utlis.StringUtil;
import com.objectreview.system.utlis.ToastUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private View normalUser, ruleUser;
    private View codeScan;
    private View outScan, outRem, makeIn;
    private TextView userName;

    private Button loginOut;

    private static boolean isExit = false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configEntity = ConfigUtil.loadConfig(getApplicationContext());
        initView();
        initListener();
        isUpdateApk();
    }

    private void initView(){
        initTopView();
        setLeftBackShow(false);
        userName = (TextView) findViewById(R.id.tv_user_login);
        userName.setText(configEntity.username);
        normalUser = findViewById(R.id.lin_nomal_user);
        ruleUser = findViewById(R.id.lin_rule_user);
        if(configEntity.usertype.equals("3")){
            normalUser.setVisibility(View.VISIBLE);
            ruleUser.setVisibility(View.GONE);
        } else{
            normalUser.setVisibility(View.GONE);
            ruleUser.setVisibility(View.VISIBLE);
            getLogisticsInfo();
        }
        outScan = findViewById(R.id.rel_out_scan);
        outRem = findViewById(R.id.rel_out_rem);
        makeIn = findViewById(R.id.rel_makein);

        codeScan = findViewById(R.id.rel_code_scan);
        loginOut = (Button) findViewById(R.id.btn_login_out);
    }

    @Override
    public void initListener() {
        outScan.setOnClickListener(this);
        outRem.setOnClickListener(this);
        makeIn.setOnClickListener(this);
        codeScan.setOnClickListener(this);
        loginOut.setOnClickListener(this);
    }

    private void getLogisticsInfo(){
        httpGetRequest(LogisticsApi.getLogisticUrl(configEntity.key), LogisticsApi.API_LOGISTIC);
    }

    @Override
    public void onClick(View v) {
        Intent scanIntent = new Intent(this,ScanActivity.class);
        switch (v.getId()){
            case R.id.rel_out_scan:
                startActivity(new Intent(this, FactoryScanActivity.class));
                break;
            case R.id.rel_out_rem:
                startActivity(new Intent(this, FactoryLogActivity.class));
                break;
            case R.id.rel_makein:
                startActivity(new Intent(this, MergePackagActivity.class));
                break;
            case R.id.rel_code_scan:
                startActivity(scanIntent);
                break;
            case R.id.btn_login_out:
                configEntity.isLogin = false;
                configEntity.key = "";
                configEntity.usertype = "";
                configEntity.username = "";
                ConfigUtil.saveConfig(this, configEntity);
                ToastUtil.showToast("退出账户成功", this, ToastUtil.DELAY_SHORT);
                startActivity(new Intent(this, LoginActivity.class));
                this.finish();
                break;
        }
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){
            case LogisticsApi.API_LOGISTIC:
                LogisticsHander(json);
                break;
        }
    }

    private void LogisticsHander(String json){
        LogisticsList logisticsList = JSON.parseObject(json,LogisticsList.class);
        if(!StringUtil.isEmpty(logisticsList.state) && logisticsList.state.equals("0")){
            ConfigUtil.logisticsList = logisticsList.list;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            if (ConfigUtil.IS_DOWNLOAD) {
                Toast.makeText(getApplicationContext(), "最新安装包正在下载...", Toast.LENGTH_SHORT).show();
            } else {
                isExit = true;
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                // 利用handler延迟发送更改状态信息
                handler.sendEmptyMessageDelayed(0, 2000);
            }
        } else {
            finish();
            System.exit(0);
        }
    }

}
