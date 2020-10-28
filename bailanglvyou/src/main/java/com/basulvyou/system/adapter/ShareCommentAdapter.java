package com.basulvyou.system.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.basulvyou.system.R;
import com.basulvyou.system.entity.ShareChildrenCommentEntity;
import com.basulvyou.system.entity.ShareCommentEntity;
import com.basulvyou.system.util.ListUtils;
import com.basulvyou.system.wibget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 分享评论适配器
 */
public class ShareCommentAdapter extends  AbsBaseAdapter<ShareCommentEntity>{

    private List<ShareCommentEntity> mData;
    private LayoutInflater inflater;
    private CommentClickListenter listenter;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public ShareCommentAdapter(List<ShareCommentEntity> branchInfos, Context context) {
        mData = branchInfos;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {

        if (null == mData) {
            return 0;
        }
        return mData.size();
    }

    public ShareCommentEntity getItem(int position) {

        return mData.get(position);
    }

    public long getItemId(int position) {

        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_comment_details, null);
			holder = new ViewHolder();
            holder.ivItemIcon = (CircleImageView) convertView.findViewById(R.id.iv_item_icon);
            holder.ll_comment = (LinearLayout) convertView.findViewById(R.id.lin_add_child_comment);
            holder.tvItemCommentName = (TextView) convertView.findViewById(R.id.tv_item_comment_name);
            holder.tvItemCommentText = (TextView) convertView.findViewById(R.id.tv_item_comment_text);
            holder.tvItemCommentTime = (TextView) convertView.findViewById(R.id.tv_item_comment_time);
            holder.tvItemCommentReply = (TextView) convertView.findViewById(R.id.tv_item_comment_reply);
            holder.tvItemCommentReplyComment = (TextView) convertView.findViewById(R.id.tv_item_comment_reply_comment);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ShareCommentEntity entity = getItem(position);
        imageLoader.displayImage(entity.user_logo, holder.ivItemIcon);
        holder.tvItemCommentName.setText(entity.add_name);
        holder.tvItemCommentText.setText(entity.comment_content);
        holder.tvItemCommentTime.setText(entity.add_date);
        holder.tvItemCommentReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenter.onClickComment(entity.comment_id, entity.add_user_id);
            }
        });
        holder.ll_comment.removeAllViews();
        if(!ListUtils.isEmpty(entity.comment_list_fb)) {
            for (int i = 0; i < entity.comment_list_fb.size(); i++) {
                View view = inflater.inflate(R.layout.item_return_comment, null);
                final ShareChildrenCommentEntity childrenCommentEntity=entity.comment_list_fb.get(i);
                String comment_content = childrenCommentEntity.comment_content;
                String from_name = childrenCommentEntity.from_name;
                String to_name = childrenCommentEntity.to_name;
                String comment_text = from_name + " 回复 " + to_name + ": " + comment_content;
                SpannableStringBuilder builder = new SpannableStringBuilder(comment_text);
                ForegroundColorSpan yelSpan1 = new ForegroundColorSpan(Color.parseColor("#4CB4FB"));
                ForegroundColorSpan yelSpan2 = new ForegroundColorSpan(Color.parseColor("#4CB4FB"));
                ForegroundColorSpan blackSpan = new ForegroundColorSpan(Color.BLACK);
                builder.setSpan(yelSpan1, 0, from_name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(blackSpan, from_name.length(), from_name.length() + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(yelSpan2, from_name.length() + 4, from_name.length() + 4 + to_name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                TextView tvReturnComment = (TextView) view.findViewById(R.id.tv_item_comment_reply_comment);
                tvReturnComment.setText(builder);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listenter.onClickComment(entity.comment_id,childrenCommentEntity.from_user_id);
                    }
                });
                holder.ll_comment.addView(view);
            }
        }
        return convertView;
    }

    private class ViewHolder {
        CircleImageView ivItemIcon;
        TextView tvItemCommentName,tvItemCommentText, tvItemCommentTime,tvItemCommentReply, tvItemCommentReplyComment;
        LinearLayout ll_comment;
    }

    public void setData(List<ShareCommentEntity> entity) {
        mData = entity;
    }

    public interface CommentClickListenter{
        void onClickComment(String commentId, String toId);
    }

    public void setCommentClickListener(CommentClickListenter listener){
        this.listenter = listener;
    }
}
