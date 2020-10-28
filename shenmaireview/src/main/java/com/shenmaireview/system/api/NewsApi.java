package com.shenmaireview.system.api;

import com.shenmaireview.system.utils.ConfigUtil;

/**
 * Created by Administrator on 2017/3/23.
 */

public class NewsApi {

    public static String url;
    public static final String ACTION_NEWS_LIST = "/News?";
    public static final int API_NEWS_LIST = 1;//新闻列表接口

    /**
     * 获取拼成列表数据
     *
     * @return
     */
    public static String getNewsList(String curpage,String modId,String page) {
        url = String.format(ACTION_NEWS_LIST);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("mod_id=").append(modId).
                append("&curpage=").append(curpage).append("&page=").append(page);
        return urlBuffer.toString();
    }

}
