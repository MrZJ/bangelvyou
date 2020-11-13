package com.shishoureport.system.ui.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.shishoureport.system.databinding.ItemMedicalBinding;
import com.shishoureport.system.entity.MedicalEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @date
 **/
public class ProductAdapter extends BaseAdapter {
    private List<MedicalEntity> mData = new ArrayList<>();
    private Context mContext;
    private boolean isReserve;

    public ProductAdapter(Context context, boolean isReserve) {
        mContext = context;
        this.isReserve = isReserve;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ItemMedicalBinding binding = ItemMedicalBinding.inflate(LayoutInflater.from(mContext), parent, false);
        binding.delTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
            }
        });
        final MedicalEntity entity = mData.get(position);
        binding.reagentName.setText("试剂名称：" + (isReserve ? entity.reagentName : entity.name));
        binding.code.setText("规格型号：" + entity.code);
        binding.supplierName.setText("供应商：" + entity.supplierName);
        if (isReserve) {
            binding.kucun.setText("库存数量：" + entity.quantity);
            binding.quantity.setHint("请输入领用数量");
        }
        binding.quantity.setText(entity.inputQuantity);
        binding.quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                entity.inputQuantity = s.toString();
            }
        });
        binding.remark.setText(entity.inputRemark);
        binding.remark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                entity.inputRemark = s.toString();
            }
        });
        return binding.getRoot();
    }

    public void add(MedicalEntity entity) {
        mData.add(0, entity);
        notifyDataSetChanged();
    }

    public void remove(int pos) {
        mData.remove(pos);
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return mData.size() == 0;
    }

    public List<MedicalEntity> getData() {
        return mData;
    }
}
