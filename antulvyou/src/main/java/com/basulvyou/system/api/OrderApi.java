package com.basulvyou.system.api;


import com.basulvyou.system.util.ConfigUtil;

/**
 * 我的订单列表api
 *
 */
public class OrderApi {

	public static String url;
	public static final String ACTION_GET_MY_ORDER_LIST = "/Service.do?method=getMyOrderInfoList&op=getMyOrderInfoList";
	public static final int API_GET_MY_ORDER_LIST = 1;//我的订单列表
	public static final String ACTION_GET_CANCEL_OR_RECEIVE_ORDER = "/Service.do?method=updateOrderInfoState&op=updateOrderInfoState";
	public static final int API_GET_CANCEL_ORDER = 2;//取消订单
	public static final int API_GET_RECEIVE_ORDER = 3;//确认收货
	
	/**
	 * 获取我的订单列表
	 * 待评价:state_noeval
	 * 已取消:state_cancel
	 * 已完成:state_success
	 * @return
	 */
	public static String getMyOrderListUrl(String key, String page, String curpage, String keyId){
		url = String.format(ACTION_GET_MY_ORDER_LIST);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url).append("&key=").append(key).append("&page=").append(page)
				.append("&curpage=").append(curpage);
		if("0".endsWith(keyId)){//全部
//			urlBuffer.append("&state_type=").append(curpage);
		} else if("1".endsWith(keyId)){//待支付
			urlBuffer.append("&state_type=").append("state_new");
		} else if("2".endsWith(keyId)){//待发货
			urlBuffer.append("&state_type=").append("state_pay");
		} else if("3".endsWith(keyId)){//待收货
			urlBuffer.append("&state_type=").append("state_send");
		} else if("4".endsWith(keyId)){//待评价
			urlBuffer.append("&state_type=").append("state_noeval");
		}
		return urlBuffer.toString();
	}
	
	/**
	 * 取消订单地址
	 */
	public static String getCancelOrderUrl(){
		url = String.format(ACTION_GET_CANCEL_OR_RECEIVE_ORDER);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}
	
	/**
	 * 获取确认收货地址
	 * 
	 * @return
	 */
	public static String getReceiveOrderUrl(){
		url = String.format(ACTION_GET_CANCEL_OR_RECEIVE_ORDER);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}
	
	/**
	 * 获取推送订单列表详情
	 * 加参数&order_sn=订单号
	 * @return
	 */
	public static String getPushOrderListUrl(String key, String orderId){
		url = String.format(ACTION_GET_MY_ORDER_LIST);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url).append("&key=").append(key).append("&page=").append("1")
				.append("&curpage=").append("20").append("&order_sn=").append(orderId);
		return urlBuffer.toString();
	}
}
