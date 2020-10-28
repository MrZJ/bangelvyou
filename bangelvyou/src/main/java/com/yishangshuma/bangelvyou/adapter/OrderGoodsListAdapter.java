package com.yishangshuma.bangelvyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.entity.OrderGoodsList;
import com.yishangshuma.bangelvyou.util.AsynImageUtil;

import java.util.List;

/**
 * 订单商品列表适配器
 *
 */
public class OrderGoodsListAdapter extends BaseAdapter {

	private List<OrderGoodsList> mData;
	private LayoutInflater inflater;
	private ImageLoader imageLoader = ImageLoader.getInstance();

	public OrderGoodsListAdapter(List<OrderGoodsList> branchInfos, Context context) {
		mData = branchInfos;
		inflater = LayoutInflater.from(context);
	}

	public int getCount() {

		if (null == mData) {
			return 0;
		}
		return mData.size();
	}

	public OrderGoodsList getItem(int position) {

		return mData.get(position);
	}

	public long getItemId(int position) {

		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_collect, null);//复用收藏列表
			holder = new ViewHolder();
			holder.img_goods_pic = (ImageView) convertView
					.findViewById(R.id.img_collect_img);
			holder.tv_goods_name = (TextView) convertView
					.findViewById(R.id.tv_collect_goodsname);
			holder.tv_goods_price = (TextView) convertView
					.findViewById(R.id.tv_collect_goodsprice);
			holder.tv_goods_num = (TextView) convertView
					.findViewById(R.id.tv_collect_commentnum);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		OrderGoodsList orderGoods = getItem(position);

		holder.tv_goods_name.setText(orderGoods.goods_name);
		holder.tv_goods_price.setText("￥" + orderGoods.goods_price);
		holder.tv_goods_num.setText("已购买" + orderGoods.goods_num + "件");
		imageLoader.displayImage(orderGoods.goods_image_url, holder.img_goods_pic,
				AsynImageUtil.getImageOptions(), null);

		return convertView;
	}

	private class ViewHolder {
		TextView tv_goods_name, tv_goods_price, tv_goods_num;
		ImageView img_goods_pic;

	}

	public void setData(List<OrderGoodsList> orderGoods) {
		mData = orderGoods;
	}
}

