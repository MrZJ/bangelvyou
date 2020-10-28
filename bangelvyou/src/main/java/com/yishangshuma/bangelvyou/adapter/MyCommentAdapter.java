package com.yishangshuma.bangelvyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.entity.CommentInfoEntity;

import java.util.List;

/**
 * 我的评论适配
 */
public class MyCommentAdapter extends  AbsBaseAdapter<CommentInfoEntity>{

    private List<CommentInfoEntity> mData;
    private LayoutInflater inflater;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public MyCommentAdapter(List<CommentInfoEntity> branchInfos, Context context) {
        mData = branchInfos;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {

        if (null == mData) {
            return 0;
        }
        return mData.size();
    }

    public CommentInfoEntity getItem(int position) {

        return mData.get(position);
    }

    public long getItemId(int position) {

        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_my_comment, null);
            holder = new ViewHolder();
            holder.tv_goods_name = (TextView) convertView
                    .findViewById(R.id.item_my_comment_name);
            holder.tv_tourist_commentdate = (TextView) convertView
                    .findViewById(R.id.item_my_comment_date);
            holder.rating_level = (RatingBar) convertView
                    .findViewById(R.id.item_my_comment_rating);
            holder.tv_tourist_commentcontent = (TextView) convertView
                    .findViewById(R.id.item_my_comment_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CommentInfoEntity entity = getItem(position);
        holder.tv_goods_name.setText(entity.comment_name);
        holder.tv_tourist_commentdate.setText(entity.comment_date);
        holder.rating_level.setRating(Float.parseFloat(entity.comment_level));
        holder.tv_tourist_commentcontent.setText(entity.comment_content);
        return convertView;
    }

    private class ViewHolder {
        TextView tv_goods_name, tv_tourist_commentdate,tv_tourist_commentcontent;
        RatingBar rating_level;
    }

    public void setData(List<CommentInfoEntity> entity) {
        mData = entity;
    }
}
