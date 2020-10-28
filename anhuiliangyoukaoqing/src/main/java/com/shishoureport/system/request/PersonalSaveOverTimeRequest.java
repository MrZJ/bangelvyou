package com.shishoureport.system.request;

import com.shishoureport.system.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/13.
 * copyright easybiz.
 */

public class PersonalSaveOverTimeRequest extends BaseRequest {
    private static final String METHOD = "/mobile/attendanceOvertimeMyself/save";
    public static final int PERSONAL_SAVE_OVERTIME_REQUEST = 10037;
    private String overtime_start, overtime_end, user_ids, user_names, cc_user_ids, cc_user_names, user_id, overtime_date, overtime_hours, overtime_reason;

    public PersonalSaveOverTimeRequest(String overtime_start, String overtime_end, String user_ids, String user_names,
                                       String cc_user_ids, String cc_user_names, String user_id, String overtime_date, String overtime_hours, String overtime_reason) {
        super();
        this.overtime_start = overtime_start;
        this.overtime_end = overtime_end;
        this.user_ids = user_ids;
        this.user_names = user_names;
        this.cc_user_ids = cc_user_ids;
        this.cc_user_names = cc_user_names;
        this.user_id = user_id;
        this.overtime_date = overtime_date;
        this.overtime_hours = overtime_hours;
        this.overtime_reason = overtime_reason;
    }

    @Override
    public Map<String, String> getRequestParams() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("overtime_start", overtime_start);
        hashMap.put("overtime_end", overtime_end);
        hashMap.put("overtime_end", overtime_end);
        hashMap.put("user_ids", user_ids);
        hashMap.put("user_names", user_names);
        hashMap.put("overtime_hours", overtime_hours);
        if (!StringUtil.isEmpty(cc_user_ids)) {
            hashMap.put("cc_user_ids", cc_user_ids);
        } else {
            hashMap.put("cc_user_ids", "");
        }
        if (!StringUtil.isEmpty(cc_user_names)) {
            hashMap.put("cc_user_names", cc_user_names);
        } else {
            hashMap.put("cc_user_names", "");
        }
        if (!StringUtil.isEmpty(overtime_reason)) {
            hashMap.put("overtime_reason", overtime_reason);
        }
        hashMap.put("user_id", user_id);
        hashMap.put("overtime_date", overtime_date);
        hashMap.put("is_submit", "1");
        return hashMap;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD);
        return builder.toString();
    }
}
