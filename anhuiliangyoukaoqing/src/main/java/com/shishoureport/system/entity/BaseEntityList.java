package com.shishoureport.system.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/7.
 * copyright easybiz.
 */

public class BaseEntityList implements Serializable {
    public List<BaseDataEntity> entity90List;
    public List<BaseDataEntity> entity100List;
    public List<BaseDataEntity> entity110List;
    public List<BaseDataEntity> entity500List;
    public List<User> busInfoList;
}
