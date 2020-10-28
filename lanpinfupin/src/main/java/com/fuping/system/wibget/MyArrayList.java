package com.fuping.system.wibget;

import java.util.ArrayList;

/**
 * Created by jianzhang.
 * on 2017/10/27.
 * copyright easybiz.
 */

public class MyArrayList<E> extends ArrayList<E> {
    @Override
    public void add(int index, E object) {
        super.add(index, object);
        if (size() > 4) {
            this.remove(4);
        }
    }
}
