package com.basulvyou.system.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.basulvyou.system.R;
import com.basulvyou.system.entity.ShareEntity;
import com.basulvyou.system.ui.activity.GalleyImageActivity;
import com.basulvyou.system.ui.activity.ShareDetailActivity;
import com.basulvyou.system.util.AsynImageUtil;
import com.basulvyou.system.util.ListUtils;
import com.basulvyou.system.util.StringUtil;
import com.basulvyou.system.wibget.CircleImageView;
import com.basulvyou.system.wibget.MyGridView;
import com.basulvyou.system.wibget.RoundedImage.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.List;

/**
 * Created by KevinLi on 2016/1/28.
 */
public class HomeTodayHotAdapter extends AbsBaseAdapter<ShareEntity>{

    private List<ShareEntity> mData;
    private LayoutInflater inflater;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private Context ctx;
    private OnZanClickListenter listenter;

    public HomeTodayHotAdapter(List<ShareEntity> branchInfos, Context context) {
        mData = branchInfos;
        inflater = LayoutInflater.from(context);
        this.ctx = context;
    }

    public int getCount() {

        if (null == mData) {
            return 0;
        }
        return mData.size();
    }

    public ShareEntity getItem(int position) {

        return mData.get(position);
    }

    public long getItemId(int position) {

        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_home_today_hot, null);
            holder = new ViewHolder();
            holder.share_item_layout = convertView.findViewById(R.id.share_item_layout);
            holder.divider_today_hot = (View) convertView
                    .findViewById(R.id.divider_today_hot);
            holder.ll_today_hot = (View) convertView
                    .findViewById(R.id.ll_today_hot);
            holder.item_home_line =
                    convertView.findViewById(R.id.item_home_line);
            holder.user_icon = (CircleImageView) convertView.findViewById(R.id.img_today_hot_user_icon);
            /*holder.tv_today_hot_num = (TextView) convertView
                    .findViewById(R.id.tv_today_hot_num);*/
            holder.tv_today_hot_name = (TextView) convertView
                    .findViewById(R.id.tv_today_hot_name);
            holder.tv_today_hot_content = (TextView) convertView.findViewById(R.id.tv_today_hot_content);
            /*holder.img_today_hot = (RoundedImageView) convertView
                    .findViewById(R.id.img_today_hot);
            holder.img_today_hot2 = (RoundedImageView) convertView
                    .findViewById(R.id.img_today_hot2);
            holder.img_today_hot3 = (RoundedImageView) convertView
                    .findViewById(R.id.img_today_hot3);*/
            holder.item_gv_img = (MyGridView) convertView.findViewById(R.id.item_gv_img);
            holder.tv_today_hot_date = (TextView) convertView.findViewById(R.id.tv_today_hot_date);
            holder.tv_today_hot_ads = (TextView) convertView.findViewById(R.id.tv_today_hot_ads);
            holder.tv_today_hot_goods = (TextView) convertView.findViewById(R.id.tv_today_hot_viewnum);
            holder.tv_today_hot_comm = (TextView) convertView.findViewById(R.id.tv_today_hot_commnum);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ShareEntity shareEntity = getItem(position);
        if (shareEntity == null || shareEntity.friend_id == null) {
            holder.share_item_layout.setVisibility(View.GONE);
        } else {
            holder.share_item_layout.setVisibility(View.VISIBLE);
        }
        final Intent galley = new Intent(ctx, GalleyImageActivity.class);
        if(!ListUtils.isEmpty(shareEntity.friend_list)){
            galley.putExtra("imgList", (Serializable) shareEntity.friend_list);
        }
        if (!ListUtils.isEmpty(shareEntity.friend_list)) {
            holder.item_gv_img.setVisibility(View.VISIBLE);
            MyWordsGridAdapter adapter = new MyWordsGridAdapter(ctx, shareEntity.friend_list);
            holder.item_gv_img.setAdapter(adapter);
            holder.item_gv_img.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    galley.putExtra("index", position);
                    ctx.startActivity(galley);
                }
            });
            final ViewHolder finalHolder = holder;
            final ShareEntity finalShareEntity = shareEntity;
            final int num = 3;//列数
            holder.item_gv_img.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        int w = finalHolder.item_gv_img.getMeasuredWidth() / num;
                        int yP = finalHolder.item_gv_img.getLastVisiblePosition() / num;
                        int xP = finalHolder.item_gv_img.getLastVisiblePosition() + 1 - yP * num;
                        int height = w * yP;
                        int width = w * xP;
                        if (event.getY() > height && event.getX() > width) {
                            Intent intent = new Intent(ctx, ShareDetailActivity.class);
                            intent.putExtra("shareId", finalShareEntity.friend_id);
                            intent.putExtra("commentNum", finalShareEntity.comment_count);
                            intent.putExtra("shareTitle", finalShareEntity.title);
                            ctx.startActivity(intent);
                            return true;
                        }
                        return false;
                    }
                    return false;
                }
            });
        } else {
            holder.item_gv_img.setAdapter(null);
            holder.item_gv_img.setVisibility(View.GONE);
        }

        if(position > 0){
            holder.divider_today_hot.setVisibility(View.GONE);
            holder.item_home_line.setVisibility(View.VISIBLE);
            /*holder.ll_today_hot.setVisibility(View.GONE);*/
        } else {
            holder.divider_today_hot.setVisibility(View.VISIBLE);
            holder.item_home_line.setVisibility(View.GONE);
            /*holder.ll_today_hot.setVisibility(View.VISIBLE);*/
        }
        /*holder.tv_today_hot_num.setText(locationEntity.location_collect_count);*/
        holder.user_icon.setImageResource(0);//清空背景图片
        holder.user_icon.setImageBitmap(null);//清空背景图片
        if (!StringUtil.isEmpty(shareEntity.user_logo)) {
            imageLoader.displayImage(shareEntity.user_logo, holder.user_icon,
                    AsynImageUtil.getImageOptions(), null);
        } else {
            holder.user_icon.setImageResource(R.mipmap.mine_toux);
        }
        holder.tv_today_hot_name.setText(shareEntity.add_name);
        holder.tv_today_hot_content.setText(shareEntity.summary);
        holder.tv_today_hot_date.setText(shareEntity.add_date);
        holder.tv_today_hot_ads.setText(shareEntity.addr);
        /*holder.tv_today_hot_view.setText(" "+shareEntity.ok_count);
        holder.tv_today_hot_comm.setText(" "+shareEntity.comment_count);*/
        holder.tv_today_hot_goods.setText("浏览 "+shareEntity.view_count);
        holder.tv_today_hot_comm.setText("评论 "+shareEntity.comment_count);
        /*if(!StringUtil.isEmpty(shareEntity.friend_id)) {
            final String shareId = shareEntity.friend_id;
            holder.tv_today_hot_comm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent commentIntent = new Intent(ctx, ShareCommentActivity.class);
                        commentIntent.putExtra("shareId", shareId);
                        ctx.startActivity(commentIntent);
                }
            });
            final View finalConvertView = convertView;
            holder.tv_today_hot_goods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenter.zan(shareId, finalConvertView,position);
                }
            });
        }*/
        return convertView;
    }

    private class ViewHolder {
        View divider_today_hot, ll_today_hot, share_item_layout, item_home_line;
        TextView /*tv_today_hot_num,*/tv_today_hot_name,tv_today_hot_content,tv_today_hot_date,tv_today_hot_ads,tv_today_hot_goods,tv_today_hot_comm;
        CircleImageView user_icon;
        MyGridView item_gv_img;
        RoundedImageView img_today_hot,img_today_hot2,img_today_hot3;
    }

    public void setData(List<ShareEntity> shopEntity) {
        mData = shopEntity;
    }

    public interface OnZanClickListenter {
        void zan(String shareId,View clickView,int position);
    }

    public void setOnZanClickListenter(OnZanClickListenter onZanClickListenter) {
        listenter = onZanClickListenter;
    }
}
