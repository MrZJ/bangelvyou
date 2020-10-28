package com.basulvyou.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.basulvyou.system.R;
import com.basulvyou.system.entity.CollectEntity;
import com.basulvyou.system.util.AsynImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 浏览历史适配器
 * Created by KevinLi on 2016/3/21.
 */
public class BrowsingHistoryAdapter extends AbsBaseAdapter<CollectEntity> {

    private List<CollectEntity> mData;
    private LayoutInflater inflater;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public BrowsingHistoryAdapter(List<CollectEntity> branchInfos, Context context) {
        mData = branchInfos;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {

        if (null == mData) {
            return 0;
        }
        return mData.size();
    }

    public CollectEntity getItem(int position) {

        return mData.get(position);
    }

    public long getItemId(int position) {

        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_collect, null);
            holder = new ViewHolder();
			/*AbsListView.LayoutParams param = new AbsListView.LayoutParams(
	                android.view.ViewGroup.LayoutParams.FILL_PARENT, 300);
	        convertView.setLayoutParams(param);*/
            holder.img_goods_pic = (ImageView) convertView
                    .findViewById(R.id.img_collect_img);
            holder.tv_goods_name = (TextView) convertView
                    .findViewById(R.id.tv_collect_goodsname);
            holder.tv_goods_price = (TextView) convertView
                    .findViewById(R.id.tv_collect_goodsprice);
            holder.tv_goods_commentnum = (TextView) convertView
                    .findViewById(R.id.tv_collect_commentnum);
            holder.tv_goods_commentnum.setVisibility(View.GONE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CollectEntity collectEntity = getItem(position);
        imageLoader.displayImage(collectEntity.goods_image_url, holder.img_goods_pic,
                AsynImageUtil.getImageOptions(), null);
        holder.tv_goods_name.setText(collectEntity.goods_name);
        holder.tv_goods_price.setText("￥"+collectEntity.goods_price);
        return convertView;
    }

    private class ViewHolder {
        TextView tv_goods_name, tv_goods_price, tv_goods_commentnum;
        ImageView img_goods_pic;
    }

    public void setData(List<CollectEntity> viewEntity) {
        mData = viewEntity;
    }

}