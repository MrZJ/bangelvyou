package com.basulvyou.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.basulvyou.system.R;
import com.basulvyou.system.entity.AddressEntity;

import java.util.List;


/**
 * 地址列表界面适配器
 *
 */
public class AddressAdapter extends BaseAdapter {

	private Context ctx;
	private LayoutInflater inflater;
	private List<AddressEntity> classNameList;
	private int width;
	private OnClickAddressListener listener;
	public AddressAdapter(Context ctx, List<AddressEntity> classNameList){
		this.ctx = ctx;
		inflater = LayoutInflater.from(ctx);
		this.classNameList = classNameList;
		/*DisplayMetrics dm = new DisplayMetrics();
		dm = ctx.getResources().getDisplayMetrics();   
		width = dm.widthPixels;//宽度
*/	}
	
	@Override
	public int getCount() {
		if(classNameList == null){
			return 0;
		}
		return classNameList.size();
	}

	@Override
	public AddressEntity getItem(int position) {
		return classNameList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void setData(List<AddressEntity> classNameList){

		this.classNameList = classNameList;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_address, null);
			/*AbsListView.LayoutParams param = new AbsListView.LayoutParams(
	                android.view.ViewGroup.LayoutParams.FILL_PARENT, (int)(width * 1.3) / 8);
	        convertView.setLayoutParams(param);*/
			holder = new ViewHolder();
			holder.rl_delete = (View)
					convertView.findViewById(R.id.rel_address_delete);
			holder.rl_edit = (View)
					convertView.findViewById(R.id.rel_address_edit);
			holder.tv_address_name = (TextView)
					convertView.findViewById(R.id.tv_address_consignee_name);
			holder.tv_address_phone = (TextView)
					convertView.findViewById(R.id.tv_address_consignee_phone);
			holder.tv_address = (TextView)
					convertView.findViewById(R.id.tv_address_consignee_ads);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		AddressEntity addressEntity = getItem(position);
		holder.tv_address_name.setText(addressEntity.true_name);
		holder.tv_address_phone.setText(addressEntity.mob_phone);
		holder.tv_address.setText(addressEntity.area_info + addressEntity.address);
		holder.rl_edit.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				listener.editAddress(position);
			}

		});
		holder.rl_delete.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				listener.deleteAddress(position);
			}

		});
		return convertView;
	}
	
	private class ViewHolder{
		View rl_delete, rl_edit;
		TextView tv_address_name, tv_address_phone, tv_address;
	}
	
	public interface OnClickAddressListener{
		public void editAddress(int position);
		public void deleteAddress(int position);
	}
	
	public void setOnClickAddress(OnClickAddressListener listener){
		this.listener = listener;
	}
	
}

