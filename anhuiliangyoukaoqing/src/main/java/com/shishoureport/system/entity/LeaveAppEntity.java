package com.shishoureport.system.entity;

import java.io.Serializable;

/**
 * Created by jianzhang.
 * on 2017/9/7.
 * copyright easybiz.
 */

public class LeaveAppEntity implements Serializable {
    public String id;
    public String start_time;
    public String end_time;
    public String hours;
    public String days;
    public String reson;
    public String overtime;
    public String leave_type_name;
    public String leave_type;
    public String add_date;
    public String add_uname;
    public String add_uid;
    public String update_date;
    public String update_uid;
    public String del_date;
    public String del_uid;
    public String is_del;
    public String rmark;
    public String audit_name;
    public String audit_uid;
    public String audit_state;
    public String audit_date;
    public String user_ids;
    public String user_names;
    public String is_submit;
    public String cc_user_names;
    public String cc_user_ids;
}
