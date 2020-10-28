package com.shishoureport.system.entity;

import java.io.Serializable;

/**
 * Created by jianzhang.
 * on 2017/9/12.
 * copyright easybiz.
 */

public class TaskEntity implements Serializable {
    public static final int TASK_COMPLET = 1;
    public String id;
    public String task_name;
    public String task_type_name;
    public String task_type;
    public int task_audit;
    public String task_audit_desc;
    public String task_desc;
    public String user_type;
    public String file_path;
    public String file_name;
    public String link_id;
    public String accpect_person_id;
    public String accpect_person_name;
    public String complete_date;
    public String order_value;
    public String is_submit;
    public String is_del;
    public long add_date;
    public String add_user_id;
    public String update_date;
    public String update_user_id;
    public String del_date;
    public String del_user_id;
    public TaskPersonMap map;
    public long get_date;
    public String civil_service;
    public String word_size;
    public String copies;
    public String nature;
    public String remark;
    public String reason;
}
