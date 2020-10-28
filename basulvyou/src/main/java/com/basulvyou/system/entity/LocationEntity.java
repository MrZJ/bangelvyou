package com.basulvyou.system.entity;

import java.util.List;

/**
 * 当地列表实体类
 * Created by KevinLi on 2016/3/2.
 */
public class LocationEntity {
    public String location_date;//时间
    public String location_brief;//描述
    public String location_id;//ID
    public List<ShopDetailImgEntity> location_img;//图地址
    public String location_img_one;//单图地址
    public String location_title;//标题
    public String location_visit_count;//浏览数
    public String location_collect_count;//收藏数
    public String location_evaluation_count;//评论数
    public String location_type;//类别
    public String location_url;//链接地址
    public String location_group_price;//团购价格
    public String location_group_rebate;//折扣
    public String location_end_date;//截至日期
    public String location_address;//店铺地址
    public String location_price;//价格
}
