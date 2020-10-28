package com.zycreview.system.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/1/6 0006.
 *
 * 检验管理保存界面实体类
 */

public class CheckManageEntity {

    public CheckManageDetail entity;
    public List<RmNorm> rm_norm;//执行标准（集合）
    public List<HoldzCond> holdz_cond;//贮藏条件（集合）
    public List<TestMethods> testItemList;//检验项目（集合）
    public List<TestPerson> user_list;

    public class CheckManageDetail{
        public String receive_id;//采收批次ID
        public String drugs_name;//药材名称
        public String drugs_code;//药材编码
        public String receive_weight;//药材重量
        public String receive_date;//采收时间
        public String receive_man;//采收人
        public String test_name;//采收人
        public String receive_no_in;//采收批次号
        public String receive_w_unit;//单位
    }

    public class RmNorm{
        public String name;//名称
        public String type;//类型
        public String value;//值
    }

    public class HoldzCond{
        public String name;//名称
        public String type;//类型
        public String value;//值
    }

    public class TestMethods{
        public String item_name;//检验名称
        public String standrad;//检验说明
        public String item_id;//id
    }
}
