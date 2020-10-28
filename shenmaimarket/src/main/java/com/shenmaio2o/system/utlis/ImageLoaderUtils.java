package com.shenmaio2o.system.utlis;

import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * 图片加载工具类
 * Created by Administrator on 2017/3/2.
 */

public class ImageLoaderUtils {

    /**
     * 加载图片
     * @param url
     * @param view
     */
    public static void getImage(String url, SimpleDraweeView view){
        Uri uri = Uri.parse(url);
        view.setImageURI(uri);
    }

}
