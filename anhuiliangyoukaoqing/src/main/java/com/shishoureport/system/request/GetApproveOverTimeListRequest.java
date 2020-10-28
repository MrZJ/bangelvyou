package com.shishoureport.system.request;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/8.
 * copyright easybiz.
 */

public class GetApproveOverTimeListRequest extends BaseRequest {
    private static final String METHOD = "/mobile/attendanceOvertime/auditList";
    public static final int GET_APPROVE_OVERTIME_LIST_REQUEST = 12008;
    private int page, pagecount;
    private String id;

    public GetApproveOverTimeListRequest(String id, int pagecount, int page) {
        super();
        this.page = page;
        this.pagecount = pagecount;
        this.id = id;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD).append("?user_id=").append(id).append("&row.first=").append(page).append("&row.count=").append(pagecount);
        return builder.toString();
    }
}
