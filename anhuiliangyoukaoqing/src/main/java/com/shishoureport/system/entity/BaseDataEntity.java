package com.shishoureport.system.entity;

import java.io.Serializable;

/**
 * Created by jianzhang.
 * on 2017/9/7.
 * copyright easybiz.
 */

public class BaseDataEntity implements Serializable {
    public String id;
    public String type;
    public String type_name;
    public String remark;
    public String order_value;
    public String is_lock;
    public String is_del;
    public String add_date;
    public String add_user_id;
    public String update_date;
    public String update_user_id;
    public String del_date;
    public String del_user_id;
    public String type_value;
    public String pre_number;
    public String pre_varchar;
}
