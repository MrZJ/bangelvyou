package com.zycreview.system.entity;

import java.util.List;

/**
 * 获得需要添加的基地和药材的数据
 *
 * Created by Administrator on 2016/9/28 0028.
 */
public class PlantAddDataEntity {
    public List<IngredInfos> ingredInfos;//药材信息
    public List<PlantBases> plantBases;//基地信息
    public List<PlantWay> zzfsList;//方式信息

    public class IngredInfos{
        public String id;
        public String ingred_code;
        public String ingred_name;
    }

    public class PlantBases{
        public String id;
        public String base_code_in;
        public String base_name;
    }

    public class PlantWay{
        public String grow_way;
        public String type_value;
    }
}
