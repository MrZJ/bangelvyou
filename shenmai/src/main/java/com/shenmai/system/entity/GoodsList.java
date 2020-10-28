package com.shenmai.system.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 商品信息集合类
 */
public class GoodsList implements Serializable{

    public List<GoodsEntity> goodsList;
    public List<HomeEntity> home1;//首页才会用到的数据集合
}
