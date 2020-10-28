package com.zycreview.system.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/1/5 0005.
 *
 * 采收
 */

public class HarvestManageEntity {

    public String base_code_in;//种植基地,
    public String grow_way;//种植方式,
    public String manager;//种植负责人,
    public String manager_phone;//负责人电话,
    public String name;//任务名称,
    public String place_code;//产地编码,
    public String plant_area;//种植面积,
    public String plant_date;//种植时间,
    public String plant_name;//种植物名称,
    public String plant_zq;//种植周期,
    public String seed_source;//种植来源,
    public String seed_weight;//种植重量

    public List<IngredInfos> list;

    public class IngredInfos{

        public String drugs_code;
        public String drugs_name;//药材名称,
        public String plan_output;//预计产量
        public String plan_no;

    }
}
