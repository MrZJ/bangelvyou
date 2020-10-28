package com.shenmai.system.entity;

import java.io.Serializable;

/**
 * 商品信息实体类
 */
public class GoodsEntity implements Serializable{

    public String goods_id;// 商品编号
    public String goods_name;// 商品名称
    public String goods_price;// 商品价格
    public String goods_marketprice;// 商品市场价
    public String goods_salenum;// 销量
    public String evaluation_good_star;// 评价星级
    public String evaluation_count;// 评价数
    public boolean group_flag;// 是否抢购
    public boolean xianshi_flag;// 是否限时折扣
    public String goods_image;// 图片名称
    public String goods_image_url;// 图片地址
    public String is_fcode;// 是否为F码商品 1-是 0-否
    public String is_appoint;// 是否是预约商品 1-是 0-否
    public String is_presell;// 是否是预售商品 1-是 0-否
    public String have_gift;// 是否拥有赠品 1-是 0-否
    public String pd_stock;// 商品库存
    public String fre_id;// 运费ID
    public String fav_id;// 收藏ID
    public String comm_no;// 商品编号
    public String yj_price;// 佣金
    public String sub_name;// 描述
    public boolean is_has_kc;//是否有库存

}
