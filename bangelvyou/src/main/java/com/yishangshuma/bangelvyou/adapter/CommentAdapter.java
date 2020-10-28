package com.yishangshuma.bangelvyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.entity.CommentInfoEntity;
import com.yishangshuma.bangelvyou.wibget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.List;

/**
 * 评论适配器
 */
public class CommentAdapter extends  AbsBaseAdapter<CommentInfoEntity>{

    private List<CommentInfoEntity> mData;
    private LayoutInflater inflater;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public CommentAdapter(List<CommentInfoEntity> branchInfos, Context context) {
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
			convertView = inflater.inflate(R.layout.item_tourist_comment, null);
			holder = new ViewHolder();
			holder.img_tourist_icon = (CircleImageView) convertView
					.findViewById(R.id.item_comment_tourist_icon);
			holder.tv_tourist_name = (TextView) convertView
					.findViewById(R.id.item_comment_tourist_name);
            holder.tv_tourist_level = (TextView) convertView
                    .findViewById(R.id.item_comment_tourist_level);
            holder.tv_tourist_commentdate = (TextView) convertView
                    .findViewById(R.id.item_comment_tourist_date);
            holder.rating_level = (RatingBar) convertView
                    .findViewById(R.id.item_comment_tourist_rating);
            holder.tv_tourist_commentcontent = (TextView) convertView
                    .findViewById(R.id.item_comment_tourist_content);
            holder.lookMore = convertView.findViewById(R.id.item_comment_tourist_more);
            holder.tv_tourist_commentcontent.setEllipsize(null);
            holder.tv_tourist_commentcontent.setSingleLine(false);
            holder.lookMore.setVisibility(View.GONE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CommentInfoEntity entity = getItem(position);
        imageLoader.displayImage(entity.comment_user_icon,holder.img_tourist_icon);
        holder.tv_tourist_name.setText(entity.comment_user_name);
        holder.tv_tourist_level.setText("Lv"+entity.comment_user_level);
        holder.tv_tourist_commentdate.setText(entity.comment_date);
        holder.rating_level.setRating(Float.parseFloat(entity.comment_level));
        holder.tv_tourist_commentcontent.setText(entity.comment_content);
        return convertView;
    }

    private class ViewHolder {
        TextView tv_tourist_name, tv_tourist_level, tv_tourist_commentdate,tv_tourist_commentcontent;
        CircleImageView img_tourist_icon;
        View lookMore;
        RatingBar rating_level;
    }

    public void setData(List<CommentInfoEntity> entity) {
        mData = entity;
    }
}
