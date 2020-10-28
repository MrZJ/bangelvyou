package com.shishoureport.system.entity;

/**
 * Created by jianzhang.
 * on 2017/9/6.
 * copyright easybiz.
 */

public class AttendanceLeave extends BaseResult {
    public static final int SUBMIT_OK = 1;
    public String id;
    public long start_time;
    public long end_time;
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
    public int audit_state;
    public String audit_date;
    public int is_submit;
    public String attendanceAuditList;
}
