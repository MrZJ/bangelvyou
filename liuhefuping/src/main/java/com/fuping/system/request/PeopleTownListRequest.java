package com.fuping.system.request;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/10/10.
 * copyright easybiz.
 */

public class PeopleTownListRequest extends BaseRequest {
    public static final int PEOPLE_TOWN_LIST_REQUEST = 100000;
    private static final String METHOD = "/MInspection.do?method=getInspectionTownPoorerList";
    private int curpage;
    String user_id, town_p_name_like;

    public PeopleTownListRequest(int curpage, String town_p_name_like, String user_id) {
        super();
        this.curpage = curpage;
        this.town_p_name_like = town_p_name_like;
        this.user_id = user_id;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        if (town_p_name_like != null) {
            builder.append(METHOD).append("&curpage=").append(curpage).append("&town_p_name_like=").
                    append(town_p_name_like).append("&user_id=").append(user_id);
        } else {
            builder.append(METHOD).append("&curpage=").append(curpage).append("&user_id=").append(user_id);
        }
        return builder.toString();
    }
}
