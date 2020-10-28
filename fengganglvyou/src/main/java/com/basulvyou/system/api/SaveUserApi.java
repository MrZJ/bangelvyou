package com.basulvyou.system.api;


import com.basulvyou.system.util.ConfigUtil;

/**
 * 保存并上传用户信息接口
 *
 */
public class SaveUserApi {

	public static String url;
	public static final String ACTION_SAVE_USER = "/Service.do?method=updateUserInfo&op=updateUserInfo";
	public static final int API_SAVE_USER = 1;//注册时获取验证码


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


}
