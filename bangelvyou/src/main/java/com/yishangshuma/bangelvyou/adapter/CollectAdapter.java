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
import com.yishangshuma.bangelvyou.entity.CollectEntity;
import com.yishangshuma.bangelvyou.util.AsynImageUtil;

import java.util.List;

/**
 * 我的收藏列表适配器
 *
 */
public class CollectAdapter extends BaseAdapter {

	private List<CollectEntity> mData;
	LayoutInflater inflater;
	int width;
	private ImageLoader imageLoader = ImageLoader.getInstance();

	public CollectAdapter(List<CollectEntity> branchInfos, Context context) {
		mData = branchInfos;
		inflater = LayoutInflater.from(context);
		/*DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();   
		width = dm.widthPixels;//宽度
*/	}

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

	public void setData(List<CollectEntity> collectEntity) {
		mData = collectEntity;
	}
}
