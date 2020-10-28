package com.fuping.system.request;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/10/10.
 * copyright easybiz.
 */

public class PeopleBaseRequest extends BaseRequest {
    public static final int PEOPLE_BASE_REQUEST = 10001234;
    private static final String METHOD = "/MInspection.do?method=getPoorSearchBaseDataList";

    public PeopleBaseRequest() {
        super();
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD);
        return builder.toString();
    }
}
