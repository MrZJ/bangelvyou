package com.basulvyou.system.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 订单集合信息实体类
 */
public class OrderListEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    public List<OrderEntity> order_list;//订单列表
    public String pay_amount;// 支付总额，该字段存在且大于0时显示支付按钮
    public String add_time;// 订单提交时间
    public String pay_sn;// 支付编号
}
