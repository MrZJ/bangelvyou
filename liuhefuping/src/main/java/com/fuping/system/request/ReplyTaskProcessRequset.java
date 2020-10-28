package com.fuping.system.request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/10/11.
 * copyright easybiz.
 */

public class ReplyTaskProcessRequset extends BaseRequest {
    private static final String METHOD = "/MVillageTask.do?method=saveProgressRecord";
    public static final int REPLY_TASK_PROCESS_REQUSET = 10010;
    private String user_id, task_id, this_progress, task_desc;

    public ReplyTaskProcessRequset(String user_id, String task_id, String this_progress, String task_desc) {
        super();
        this.user_id = user_id;
        this.task_id = task_id;
        this.this_progress = this_progress;
        this.task_desc = task_desc;
    }

    @Override
    public Map<String, String> getRequestParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("task_id", task_id);
        map.put("this_progress", this_progress);
        map.put("task_desc", task_desc);
        return map;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD);
        return builder.toString();
    }
}
