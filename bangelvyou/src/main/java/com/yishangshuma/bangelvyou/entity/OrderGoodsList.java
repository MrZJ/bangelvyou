package com.yishangshuma.bangelvyou.entity;

import java.io.Serializable;

public class OrderGoodsList implements Serializable{

	private static final long serialVersionUID = 1L;
	public String goods_id;//商品id
	public String goods_name;// 商品名称
	public String goods_price;// 商品价格
	public String goods_num;// 商品购买数量
	public String goods_image_url;// 商品图片地址
}
