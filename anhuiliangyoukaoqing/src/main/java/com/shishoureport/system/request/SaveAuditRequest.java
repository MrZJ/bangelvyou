package com.shishoureport.system.request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/8.
 * copyright easybiz.
 */

public class SaveAuditRequest extends BaseRequest {
    private static final String METHOD = "/mobile/attendanceLeave/saveAudit";
    public static final String PASS = "1";
    public static final String NOT_PASS = "0";
    public static final int SAVA_AUDIT_REQUEST = 100010;
    private String user_id;
    private String is_pass;
    private String id;
    private String audit_desc;

    public SaveAuditRequest(String user_id, String is_pass, String id, String audit_desc) {
        super();
        this.user_id = user_id;
        this.is_pass = is_pass;
        this.id = id;
        this.audit_desc = audit_desc;
    }

    @Override
    public Map<String, String> getRequestParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("is_pass", is_pass);
        map.put("audit_desc", audit_desc);
        map.put("id", id);
        return map;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD);
        return builder.toString();
    }
}
