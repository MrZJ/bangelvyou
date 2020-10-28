package com.shenmai.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shenmai.system.R;
import com.shenmai.system.entity.GoodsEntity;
import com.shenmai.system.utlis.AsynImageUtil;

import java.util.List;

/**
 * 我的收藏适配器
 */
public class CollectAdapter extends AbsBaseAdapter<GoodsEntity> {

    private List<GoodsEntity> mData;
    private Context context;
    LayoutInflater inflater;
    int width;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public CollectAdapter(List<GoodsEntity> branchInfos, Context context) {
        this.context = context;
        mData = branchInfos;
        inflater = LayoutInflater.from(context);
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
            convertView = inflater.inflate(R.layout.item_list_collect, null);
            holder = new ViewHolder();
            holder.tv_goods_name = (TextView) convertView
                    .findViewById(R.id.tv_item_list_collect_goodname);
            holder.tv_goods_type = (TextView) convertView
                    .findViewById(R.id.tv_item_list_collect_goodtype);
            holder.tv_goods_price = (TextView) convertView
                    .findViewById(R.id.tv_item_list_collect_goodprice);
            holder.tv_goods_weiprice = (TextView) convertView
                    .findViewById(R.id.tv_item_list_collect_goodweiprice);
            holder.tv_goods_reback = (TextView) convertView
                    .findViewById(R.id.tv_item_list_collect_goodreback);
            holder.img_goods_img = (ImageView) convertView
                    .findViewById(R.id.img_item_list_collect_goodimg);

            /*RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.img_shop.getLayoutParams();
            params.height = (int) (width - DensityUtil.dip2px(context, 40)) / 2;
            holder.img_shop.setLayoutParams(params);*/

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GoodsEntity goodsEntity = getItem(position);
        imageLoader.displayImage(goodsEntity.goods_image_url, holder.img_goods_img,
                AsynImageUtil.getImageOptions(R.mipmap.goods_image), null);
        holder.tv_goods_name.setText(goodsEntity.goods_name);
        holder.tv_goods_weiprice.setText("价格: ￥" + goodsEntity.goods_price);
        /*holder.tv_goods_weiprice.setText("微价:￥" + goodsEntity.goods_price);
        String rebackString = "佣金:￥" + "100.0";
        SpannableStringBuilder rebackStyle = new SpannableStringBuilder(rebackString);
        rebackStyle.setSpan(new TextAppearanceSpan(context, R.style.GoodsReback), 0, rebackString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv_goods_reback.setText(rebackStyle);*/
        return convertView;
    }

    private class ViewHolder {

        TextView tv_goods_name, tv_goods_type, tv_goods_price, tv_goods_weiprice, tv_goods_reback;
        ImageView img_goods_img;

    }

    public void setData(List<GoodsEntity> goodsEntity) {
        mData = goodsEntity;
    }

}
