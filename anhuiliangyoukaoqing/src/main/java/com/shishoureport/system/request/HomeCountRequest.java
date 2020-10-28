package com.shishoureport.system.request;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/12.
 * copyright easybiz.
 */

public class HomeCountRequest extends BaseRequest {
    private static final String METHOD = "/mobile/public/getLate_count";
    public static final int HOME_COUNT_REQUEST = 100906;
    private String user_id;

    public HomeCountRequest(String user_id) {
        super();
        this.user_id = user_id;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD).append("?user_id=").append(user_id);
        return builder.toString();
    }

}
