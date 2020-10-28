package com.shishoureport.system.request;

import com.shishoureport.system.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/6.
 * copyright easybiz.
 */

public class LoginRequest extends BaseRequest {
    public static final int LOGIN_REQUEST = 100000;
    public static final String METHOD = "/mobile/login";
    private String user_name;
    private String password, app_device_token, umeng_device_token;

    public LoginRequest(String user_name, String password, String app_device_token, String umeng_device_token) {
        super();
        this.user_name = user_name;
        this.password = password;
        this.app_device_token = app_device_token;
        this.umeng_device_token = umeng_device_token;
    }

    @Override
    public Map<String, String> getRequestParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_name", user_name);
        map.put("app_device_token", app_device_token);
        map.put("password", password);
        if (!StringUtil.isEmpty(umeng_device_token)) {
            map.put("umeng_device_token", umeng_device_token);
        }
        return map;
    }

    @Override
    public String getRequestUrl() {
        return baseUrl + METHOD;
    }
}
