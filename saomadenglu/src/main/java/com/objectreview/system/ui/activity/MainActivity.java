package com.objectreview.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.objectreview.system.R;
import com.objectreview.system.api.ComfirmApi;
import com.objectreview.system.entity.MyCodeResult;
import com.objectreview.system.entity.MyComfimResult;
import com.objectreview.system.utlis.ConfigUtil;
import com.objectreview.system.utlis.ToastUtil;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private View normalUser, ruleUser;
    private View codeScan;
    private View outScan, outRem, makeIn;
    private TextView userName;
    public static final String EXTRA_DATA = "extra_data";

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
    }

    private void initView() {
        initTopView();
        setLeftBackShow(false);
        userName = (TextView) findViewById(R.id.tv_user_login);
        userName.setText(configEntity.username);
        normalUser = findViewById(R.id.lin_nomal_user);
        ruleUser = findViewById(R.id.lin_rule_user);
        normalUser.setVisibility(View.GONE);
        ruleUser.setVisibility(View.VISIBLE);
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

    @Override
    public void onClick(View v) {
        Intent scanIntent = new Intent(this, ScanActivity.class);
        switch (v.getId()) {
            case R.id.rel_code_scan:
                startActivityForResult(scanIntent, 1000);
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
    public void httpOnResponse(String json, int action) {
        switch (action) {
            case ComfirmApi.API_COMFIRM:
                comfimHander(json);
                break;
        }
    }

    private void comfimHander(String json) {
        MyComfimResult result = JSON.parseObject(json, MyComfimResult.class);
        if (result != null && "200".equals(result.code)) {
            Log.e("jianzhang", "已确认");
            Toast.makeText(this, "扫码成功！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "扫码失败，请重试！", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (1000 == requestCode) {
            if (resultCode == RESULT_OK) {
                MyCodeResult result = (MyCodeResult) data.getSerializableExtra(EXTRA_DATA);
                if (result != null) {
                    doComFirmLogin(result);
                } else {
                    Toast.makeText(this, "扫码失败，请重试！", Toast.LENGTH_SHORT).show();
                }
            }
        }
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

    private void doComFirmLogin(MyCodeResult result) {
        httpPostRequest(ComfirmApi.getComfirmUrl(), getPamarams(result), ComfirmApi.API_COMFIRM);
    }

    private Map<String, String> getPamarams(MyCodeResult result) {
        HashMap<String, String> hashMap = new HashMap<>();
        configEntity = ConfigUtil.loadConfig(getApplicationContext());
         hashMap.put("key", configEntity.key);
        hashMap.put("login_name", configEntity.username);
        hashMap.put("access_token", result.access_token);
        hashMap.put("scope", result.scope);
        String device_token = ConfigUtil.phoneIMEI;
        hashMap.put("device_tokens", device_token);
        hashMap.put("client", "android");
        return hashMap;
    }
}
