package com.basulvyou.system.entity;

/**
 * Created by KevinLi on 2016/1/28.
 */
public class Pager {
    int page = 0;

    public static final int rows = 10;

    int count;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
