package com.objectreview.system.api;

import com.objectreview.system.utlis.ConfigUtil;

/**
 * 扫描获取信息接口
 */
public class ScanApi {

    public static String url;
    public static final String ACTION_SCAN_BZ = "/Service.do?method=scan_pack_code";
    public static final int API_SCAN_BZ = 1;//包装码扫描接口
    public static final String ACTION_SCAN_CP = "/Service.do?method=scan_pd_code";
    public static final int API_SCAN_CP = 2;//产品码扫描接口

    /**
     * 包装码包含信息获取
     * @return
     */
    public static String getBZUrl(String userKey,String code)
    {
        url = String.format(ACTION_SCAN_BZ);
        StringBuffer stringBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        stringBuffer.append(url).append("&key=").append(userKey).append("&").append(code);
        return stringBuffer.toString();
    }

    /**
     * 产品码包含信息获取
     * @return
     */
    public static String getCPUrl(String userKey,String code)
    {
        url = String.format(ACTION_SCAN_CP);
        StringBuffer stringBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        stringBuffer.append(url).append("&key=").append(userKey).append("&").append(code);
        return stringBuffer.toString();
    }

}
