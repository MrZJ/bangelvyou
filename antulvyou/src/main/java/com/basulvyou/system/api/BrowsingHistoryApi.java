package com.basulvyou.system.api;


import com.basulvyou.system.util.ConfigUtil;

/**
 * 浏览历史api
 * Created by KevinLi on 2016/3/18.
 */
public class BrowsingHistoryApi {
    public static String url;
    public static final String ACTION_GET_HISTORY_LIST = "/Service.do?method=browsing_history_goods&op=browsing_history_goods";
    public static final int API_GET_HISTORY_LIST = 1;//浏览历史列表
    public static final String ACTION_GET_DELETE_HISTORY = "/Service.do?method=delBrowsingHistory";
    public static final int API_GET_DELETE_HISTORY = 2;//删除浏览历史

    /**
     * 获取收藏列表
     * @return
     */
    public static String getBrowsingHistoryListUrl(String page, String curPage, String phoneIMEI){
        url = String.format(ACTION_GET_HISTORY_LIST);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&curpage=")
                .append(curPage).append("&page=").append(page)
                .append("&udid=").append(phoneIMEI);
        return urlBuffer.toString();
    }

    /**
     * 获取删除收藏地址
     *
     * @return
     */
    public static String getDeleteHistoryUrl(){
        url = String.format(ACTION_GET_DELETE_HISTORY);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
    }
}
