package com.chongqingliangyou.system.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.chongqingliangyou.system.R;
import com.chongqingliangyou.system.api.WelcomeApi;
import com.chongqingliangyou.system.entity.FieldErrors;
import com.chongqingliangyou.system.util.StringUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

/**
 * 欢迎页面
 */
public class WelcomeActivity extends BaseActivity {

    private final static int MSG_IS_LOGIN = 1;
    private final static int MSG_UNLOGIN = 2;
    private boolean login;	//判断用户是否登录
    private String key, uMessage;

    private ImageView iv_Welcome_background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //友盟禁止默认的页面统计方式
        MobclickAgent.openActivityDurationTrack(false);
        //开启友盟推送
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.onAppStart();
        //mPushAgent.setDebugMode(true);//调试模式开启
        mPushAgent.enable();
        //如果有推送信息就获取推送信息
        if(getIntent() != null && getIntent().getExtras() != null){
            key = getIntent().getExtras().getString("key");
            uMessage = getIntent().getExtras().getString("uMessage");
        }
        initView();
        initWelcomImage();
        // 如果网络可用则判断是否第一次进入，如果是第一次则进入欢迎界面
        login = configEntity.isLogin;
        if(login){
            mHandler.sendEmptyMessageDelayed(MSG_IS_LOGIN, 5000);
        } else {
            mHandler.sendEmptyMessageDelayed(MSG_UNLOGIN, 5000);
        }

    }

    private void initView() {
        iv_Welcome_background = (ImageView)findViewById(R.id.img_welcome_bg);
    }

    /**
     * 获取image Url
     */
    private void initWelcomImage() {
        httpGetRequest(WelcomeApi.getWelcomeImageUrl(), WelcomeApi.API_GET_WELCOME_IMAGE);
    }

    /**
     * 得到image Url
     * @param json
     * @param action
     */
    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){
            case WelcomeApi.API_GET_WELCOME_IMAGE:
                String image_path = JSONObject.parseObject(json).getString("image_path");
                if(!StringUtil.isEmpty(image_path)){
                    LoadImage(image_path);
                }else{
                    iv_Welcome_background.setBackgroundResource(R.mipmap.bg_welcome);
                }
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        iv_Welcome_background.setBackgroundResource(R.mipmap.bg_welcome);
    }

    /**
     * 显示欢迎界面图片
     *
     * @param image_path
     */
    private void LoadImage(String image_path) {
        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                /*.showImageOnLoading(R.mipmap.bg_welcome) // 设置图片下载期间显示的图片*/
                .showImageForEmptyUri(R.mipmap.bg_welcome) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.bg_welcome) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(false)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
        ImageLoader.getInstance().displayImage(image_path, iv_Welcome_background, options);
    }

    protected void processMessage(Message msg) {
        super.processMessage(msg);
        switch (msg.what) {
            case MSG_IS_LOGIN:
                mHandler.removeMessages(MSG_IS_LOGIN);
                Intent intent = new Intent(this, MainActivity.class);
                if(!StringUtil.isEmpty(uMessage)){
                    intent.putExtra("key", key);
                    intent.putExtra("uMessage", uMessage);
                }
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
