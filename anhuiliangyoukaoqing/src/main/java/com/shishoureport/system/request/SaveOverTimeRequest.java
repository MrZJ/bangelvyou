package com.shishoureport.system.request;

import com.shishoureport.system.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/13.
 * copyright easybiz.
 */

public class SaveOverTimeRequest extends BaseRequest {
    private static final String METHOD = "/mobile/attendanceOvertime/save";
    public static final int SAVE_OVERTIME_REQUEST = 10077;
    private String user_id, user_apply_ids, user_apply_names, user_apply_start_times, user_apply_end_times,
            user_apply_hours, user_ids, user_names, application_dept_id, overtime_date, overtime_reason, cc_user_names, cc_user_ids;

    public SaveOverTimeRequest(String user_id, String user_apply_ids, String user_apply_names, String user_apply_start_times,
                               String user_apply_end_times, String user_apply_hours, String user_ids,
                               String user_names, String application_dept_id, String overtime_date, String overtime_reason,
                               String cc_user_names, String cc_user_ids) {
        super();
        this.user_id = user_id;
        this.user_apply_ids = user_apply_ids;
        this.user_apply_names = user_apply_names;
        this.user_apply_start_times = user_apply_start_times;
        this.user_apply_end_times = user_apply_end_times;
        this.user_apply_hours = user_apply_hours;
        this.user_ids = user_ids;
        this.user_names = user_names;
        this.application_dept_id = application_dept_id;
        this.overtime_date = overtime_date;
        this.overtime_reason = overtime_reason;
        this.cc_user_names = cc_user_names;
        this.cc_user_ids = cc_user_ids;
    }

    @Override
    public Map<String, String> getRequestParams() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("user_id", user_id);
        hashMap.put("user_apply_ids", user_apply_ids);
        hashMap.put("user_apply_names", user_apply_names);
        hashMap.put("user_apply_start_times", user_apply_start_times);
        hashMap.put("user_apply_end_times", user_apply_end_times);
        hashMap.put("user_apply_hours", user_apply_hours);
        hashMap.put("user_ids", user_ids);
        hashMap.put("user_names", user_names);
        hashMap.put("application_dept_id", application_dept_id);
        hashMap.put("overtime_date", overtime_date);
        hashMap.put("overtime_reason", overtime_reason);
        hashMap.put("is_submit", "1");
        if (!StringUtil.isEmpty(cc_user_names)) {
            hashMap.put("cc_user_names", cc_user_names);
        } else {
            hashMap.put("cc_user_names", "");
        }
        if (!StringUtil.isEmpty(cc_user_ids)) {
            hashMap.put("cc_user_ids", cc_user_ids);
        } else {
            hashMap.put("cc_user_ids", "");
        }
        return hashMap;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD);
        return builder.toString();
    }
}
