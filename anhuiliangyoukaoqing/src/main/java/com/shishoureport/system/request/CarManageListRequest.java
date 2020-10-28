package com.shishoureport.system.request;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/8.
 * copyright easybiz.
 */

public class CarManageListRequest extends BaseRequest {
    private static final String METHOD = "/mobile/attendanceBus";
    public static final int CAR_MANAGE_LIST_REQUEST = 100028;
    private int page, pagecount;
    private String id;

    public CarManageListRequest(String id, int pagecount, int page) {
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
