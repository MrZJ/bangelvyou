package com.shishoureport.system.request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/13.
 * copyright easybiz.
 */

public class ApproveCarManageRequest extends BaseRequest {
    private static final String METHOD = "/mobile/attendanceBus/saveAudit";
    public static final int APPROVE_CAR_MANAGE_REQUEST = 103277;
    private String user_id, is_pass, audit_desc, id;

    public ApproveCarManageRequest(String user_id, String is_pass, String audit_desc, String id) {
        super();
        this.user_id = user_id;
        this.is_pass = is_pass;
        this.audit_desc = audit_desc;
        this.id = id;
    }

    @Override
    public Map<String, String> getRequestParams() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("user_id", user_id);
        hashMap.put("is_pass", is_pass);
        hashMap.put("audit_desc", audit_desc);
        hashMap.put("id", id);
        return hashMap;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD);
        return builder.toString();
    }
}
