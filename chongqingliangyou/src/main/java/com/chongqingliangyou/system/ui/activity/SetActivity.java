package com.chongqingliangyou.system.ui.activity;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chongqingliangyou.system.R;
import com.chongqingliangyou.system.api.UpdateApkApi;
import com.chongqingliangyou.system.entity.FieldErrors;
import com.chongqingliangyou.system.entity.UpdateEntity;
import com.chongqingliangyou.system.util.CacheImgUtil;
import com.chongqingliangyou.system.util.ConfigUtil;
import com.chongqingliangyou.system.util.GetVersion;
import com.chongqingliangyou.system.util.Openfile;
import com.chongqingliangyou.system.util.StringUtil;
import com.chongqingliangyou.system.util.WifiNetworkUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * 设置页面
 */
public class SetActivity extends BaseActivity implements View.OnClickListener{

    private View btnSettingPassword, btnUpdate, btnHelp, btnLoginOut;
    private NotificationManager manager;
    private NotificationCompat.Builder builder;
    private String locationSrc;
    private static int NOTIFICATION_ID=111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        initView();
        initListener();
    }

    private void initView() {
        initTopView();
        setTitle("设置");
        btnSettingPassword = findViewById(R.id.btn_settingPassword);
        btnUpdate = findViewById(R.id.btn_update);
        btnHelp = findViewById(R.id.btn_help);
        btnLoginOut = findViewById(R.id.btn_loginout);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        btnSettingPassword.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnHelp.setOnClickListener(this);
        btnLoginOut.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_settingPassword:
                IntentOtherActivity(ModifyPassWordActivity.class);
                break;
            case R.id.btn_update:
                btnUpdate.setClickable(false);
                updateApk();
                break;
            case R.id.btn_help:
                IntentOtherActivity(HelpActivity.class);
                break;
            case R.id.btn_loginout:
                configEntity = ConfigUtil.loadConfig(this);
                configEntity.isLogin = false;
                configEntity.passwordMD5 = "";
                configEntity.key = "";
                configEntity.username = "";
                ConfigUtil.saveConfig(this, configEntity);
                Toast.makeText(this,"退出账户成功",Toast.LENGTH_LONG).show();
                IntentOtherActivity(LoginActivity.class);
                appManager.finishLoginOtherActivity();
                break;
        }
    }

    /**
     * 发请求更新apk
     */
    private void updateApk() {
        httpGetRequest(UpdateApkApi.getUpdateUrl(), UpdateApkApi.API_UPDATE);
    }

    @Override
    public void httpResponse(String json, int action){
        super.httpResponse(json, action);
        switch (action)
        {
            case UpdateApkApi.API_UPDATE:
                IsUpdate(json);
                break;
            default:
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        setBtnClickable();
    }

    /**
     * 判断是否更新apk
     *
     * @param json
     */
    private void IsUpdate(String json) {
        UpdateEntity updateEntity=JSON.parseObject(json, UpdateEntity.class);
        int getCode = Integer.valueOf(updateEntity.versionCode);
        if (getCode > GetVersion.getVersionCode(this)) {
            OpenHintUpdate(updateEntity.downloadUrl,updateEntity.updateLog);
        }else {
            Toast.makeText(this, "当前为最新版本", Toast.LENGTH_SHORT).show();
            setBtnClickable();
        }

    }

    /**
     * 提示用户更新
     *
     */
    private void OpenHintUpdate(final String url,String log) {
        AlertDialog.Builder dialog= new AlertDialog.Builder(this);
        dialog.setMessage(log);
        dialog.setPositiveButton("升级", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean connect=WifiNetworkUtil.OpenNetworkSetting(SetActivity.this);
                if (!connect){
                    return;
                }
                if(!TextUtils.isEmpty(url)) {
                    locationSrc = CacheImgUtil.PATH_DATA_CACHE + "/" + System.currentTimeMillis() + ".apk";
                    download(url);
                }
            }

        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                setBtnClickable();
            }
        });
        dialog.show();
    }

    /**
     * 设置更新按钮可以点击
     */
    private void setBtnClickable() {
        btnUpdate.setClickable(true);
    }

    public void download(String url) {
        RequestParams params = new RequestParams(url);
        params.setSaveFilePath(locationSrc);
        x.http().get(params, new Callback.ProgressCallback<File>() {

            @Override
            public void onSuccess(File file) {
                complateDownload();
                setBtnClickable();
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                if(!StringUtil.isEmpty(throwable.getMessage())){
                    Toast.makeText(SetActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                    manager.cancel(NOTIFICATION_ID);
                    setBtnClickable();
                }
            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                startDownload();
            }

            @Override
            public void onLoading(long total, long current, boolean b) {
                try {
                    int progress = (int) (100 * current / total);
                    downLoading(progress);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void initNotification() {
        manager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this);
        builder.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                // .setNumber(number)//显示数量
                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                        // .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                        // .setDefaults(Notification.DEFAULT_LIGHTS)//
                        // 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                        // Notification.DEFAULT_ALL Notification.DEFAULT_SOUND 添加声音 //
                        // requires VIBRATE permission
                .setSmallIcon(R.mipmap.ic_launcher);

    }

    public void clear() {
        if (manager != null) {
            manager = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);
            manager.cancel(NOTIFICATION_ID);
        }
    }

    public void startDownload() {
        clear();
        initNotification();
        builder.setContentText("开始下载...").setContentTitle("下载");
        Notification n = builder.build();
        manager.notify(NOTIFICATION_ID, n);
    }

    public void downLoading(int progress) throws InterruptedException {
        builder.setContentText("正在下载:" + progress + "%")
                .setProgress(100, progress, false).setContentTitle("下载中");
        Notification n1 = builder.build();
        manager.notify(NOTIFICATION_ID, n1);
        if (android.os.Build.VERSION.SDK_INT > 19) {
            Thread.sleep(50);
        }
    }

    public void complateDownload() {
        clear();
        initNotification();
        builder.setContentText("下载完成	").setContentTitle("下载")
                .setLights(Color.BLUE, 1000, 1000).setAutoCancel(true);
        if (locationSrc!=null) {
            Intent intent = Openfile.openFile(locationSrc);
            PendingIntent pendingIntent = PendingIntent
                    .getActivity(SetActivity.this, 0, intent,
                            PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setContentIntent(pendingIntent);
            locationSrc = null;
        }
        Notification n2 = builder.build();
        manager.notify(NOTIFICATION_ID, n2);
    }
}
