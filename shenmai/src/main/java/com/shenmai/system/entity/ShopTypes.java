package com.shenmai.system.entity;

import java.io.Serializable;

public class ShopTypes implements Serializable{

	private String content;
	private int imgUrl;

	public int getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(int imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public ShopTypes(String content, int imgUrl) {
		super();
		this.content = content;
		this.imgUrl = imgUrl;
	}

	public ShopTypes() {
		super();
	}


}
