package com.shishoureport.system.entity;

import java.io.Serializable;

/**
 * Created by jianzhang.
 * on 2017/9/5.
 * copyright easybiz.
 */

public class BusinessTravel implements Serializable {
    public String place;
    public String start_time;
    public boolean is_start_time_checked;
    public String end_time;
    public boolean is_end_time_checked;
    public String time_length;
}
