package com.basulvyou.system.entity;

/**
 * @autor jianzhang
 * Version 1.0.0.
 * <p>
 * Date 2017/5/18.
 * <p>
 * Copyright iflytek.com
 */

public class BDZXEntity extends Object{
    private int n_id;
    private int n_visit_count;
    private String n_add_date;
    private String n_brief;
    private String n_main_img;
    private String n_title;
    private String n_url;

    public int getN_id() {
        return n_id;
    }

    public void setN_id(int n_id) {
        this.n_id = n_id;
    }

    public int getN_visit_count() {
        return n_visit_count;
    }

    public void setN_visit_count(int n_visit_count) {
        this.n_visit_count = n_visit_count;
    }

    public String getN_add_date() {
        return n_add_date;
    }

    public void setN_add_date(String n_add_date) {
        this.n_add_date = n_add_date;
    }

    public String getN_brief() {
        return n_brief;
    }

    public void setN_brief(String n_brief) {
        this.n_brief = n_brief;
    }

    public String getN_main_img() {
        return n_main_img;
    }

    public void setN_main_img(String n_main_img) {
        this.n_main_img = n_main_img;
    }

    public String getN_title() {
        return n_title;
    }

    public void setN_title(String n_title) {
        this.n_title = n_title;
    }

    public String getN_url() {
        return n_url;
    }

    public void setN_url(String n_url) {
        this.n_url = n_url;
    }
}
