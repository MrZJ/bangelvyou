package com.basulvyou.system.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.basulvyou.system.R;
import com.basulvyou.system.entity.SearchShopEntity;
import com.basulvyou.system.util.AsynImageUtil;
import com.basulvyou.system.util.DensityUtil;
import com.basulvyou.system.util.StringUtil;
import com.basulvyou.system.wibget.RoundedImage.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 特产界面适配器
 * Created by KevinLi on 2016/2/4.
 */
public class SpecialAdapter extends AbsBaseAdapter<SearchShopEntity> {

    private Context context;
    private List<SearchShopEntity> mData;
    private LayoutInflater inflater;
    private int width;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public SpecialAdapter(List<SearchShopEntity> branchInfos, Context context) {
        this.context = context;
        mData = branchInfos;
        inflater = LayoutInflater.from(context);
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        width = dm.widthPixels;//宽度
    }

    public int getCount() {

        if (null == mData) {
            return 0;
        }
        return mData.size();
    }

    public SearchShopEntity getItem(int position) {

        return mData.get(position);
    }

    public long getItemId(int position) {

        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_special, null);
            holder = new ViewHolder();
            holder.tv_special_price = (TextView) convertView
                    .findViewById(R.id.tv_special_price);
            holder.tv_special_name = (TextView) convertView
                    .findViewById(R.id.tv_special_name);
            holder.tv_special_like_num = (TextView) convertView
                    .findViewById(R.id.tv_special_like_num);
            holder.img_itm_special = (RoundedImageView) convertView
                    .findViewById(R.id.img_itm_special);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                    holder.img_itm_special.getLayoutParams();
            params.height = (int) ((width - DensityUtil.dip2px(context, 30)) / 2
                    + DensityUtil.dip2px(context, 1));
            holder.img_itm_special.setLayoutParams(params);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SearchShopEntity shopEntity = getItem(position);
        if(!StringUtil.isEmpty(shopEntity.goods_image_url)){
            imageLoader.displayImage(shopEntity.goods_image_url, holder.img_itm_special,
                    AsynImageUtil.getImageOptions(R.mipmap.img_special), null);
        }
        /*if(shopEntity.goods_image_url1.get(0)!=null &&
                !TextUtils.isEmpty(shopEntity.goods_image_url1.get(0).comm_image_url)) {
            imageLoader.displayImage(shopEntity.goods_image_url1.get(0).comm_image_url, holder.img_itm_special,
                    AsynImageUtil.getImageOptions(R.mipmap.img_special), null);
        }*/
        holder.tv_special_name.setText(shopEntity.goods_name);
        holder.tv_special_price.setText("￥"+shopEntity.goods_price);
        holder.tv_special_like_num.setText(shopEntity.sccomm);
        return convertView;
    }

    private class ViewHolder {
        TextView tv_special_name, tv_special_like_num, tv_special_price;
        RoundedImageView img_itm_special;

    }

    public void setData(List<SearchShopEntity> shopEntity) {
        mData = shopEntity;
    }
}
