package com.objectreview.system.api;

import com.objectreview.system.utlis.ConfigUtil;

/**
 * 物流信息接口
 */
public class LogisticsApi {

    public static String url;
    public static final String ACTION_LOGISTIC = "/Service.do?method=wl_comp_list";
    public static final int API_LOGISTIC = 1;//物流信息

    /**
     * 物流数据
     * @return
     */
    public static String getLogisticUrl(String userKey)
    {
        url = String.format(ACTION_LOGISTIC);
        return new StringBuffer(ConfigUtil.HTTP_URL).append(url).append("&key=").append(userKey).toString();
    }

}
