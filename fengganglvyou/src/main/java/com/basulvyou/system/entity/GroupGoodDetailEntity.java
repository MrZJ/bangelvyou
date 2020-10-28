package com.basulvyou.system.entity;

import java.util.List;

/**
 * 团购商品实习实体类
 */
public class GroupGoodDetailEntity {

    public String goods_name;//商品名称
    public String goods_price;//商品价格
    public List<ShopDetailImgEntity> groupbuy_image;//商品图片
    public String commentScore;//评论平均分
    public String fre_id;//运费ID
    public String commentCount;//评论数
    public EntpInfoEntity entpInfo;//店铺信息
    public List<CommentInfoEntity> commentInfoList;//评论信息
    public String group_id;// 团购商品id
    public String groupbuy_price;// 团购价格
    public String groupbuy_rebate;// 折扣
    public String start_time_text;// 开始时间
    public String end_time_text;// 结束时间
    public long xiangcha_date_s;// 距离结束时间（秒）
    public String can_buy;//是否能买 0 能买 1不能买
    public String pd_stock;//商品库存
}
