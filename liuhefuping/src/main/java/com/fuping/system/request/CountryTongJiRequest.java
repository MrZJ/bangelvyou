package com.fuping.system.request;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/10/10.
 * copyright easybiz.
 */

public class CountryTongJiRequest extends BaseRequest {
    public static final int COUNTRY_TONG_JI_REQUEST = 100004;
    private static final String METHOD = "/MInspection.do?method=getInspectionVillageCount";

    public CountryTongJiRequest() {
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
