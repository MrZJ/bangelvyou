package com.objectreview.system.api;

import com.objectreview.system.utlis.ConfigUtil;

/**
 * 扫码确认接口
 */
public class ComfirmApi {

    public static String url;
    public static final String ACTION_COMFIRM = "/webservice/Service.do?method=scan";
    public static final int API_COMFIRM = 1;//物流信息

    /**
     * 物流数据
     *
     * @return
     */
    public static String getComfirmUrl() {
        url = String.format(ACTION_COMFIRM);
        return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
    }

}
