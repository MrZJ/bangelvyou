package com.shishoureport.system.request;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/8.
 * copyright easybiz.
 */

public class AttendanceAuditListRequest extends BaseRequest {
    public static final String TYPE_LEAVE_APP = "1003002030";
    public static final String TYPE_BUSINESS_TRAVEL = "1003004030";
    public static final String TYPE_OVERT_TIME = "1003005030";
    public static final String TYPE_CAR_MANAGE = "1003008030";
    private static final String METHOD = "/mobile/attendanceAudit";
    public static final int ATTENDANCE_AUDIT_LIST_REQUEST = 100011;
    private String user_id;
    private String mod_id;
    private int row_first;
    private int row_count;


    public AttendanceAuditListRequest(String user_id, String mod_id, int row_first, int row_count) {
        super();
        this.user_id = user_id;
        this.mod_id = mod_id;
        this.row_first = row_first;
        this.row_count = row_count;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD).append("?user_id=").append(user_id).
                append("&mod_id=").append(mod_id).append("&row.first=").append(row_first).
                append("&row.count=").append(row_count);
        return builder.toString();
    }
}
