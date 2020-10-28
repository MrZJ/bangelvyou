package com.shenmai.system.entity;

import java.io.Serializable;

/**
 * 详细地址实体类
 * @author KevinLi
 *
 */
public class AddressEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	public String city_id;// 城市编号
	public String area_id;// 地区编号
	public String area_info;// 地址
	public String address;// 详细地址
	public String tel_phone;// 固定电话机
	public String mob_phone;// 手机
	public String address_id;// 地址ID
	public String member_id;// 会员ID
	public String true_name;// 收货人姓名
}
