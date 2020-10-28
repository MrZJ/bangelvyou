package com.shishoureport.system;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.hss01248.notifyutil.NotifyUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.L;
import com.shishoureport.system.http.OkHttpStack;
import com.shishoureport.system.service.UmengNotificationService;
import com.shishoureport.system.utils.CacheImgUtil;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import org.xutils.x;

/**
 * Created by Administrator on 2016/6/27.
 */
public class UIApplication extends Application {

    /**
     * 应用程序全局变量
     */
    private static UIApplication app;
    public static RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(true);
        mPushAgent.setResourcePackageName("com.shishoureport.system");
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String s) {
                Log.e("register","onSuccess="+s);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.e("register","onFailure="+s1);
            }
        });
        mPushAgent.setNotificationClickHandler(new UmengNotificationClickHandler(){
            @Override
            public void dealWithCustomAction(Context context, UMessage uMessage) {
                Log.e("dealWithCustomAction","onFailure="+uMessage.toString());
                super.dealWithCustomAction(context, uMessage);
            }
        });
        //注册推送服务，每次调用register方法都会回调该接口
        Fresco.initialize(this);
        CacheImgUtil.getInstance(this);
        initImageLoader(getApplicationContext());
        x.Ext.init(this);
        mRequestQueue = Volley.newRequestQueue(this, new OkHttpStack());
        NotifyUtil.init(getApplicationContext());
        mPushAgent.setPushIntentServiceClass(UmengNotificationService.class);
    }

    public static UIApplication getInstance() {
        if (null == app) {
            app = new UIApplication();
        }
        return app;
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2).threadPoolSize(5).memoryCacheExtraOptions(480, 800)
                .denyCacheImageMultipleSizesInMemory().memoryCache(new WeakMemoryCache())
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        L.disableLogging();//关闭log
        ImageLoader.getInstance().init(config);
    }
}
