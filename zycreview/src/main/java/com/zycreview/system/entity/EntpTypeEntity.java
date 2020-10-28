package com.zycreview.system.entity;

import java.util.List;

/**
 * 企业类型
 */
public class EntpTypeEntity {

    public List<EntpType> dataList;

    public class EntpType{
        public String base_id;//类型id
        public String base_name;//类型名称
    }

}
