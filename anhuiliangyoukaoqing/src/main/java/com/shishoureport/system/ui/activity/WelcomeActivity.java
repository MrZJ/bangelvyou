package com.shishoureport.system.ui.activity;

import android.os.Message;
import android.view.WindowManager;

import com.shishoureport.system.R;
import com.shishoureport.system.utils.MySharepreference;

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
        if (MySharepreference.getInstance(this).isLogin()) {
            mHandler.sendEmptyMessageDelayed(MSG_IS_DELAY_MAIN, 1000);
        } else {
            mHandler.sendEmptyMessageDelayed(MSG_IS_DELAY_LOGIN, 1000);
        }
    }

    protected void processMessage(Message msg) {
        super.processMessage(msg);
        switch (msg.what) {
            case MSG_IS_DELAY_MAIN:
                mHandler.removeMessages(MSG_IS_DELAY_MAIN);
                MainActivity.startActivity(this);
                finish();
                break;
            case MSG_IS_DELAY_LOGIN:
                mHandler.removeMessages(MSG_IS_DELAY_MAIN);
                LoginActivity.startActivity(this);
                finish();
                break;
        }
    }
}
