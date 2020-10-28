package com.basulvyou.system.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.WindowManager;

import com.basulvyou.system.R;
import com.basulvyou.system.util.AsyncExecuter;
import com.basulvyou.system.util.CacheImgUtil;
import com.basulvyou.system.util.StringUtil;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

/**
 * 欢迎页面
 */
public class WelcomeActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    public static final int RC_CAMERA_AND_LOCATION = 222;
    private final static int MSG_WELCOME_FINISH = 1;
    private final static int MSG_GUIDE = 2;
    private boolean first;    //判断是否第一次打开软件
    private String key, uMessage;
    String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
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
//        first = configEntity.isFirst;
//        if (first) {
//            mHandler.sendEmptyMessageDelayed(MSG_GUIDE, 2000);
//        } else {
//        }
        //mHandler.sendEmptyMessageDelayed(MSG_WELCOME_FINISH, 2000);
        EasyPermissions.requestPermissions(
                new PermissionRequest.Builder(this, RC_CAMERA_AND_LOCATION, perms)
                        .setRationale("需要定位权限才能正常使用APP")
                        .setPositiveButtonText("允许")
                        .setNegativeButtonText("退出")
                        .build());
        saveLogoImg();
    }

    protected void processMessage(Message msg) {
        super.processMessage(msg);
        switch (msg.what) {
            case MSG_WELCOME_FINISH:
                mHandler.removeMessages(MSG_WELCOME_FINISH);
                Intent intent = new Intent(this, MainActivity.class);
                if (!StringUtil.isEmpty(uMessage)) {
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
    public void saveLogoImg() {
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
                        while ((count = is.read(buffer)) > 0) {
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

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.e("jianzhang", "onPermissionsGranted" + perms.toString());
        if (EasyPermissions.hasPermissions(this, this.perms)) {
            Log.e("jianzhang", "onPermissionsGranted true");
            mHandler.sendEmptyMessageDelayed(MSG_WELCOME_FINISH, 1000);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        EasyPermissions.requestPermissions(this, getString(R.string.camera_and_location_rationale),
                RC_CAMERA_AND_LOCATION, this.perms);
    }
}
