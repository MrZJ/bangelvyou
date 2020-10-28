package com.fuping.system.request;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/10/10.
 * copyright easybiz.
 */

public class CountryDuChaDetailRequest2 extends BaseRequest {
    private static final String METHOD = "/MInspection.do?method=getInspectionVillageView";
    private String user_id, inspection_village_id;

    public CountryDuChaDetailRequest2(String user_id, String inspection_village_id) {
        super();
        this.user_id = user_id;
        this.inspection_village_id = inspection_village_id;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD).append("&user_id=").
                append(user_id).append("&inspection_village_id=").append(inspection_village_id);
        return builder.toString();
    }
}
