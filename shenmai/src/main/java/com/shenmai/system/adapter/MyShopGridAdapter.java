package com.shenmai.system.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shenmai.system.R;
import com.shenmai.system.entity.ShopTypes;
import com.shenmai.system.utlis.ListUtils;

import java.util.ArrayList;

/**
 * 选着图片适配器
 */
public class MyShopGridAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Activity ctx;
	private ArrayList<ShopTypes> mdata;

	public MyShopGridAdapter(Activity context,
							 ArrayList<ShopTypes> data) {
		inflater = LayoutInflater.from(context);
		ctx = context;
		mdata = data;
	}

	public int getCount() {
		return mdata.size();
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_published_grida,
					parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView
					.findViewById(R.id.item_grida_image);
			holder.tv = (TextView) convertView
					.findViewById(R.id.item_grida_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (!ListUtils.isEmpty(mdata)) {
			ShopTypes shopTypes = mdata.get(position);
			if(shopTypes != null){
				holder.image.setImageResource(shopTypes.getImgUrl());
				holder.tv.setText(shopTypes.getContent());
			}
		}
		return convertView;
	}

	private class ViewHolder {
		ImageView image;
		TextView tv;
	}

	@Override
	public boolean isEnabled(int position) {
		return super.isEnabled(position);
	}
}
