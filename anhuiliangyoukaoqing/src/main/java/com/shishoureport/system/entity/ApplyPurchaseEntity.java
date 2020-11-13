package com.shishoureport.system.entity;

import java.io.Serializable;

/**
 * @date
 **/
public class ApplyPurchaseEntity implements Serializable {
    public String batch;
    public String place;
    public String applyId;
    public String applyName;
    public String reciveId;
    public String reciveName;
    public String reciveDate;
    public String reagentIds;
    public String quantitys;
    public String remarks;
    public String remark;
    public int is_submit = 1;
    public String audit_uid;
    public String audit_name;
    public String auditState;
    public String checkRemark;
}
//yyyy-MM-dd HH:mm:ss
