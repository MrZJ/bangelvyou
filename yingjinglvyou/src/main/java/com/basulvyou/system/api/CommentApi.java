package com.basulvyou.system.api;


import com.basulvyou.system.util.ConfigUtil;

/**
 * 评论API
 *
 */
public class CommentApi {

	public static String url;
	public static final String ACTION_COMMENT = "/Service.do?method=saveCommentInfo&op=saveCommentInfo";
	public static final int API_COMMENT = 1;//发表评论
	public static final String ACTION_COMMENT_LIST = "/Service.do?method=getMyCommentInfo";
	public static final int API_COMMENT_LIST = 2;//评论列表
	public static final String ACTION_GOODS_COMMENT = "/Service.do?method=getCommentInfo";
	public static final int API_GOODS_COMMENT = 3;//商品评论列表
	public static final String ACTION_VIRTUAL_COMMENT="/Service.do?method=saveVirtualCommentInfo&op=saveVirtualCommentInfo";
	public static final int API_VIRTUAL_COMMENT = 4;//发表虚拟商品评论

	/**
	 * 获取评论网络url
	 * @return
	 */
	public static String getCommentUrl(){
		url=String.format(ACTION_COMMENT);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}

	/**
	 * 我的评论列表
	 * @return
	 */
	public static String commentListUrl(){
		url=String.format(ACTION_COMMENT_LIST);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}

	/**
	 * 商品评论列表
	 * @return
	 */
	public static String goodsCommentListUrl(){
		url=String.format(ACTION_GOODS_COMMENT);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}

	/**
	 * 当地商品发表评论URL
	 */
	public static String saveVirtualCommentUrl(){
		url=String.format(ACTION_VIRTUAL_COMMENT);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}
	
}
