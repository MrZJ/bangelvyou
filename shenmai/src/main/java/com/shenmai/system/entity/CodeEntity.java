package com.shenmai.system.entity;

import java.io.Serializable;

/**
 * 验证码实体类
 * @author KevinLi
 *
 */
public class CodeEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	public String username;
	public String veriCode;//验证码
}
