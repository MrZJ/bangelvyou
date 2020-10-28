package com.fuping.system.request;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/10/10.
 * copyright easybiz.
 */

public class PeopleListRequest extends BaseRequest {
    public static final int PEOPLE_LIST_REQUEST = 1000002;
    private static final String METHOD = "/MInspection.do?method=getInspectionPoorerList";
    private int curpage;
    private String user_id, poorer_name_like, village_p_index, inspection_state;

    public PeopleListRequest(int curpage, String user_id, String poorer_name_like, String village_p_index, String inspection_state) {
        super();
        this.curpage = curpage;
        this.poorer_name_like = poorer_name_like;
        this.user_id = user_id;
        this.village_p_index = village_p_index;
        this.inspection_state = inspection_state;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD).append("&curpage=").append(curpage).append("&user_id=").append(user_id);
        if (poorer_name_like != null) {
            builder.append("&poorer_name_like=").append(poorer_name_like);
        }
        if (inspection_state != null) {
            builder.append("&inspection_state=").append(inspection_state);
        }
        if (village_p_index != null){
            builder.append("&village_p_index=").append(village_p_index);
        }
        return builder.toString();
    }
}
