package com.shishoureport.system.request;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/8.
 * copyright easybiz.
 */

public class ApproveCarManageDetailListRequest extends BaseRequest {
    private static final String METHOD = "/mobile/attendanceBus/view";
    public static final int APPROVE_CAR_MANAGE_DETAIL_LIST_REQUEST = 123218;
    private String id, user_id;

    public ApproveCarManageDetailListRequest(String id, String user_id) {
        super();
        this.user_id = user_id;
        this.id = id;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD).append("?user_id=").append(user_id).append("&id=").append(id);
        return builder.toString();
    }
}
