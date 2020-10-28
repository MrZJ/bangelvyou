package com.shenmai.system.api;


import com.shenmai.system.utlis.ConfigUtil;

/**
 * 会员api
 *
 */
public class MemberApi {

	public static String url;
	public static final String ACTION_MEMBER = "/Service.do?method=getUserInfo";
	public static final int API_MEMBER = 1;//会员

	/**
	 * 会员数据
	 *
	 * @return
	 */
	public static String getMemberUrl()
	{
		url = String.format(ACTION_MEMBER);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}
}
