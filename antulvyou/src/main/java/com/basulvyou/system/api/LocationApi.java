package com.basulvyou.system.api;


import com.basulvyou.system.util.ConfigUtil;
import com.basulvyou.system.util.StringUtil;

/**
 * 当地列表api
 * Created by KevinLi on 2016/3/2.
 */
public class LocationApi {
    public static String url;
    public static final String ACTION_GET_NEWS = "/Service.do?method=culture_goods&op=culture_goods";//当地文化
    public static final String ACTION_GET_VIRTUAL = "/Service.do?method=virtual_goods&op=virtual_goods_list";
    public static final int API_GET_LOCATION_LIST = 3;//当地数据列表
    public static final String ACTION_GET_VIRTUAL_DETAIL = "/Service.do?method=getVirtualCommInfo&op=getVirtualCommInfo";
    public static final int API_GET_LOCATION_DETAIL = 2;//虚拟商品详情
    public static final String ACTION_GET_NEARBY_LIST = "/Service.do?method=getMoreVirtualCommList&op=more_virtual_comm_list";
    public static final int API_GET_NEARBY_LIST = 4;//附近商品列表
    public static final int API_GET_NEWS_LIST = 5;//新闻列表

    /**
     * 获取文化列表数据
     * 1、pager.requestPage请求页；
     * 2、pager.pageSize设置请求数量；
     * orderkey 排序的条件
     * @return
     */
    public static String getNewsUrl(String page, String curPage, String keyword,String orderkey){
        url = String.format(ACTION_GET_NEWS);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&page=")
                .append(page).append("&curpage=").append(curPage);
        if(orderkey != null && !"null".equals(orderkey)){
            urlBuffer.append("&orderbydate=").append(orderkey);
        }if(keyword != null && !"null".equals(keyword)){
            urlBuffer.append("&keyword=").append(keyword);
        }
        return urlBuffer.toString();
    }

    /**
     * 获取虚拟商品列表数据
     * 请求参数
     * key 排序方式 1-销量 2-浏览量 3-时间 4-收藏量 5距离
     * order 排序方式 1-升序 2-降序
     * page 每页数量
     * curpage 当前页码
     * gc_id 分类编号
     * appkey 搜索app标签
     * keyword 搜索关键字
     * 一级分类ID gc_id和keyword和appkey三选一不能同时出现
     * store_id 店铺id
     * user_area_id:用户区域
     * isNew 是否是新商品
     * p_index 区域代码
     * @return
     */
    public static String getVirtualShopListUrl(String key, String page,
                                        String curpage, String gc_id, String keyword, String appkey,
                                        String order, String store_id, String isNew, String pIndex) {
        url = String.format(ACTION_GET_VIRTUAL);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&page=")
                .append(page).append("&curpage=").append(curpage);
        if (!StringUtil.isEmpty(key)) {
            urlBuffer.append("&key=").append(key);
        }
        if (!StringUtil.isEmpty(order)) {
            urlBuffer.append("&order=").append(order);
        }
        if (store_id != null && !"null".equals(store_id)) {
            urlBuffer.append("&store_id=").append(store_id);
        }
        if (isNew != null && !"null".equals(isNew)) {
            urlBuffer.append("&isNew=").append(isNew);
        }
        if (appkey != null && !"null".equals(appkey)) {
            urlBuffer.append("&appkey=").append(appkey);
        }
        if (gc_id != null && !"null".equals(gc_id)) {
            urlBuffer.append("&gc_id=").append(gc_id);
        }
        if (keyword != null && !"null".equals(keyword)) {
            urlBuffer.append("&keyword=").append(keyword);
        }
        if (pIndex != null && !"null".equals(pIndex)) {
            urlBuffer.append("&p_index=").append(pIndex);
        }
        return urlBuffer.toString();
    }

    /**
     * 获取虚拟商品详情数据
     * comm_id:商品id
     * @return
     */
    public static String getVirtualShopDetailUrl(String comm_id, String udid,String latlong){
        url = String.format(ACTION_GET_VIRTUAL_DETAIL);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&comm_id=").append(comm_id)
                .append("&udid=").append(udid);
        if(!StringUtil.isEmpty(latlong)){
            urlBuffer.append("&addr_latlng=").append(latlong);
        }
        return urlBuffer.toString();
    }

    /**
     *获取附近相关商品列表
     */
    public static String getNearbyMoreList(){
        url = String.format(ACTION_GET_NEARBY_LIST);
        return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
    }
}
