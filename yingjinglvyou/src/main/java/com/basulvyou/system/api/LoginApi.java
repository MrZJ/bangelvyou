package com.basulvyou.system.api;


import com.basulvyou.system.util.ConfigUtil;

/**
 * 登录api
 *
 */
public class LoginApi {

	public static String url;
	public static final String ACTION_LOGIN = "/Service.do?method=login";
	public static final int API_LOGIN = 1;//登录
//	public static final String ACTION_THIRD_LOGIN = "?act=login&op=qqlogin";
	public static final String ACTION_THIRD_LOGIN = "/Service.do?method=OtherLoginSystem&op=OtherLoginSystem";
	public static final int API_THIRD_LOGIN = 2;//第三方登录
	public static final String ACTION_THIRD_REGISTER = "?act=login&op=qqregister";
	public static final int API_THIRD_REGISTER = 3;//第三方账号注册
	public static final String ACTION_VERIFICATION_LOGIN = "/Service.do?method=validateUserToken";
	public static final int API_VERIFICATION_LOGIN  = 4;//用户验证登录
	
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

	/**
	 * 第三方登录数据
	 * 
	 * @return
	 */
	public static String getThirdLoginUrl()
	{
		url = String.format(ACTION_THIRD_LOGIN);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}
	
	/**
	 * 第三方账号注册数据
	 * 
	 * @return
	 */
	public static String getThirdRegisterUrl()
	{
		url = String.format(ACTION_THIRD_REGISTER);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}

	/**
	 * 第三方账号注册数据
	 *
	 * @return
	 */
	public static String getVerificationLoginUrl()
	{
		url = String.format(ACTION_VERIFICATION_LOGIN);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}
}
