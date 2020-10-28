package com.basulvyou.system.api;

import com.basulvyou.system.util.ConfigUtil;

/**
 * 微信支付API
 */
public class PayApi {

    public static String url;
    public static final String ACTION_GET_WEIXIN_PAY_INFO = "/Service.do?method=weixinPay&op=weixinPay&out_trade_no=";
    public static final int API_GET_WEIXIN_PAY_INFO = 6;//获取微信支付信息


    /**
     * 获取微信支付信息
     * @param id
     * @return
     */
    public static String getWeiXinPayInfo(String id){
        url = String.format(ACTION_GET_WEIXIN_PAY_INFO);
        return new StringBuffer(ConfigUtil.HTTP_URL).append(url).append(id).toString();
    }

}
