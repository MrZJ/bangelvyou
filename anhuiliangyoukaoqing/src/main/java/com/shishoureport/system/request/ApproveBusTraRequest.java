package com.shishoureport.system.request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/12.
 * copyright easybiz.
 */

public class ApproveBusTraRequest extends BaseRequest {
    private String METHOD = "/mobile/attendancetravel/saveAudit";
    public static final int APPROVE_BUS_TRA_REQUEST = 100045;
    public static final String IS_PASS = "1";
    public static final String NOT_PASS = "0";
    private String user_id, is_pass, id, audit_desc;


    public ApproveBusTraRequest(String user_id, String is_pass, String id, String audit_desc) {
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
