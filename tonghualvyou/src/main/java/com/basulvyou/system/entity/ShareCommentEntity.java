package com.basulvyou.system.entity;

import java.util.List;

/**
 * 分享评论数据实体类
 */
public class ShareCommentEntity {

    public String add_date;//评论的时间
    public String add_name;//评论人名称
    public String add_user_id;//评论人id
    public String comment_content;//评论内容
    public String comment_id;//评论id
    public List<ShareChildrenCommentEntity> comment_list_fb;//子评论
    public String user_logo;//评论人头像
    public String ok_count;//点赞数

}
