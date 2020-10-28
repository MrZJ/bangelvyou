package com.yishangshuma.bangelvyou.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * 缓存图片工具类
 */
public class CacheImgUtil {

	private static final String ProjectName = "bange";
	private static final String CacheName = "img";
	private static CacheImgUtil INSTANCE = null;  
	private static String PATH_CACHE; 
	public static String adv_top, adv1, adv2, adv3, adv4, adv5, adv6, adv7, adv8, adv9, adv10, adv11
	, img_logo,user_icon,home_btn1,home_btn2,home_btn3,home_btn4;
    
    /** 
     *  
     * 初始化目录 
     *  
     * */  
    public void init(Context context) {  
        if (Environment.getExternalStorageState().equals(  
                Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中  
        	PATH_CACHE = Environment.getExternalStorageDirectory()  
                    .getAbsolutePath() + File.separator + ProjectName
                    + File.separator + CacheName;  
        } else {// 如果SD卡不存在，就保存到本应用的目录下  
        	PATH_CACHE = context.getFilesDir().getAbsolutePath()  
                    + File.separator + ProjectName
                    + File.separator + CacheName;  
        }  
        File file = new File(PATH_CACHE);  
        if (!file.exists()) {  
            file.mkdirs();  
        }
        adv_top = PATH_CACHE + File.separator + "advtop.png";
    	adv1 = PATH_CACHE + File.separator + "adv1.png";
    	adv2 = PATH_CACHE + File.separator + "adv2.png";
    	adv3 = PATH_CACHE + File.separator + "adv3.png";
    	adv4 = PATH_CACHE + File.separator + "adv4.png";
    	adv5 = PATH_CACHE + File.separator + "adv5.png";
    	adv6 = PATH_CACHE + File.separator + "adv6.png";
    	adv7 = PATH_CACHE + File.separator + "adv7.png";
    	adv8 = PATH_CACHE + File.separator + "adv8.png";
    	adv9 = PATH_CACHE + File.separator + "adv9.png";
    	adv10 = PATH_CACHE + File.separator + "adv10.png";
    	adv11 = PATH_CACHE + File.separator + "adv11.png";
    	img_logo = PATH_CACHE + File.separator + "logo.png";
    	user_icon = PATH_CACHE + File.separator + "user_icon.jpeg";
    	home_btn1 = PATH_CACHE + File.separator + "home_btn1.png";
    	home_btn2 = PATH_CACHE + File.separator + "home_btn2.png";
    	home_btn3 = PATH_CACHE + File.separator + "home_btn3.png";
    	home_btn4 = PATH_CACHE + File.separator + "home_btn4.png";
    }  
  
    public static CacheImgUtil getInstance(Context context) {  
        if (INSTANCE == null) {  
            INSTANCE = new CacheImgUtil(context);  
        }  
        return INSTANCE;  
    }  
  
    private CacheImgUtil(Context context) {  
        init(context);  
    }
    
    /**
     * 清楚缓存图片
     */
    public static void wipeCache() {  
         new File(adv_top).delete();
         new File(adv1).delete();
         new File(adv2).delete();
         new File(adv3).delete();
         new File(adv4).delete();
         new File(adv5).delete();
         new File(adv6).delete();
         new File(adv7).delete();
         new File(adv8).delete();
         new File(adv9).delete();
         new File(adv10).delete();
         new File(adv11).delete();
         new File(home_btn1).delete();
         new File(home_btn2).delete();
         new File(home_btn3).delete();
         new File(home_btn4).delete();
    }
}
