package com.basulvyou.system.api;

import com.basulvyou.system.util.ConfigUtil;
import com.basulvyou.system.util.StringUtil;

import static com.basulvyou.system.ui.fragment.MyEvaluateFragment.RELEASED_EVALUATE;

/**
 * 拼车接口
 */
public class CarpoolingApi {

    public static String url;
    public static final String ACTION_GET_CARPOOLING_INFO_LIST = "/Service.do?method=virtual_pc_goods&op=virtual_pc_goods_list";
    public static final String ACTION_GET_CARPOOLING_WAIT_COMFIRM_LIST = "/Service.do?method=virtual_pc_goods_confirm&op=virtual_pc_goods_confirm";
    public static final String ACTION_GET_CARPOOLING_GRAB_ORDER_LIST = "/Service.do?method=virtual_pc_goods_grab&op=virtual_pc_goods_grab";
    public static final String ACTION_GET_CARPOOLING_ROBBED_LIST = "/Service.do?method=virtual_pc_goods_robbed&op=virtual_pc_goods_robbed";
    public static final int API_GET_CARPOOLING_INFO_LIST = 1;//拼车信息列表
    public static final String ACTION_SEND_CARPOOLING_INFO = "/Service.do?method=saveCommInfo&op=saveCommInfo";
    public static final int API_SEND_CARPOOLING_INFO = 2;//发布拼车信息
    public static final String ACTION_GET_CARPOOLING_INFO_DETAIL = "/Service.do?method=addCartInfo&op=addCartInfo";
    public static final int API_GET_CARPOOLING_INFO_DETAIL = 3;//拼车信息详情
    public static final String ACTION_JOIN_CAPOOLING = "/Service.do?method=save_pc_order_info&op=save_pc_order_info";
    public static final int API_JOIN_CAPOOLING = 4;//参加拼车
    public static final String ACTION_CHANGE_CAPOOLING = "/Service.do?method=updateVirtualOrderState&op=updateVirtualOrderState";
    public static final int API_CHANGE_CAPOOLING = 6;//修改拼车订单状态
    public static final String ACTION_CAPOOLING_ORDER_YD_LIST = "/Service.do?method=virtual_my_order_info&op=virtual_my_order_info";
    public static final int API_CAPOOLING_ORDER_LIST = 5;//拼车、物流我预定订单列表
    public static final String ACTION_CAPOOLING_ORDER_FB_LIST = "/Service.do?method=virtual_my_order_info1&op=virtual_my_order_info1";
    public static final String ACTION_CAPOOLING_ORDER_COMM_INFO = "/Service.do?method=getVirtualPcCommInfo&op=getVirtualPcCommInfo";
    public static final String ACTION_CAPOOLING_ORDER_COMM_INFO_2 = "/Service.do?method=getVirtualPcCommInfoForConfirm&op=getVirtualPcCommInfoForConfirm";
    public static final int API_CAPOOLING_ORDER_COMM_INFO = 7;//订单详情
    public static final String ACTION_CAPOOLING_ORDER_EVALUATE = "/Service.do?method=saveVirtualPcCommentInfo&op=saveVirtualPcCommentInfo";
    public static final int API_CAPOOLING_ORDER_COMM_EVALUATE = 8;//提交评论
    public static final String API_CAPOOLING_ORDER_COMMITE_EVALUATE = "/Service.do?method=virtual_pc_order_info_confirm&op=virtual_pc_order_info_confirm";
    public static final int API_CAPOOLING_ORDER_COMMITE_EVALUATE_ACTION = 9;//提交评论
    public static final String URL_MY_RELEASED_EVALUATE = "/Service.do?method=getMyCommentInfoList&op=getMyCommentInfoList";
    public static final String URL_MY_RECEIVED_EVALUATE = "/Service.do?method=getMYCommentMeInfoList&op=getMYCommentMeInfoList";
    public static final int ACTION_GET_RELEASE_EVALUATE = 10;//评论列表


    /**
     * 获取拼成列表数据
     *
     * @return
     */
    public static String getCarpoolingList(String key, String page, String curpage, String type, String lnglat, String gcid) {
        url = String.format(ACTION_GET_CARPOOLING_INFO_LIST);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&key=").append(key).append("&page=").append(page)
                .append("&curpage=").append(curpage).append("&type=").append(type).append("&gc_id=").append(gcid);
        if (!StringUtil.isEmpty(lnglat)) {
            urlBuffer.append("&lnglat=").append(lnglat);
        }
        return urlBuffer.toString();
    }

