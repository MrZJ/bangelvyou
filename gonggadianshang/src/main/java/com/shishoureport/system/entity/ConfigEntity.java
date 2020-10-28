package com.shishoureport.system.entity;

/**
 * 用户配置实体类
 *
 */
public class ConfigEntity {
	public String username;// 用户名
	public String key;// 登录令牌(token)
	public boolean isLogin;// 是否登录
	public boolean isFirst;// 是否第一次进入app
}
