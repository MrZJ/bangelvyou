package com.zycreview.system.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/1/6 0006.
 */

public class CheckTaskList {

    public List<CheckTaskEntity> list;

    public class CheckTaskEntity {
        public String receive_id;//采收批次ID
        public String drugs_name;//药材名称
        public String receive_date;//采收时间
        public String receive_weight;//采收重量
        public String baseName;//基地名称

        public String test_id;
        public String test_name;
        public String test_date;
        public String test_man;
    }
}
