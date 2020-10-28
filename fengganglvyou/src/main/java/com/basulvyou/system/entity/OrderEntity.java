package com.basulvyou.system.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/3/1.
 */
public class OrderEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public String order_id;// 订单id
    public String order_sn;// 订单sn
    public String pay_sn;// 支付编号sn
    public String order_type;// 订单类型0普通1团购
    public String pay_type;// 支付编号方式
    public String order_state;//订单状态
    public String store_name;// 店铺名称
    public String goods_amount;// 商品总价
    public String order_amount;// 订单总价
    public String valid_code;// 消费码
    public String pd_amount;// 预存款支付总价
    public String shipping_fee;// 运费
    public String store_pstype;//派送方式
    public String state_desc;// 状态文字
    public String payment_name;// 支付方式文字
    public String evaluation_state;// 订单评论状态
    public boolean if_cancel;// 是否显示取消按钮 true/false
    public boolean if_receive;// 是否显示确认收货按钮 true/false
    public boolean if_lock;// 是否显示锁定中状态 true/false
    public boolean if_deliver;// 是否显示查看物流按钮 true/false
    public List<OrderGoodsList> extend_order_goods;// 订单商品列表
    public OrderAddress extend_order_common;//订单收货地址
}
