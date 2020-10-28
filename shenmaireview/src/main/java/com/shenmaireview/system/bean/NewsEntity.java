package com.shenmaireview.system.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/24.
 */

public class NewsEntity {

    public List<News> list;

    public class News{
        public String id;
        public String pub_time;
        public String title;
        public String source;
        public String view_count;
    }
}
