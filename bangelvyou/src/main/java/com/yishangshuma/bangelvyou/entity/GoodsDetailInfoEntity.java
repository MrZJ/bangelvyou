package com.yishangshuma.bangelvyou.entity;

import java.util.List;

/**
 * 商品详情信息实体类
 *
 */
public class GoodsDetailInfoEntity {

	public String comm_name;//商品名称
	public String comm_price;//商品价格
	public List<GoodsDetailImgEntity> comm_image_url;//商品图片
	public String commentScore;//评论平均分
	public String fre_id;//运费ID
	public String comm_id;//商品id
	public String commentCount;//评论数
	public String pd_stock;//商品库存
	public List<GoodsDetailAttrEntity> attr_arrs;//套餐
	public EntpInfoEntity entpInfo;//企业信息
	public List<CommentInfoEntity> commentInfoList;//评论信息
	
}