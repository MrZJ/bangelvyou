package com.zycreview.system.api;


import com.zycreview.system.utils.ConfigUtil;

/**
 * 施肥或者农药api
 *
 */
public class FertilizerOrPesticideApi {

	public static String url;
	public static final String ACTION_F= "/admin/Ferti.do?method=list&mod_id=5002000100&page=6&mobileLogin=true";
	public static final int API_F = 1;//施肥查询
	public static final String ACTION_P= "/admin/Spray.do?method=list&mod_id=5002000100&page=6&mobileLogin=true";
	public static final int API_P = 2;//农药查询
	public static final String ACTION_F_H= "/admin/Ferti.do?method=list1&mod_id=5002000100&page=6&mobileLogin=true";
	public static final int API_F_H = 5;//施肥历史查询
	public static final String ACTION_P_H= "/admin/Spray.do?method=list1&mod_id=5002000100&page=6&mobileLogin=true";
	public static final int API_P_H = 6;//农药历史查询
	public static final String ACTION_F_SAVE= "/admin/Ferti.do?method=save";
	public static final int API_F_SAVE = 3;//施肥保存
	public static final String ACTION_P_SAVE= "/admin/Spray.do?method=save";
	public static final int API_P_SAVE = 4;//农药保存
	public static final String ACTION_F_DETELE= "/admin/Ferti.do?method=delete&mod_id=5002000100&mobileLogin=true";
	public static final int API_F_DELETE = 7;//施肥删除
	public static final String ACTION_P_DETELE= "/admin/Spray.do?method=delete&mod_id=5002000100&mobileLogin=true";
	public static final int API_P_DELETE = 8;//农药删除

	/**
	 * 施肥搜索接口
	 *
	 * @return
	 */
	public static String getFertilizerTaskUrl(String curpage,String st_pub_date,String en_pub_date,String key,String job_no_in) {
		url = String.format(ACTION_F);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url).append("&curpage=").append(curpage);
		urlBuffer.append("&key=").append(key);
		urlBuffer.append("&job_no_in=").append(job_no_in);
		if (!st_pub_date.equals("年/月/日")) {
			urlBuffer.append("&st_pub_date=").append(st_pub_date);
		}
		if (!en_pub_date.equals("年/月/日")) {
			urlBuffer.append("&en_pub_date=").append(en_pub_date);
		}
		return urlBuffer.toString();
	}

	/**
	 * 农药搜索接口
	 *
	 * @return
	 */
	public static String getPesticideTaskUrl(String curpage,String st_pub_date,String en_pub_date,String key,String job_no_in) {
		url = String.format(ACTION_P);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url).append("&curpage=").append(curpage);
		urlBuffer.append("&key=").append(key);
		urlBuffer.append("&job_no_in=").append(job_no_in);
		if (!st_pub_date.equals("年/月/日")) {
			urlBuffer.append("&st_pub_date=").append(st_pub_date);
		}
		if (!en_pub_date.equals("年/月/日")) {
			urlBuffer.append("&en_pub_date=").append(en_pub_date);
		}
		return urlBuffer.toString();
	}

	/**
	 * 施肥搜索接口
	 *
	 * @return
	 */
	public static String getFertilizerHistoryUrl(String curpage,String st_pub_date,String en_pub_date,String key,String job_no_in) {
		url = String.format(ACTION_F_H);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url).append("&curpage=").append(curpage);
		urlBuffer.append("&key=").append(key);
		urlBuffer.append("&job_no_in=").append(job_no_in);
		if (!st_pub_date.equals("年/月/日")) {
			urlBuffer.append("&st_pub_date=").append(st_pub_date);
		}
		if (!en_pub_date.equals("年/月/日")) {
			urlBuffer.append("&en_pub_date=").append(en_pub_date);
		}
		return urlBuffer.toString();
	}

	/**
	 * 农药搜索接口
	 *
	 * @return
	 */
	public static String getPesticideHistoryUrl(String curpage,String st_pub_date,String en_pub_date,String key,String job_no_in) {
		url = String.format(ACTION_P_H);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url).append("&curpage=").append(curpage);
		urlBuffer.append("&key=").append(key);
		urlBuffer.append("&job_no_in=").append(job_no_in);
		if (!st_pub_date.equals("年/月/日")) {
			urlBuffer.append("&st_pub_date=").append(st_pub_date);
		}
		if (!en_pub_date.equals("年/月/日")) {
			urlBuffer.append("&en_pub_date=").append(en_pub_date);
		}
		return urlBuffer.toString();
	}

	/**
	 * 保存施肥url
	 *
	 * @return
	 */
	public static String getFertilizerSaveTaskUrl()
	{
		url = String.format(ACTION_F_SAVE);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}

	/**
	 * 保存施肥url
	 *
	 * @return
	 */
	public static String getPesticideSaveTaskUrl()
	{
		url = String.format(ACTION_P_SAVE);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}

	/**
	 * 施肥删除接口
	 *
	 * @return
	 */
	public static String getFertilizerDeteleUrl(String id,String key) {
		url = String.format(ACTION_F_DETELE);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url);
		urlBuffer.append("&key=").append(key);
		urlBuffer.append("&id=").append(id);
		return urlBuffer.toString();
	}

	/**
	 * 农药删除接口
	 *
	 * @return
	 */
	public static String getPesticideDeteleUrl(String id,String key) {
		url = String.format(ACTION_P_DETELE);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url);
		urlBuffer.append("&key=").append(key);
		urlBuffer.append("&id=").append(id);
		return urlBuffer.toString();
	}
}
