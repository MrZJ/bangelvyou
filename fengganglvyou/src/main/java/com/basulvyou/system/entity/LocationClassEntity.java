package com.basulvyou.system.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/8 0008.
 */
public class LocationClassEntity implements Serializable{
    public int gc_id;
    public String gc_name;

    public LocationClassEntity(int classId, String className) {
        this.gc_id = classId;
        this.gc_name = className;
    }

    public LocationClassEntity(){}

    public int getClassId() {
        return gc_id;
    }

    public void setClassId(int classId) {
        this.gc_id = classId;
    }

    public String getClassName() {
        return gc_name;
    }

    public void setClassName(String className) {
        this.gc_name = className;
    }


}
