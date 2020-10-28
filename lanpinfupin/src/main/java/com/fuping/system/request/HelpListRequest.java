package com.fuping.system.request;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/10/10.
 * copyright easybiz.
 */

public class HelpListRequest extends BaseRequest {
    public static final int HELP_REQUEST = 100000;
    public static final int SEARCH_0 = 237;
    public static final int SEARCH_1 = 238;
    public static final int SEARCH_2 = 239;
    public static final int SEARCH_3 = 240;
    public static final int SEARCH_4 = 241;
    private static final String METHOD = "/MVillageTask.do?method=getVillageTaskList";
    private int curpage;
    private int baseDataId;
    private String link_village_name_like, user_id;

    public HelpListRequest(int curpage, int baseDataId, String link_village_name_like, String user_id) {
        super();
        this.curpage = curpage;
        this.baseDataId = baseDataId;
        this.link_village_name_like = link_village_name_like;
        this.user_id = user_id;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD).append("&curpage=").append(curpage).append("&baseDataId=").
                append(baseDataId).append("&link_village_name_like=").append(link_village_name_like).append("&user_id=").append(user_id);
        return builder.toString();
    }
}
