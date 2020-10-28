package com.basulvyou.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.basulvyou.system.R;
import com.basulvyou.system.entity.BDZXEntity;
import com.basulvyou.system.entity.LYJDEntity;
import com.basulvyou.system.util.AsynImageUtil;
import com.basulvyou.system.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @autor jianzhang
 * Version 1.0.0.
 * <p>
 * Date 2017/5/18.
 * <p>
 * Copyright iflytek.com
 */

public class HomeTopAdapter extends BaseAdapter {
    public interface ListItemClickInterface {
        void onListItemClick(Object o);
    }

    private Context mContext;
    private List<Object> objects;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    public ListItemClickInterface onItemClick;
    private boolean isFirstZX = true;
    private boolean isFirstJD = true;
    private int mFirstJDPosition;
    private int mFirstZXPosition;

    public HomeTopAdapter(Context context, ListItemClickInterface onItemClick) {
        mContext = context;
        objects = new ArrayList<>();
        this.onItemClick = onItemClick;
    }


    public void addList(List<Object> list) {
        objects.addAll(list);
    }
    public void replaceList(List<Object> list) {
        objects = list;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_home_top, null);
            holder = new ViewHolder();
            holder.jd_layout = convertView.findViewById(R.id.jd_layout);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.textView = (TextView) convertView.findViewById(R.id.text);
            holder.title_layoutJD = convertView.findViewById(R.id.title_layout_jd);
            holder.titleView_jd = (TextView) convertView.findViewById(R.id.title_name_jd);

            holder.zx_layout = convertView.findViewById(R.id.zx_layout);
            holder.divider_today_hot = convertView.findViewById(R.id.divider_today_hot);
            holder.ll_today_hot = convertView.findViewById(R.id.ll_today_hot);
            holder.img_today_hot = (ImageView) convertView.findViewById(R.id.img_today_hot);
            holder.tv_today_hot_name = (TextView) convertView.findViewById(R.id.tv_today_hot_name);
            holder.tv_today_hot_des = (TextView) convertView.findViewById(R.id.tv_today_hot_des);
            holder.titleView_zx = (TextView) convertView.findViewById(R.id.title_name_zx);
            holder.title_layoutZX = convertView.findViewById(R.id.title_layout_zx);
            holder.lastDivideLine = convertView.findViewById(R.id.divide_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Object object = objects.get(position);
        String picUrl = null;
        if (object instanceof BDZXEntity) {
            if (isFirstZX) {
                isFirstZX = false;
                mFirstZXPosition = position;
            }
            if (mFirstZXPosition == position) {
                holder.title_layoutZX.setVisibility(View.VISIBLE);
                holder.titleView_zx.setText("本地资讯");
            } else {
                holder.title_layoutZX.setVisibility(View.GONE);
            }
            holder.jd_layout.setVisibility(View.GONE);
            holder.zx_layout.setVisibility(View.VISIBLE);
            BDZXEntity bdzxEntity = (BDZXEntity) object;
            holder.tv_today_hot_name.setText(bdzxEntity.getN_title());
            holder.tv_today_hot_des.setText(bdzxEntity.getN_brief());
            picUrl = bdzxEntity.getN_main_img();
            holder.img_today_hot.setImageResource(0);//清空背景图片
            holder.img_today_hot.setImageBitmap(null);//清空背景图片
            if (!StringUtil.isEmpty(picUrl)) {
                imageLoader.displayImage(picUrl, holder.img_today_hot,
                        AsynImageUtil.getImageOptions(R.mipmap.location_view), null);
            } else {
                holder.img_today_hot.setImageResource(R.mipmap.detail_pic);
            }
        }
        if (object instanceof LYJDEntity) {
            if (isFirstJD) {
                isFirstJD = false;
                mFirstJDPosition = position;
            }
            if (mFirstJDPosition == position) {
                holder.title_layoutJD.setVisibility(View.VISIBLE);
                holder.titleView_jd.setText("旅游景点");
            } else {
                holder.title_layoutJD.setVisibility(View.GONE);
            }
            if (position == (objects.size() - 1)) {
                holder.lastDivideLine.setVisibility(View.GONE);
            }
            holder.jd_layout.setVisibility(View.VISIBLE);
            holder.zx_layout.setVisibility(View.GONE);
            LYJDEntity lyjdEntity = (LYJDEntity) object;
            picUrl = lyjdEntity.getGoods_image_url();
            holder.textView.setText(lyjdEntity.getGoods_name());
            holder.image.setImageResource(0);//清空背景图片
            holder.image.setImageBitmap(null);//清空背景图片
            if (!StringUtil.isEmpty(picUrl)) {
                imageLoader.displayImage(picUrl, holder.image,
                        AsynImageUtil.getImageOptions(R.mipmap.detail_pic), null);
            } else {
                holder.image.setImageResource(R.mipmap.detail_pic);
            }
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick != null) {
                    onItemClick.onListItemClick(object);
                }

            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView image;
        View divider_today_hot, ll_today_hot, zx_layout, jd_layout, title_layoutJD, title_layoutZX, lastDivideLine;
        TextView tv_today_hot_name, tv_today_hot_des, textView, titleView_zx, titleView_jd;
        ImageView img_today_hot;
    }
}
