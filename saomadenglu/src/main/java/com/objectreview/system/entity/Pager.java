package com.objectreview.system.entity;

/**
 * 加载更多页面标识
 */
public class Pager {
    int page = 0;

    public static final int rows = 6;

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
