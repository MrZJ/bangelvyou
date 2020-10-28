package com.zycreview.system.api;


import com.zycreview.system.utils.ConfigUtil;

/**
 * 注册api
 *
 */
public class RegisterApi {

	public static String url;
	public static final String ACTION_REGISTER = "/service/WebService.do?method=register";
	public static final int API_REGISTER = 1;//注册
	public static final String ACTION_MODIFY_PWD = "/Service.do?method=updatePassword&op=updatePassword";
	public static final int API_UPDATE_PWD = 2;//修改密码
	public static final String ACTION_UPDATE_PHOTO = "/uploaderForApp.do";
	public static final String ACTION_GET_ENTPTYPE = "/service/WebService.do?method=getBaseDataList&data_type=1";
	public static final String ACTION_GET_COM_TYPE = "/service/WebService.do?method=getEntpNatureTypeist";
	public static final int API_GET_ENTPTYPE = 3;//获取企业类型
	public static final int API_GET_COM_TYPE = 4;//获取企业类型

	/**
	 * 注册数据
	 *
	 * @return
	 */
	public static String getRegisterUrl()
	{
		url = String.format(ACTION_REGISTER);
		return new StringBuffer(ConfigUtil.HTTP).append(url).toString();
	}

	/**
	 * 上传图片
	 *
	 * @return
	 */
	public static String getActionUpdatePhotoUrl()
	{
		url = String.format(ACTION_UPDATE_PHOTO);
		return new StringBuffer(ConfigUtil.HTTP).append(url).toString();
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
	 * 获取修改密码数据
	 *
	 * @return
	 */
	public static String getEntpTypeUrl()
	{
		url = String.format(ACTION_GET_ENTPTYPE);
		return new StringBuffer(ConfigUtil.HTTP).append(url).toString();
	}
	public static String getComTypeUrl()
	{
		url = String.format(ACTION_GET_COM_TYPE);
		return new StringBuffer(ConfigUtil.HTTP).append(url).toString();
	}
}
