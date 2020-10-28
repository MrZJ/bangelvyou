package com.objectreview.system.api;


import com.objectreview.system.utlis.ConfigUtil;

/**
 * 包装码打包保存API
 */
public class MergePackageSaveApi {

    public static String url;
    public static final String ACTION_MERGE_PACKAGE_SAVE = "/Service.do?method=pack_code_pack_save";
    public static final int API_MERGE_PACKAGE_SAVE = 1;//保存纪录

    /**
     * url
     *
     * @return
     */
    public static String getMergePackageSave() {
        url = String.format(ACTION_MERGE_PACKAGE_SAVE);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url);
        return urlBuffer.toString();
    }
}
