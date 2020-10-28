package com.fuping.system.request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/10/11.
 * copyright easybiz.
 */

public class SaveReplyInstructionRequest extends BaseRequest {
    private static final String METHOD = "/MVillageTask.do?method=saveReplay";
    public static final int SAVE_INS_REP_INS_REQ = 100008;
    private String user_id, instructions_id, replay_content;

    public SaveReplyInstructionRequest(String user_id, String instructions_id, String replay_content) {
        super();
        this.user_id = user_id;
        this.instructions_id = instructions_id;
        this.replay_content = replay_content;
    }

    @Override
    public Map<String, String> getRequestParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("instructions_id", instructions_id);
        map.put("replay_content", replay_content);
        return map;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD);
        return builder.toString();
    }
}
