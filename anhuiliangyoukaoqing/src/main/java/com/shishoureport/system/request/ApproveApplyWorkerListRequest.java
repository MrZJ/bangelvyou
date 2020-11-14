package com.shishoureport.system.request;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/8.
 * copyright easybiz.
 */

public class ApproveApplyWorkerListRequest extends BaseRequest {
    private static final String METHOD = "/mobile/attendancePerson/auditList";
    public static final int APPROVE_APPLY_WORKER_LISTREQUEST = 1020325;
    private int page, pagecount;
    private String id;

    public ApproveApplyWorkerListRequest(String id, int pagecount, int page) {
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
