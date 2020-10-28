package com.yishangshuma.bangelvyou.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.umeng.analytics.MobclickAgent;
import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.util.AsyncExecuter;
import com.yishangshuma.bangelvyou.util.CacheImgUtil;
import com.yishangshuma.bangelvyou.util.StringUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 欢迎页面
 */
public class WelcomeActivity extends BaseActivity {

    private final static int MSG_WELCOME_FINISH = 1;
    private final static int MSG_GUIDE = 2;
    private boolean first;	//判断是否第一次打开软件
    private String key, uMessage;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
//        //友盟禁止默认的页面统计方式
        MobclickAgent.openActivityDurationTrack(false);
//        //开启友盟推送
//        PushAgent mPushAgent = PushAgent.getInstance(this);
//        mPushAgent.onAppStart();
//        //mPushAgent.setDebugMode(true);//调试模式开启
//        mPushAgent.enable();
//
//        //如果有推送信息就获取推送信息
//        if(getIntent() != null && getIntent().getExtras() != null){
//            key = getIntent().getExtras().getString("key");
//            uMessage = getIntent().getExtras().getString("uMessage");
//        }

        // 如果网络可用则判断是否第一次进入，如果是第一次则进入欢迎界面
        first = configEntity.isFirst;
        if(first){
            mHandler.sendEmptyMessageDelayed(MSG_GUIDE, 2000);
        } else {
            mHandler.sendEmptyMessageDelayed(MSG_WELCOME_FINISH, 2000);
        }
        //mHandler.sendEmptyMessageDelayed(MSG_WELCOME_FINISH, 2000);
        saveLogoImg();
    }

    protected void processMessage(Message msg) {
        super.processMessage(msg);
        switch (msg.what) {
            case MSG_WELCOME_FINISH:
                mHandler.removeMessages(MSG_WELCOME_FINISH);
                Intent intent = new Intent(this, MainActivity.class);
                if(!StringUtil.isEmpty(uMessage)){
                    intent.putExtra("key", key);
                    intent.putExtra("uMessage", uMessage);
                }
                startActivity(intent);
                finish();
                break;
            case MSG_GUIDE:
                mHandler.removeMessages(MSG_GUIDE);
                startActivity(new Intent(this, GuideActivity.class));
                finish();
                break;
        }
    }

    /**
     * 存储logo图片到手机存储卡
     */
    public void saveLogoImg(){
        AsyncExecuter.getInstance().execute(new Runnable() {
            public void run() {
                try {
                    File imgFile = new File(CacheImgUtil.img_logo);
                    if (!imgFile.exists()) {
                        InputStream is = getResources().openRawResource(R.raw.ic_launcher);
                        FileOutputStream fos;
                        fos = new FileOutputStream(CacheImgUtil.img_logo);
                        byte[] buffer = new byte[8192];
                        int count = 0;

                        while ((count = is.read(buffer)) > 0)
                        {
                            fos.write(buffer, 0, count);
                        }
                        fos.close();
                        is.close();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
