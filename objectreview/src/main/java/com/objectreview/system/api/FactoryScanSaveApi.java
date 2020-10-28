package com.objectreview.system.api;


import com.objectreview.system.utlis.ConfigUtil;

/**
 *产品出厂扫描保存API
 */
public class FactoryScanSaveApi {

    public static String url;
    public static final String ACTION_FACTORY_SCAN_SAVE = "/Service.do?method=factory_info_save";
    public static final int API_FACTORY_SCAN_SAVE = 1;//保存纪录

    /**
     * @return
     */
    public static String getFactoryScanSave() {
        url = String.format(ACTION_FACTORY_SCAN_SAVE);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url);
        return urlBuffer.toString();
    }
}
