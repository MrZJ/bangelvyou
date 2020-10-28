package com.shishoureport.system.request;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/10.
 * copyright easybiz.
 */

public class AttanceTravelListRequest extends BaseRequest {
    private static final String METHOD = "/mobile/attendancetravel";
    public static final int ATTANCE_TRAVEL_LIST_REQUEST = 100019;
    private String user_id;
    private int row_first;
    private int row_count;

    public AttanceTravelListRequest(String user_id, int row_count, int row_first) {
        super();
        this.user_id = user_id;
        this.row_count = row_count;
        this.row_first = row_first;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD).append("?row.first=").append(row_first).append("&row.count=").append(row_count).append("&user_id=").append(user_id);
        return builder.toString();
    }
}
