package com.shenmai.system.api;


import com.shenmai.system.utlis.ConfigUtil;

/**
 * 验证码接口
 * @author KevinLi
 *
 */
public class CodeApi {

	public static String url;
	public static final String ACTION_GET_CODE = "/Service.do?method=sendMobileMessage";
	public static final int API_GET_CODE = 3;//注册时获取验证码
	public static final String ACTION_GET_FORGET_CODE = "/Service.do?method=sendMobileMessage";//忘记密码获取验证码
	public static final String ACTION_MODIFY_PWD = "/Service.do?method=resetPassword";
	public static final int API_MODIFY_PWD = 4;//修改密码
	public static final String ACTION_MODIFY_RETR_PASSWORD = "/Service.do?method=updatePassword";
	public static final int API_MODIFY_RETR_PASSWORD = 5;//找回密码

	/**
	 * 获取验证码数据
	 *
	 *  参数 type 1 注册
	 *           2 找回密码
	 *           3 绑定手机
	 * @return
	 */
	public static String getCodeUrl()
	{
		url = String.format(ACTION_GET_CODE);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}

	/**
	 * 获取忘记密码验证码数据
	 *
	 * @return
	 */
	public static String setForgetPwdUrl()
	{
		url = String.format(ACTION_GET_FORGET_CODE);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}

	/**
	 * 获取修改密码数据
	 *
	 * @return
	 */
	public static String getModifyPwdUrl()
	{
		url = String.format(ACTION_MODIFY_PWD);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}

	/**
	 * 找回密码
	 * @return
	 */
	public static String setNewPassword()
	{
		url = String.format(ACTION_MODIFY_RETR_PASSWORD);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}

}
