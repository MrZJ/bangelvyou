package com.shenmai.system.api;

import com.shenmai.system.utlis.ConfigUtil;

/**
 * 市场广告数据接口
 */
public class AdsApi {

    public static String url;
    public static final String ACTION_GET_ADS_CATE = "/Service.do?method=getBannerAndBaseClass";
    public static final int API_GET_ADS_CATE = 1;//首页广告以及分类

    /**
     * 获取首页广告数据
     */
    public static String getAdsAndCateUrl()
    {
        url = String.format(ACTION_GET_ADS_CATE);
        return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
    }
}
