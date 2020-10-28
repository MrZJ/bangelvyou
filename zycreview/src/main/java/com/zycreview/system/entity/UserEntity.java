package com.zycreview.system.entity;

import java.util.List;

/**
 * 用户信息实体类
 *
 */
public class UserEntity {

	public String entp_name;// 企业名名
	public String entpId;// 企业id
	public String login_name;// 登录名
	public String key;// 登录令牌(token)
	public String user_type_name;// 企业用户类型
	public List<UrlListBean> urlList;
}
