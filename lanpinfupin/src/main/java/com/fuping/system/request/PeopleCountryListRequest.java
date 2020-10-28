package com.fuping.system.request;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/10/10.
 * copyright easybiz.
 */

public class PeopleCountryListRequest extends BaseRequest {
    public static final int PEOPLE_COUNTRY_LIST_REQUEST = 1000006;
    private static final String METHOD = "/MInspection.do?method=getInspectionVillagePoorerList";
    private int curpage;
    private String user_id, village_p_name_like, town_p_index, inspection_state;

    public PeopleCountryListRequest(int curpage, String village_p_name_like, String user_id, String town_p_index, String inspection_state) {
        super();
        this.curpage = curpage;
        this.village_p_name_like = village_p_name_like;
        this.user_id = user_id;
        this.town_p_index = town_p_index;
        this.inspection_state = inspection_state;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD).append("&curpage=").append(curpage).
                append("&user_id=").append(user_id);
        if (village_p_name_like != null) {
            builder.append("&village_p_name_like=").append(village_p_name_like);
        }
        if (town_p_index != null) {
            builder.append("&town_p_index=").append(town_p_index);
        }
        if (inspection_state != null) {
            builder.append("&inspection_state=").append(inspection_state);
        }
        return builder.toString();
    }
}
