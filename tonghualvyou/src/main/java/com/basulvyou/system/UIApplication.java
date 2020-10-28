package com.basulvyou.system;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.basulvyou.system.http.OkHttpStack;
import com.basulvyou.system.util.CacheImgUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.L;

import org.xutils.x;

/**
 * Created by Administrator on 2016/1/25.
 */
public class UIApplication extends Application {

    /** 应用程序全局变量 */
    private static UIApplication app;
    public static RequestQueue mRequestQueue;
    @Override
    public void onCreate(){
        super.onCreate();
        app = this;
        CacheImgUtil.getInstance(this);
        initImageLoader(getApplicationContext());
        SDKInitializer.initialize(getApplicationContext());
        x.Ext.init(this);
        mRequestQueue = Volley.newRequestQueue(this, new OkHttpStack());
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
