package com.yishangshuma.bangelvyou.api;


import com.yishangshuma.bangelvyou.util.ConfigUtil;

/**
 * 购买api
 * @author KevinLi
 *
 */
public class BuyApi {

	public static String url;
	public static final String ACTION_GET_WEIXIN_PAY = "/Service.do?method=getOrderInfoByTradeIndexForWeiXin&op=getOrderInfoByTradeIndexForWeiXin&out_trade_no=";
	public static final int API_GET_WEIXIN_PAY = 5;//微信支付信息
	
	public static final String ACTION_GET_WEIXIN_PAY_INFO = "/Service.do?method=weixinPay&op=weixinPay&out_trade_no=";
	public static final int API_GET_WEIXIN_PAY_INFO = 6;//获取微信支付信息
	

	public static String getWeiXinPayUrl(String id){
		url = String.format(ACTION_GET_WEIXIN_PAY);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).append(id).toString();
	}
	
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
