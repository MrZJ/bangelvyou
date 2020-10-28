package com.shishoureport.system.ui.activity;

import android.content.Intent;
import android.os.Message;
import android.view.WindowManager;

import com.shishoureport.system.R;

public class WelcomeActivity extends BaseActivity {

    private final static int MSG_IS_DELAY = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mHandler.sendEmptyMessageDelayed(MSG_IS_DELAY, 2000);
    }

    protected void processMessage(Message msg) {
        super.processMessage(msg);
        switch (msg.what) {
            case MSG_IS_DELAY:
                mHandler.removeMessages(MSG_IS_DELAY);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
