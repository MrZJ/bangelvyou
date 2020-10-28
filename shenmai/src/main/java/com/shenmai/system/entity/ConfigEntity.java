package com.shenmai.system.entity;

/**
 * 用户配置实体类
 *
 */
public class ConfigEntity {

	public String username;// 用户名
	public String passwordMD5;// 加密密码
	public String key;// 登录令牌(token)
	public String province;// 用户当前省市
	public String address;// 用户当前地理位置
	public String userRole;// 用户类型 0普通用户 1分销商
	public boolean isLogin;// 是否登录
	public boolean isFirst;// 是否第一次进入app
}
