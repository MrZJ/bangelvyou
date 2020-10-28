package com.basulvyou.system.api;


import com.basulvyou.system.util.ConfigUtil;

/**
 * 商品详情api
 *
 */
public class ShopDetailApi {

	public static String url;
	public static final String ACTION_GET_MY_ORDER_LIST = "/Service.do?method=getCommInfo&op=getCommInfo";
	public static final int API_GET_MY_ORDER_LIST = 2;//商品详情
	public static final String ACTION_POST_GOOD_CAN_BUY_LIST = "/Service.do?method=getCanBuyInfo&op=getCanBuyInfo";
	public static final int API_POST_GOOD_CAN_BUY_LIST = 13;//商品是否可以购买
	public static final String ACTION_POST_GOOD_INV_PRICE = "/Service.do?method=getCommInvAndPrice&op=getCommInvAndPrice";
	public static final int API_POST_GOOD_INV_PRICE = 14;//商品库存和价格
	public static final String ACTION_GET_GROUP_GOOD_DETAIL = "/Service.do?method=groupBuyDetails&op=groupBuyDetails";
	public static final int API_GET_GROUP_GOOD_DETAIL = 3;//团购商品详情

	/**
	 * 获取商品详情
	 * @return
	 */
	public static String getPushOrderListUrl(String goodsId, String udid){
		url = String.format(ACTION_GET_MY_ORDER_LIST);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url).append("&comm_id=").append(goodsId)
				.append("&udid=").append(udid);
		return urlBuffer.toString();
	}

	/**
	 * 获取商品是否可购买
	 */
	public static String getGoodCanBuyUrl(){
		url=String.format(ACTION_POST_GOOD_CAN_BUY_LIST);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url);
		return urlBuffer.toString();
	}

	/**
	 * 获取商品库存和价格
	 */
	public static String getGoodInvAndPrice(){
		url=String.format(ACTION_POST_GOOD_INV_PRICE);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url);
		return urlBuffer.toString();
	}

	/**
	 * 获取团购商品详情
	 * @return
	 */
	public static String getGroupGoodDetailUrl(String goodsId){
		url = String.format(ACTION_GET_GROUP_GOOD_DETAIL);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url).append("&group_id=").append(goodsId);
		return urlBuffer.toString();
	}
}
