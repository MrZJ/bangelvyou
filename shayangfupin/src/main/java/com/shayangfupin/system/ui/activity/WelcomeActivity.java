package com.shayangfupin.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.WindowManager;

import com.shayangfupin.system.R;


public class WelcomeActivity extends BaseActivity {

    private final static int MSG_IS_DELAY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
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
