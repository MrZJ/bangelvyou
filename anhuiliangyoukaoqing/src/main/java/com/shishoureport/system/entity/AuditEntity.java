package com.shishoureport.system.entity;

import java.io.Serializable;

/**
 * Created by jianzhang.
 * on 2017/9/8.
 * copyright easybiz.
 */

public class AuditEntity implements Serializable {
    public String id;
    public String link_id;
    public long audit_date;
    public String audit_name;
    public String audit_uid;
    public long add_date;
    public String add_uid;
    public String update_date;
    public String update_uid;
    public String del_date;
    public String del_uid;
    public String is_del;
    public String rmark;
    public String order_value;
    public int audit_state;
    public String audit_desc;
    public String link_table;
    public AuditMap map;
}
