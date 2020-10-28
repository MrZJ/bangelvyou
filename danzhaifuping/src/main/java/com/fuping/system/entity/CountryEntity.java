package com.fuping.system.entity;

/**
 * Created by jianzhang.
 * on 2017/10/11.
 * copyright easybiz.
 */

public class CountryEntity extends BaseEntity {
    public static final String NOT_INSPECT = "0";
    public static final String INSPECT_POOR = "1";
    public static final String INSPECT_NOT_POOR = "2";

    public String link_village_id;
    public String link_village_name;
    public int is_poor_village;

    /**
     * 督查需要参数
     */
    public static final String IS_INSPECT = "1";
    public String village_p_index;
    public String village_p_name;
    public String no_poor_count;
    public String yes_poor_count;
    public String village_id;//贫困村的id
    public String village_name;// 贫困村的名称
    public String is_inspection;// 是否督查 0未督查，1已督查
    public String process_percent;// 督查进度
    public String poor_count;
}
