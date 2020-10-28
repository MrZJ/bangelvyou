package com.yishangshuma.bangelvyou.api;


import com.yishangshuma.bangelvyou.util.ConfigUtil;

/**
 * 我的优惠券api
 *
 */
public class CouponApi {

	public static String url;
	public static final String ACTION_GET_VOUCHER_LIST = "/Service.do?method=getYhqList&op=getYhqList";
	public static final int API_GET_VOUCHER_LIST = 1;//我的优惠券列表
	
	/**
	 * 获取我的优惠券列表
	 * 
	 * @return
	 */
	public static String getCouponUrl(){
		url = String.format(ACTION_GET_VOUCHER_LIST);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}
}
