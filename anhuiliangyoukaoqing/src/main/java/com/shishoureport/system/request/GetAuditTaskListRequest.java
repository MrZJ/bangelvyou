package com.shishoureport.system.request;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/7.
 * copyright easybiz.
 */

public class GetAuditTaskListRequest extends BaseRequest {
    private static final String METHOD = "/mobile/taskinfoAudit";
    public static final int GET_AUDIT_TASK_LIST_REQUEST = 123028;
    private int row_first;
    private int row_count;
    private String user_id;

    public GetAuditTaskListRequest(String user_id, int row_first, int row_count) {
        super();
        this.row_first = row_first;
        this.row_count = row_count;
        this.user_id = user_id;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD).append("?row.first=").append(row_first)
                .append("&row.count=").append(row_count).append("&user_id=").append(user_id);
        return builder.toString();
    }
}
