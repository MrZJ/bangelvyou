package com.shishoureport.system.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jianzhang.
 * on 2018/5/8.
 * copyright easybiz.
 */
public class AproveOverTimeEntity implements Serializable {
    public String id;// 25,
    public String application_dept_id;// 26,
    public String application_dept_name;// public String 安徽省粮油质监站public String ,
    public String application_user_id;// 112,
    public String application_user_name;// public String 李同让public String ,
    public long overtime_date;// 1525190400000,
    public String overtime_reason;// public String 10public String ,
    public String add_date;// 1525758081000,
    public String add_uname;// public String 李同让public String ,
    public String add_uid;// 112,
    public String update_date;// null,
    public String update_uid;// null,
    public String del_date;// null,
    public String del_uid;// null,
    public String is_del;// 0,
    public String rmark;// null,
    public String audit_name;// public String 李同让public String ,
    public String audit_uid;// 112,
    public String audit_state;// 0,
    public String audit_date;// null,
    public String is_submit;// 1,
    public String over_time_type;// 1,
    public List<AttendanceAuditList> attendanceAuditList;// null,
    public List<AttendanceOvertimeDetailList> attendanceOvertimeDetailList;
    public List<AttendanceCcList> attendanceCcList;
}
