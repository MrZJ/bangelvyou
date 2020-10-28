package com.basulvyou.system.api;

import com.basulvyou.system.util.ConfigUtil;

/**
 * 报警救援接口
 */
public class RescueApi {

    public static String url;
    public static final String ACTION_GET_RESCUE_LIST = "?act=member_cart&op=cart_list";
    public static final int API_GET_RESCUE_LIST = 1;//报警救援列表


    /**
     * 获取报警救援列表数据
     *
     * @return
     */
    public static String getRescueList(){
        url = String.format(ACTION_GET_RESCUE_LIST);
        return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
    }

}
