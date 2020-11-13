package com.shishoureport.system.request;


import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/8.
 * copyright easybiz.
 */

public class MedicalListRequest extends BaseRequest {
    private static final String METHOD = "/mobile/reagent";
    private static final String METHOD2 = "/mobile/stock";
    public static final int MEDICAL_LIST_REQUEST = 120011;
    private boolean isReserve;

    public MedicalListRequest(boolean isReserve) {
        this.isReserve = isReserve;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(isReserve ? METHOD2 : METHOD);
        return builder.toString();
    }
}
