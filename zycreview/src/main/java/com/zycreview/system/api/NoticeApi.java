package com.zycreview.system.api;

import com.zycreview.system.utils.ConfigUtil;
import com.zycreview.system.utils.StringUtil;

/**
 *最新公告API
 */
public class NoticeApi {

    public static String url;
    public static final String ACTION_NOTICE_LIST = "/admin/NewsInfo.do?mod_id=1003003000&pub_state=1&mobileLogin=true";
    public static final int API_NOTICE_LIST = 1;//公告列表接口

    /**
     * 获取拼成列表数据
     *
     * @return
     */
    public static String getNoticeList(String curpage,String titleLike) {
        url = String.format(ACTION_NOTICE_LIST);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&curpage=").append(curpage);
        if(!StringUtil.isEmpty(titleLike)){
            urlBuffer.append("&title_like=").append(titleLike);
        }
        return urlBuffer.toString();
    }
}
