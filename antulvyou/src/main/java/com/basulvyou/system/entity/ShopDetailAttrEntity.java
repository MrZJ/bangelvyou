package com.basulvyou.system.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 商品详情套餐实体类
 * @author KevinLi
 *
 */
public class ShopDetailAttrEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	public String attr_id;//套餐id
	public List<ShopDetailAttrInfoEntity> son_attr;//套餐详情
	public String attr_name;//套餐名称
}
