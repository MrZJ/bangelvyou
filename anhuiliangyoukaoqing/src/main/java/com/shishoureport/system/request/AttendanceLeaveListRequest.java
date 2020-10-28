package com.shishoureport.system.request;

import android.util.Log;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/6.
 * copyright easybiz.
 */

public class AttendanceLeaveListRequest extends BaseRequest {
    public static final int ATT_LEA_REQUEST = 100001;
    public static final String METHOD = "/mobile/attendanceLeave";
    private String user_id;
    private int row_first;
    private int row_count;

    public AttendanceLeaveListRequest(String user_id, int page, int curpage) {
        super();
        this.user_id = user_id;
        this.row_count = page;
        this.row_first = curpage;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD).append("?user_id=").append(user_id).append("&row.count=").append(row_count).append("&row.first=").append(row_first);
        Log.e("request", builder.toString());
        return builder.toString();
    }
}
