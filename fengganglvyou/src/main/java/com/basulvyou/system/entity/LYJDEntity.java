package com.basulvyou.system.entity;

/**
 * @autor jianzhang
 * Version 1.0.0.
 * <p>
 * Date 2017/5/18.
 * <p>
 * Copyright iflytek.com
 */

public class LYJDEntity {
    private int goods_id;
    private String goods_image_url;
    private String goods_name;

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_image_url() {
        return goods_image_url;
    }

    public void setGoods_image_url(String goods_image_url) {
        this.goods_image_url = goods_image_url;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }
}
