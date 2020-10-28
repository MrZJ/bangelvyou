package com.basulvyou.system.api;


import com.basulvyou.system.util.ConfigUtil;

/**
 * 购物车api
 *
 */
public class CartApi {

	public static String url;
	public static final String ACTION_GET_CART = "?act=member_cart&op=cart_list";
	public static final int API_GET_CART = 1;//购物车
	public static final String ACTION_GET_CART_NUM = "?act=member_cart&op=cart_edit_quantity";
	public static final int API_GET_CART_NUM = 2;//购物车商品数量
	public static final String ACTION_GET_ADD_CART = "/Service.do?method=addCartInfo&op=addCartInfo";
	public static final int API_GET_ADD_CART = 3;//添加购物车
	public static final String ACTION_GET_DEL_CART = "?act=member_cart&op=cart_del";
	public static final int API_GET_DEL_CART = 4;//删除购物车
	
	/**
	 * 获取首页广告数据
	 * 
	 * @return
	 */
	public static String getCartUrl(){
		url = String.format(ACTION_GET_CART);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}
	
	/**
	 * 获取购物车商品数量
	 * 
	 * @return
	 */
	public static String getCartNumUrl(){
		url = String.format(ACTION_GET_CART_NUM);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}
	
	/**
	 * 获取添加购物车
	 * 
	 * @return
	 */
	public static String getAddCartUrl(){
		url = String.format(ACTION_GET_ADD_CART);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}
	
	/**
	 * 获取删除购物车
	 * 
	 * @return
	 */
	public static String getDelCartUrl(){
		url = String.format(ACTION_GET_DEL_CART);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}
}

