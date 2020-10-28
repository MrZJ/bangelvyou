package com.objectreview.system.entity;

import java.io.Serializable;

/**
 * 用户配置实体类

 */
public class ConfigEntity implements Serializable{

	public String username;// 用户名
	public String usertype;// 用户类型
	public String passwordMD5;// 加密密码
	public String key;// 登录令牌(token)
	public String province;// 用户当前省市
	public String address;// 用户当前地理位置
	public boolean isLogin;// 是否登录
	public boolean isFirst;// 是否第一次进入app
}
