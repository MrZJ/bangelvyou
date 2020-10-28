package com.shenmailogistics.system.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/5.
 */

public class TraceEntity {

    public List<TraceItemEntity> list;

    public class TraceItemEntity{
        public String start_date;
        public String end_date;
        public String car_id;
        public String record_id;
        public String mileage;
    }
}
