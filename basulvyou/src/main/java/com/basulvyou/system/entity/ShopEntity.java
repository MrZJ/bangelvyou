package com.basulvyou.system.entity;

import java.util.List;

/**
 * 商品数据实体类
 * @author KevinLi
 *
 */
public class ShopEntity {

    public String goods_id;// 商品编号
    public String goods_name;// 商品名称
    public String goods_price;// 商品价格
    public String goods_content;// 商品描述
    public String goods_marketprice;// 商品市场价
    public String goods_salenum;// 销量
    public String evaluation_good_star;// 评价星级
    public String evaluation_count;// 评价数
    public boolean group_flag;// 是否抢购
    public boolean xianshi_flag;// 是否限时折扣
    public String goods_image_url1;// 单张图片名称
    public List<ShopDetailImgEntity> goods_image_url;// 多张图片地址
    public String is_fcode;// 是否为F码商品 1-是 0-否
    public String is_appoint;// 是否是预约商品 1-是 0-否
    public String is_presell;// 是否是预售商品 1-是 0-否
    public String have_gift;// 是否拥有赠品 1-是 0-否
    public String pd_stock;// 商品库存
    public String fre_id;// 运费ID
    public String sccomm;// 商品收藏数
    public String cls_type;//商品所属类别
    public String goods_distance;//商品与附近商品的距离
    //团购参数
    public String group_id;// 团购商品ID
    public String groupbuy_price;// 团购商品价格
    public String groupbuy_image;// 团购商品图片
    public String groupbuy_rebate;// 团购商品折扣
    public String end_time_text;// 团购结束时间
    public String entp_addr;// 店铺地址


}
