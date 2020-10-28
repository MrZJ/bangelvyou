package com.objectreview.system.api;


import com.objectreview.system.entity.Pager;
import com.objectreview.system.utlis.ConfigUtil;

/**
 *产品出厂记录API
 */
public class FactoryLogApi {

    public static String url;
    public static final String ACTION_FACTORY_LOG_LIST = "/Service.do?method=scan_list";
    public static final int API_FACTORY_LOG_LIST = 1;//出厂纪录

    /**
     * 获取拼成列表数据
     *
     * @return
     */
    public static String getFactoryLogList(String key,String curpage) {
        url = String.format(ACTION_FACTORY_LOG_LIST);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&key=").append(key)
                .append("&curpage=").append(curpage)
                .append("&pagesize=").append(Pager.rows);
        return urlBuffer.toString();
    }
}
