package com.shenmai.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shenmai.system.R;
import com.shenmai.system.entity.AddressEntity;

import java.util.List;

/**
 * 地址列表界面适配器
 * @author KevinLi
 *
 */
public class AddressAdapter extends AbsBaseAdapter<AddressEntity> {

	private Context ctx;
	private LayoutInflater inflater;
	private List<AddressEntity> classNameList;
	private onClickAddressListener listener;
	public AddressAdapter(Context ctx, List<AddressEntity> classNameList){
		this.ctx = ctx;
		inflater = LayoutInflater.from(ctx);
		this.classNameList = classNameList;
	}

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
			holder = new ViewHolder();
			holder.tv_edit = (TextView)
					convertView.findViewById(R.id.tv_edit);
			holder.tv_delete = (TextView)
					convertView.findViewById(R.id.tv_delete);
			holder.tv_address_name = (TextView)
					convertView.findViewById(R.id.tv_address_name);
			holder.tv_address_phone = (TextView)
					convertView.findViewById(R.id.tv_address_phone);
			holder.tv_address = (TextView)
					convertView.findViewById(R.id.tv_address);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_edit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				listener.editAddress(position);
			}

		});
		holder.tv_delete.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				listener.deleteAddress(position);
			}

		});
		AddressEntity addressEntity = getItem(position);
		holder.tv_address_name.setText(addressEntity.true_name);
		holder.tv_address_phone.setText(addressEntity.mob_phone);
		holder.tv_address.setText(addressEntity.area_info + addressEntity.address);
		return convertView;
	}

	private class ViewHolder{
		TextView tv_address_name, tv_address_phone, tv_address,tv_delete,tv_edit;
	}

	public interface onClickAddressListener{

		void editAddress(int position);
		void deleteAddress(int position);
	}

	public void setOnClickAddress(onClickAddressListener listener){
		this.listener = listener;
	}

}

