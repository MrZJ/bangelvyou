package com.yishangshuma.bangelvyou.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.entity.TopInfo;
import com.yishangshuma.bangelvyou.util.AsynImageUtil;
import com.yishangshuma.bangelvyou.util.CacheImgUtil;
import com.yishangshuma.bangelvyou.util.ListUtils;

import java.util.List;


public class TopOneAdapter extends RecyclingPagerAdapter {

	private Context ctx;
	private List<TopInfo> topInfos;
	private int size;
	private boolean isInfiniteLoop;
	private OnModelClickListener listener;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private String whereUse;

	public TopOneAdapter(Context ctx,String whereUse) {
		this.ctx = ctx;
		this.whereUse = whereUse;
	}

	public void setData(List<TopInfo> topInfos) {
		this.topInfos = topInfos;
		this.size = ListUtils.getSize(topInfos);
		isInfiniteLoop = false;
	}

	public int getCount() {
		// Infinite loop
		return isInfiniteLoop ? Integer.MAX_VALUE : ListUtils.getSize(topInfos);
	}

	public TopInfo getData(int position){
		if(topInfos == null || topInfos.size() <= position){
			return null;
		} else {
			return topInfos.get(position);
		}
	}

	/**
	 * get really position
	 *
	 * @param position
	 * @return
	 */
	private int getPosition(int position) {
		return isInfiniteLoop ? position % size : position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {

		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = holder.imageView = new ImageView(ctx);
			holder.imageView.setScaleType(ScaleType.FIT_XY);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		final TopInfo topInfo = topInfos.get(getPosition(position));
		if ("home".equals(whereUse)) {
			AsynImageUtil.promotionFirstListener.setPath(CacheImgUtil.getInstance(ctx).adv_top);
		}else if("storeHome".equals(whereUse)){
			//AsynImageUtil.promotionFirstListener.setPath(CacheImgUtil.getInstance(ctx).store_adv_top);
		}
		imageLoader.displayImage(topInfo.pciture, holder.imageView,
				AsynImageUtil.getImageOptions(R.mipmap.banner),
				AsynImageUtil.promotionFirstListener);

		holder.imageView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				listener.gotoShop(topInfo.picUrl);
			}

		});
		return convertView;
	}

	private static class ViewHolder {

		ImageView imageView;
	}

	/**
	 * @return the isInfiniteLoop
	 */
	public boolean isInfiniteLoop() {
		return isInfiniteLoop;
	}

	/**
	 * @param isInfiniteLoop the isInfiniteLoop to set
	 */
	public void setInfiniteLoop(boolean isInfiniteLoop) {
		this.isInfiniteLoop = isInfiniteLoop;
	}

	public interface OnModelClickListener{

		public void gotoShop(String key);
	}

	public void setOnModelClickListener(OnModelClickListener listener){
		this.listener = listener;
	}
}

