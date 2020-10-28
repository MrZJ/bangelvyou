package com.shenmai.system.api;


import com.shenmai.system.utlis.ConfigUtil;

/**
 * 地址api
 * @author KevinLi
 *
 */
public class AddressApi {

	public static String url;
	public static final String ACTION_GET_ADDRESS = "/Service.do?method=addrList";
	public static final int API_GET_ADDRESS = 1;//地址列表
	public static final String ACTION_GET_DELETE_ADDRESS = "/Service.do?method=addrDel";
	public static final int API_GET_DELETE_ADDRESS = 2;//删除地址
	public static final String ACTION_GET_ADD_ADDRESS = "/Service.do?method=addrAdd";
	public static final int API_GET_ADD_ADDRESS = 3;//增加地址
	public static final String ACTION_GET_ADDRESS_REGION = "/Service.do?method=areaList";
	public static final int API_GET_ADDRESS_REGION = 4;//地区列表region
	public static final String ACTION_EDIT_ADDRESS = "/Service.do?method=addrEdit";
	public static final int API_GET_EDIT_ADDRESS = 5;//编辑修改地址
	public static final String ACTION_GET_CHANGE_ADDRESS = "?act=member_buy&op=change_address";
	public static final int API_GET_CHANGE_ADDRESS = 6;//更换订单地址

	/**
	 * 获取地址列表
	 *
	 * @return
	 */
	public static String getAddressUrl(){
		url = String.format(ACTION_GET_ADDRESS);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}

	/**
	 * 删除地址
	 *
	 * @return
	 */
	public static String getDeleteAddressUrl(){
		url = String.format(ACTION_GET_DELETE_ADDRESS);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}

	/**
	 * 增加地址
	 * true_name 姓名
		city_id 城市编号(地址联动的第二级)
		area_id 地区编号(地址联动的第三级)
		area_info 地区信息，例：天津 天津市 红桥区
		address 地址信息，例：水游城8层
		tel_phone 电话号码
		mob_phone 手机
	 * @return
	 */
	public static String getAddAddressUrl(){
		url = String.format(ACTION_GET_ADD_ADDRESS);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}

	/**
	 * 地区列表
	 * 请求参数

		key 当前登录令牌
		area_id 地区编号(为空时默认返回一级分类)
		返回数据

		area_id 地区编号
		area_name 地区名称
	 *
	 * @return
	 */
	public static String getAddressRegionUrl(){
		url = String.format(ACTION_GET_ADDRESS_REGION);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}

	/**
	 * 增加地址
	 * true_name 姓名
	 * address_id 地址编号
		city_id 城市编号(地址联动的第二级)
		area_id 地区编号(地址联动的第三级)
		area_info 地区信息，例：天津 天津市 红桥区
		address 地址信息，例：水游城8层
		tel_phone 电话号码
		mob_phone 手机
	 * @return
	 */
	public static String getEditAddressUrl(){
		url = String.format(ACTION_EDIT_ADDRESS);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}

	/**
	 * 更换地址
	 * true_name 姓名
	 * address_id 地址编号
		city_id 城市编号(地址联动的第二级)
		area_id 地区编号(地址联动的第三级)
		area_info 地区信息，例：天津 天津市 红桥区
		address 地址信息，例：水游城8层
		tel_phone 电话号码
		mob_phone 手机
	 * @return
	 */
	public static String getChangeAddressUrl(){
		url = String.format(ACTION_GET_CHANGE_ADDRESS);
		return new StringBuffer(ConfigUtil.HTTP_URL).append(url).toString();
	}
}
