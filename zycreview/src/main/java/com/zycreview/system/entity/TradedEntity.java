package com.zycreview.system.entity;

import java.io.Serializable;

/**
 * 交易单信息实体类
 * 已生成
 */
public class TradedEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public String trade_id;//交易id
    public String caigou_entp_name;//采购企业
    public String trade_date;//交易时间
    public String trade_money;//交易金额
    public String trade_state;//交易状态
    public String trade_uname;//交易人员
    public String trade_uname_id;//交易人员id
    public String trade_no;//交易批次号
    public String bsi_code_in;//采购方企业代码
}
