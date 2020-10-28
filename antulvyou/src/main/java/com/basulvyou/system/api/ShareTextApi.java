package com.basulvyou.system.api;


import com.basulvyou.system.util.ConfigUtil;

/**
 * 分享内容接口
 *
 */
public class ShareTextApi {

	public static String url;
	public static final String ACTION_SHARE_TEXT = "/Service.do?method=saveFriendPublishContent&op=saveFriendPublishContent";
	public static final int API_SHARE_TEXT = 1;//分享时的api
	public static final String ACTION_GET_SHARE_LIST = "/Service.do?method=friend_publish_content_list&op=friend_publish_content_list";
	public static final int API_GET_SHARE_LIST = 3;//首页分享列表数据
	public static final String ACTION_GET_SHARE_COMMENT_LIST = "/Service.do?method=friend_publish_comment_list&op=friend_publish_comment_list&id=";
	public static final int API_GET_SHARE_COMMENT_LIST = 4;//分享评论列表数据
	public static final String ACTION_GET_SHARE_SEND_COMMENT = "/Service.do?method=saveFriendComment&op=saveFriendComment";
	public static final int API_GET_SHARE_SEND_COMMENT = 5;//分享发表评论接口
	public static final String ACTION_GET_SHARE_ZAN = "/Service.do?method=saveFriendPraise&op=saveFriendPraise&type=1";
	public static final int API_GET_SHARE_ZAN = 6;//分享点赞接口


	/**
	 * 获取分享信息的url
	 *
	 * @return
	 */
	public static String getShareTextUrl()
	{
		url = String.format(ACTION_SHARE_TEXT);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}

	/**
	 * 获取首页热门分享数据列表
	 * @param page
	 * @param curpage
	 * @return
	 */
	public static String getHotShareListUrl(String page, String curpage){
		url = String.format(ACTION_GET_SHARE_LIST);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url).append("&page=").append(page)
				.append("&curpage=").append(curpage);
		return  urlBuffer.toString();
	}

	/**
	 * 获取分享评论列表数据
	 * @param id
	 * @return
	 */
	public static String getHotShareCommentListlUrl(String id,String page, String curpage){
		url = String.format(ACTION_GET_SHARE_COMMENT_LIST);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url).append(id).append("&page=").append(page)
				.append("&curpage=").append(curpage);
		return  urlBuffer.toString();
	}

	/**
	 * 发表分享评论接口
	 * @return
	 */
	public static String getHotShareSendCommentUrl(){
		url = String.format(ACTION_GET_SHARE_SEND_COMMENT);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url);
		return  urlBuffer.toString();
	}

	/**
	 * 电子分享
	 * @param key 用户id
	 * @param link_id 被点赞的内容id
	 * @return
	 */
	public static String getZanShareUrl(String key,String link_id){
		url = String.format(ACTION_GET_SHARE_ZAN);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url).append("&key=").append(key)
				.append("&link_id=").append(link_id);
		return  urlBuffer.toString();
	}


}
