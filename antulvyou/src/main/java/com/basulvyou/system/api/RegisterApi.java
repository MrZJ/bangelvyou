package com.basulvyou.system.api;


import com.basulvyou.system.util.ConfigUtil;

/**
 * 注册api
 *
 */
public class RegisterApi {

	public static String url;
	public static final String ACTION_REGISTER = "/Service.do?method=register";
	public static final int API_REGISTER = 1;//注册
	public static final String ACTION_MODIFY_PWD = "/Service.do?method=updatePassword&op=updatePassword";
	public static final int API_UPDATE_PWD = 2;//修改密码
	
	/**
	 * 注册数据
	 * 
	 * @return
	 */
	public static String getRegisterUrl()
	{
		url = String.format(ACTION_REGISTER);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}

	/**
	 * 获取修改密码数据
	 *
	 * @return
	 */
	public static String getUpdatePwdUrl()
	{
		url = String.format(ACTION_MODIFY_PWD);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}
}
