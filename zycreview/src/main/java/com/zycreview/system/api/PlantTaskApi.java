package com.zycreview.system.api;


import com.zycreview.system.utils.ConfigUtil;
import com.zycreview.system.utils.StringUtil;

/**
 * 查询种植任务api
 *
 */
public class PlantTaskApi {

	public static String url;
	public static final String ACTION_PLANTTASK = "/admin/Plants.do?mod_id=5002000100";
	public static final int API_PLANTTASK = 1;//查询种植任务api
	public static final String ACTION_PLANTHISTORY = "/admin/History.do?mod_id=5002000100";
	public static final int API_PLANTHISTORY = 2;//查询种植历史api
	public static final String ACTION_PLANTTASK_DETELE = "/admin/Plants.do?method=delete&mod_id=5002000100&mobileLogin=true";
	public static final int API_PLANTHISTORY_DETELE = 3;//删除
	/**
	 * 种植任务搜索接口
	 *
	 * @return
	 */
	public static String getPlantTaskUrl(String curpage,String title_like,String st_pub_date,String en_pub_date,String key) {
		url = String.format(ACTION_PLANTTASK);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url).append("&curpage=").append(curpage).append("&page=6").append("&mobileLogin=true");
		urlBuffer.append("&key=").append(key);
		if(!StringUtil.isEmpty(title_like)) {
			urlBuffer.append("&title_like=").append(title_like);
		}
		if (!st_pub_date.equals("年/月/日")) {
			urlBuffer.append("&st_pub_date=").append(st_pub_date);
		}
		if (!en_pub_date.equals("年/月/日")) {
			urlBuffer.append("&en_pub_date=").append(en_pub_date);
		}
		return urlBuffer.toString();
	}

	/**
	 * 种植任务搜索接口
	 *
	 * @return
	 */
	public static String getPlantHistoryUrl(String curpage,String title_like,String st_pub_date,String en_pub_date,String key) {
		url = String.format(ACTION_PLANTHISTORY);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url).append("&curpage=").append(curpage).append("&page=6").append("&mobileLogin=true");
		urlBuffer.append("&key=").append(key);
		if(!StringUtil.isEmpty(title_like)) {
			urlBuffer.append("&title_like=").append(title_like);
		}
		if (!st_pub_date.equals("年/月/日")) {
			urlBuffer.append("&st_pub_date=").append(st_pub_date);
		}
		if (!en_pub_date.equals("年/月/日")) {
			urlBuffer.append("&en_pub_date=").append(en_pub_date);
		}
		return urlBuffer.toString();
	}

	/**
	 * 种植任务list删除接口
	 * @param id
	 * @param key
     * @return
     */
	public static String getPlantTaskDeteleUrl(String id,String key) {
		url = String.format(ACTION_PLANTTASK_DETELE);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url).append("&id=").append(id).append("&key=").append(key);
		return urlBuffer.toString();
	}
}
