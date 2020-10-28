package com.fuping.system.api;


import com.fuping.system.utils.ConfigUtil;

/**
 * 用户信息api
 *
 */
public class MemberApi {

	public static String url;
	public static final String ACTION_MEMBER = "/Service.do?method=member_index";
	public static final int API_MEMBER = 1;//会员
	public static final String ACTION_MEMBER_POINT = "/Service.do?method=getUserScoreRecordList&op=user_score_record_list";
	public static final int API_MEMBER_POINT = 2;//会员积分信息

	/**
	 * 会员数据
	 *
	 * @return
	 */
	public static String getMemberInfoUrl()
	{
		url = String.format(ACTION_MEMBER);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}

	/**
	 * 会员积分信息
	 *
	 * @return
	 */
	public static String getMemberPointUrl()
	{
		url = String.format(ACTION_MEMBER_POINT);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}
}
