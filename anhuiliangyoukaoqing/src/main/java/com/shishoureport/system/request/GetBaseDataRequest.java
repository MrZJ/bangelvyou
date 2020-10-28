package com.shishoureport.system.request;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/7.
 * copyright easybiz.
 */

public class GetBaseDataRequest extends BaseRequest {
    private static final String METHOD = "/mobile/public/getBaseDataList";
    public static final int GET_BASE_DATA_REQUEST = 100005;

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD);
        return builder.toString();
    }
}
