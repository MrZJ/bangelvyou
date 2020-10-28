package com.zycreview.system.api;

import com.zycreview.system.utils.ConfigUtil;
import com.zycreview.system.utils.StringUtil;

/**
 * 药材认知
 */
public class PolicyApi {
    public static String url;
    public static final String ACTION_POLICY_LIST = "/admin/NewsInfo.do?pub_state=1&mobileLogin=true";
    public static final int API_POLICY_LIST = 1;//政策列表接口

    /**
     * 获取拼成列表数据
     *
     * @return
     */
    public static String getPolicyList(String curpage,String modId,String searchType,String titleLike) {
        url = String.format(ACTION_POLICY_LIST);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&curpage=").append(curpage);
        if(!StringUtil.isEmpty(modId)){
            urlBuffer.append("&mod_id=").append(modId);
        }
        if(!StringUtil.isEmpty(searchType)){
            urlBuffer.append("&sarchType=").append(searchType);
        }
        if(!StringUtil.isEmpty(titleLike)){
            urlBuffer.append("&title_like=").append(titleLike);
        }
        return urlBuffer.toString();
    }

}
