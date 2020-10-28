package com.basulvyou.system.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/11 0011.
 */
public class PassengerEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    public String is_pj_ck;
    public String is_pj_zz;
    public String order_id;//拼车订单id
    public String order_pic;//拼车价格
    public String order_state;//拼车状态
    public String rel_name;//乘客名称
    public String rel_phone;//乘客电话
    public String remark;//乘客评价
}
