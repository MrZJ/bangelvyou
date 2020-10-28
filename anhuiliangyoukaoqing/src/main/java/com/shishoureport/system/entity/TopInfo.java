package com.shishoureport.system.entity;

import java.io.Serializable;

/**
 * 置顶布局实体类
 */
public class TopInfo implements Serializable {
    /* 实体类标识 */
    public String adv_id;
    /* 实体类标题 */
    public String adv_title;
    /* 实体类图片 */
    public String adv_pic;
    /* 跳转URL(首页是商品ID) */
    public String adv_pic_url;

    //标题
    public String adv_title1;
    public String adv_title2;
    public String floor_id;
    public String floor_title;

}
