package com.fuping.system.request;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/10/10.
 * copyright easybiz.
 */

public class PeopleSearchRequest extends BaseRequest {
    public static final int PEOPLE_SEARCH_REQUEST = 1000125;
    private static final String METHOD = "/MInspection.do?method=getInspectionPoorerSearchList";
    private int curpage;
    private String user_id, search_state, poorSearch_names;

    public PeopleSearchRequest(int curpage, String user_id, String search_state, String poorSearch_names) {
        super();
        this.curpage = curpage;
        this.user_id = user_id;
        this.search_state = search_state;
        this.poorSearch_names = poorSearch_names;
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
                append("&poorSearch_names=").append(poorSearch_names);
        return builder.toString();
    }
}
