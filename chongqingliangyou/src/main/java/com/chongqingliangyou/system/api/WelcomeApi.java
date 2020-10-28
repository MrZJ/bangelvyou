package com.chongqingliangyou.system.api;


import com.chongqingliangyou.system.util.ConfigUtil;

/**
 * 欢迎界面图片接口
 *
 */
public class WelcomeApi {

	public static String url;
	public static final String ACTION_GET_WELCOME_IAMGE = "/Service.do?method=getZdyStartImage";
	public static final int API_GET_WELCOME_IMAGE = 3;


	/**
	 * 欢迎界面图片Url
	 *
	 * @return
	 */
	public static String getWelcomeImageUrl()
	{
		url = String.format(ACTION_GET_WELCOME_IAMGE);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}


}
