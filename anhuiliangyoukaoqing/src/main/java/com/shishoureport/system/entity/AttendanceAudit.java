package com.shishoureport.system.entity;

import java.io.Serializable;

/**
 * Created by jianzhang.
 * on 2017/9/8.
 * copyright easybiz.
 */

public class AttendanceAudit implements Serializable {
    public String id;
    public String link_id;
    public String audit_date;
    public String audit_name;
    public String audit_uid;
    public String add_date;
    public String add_uid;
    public String update_date;
    public String update_uid;
    public String del_date;
    public String del_uid;
    public String is_del;
    public String rmark;
    public String order_value;
    public String audit_state;
    public String audit_desc;
    public String link_table;
}
