package com.shayangfupin.system.entity;

/**
 * 上拉下拉页数
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
