package com.yishangshuma.bangelvyou.entity;

import java.io.Serializable;

/**
 * 商品传输实体类
 * Created by KevinLi on 2016/3/3.
 */
public class ShopBundleEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    public String key = "1";//排序方式 1-销量 2-浏览量 3-价格 4-推荐位
    public String keyword;//搜索关键字
    public String order = "2";//排序方式 1-升序 2-降序
}
