package com.basulvyou.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.basulvyou.system.R;
import com.basulvyou.system.entity.CouponEntity;

import java.util.List;

/**
 * 优惠券适配器
 *
 */
public class CouponAdapter extends AbsBaseAdapter<CouponEntity> {

	private List<CouponEntity> mData;
	private LayoutInflater inflater;

	public CouponAdapter(List<CouponEntity> branchInfos, Context context) {
		mData = branchInfos;
		inflater = LayoutInflater.from(context);
	}

	public int getCount() {

		if (null == mData) {
			return 0;
		}
		return mData.size();
	}

	public CouponEntity getItem(int position) {

		return mData.get(position);
	}

	public long getItemId(int position) {

		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_coupon, null);
			holder = new ViewHolder();
			holder.tv_coupon_price = (TextView) convertView
					.findViewById(R.id.tv_coupon_quota);
			holder.tv_coupon_name = (TextView) convertView
					.findViewById(R.id.tv_coupon_name);
			holder.tv_coupon_limit = (TextView) convertView
					.findViewById(R.id.tv_coupon_limit);
			holder.tv_coupon_rule = (TextView) convertView
					.findViewById(R.id.tv_coupon_rule);
			holder.tv_coupon_date = (TextView) convertView
					.findViewById(R.id.tv_coupon_date);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CouponEntity entity = getItem(position);
		holder.tv_coupon_price.setText(entity.voucher_price);
		holder.tv_coupon_name.setText(entity.voucher_title);
		holder.tv_coupon_limit.setText("订单满"+entity.voucher_limit+"元使用");
		holder.tv_coupon_rule.setText(entity.store_name);
		holder.tv_coupon_date.setText(entity.voucher_start_date+"至"+entity.voucher_end_date);
		return convertView;
	}

	private class ViewHolder {
		TextView tv_coupon_date, tv_coupon_price, tv_coupon_limit,tv_coupon_name,tv_coupon_rule;
	}

	public void setData(List<CouponEntity> entity) {
		mData = entity;
	}
	
}

