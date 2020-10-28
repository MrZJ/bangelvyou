package com.basulvyou.system.api;


import com.basulvyou.system.util.ConfigUtil;

/**
 * 关注api
 *
 */
public class FocusApi {

	public static String url;
	public static final String ACTION_GET_ADD_FOCUS = "/Service.do?method=favorites_add";
	public static final int API_GET_ADD_FOCUS = 11;//收藏
	public static final String ACTION_GET_COLLECT_LIST = "/Service.do?method=favorites_list";
	public static final int API_GET_COLLECT_LIST = 2;//收藏列表	
	public static final String ACTION_GET_DELETE_COLLECT = "/Service.do?method=favorites_del";
	public static final int API_GET_DELETE_COLLECT = 3;//删除收藏	
	public static final String ACTION_COLLECTING_STORE = "/Service.do?method=scEntpInfo&op=scEntpInfo";
	public static final int API_COLLECTING_STORE = 4;//收藏店铺	
	
	/**
	 * 获取关注地址
	 * 
	 * @return
	 */
	public static String getAddFocusUrl(){
		url = String.format(ACTION_GET_ADD_FOCUS);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}
	
	/**
	 * 获取收藏列表
	 * @return
	 */
	public static String getCollectListUrl(){
		url = String.format(ACTION_GET_COLLECT_LIST);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}
	
	/**
	 * 获取删除收藏地址
	 * 
	 * @return
	 */
	public static String getDeleteCollectUrl(){
		url = String.format(ACTION_GET_DELETE_COLLECT);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}
	
	/**
	 * 获取删除收藏地址
	 * 
	 * @return
	 */
	public static String getStoreCollectUrl(){
		url = String.format(ACTION_COLLECTING_STORE);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}
	
	
}
