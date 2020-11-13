package com.shishoureport.system.entity;

import java.io.Serializable;

/**
 * @date
 **/
public class ApplyWorkerEntity implements Serializable {
    public String num;
    public String reason;
    public String start_time;
    public String end_time;
    public String detail;
    public String content;
    public String audit_name;
    public String audit_uid;
    public String cc_user_ids;
    public String cc_user_names;
    public int is_submit = 1;
    public String application_user_name;
    public String application_dept_name;
    public String audit_state;
}

