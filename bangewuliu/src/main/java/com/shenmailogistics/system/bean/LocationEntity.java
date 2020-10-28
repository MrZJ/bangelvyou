package com.shenmailogistics.system.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/31.
 */

public class LocationEntity {

    public List<LocationItemEntity> list;

    public class LocationItemEntity{
        public Double lon;
        public Double lat;
    }
}
