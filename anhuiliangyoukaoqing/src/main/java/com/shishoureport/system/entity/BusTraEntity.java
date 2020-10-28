package com.shishoureport.system.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/10.
 * copyright easybiz.
 */

public class BusTraEntity implements Serializable {
    public static final int AUDIT_PASS = 1;
    public static final int AUDIT_WAITE_APRROVE = 0;
    public String id;
    public String p_name;
    public String p_index;
    public long start_time;
    public long end_time;
    public String days;
    public String vehicle;
    public String business_purpose;
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
    public int audit_state;    //    最终审核状态（0：待审核 1：审核通过 -1 审核不通过）
    public String audit_date;
    public int is_submit;
    public String fellow_people;
    public String temp_travel_money;
    public List<AttendanceAuditList> attendanceAuditList;
    public List<AttendanceCcList> attendanceCcList;
}
