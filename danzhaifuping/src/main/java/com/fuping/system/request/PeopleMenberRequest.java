package com.fuping.system.request;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/10/10.
 * copyright easybiz.
 */

public class PeopleMenberRequest extends BaseRequest {
    public static final int PEOPLE_MENBER_REQUEST = 10001231;
    private static final String METHOD = "/MInspection.do?method=getFamilyInfoList";
    private String user_id, poor_id;

    public PeopleMenberRequest(String user_id, String poor_id) {
        super();
        this.user_id = user_id;
        this.poor_id = poor_id;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD).append("&user_id=").
                append(user_id).append("&poor_id=").append(poor_id);
        return builder.toString();
    }
}
