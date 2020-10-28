package com.shenmai.system.api;

import com.shenmai.system.utlis.ConfigUtil;

/**
 * 订单信息
 */
public class BuyApi {

    public static String url;
    public static final String ACTION_GET_WEIXIN_PAY = "/Service.do?method=getOrderInfoByTradeIndexForWeiXin&out_trade_no=";
    public static final int API_GET_WEIXIN_PAY = 1;//微信支付信息

    public static String getWeiXinPayUrl(String id){
        url = String.format(ACTION_GET_WEIXIN_PAY);
        return new StringBuffer(ConfigUtil.HTTP_URL).append(url).append(id).toString();
    }

}
