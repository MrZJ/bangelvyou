package com.shenmai.system;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.L;
import com.shenmai.system.http.OkHttpStack;
import com.shenmai.system.utlis.AsynImageUtil;
import com.shenmai.system.utlis.CacheImgUtil;
import com.shenmai.system.utlis.GetHeightAndWidthUtil;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.utils.Log;

import org.xutils.x;

import java.io.File;

/**
 * Created by Administrator on 2016/6/27.
 */
public class UIApplication extends Application {

    /** 应用程序全局变量 */
    private static UIApplication app;
    public static RequestQueue mRequestQueue;
    @Override
    public void onCreate(){
        super.onCreate();
        UMShareAPI.get(this);
        Log.LOG = false;//关闭友盟log
        Config.IsToastTip = false;//关闭友盟Toast
        Config.dialogSwitch = false;//不使用友盟默认dialog
        app = this;
        x.Ext.init(this);
        CacheImgUtil.getInstance(this);
        initImageLoader(getApplicationContext());
        Config.REDIRECT_URL = "http://www.sharesdk.cn";

        GetHeightAndWidthUtil.getHeightAndWidth(this);
        mRequestQueue = Volley.newRequestQueue(this, new OkHttpStack());
    }

    {
        PlatformConfig.setWeixin("wx6f32a487055176a1", "d4624c36b6795d1d99dcf0547af5443d");
        PlatformConfig.setSinaWeibo("212724127", "4b0dc71ce11aacf24c7f91a8f5bc3e01");
        PlatformConfig.setQQZone("1104949338", "pHGJI3D2JILSBQHT");
    }

    public static UIApplication getInstance() {
        if (null == app) {
            app = new UIApplication();
        }
        return app;
    }

    public static void initImageLoader(Context context) {
        File imgCache = new File(CacheImgUtil.PATH_CACHE);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .threadPoolSize(5)
                .memoryCacheExtraOptions(480, 800)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCacheSize(2 * 1024 * 1024)  // 内存缓存的最大值
                .memoryCache(new WeakMemoryCache())
                .discCache(new UnlimitedDiscCache(imgCache)) // 可以自定义缓存路径
                .discCacheSize(30 * 1024 * 1024) // 30 Mb sd卡(本地)缓存的最大值
                .discCacheFileCount(100)  // 可以缓存的文件数量
                .defaultDisplayImageOptions(AsynImageUtil.getImageOptions())
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        L.disableLogging();//关闭log
        ImageLoader.getInstance().init(config);

    }

}
