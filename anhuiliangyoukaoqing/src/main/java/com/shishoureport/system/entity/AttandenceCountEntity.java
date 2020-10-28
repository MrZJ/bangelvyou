package com.shishoureport.system.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/13.
 * copyright easybiz.
 */

public class AttandenceCountEntity implements Serializable {
    public List<AttendanceItem> attendanceSheetAttendanceList;//出勤天数（天）
    public List<AttendanceItem> attendanceSheetLateList;//迟到（次）
    public List<AttendanceItem> attendanceSheetLeaveList;//早退（次）
    public List<AttendanceItem> attendanceSheetMissingList;//缺卡（次）
    public List<AttendanceItem> attendanceSheetOutworkList;//外勤（次）
}
