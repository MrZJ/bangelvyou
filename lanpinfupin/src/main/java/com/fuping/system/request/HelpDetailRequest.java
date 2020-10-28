package com.fuping.system.request;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/10/10.
 * copyright easybiz.
 */

public class HelpDetailRequest extends BaseRequest {
    public static final int HELP_DETAIL_REQUEST = 10001;
    private static final String METHOD = "/MVillageTask.do?method=getVillageTaskView";
    private String id, user_id;

    public HelpDetailRequest(String id, String user_id) {
        super();
        this.id = id;
        this.user_id = user_id;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD).append("&id=").append(id).append("&user_id=").append(user_id);
        return builder.toString();
    }
}
