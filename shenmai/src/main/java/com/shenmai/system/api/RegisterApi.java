package com.shenmai.system.api;


import com.shenmai.system.utlis.ConfigUtil;

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
	public static final String ACTION_MODIFY_RES_SHOP = "/Service.do?method=openShop";
	public static final int API_UPDATE_RES_SHOP = 3;//申请开店
	public static final String ACTION_SET_BANK_INFO = "/Service.do?method=bindBankInfo";
	public static final int API_SET_BANK_INFO = 4;//绑定银行卡
	public static final String ACTION_GET_BANK_INFO = "/Service.do?method=viewBankInfo&key=";
	public static final int API_GET_BANK_INFO = 5;//获取银行卡信息
	public static final String ACTION_BING_PHONE = "/Service.do?method=updateUserMobile";
	public static final int API_BING_PHONE = 6;//绑定手机号

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

	/**
	 * 申请开店
	 * @return
	 */
	public static String getResShopUrl()
	{
		url = String.format(ACTION_MODIFY_RES_SHOP);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}

	/**
	 * 设置银行卡信息
	 * @return
	 */
	public static String setBankInfoUrl()
	{
		url = String.format(ACTION_SET_BANK_INFO);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}

	/**
	 * 获取银行卡信息
	 * @return
	 */
	public static String getBankInfoUrl(String key)
	{
		url = String.format(ACTION_GET_BANK_INFO);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).append(key).toString();
	}

	/**
	 * 绑定手机号
	 * @return
	 */
	public static String getBingPhoneUrl()
	{
		url = String.format(ACTION_BING_PHONE);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}
}
