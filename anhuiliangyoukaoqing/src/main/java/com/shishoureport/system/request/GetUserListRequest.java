package com.shishoureport.system.request;

import com.shishoureport.system.utils.StringUtil;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/7.
 * copyright easybiz.
 */

public class GetUserListRequest extends BaseRequest {
    public static final String METHOD = "/mobile/public/getDeptAndUserInfo";
    public static final int GET_USER_LIST_REQUEST = 100003;
    private String user_ids;

    public GetUserListRequest(String user_ids) {
        super();
        this.user_ids = user_ids;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD);
        if (!StringUtil.isEmpty(user_ids)) {
            builder.append("?user_ids=").append(user_ids);
        }
        return builder.toString();
    }
}
