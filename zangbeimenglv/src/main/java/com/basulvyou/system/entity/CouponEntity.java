package com.basulvyou.system.entity;

import java.io.Serializable;

/**
 * 优惠券实体类
 */
public class CouponEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public String voucher_id;// 兑换订单索引
    public String voucher_code;// 代金券编码
    public String voucher_title;// 代金券标题
    public String voucher_desc;// 代金券描述
    public String voucher_start_date;// 代金券有效期开始时间
    public String voucher_end_date;// 代金券有效期结束时间
    public String voucher_price;// 代金券面额
    public String voucher_limit;// 代金券使用时的订单限额
    public String voucher_state;// 代金券状态(1-未用,2-已用,3-过期,4-收回)
    public String voucher_order_id;// 使用该代金券的订单编号
    public String voucher_store_id;// 代金券的店铺id
    public String store_name;// 店铺名称
    public String store_id;// 店铺ID
    public String voucher_t_customimg;// 代金券模板图片

}
