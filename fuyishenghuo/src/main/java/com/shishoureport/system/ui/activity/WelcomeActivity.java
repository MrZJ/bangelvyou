package com.shishoureport.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;

import com.shishoureport.system.R;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

public class WelcomeActivity extends BaseActivity {

    private final static int MSG_IS_DELAY = 1;
    private String key, uData;

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initView() {
        //开启友盟推送
        //如果有推送信息就获取推送信息
        if (getIntent() != null && getIntent().getExtras() != null) {
            key = getIntent().getExtras().getString("msg_type");
            uData = getIntent().getExtras().getString("msg_data");
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mHandler.sendEmptyMessageDelayed(MSG_IS_DELAY, 2000);
    }

    protected void processMessage(Message msg) {
        super.processMessage(msg);
        switch (msg.what) {
            case MSG_IS_DELAY:
                mHandler.removeMessages(MSG_IS_DELAY);
                Intent intent = new Intent(this, MainActivity.class);
                if (key != null && uData != null) {
                    intent.putExtra("key", key);
                    intent.putExtra("uData", uData);
                }
                startActivity(intent);
                finish();
                break;
        }
    }
}
