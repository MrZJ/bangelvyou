package com.shishoureport.system.request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/12.
 * copyright easybiz.
 */

public class ApproveOverTimeDetailRequest extends BaseRequest {
    private String METHOD = "/mobile/attendanceOvertime/audit";
    public static final int APPROVE_OVERTIME_DETAIL_REQUEST = 12100325;
    private String id;


    public ApproveOverTimeDetailRequest(String id) {
        super();
        this.id = id;
    }

    @Override
    public Map<String, String> getRequestParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);
        return map;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD).append("?id=").append(id);
        return builder.toString();
    }
}
