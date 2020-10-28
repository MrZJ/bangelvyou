package com.basulvyou.system.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 我的订单列表实体类
 *
 */
public class OrderList implements Serializable{

	private static final long serialVersionUID = 1L;

	public List<OrderListEntity> order_group_list;
}