    /**
     * 根据车主还是乘客获取拼成列表数据
     *
     * @return
     */
    public static String getCarpoolingInfo(String key, String page, String curpage, String type, String lnglat, String gcid, String comm_type) {
        if ("20890".equals(type)) {
            url = String.format(ACTION_GET_CARPOOLING_INFO_LIST);
        } else if ("20888".equals(type)) {
            url = String.format(ACTION_GET_CARPOOLING_WAIT_COMFIRM_LIST);
        } else if ("20894".equals(type)) {
            url = String.format(ACTION_GET_CARPOOLING_GRAB_ORDER_LIST);
        } else if ("20892".equals(type)) {
            url = String.format(ACTION_GET_CARPOOLING_ROBBED_LIST);
        }
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&key=").append(key).append("&page=").append(page)
                .append("&curpage=").append(curpage).append("&type=").append(type).append("&gc_id=").append(gcid).append("&comm_type=").append(comm_type);
        if (!StringUtil.isEmpty(lnglat)) {
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
     * 确认拼车信息拼车信息
     *
     * @return
     */
    public static String comfirmOrderInfo(String key, String comm_id, String order_id) {
        url = String.format(API_CAPOOLING_ORDER_COMMITE_EVALUATE);
        StringBuilder urlBuider = new StringBuilder(ConfigUtil.HTTP_URL);
        urlBuider.append(url).append("&key=").append(key).append("&comm_id=").append(comm_id).append("&order_id=").append(order_id);
        return urlBuider.toString();
    }

    /**
     * 获取订单信息详情
     *
     * @param comm_id
     * @return
     */
    public static String getOrderCommInfo(String comm_id, int pos, String key, String comm_type) {
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        if (pos == 1) {
            url = String.format(ACTION_CAPOOLING_ORDER_COMM_INFO_2);
        } else {
            url = String.format(ACTION_CAPOOLING_ORDER_COMM_INFO);
        }
        urlBuffer.append(url).append("&uuid=").append("1230021").append("&comm_id=").append(comm_id).append("&key=").append(key).append("&comm_type=").append(comm_type);
        return urlBuffer.toString();
    }

    public static String getCommitEvaluate(String comm_id, String key, String comm_experience, String comm_score, String order_id, String comm_type) {
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        url = String.format(ACTION_CAPOOLING_ORDER_EVALUATE);
        urlBuffer.append(url).append("&comm_id=").append(comm_id).append("&key=").append(key).append("&comm_experience=").append(comm_experience)
                .append("&comm_score=").append(comm_score).append("&order_id=").append(order_id).append("&comm_type=").append(comm_type);
        return urlBuffer.toString();
    }

    public static String getReleasedEvaluateList(String comm_score, String key, String curpage, String page, int pos) {
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        if (pos == RELEASED_EVALUATE) {
            url = String.format(URL_MY_RELEASED_EVALUATE);
        } else {
            url = String.format(URL_MY_RECEIVED_EVALUATE);
        }
        urlBuffer.append(url).append("&comm_score=").append(comm_score).append("&key=").append(key).append("&curpage=").append(curpage).append("&page=").append(page);
        return urlBuffer.toString();
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
     *
     * @return
     */
    public static String changeCarpoolingStatus() {
        url = String.format(ACTION_CHANGE_CAPOOLING);
        return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
    }

    /**
     * 获取我的拼车或我的物流
     * 预定数据
     *
     * @return
     */
    public static String getCarpoolingOrderYdList(String key, String page, String curpage, String orderType) {
        url = String.format(ACTION_CAPOOLING_ORDER_YD_LIST);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&key=").append(key).append("&page=").append(page)
                .append("&curpage=").append(curpage).append("&order_type=").append(orderType);
        return urlBuffer.toString();
    }

    /**
     * 获取我的拼车或我的物流
     * 发布数据
     *
     * @return
     */
    public static String getCarpoolingOrderFbList(String key, String page, String curpage, String orderType) {
        url = String.format(ACTION_CAPOOLING_ORDER_FB_LIST);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        if ("101".equals(orderType)){
            orderType = "21056";
        } else {
            orderType = "21058";
        }
        urlBuffer.append(url).append("&key=").append(key).append("&page=").append(page)
                .append("&curpage=").append(curpage).append("&gc_id=").append(orderType);
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