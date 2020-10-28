package com.shenmailogistics.system.activity;

import android.content.Intent;
import android.os.Message;
import android.view.WindowManager;

import com.shenmailogistics.system.R;


public class WelcomeActivity extends BaseActivity {

    private final static int MSG_IS_DELAY_MAIN = 1;
    private final static int MSG_IS_DELAY_LOGIN = 2;

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessageDelayed(MSG_IS_DELAY_LOGIN, 2000);
    }

    protected void processMessage(Message msg) {
        super.processMessage(msg);
        switch (msg.what) {
            case MSG_IS_DELAY_MAIN:
                mHandler.removeMessages(MSG_IS_DELAY_MAIN);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case MSG_IS_DELAY_LOGIN:
                mHandler.removeMessages(MSG_IS_DELAY_LOGIN);
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
                break;
        }
    }
}
