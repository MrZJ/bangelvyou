package com.shishoureport.system.request;

import com.shishoureport.system.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/13.
 * copyright easybiz.
 */

public class CommitTaskRequest extends BaseRequest {
    private static final String METHOD = "/mobile/taskinfo/saveAudit";
    public static final int COMMIT_TASK_REQUEST = 100098;
    private String user_id, id, complete_desc, is_pass, user_ids, user_names;

    public CommitTaskRequest(String user_id, String id, String is_pass, String complete_desc, String user_ids, String user_names) {
        super();
        this.user_id = user_id;
        this.id = id;
        this.is_pass = is_pass;
        this.complete_desc = complete_desc;
        this.user_ids = user_ids;
        this.user_names = user_names;
    }

    @Override
    public Map<String, String> getRequestParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("id", id);
        map.put("complete_desc", complete_desc);
        map.put("is_pass", is_pass);
        if (!StringUtil.isEmpty(user_ids)){
            map.put("user_ids", user_ids);
        }
        if (!StringUtil.isEmpty(user_names)){
            map.put("user_names", user_names);
        }
        return map;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder stringBuilder = new StringBuilder(baseUrl);
        stringBuilder.append(METHOD);
        return stringBuilder.toString();
    }
}
