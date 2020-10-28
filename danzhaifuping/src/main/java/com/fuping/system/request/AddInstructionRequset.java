package com.fuping.system.request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/10/11.
 * copyright easybiz.
 */

public class AddInstructionRequset extends BaseRequest {
    private static final String METHOD = "/MVillageTask.do?method=saveInstructions";
    public static final int ADD_INS_REQUEST = 10009;
    private String user_id, villageTask_id, instructions_content;

    public AddInstructionRequset(String user_id, String villageTask_id, String instructions_content) {
        super();
        this.user_id = user_id;
        this.villageTask_id = villageTask_id;
        this.instructions_content = instructions_content;
    }

    @Override
    public Map<String, String> getRequestParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("villageTask_id", villageTask_id);
        map.put("instructions_content", instructions_content);
        return map;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD);
        return builder.toString();
    }
}
