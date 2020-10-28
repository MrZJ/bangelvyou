package com.shishoureport.system.request;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/8.
 * copyright easybiz.
 */

public class GetMenberListRequest extends BaseRequest {
    private static final String METHOD = "/mobile/attendanceOvertime/getApplyUserList";
    public static final int GET_MENBER_LISTREQUEST = 100052;
    private String dept_id;

    public GetMenberListRequest(String dept_id) {
        super();
        this.dept_id = dept_id;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD).append("?dept_id=").append(dept_id);
        return builder.toString();
    }
}
