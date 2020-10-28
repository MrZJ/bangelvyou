package com.objectreview.system.api;

import com.objectreview.system.utlis.ConfigUtil;

/**
 *上传信息接口
 */
public class CommitInfoApi {

    public static String url;
    public static final String ACTION_COMMIT_LOGISTICS = "/Service.do?method=wl_no_save";
    public static final int API_COMMIT_LOGISTICS = 3;//上传物流和备注信息接口

    /**
     * 提交物流和备注信息
     * @return
     */
    public static String commitLogisticsInfoUrl()
    {
        url = String.format(ACTION_COMMIT_LOGISTICS);
        return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
    }

}
