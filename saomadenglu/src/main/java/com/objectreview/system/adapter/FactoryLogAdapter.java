package com.objectreview.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.objectreview.system.R;
import com.objectreview.system.entity.FactoryLogEntity;
import com.objectreview.system.utlis.AsynImageUtil;
import com.objectreview.system.utlis.StringUtil;

import java.util.List;

/**
 * 出厂记录适配器
 */
public class FactoryLogAdapter extends AbsBaseAdapter<FactoryLogEntity> {

    private List<FactoryLogEntity> mData;
    private LayoutInflater inflater;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public FactoryLogAdapter(List<FactoryLogEntity> data, Context context) {
        mData = data;
        inflater = LayoutInflater.from(context.getApplicationContext());
    }

    @Override
    public void setData(List<FactoryLogEntity> infos) {
        mData = infos;
    }

    @Override
    public int getCount() {
        if (null == mData) {
            return 0;
        }
        return mData.size();
    }

    @Override
    public FactoryLogEntity getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_factory_log_info, null);
            holder = new ViewHolder();
            holder.tv_product_name_title = (TextView) convertView.findViewById(R.id.tv_product_name_title);
            holder.tv_entp_name = (TextView) convertView.findViewById(R.id.tv_entp_name);
            holder.tv_pack_code = (TextView) convertView.findViewById(R.id.tv_item_pack_code);
            holder.iv_product_icon = (ImageView) convertView.findViewById(R.id.iv_product_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FactoryLogEntity entity = getItem(position);
        holder.tv_product_name_title.setText(entity.pd_name);
        if (!StringUtil.isEmpty(entity.entp_name)) {
            holder.tv_entp_name.setText("生产企业:" + entity.entp_name);
        }
        if (!StringUtil.isEmpty(entity.pack_code)) {
            holder.tv_pack_code.setText("包装码:" + entity.pack_code);
        }
        if (!StringUtil.isEmpty(entity.pd_pic)) {
            final ViewHolder finalHolder = holder;
            imageLoader.displayImage(entity.pd_pic, holder.iv_product_icon, AsynImageUtil.getImageOptions(), new AsynImageUtil.PromotionNotFirstDisplayListener() {
                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    super.onLoadingFailed(imageUri, view, failReason);
                    finalHolder.iv_product_icon.setImageResource(R.mipmap.image_erro);
                }
            });
        } else {
            holder.iv_product_icon.setImageResource(R.mipmap.image_erro);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tv_product_name_title, tv_entp_name, tv_pack_code;
        ImageView iv_product_icon;
    }

}
