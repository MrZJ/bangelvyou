package com.shishoureport.system.request;

import android.content.Context;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/13.
 * copyright easybiz.
 */

public class ApproveApplyUseRequest extends BaseRequest {
    private static final String METHOD = "/mobile/applyOrder/saveAudit";
    public static final int APPROVE_APPLY_USE_REQUEST = 100277;
    private String is_pass, audit_desc, id;

    public ApproveApplyUseRequest(Context context, String is_pass, String audit_desc, String id) {
        super(context);
        this.is_pass = is_pass;
        this.audit_desc = audit_desc;
        this.id = id;
    }

    @Override
    public Map<String, String> getRequestParams() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("user_id", user.id);
        hashMap.put("is_pass", is_pass);
        if (!TextUtils.isEmpty(audit_desc)) {
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
