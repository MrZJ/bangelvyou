package com.objectreview.system.api;


import com.objectreview.system.utlis.ConfigUtil;

/**
 * 登录api
 *
 */
public class LoginApi {

	public static String url;
	public static final String ACTION_LOGIN = "/Service.do?method=login";
	public static final int API_LOGIN = 1;//登录

	/**
	 * 登录数据
	 * 
	 * @return
	 */
	public static String getLoginUrl()
	{
		url = String.format(ACTION_LOGIN);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}

}
