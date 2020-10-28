package com.shenmailogistics.system.api;

import com.shenmailogistics.system.utils.ConfigUtil;

/**
 * Created by Administrator on 2017/4/5.
 */

public class UpdataLocationApi {

    public static String url;
    public static final String ACTION_UPDATA = "/Service.do?method=save_m";
    public static final int API_UPDATA = 1;//上传地理位置信息
    public static final String ACTION_GET_LIST = "/Service.do?method=get_list";
    public static final int API_GET_LIST = 2;//获取路径列表
    public static final String ACTION_GET_DETIAL = "/Service.do?method=get_detail";
    public static final int API_GET_DETIAL = 3;//获取路径详情

    /**
     * 上传数据
     * @return
     */
    public static String updataUrl()
    {
        url = String.format(ACTION_UPDATA);
        return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
    }

    /**
     * 获取路径列表数据
     * @return
     */
    public static String getTraceList(String curpage,String key,String page) {
        url = String.format(ACTION_GET_LIST);
        StringBuffer traceBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        traceBuffer.append(url).append("&user_id=").append(key).append("&curpage=").append(curpage).append("&page=").append(page);
        return traceBuffer.toString();
    }

    /**
     * 获取路径列表数据
     * @return
     */
    public static String getTraceDetail(String id) {
        url = String.format(ACTION_GET_DETIAL);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&app_record_id=").append(id);
        return urlBuffer.toString();
    }
}
