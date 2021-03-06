package com.fuping.system.request;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/10/10.
 * copyright easybiz.
 */

public class AddTaskDetailRequest extends BaseRequest {
    public static final int HELP_DETAIL_REQUEST = 10002;
    private static final String METHOD = "/MVillageTask.do?method=getVillageTaskAdd";

    public AddTaskDetailRequest() {
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
