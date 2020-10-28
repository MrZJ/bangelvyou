package com.shenmai.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.shenmai.system.R;
import com.umeng.message.PushAgent;

public class WelcomeActivity extends BaseActivity {

    private final static int MSG_WELCOME_FINISH = 1;
    private final static int MSG_GUIDE = 2;
    private boolean first;	//判断是否第一次打开软件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        PushAgent mPushAgent = PushAgent.getInstance(this);
        // mPushAgent.setDebugMode(true);//调试模式�?�?
        mPushAgent.enable();
        mPushAgent.onAppStart();

        first = configEntity.isFirst;
        if (first) {
            mHandler.sendEmptyMessageDelayed(MSG_GUIDE, 2000);
        } else {
            mHandler.sendEmptyMessageDelayed(MSG_WELCOME_FINISH, 2000);
        }
    }

    protected void processMessage(Message msg) {
        super.processMessage(msg);
        switch (msg.what) {
            case MSG_WELCOME_FINISH:
                mHandler.removeMessages(MSG_WELCOME_FINISH);
                if(configEntity.isLogin){
                    if(configEntity.userRole.equals("1")){
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(this, NoticeActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                finish();
                break;
            case MSG_GUIDE:
                mHandler.removeMessages(MSG_GUIDE);
                startActivity(new Intent(this, GuideActivity.class));
                finish();
                break;
        }
    }
}
