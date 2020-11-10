package com.shishoureport.system.request;

import android.content.Context;

import com.shishoureport.system.entity.User;
import com.shishoureport.system.utils.ConfigUtil;
import com.shishoureport.system.utils.MySharepreference;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/6.
 * copyright easybiz.
 */

public abstract class BaseRequest implements Serializable {
    public String baseUrl;
    protected User user;

    public BaseRequest() {
        baseUrl = ConfigUtil.HTTP_URL;
    }

    public BaseRequest(Context context) {
        baseUrl = ConfigUtil.HTTP_URL;
        user = MySharepreference.getInstance(context).getUser();

    }

    public abstract Map<String, String> getRequestParams();

    public abstract String getRequestUrl();
}
