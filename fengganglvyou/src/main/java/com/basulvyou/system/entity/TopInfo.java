package com.basulvyou.system.entity;

import java.io.Serializable;

/**
 * 
 * 置顶布局实体类
 *
 */
public class TopInfo implements Serializable{
	/* 实体类标识 */
	public String Id;
	/* 实体类标题 */
	public String title;
	/* 实体类图片 */
	public String pciture;
	/* 跳转URL(首页是商品ID) */
	public String picUrl;
	//视频播放地址
	public String adv_video_url;
}
