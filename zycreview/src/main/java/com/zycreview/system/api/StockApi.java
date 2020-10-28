package com.zycreview.system.api;

import com.zycreview.system.utils.ConfigUtil;
import com.zycreview.system.utils.StringUtil;

/**
 * 仓库接口
 */
public class StockApi {

    public static String url;
    public static final String ACTION_INSTOCK_MANAGE_LIST = "/admin/PlantForInput.do?method=list&mod_id=5001001000&mobileLogin=true";
    public static final int API_INSTOCK_MANAGE_LIST = 1;//入库管理列表信息
    public static final String ACTION_INSTOCK_MANAGE_INFO = "/admin/PlantInPut.do?method=add&mod_id=5001002000&mobileLogin=true";
    public static final int API_INSTOCK_MANAGE_INFO = 2;//待操作入库信息
    public static final String ACTION_INSTOCK_MANAGE_UPDATE = "/admin/PlantInPut.do?method=save&mod_id=5001002000&mobileLogin=true";
    public static final int API_INSTOCK_MANAGE_UPDATE = 3;//入库管理操作
    public static final String ACTION_STOCK_CHECK_LIST = "/admin/PlantOutPutView.do?mod_id=5007000200&mobileLogin=true";
    public static final int API_STOCK_CHECK_LIST = 4;//库存查询
    public static final String ACTION_SUB_PACKAGE_LIST = "/admin/NewsInfo.do?mod_id=1003003000&pub_state=1&mobileLogin=true";
    public static final int API_SUB_PACKAGE_LIST = 5;//药材分包列表
    public static final String ACTION_INSTOCK_RECORD_LIST = "/admin/PlantInPut.do?method=list&mod_id=5001002000&mobileLogin=true";
    public static final int API_INSTOCK_RECORD_LIST = 6;//入库记录
    public static final String ACTION_INSTOCK_RECORD_INFO = "/admin/PlantInPut.do?method=view&mod_id=5001002000&mobileLogin=true";
    public static final int API_INSTOCK_RECORD_INFO = 7;//入库详情


    /**
     * 获取库存查询列表数据
     *
     * @return
     */
    public static String getStockList(String key,String curpage,String pager,String titleLike) {
        url = String.format(ACTION_STOCK_CHECK_LIST);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&key=").append(key);
        urlBuffer.append("&curpage=").append(curpage);
        urlBuffer.append("&page=").append(pager);
        if(!StringUtil.isEmpty(titleLike)){
            urlBuffer.append("&drugs_name_like=").append(titleLike);
        }
        return urlBuffer.toString();
    }

    /**
     * 获取入库管理列表
     * @param key
     * @param curpage
     * @param pager
     * @param titleLike
     * @return
     */
    public static String getInstockManageList(String key,String curpage,String pager,String titleLike,String startDate,String endDate) {
        url = String.format(ACTION_INSTOCK_MANAGE_LIST);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&key=").append(key);
        urlBuffer.append("&curpage=").append(curpage);
        urlBuffer.append("&page=").append(pager);
        if(!StringUtil.isEmpty(titleLike)){
            urlBuffer.append("&ingred_name_like=").append(titleLike);
        }
        if(!StringUtil.isEmpty(startDate)){
            urlBuffer.append("&receive_date_s=").append(startDate);
        }
        if(!StringUtil.isEmpty(endDate)){
            urlBuffer.append("&receive_date_e=").append(endDate);
        }
        return urlBuffer.toString();
    }

    /**
     * 获取待入库信息
     */
    public static String getInstockingInfo(String key,String code) {
        url = String.format(ACTION_INSTOCK_MANAGE_INFO);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&key=").append(key);
        urlBuffer.append("&receive_no_in=").append(code);
        return urlBuffer.toString();
    }

    /**
     * 获取入库记录列表
     * @param key
     * @param curpage
     * @param pager
     * @param titleLike
     * @return
     */
    public static String getInstockRecordList(String key,String curpage,String pager,String titleLike,String startDate,String endDate) {
        url = String.format(ACTION_INSTOCK_RECORD_LIST);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&key=").append(key);
        urlBuffer.append("&curpage=").append(curpage);
        urlBuffer.append("&page=").append(pager);
        if(!StringUtil.isEmpty(titleLike)){
            urlBuffer.append("&ingred_name_like=").append(titleLike);
        }
        if(!StringUtil.isEmpty(startDate)){
            urlBuffer.append("&input_date_s=").append(startDate);
        }
        if(!StringUtil.isEmpty(endDate)){
            urlBuffer.append("&input_date_e=").append(endDate);
        }
        return urlBuffer.toString();
    }

    /**
     * 获取入库记录信息
     */
    public static String getRecodeInfo(String key,String id) {
        url = String.format(ACTION_INSTOCK_RECORD_INFO);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&key=").append(key);
        urlBuffer.append("&id=").append(id);
        return urlBuffer.toString();
    }

    /**
     * 药材入库
     */
    public static String instockingUpdate() {
        url = String.format(ACTION_INSTOCK_MANAGE_UPDATE);
        return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
    }

}
