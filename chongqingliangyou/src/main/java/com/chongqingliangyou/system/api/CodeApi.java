package com.chongqingliangyou.system.api;


import com.chongqingliangyou.system.util.ConfigUtil;

/**
 * 验证码接口
 *
 */
public class CodeApi {

	public static String url;
	public static final String ACTION_GET_CODE = "/Service.do?method=sendMessage&op=sendMessage";
	public static final int API_GET_CODE = 1;//注册时获取验证码


	/**
	 * 获取验证码数据
	 *
	 * @return
	 */
	public static String getCodeUrl()
	{
		url = String.format(ACTION_GET_CODE);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}


}
