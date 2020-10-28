package com.zycreview.system.api;


import com.zycreview.system.utils.ConfigUtil;

/**
 * 提交收获管理信息api
 *
 */
public class HarvestManageApi {

	public static String url;
	public static final String ACTION_HARVESTMANAGE = "/admin/Gain.do?method=view&mobileLogin=true";
	public static final int API_HARVESTMANAGE = 1;//收获管理api
	public static final String ACTION_HARVESTMANAGE_H = "/admin/Gain.do?method=view&mobileLogin=true";
	public static final int API_HARVESTMANAGE_H = 3;//收获历史管理api
	public static final String ACTION_HARVESTMANAGE_SAVE = "/admin/Gain.do?method=save&mobileLogin=true";
	public static final int API_HARVESTMANAGE_SAVE = 2;//收获保存api

	/**
	 * 获得收获管理url
	 *
	 * @return
	 */
	public static String getHarvestManageUrl(String id,String key)
	{
		url = String.format(ACTION_HARVESTMANAGE);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url).append("&id=").append(id).append("&key=").append(key);
		return urlBuffer.toString();
	}

	/**
	 * 获得收获管理url
	 *
	 * @return
	 */
	public static String getHarvestManageHistoryUrl(String id,String key)
	{
		url = String.format(ACTION_HARVESTMANAGE_H);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url).append("&id=").append(id).append("&key=").append(key);
		return urlBuffer.toString();
	}


	/**
	 * 提交收获管理url
	 *
	 * @return
	 */
	public static String getHarvestManageSaveUrl(String id,String key,String receive_man,String receive_date,String drugs_code,String receive_weight,String job_no,String job_no_in, String plan_no, String plan_state)
	{
		url = String.format(ACTION_HARVESTMANAGE_SAVE);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url).append("&id=").append(id).append("&key=").append(key);
		urlBuffer.append("&receive_man=").append(receive_man).append("&receive_date=").append(receive_date);
		urlBuffer.append("&receive_weight=").append(receive_weight).append("&drugs_code=").append(drugs_code);
		urlBuffer.append("&job_no_in=").append(job_no_in).append("&job_no=").append(job_no);
		urlBuffer.append("&plan_no=").append(plan_no).append("&plan_state=").append(plan_state);
		return urlBuffer.toString();
	}

}
