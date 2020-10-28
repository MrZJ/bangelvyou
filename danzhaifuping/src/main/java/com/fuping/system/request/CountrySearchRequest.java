package com.fuping.system.request;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/10/10.
 * copyright easybiz.
 */

public class CountrySearchRequest extends BaseRequest {
    public static final int COUNTRY_SEARCH_REQUEST = 10001241;
    private static final String METHOD = "/MInspection.do?method=getInspectionVillageSearchList";
    private int curpage;
    private String user_id, search_state, villageSearch_names;

    public CountrySearchRequest(int curpage, String user_id, String search_state, String villageSearch_names) {
        super();
        this.curpage = curpage;
        this.user_id = user_id;
        this.search_state = search_state;
        this.villageSearch_names = villageSearch_names;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD).append("&curpage=").append(curpage).
                append("&user_id=").append(user_id).
                append("&search_state=").append(search_state).
                append("&villageSearch_names=").append(villageSearch_names);
        return builder.toString();
    }
}
