package com.basulvyou.system.adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.basulvyou.system.R;
import com.basulvyou.system.entity.LocationEntity;
import com.basulvyou.system.util.AsynImageUtil;
import com.basulvyou.system.util.DensityUtil;
import com.basulvyou.system.util.ListUtils;
import com.basulvyou.system.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 当地特产列表数据显示适配器
 */
public class LocationGoodsAdapter extends AbsBaseAdapter<LocationEntity> {

    private List<LocationEntity> mData;
    private LayoutInflater inflater;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private Context context;
    private int width;
    //定义两个int常量标记不同的Item视图
    public static final int IMA_DES = 0;
    public static final int DES_IMA = 1;
    public boolean isCate = false;

    public LocationGoodsAdapter(List<LocationEntity> branchInfos, Context context) {
        mData = branchInfos;
        this.context = context;
        inflater = LayoutInflater.from(context);
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        width = (dm.widthPixels-DensityUtil.dip2px(context, 20)) / 2;//屏幕一半宽度
    }

    public int getCount() {
        if (null == mData) {
            return 0;
        }
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position % 2 == 0) {
            return IMA_DES;
        } else {
            return DES_IMA;
        }
    }

    @Override
    public int getViewTypeCount() {
        //因为有两种视图，所以返回2
        return 2;
    }

    public LocationEntity getItem(int position) {
        return mData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            if(getItemViewType(position) == IMA_DES){
                convertView = inflater.inflate(R.layout.item_location_goods_one, null);
                holder.reLDes = convertView.findViewById(R.id.rel_item_location_goods_img_one);
                holder.tv_goods_title = (TextView) convertView.findViewById(R.id.tv_item_location_goods_title);
                holder.img_goods_image = (ImageView) convertView.findViewById(R.id.img_item_location_goods_image);
                holder.tv_goods_name = (TextView) convertView.findViewById(R.id.tv_item_location_goods_name);
                holder.tv_goods_des = (TextView) convertView.findViewById(R.id.tv_item_location_goods_des);
                holder.tv_goods_prices = (TextView) convertView.findViewById(R.id.tv_item_location_goods_price);
            } else {
                convertView = inflater.inflate(R.layout.item_location_goods_two, null);
                holder.reLDes = convertView.findViewById(R.id.rel_item_location_goods_img_two);
                holder.img_goods_image = (ImageView) convertView.findViewById(R.id.img_item_location_goods_image_two);
                holder.tv_goods_name = (TextView) convertView.findViewById(R.id.tv_item_location_goods_name_two);
                holder.tv_goods_des = (TextView) convertView.findViewById(R.id.tv_item_location_goods_des_two);
                holder.tv_goods_prices = (TextView) convertView.findViewById(R.id.tv_item_location_goods_price_two);
            }
            LinearLayout.LayoutParams desParams = (LinearLayout.LayoutParams) holder.reLDes.getLayoutParams();
            LinearLayout.LayoutParams imaParams = (LinearLayout.LayoutParams) holder.img_goods_image.getLayoutParams();
            int desWidth = width + DensityUtil.dip2px(context, 25) ;//文字宽度
            int imgWidth = width - DensityUtil.dip2px(context, 25);// 图片宽度
            desParams.width = desWidth;
            desParams.height = desWidth * 2 / 3;
            imaParams.width = imgWidth;
            imaParams.height = imgWidth * 2 / 3;
            if(getItemViewType(position) == IMA_DES){
                desParams.leftMargin = DensityUtil.dip2px(context, 5);
            }else{
                desParams.rightMargin = DensityUtil.dip2px(context, 5);
            }
            holder.reLDes.setLayoutParams(desParams);
            int maxLine = ((desWidth * 2 / 3)-DensityUtil.dip2px(context, 40)) / DensityUtil.dip2px(context, 20);
            holder.tv_goods_des.setMaxLines(maxLine);
            holder.img_goods_image.setLayoutParams(imaParams);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(getItemViewType(position) == IMA_DES){
            if(!isCate){
                if (position != 0){
                    holder.tv_goods_title.setVisibility(View.GONE);
                } else {
                    holder.tv_goods_title.setVisibility(View.VISIBLE);
                }
            }else{
                holder.tv_goods_title.setVisibility(View.GONE);
            }
        }
        LocationEntity viewEntity = getItem(position);
        if(!StringUtil.isEmpty(viewEntity.location_img_one)){
            imageLoader.displayImage(viewEntity.location_img_one, holder.img_goods_image,
                    AsynImageUtil.getImageOptions(R.mipmap.location_view), null);
        }else{
            if(!ListUtils.isEmpty(viewEntity.location_img)){
                imageLoader.displayImage(viewEntity.location_img.get(0).comm_image_url, holder.img_goods_image,
                        AsynImageUtil.getImageOptions(R.mipmap.location_view), null);
            }
        }
        holder.tv_goods_name.setText(viewEntity.location_title);
        holder.tv_goods_des.setText(viewEntity.location_brief);
        int Nstart = viewEntity.location_price.indexOf("￥")+1;
        SpannableStringBuilder style = new SpannableStringBuilder(viewEntity.location_price);
        style.setSpan(new TextAppearanceSpan(context, R.style.LocationItemTextBlack_10), 0, Nstart, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new TextAppearanceSpan(context, R.style.LocationItemTextGoodsPrice), Nstart, viewEntity.location_price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv_goods_prices.setText(style);
        return convertView;
    }

    private class ViewHolder {
        TextView tv_goods_title, tv_goods_name, tv_goods_des , tv_goods_prices;
        ImageView img_goods_image;
        View reLDes;
    }

    public void setData(List<LocationEntity> viewEntity) {
        mData = viewEntity;
    }

    public void isCateList(boolean isCate){
        this.isCate = isCate;
    }

}
