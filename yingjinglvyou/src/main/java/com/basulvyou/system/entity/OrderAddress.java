package com.basulvyou.system.entity;

import java.io.Serializable;

/**
 * 订单收货地址
 *
 */
public class OrderAddress implements Serializable{

	private static final long serialVersionUID = 1L;

	public String reciver_name;//收货姓名
	public OrderAddressPhone reciver_info;//收货地址电话
	public Object invoice_info;//发票信息
}
