package com.fuping.system.request;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/10/11.
 * copyright easybiz.
 */

public class SaveTaskRequest extends BaseRequest {
    public static final int SAVE_TASK_REQUEST = 100003;
    private static final String METHOD = "/MVillageTask.do?method=saveVillageTask";
    private String user_id, task_name, poor_target, link_unit_name, link_unit_id,
            link_head_name, link_village_name, link_village_id, complete_date;

    public SaveTaskRequest(String user_id, String task_name, String poor_target, String link_unit_name,
                           String link_unit_id, String link_head_name, String link_village_name,
                           String link_village_id, String complete_date) {
        super();
        this.user_id = user_id;
        this.task_name = task_name;
        this.poor_target = poor_target;
        this.link_unit_name = link_unit_name;
        this.link_unit_id = link_unit_id;
        this.link_head_name = link_head_name;
        this.link_village_name = link_village_name;
        this.link_village_id = link_village_id;
        this.complete_date = complete_date;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String, String> map = new Hashtable<>();
        map.put("user_id", user_id);
        map.put("task_name", task_name);
        map.put("poor_target", poor_target);
        map.put("link_unit_name", link_unit_name);
        map.put("link_unit_id", link_unit_id);
        map.put("link_head_name", link_head_name);
        map.put("link_village_name", link_village_name);
        map.put("link_village_id", link_village_id);
        map.put("complete_date", complete_date);
        return map;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD);
        return builder.toString();
    }
}
