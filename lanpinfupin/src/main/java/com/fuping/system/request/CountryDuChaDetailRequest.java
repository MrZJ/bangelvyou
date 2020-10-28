package com.fuping.system.request;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/10/10.
 * copyright easybiz.
 */

public class CountryDuChaDetailRequest extends BaseRequest {
    public static final int COUNTRY_DUCHA_DETAIL_REQUEST = 100009;
    private static final String METHOD = "/MInspection.do?method=getInspectionVillageAdd";
    private String user_id, village_id;

    public CountryDuChaDetailRequest(String user_id, String village_id) {
        super();
        this.user_id = user_id;
        this.village_id = village_id;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD).append("&user_id=").
                append(user_id).append("&village_id=").append(village_id);
        return builder.toString();
    }
}
