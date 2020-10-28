package com.basulvyou.system.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 拼车信息实体类
 */
public class CarpoolingInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public String add_user_id;//联系人的id
    public String comm_id;//标识
    public String comm_name;
    public String comm_price;
    public String down_date;//用车时间
    public String up_date;//发布时间
    public String goods_content;//联系人电话
    public String inventory;//座位
    public List<RescuePicEntity> main_pics;//图片
    public String pcqd;//拼车起点
    public String pczd;//拼车重点
    public String pd_stock;//剩余座位
    public String goods_price;//价格
    public String status;//状态
    public String comm_type;//拼车类型
    public String distance;//距离
    public String time;//已发布的时间
    public String goods_id;//编码
    public String fctime;//发车时间
    public String order_state;//订单状态
    public String goods_name;//救援名称
    public String goods_jdcontent;//救援工作时间
    public String goods_qtcontent;//救援单位地址
    public String seo_title;//救援单位首字母
    public String p_name;//救援小名称
    public String order_id;//拼车订单id
    public String goods_latlng;//救援经纬坐标
    public List<PassengerEntity> order_list;//乘客
    public String sale_count; // 实际拼车人数
    public String rel_name;//姓名
    public String rel_phone;//电话
    public String remark;
    public String sccomm;
    public String evaluation_count;
    public String credit;//评分
    public String user_logo;// 用户头像
    public String order_pic;
    public String user_name;
}
