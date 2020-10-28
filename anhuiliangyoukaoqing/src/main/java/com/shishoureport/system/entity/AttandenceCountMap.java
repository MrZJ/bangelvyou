package com.shishoureport.system.entity;

import java.io.Serializable;

/**
 * Created by jianzhang.
 * on 2017/9/13.
 * copyright easybiz.
 */

public class AttandenceCountMap implements Serializable {
    public AttandenceCountEntity map;
    public String id;
    public String year;
    public String month;
    public String punch_name;
    public String punch_uid;
    public int attendance_days;
    public int attendance_shift;
    public int rest_days;
    public int late_count;
    public int leave_count;
    public int missing_card_count;
    public int absenteeism_count;
    public int outwork_count;
    public int overwork_count;
}
