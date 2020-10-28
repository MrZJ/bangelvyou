package com.basulvyou.system.api;


import com.basulvyou.system.util.ConfigUtil;

/**
 * 首页广告轮播图接口
 * Created by KevinLi on 2016/2/29.
 */
public class HomeApi {
    public static String url;
    public static final String ACTION_GET_HOME = "/Service.do?method=index&op=indexad";
    public static final int API_GET_HOME = 1;//首页广告
    public static final String ACTION_GET_HOME_WEATHER = "/Service.do?method=selectWeather&op=selectWeather";
    public static final int API_GET_HOME_WEATHER = 2;//首页天气

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

    /**
     * 获取首页天气数据
     *
     * @return
     */
    public static String getHomeWeatherUrl()
    {
        url = String.format(ACTION_GET_HOME_WEATHER);
        return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
    }
}
