package com.objectreview.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.WindowManager;

import com.objectreview.system.R;

/**
 * 欢迎页面
 */
public class WelcomeActivity extends BaseActivity {

    private final static int MSG_IS_LOGIN = 1;
    private final static int MSG_UNLOGIN = 2;
    private boolean login;	//判断用户是否登录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 如果网络可用则判断是否第一次进入，如果是第一次则进入欢迎界面
        login = configEntity.isLogin;
        if(login){
            mHandler.sendEmptyMessageDelayed(MSG_IS_LOGIN, 2000);
        } else {
            mHandler.sendEmptyMessageDelayed(MSG_UNLOGIN, 2000);
        }
    }


    protected void processMessage(Message msg) {
        super.processMessage(msg);
        switch (msg.what) {
            case MSG_IS_LOGIN:
                mHandler.removeMessages(MSG_IS_LOGIN);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case MSG_UNLOGIN:
                mHandler.removeMessages(MSG_UNLOGIN);
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
                break;
        }
    }
}
