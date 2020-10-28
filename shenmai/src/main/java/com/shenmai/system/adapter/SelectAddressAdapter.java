package com.shenmai.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shenmai.system.R;
import com.shenmai.system.entity.RegionAddressEntity;
import com.shenmai.system.utlis.DensityUtil;

import java.util.List;


/**
 * 地址适配器
 * @author KevinLi
 *
 */
public class SelectAddressAdapter extends BaseAdapter {

	private Context ctx;
	private List<RegionAddressEntity> mData;
	LayoutInflater inflater;

	public SelectAddressAdapter(Context context) {
		ctx = context;
		inflater = LayoutInflater.from(context);
	}

	public void setData(List<RegionAddressEntity> mData){
		this.mData = mData;
	}

	public int getCount() {

		if (null == mData) {
			return 0;
		}
		return mData.size();
	}

	public RegionAddressEntity getItem(int position) {

		return mData.get(position);
	}

	public long getItemId(int position) {

		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_selete_address, null);
			AbsListView.LayoutParams param = new AbsListView.LayoutParams(
	                ViewGroup.LayoutParams.FILL_PARENT, DensityUtil.dip2px(ctx, 60));
	        convertView.setLayoutParams(param);
			holder = new ViewHolder();
			holder.name = (TextView) convertView
					.findViewById(R.id.name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		RegionAddressEntity city = getItem(position);
		holder.name.setText(city.area_name);

		return convertView;
	}

	private class ViewHolder {
		TextView name;

	}
}

