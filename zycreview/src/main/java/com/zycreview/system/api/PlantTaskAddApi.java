package com.zycreview.system.api;


import com.zycreview.system.utils.ConfigUtil;

/**
 * 提交种植任务添加信息api
 *
 */
public class PlantTaskAddApi {

	public static String url;
	public static final String ACTION_PLANTTASKADD = "/admin/Plants.do?method=save";
	public static final int API_PLANTTASKADD = 1;//种植任务添加
	public static final String ACTION_PLANTTASKADDGET = "/admin/Plants.do?method=add&mod_id=5002000100&mobileLogin=true&key=";
	public static final int API_PLANTTASKADDGET = 2;//种植任务添加第一次调用获得数据

	/**
	 * 提交种植任务添加信息url
	 *
	 * @return
	 */
	public static String getPlantTaskAddUrl()
	{
		url = String.format(ACTION_PLANTTASKADD);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}

	/**
	 * 获得种植任务添加信息url
	 *
	 * @return
	 */
	public static String getPlantTaskAddGetUrl(String key)
	{
		url = String.format(ACTION_PLANTTASKADDGET);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).append(key).toString();
	}

}
