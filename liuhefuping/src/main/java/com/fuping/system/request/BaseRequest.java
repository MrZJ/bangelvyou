package com.fuping.system.request;


import com.fuping.system.utils.ConfigUtil;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/6.
 * copyright easybiz.
 */

public abstract class BaseRequest implements Serializable {
    public String baseUrl;

    public BaseRequest() {
        baseUrl = ConfigUtil.HTTP_URL;
    }

    public abstract Map<String, String> getRequestParams();

    public abstract String getRequestUrl();
}
