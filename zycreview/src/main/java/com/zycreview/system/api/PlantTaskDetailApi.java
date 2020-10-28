package com.zycreview.system.api;


import com.zycreview.system.utils.ConfigUtil;

/**
 * 种植任务详情api
 *
 */
public class PlantTaskDetailApi {

	public static String url;
	public static final String ACTION_PLANTTASK_DETAIL = "/admin/Plants.do?method=view&mod_id=5002000100&mobileLogin=true";
	public static final int API_PLANTTASK_DETAIL = 1;//查询种植任务api
	public static final String ACTION_PLANTHISTORY_DETAIL = "/admin/Gain.do?method=view1&mod_id=5002000100&mobileLogin=true";
	public static final int API_PLANTHISTORY_DETAIL = 2;//查询种植任务api

	/**
	 *
	 * @return
	 */
	public static String getPlantTaskDetailUrl(String id,String key) {
		url = String.format(ACTION_PLANTTASK_DETAIL);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url).append("&id=").append(id).append("&key=").append(key);
		return urlBuffer.toString();
	}

	/**
	 *
	 * @return
	 */
	public static String getPlantHistoryDetailUrl(String id,String key) {
		url = String.format(ACTION_PLANTHISTORY_DETAIL);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url).append("&id=").append(id).append("&key=").append(key);
		return urlBuffer.toString();
	}

}
