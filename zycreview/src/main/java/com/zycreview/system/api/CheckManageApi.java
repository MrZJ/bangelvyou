package com.zycreview.system.api;


import com.zycreview.system.utils.ConfigUtil;

/**
 * 检验管理和已检药材信息api
 *
 */
public class CheckManageApi {

	public static String url;
	public static final String ACTION_CHECKMANAGE_LIST = "/admin/PlantJobTest.do?method=list";
	public static final int API_CHECKMANAGE_LIST = 1;//检验管理列表api
	public static final String ACTION_CHECKMANAGE_CHECK = "/admin/PlantJobTest.do?method=check&mod_id=5005000100&mobileLogin=true";
	public static final int API_CHECKMANAGE_CHECK = 3;//检验管理检查api
	public static final String ACTION_CHECKMANAGE_SAVE = "/admin/PlantJobTest.do?method=save";
	public static final int API_CHECKMANAGE_SAVE = 2;//检验管理保存api
	public static final String ACTION_CHECKSEE_LIST = "/admin/CheckedQuery.do?method=list";
	public static final int API_CHECKSEE_LIST = 4;//已检药材列表api
	public static final String ACTION_CHECKSEE_CHECK = "/admin/CheckedQuery.do?method=view&mod_id=5005000200&mobileLogin=true";
	public static final int API_CHECKSEE_CHECK = 6;//已检药材详情api

	/**
	 * 检验管理列表url
	 *
	 * @return
	 */
	public static String getCheckManageListUrl()
	{
		url = String.format(ACTION_CHECKMANAGE_LIST);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url);
		return urlBuffer.toString();
	}

	/**
	 * 已检药材列表url
	 *
	 * @return
	 */
	public static String getCheckSeeListUrl()
	{
		url = String.format(ACTION_CHECKSEE_LIST);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url);
		return urlBuffer.toString();
	}

	/**
	 * 检验管理检查url
	 *
	 * @return
	 */
	public static String getCheckManageCheckUrl(String id,String key)
	{
		url = String.format(ACTION_CHECKMANAGE_CHECK);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url).append("&id=").append(id).append("&key=").append(key);
		return urlBuffer.toString();
	}


	/**
	 * 已检药材详情url
	 *
	 * @return
	 */
	public static String getCheckSeeCheckUrl(String id,String key)
	{
		url = String.format(ACTION_CHECKSEE_CHECK);
	StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url).append("&id=").append(id).append("&key=").append(key);
		return urlBuffer.toString();
	}

	/**
	 * 检验管理保存url
	 *
	 * @return
	 */
	public static String getCheckManageSaveUrl()
	{
		url = String.format(ACTION_CHECKMANAGE_SAVE);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url);
		return urlBuffer.toString();
	}

}
