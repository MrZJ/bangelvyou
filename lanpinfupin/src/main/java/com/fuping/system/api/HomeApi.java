package com.fuping.system.api;


import com.fuping.system.utils.ConfigUtil;

/**
 * 首页广告轮播图接口
 * Created by KevinLi on 2016/2/29.
 */
public class HomeApi {
    public static String url;
    public static final String ACTION_GET_HOME = "/WebService.do?method=getHomeImageList";
    public static final int API_GET_HOME = 1;//首页广告

    /**
     * 获取首页广告数据
     *
     * @return
     */
    public static String getHomeUrl()
    {
        url = String.format(ACTION_GET_HOME);
        return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
    }

}
