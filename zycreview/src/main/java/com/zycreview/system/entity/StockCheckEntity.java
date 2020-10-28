package com.zycreview.system.entity;

import java.io.Serializable;

/**
 * 库存查询信息实体类
 */
public class StockCheckEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    public String drugs_name;//药材名称
    public String drugs_code;//药材编码
    public String input_code;//入库编码
    public String drugs_number;//总重量
    public String drugs_in;//在库量
    public String drugs_out;//出库量
    public String grow_pattern;//生长方式
    public String drugs_unit;//计量单位
    public String drugs_id;//id
}
