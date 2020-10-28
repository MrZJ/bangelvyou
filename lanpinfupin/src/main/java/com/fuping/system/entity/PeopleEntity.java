package com.fuping.system.entity;

/**
 * Created by jianzhang.
 * on 2017/10/18.
 * copyright easybiz.
 */

public class PeopleEntity extends BaseEntity {
    public String poor_id;//贫困户id
    public String poor_name;// 贫困户姓名
    public String poor_addr;// 贫困户地址
    public String process_percent;// 督查进度
    public String poor_reson;//致贫原因
    public String is_inspection;//是否被督查 0未督查，1已督查

}
