package com.zycreview.system.api;

import com.zycreview.system.utils.ConfigUtil;
import com.zycreview.system.utils.StringUtil;

/**
 *网点查询接口
 */
public class DotSearchApi {
    public static String url;
    public static final String ACTION_ENTP_SEARCH = "/service/WebService.do?method=searchEntp";
    public static final String ACTION_DOT_SEARCH = "/service/WebService.do?method=searchWd";
    public static final int API_DOT_SEARCH = 2;//网点查询

    /**
     * 获取拼成企业列表数据
     * @return
     */
    public static String getEntpSearchList(String curpage,String name,String type) {
        url = String.format(ACTION_ENTP_SEARCH);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP);
        urlBuffer.append(url).append("&curpage=").append(curpage);
        if(!StringUtil.isEmpty(name)){
            urlBuffer.append("&bis_name_like=").append(name);
        }
        if(!StringUtil.isEmpty(type)){
            urlBuffer.append("&type_name=").append(type);
        }
        return urlBuffer.toString();
    }

    /**
     * 获取拼成企业列表数据
     * @return
     */
    public static String getDotSearchList(String curpage,String name) {
        url = String.format(ACTION_DOT_SEARCH);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP);
        urlBuffer.append(url).append("&curpage=").append(curpage);
        if(!StringUtil.isEmpty(name)){
            urlBuffer.append("&bis_name_like=").append(name);
        }
        return urlBuffer.toString();
    }

}
