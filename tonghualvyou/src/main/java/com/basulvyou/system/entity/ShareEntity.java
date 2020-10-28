package com.basulvyou.system.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 热门分享信息实体类
 */
public class ShareEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    public String add_date;//分享时间
    public String add_name;//分享者名次
    public String addr;//分享的地址
    public String friend_id;//分享的id
    public String summary;//分享时间
    public String title;//分享的标题
    public String user_logo;//分享者的图像
    public String view_count;//浏览量
    public String ok_count;//点赞数
    public String comment_count;//评论数
    public List<ShareImgEntity> friend_list;//图片集合


}
