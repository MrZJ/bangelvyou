package com.shenmai.system.api;


import com.shenmai.system.utlis.ConfigUtil;

/**
 * 登录api
 */
public class LoginApi {

    public static String url;
    public static final String ACTION_LOGIN = "/Service.do?method=login";
    public static final int API_LOGIN = 1;//登录
    public static final String ACTION_THIRD_LOGIN = "/Service.do?method=OtherLoginSystem";
    public static final int API_THIRD_LOGIN = 2;//第三方登录

    /**
     * 登录数据
     *
     * @return
     */
    public static String getLoginUrl() {
        url = String.format(ACTION_LOGIN);
        return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
    }

    /**
     * 第三方登录数据
     *
     * @return
     */
    public static String getThirdLoginUrl() {
        url = String.format(ACTION_THIRD_LOGIN);
        return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
    }

}
