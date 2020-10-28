package com.shishoureport.system.entity;

import java.io.Serializable;

/**
 * Created by jianzhang.
 * on 2017/9/13.
 * copyright easybiz.
 */

public class TaskPersonList implements Serializable {
    public String id;
    public String link_id;
    public String task_name;
    public String task_type_name;
    public String task_type;
    public int task_audit;
    public String task_desc;
    public String complete_desc;
    public String user_type;
    public String file_path;
    public String file_name;
    public String accpect_person_id;
    public String accpect_person_name;
    public long complete_date;
    public String order_value;
    public String is_del;
    public String add_date;
    public String add_user_id;
    public String update_date;
    public String update_user_id;
    public String del_date;
    public String del_user_id;
}
