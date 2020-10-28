package com.shishoureport.system.entity;

import java.io.Serializable;

/**
 * Created by jianzhang.
 * on 2017/9/13.
 * copyright easybiz.
 */

public class AttendanceItem implements Serializable {
    public String id;
    public String punch_name;
    public String punch_uid;
    public String year;
    public String month;
    public String day;
    public long attendance_time;
    public String punch_hour_time;
    public String punch_point_gps_t;
    public String punch_point_gps_w;
    public String punch_point;
    public String is_punch_line;
    public String punch_line_rmark;
    public String punch_out_time;
    public String punch_out_place_gps_t;
    public String punch_out_place_gps_w;
    public String punch_out_place;
    public String is_punch_out_line;
    public String punch_out_line_rmark;
    public String is_leave;
    public String is_unusual;
    public String punch_state;
    public String is_overtime;
    public String overtime;
    public String is_travel;
    public String remark;
    public String is_del;
    public String is_cd;
    public String is_zt;
    public String missing_card;
    public String add_date;
    public String add_user_id;
    public String update_date;
    public String update_user_id;
    public String del_date;
    public String del_user_id;
}
