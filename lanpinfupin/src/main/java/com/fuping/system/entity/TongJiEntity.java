package com.fuping.system.entity;

/**
 * Created by jianzhang.
 * on 2017/10/18.
 * copyright easybiz.
 */

public class TongJiEntity extends BaseEntity {
    public String title;
    public String percent;
    public String count;

    public String all_count;//贫困村总数;
    public String all_count_ratio;//贫困村总数百分比
    public String all_no_inspection_count;//未督查贫困村
    public String all_no_inspection_count_ratio;//未督查贫困村百分比
    public String some_no_inspection_count;//已督查但未达标贫困村
    public String some_no_inspection_count_ratio;//已督查但未达标贫困村百分比
    public String yes_inspection_count;//已督查已达标贫困村
    public String yes_inspection_count_ratio;//已督查已达标贫困村百分比
}
