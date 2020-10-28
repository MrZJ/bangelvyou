package com.shenmai.system.entity;

import java.io.Serializable;

/**
 * 广告信息实体类
 */
public class AdsEntity implements Serializable{
    public String adv_id;// 广告ID
    public String adv_title;// 标题
    public String image_path;// 图片路径
    public String link_url;// 跳转URL(首页是商品ID)App标签是首页banner 广告等方式
                                //	http:// 专题外链
                                //	**XXXXX** app标签搜索 appkey= xxxx
                                //	&&XXXX&& 关键词搜索 keyword=xxxx
                                //	##23## 商品内容 goods_id=
                                //	$$2011$$ 分类ID store_id=
    public String adv_word;// 描述
}
