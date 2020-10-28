package com.shishoureport.system.request;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/12.
 * copyright easybiz.
 */

public class AttendanceDetailRequest extends BaseRequest {
    private static final String METHOD = "/mobile/sheet";
    public static final int ATTENDANCE_DETAIL_REQUEST = 100056;
    private String user_id;
    private double lat, lng;

    public AttendanceDetailRequest(String user_id, double lat, double lng) {
        super();
        this.user_id = user_id;
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD).append("?user_id=").append(user_id).append("&lat=").append(lat).
                append("&lng=").append(lng);
        return builder.toString();
    }

}
