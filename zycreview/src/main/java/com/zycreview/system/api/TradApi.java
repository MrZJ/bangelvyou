package com.zycreview.system.api;

import com.zycreview.system.utils.ConfigUtil;
import com.zycreview.system.utils.StringUtil;

/**
 * 交易单接口
 */
public class TradApi {

    public static String url;
    public static final String ACTION_TRADED_LIST = "/admin/PlantOutPut.do?mod_id=5007000100&mobileLogin=true";
    public static final int API_TRADED_LIST = 1;//交易单列表
    public static final String ACTION_TRADING_LIST = "/admin/PlantOutPut.do?method=listForOut&mod_id=5007000100&mobileLogin=true";
    public static final int API_TRADING_LIST = 2;//可添加交易单列表
    public static final String ACTION_UPDATE_TRADED = "/admin/PlantOutPut.do?method=save&mod_id=5007000100&mobileLogin=true";
    public static final int API_UPDATE_TRADED = 3;//修改交易单状态
    public static final String ACTION_ADD_TRADING = "/admin/PlantOutPut.do?method=ckSave&mod_id=5007000100&mobileLogin=true";
    public static final int API_ADD_TRADING = 4;//生成交易单
    public static final String ACTION_TRADED_DRUGS_LIST = "/admin/PlantTradeDetial.do?mobileLogin=true";
    public static final int API_TRADED_DRUGS_LIST = 5;//交易单药材列表

    /**
     * 获取交易单列表数据
     *
     * @return
     */
    public static String getTradedList(String key,String curpage,String pager,String titleLike, String startDate, String endDate) {
        url = String.format(ACTION_TRADED_LIST);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&key=").append(key);
        urlBuffer.append("&curpage=").append(curpage);
        urlBuffer.append("&page=").append(pager);
        if(!StringUtil.isEmpty(titleLike)){
            urlBuffer.append("&entp_name_like=").append(titleLike);
        }
        if(!StringUtil.isEmpty(startDate)){
            urlBuffer.append("&trade_date_min=").append(startDate);
        }
        if(!StringUtil.isEmpty(endDate)){
            urlBuffer.append("&trade_date_max=").append(endDate);
        }
        return urlBuffer.toString();
    }

    /**
     * 获取交易单列表数据
     *
     * @return
     */
    public static String getTradingList(String key,String curpage,String pager,String titleLike, String startDate, String endDate) {
        url = String.format(ACTION_TRADING_LIST);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&key=").append(key);
        urlBuffer.append("&curpage=").append(curpage);
        urlBuffer.append("&page=").append(pager);
        if(!StringUtil.isEmpty(titleLike)){
            urlBuffer.append("&drugs_name_like=").append(titleLike);
        }
        if(!StringUtil.isEmpty(startDate)){
            urlBuffer.append("&pack_date_min=").append(startDate);
        }
        if(!StringUtil.isEmpty(endDate)){
            urlBuffer.append("&pack_date_max=").append(endDate);
        }
        return urlBuffer.toString();
    }

    /**
     * 获取交易单药材列表数据
     *
     * @return
     */
    public static String getTradedDrugsList(String key,String curpage,String pager,String tradedId) {
        url = String.format(ACTION_TRADED_DRUGS_LIST);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&key=").append(key);
        urlBuffer.append("&curpage=").append(curpage);
        urlBuffer.append("&page=").append(pager);
        urlBuffer.append("&trade_id=").append(tradedId);
        return urlBuffer.toString();
    }

    /**
     * 修改交易单状态
     * @return
     */
    public static String updateTradedState() {
        url = String.format(ACTION_UPDATE_TRADED);
        return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
    }

    /**
     * 生成交易单
     * @return
     */
    public static String addTrading() {
        url = String.format(ACTION_ADD_TRADING);
        return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
    }
}
