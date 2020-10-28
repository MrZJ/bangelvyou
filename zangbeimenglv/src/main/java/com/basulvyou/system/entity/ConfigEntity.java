package com.basulvyou.system.entity;

import java.io.Serializable;

/**
 * 用户配置实体类
 */
public class ConfigEntity implements Serializable{

    public String username;// 用户名
    public String key;// 登录令牌(token)
    public String province;// 用户当前省市
    public String address;// 用户当前地理位置
    public String level;// 用户等级
    public boolean isLogin;// 是否登录
    public boolean isFirst;// 是否第一次进入app
    public String kuaiAddress;// 用户送货地址
    public String kuaiAddressCode;// 用户送货地址编码
    public String searchHistory = "";// 用户搜索历史
    public String point = "";// 用户积分
    public String credit_score;//信用分数
    public String mobile;//电话号码
    public String birthday;//生日
    public String sex;//性别
    public String real_name;//昵称
}
