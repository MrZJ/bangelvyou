package com.objectreview.system.api;


import com.objectreview.system.utlis.ConfigUtil;

/**
 *产品出厂记录详情API
 */
public class FactoryLogDetailApi {

    public static String url;
    public static final String ACTION_FACTORY_LOG_DETAIL = "/Service.do?method=scan_list_detail";
    public static final int API_FACTORY_LOG_DETAIL = 1;//出厂纪录

    /**
     * 获取拼成列表数据
     *
     * @return
     */
    public static String getFactoryLogDetail(String key,String id) {
        url = String.format(ACTION_FACTORY_LOG_DETAIL);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&key=").append(key)
                .append("&id=").append(id);
        return urlBuffer.toString();
    }
}
