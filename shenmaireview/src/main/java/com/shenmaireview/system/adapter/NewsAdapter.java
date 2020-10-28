package com.shenmaireview.system.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shenmaireview.system.R;
import com.shenmaireview.system.bean.NewsEntity;

/**
 * Created by Administrator on 2017/3/24.
 */

public class NewsAdapter extends BaseQuickAdapter<NewsEntity.News,BaseViewHolder>{

    public NewsAdapter() {
        super(R.layout.item_news_info);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsEntity.News item) {
        helper.setText(R.id.tv_item_news_title,item.title);
        helper.setText(R.id.tv_item_news_sender,item.source);
        helper.setText(R.id.tv_item_news_senddate,item.pub_time);
        helper.setText(R.id.tv_item_news_readnum,"阅读"+item.view_count);
    }
}
