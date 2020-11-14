package com.shishoureport.system.request;

import android.content.Context;
import android.text.TextUtils;

import com.shishoureport.system.entity.ApplyWorkerEntity;
import com.shishoureport.system.entity.LeaveAppEntity;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.utils.MySharepreference;
import com.shishoureport.system.utils.StringUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/7.
 * copyright easybiz.
 */

public class SaveApplyWorkerRequest extends BaseRequest {
    public static final int SAVE_APPLY_WORKER_REQUEST = 1000010;
    private static final String METHOD = "/mobile/attendancePerson/save";
    private ApplyWorkerEntity entity;

    public SaveApplyWorkerRequest(ApplyWorkerEntity entity, Context context) {
        super(context);
        this.entity = entity;
    }

    @Override
    public Map<String, String> getRequestParams() {
        HashMap<String, String> map = new HashMap<>();
        if (entity != null) {
            map.put("application_dept_id", user.dept_id);
            map.put("application_dept_name", TextUtils.isEmpty(user.dept_name) ? "" : user.dept_name);
            map.put("application_user_id", user.id);
            map.put("application_user_name", user.real_name);
            map.put("user_id", user.id);
            map.put("num", entity.num);
            map.put("reason", entity.reason);
            map.put("start_time", entity.start_time);
            map.put("end_time", entity.end_time);
            map.put("detail", entity.detail);
            map.put("content", entity.content);
            map.put("user_names", entity.audit_name);
            map.put("user_ids", entity.audit_uid);
            if (!StringUtil.isEmpty(entity.cc_user_ids)) {
                map.put("cc_user_ids", entity.cc_user_ids);
            }
            if (!StringUtil.isEmpty(entity.cc_user_names)) {
                map.put("cc_user_names", entity.cc_user_names);
            }
            map.put("is_submit", "1");
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
