package com.basulvyou.system.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 商品详情信息实体类
 * @author KevinLi
 *
 */
public class ShopDetailInfoEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	public String comm_name;//商品名称
	public String comm_price;//商品价格
	public String comm_old_price;//商品价格
	public String comm_image_url;//商品图片
	public List<ShopDetailImgEntity> comm_image_urls;//商品图片集合
	public String commentScore;//评论平均分
	public String fre_id;//运费ID
	public String comm_id;//商品id
	public String commentCount;//评论数
	public String pd_stock;//商品库存
	public List<ShopDetailAttrEntity> attr_arrs;//套餐子属性
	public List<ShopDetailAttrInfoEntity> attr_list;//套餐
	public EntpInfoEntity entpInfo;//企业信息
	public List<CommentInfoEntity> commentInfoList;//评论信息
	public String goods_content;//商品描述
	public String goods_glcontent;//商品攻略描述
	public String goods_jtcontent;//商品交通描述
	public String kfdate;//开放时间
	public List<ShopEntity> fjcommList;//附近相关信息
	public String contentUrl;//套餐内容
	public String noticeUrl;//消费需知
}