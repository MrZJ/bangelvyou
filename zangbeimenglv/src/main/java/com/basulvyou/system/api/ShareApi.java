package com.basulvyou.system.api;

import com.basulvyou.system.util.ConfigUtil;

/**
 * 问吧分享接口
 */
public class ShareApi {
    public static String url;
    public static final String ACTION_SHARE_CONTENT = "/Service.do?method=share&id=";
    public static final int API_SHARE_CONTENT = 3;//获取分享内容


    public static String getCheckTokenUrl(String detailId,String type){
        url = String.format(ACTION_SHARE_CONTENT);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        return urlBuffer.append(url).append(detailId).append("&type=").append(type).toString();
    }
}
