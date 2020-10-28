package com.objectreview.system.api;


import com.objectreview.system.utlis.ConfigUtil;

/**
 * 自动添加产品码API
 */
public class AddProductCodeApi {

    public static String url;
    public static final String ACTION_ADD_PRODUCT_CODE = "/Service.do?method=check_pd_code_s";
    public static final int API_ADD_PRODUCT_CODE = 2;//自动添加产品码

    /**
     * url
     *
     * @return
     */
    public static String getAddProductCode() {
        url = String.format(ACTION_ADD_PRODUCT_CODE);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url);
        return urlBuffer.toString();
    }
}
