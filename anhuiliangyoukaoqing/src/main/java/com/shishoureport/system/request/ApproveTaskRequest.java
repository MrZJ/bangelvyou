package com.shishoureport.system.request;

import com.shishoureport.system.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/13.
 * copyright easybiz.
 */

public class ApproveTaskRequest extends BaseRequest {
    private static final String METHOD = "/mobile/taskinfoAudit/saveAudit";
    public static final int APPROVE_TASK_REQUEST = 103217;
    private String user_id, audit_state, audit_desc, id;

    public ApproveTaskRequest(String user_id, String audit_state, String audit_desc, String id) {
        super();
        this.user_id = user_id;
        this.audit_state = audit_state;
        this.audit_desc = audit_desc;
        this.id = id;
    }

    @Override
    public Map<String, String> getRequestParams() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("user_id", user_id);
        hashMap.put("audit_state", audit_state);
        if (!StringUtil.isEmpty(audit_desc)) {
            hashMap.put("audit_desc", audit_desc);
        }
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
