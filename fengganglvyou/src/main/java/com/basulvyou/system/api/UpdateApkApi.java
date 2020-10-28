package com.basulvyou.system.api;


import com.basulvyou.system.util.ConfigUtil;

/**
 * 更新Apk的api
 *
 */
public class UpdateApkApi {

	public static String url;
	public static final String ACTION_UPDATE = "/Service.do?method=getVersionInfo";
	public static final int API_UPDATE = 1;//更新Apk

	/**
	 * 登录数据
	 *
	 * @return
	 */
	public static String getUpdateUrl()
	{
		url = String.format(ACTION_UPDATE);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}

}
