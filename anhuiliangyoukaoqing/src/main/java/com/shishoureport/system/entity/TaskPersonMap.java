package com.shishoureport.system.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/13.
 * copyright easybiz.
 */

public class TaskPersonMap implements Serializable {
    public List<TaskPersonList> taskLinkInfoList;
    public List<TaskFileEntity> bfList;
    public boolean is_first;
    public String real_name;
}
