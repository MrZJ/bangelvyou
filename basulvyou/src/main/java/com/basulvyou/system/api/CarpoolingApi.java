package com.basulvyou.system.api;

import com.basulvyou.system.util.ConfigUtil;
import com.basulvyou.system.util.StringUtil;

/**
 * 拼车接口
 */
public class CarpoolingApi {

    public static String url;
    public static final String ACTION_GET_CARPOOLING_INFO_LIST = "/Service.do?method=virtual_pc_goods&op=virtual_pc_goods_list";
    public static final int API_GET_CARPOOLING_INFO_LIST = 1;//拼车信息列表
    public static final String ACTION_SEND_CARPOOLING_INFO = "/Service.do?method=saveCommInfo&op=saveCommInfo";
    public static final int API_SEND_CARPOOLING_INFO = 2;//发布拼车信息
    public static final String ACTION_GET_CARPOOLING_INFO_DETAIL = "/Service.do?method=addCartInfo&op=addCartInfo";
    public static final int API_GET_CARPOOLING_INFO_DETAIL = 3;//拼车信息详情
    public static final String ACTION_JOIN_CAPOOLING = "/Service.do?method=virtual_order_info&op=virtual_order_info";
    public static final int API_JOIN_CAPOOLING = 4;//参加拼车
    public static final String ACTION_CHANGE_CAPOOLING = "/Service.do?method=updateVirtualOrderState&op=updateVirtualOrderState";
    public static final int API_CHANGE_CAPOOLING = 6;//修改拼车订单状态
    public static final String ACTION_CAPOOLING_ORDER_YD_LIST = "/Service.do?method=virtual_my_order_info&op=virtual_my_order_info";
    public static final int API_CAPOOLING_ORDER_LIST = 5;//拼车、物流我预定订单列表
    public static final String ACTION_CAPOOLING_ORDER_FB_LIST = "/Service.do?method=virtual_my_order_info1&op=virtual_my_order_info1";


    /**
     * 获取拼成列表数据
     *
     * @return
     */
    public static String getCarpoolingList(String key,String page,String curpage,String type,String lnglat,String gcid) {
        url = String.format(ACTION_GET_CARPOOLING_INFO_LIST);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&key=").append(key).append("&page=").append(page)
                .append("&curpage=").append(curpage).append("&type=").append(type).append("&gc_id=").append(gcid);
        if(!StringUtil.isEmpty(lnglat)){
            urlBuffer.append("&lnglat=").append(lnglat);
        }
        return urlBuffer.toString();
    }

    /**
     * 发布拼车信息
     *
     * @return
     */
    public static String sendCarpoolingInfo() {
        url = String.format(ACTION_SEND_CARPOOLING_INFO);
        return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
    }

    /**
     * 获取拼车信息详情
     *
     * @return
     */
    public static String getCarpoolingInfoDetail() {
        url = String.format(ACTION_GET_CARPOOLING_INFO_DETAIL);
        return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
    }

    /**
     * 参加拼车
     *
     * @return
     */
    public static String joinCarpooling() {
        url = String.format(ACTION_JOIN_CAPOOLING);
        return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
    }

    /**
     * 修改状态
     * @return
     */
    public static String changeCarpoolingStatus() {
        url = String.format(ACTION_CHANGE_CAPOOLING);
        return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
    }

    /**
     * 获取我的拼车或我的物流
     *预定数据
     * @return
     */
    public static String getCarpoolingOrderYdList(String key,String page,String curpage,String orderType) {
        url = String.format(ACTION_CAPOOLING_ORDER_YD_LIST);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&key=").append(key).append("&page=").append(page)
                .append("&curpage=").append(curpage).append("&order_type=").append(orderType);
        return urlBuffer.toString();
    }

    /**
     * 获取我的拼车或我的物流
     *发布数据
     * @return
     */
    public static String getCarpoolingOrderFbList(String key,String page,String curpage,String orderType) {
        url = String.format(ACTION_CAPOOLING_ORDER_FB_LIST);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&key=").append(key).append("&page=").append(page)
                .append("&curpage=").append(curpage).append("&order_type=").append(orderType);
        return urlBuffer.toString();
    }


    /**
     * 获取拼成列表数据
     *
     * @return
     */
    public static String getAlarmRescueList(String gcid) {
        url = String.format(ACTION_GET_CARPOOLING_INFO_LIST);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&gc_id=").append(gcid);
        return urlBuffer.toString();
    }

}