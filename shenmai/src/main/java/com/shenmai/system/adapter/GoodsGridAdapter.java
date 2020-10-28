package com.shenmai.system.adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shenmai.system.R;
import com.shenmai.system.entity.ConfigEntity;
import com.shenmai.system.entity.GoodsEntity;
import com.shenmai.system.utlis.AsynImageUtil;
import com.shenmai.system.utlis.ConfigUtil;

import java.util.List;

/**
 * 商品Grid显示适配器
 */
public class GoodsGridAdapter extends AbsBaseAdapter<GoodsEntity> {

    private List<GoodsEntity> mData;
    private Context context;
    LayoutInflater inflater;
    int width;
    private onAddShopListener listener;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ConfigEntity configEntity;

    public GoodsGridAdapter(List<GoodsEntity> branchInfos, Context context) {
        this.context = context;
        mData = branchInfos;
        inflater = LayoutInflater.from(context);
        configEntity = ConfigUtil.loadConfig(context);
        /*DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        width = dm.widthPixels;//宽度*/
    }

    public int getCount() {

        if (null == mData) {
            return 0;
        }
        return mData.size();
    }

    public GoodsEntity getItem(int position) {

        return mData.get(position);
    }

    public long getItemId(int position) {

        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_grid_goods, null);
            holder = new ViewHolder();
            holder.tv_goods_name = (TextView) convertView
                    .findViewById(R.id.tv_item_grid_goodname);
            holder.tv_goods_type = (TextView) convertView
                    .findViewById(R.id.tv_item_grid_goodtype);
            holder.tv_goods_price = (TextView) convertView
                    .findViewById(R.id.tv_item_grid_goodprice);
            holder.tv_goods_reback = (TextView) convertView
                    .findViewById(R.id.tv_item_grid_goodreback);
            holder.tv_goods_state = (TextView) convertView
                    .findViewById(R.id.tv_item_grid_goodstate);
            holder.img_add_shop = (ImageView) convertView
                    .findViewById(R.id.img_item_grid_addshop);
            holder.img_goods_img = (ImageView) convertView
                    .findViewById(R.id.img_item_grid_goodimg);

            /*RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.img_shop.getLayoutParams();
            params.height = (int) (width - DensityUtil.dip2px(context, 40)) / 2;
            holder.img_shop.setLayoutParams(params);*/

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.img_add_shop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                listener.addShop(position);
            }

        });
        GoodsEntity goodsEntity = getItem(position);
        imageLoader.displayImage(goodsEntity.goods_image_url, holder.img_goods_img,
                AsynImageUtil.getImageOptions(R.mipmap.goods_image), null);
        holder.tv_goods_name.setText(goodsEntity.goods_name);
        holder.tv_goods_type.setText(goodsEntity.sub_name);
        String priceString = "价格:￥" + goodsEntity.goods_price;
        int Nstart = priceString.indexOf("￥") + 1;
        SpannableStringBuilder style = new SpannableStringBuilder(priceString);
        style.setSpan(new TextAppearanceSpan(context, R.style.GoodsReback), Nstart, priceString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv_goods_price.setText(style);
        if(goodsEntity.is_has_kc){
            holder.tv_goods_state.setText("有货");
        } else {
            holder.tv_goods_state.setText("无货");
        }
        if(configEntity.userRole.equals("1")){
            holder.tv_goods_reback.setVisibility(View.VISIBLE);
            holder.img_add_shop.setVisibility(View.VISIBLE);
            String rebackString = "佣金:￥"+goodsEntity.yj_price;
            SpannableStringBuilder rebackStyle = new SpannableStringBuilder(rebackString);
            rebackStyle.setSpan(new TextAppearanceSpan(context, R.style.GoodsReback), 0, rebackString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_goods_reback.setText(rebackStyle);
        } else {
            holder.tv_goods_reback.setVisibility(View.GONE);
            holder.img_add_shop.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ViewHolder {

        TextView tv_goods_name, tv_goods_type,tv_goods_price,tv_goods_reback,tv_goods_state;
        ImageView img_add_shop,img_goods_img;

    }

    public void setData(List<GoodsEntity> goodsEntity) {
        mData = goodsEntity;
    }

    public interface onAddShopListener{

        public void addShop(int position);
    }

    public void setOnAddShopListener(onAddShopListener listener){
        this.listener = listener;
    }
}
