package com.shishoureport.system.request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/6.
 * copyright easybiz.
 */

public class UmengUpdateDTRequest extends BaseRequest {
    public static final int UMENG_UPDATE_DT_REQUEST = 123310;
    public static final String METHOD = "/mobile/login/updateDeviceToken";
    private String umeng_device_token, user_id;

    public UmengUpdateDTRequest(String umeng_device_token, String user_id) {
        super();
        this.umeng_device_token = umeng_device_token;
        this.user_id = user_id;
    }

    @Override
    public Map<String, String> getRequestParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("umeng_device_token", umeng_device_token);
        map.put("user_id", user_id);
        return map;
    }

    @Override
    public String getRequestUrl() {
        return baseUrl + METHOD;
    }
}
