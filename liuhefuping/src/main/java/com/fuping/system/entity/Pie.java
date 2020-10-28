package com.fuping.system.entity;

import java.io.Serializable;

/**
 * Created by jianzhang.
 * on 2017/10/18.
 * copyright easybiz.
 */

public class Pie implements Serializable {
    public int PieColor;
    public float PieValue;
    public String PieString;

    public Pie(float pieValue, String pieString, int pieColor) {
        this.PieValue = pieValue;
        this.PieString = pieString;
        this.PieColor = pieColor;
    }
}