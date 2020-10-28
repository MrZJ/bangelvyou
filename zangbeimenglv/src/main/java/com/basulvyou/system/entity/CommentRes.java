package com.basulvyou.system.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/6/22.
 * copyright easybiz.
 */

public class CommentRes implements Serializable {
    public String countcp;
    public String counthp;
    public String countzp;
    public List<CommentItem> comment_list;
}
