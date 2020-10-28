package com.yishangshuma.bangelvyou;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.L;
import com.yishangshuma.bangelvyou.util.CacheImgUtil;

/**
 * Created by Administrator on 2016/1/25.
 */
public class UIApplication extends Application {

    /** 应用程序全局变量 */
    private static UIApplication app;
    @Override
    public void onCreate(){
        super.onCreate();
        app = this;
        CacheImgUtil.getInstance(this);
        initImageLoader(getApplicationContext());
        SDKInitializer.initialize(getApplicationContext());
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
