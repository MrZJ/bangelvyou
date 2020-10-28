package com.zycreview.system.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/1/6 0006.
 *
 * 检验管理保存界面实体类
 */

public class CheckDetailEntity {

    public String holdz_cond;//贮藏条件
    public List<CheckItem> methods;//检验项目
    public String rm_norm;//执行标准
    public String testDate;//检验时间
    public String testMan;//检验人员
    public String testName;//检验名称

    public class CheckItem{
        public String item_name;//检验项目名
        public String standrad;//检验描述
        public String result;//检验结果
    }
}
