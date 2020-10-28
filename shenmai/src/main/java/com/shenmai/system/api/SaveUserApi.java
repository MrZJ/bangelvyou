package com.shenmai.system.api;


import com.shenmai.system.utlis.ConfigUtil;

/**
 * 保存并上传用户信息接口
 *
 */
public class SaveUserApi {

	public static String url;
	public static final String ACTION_SAVE_USER = "/Service.do?method=resetUser";
	public static final int API_SAVE_USER = 1;//保存并上传用户信息
	public static final String ACTION_UPDATE_ICON = "/uploaderForApp.do";


	/**
	 * 获取上传用户信息url
	 *
	 * @return
	 */
	public static String getSaveUserUrl()
	{
		url = String.format(ACTION_SAVE_USER);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}

	/**
	 * 获取上传用户信息url
	 *
	 * @return
	 */
	public static String getUpdateIconUrl()
	{
		url = String.format(ACTION_UPDATE_ICON);
		return new StringBuffer(ConfigUtil.HTTP).append(url).toString();
	}
}
