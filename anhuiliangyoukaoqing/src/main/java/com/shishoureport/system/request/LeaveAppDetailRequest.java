package com.shishoureport.system.request;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/8.
 * copyright easybiz.
 */

public class LeaveAppDetailRequest extends BaseRequest {
    public static final int LEAVE_APP_REQUEST = 100009;
    private String METHOD = "/mobile/attendanceLeave/view";
    private String id;

    public LeaveAppDetailRequest(String id) {
        super();
        this.id = id;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD).append("?id=").append(id);
        return builder.toString();
    }
}
