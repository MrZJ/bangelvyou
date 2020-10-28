package com.shenmai.system.entity;

/**
 * 用户信息实体类
 *
 */
public class UserEntity {

	public String username;// 用户名
	public String userRole;// 用户类型 0普通用户 1分销商
	public String key;// 登录令牌(token)
}
