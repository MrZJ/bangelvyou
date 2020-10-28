package com.shishoureport.system.entity;

/**
 * Created by jianzhang.
 * on 2017/12/4.
 * copyright easybiz.
 */

public class OverTimeEntity extends BaseResult {
    public String id;//9,
    public String application_dept_id;//1,
    public String application_dept_name;//组织架构
    public String application_user_id;//1,
    public String application_user_name;//系统管理员
    public long overtime_date;//null,
    public String overtime_reason;//null,
    public String add_date;//1512377028000,
    public String add_uname;//系统管理员
    public String add_uid;//1,
    public String update_date;//null,
    public String update_uid;//null,
    public String del_date;//null,
    public String del_uid;//null,
    public String is_del;//0,
    public String rmark;//null,
    public String audit_name;//苑传仓
    public String audit_uid;//142,
    public int audit_state;//0,
    public String audit_date;//null,
    public String is_submit;//1,
    public String attendanceAuditList;//null,
    public String attendanceOvertimeDetailList;//[]
    public OverTimeDetailEntity map;
    public String over_time_type;
}
