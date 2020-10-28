package com.shenmai.system.api;

import com.shenmai.system.utlis.ConfigUtil;
import com.shenmai.system.utlis.StringUtil;

/**
 * 商品列表API
 */
public class GoodsApi {

    public static String url;
    public static final String ACTION_GET_SHOP_LIST = "/Service.do?method=getCommInfoList";
    public static final int API_GET_SHOP_LIST = 2;// 商品列表
    public static final int API_GET_CATE_GOODS_LIST = 3;// 分类商品列表
    public static final String ACTION_GET_SHOP_COLLECT_LIST = "/Service.do?method=myFavList";
    public static final int API_GET_SHOP_COLLECT_LIST = 4;// 商品收藏列表
    public static final String ACTION_GET_SHOP_DEL_COLLECT = "/Service.do?method=myFavDel";
    public static final int API_GET_SHOP_DEL_COLLECT = 5;// 删除商品收藏
    public static final String ACTION_ADD_GOODS_TOSHOP = "/Service.do?method=purchase";
    public static final int API_ADD_GOODS_TOSHOP = 6;// 商品加入店铺
    public static final String ACTION_GOODS_DETAIL_SHARE = "/Service.do?method=getCommInfo";
    public static final int API_GOODS_DETAIL_SHARE = 7;// 得到商品详情中的分享参数

    /**
     * 获取商品列表数据
     * 请求参数
     * key 排序方式 1-销量 2-浏览量 3-价格 4-推荐位
     * order 排序方式 1-升序 2-降序
     * page 每页数量
     * curpage 当前页码
     * gc_id 分类编号
     * appkey 搜索app标签
     * keyword 搜索关键字
     * 一级分类ID gc_id和keyword和appkey三选一不能同时出现
     * store_id 店铺id
     * isNew 是否是新商品
     * @return
     */
    public static String getGoodsListUrl(String curpage,String keyword,String order,String cateId,String needHome1) {
        url = String.format(ACTION_GET_SHOP_LIST);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&curpage=").append(curpage);
        if (!StringUtil.isEmpty(order)) {
            urlBuffer.append("&order=").append(order);
        }
        if (!StringUtil.isEmpty(keyword)) {
            urlBuffer.append("&keyword=").append(keyword);
        }
        if (!StringUtil.isEmpty(cateId)) {
            urlBuffer.append("&par_cls_id=").append(cateId);
        }
        if (!StringUtil.isEmpty(needHome1)) {
            urlBuffer.append("&needHome1=").append("true");
        }
        return urlBuffer.toString();
    }

    /**
     * 根据一级分类ID获取商品列表
     * page 每页数量
     * curpage 当前页码
     * par_gc_id 分类编号
     */
    public static String getGoodsListByClassIdUrl(String par_gc_id, String page,
                                                 String curpage){
        url = String.format(ACTION_GET_SHOP_LIST);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&par_cls_id=").append(par_gc_id).append("&curpage=").append(curpage).append("&page=").append(page);
        return urlBuffer.toString();
    }

    /**
     * 获取收藏商品列表
     * @param key
     * @param page
     * @param curpage
     * @return
     */
    public static String getCollectGoodsListUrl(String key, String page,
                                                  String curpage){
        url = String.format(ACTION_GET_SHOP_COLLECT_LIST);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&key=").append(key).append("&curpage=").append(curpage).append("&page=").append(page);
        return urlBuffer.toString();
    }

    /**
     * 删除收藏商品
     * @return
     */
    public static String delCollectGoodsUrl(){
        url = String.format(ACTION_GET_SHOP_DEL_COLLECT);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url);
        return urlBuffer.toString();
    }

    /**
     * 添加商品去商店
     * @return
     */
    public static String addGoodsToShopUrl(){
        url = String.format(ACTION_ADD_GOODS_TOSHOP);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url);
        return urlBuffer.toString();
    }

    /**
     * 得到商品详情中的分享参数
     *
     * @return
     */
    public static String getActionGoodsDetailShare(String id){
        url = String.format(ACTION_GOODS_DETAIL_SHARE);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&id=").append(id);
        return urlBuffer.toString();
    }
}
