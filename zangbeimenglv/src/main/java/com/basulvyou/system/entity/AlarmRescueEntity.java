package com.basulvyou.system.entity;

import java.io.Serializable;

/**
 * 报警救援实体类
 */
public class AlarmRescueEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    public String rescue_alp;//字母
    public String rescue_icon;//图片
    public String rescue_name;//救援名称
    public String rescue_tel;//救援电话信息
    public String rescue_address;//救援地址信息
}
