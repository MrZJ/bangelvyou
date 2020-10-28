package com.shenmailogistics.system.api;


import com.shenmailogistics.system.utils.ConfigUtil;

/**
 * 登录api
 *
 */
public class LoginApi {

	public static String url;
	public static final String ACTION_LOGIN = "/Service.do?method=login";
	public static final int API_LOGIN = 1;//登录
	public static final String ACTION_REVISE_PASSWORD = "/admin/Profile.do?method=saveForMobile&mobileLogin=true";
	public static final int API_REVISE_PASSWORD = 2;//修改密码
	public static final String ACTION_VERIFICATION_LOGIN = "/Service.do?method=validateUserToken";
	public static final int API_VERIFICATION_LOGIN  = 4;//用户验证登录

	/**
	 * 登录数据
	 * @return
	 */
	public static String getLoginUrl()
	{
		url = String.format(ACTION_LOGIN);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}

	/**
	 * 第三方账号注册数据
	 * @return
	 */
	public static String getVerificationLoginUrl()
	{
		url = String.format(ACTION_VERIFICATION_LOGIN);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}

	/**
	 * 修改密码数据
	 * @return
	 */
	public static String getRevisePassWUrl()
	{
		url = String.format(ACTION_REVISE_PASSWORD);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}
}
