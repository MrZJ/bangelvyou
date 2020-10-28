package com.shayangfupin.system.api;


import com.shayangfupin.system.utlis.ConfigUtil;
import com.shayangfupin.system.utlis.StringUtil;

/**
 *最新公告API
 */
public class NoticeApi {

    public static String url;
    public static final String ACTION_NOTICE_LIST = "/WebService.do?method=getNewsInfoList";
    public static final int API_NOTICE_LIST = 2;//公告列表接口

    /**
     * 获取拼成列表数据
     *
     * @return
     */
    public static String getNoticeList(String curpage,String modId,String titleLike) {
        url = String.format(ACTION_NOTICE_LIST);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&curpage=").append(curpage);
        if(!StringUtil.isEmpty(modId)){
            urlBuffer.append("&mod_id=").append(modId);
        }
        if(!StringUtil.isEmpty(titleLike)){
            urlBuffer.append("&title_like=").append(titleLike);
        }
        return urlBuffer.toString();
    }
}