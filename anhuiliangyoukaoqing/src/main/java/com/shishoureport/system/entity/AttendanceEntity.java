package com.shishoureport.system.entity;

import java.io.Serializable;

/**
 * Created by jianzhang.
 * on 2017/9/12.
 * copyright easybiz.
 */

public class AttendanceEntity implements Serializable {
    public static final int UNNORMAL = 1;
    //    【in_or_out：1：外勤打卡，0，正常打卡】
    // 【is_cd：1：迟到打卡，0正常打卡】
// 【is_zt：1：早退打卡，0正常打卡】
// 【is_first：1：不是第一次，第一次打卡】
// 【is_no：1：上班打卡，2：下班打卡】
// 【id：不是第一次打卡的时候，会返回一个该数据的id】
// 【punch_point：上班打卡地点】
// 【punch_out_place：下班打卡地点】
    public int in_or_out;
    public int is_first;
    public int is_cd;
    public int is_zt;
    public int is_no;
    public String punch_point;
    public String punch_out_place;
    public String id;
    public int is_complete;
    public String start_time;
    public String end_time;
    public String punch_hour_time;
    public String punch_out_time;
    public String sb_in;
    public String sb_out;
    public String is_can_punch;
}
