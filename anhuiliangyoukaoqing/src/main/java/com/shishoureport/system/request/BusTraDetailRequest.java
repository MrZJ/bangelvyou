package com.shishoureport.system.request;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/8.
 * copyright easybiz.
 */

public class BusTraDetailRequest extends BaseRequest {
    public static final int BUS_TRA_DETAIL_REQUEST = 100025;
    private String METHOD = "/mobile/attendancetravel/view";
    private String id;

    public BusTraDetailRequest(String id) {
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
