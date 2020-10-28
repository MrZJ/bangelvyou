package com.shishoureport.system.request;

import android.content.Context;

import com.shishoureport.system.entity.LeaveAppEntity;
import com.shishoureport.system.utils.MySharepreference;
import com.shishoureport.system.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/7.
 * copyright easybiz.
 */

public class SaveLeaveAppRequest extends BaseRequest {
    public static final int SAVA_LEAVE_REQUEST = 100004;
    private static final String METHOD = "/mobile/attendanceLeave/save";
    private LeaveAppEntity entity;
    private Context mContext;

    public SaveLeaveAppRequest(LeaveAppEntity entity, Context context) {
        this.entity = entity;
        mContext = context;
    }

    @Override
    public Map<String, String> getRequestParams() {
        HashMap<String, String> map = new HashMap<>();
        if (entity != null) {
            map.put("leave_type", entity.leave_type);
            map.put("leave_type_name", entity.leave_type_name);
            map.put("start_time", entity.start_time);
            map.put("end_time", entity.end_time);
            map.put("hours", entity.hours);
            map.put("days", entity.days);
            map.put("overtime", entity.overtime);
            map.put("reson", entity.reson);
            map.put("user_ids", entity.user_ids);
            map.put("user_names", entity.user_names);
            map.put("is_submit", "1");
            if (!StringUtil.isEmpty(entity.cc_user_ids)) {
                map.put("cc_user_ids", entity.cc_user_ids);
            }
            if (!StringUtil.isEmpty(entity.cc_user_names)) {
                map.put("cc_user_names", entity.cc_user_names);
            }
            map.put("user_id", MySharepreference.getInstance(mContext).getUser().id);
        }
        return map;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD);
        return builder.toString();
    }
}
