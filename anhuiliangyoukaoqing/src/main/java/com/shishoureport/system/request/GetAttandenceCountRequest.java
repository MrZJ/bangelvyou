package com.shishoureport.system.request;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/13.
 * copyright easybiz.
 */

public class GetAttandenceCountRequest extends BaseRequest {
    private static final String METHOD = "/mobile/sheet/view";
    public static final int GET_ATTANDENCE_COUNT_REQUEST = 100070;
    private String user_id;
    private int year, month;

    public GetAttandenceCountRequest(String user_id, int year, int month) {
        super();
        this.user_id = user_id;
        this.year = year;
        this.month = month;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD).append("?user_id=").append(user_id).append("&year=").
                append(year).append("&month=").append(month);
        return builder.toString();
    }
}
