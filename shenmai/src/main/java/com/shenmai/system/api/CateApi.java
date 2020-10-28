package com.shenmai.system.api;

import com.shenmai.system.utlis.ConfigUtil;

/**
 * 分类接口
 */
public class CateApi {

    public static String url;
    public static final String ACTION_GET_CATE = "/Service.do?method=goods_class";
    public static final int API_GET_CATE = 2;//一级分类

    /**
     * 获取一级分类数据
     *
     * @return
     */
    public static String getCateUrl(String storeId)
    {
        url = String.format(ACTION_GET_CATE);
        StringBuffer stringBuffer = new StringBuffer(ConfigUtil.HTTP_URL).append(url);
        if (null != storeId && !"".equals(storeId)) {
            stringBuffer.append("&store_id=").append(storeId);//id不为空时查询店铺一级商品分类
        }
        return stringBuffer.toString();
    }
}
