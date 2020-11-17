package com.shishoureport.system.entity;

import java.io.Serializable;

/**
 * Created by jianzhang.
 * on 2017/9/7.
 * copyright easybiz.
 */

public class ApplyUseEntity implements Serializable {
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
    public String checkRemark;
    public String auditState;
    public String outName;
    public String auditName;
    public String outState;
    public String id;
}
