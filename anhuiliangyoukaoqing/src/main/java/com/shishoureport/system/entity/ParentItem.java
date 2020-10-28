package com.shishoureport.system.entity;

import java.io.Serializable;

/**
 * Created by jianzhang.
 * on 2017/9/13.
 * copyright easybiz.
 */

public class ParentItem implements Serializable {
    public String name;
    public String size;

    public ParentItem(String name, String size) {
        this.name = name;
        this.size = size;
    }

}
