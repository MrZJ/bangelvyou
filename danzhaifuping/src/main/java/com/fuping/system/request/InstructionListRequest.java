package com.fuping.system.request;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/10/11.
 * copyright easybiz.
 */

public class InstructionListRequest extends BaseRequest {
    private static final String METHOD = "/MVillageTask.do?method=getInstructionsList";
    public static final int INS_LIST_REQUEST = 10005;
    private int curpage, is_replay;
    private String villageTask_id;

    public InstructionListRequest(int curpage, int is_replay, String villageTask_id) {
        super();
        this.curpage = curpage;
        this.is_replay = is_replay;
        this.villageTask_id = villageTask_id;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD).append("&curpage=").append(curpage).
                append("&is_replay=").append(is_replay).append("&villageTask_id=").append(villageTask_id);
        return builder.toString();
    }
}
