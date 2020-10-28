package com.zycreview.system.entity;

import java.util.List;

/**
 * 种植详情实体类
 */
public class PlantTaskDetailEntity {

    public String base_code_in;//种植基地
    public String grow_way;//种植方式
    public String manager;//种植负责人
    public String manager_phone;//负责人电话
    public String name;//任务名称
    public String place_code;//产地编码
    public String plant_area;//种植面积
    public String plant_name;//种植物名称
    public String plant_zq;//种植周期
    public String seed_source;//种植来源
    public String seed_weight;//种植重量
    public List<PlantDetailEntity> list;

}
