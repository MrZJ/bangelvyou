package com.yishangshuma.bangelvyou.api;


import com.yishangshuma.bangelvyou.util.ConfigUtil;

/**
 * 商品列表api
 *
 */
public class ShopListApi {

	public static String url;
	public static final String ACTION_GET_SHOP_LIST = "/Service.do?method=goods&op=goodsList";
	public static final int API_GET_SHOP_LIST = 2;//商品列表

	/**
	 * 获取商品列表数据
	 * 请求参数
	 *	key 排序方式 1-销量 2-浏览量 3-价格 4-推荐位
	 *	order 排序方式 1-升序 2-降序
	 *	page 每页数量
	 *	curpage 当前页码
	 *  gc_id 分类编号
	 *  appkey 搜索app标签
	 *  keyword 搜索关键字
	 *  gc_id和keyword和appkey三选一不能同时出现
	 * @return
	 */
	public static String getShopListUrl(String key, String page, String curpage, String gc_id
											, String keyword, String appkey, String order)
	{
		url = String.format(ACTION_GET_SHOP_LIST);
		StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
		urlBuffer.append(url).append("&key=").append(key).append("&page=").append(page)
				.append("&curpage=").append(curpage);
		if(order != null && !"null".equals(order)){
			urlBuffer.append("&order=").append(order);
		}
		if(appkey != null && !"null".equals(appkey)){
			urlBuffer.append("&appkey=").append(appkey);
		}
		if(gc_id != null && !"null".equals(gc_id)){
			urlBuffer.append("&gc_id=").append(gc_id);
		}
		if(keyword != null && !"null".equals(keyword)){
			urlBuffer.append("&keyword=").append(keyword);
		}
		return urlBuffer.toString();
	}
}
